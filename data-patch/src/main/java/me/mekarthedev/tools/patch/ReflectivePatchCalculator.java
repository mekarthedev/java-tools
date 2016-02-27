package me.mekarthedev.tools.patch;

import static me.mekarthedev.tools.error.ErrorHandling.noThrow;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Uses reflection to calculate change set by comparing one by one the old and the updated object fields.
 * @param <Data> The type of data that is updated.
 */
public class ReflectivePatchCalculator<Data> implements PatchCalculator<Data> {
    @Override
    public Patch<Data> diff(Data original, Data updated) {
        List<Field> updatedFields = new LinkedList<>();
        for (Field field : original.getClass().getDeclaredFields()) {
            boolean wasAccessible = field.isAccessible();
            field.setAccessible(true);

            Object originalValue = noThrow(() -> field.get(original));
            Object updatedValue = noThrow(() -> field.get(updated));
            boolean valueChanged = originalValue != updatedValue
                    && (originalValue == null || updatedValue == null || !originalValue.equals(updatedValue));

            if (valueChanged) {
                updatedFields.add(field);
            }

            field.setAccessible(wasAccessible);
        }

        return new Patch<>(updated, updatedFields);
    }
}
