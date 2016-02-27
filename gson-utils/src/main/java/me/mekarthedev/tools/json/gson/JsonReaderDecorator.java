package me.mekarthedev.tools.json.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

/**
 * A simple decorator of a JsonReader instance. Allows hooking into the parsing process.
 * Just pass an overridden decorated reader to the target type adapter.
 */
public class JsonReaderDecorator extends JsonReader {
    private final JsonReader _delegate;

    public JsonReaderDecorator(JsonReader delegate) {
        super(new StringReader("null"));
        _delegate = delegate;
    }

    @Override
    public void beginArray() throws IOException {
        _delegate.beginArray();
    }

    @Override
    public void endArray() throws IOException {
        _delegate.endArray();
    }

    @Override
    public void beginObject() throws IOException {
        _delegate.beginObject();
    }

    @Override
    public void endObject() throws IOException {
        _delegate.endObject();
    }

    @Override
    public boolean hasNext() throws IOException {
        return _delegate.hasNext();
    }

    @Override
    public JsonToken peek() throws IOException {
        return _delegate.peek();
    }

    @Override
    public String nextName() throws IOException {
        return _delegate.nextName();
    }

    @Override
    public String nextString() throws IOException {
        return _delegate.nextString();
    }

    @Override
    public boolean nextBoolean() throws IOException {
        return _delegate.nextBoolean();
    }

    @Override
    public void nextNull() throws IOException {
        _delegate.nextNull();
    }

    @Override
    public double nextDouble() throws IOException {
        return _delegate.nextDouble();
    }

    @Override
    public long nextLong() throws IOException {
        return _delegate.nextLong();
    }

    @Override
    public int nextInt() throws IOException {
        return _delegate.nextInt();
    }

    @Override
    public void close() throws IOException {
        _delegate.close();
    }

    @Override
    public void skipValue() throws IOException {
        _delegate.skipValue();
    }

    @Override
    public String toString() {
        return _delegate.toString();
    }

    @Override
    public String getPath() {
        return _delegate.getPath();
    }
}
