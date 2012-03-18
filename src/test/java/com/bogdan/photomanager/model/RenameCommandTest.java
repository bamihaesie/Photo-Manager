package com.bogdan.photomanager.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RenameCommandTest {

    RenameCommand renameCommand;

    @Before
    public void setUp() {
        renameCommand = new RenameCommand();
    }

    @Test
    public void isValidNoDirectoryTest() {
        assertNull("Message is not empty!", renameCommand.getMessage());
        renameCommand.isValid();
        assertEquals("Directory must be specified!", renameCommand.getMessage());
    }

    @Test
    public void isValidNoOperationTest() {
        assertNull("Message is not empty!", renameCommand.getMessage());
        renameCommand.setDirectory("test dir");
        renameCommand.isValid();
        assertEquals("At least one operation must be specified!", renameCommand.getMessage());
    }

    @Test
    public void prefixTest() {
        assertNull("Message is not empty!", renameCommand.getMessage());
        renameCommand.setDirectory("test dir");
        renameCommand.setApplyPrefix(true);
        renameCommand.isValid();
        assertEquals("Is prefix operation, must have prefix value!", renameCommand.getMessage());
    }

    @Test
    public void suffixTest() {
        assertNull("Message is not empty!", renameCommand.getMessage());
        renameCommand.setDirectory("test dir");
        renameCommand.setApplySuffix(true);
        renameCommand.isValid();
        assertEquals("Is suffix operation, must have suffix value!", renameCommand.getMessage());
    }

}
