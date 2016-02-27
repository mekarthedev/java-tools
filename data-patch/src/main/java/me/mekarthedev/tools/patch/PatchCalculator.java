package me.mekarthedev.tools.patch;

/**
 * Calculates change set between old and updated object states.
 * @param <Data> The type of data object.
 */
public interface PatchCalculator<Data> {
    Patch<Data> diff(Data original, Data updated);
}
