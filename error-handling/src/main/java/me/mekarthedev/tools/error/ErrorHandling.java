package me.mekarthedev.tools.error;

import java.util.concurrent.Callable;

public class ErrorHandling {

    public interface VoidCallable {
        void call() throws Exception;
    }

    /**
     * Wraps the given call in assertion that it will not throw.
     * <p>
     * Implements typical try-catch-rethrow technique for dealing with checked exceptions
     * in situations where they are expected not to be thrown. The given callable is called and the result is returned.
     * If the action throws, the exception is rethrown as a RuntimeException.
     *
     * @param action The callable to be called to get the result.
     * @param <Result> Type of the resulting value.
     * @return Result of action.call().
     */
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
