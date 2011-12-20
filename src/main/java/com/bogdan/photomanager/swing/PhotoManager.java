package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.model.RenameCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 
 * @author bogdan
 * @version 1
 * @since 2011
 *
 */
public class PhotoManager extends JPanel implements ActionListener {

	/**
	 * constants
	 */
	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
	
	/**
	 * components
	 */
	static JLabel label;
	static JTextField textField;
	static JButton openButton, startButton, cancelButton;
	static JFileChooser fc;
	static JCheckBox prefixBox, suffixBox, resetNamesBox;
	static JTextField prefixText, suffixText;
	static JTextArea log;
	static JProgressBar progressBar;
	static ProgressListener progressListener;
	
	ImageProcessingTask imageProcessingTask;

	/**
	 * Constructor
	 */
	public PhotoManager() {

		super(new BorderLayout());

		// Create log section
		progressBar = new javax.swing.JProgressBar();
		progressBar.setPreferredSize(new Dimension(580, 20));
        progressListener = new ProgressListener(progressBar);
		log = new JTextArea(10, 50);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create file chooser section
		label = new JLabel("Directory ");
		textField = new JTextField(30);
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		openButton = new JButton("Browse");
		openButton.addActionListener(this);

		// Create checkbox section
		prefixBox = new JCheckBox("Prefix");
		suffixBox = new JCheckBox("Suffix");
		resetNamesBox = new JCheckBox("Reset Names");
		prefixText = new JTextField(10);
		suffixText = new JTextField(10);
		startButton = new JButton("START");
		startButton.addActionListener(this);
		cancelButton = new JButton("CANCEL");
		cancelButton.addActionListener(this);

		// Display layout
		JPanel panel1 = new JPanel();
		panel1.add(label);
		panel1.add(textField);
		panel1.add(openButton);

		JPanel panel2 = new JPanel();
		GroupLayout layout = new GroupLayout(panel2);
		panel2.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.PAGE_AXIS));
		panel3.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		panel3.add(progressBar);
		panel3.add(new JSeparator(SwingConstants.HORIZONTAL));
		panel3.add(logScrollPane);

		// Add the buttons and the log to this panel.
		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(prefixBox)
								.addComponent(suffixBox)
								.addComponent(resetNamesBox))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(prefixText)
								.addComponent(suffixText))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(startButton)
								.addComponent(cancelButton)));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE)
								.addComponent(prefixBox)
								.addComponent(prefixText))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE)
								.addComponent(suffixBox)
								.addComponent(suffixText)
								.addComponent(startButton))
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.TRAILING)
								.addComponent(resetNamesBox)
								.addComponent(cancelButton)));

		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.CENTER);
		add(panel3, BorderLayout.SOUTH);
		
		enableInteraction();

	}

	/**
	 * 
	 * Button event listeners
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(PhotoManager.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				// This is where a real application would open the file.
				textField.setText(file.getPath());
				log.append("Opening: " + file.getPath() + "." + newline);
			} else {
				log.append("Open command cancelled by user." + newline);
			}

			// Handle start button action
		} else if (e.getSource() == startButton) {
			
			// Create command object
			RenameCommand command = new RenameCommand();
			command.setApplyPrefix(prefixBox.isSelected());
			command.setApplySuffix(suffixBox.isSelected());
			command.setDirectory(textField.getText());
			command.setPrefixValue(prefixText.getText());
			command.setSuffixValue(suffixText.getText());
			command.setResetNames(resetNamesBox.isSelected());
			
			if (command.isValid()) {
				
				disableInteraction();

				log.append("Operation started!" + newline);
				
				// Create worker thread
				imageProcessingTask = new ImageProcessingTask(command, log);
				progressBar.setValue(0);
				imageProcessingTask.addPropertyChangeListener(progressListener);
				
				// Start worker thread
				imageProcessingTask.execute();
				
			} else {
				log.append("Error: " + command.getMessage() + newline);
			}

			// Handle start button action
		} else if (e.getSource() == cancelButton) {
			
			log.append("Cancelling ..." + newline);
			imageProcessingTask.cancel(false);
			enableInteraction();
			
		}

		log.setCaretPosition(log.getDocument().getLength());

	}
	
	public static void enableInteraction () {
		label.setEnabled(true);
		textField.setEnabled(true);
		openButton.setEnabled(true);
		prefixBox.setEnabled(true);
		prefixText.setEnabled(true);
		suffixBox.setEnabled(true);
		suffixText.setEnabled(true);
		resetNamesBox.setEnabled(true);
		startButton.setEnabled(true);
		cancelButton.setEnabled(false);
	}
	
	public static void disableInteraction () {
		label.setEnabled(false);
		textField.setEnabled(false);
		openButton.setEnabled(false);
		prefixBox.setEnabled(false);
		prefixText.setEnabled(false);
		suffixBox.setEnabled(false);
		suffixText.setEnabled(false);
		resetNamesBox.setEnabled(false);
		startButton.setEnabled(false);
		cancelButton.setEnabled(true);
	}

	/**
	 * 
	 * Create and show frame
	 * 
	 */
	private static void createAndShowGUI() {
		
		int frameWidth = 600;
		int frameHeight = 360;
		
		// Create and set up the window.
		JFrame frame = new JFrame("Photo Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new PhotoManager());
		
		// Display the window in the center of the screen
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    frame.setLocation((screenWidth / 2) - (frameWidth / 2), (screenHeight / 2) - (frameHeight / 2));
		
		frame.setSize(frameWidth, frameHeight);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
