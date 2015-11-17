package me.mekarthedev.tools.patch;

import java.lang.reflect.Field;
import java.util.Collection;

public class Patch<Data> {
    private final Data _data;
    private final Collection<Field> _changedFields;

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
