package me.mekarthedev.tools.patch;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Represents a change set between two object states.
 * @param <Data> The type of data that is being patched.
 */
public class Patch<Data> {
    private final Data _data;
    private final Collection<Field> _changedFields;

   /**
    * @param data The new/updated values for the corresponding updated fields.
    * @param changedFields Which fields are updated.
    */
    public Patch(Data data, Collection<Field> changedFields) {
        _data = data;
        _changedFields = changedFields;
    }

    public Data data() {
        return _data;
    }

    public Collection<Field> changedFields() {
        return _changedFields;
    }
}
