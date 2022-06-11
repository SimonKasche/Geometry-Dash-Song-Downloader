package me.Stun.Console;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class window extends JFrame implements KeyListener {

	public static JTextPane TextArea;
	public static JScrollPane ScrollPane;
	public static Container cp;

	public int frameWidth;
	public int frameHeight;
	public int x;
	public int y;

	public static window windowInstance;

	public window() {

		super();
		windowInstance = this;

		frameWidth = 1280;
		frameHeight = 720;

		setSize(frameWidth, frameHeight);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		x = (d.width - getSize().width) / 2;
		y = (d.height - getSize().height) / 2;
		setLocation(x, y);
		setTitle("StunShell");
		setResizable(false);
		cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.setBackground(Color.BLACK);

		Font font = new Font("Lucida Console", Font.PLAIN, 20);
		TextArea = new JTextPane();
		TextArea.setFont(font);

		TextArea.setBounds(1, 1, 1920, 1050);
		TextArea.setBackground(Color.BLACK);
		TextArea.setForeground(Color.WHITE);

		ScrollPane = new JScrollPane(TextArea);
		ScrollPane.setBounds(1, 1, 1920, 1050);
		ScrollPane.setBackground(Color.BLACK);
		cp.add(ScrollPane);

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		setVisible(true);
		
	}

	public void keyPressed(KeyEvent e) {
		// key pressed
	}

	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//program stopps
			System.out.println("stopping..");
			StringBuilder sb = new StringBuilder(TextArea.getText());
			
			int index = 0;
			for(int i = 0;i<sb.length();i++) {
				
				if(sb.charAt(i) == '!') {
					index = i+1;
				}
				
			}
			sb.insert(index, "\n\nstopping!\n");
			TextArea.setText(sb.toString());

			me.Stun.InformationProcessor.informationManager.stop = true;
		}

	}

	public void keyTyped(KeyEvent e) {
		// key typed
	}
	
	public static void appendToPane(String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = TextArea.getDocument().getLength();
        TextArea.setCaretPosition(len);
        TextArea.setCharacterAttributes(aset, false);
        TextArea.replaceSelection(msg);
    }

}
