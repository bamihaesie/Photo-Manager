package com.bogdan.photomanager.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileManagerTest {
    
    private File workFolder;
    private File file1, file2;
    
    @Before
    public void setUp() throws IOException {
        File temporaryFolder = new File(System.getProperty("java.io.tmpdir"));
        workFolder = new File(temporaryFolder, "fileUtilsTest");
        createFolder(workFolder);
        file2 = File.createTempFile("file0", ".txt", workFolder);
        file1 = File.createTempFile("file1", ".txt", workFolder);
    }

    @After
    public void tearDown() throws IOException {
        clearWorkArea();
    }

    @Test
    public void testGetAllFilesInFolderValid() {
        File[] files = FileManager.getAllFilesInFolder(workFolder);
        assertNotNull("Null list of files returned!", files);
        assertTrue("Empty list of files returned!", files.length > 0);
        for (File file : files) {
            assertTrue("Must only return files!", file.isFile());
        }
    }

    @Test
    public void testGetAllFilesInFolderFile() {
        File[] files = FileManager.getAllFilesInFolder(file1);
        assertNotNull("Null list of files returned!", files);
        assertEquals("No files should be returned!", 0, files.length);
    }

    @Test
    public void testGetAllFilesInFolderSorting() {
        File[] files = FileManager.getAllFilesInFolder(workFolder);
        assertTrue(files[0].getName().compareTo(files[1].getName()) < 0);
    }

    @Test
    public void testRenameFileWithReset() throws IOException {
        file1 = FileManager.renameFile(file1, "prefix", "suffix", true, 0, 1);
        assertEquals("Rename did not succeed", "prefix1suffix.txt", file1.getName());
    }

    @Test
    public void testRenameFileWithNoReset() throws IOException {
        String beforeName = file1.getName().split("\\.")[0];
        file1 = FileManager.renameFile(file1, "prefix", "suffix");
        assertEquals("Rename did not succeed",
                "prefix" + beforeName + "suffix.txt", file1.getName());
    }

    private void clearWorkArea() throws IOException {
        deleteFileOrFolder(file1);
        deleteFileOrFolder(file2);
        deleteFileOrFolder(workFolder);
    }

    private void createFolder(File folder) throws IOException {
        boolean folderStatus = folder.mkdir();
        if (!folderStatus) {
            throw new IOException("Folder could not be created!");
        }
    }

    private void deleteFileOrFolder(File file) throws IOException {
        if (file.exists()) {
            boolean deleteStatus = file.delete();
            if (!deleteStatus) {
                throw new IOException("File or folder could not be deleted!");
            }
        }
    }
    
}
