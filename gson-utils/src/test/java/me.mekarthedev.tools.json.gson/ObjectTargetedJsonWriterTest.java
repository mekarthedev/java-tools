package me.mekarthedev.tools.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ObjectTargetedJsonWriterTest {

    class TestData {
        public Object v1 = "v-1";
        public Object v2 = "v-2";
        public TestData inner = null;
        public Object v3 = "v-3";
    }

    private Gson gson() {
        return new GsonBuilder().create();
    }

    @Test
    public void writer_onObjectWithInnerObject_serializesNullsOnlyForOuterObject() throws IOException {
        TestData data = new TestData();
        data.v2 = null;
        data.inner = new TestData();
        data.inner.v2 = null;
        data.inner.v3 = null;
        data.v3 = null;

        JsonTreeWriter commonWriter = new JsonTreeWriter();
        commonWriter.setSerializeNulls(false);
        ObjectTargetedJsonWriter specificWriter = new ObjectTargetedJsonWriter(commonWriter);
        specificWriter.setTargetedSerializeNulls(true);
        gson().getAdapter(TestData.class).write(specificWriter, data);

        JsonObject json = commonWriter.get().getAsJsonObject();
        assertTrue(json.has("v2"));
        assertTrue(json.has("inner"));
        assertThat(json.get("inner").getAsJsonObject().has("v2"), is(false));
        assertThat(json.get("inner").getAsJsonObject().has("v3"), is(false));
        assertTrue(json.has("v3"));
    }
}
