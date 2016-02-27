package me.mekarthedev.tools.json.gson;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * Allows watching for events regarding only the root object being read with this reader.
 */
public class ObjectWatchingJsonReader extends JsonReaderDecorator {

    private int _depth = 0;

    public ObjectWatchingJsonReader(JsonReader delegate) {
        super(delegate);
    }

   /**
    * Called when next name of the root object is being read.
    * @param foundName The next root's name being read.
    */
    protected void onNextName(String foundName) {
        // Default implementation.
    }

    @Override
    public void beginObject() throws IOException {
        super.beginObject();
        _depth++;
    }

    @Override
    public String nextName() throws IOException {
        String name = super.nextName();
        boolean isReadingWatchedObjectFields = _depth == 1;
        if (isReadingWatchedObjectFields) {
            onNextName(name);
        }
        return name;
    }

    @Override
    public void endObject() throws IOException {
        _depth--;
        super.endObject();
    }
}
