package me.mekarthedev.tools.patch.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import me.mekarthedev.tools.patch.Patch;
import me.mekarthedev.tools.patch.MakePatch;
import me.mekarthedev.tools.patch.ReflectivePatchCalculator;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PatchTypeAdapterTest {

    private static class TestData {
        public String name1 = "value1";
        public String name2 = "value2";
        @SerializedName("json-name-alias")
        public String aliasedName = "namedValue";
        public String nullName = null;
    }

    private Gson gson() {
        TypeAdapterFactory factory = new PatchTypeAdapterFactory();
        return new GsonBuilder().registerTypeAdapterFactory(factory).create();
    }

    @Test
    public void write_onPatchWithUnchangedFields_writesOnlyChangedFieldsToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.name1 = "updated value";
        updated.aliasedName = null;

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = gson().toJsonTree(patch, new TypeToken<Patch<TestData>>(){}.getType()).getAsJsonObject();

        assertThat(json.get("name1").getAsString(), is("updated value"));
        assertTrue(json.get("json-name-alias").isJsonNull());
    }

    @Test
    public void write_onPatchWithNull_writesNullToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.name2 = null;

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = gson().toJsonTree(patch, new TypeToken<Patch<TestData>>(){}.getType()).getAsJsonObject();

        assertTrue(json.get("name2").isJsonNull());
        assertThat(json.has("nullName"), is(false));
    }

    @Test
    public void write_onAliasedFieldName_writesAliasToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.aliasedName = "updated-aliased-value";

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = gson().toJsonTree(patch, new TypeToken<Patch<TestData>>(){}.getType()).getAsJsonObject();

        assertThat(json.get("json-name-alias").getAsString(), is("updated-aliased-value"));
    }
}