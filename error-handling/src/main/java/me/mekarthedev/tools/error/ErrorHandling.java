package me.mekarthedev.tools.error;

import java.util.concurrent.Callable;

public class ErrorHandling {

    public interface VoidCallable {
        void call() throws Exception;
    }

    public static <Result> Result noThrow(Callable<Result> action) {
        try {
            return action.call();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void noThrow(VoidCallable action) {
        try {
            action.call();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
