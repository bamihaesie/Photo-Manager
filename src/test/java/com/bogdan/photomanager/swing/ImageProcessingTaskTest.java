package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.model.RenameCommand;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.Document;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ImageProcessingTaskTest {

    ImageProcessingTask imageProcessingTask;
    JTextArea textArea;

    @Before
    public void setUp() {
        RenameCommand renameCommand = new RenameCommand();
        textArea = mock(JTextArea.class);
        Document document = mock(Document.class);
        when(document.getLength()).thenReturn(1);
        when(textArea.getDocument()).thenReturn(document);
        PhotoManager photoManager = mock(PhotoManager.class);
        imageProcessingTask = new ImageProcessingTask(renameCommand, textArea, photoManager);
    }

    @Test
    public void testDoneCancelled() {
        imageProcessingTask.cancel(false);
        imageProcessingTask.done();
        verify(textArea).append("Cancelled!\n");
        assertEquals(0, imageProcessingTask.getProgress());
    }

    @Test
    public void testDoneNotCancelled() {
        imageProcessingTask.done();
        verify(textArea).append("Done!\n");
        assertEquals(100, imageProcessingTask.getProgress());
    }

    @Test
    public void testProcess() {
        List<String> chunks = new ArrayList<String>();
        chunks.add("test1");
        chunks.add("test2");
        imageProcessingTask.process(chunks);
        verify(textArea).append("test1\n");
        verify(textArea).append("test2\n");
    }
}
