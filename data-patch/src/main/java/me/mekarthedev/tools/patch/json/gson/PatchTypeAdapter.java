package me.mekarthedev.tools.patch.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.mekarthedev.tools.json.gson.JsonReaderDecorator;
import me.mekarthedev.tools.patch.Patch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        boolean serializeNulls = out.getSerializeNulls();
        out.setSerializeNulls(true);
        _gson.getAdapter(JsonObject.class).write(out, tree);
        out.setSerializeNulls(serializeNulls);
    }

    @Override
    public Patch<Data> read(JsonReader in) throws IOException {
        final Set<String> receivedJsonProperties = new HashSet<>();
        JsonReader watchingReader = new JsonReaderDecorator(in) {
            @Override
            public String nextName() throws IOException {
                String name = super.nextName();
                receivedJsonProperties.add(name);
                return name;
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
