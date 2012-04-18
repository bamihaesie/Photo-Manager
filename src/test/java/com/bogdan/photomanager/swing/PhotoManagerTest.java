package com.bogdan.photomanager.swing;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import javax.swing.*;

import java.awt.*;

import static org.junit.Assert.*;

public class PhotoManagerTest {

    private JFrame frame;
    private PhotoManager photoManager;

    @Before
    public void setUp() {
        frame = PhotoManager.createFrame();
        photoManager = new PhotoManager();
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

    @Test
    public void testComponents() {
        List<JComponent> components = photoManager.getComponentList();
        assertNotNull("Components should not be null!", components);
        assertEquals("Wrong number of components returned!",
                10, components.size());
    }
    
    @Test
    public void testEnableInteraction() {
        photoManager.enableInteraction();
        List<JComponent> components = photoManager.getComponentList();
        for (JComponent component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if ("CANCEL".equals(button.getText())) {
                    assertFalse("Component " + component.getName() + " should be disabled!",
                            component.isEnabled());
                    continue;
                }
            }
            assertTrue("Component " + component.getName() + " should be enabled!",
                    component.isEnabled());
        }
    }

    @Test
    public void testDisableInteraction() {
        photoManager.disableInteraction();
        List<JComponent> components = photoManager.getComponentList();
        for (JComponent component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if ("CANCEL".equals(button.getText())) {
                    assertTrue("Component " + component.getName() + " should be enabled!",
                            component.isEnabled());
                    continue;
                }
            }
            assertFalse("Component " + component.getName() + " should be disabled!",
                    component.isEnabled());
        }
    }

}
