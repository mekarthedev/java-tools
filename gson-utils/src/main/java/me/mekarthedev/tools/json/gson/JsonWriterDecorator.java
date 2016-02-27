package me.mekarthedev.tools.json.gson;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * A simple decorator of a JsonWriter instance. Allows hooking into the serialization process.
 * Just pass an overridden decorated writer to the target type adapter.
 */
public class JsonWriterDecorator extends JsonWriter {
    private final JsonWriter _delegate;

    public JsonWriterDecorator(JsonWriter delegate) {
        super(new StringWriter());
        _delegate = delegate;
    }

    protected final JsonWriter delegate() {
        return _delegate;
    }

    @Override
    public boolean isLenient() {
        return _delegate.isLenient();
    }

    @Override
    public JsonWriter beginArray() throws IOException {
        return _delegate.beginArray();
    }

    @Override
    public JsonWriter endArray() throws IOException {
        return _delegate.endArray();
    }

    @Override
    public JsonWriter beginObject() throws IOException {
        return _delegate.beginObject();
    }

    @Override
    public JsonWriter endObject() throws IOException {
        return _delegate.endObject();
    }

    @Override
    public JsonWriter name(String name) throws IOException {
        return _delegate.name(name);
    }

    @Override
    public JsonWriter value(String value) throws IOException {
        return _delegate.value(value);
    }

    @Override
    public JsonWriter nullValue() throws IOException {
        return _delegate.nullValue();
    }

    @Override
    public JsonWriter value(boolean value) throws IOException {
        return _delegate.value(value);
    }

    @Override
    public JsonWriter value(double value) throws IOException {
        return _delegate.value(value);
    }

    @Override
    public JsonWriter value(long value) throws IOException {
        return _delegate.value(value);
    }

    @Override
    public JsonWriter value(Number value) throws IOException {
        return _delegate.value(value);
    }

    @Override
    public void flush() throws IOException {
        _delegate.flush();
    }

    @Override
    public void close() throws IOException {
        _delegate.close();
    }
}
