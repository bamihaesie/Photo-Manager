package com.bogdan.photomanager.swing;

import com.bogdan.photomanager.model.RenameCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bogdan
 */
public class PhotoManager extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = "\n";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 360;
    protected static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private JTextArea log;
    private JLabel label;
    private JFileChooser fc;
	private JProgressBar progressBar;
    protected List<JComponent> components;
    private ProgressListener progressListener;
	private ImageProcessingTask imageProcessingTask;
    private JTextField textField, prefixText, suffixText;
    private JButton openButton, startButton, cancelButton;
    private JCheckBox prefixBox, suffixBox, resetNamesBox;

    public PhotoManager() {
		super(new BorderLayout());

        log = buildTextArea();
        fc = buildFileChooser();
        label = new JLabel("Directory ");
        progressBar = buildProgressBar();
        progressListener = new ProgressListener(progressBar);

        openButton = new JButton("Browse");
        openButton.addActionListener(this);
        startButton = new JButton("START");
        startButton.addActionListener(this);
        cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(this);

        prefixBox = new JCheckBox("Prefix");
		suffixBox = new JCheckBox("Suffix");
		resetNamesBox = new JCheckBox("Reset Names");

        textField = new JTextField(30);
        prefixText = new JTextField(10);
		suffixText = new JTextField(10);

        groupComponentsIntoPanels();
		enableInteraction();
	}

    private JProgressBar buildProgressBar() {
        JProgressBar progressBar = new javax.swing.JProgressBar();
        progressBar.setPreferredSize(new Dimension(580, 20));
        return progressBar;
    }

    private JTextArea buildTextArea() {
        JTextArea log = new JTextArea(10, 50);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        return log;
    }

    private JFileChooser buildFileChooser() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return fc;
    }

    private void groupComponentsIntoPanels() {
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
        JScrollPane logScrollPane = new JScrollPane(log);
        panel3.add(logScrollPane);

        addComponentsToArray();
        handleLayout(layout);

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);
    }

    private void handleLayout(GroupLayout layout) {
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(prefixBox)
                .addComponent(suffixBox)
                .addComponent(resetNamesBox))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(prefixText)
                .addComponent(suffixText))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(startButton)
                .addComponent(cancelButton)));

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(prefixBox)
                .addComponent(prefixText))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(suffixBox)
                .addComponent(suffixText)
                .addComponent(startButton))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(resetNamesBox)
                .addComponent(cancelButton)));
    }

    private void addComponentsToArray() {
        components = new ArrayList<JComponent>();
        components.add(label);
        components.add(textField);
        components.add(openButton);
        components.add(prefixBox);
        components.add(prefixText);
        components.add(suffixBox);
        components.add(suffixText);
        components.add(resetNamesBox);
        components.add(startButton);
        components.add(cancelButton);
    }
    
    protected List<JComponent> getComponentList() {
        return components;
    }

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			handleOpenButton();
		} else if (e.getSource() == startButton) {
			handleStartButton();
		} else if (e.getSource() == cancelButton) {
			handleCancelButton();
		}
	}

    private void handleOpenButton() {
        if (fc.showOpenDialog(PhotoManager.this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            textField.setText(file.getPath());
            log.append("Opening: " + file.getPath() + "." + NEWLINE);
        } else {
            log.append("Open command cancelled by user." + NEWLINE);
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void handleStartButton() {
        RenameCommand command = buildCommand();
        if (command.isValid()) {
            disableInteraction();
            log.append("Operation started!" + NEWLINE);

            imageProcessingTask = new ImageProcessingTask(command, log, this);
            progressBar.setValue(0);
            imageProcessingTask.addPropertyChangeListener(progressListener);

            imageProcessingTask.execute();
        } else {
            log.append("Error: " + command.getMessage() + NEWLINE);
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    private RenameCommand buildCommand() {
        RenameCommand command = new RenameCommand();
        command.setApplyPrefix(prefixBox.isSelected());
        command.setApplySuffix(suffixBox.isSelected());
        command.setDirectory(textField.getText());
        command.setPrefixValue(prefixText.getText());
        command.setSuffixValue(suffixText.getText());
        command.setResetNames(resetNamesBox.isSelected());
        return command;
    }

    private void handleCancelButton() {
        log.append("Cancelling ..." + NEWLINE);
        imageProcessingTask.cancel(false);
        enableInteraction();
        log.setCaretPosition(log.getDocument().getLength());
    }
	
	public void enableInteraction () {
        for (JComponent component : components) {
            component.setEnabled(true);
        }
        cancelButton.setEnabled(false);
	}
	
	public void disableInteraction () {
        for (JComponent component : components) {
            component.setEnabled(false);
        }
        cancelButton.setEnabled(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

    private static void createAndShowGUI() {
        JFrame frame = createFrame();
        centerFrameToScreen(frame);
        setFrameProperties(frame);
    }

    protected static JFrame createFrame() {
        JFrame frame = new JFrame("Photo Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PhotoManager());
        return frame;
    }

    protected static void centerFrameToScreen(JFrame frame) {
        frame.setLocation(  (screenSize.width / 2) - (FRAME_WIDTH / 2),
                            (screenSize.height / 2) - (FRAME_HEIGHT / 2));
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    protected static void setFrameProperties(JFrame frame) {
        frame.setResizable(false);
        frame.setVisible(true);
    }

}
