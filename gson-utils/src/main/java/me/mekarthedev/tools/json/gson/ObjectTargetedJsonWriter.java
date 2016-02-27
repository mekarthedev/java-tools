package me.mekarthedev.tools.json.gson;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Allows serializing json's root object with different writer options than other parts of the json.
 * The delegate writer options are preserved and overridden only during the root object serialization.
 */
public class ObjectTargetedJsonWriter extends JsonWriterDecorator {

    private int _depth = 0;

    private boolean _serializeNulls;
    private boolean _originalSerializeNulls;

    public ObjectTargetedJsonWriter(JsonWriter delegate) {
        super(delegate);
        setSerializeNulls(delegate.getSerializeNulls());
        setHtmlSafe(delegate.isHtmlSafe());
        setLenient(delegate.isLenient());
    }

   /**
    * Sets serializeNulls option value that should be used when serializing the root object.
    * @param serializeNulls Option value to be used with the root.
    */
    public void setTargetedSerializeNulls(boolean serializeNulls) {
        _serializeNulls = serializeNulls;
    }

    @Override
    public JsonWriter beginObject() throws IOException {
        _depth++;
        switchWrittenObject();

        super.beginObject();
        return this;
    }

    @Override
    public JsonWriter endObject() throws IOException {
        super.endObject();

        _depth--;
        switchWrittenObject();

        return this;
    }

    private void switchWrittenObject() {
        boolean beganWritingTargetObject = (_depth == 1);
        if (beganWritingTargetObject) {
            _originalSerializeNulls = delegate().getSerializeNulls();
            delegate().setSerializeNulls(_serializeNulls);
            setSerializeNulls(_serializeNulls);
        }

        boolean beganWritingInnerObject = (_depth == 2);
        if (beganWritingInnerObject) {
            delegate().setSerializeNulls(_originalSerializeNulls);
            setSerializeNulls(_originalSerializeNulls);
        }
    }
}
