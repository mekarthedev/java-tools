package me.mekarthedev.tools.patch.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.mekarthedev.tools.json.gson.ObjectTargetedJsonWriter;
import me.mekarthedev.tools.json.gson.ObjectWatchingJsonReader;
import me.mekarthedev.tools.patch.Patch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A GSON implementation of Patch serializer to represent a difference between objects in json.
 * <p>
 *    The JSON object is interpreted as change set. Presence of a JSON key means that
 *    the corresponding data field is changed and the updated value is the corresponding JSON value.
 * <p>
 *    Useful with rfc5789#section-2 compliant RESTful PATCH request.
 * @param <Data> The type of data that is being patched.
 * @see PatchTypeAdapterFactory
 */
public class PatchTypeAdapter<Data> extends TypeAdapter<Patch<Data>> {
    private final TypeAdapter<Data> _dataAdapter;
    private final Gson _gson;

    public PatchTypeAdapter(TypeAdapter<Data> dataAdapter, Gson gson) {
        _dataAdapter = dataAdapter;
        _gson = gson;
    }

    @Override
    public void write(JsonWriter out, Patch<Data> value) throws IOException {
        JsonTreeWriter treeWriter = new JsonTreeWriter();
        treeWriter.setSerializeNulls(true);
        _dataAdapter.write(treeWriter, value.data());
        JsonObject tree = treeWriter.get().getAsJsonObject();

        Set<String> presentJsonProperties = new HashSet<>();
        for (Field field : value.changedFields()) {
            SerializedName nameSpecifier = field.getAnnotation(SerializedName.class);
            String jsonPropertyName = (nameSpecifier != null ? nameSpecifier.value() : field.getName());
            presentJsonProperties.add(jsonPropertyName);
        }

        for (Map.Entry<String, JsonElement> entry : new ArrayList<>(tree.entrySet())) {
            if (!presentJsonProperties.contains(entry.getKey())) {
                tree.remove(entry.getKey());
            }
        }

        // If a data field changed to null the null should be serialized.
        // This is the whole point of this adapter.
        ObjectTargetedJsonWriter patchWriter = new ObjectTargetedJsonWriter(out);
        patchWriter.setTargetedSerializeNulls(true);
        _gson.getAdapter(JsonObject.class).write(patchWriter, tree);
    }

    @Override
    public Patch<Data> read(JsonReader in) throws IOException {
        final Set<String> receivedJsonProperties = new HashSet<>();
        JsonReader watchingReader = new ObjectWatchingJsonReader(in) {
            @Override
            protected void onNextName(String foundName) {
                receivedJsonProperties.add(foundName);
            }
        };

        Data data = _dataAdapter.read(watchingReader);

        List<Field> changedFields = new ArrayList<>(receivedJsonProperties.size());
        for (Field field : data.getClass().getDeclaredFields()) {
            SerializedName nameSpecifier = field.getAnnotation(SerializedName.class);
            String jsonPropertyName = (nameSpecifier != null ? nameSpecifier.value() : field.getName());
            if (receivedJsonProperties.contains(jsonPropertyName)) {
                changedFields.add(field);
            }
        }

        return new Patch<>(data, changedFields);
    }
}
