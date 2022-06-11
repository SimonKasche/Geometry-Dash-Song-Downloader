package me.Stun.InformationProcessor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class maxWindow extends JFrame {

	private static JButton button;
	private static JTextField textField;
	private static JLabel label;

	public maxWindow() {
		
		super();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		int frameWidth = 300;
		int frameHeight = 150;
		setSize(frameWidth, frameHeight);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d.width - getSize().width) / 2;
		int y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		setTitle("Maximum Filesize");
		setResizable(false);
		setLayout(null);
		
		button = new JButton("u cant see this");
		button.setBackground(Color.WHITE);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setForeground(Color.BLACK);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				//run
				double maxFileSize = 0;
				
				try {
					maxFileSize = Double.parseDouble(textField.getText());
					dispose();
					
					informationManager manager = new informationManager(maxFileSize);
					manager.start();
					
					me.Stun.mainFile.outputThread.start();
					me.Stun.mainFile.diagramThread.start();
					me.Stun.InformationProcessor.informationManager.initializing = true;
					
				}catch(Exception e) {
					
					textField.setText("invalid!");
					
				}
				
				
			}
		});
		add(button);
		
		Font font = new Font("Arial", 1, 35);
		textField = new JTextField();
		textField.setBounds(25, 25, 190, 50);
		textField.setFont(font);
		add(textField);
		
		label = new JLabel("MB");
		label.setFont(font);
		label.setBounds(220, 25, 100, 50);
		add(label);
		
		
		getRootPane().setDefaultButton(button);
		setVisible(true);
		
	}

}
