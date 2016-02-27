package me.mekarthedev.tools.patch.json.gson;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import me.mekarthedev.tools.patch.Patch;
import me.mekarthedev.tools.patch.MakePatch;
import me.mekarthedev.tools.patch.ReflectivePatchCalculator;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PatchTypeAdapterTest {
    class TestData {
        private String name1 = "value1";
        private String name2 = "value2";
        @SerializedName("json-name-alias")
        private String aliasedName = "namedValue";
        private String nullName = null;
        private InnerTestData innerData = null;

        public String name1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String name2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

        public String aliasedName() {
            return aliasedName;
        }

        public void setAliasedName(String aliasedName) {
            this.aliasedName = aliasedName;
        }

        public String nullName() {
            return nullName;
        }

        public void setNullName(String nullName) {
            this.nullName = nullName;
        }

        public InnerTestData innerData() {
            return innerData;
        }

        public void setInnerData(InnerTestData innerData) {
            this.innerData = innerData;
        }
    }

    class InnerTestData {
        private String name1 = "inner-value-1";
        private String name2 = "inner-value-2";
        @SerializedName("json-name-alias")
        private String aliasedName = "inner-named-value";
        private String nullName = null;
    }

    private Gson gson() {
        TypeAdapterFactory factory = new PatchTypeAdapterFactory();
        return new GsonBuilder().registerTypeAdapterFactory(factory).create();
    }

    private Patch<TestData> fromJson(String json) {
        return gson().<Patch<TestData>>fromJson(json, new TypeToken<Patch<TestData>>(){}.getType());
    }

    private JsonObject toJson(Patch<TestData> patch) {
        return gson().toJsonTree(patch, new TypeToken<Patch<TestData>>(){}.getType()).getAsJsonObject();
    }

    @Test
    public void write_onPatchWithUnchangedFields_writesOnlyChangedFieldsToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.setName1("updated value");
        updated.setAliasedName(null);

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = toJson(patch);

        assertThat(json.get("name1").getAsString(), is("updated value"));
        assertTrue(json.get("json-name-alias").isJsonNull());
    }

    @Test
    public void write_onPatchWithNull_writesNullToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.setName2(null);

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = toJson(patch);

        assertTrue(json.get("name2").isJsonNull());
        assertThat(json.has("nullName"), is(false));
    }

    @Test
    public void write_onAliasedFieldName_writesAliasToJson() throws Exception {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.setAliasedName("updated-aliased-value");

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = toJson(patch);

        assertThat(json.get("json-name-alias").getAsString(), is("updated-aliased-value"));
    }

    @Test
    public void write_onPatchWithInnerObject_fullyWritesInnerObject() {
        TestData original = new TestData();
        TestData updated = new TestData();
        updated.setInnerData(new InnerTestData());

        Patch<TestData> patch = MakePatch.from(original).to(updated).with(new ReflectivePatchCalculator<>());
        JsonObject json = toJson(patch);

        assertTrue(json.has("innerData"));
        assertTrue(json.get("innerData").isJsonObject());

        JsonObject innerJson = json.get("innerData").getAsJsonObject();
        Collection<String[]> fields = Collections2.transform(
                innerJson.entrySet(),
                entry -> new String[] { entry.getKey(), entry.getValue().getAsString() }
        );
        assertThat(fields, containsInAnyOrder(
                new String[] { "json-name-alias", "inner-named-value" },
                new String[] { "name1", "inner-value-1" },
                new String[] { "name2", "inner-value-2" }
        ));
    }

    @Test
    public void read_onJsonWithFields_parsesPatchWithOnlyTheseFieldsChanged() {
        String json = "{" +
                "\"name1\": \"json-value1\"," +
                "\"json-name-alias\": \"json-value3\"," +
                "\"nullName\": null" +
                "}";

        Patch<TestData> parsedPatch = fromJson(json);

        Collection<String> changedFields = Collections2.transform(parsedPatch.changedFields(), Field::getName);
        assertThat(changedFields, containsInAnyOrder("name1", "aliasedName", "nullName"));
        assertThat(parsedPatch.data().name1(), is("json-value1"));
        assertThat(parsedPatch.data().aliasedName(), is("json-value3"));
        assertThat(parsedPatch.data().nullName(), is(nullValue()));
    }

    @Test
    public void read_onJsonWithNulls_parsesPatchWithNullsMarkedAsChanged() {
        String json = "{" +
                "\"name1\": null," +
                "\"json-name-alias\": null" +
                "}";

        Patch<TestData> parsedPatch = fromJson(json);

        Collection<String> changedFields = Collections2.transform(parsedPatch.changedFields(), Field::getName);
        assertThat(changedFields, containsInAnyOrder("name1", "aliasedName"));
        assertThat(parsedPatch.data().name1(), is(nullValue()));
        assertThat(parsedPatch.data().aliasedName(), is(nullValue()));
    }

    @Test
    public void read_onJsonWithSimilarInnerObject_correctlyParsesOuterPatch() {
        String json = "{" +
                "\"innerData\": {" +
                "\"name1\": \"json-value1\"," +
                "\"name2\": \"json-value2\"," +
                "\"json-name-alias\": \"json-value3\"," +
                "\"nullName\": null" +
                "} }";

        Patch<TestData> parsedPatch = fromJson(json);

        Collection<String> changedFields = Collections2.transform(parsedPatch.changedFields(), Field::getName);
        assertThat(changedFields, containsInAnyOrder("innerData"));
    }
}
