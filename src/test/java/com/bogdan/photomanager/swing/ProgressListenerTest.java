package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.swing.ProgressListener;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.beans.PropertyChangeEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgressListenerTest {

    private JProgressBar progressBar;
    private ProgressListener progressListener;
    private PropertyChangeEvent propertyChangeEvent;

    @Before
    public void setUp() {
        progressBar = new JProgressBar();
        progressListener = new ProgressListener(progressBar);

        propertyChangeEvent = mock(PropertyChangeEvent.class);
        when(propertyChangeEvent.getPropertyName()).thenReturn("progress");
        when(propertyChangeEvent.getNewValue()).thenReturn(20);
    }

    @Test
    public void testConstructor() {
        assertEquals("Progress bar should have no value when created",
                0, progressBar.getValue());
    }

    @Test
    public void testPropertyChange() {
        progressListener.propertyChange(propertyChangeEvent);

        assertFalse("Progress bar should not be indeterminate at this point",
                progressBar.isIndeterminate());
        assertEquals("Progress bar value was not set correctly",
                20, progressBar.getValue());
    }
}
