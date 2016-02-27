package me.mekarthedev.tools.patch.json.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import me.mekarthedev.tools.patch.Patch;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Use this to plug Patch serializer into your Gson instance.
 * @see PatchTypeAdapter
 */
public class PatchTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != Patch.class) {
            return null;
        }

        Type dataType = ((ParameterizedType) type.getType()).getActualTypeArguments()[0];
        TypeAdapter dataAdapter = gson.getAdapter(TypeToken.get(dataType));
        return new PatchTypeAdapter(dataAdapter, gson);
    }

}
