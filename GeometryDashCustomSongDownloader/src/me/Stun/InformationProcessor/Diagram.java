package me.Stun.InformationProcessor;

import java.text.DecimalFormat;

public class Diagram extends Thread {

	private static int[] state = new int[30]; // values from 0 to 10
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public void run() {

		int counter = 0;
		String oldDiagram = "";

		while (true) {

			if (me.Stun.InformationProcessor.informationManager.initializing == false) {

				String text = me.Stun.Console.window.TextArea.getText();
				StringBuilder sb = new StringBuilder(text);

				char currentChar = ' ';
				while (currentChar != '!') {
					currentChar = sb.charAt(sb.length() - 2);
					sb.deleteCharAt(sb.length() - 1);
				}

				if (me.Stun.InformationProcessor.informationManager.downloading == true) {
					sb.append("\n\nfetching " + me.Stun.InformationProcessor.informationManager.songID + ".mp3\t");
					sb.append(me.Stun.InformationProcessor.informationManager.percentage + "%  ");
					sb.append("(" + df.format(me.Stun.InformationProcessor.informationManager.speed) + "MB/s)");
				}

				counter++;
				if (counter == 10) {
					oldDiagram = makeDiagram();
					counter = 0;
				}

				sb.append(oldDiagram);

				me.Stun.Console.window.TextArea.setText(sb.toString());
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public static String editDiagram(String[] input) {

		for (int i = 0; i < input.length; i++) {

			for (int j = 0; j < input[0].length(); j++) {

				if (j != 0 && i != input.length - 1) {

					if (input[i].charAt(j) == '/' && input[i + 1].charAt(j - 1) == '_') {

						StringBuilder sb = new StringBuilder(input[i + 1]);
						sb.setCharAt(j - 1, '/');
						input[i + 1] = sb.toString();

						StringBuilder sb1 = new StringBuilder(input[i]);
						sb1.setCharAt(j, '_');
						input[i] = sb1.toString();

					}

				}
			}
		}

		String output = "\n";
		for (int i = 0; i < input.length; i++) {
			output = output + input[i];
		}

		return output;

	}

	public static String makeDiagram() {

		float speed = me.Stun.InformationProcessor.informationManager.speed;

		for (int i = 0; i < state.length - 1; i++) {
			state[i] = state[i + 1];
		}

		int scalingFactor = 1; // TODO scaling !!!!!!!!!!!!!!!!

		state[state.length - 1] = (int) (speed / scalingFactor * 10);

		if (state[state.length - 1] == 10) {
			scalingFactor++;
			state = scaleState(state, scalingFactor);
		}

		String[] diagram = new String[10];

		for (int i = 0; i < 10; i++) {

			String tempString = "";

			for (int j = 0; j < state.length; j++) {

				if (state[j] == i) {

					char line = '_';
					if (j != 0) {
						if (state[j] > state[j - 1])
							line = '/';
						if (state[j] < state[j - 1])
							line = '\\';
					}
					tempString = tempString + line;

				} else {

					tempString = tempString + " ";

				}

			}

			tempString = tempString + "\n";
			diagram[i] = tempString;

		}

		// invert array
		for (int left = 0, right = diagram.length - 1; left < right; left++, right--) {
			String temp = diagram[left];
			diagram[left] = diagram[right];
			diagram[right] = temp;
		}

		return editDiagram(diagram);

	}

	public static int[] scaleState(int[] input, int scale) {
		
		for(int i = 0;i<input.length;i++) {
			
			input[i] = input[i] / scale;
			
		}
		
		return input;
		
	}

}
