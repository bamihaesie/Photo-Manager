package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.model.RenameCommand;
import com.bogdan.photomanager.util.FileManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageProcessingTask extends SwingWorker<Void, String> {
	
	private RenameCommand command;
	private JTextArea log;
	private PhotoManager photoManager;

	private ImageProcessingTask() {}
	
	public ImageProcessingTask(RenameCommand command, JTextArea log, PhotoManager photoManager) {
		this.command = command;
		this.log = log;
        this.photoManager = photoManager;
	}

	@Override
	protected Void doInBackground() throws Exception {

        setProgress(0);
        File[] files =
                FileManager.getAllFilesInFolder(new File(command.getDirectory()));

        int index = 0;
        for (File file : files) {
            if (!isCancelled()) {
                Thread.sleep(100);
                processFile(file, index, files.length);
                if (!isCancelled()) {
                    setProgress((100*index) / files.length);
                }
            } else {
                break;
            }
            index++;
        }
		return null;
	}
    
    private void processFile(File file, int index, int numFiles) throws IOException {
        String originalFilename = file.getName();
        if (command.isResetNames()) {
            file = FileManager.renameFile(file, command.getPrefixValue(), command.getSuffixValue(), true, index, numFiles);
        } else {
            file = FileManager.renameFile(file, command.getPrefixValue(), command.getSuffixValue());
        }
        publish("Renamed  " + originalFilename + "  to  " + file.getName());
    }

    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
            log.append(chunk + "\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            setProgress(0);
            log.append("Cancelled!\n");
        } else {
            setProgress(100);
            log.append("Done!\n");
            photoManager.enableInteraction();
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

}
