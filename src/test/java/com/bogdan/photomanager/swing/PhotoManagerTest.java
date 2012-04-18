package com.bogdan.photomanager.swing;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.Assert.*;

public class PhotoManagerTest {

    private JFrame frame;

    @Before
    public void setUp() {
        frame = PhotoManager.createFrame();
    }

    @Test
    public void testCreateFrame() {
        assertNotNull("Frame should not be null!", frame);
        assertEquals("Frame title not set correctly!",
                "Photo Manager", frame.getTitle());
        assertEquals("Frame default close operation not set!",
                JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
    }
    
    @Test
    public void testCenterFrameToScreen() {
        PhotoManager.screenSize = new Dimension(1024, 768);
        PhotoManager.centerFrameToScreen(frame);
        assertEquals("Frame location is wrong!",
                new Point(212, 204), frame.getLocation());
        assertEquals("Frame size is wrong!",
                new Dimension(600, 360), frame.getSize());
    }
    
    @Test
    public void testSetFrameProperties() {
        PhotoManager.setFrameProperties(frame);
        assertFalse(frame.isResizable());
        assertTrue(frame.isVisible());
    }

}
