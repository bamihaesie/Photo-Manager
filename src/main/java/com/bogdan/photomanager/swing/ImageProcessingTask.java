package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.model.RenameCommand;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ImageProcessingTask extends SwingWorker<Void, String> {
	
	private RenameCommand command;
	private JTextArea log;
	
	private ImageProcessingTask() {}
	
	public ImageProcessingTask(RenameCommand command, JTextArea log) {
		this.command = command;
		this.log = log;
	}

	@Override
	protected Void doInBackground() throws Exception {

		setProgress(0);
		
		File dir = new File(command.getDirectory());
		String[] children = dir.list();
		Arrays.sort(children);
		int numFiles = children.length;
		
		if (children != null) {
			for (int i=0; i<children.length; i++) {
				if (!isCancelled()) {
					Thread.sleep(100);
					String filename = children[i];
					String originalFilename = filename;
					File file = new File(dir, filename);
					if (file.isDirectory()) {
						continue;
					}
					if (command.isResetNames()) {
						filename = getFilename(i, numFiles, filename);
					}
					if (command.isApplyPrefix()) {
						filename = command.getPrefixValue() + filename;
					}
					if (command.isApplySuffix()) {
						String[] filenameTokens = filename.split("\\.");
						filename = filenameTokens[0] + command.getSuffixValue() + "." + filenameTokens[1];
					}
					
					File newFile = new File(dir, filename);
					file.renameTo(newFile);
					
					publish("Renamed  " + originalFilename + "  to  " + filename);
					
					if (!isCancelled()) {
						setProgress((int)((100*i) / numFiles));
					}
					
					
				}
				else {
					break;
				}
			}
		}
		
		return null;
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
            log.setCaretPosition(log.getDocument().getLength());
        } else {
            setProgress(100);
            log.append("Done!\n");
            PhotoManager.enableInteraction();
            log.setCaretPosition(log.getDocument().getLength());
        }
    }
	
	private String getFilename (int index, int numFiles, String originalFilename) {
		int numberOfDigits = Integer.toString(numFiles).length();
		String format = "%0" + numberOfDigits + "d";
		String result = String.format(format, index + 1) + "." + getExtension(originalFilename);
		return result;
	}
	
	private String getExtension (String filename) {
		String[] tokens = filename.split("\\.");
		return tokens[1];
	}

}
