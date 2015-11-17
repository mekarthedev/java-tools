package me.mekarthedev.tools.patch;

public class MakePatch {

    public interface CalculatorSelector<Data> {
        Patch<Data> with(PatchCalculator<Data> calculantor);
    }

    public interface TargetDataSelector<Data> {
        CalculatorSelector<Data> to(Data updated);
    }

    public static <Data> TargetDataSelector<Data> from(Data original) {
        return updated -> calculator -> calculator.diff(original, updated);
    }
}
