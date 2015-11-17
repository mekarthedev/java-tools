package me.mekarthedev.tools.error;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ErrorHandlingTest {

    private class TestCheckedException extends Exception {}

    private void throwChecked() throws TestCheckedException {
        throw new TestCheckedException();
    }

    @Test
    public void noThrow_onCheckedException_rethrowsAsUnchecked() throws Exception {
        Exception thrown = null;
        try {
            ErrorHandling.noThrow(() -> throwChecked());

        } catch (Exception e) {
            thrown = e;
        }

        assertThat(thrown, instanceOf(RuntimeException.class));
        assertThat(thrown.getCause(), instanceOf(TestCheckedException.class));
    }

    @Test
    public void noThrow_onReturnedValue_returnsThatValue() throws Exception {
        Object original = new Object();
        Object returned = ErrorHandling.noThrow(() -> original);

        assertThat(returned, is(original));
    }
}