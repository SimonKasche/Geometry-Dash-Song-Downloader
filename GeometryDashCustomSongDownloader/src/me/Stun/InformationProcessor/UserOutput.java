package me.Stun.InformationProcessor;

import java.text.DecimalFormat;
import java.awt.Color;

public class UserOutput extends Thread {

	private static final DecimalFormat df = new DecimalFormat("0.00");

	public void run() {

		me.Stun.Console.window.TextArea.setText(generateNewInformation());
		me.Stun.Console.window.appendToPane("\ninitializing..\t ", Color.WHITE);

		while (me.Stun.InformationProcessor.informationManager.stop == false) {
			try {
				Thread.sleep(2);
				if (me.Stun.InformationProcessor.informationManager.update == true
						&& me.Stun.InformationProcessor.informationManager.initializing == false) {

					me.Stun.InformationProcessor.informationManager.update = false;
					me.Stun.Console.window.appendToPane("",Color.WHITE);
					me.Stun.Console.window.TextArea.setText(generateNewInformation());

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public String generateNewInformation() {

		double currentFileSize = me.Stun.InformationProcessor.informationManager.fileSize;
		double maxFileSize = me.Stun.InformationProcessor.informationManager.maxFileSize;
		double downloadingFileSize = me.Stun.InformationProcessor.informationManager.downloadingFileSize;
		String name = me.Stun.InformationProcessor.informationManager.publicName;

		String output = "Stun Shell [Version 1.0.0.3]\n" + "(c) Stun Corporation. All rights reserved.\n\n";
		
		output = output + "downloading progress:\t" + df.format(currentFileSize) + " MB / " + maxFileSize + " MB (";
		output = output + df.format((double) currentFileSize / maxFileSize * 100) + "%)\n";
		output = output + "currently downloading:\t" + name + " (" + downloadingFileSize + "MB)\n";
		output = output + "songs downloaded:\t\t" + me.Stun.InformationProcessor.informationManager.counter + "\n\n";
		
		output = output + "current Geometry Dash level:\t\t" + me.Stun.InformationProcessor.informationManager.levelName + "\n";
		output = output + "current Geometry Dash page:\t\t" + me.Stun.InformationProcessor.informationManager.page + "\n";
		output = output + "levels searched:\t\t\t\t" + me.Stun.InformationProcessor.informationManager.levelCounter + "\n\n";
		
		output = output + "download path:\t" + me.Stun.InformationProcessor.informationManager.path + "\n";
		output = output + "press 'ESC' to exit safely!\n";

		return output;

	}

}
