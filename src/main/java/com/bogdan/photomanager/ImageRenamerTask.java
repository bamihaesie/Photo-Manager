package com.bogdan.photomanager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ImageRenamerTask extends SwingWorker<Void, String> {
	
	private RenameCommand command;
	private JTextArea log;
	
	private ImageRenamerTask() {}
	
	public ImageRenamerTask(RenameCommand command, JTextArea log) {
		this.command = command;
		this.log = log;
	}

	@Override
	protected Void doInBackground() throws Exception {

		setProgress(0);
		
		File dir = new File(command.directory);
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
					if (command.resetNames) {
						filename = getFilename(i, numFiles, filename);
					}
					if (command.applyPrefix) {
						filename = command.prefixValue + filename;
					}
					if (command.applySuffix) {
						String[] filenameTokens = filename.split("\\.");
						filename = filenameTokens[0] + command.suffixValue + "." + filenameTokens[1];
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

}
