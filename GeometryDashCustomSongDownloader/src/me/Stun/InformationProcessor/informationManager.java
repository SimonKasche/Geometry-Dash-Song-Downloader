package me.Stun.InformationProcessor;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class informationManager extends Thread {

	private static final DecimalFormat df = new DecimalFormat("0.00");

	public static boolean initializing = false;
	public static boolean stop = false;
	public static boolean downloading = false;
	public static double maxFileSize = 0.0;
	public static double fileSize = 0.0;
	public static double downloadingFileSize = 0.0;
	public static String publicName = "";
	public static String levelName = "";
	public static String path = "";
	public static String percentage = "";
	public static String songID = "";
	public static int page = 0;
	public static int counter = 0;
	public static int levelCounter = 0;

	public static boolean update = false;

	public informationManager(double maxFileSizeInput) {

		maxFileSize = maxFileSizeInput;

	}

	@SuppressWarnings("deprecation")
	public void run() {

		try {

			while (stop == false) {
				try {
					me.Stun.Network.connection.getLevelData(page);
				} catch (Exception e) {
					me.Stun.Console.window.appendToPane(
							"\n\n connection to gdbrowser.com failed\n retrying in 5 seconds..", Color.RED);
					Thread.sleep(5000);
				}
				LinkedList<String[]> links = getNewgroundsLinks();

				for (int i = 0; i < links.size(); i++) {
					
					if (downloadSong(links.get(i)[0], links.get(i)[2], links.get(i)[3],
							Double.parseDouble(links.get(i)[1]), links.get(i)[4]) == true) {

						Double currentFileSize = Double.parseDouble(links.get(i)[1]);
						fileSize = (double) fileSize + currentFileSize;

						if (fileSize > maxFileSize)
							stop = true;

					}

					levelCounter++;

					if (stop == true)
						i = links.size();

				}

				page++;
			}
			
			me.Stun.mainFile.diagramThread.stop();
			Thread.sleep(1000);
			me.Stun.Console.window.windowInstance.dispose();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static LinkedList<String[]> getNewgroundsLinks() throws FileNotFoundException, IOException, ParseException {

		System.out.println("getting Newgrounds links..");

		LinkedList<String[]> links = new LinkedList<String[]>(); // [link], [fileSize], [ID], [songName], [levelName]

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("output.json"));

		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject level = (JSONObject) jsonArray.get(i);
			
			Object officialSongObject = level.get("officialSong");
			String officialSongString = officialSongObject.toString();
			int officialSong = Integer.parseInt(officialSongString);
			
			if (officialSong == 0) {
				
				String[] tempArray = new String[5];
				
				String link = (String) level.get("songLink");
				tempArray[0] = link;
				
				String fileSize = (String) level.get("songSize");
				StringBuilder sb = new StringBuilder(fileSize);
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
				tempArray[1] = sb.toString();
				
				Object id = level.get("songID");
				String idString = id.toString();
				tempArray[2] = idString;
				
				String songName1 = (String) level.get("songName");
				tempArray[3] = songName1;
				
				String levelNameInput = (String) level.get("name");
				tempArray[4] = levelNameInput;
				
				links.add(tempArray);
			}
			
		}
		
		System.out.println("successfully parsed Newgrounds links");

		return links;
	}
	
	// Diagram
	public static float speed = 0;
	
	public static boolean downloadSong(String link, String ID, String name, Double fileSize2, String levelNameInput)
			throws IOException {
		// return true = success | return false = failure/redundant
		
		songID = ID;
		
		File roaming = new File(System.getenv("APPDATA"));
		File appData = roaming.getParentFile();
		File GeometryDash = new File(appData.getAbsolutePath() + "\\local\\GeometryDash");

		path = GeometryDash.getAbsolutePath();

		File[] files = GeometryDash.listFiles();

		// search for dublicate
		boolean found = false;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(ID)) {
				found = true;
			}
		}
		
		if (found == true) {
			System.out.println("skipped " + name);

			if (initializing == true) {

				try {
					String text = me.Stun.Console.window.TextArea.getText();
					StringBuilder sb = new StringBuilder(text);
					char currentChar = ' ';
					while (currentChar != '\t') {
						currentChar = sb.charAt(sb.length() - 2);
						sb.deleteCharAt(sb.length() - 1);
					}

					sb.append("skipping: " + name + "..");
					me.Stun.Console.window.TextArea.setText(sb.toString());
				} catch (Exception e) {
					System.out.println("an error has occured:");
					System.out.println(e.toString());
				}
			}

			return false;
		} else {

			publicName = name;
			levelName = levelNameInput;
			downloadingFileSize = fileSize2;
			update = true;
			downloading = true;

			InputStream inputStream = null;
			try {
				URLConnection conn = new URL(link).openConnection();
				inputStream = conn.getInputStream();
				int size = conn.getContentLength();
				
				OutputStream outstream = new FileOutputStream(
						new File(GeometryDash.getAbsolutePath() + "/" + ID + ".mp3"));
				byte[] buffer = new byte[4096];
				int len;
				
				initializing = false;

				// progress bar
				int tempCounter = 0; // byteBuffer size counter
				int tempCounter2 = 0; // byteBuffer counter
				int dataCounter = 0; // byteBuffer counter
				
				float duration = 0;
				long begin = System.currentTimeMillis();

				while ((len = inputStream.read(buffer)) > 0) {
					outstream.write(buffer, 0, len);

					tempCounter2++;
					tempCounter = tempCounter + len;
					dataCounter = dataCounter + len;
					double percentageDouble = (double) tempCounter / size * 100;

					if (tempCounter2 % 10 == 0) {// slow progress bar down to improve performance

						long end = System.currentTimeMillis();
						duration = duration + (end - begin);

						if (duration >= 1000) {

							float data = (float) dataCounter / 1000000;
							speed = (float) data / (duration / 1000);
							duration = 0;
							dataCounter = 0;					
							
						}
						
						percentage = df.format(percentageDouble);
						
						/*String text = me.Stun.Console.window.TextArea.getText();
						StringBuilder sb = new StringBuilder(text);
						char currentChar = ' ';
						while (currentChar != '\t') {
							currentChar = sb.charAt(sb.length() - 2);
							sb.deleteCharAt(sb.length() - 1);
						}				
						
						sb.append(percentageString + "% (" + speedString + "MB/s)\n\n");						
						me.Stun.Console.window.TextArea.setText(sb.toString());*/
						begin = System.currentTimeMillis();

					}
				}

				outstream.close();

				System.out.println("successfully downloaded " + ID + ".mp3");
				
				percentage = "100.00";
				downloading = false;
				update = true;
				counter++;

				return true;

			} catch (Exception e) {
				e.printStackTrace();
				// song not allowed for use

				System.out.println("song is not allowed for use");

				return false;
			}

		}

	}

}
