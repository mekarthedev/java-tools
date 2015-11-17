package me.mekarthedev.tools.patch;

import com.google.common.collect.Collections2;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ReflectivePatchCalculatorTest {

    private static class TestData {
        public final Object v1;
        public final Object v2;
        public final Object v3;
        public final Object v4;
        public final Object v5;

        public TestData(Object v1, Object v2, Object v3, Object v4, Object v5) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
        }
    }

    @Test
    public void diff_forDifferentiatingObjects_marksOnlyDifferentiatingFieldsAsChanged() throws Exception {
        TestData original = new TestData(42, "1989", "2015", null, null);
        TestData updated = new TestData("1234", "1989", null, "1812", null);
        Patch<TestData> patch = new ReflectivePatchCalculator<TestData>().diff(original, updated);

        Collection<String> changedFields = Collections2.transform(patch.changedFields(), Field::getName);
        assertThat(changedFields, contains("v1", "v3", "v4"));
    }

    @Test
    public void diff_forDifferentiatingObjects_returnsPatchDataContainingUpdatedValuesInChangedFields() throws Exception {
        TestData original = new TestData(42, "1989", null, 1812, "1900");
        TestData updated = new TestData(23, "1989", "2015", 1812, null);
        Patch<TestData> patch = new ReflectivePatchCalculator<TestData>().diff(original, updated);

        for (Field changedField : patch.changedFields()) {
            assertThat(changedField.get(patch.data()), is(changedField.get(updated)));
        }
    }
}