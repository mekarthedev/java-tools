package me.mekarthedev.tools.patch;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MakePatchTest {

    @Test
    public void with_onCustomCalculator_calculatesUsingGivenCalculator() throws Exception {
        Object stubOriginal = new Object();
        Object stubUpdated = new Object();

        Patch<Object> stubPatch = new Patch<>(stubUpdated, new ArrayList<>());
        PatchCalculator<Object> stubCalculator = mock(PatchCalculator.class);
        when(stubCalculator.diff(any(), any())).thenReturn(stubPatch);

        Patch<Object> resultPatch = MakePatch.from(stubOriginal).to(stubUpdated).with(stubCalculator);

        verify(stubCalculator).diff(stubOriginal, stubUpdated);
        assertThat(resultPatch, is(stubPatch));
    }
}