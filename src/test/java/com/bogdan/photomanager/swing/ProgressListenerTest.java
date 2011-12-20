package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.swing.ProgressListener;
import org.junit.Test;
import javax.swing.*;
import static org.junit.Assert.assertEquals;

public class ProgressListenerTest {

    @Test
    public void createTest() {
        JProgressBar progressBar = new JProgressBar();
        ProgressListener progressListener =
                new ProgressListener(progressBar);
        assertEquals(0, progressBar.getValue());
    }

}
