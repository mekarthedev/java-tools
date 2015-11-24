package me.mekarthedev.tools.json.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

/**
 * A simple decorator of a JsonReader instance. Allows hooking into the parsing process
 * by passing an overriden decorated reader to the target adapter.
 */
public class JsonReaderDecorator extends JsonReader {
    private final JsonReader _reader;

    public JsonReaderDecorator(JsonReader reader) {
        super(null);

        _reader = reader;
    }

    @Override
    public void beginArray() throws IOException {
        _reader.beginArray();
    }

    @Override
    public void endArray() throws IOException {
        _reader.endArray();
    }

    @Override
    public void beginObject() throws IOException {
        _reader.beginObject();
    }

    @Override
    public void endObject() throws IOException {
        _reader.endObject();
    }

    @Override
    public boolean hasNext() throws IOException {
        return _reader.hasNext();
    }

    @Override
    public JsonToken peek() throws IOException {
        return _reader.peek();
    }

    @Override
    public String nextName() throws IOException {
        return _reader.nextName();
    }

    @Override
    public String nextString() throws IOException {
        return _reader.nextString();
    }

    @Override
    public boolean nextBoolean() throws IOException {
        return _reader.nextBoolean();
    }

    @Override
    public void nextNull() throws IOException {
        _reader.nextNull();
    }

    @Override
    public double nextDouble() throws IOException {
        return _reader.nextDouble();
    }

    @Override
    public long nextLong() throws IOException {
        return _reader.nextLong();
    }

    @Override
    public int nextInt() throws IOException {
        return _reader.nextInt();
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

    @Override
    public void skipValue() throws IOException {
        _reader.skipValue();
    }

    @Override
    public String toString() {
        return _reader.toString();
    }

    @Override
    public String getPath() {
        return _reader.getPath();
    }
}
