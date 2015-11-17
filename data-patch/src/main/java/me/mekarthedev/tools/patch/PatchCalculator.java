package me.mekarthedev.tools.patch;

public interface PatchCalculator<Data> {
    Patch<Data> diff(Data original, Data updated);
}
