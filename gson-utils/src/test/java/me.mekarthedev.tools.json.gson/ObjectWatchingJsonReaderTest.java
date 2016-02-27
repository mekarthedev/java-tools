package me.mekarthedev.tools.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ObjectWatchingJsonReaderTest {

    class TestData {
        public Object v1 = "v-1";
        public Object v2 = "v-2";
        public InnerTestData inner = null;
    }

    class InnerTestData {
        public Object inner1 = "inner-1";
        public Object inner2 = "inner-2";
    }

    private Gson gson() {
        return new GsonBuilder().create();
    }

    @Test
    public void onNextName_onObjectWithInnerObject_isCalledOnlyForPropertiesOfOuterObject() throws Exception {
        TestData data = new TestData();
        data.inner = new InnerTestData();
        JsonElement json = gson().toJsonTree(data);

        final List<String> readProperties = new LinkedList<>();
        JsonReader reader = new ObjectWatchingJsonReader(new JsonTreeReader(json)) {
            @Override
            protected void onNextName(String foundName) {
                readProperties.add(foundName);
            }
        };
        gson().getAdapter(TestData.class).read(reader);

        assertThat("v1", isIn(readProperties));
        assertThat("v2", isIn(readProperties));
        assertThat("inner", isIn(readProperties));

        assertThat("inner1", not(isIn(readProperties)));
        assertThat("inner2", not(isIn(readProperties)));
    }
}