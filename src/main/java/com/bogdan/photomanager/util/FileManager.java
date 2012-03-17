package com.bogdan.photomanager.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

public class FileManager {

    public static File[] getAllFilesInFolder(File folder) {
        if (folder.isDirectory()) {
            File[] children = folder.listFiles(new FilesOnlyFilter());
            Arrays.sort(children);
            return children;    
        }
        return new File[0];
    }
    
    public static File renameFile(File file, String prefix, String suffix,
                                    boolean resetNames, int index, int total)
                                        throws IOException {
        String[] fileTokens = file.getName().split("\\.");
        String fileName = fileTokens[0];
        String fileExtension = "." + fileTokens[1];

        if (resetNames) {
            fileName = getFilename(index, total);
        }
        fileName = prefix + fileName;
        fileName += suffix;

        return renameFileTo(file, fileName + fileExtension);
    }
    
    public static File renameFile(File file, String prefix, String suffix)
            throws IOException {
        return renameFile(file, prefix, suffix, false, 0, 0);
    }

    private static String getFilename (int index, int numFiles) {
        int numberOfDigits = Integer.toString(numFiles).length();
        String format = "%0" + numberOfDigits + "d";
        return String.format(format, index + 1);
    }
    
    private static File renameFileTo(File originalFile, String newFileName)
                            throws IOException {
        File newFile = new File(originalFile.getParent(), newFileName);
        FileUtils.copyFile(originalFile, newFile, true);
        boolean fileDeleteStatus = originalFile.delete();
        if (!fileDeleteStatus) {
            throw new IOException("Error renaming file!");
        }
        return newFile;
    }
}

class FilesOnlyFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.isFile();
    }
}
