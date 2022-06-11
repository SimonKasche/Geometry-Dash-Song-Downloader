package me.Stun.Network;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class connection {

	private static HttpURLConnection connection;
	private static URL url;
	private static String baseURL = "https://gdbrowser.com/api";

	public static void getLevelData(int page) throws IOException, InterruptedException {
		
		url = new URL(baseURL + "/search/*?page=" + page);

		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		int status = connection.getResponseCode();

		if (status > 299) {
			me.Stun.Console.window.appendToPane("connection failed, exit code " + status + "\n", Color.RED);

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			LinkedList<String> response = new LinkedList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				response.add(line);
			}

			for (int i = 0; i < response.size(); i++) {
				me.Stun.Console.window.appendToPane(response.get(i), Color.RED);
				me.Stun.Console.window.appendToPane("", Color.WHITE);
			}
			Thread.sleep(5000);
		}

		else {

			LinkedList<String> response = new LinkedList<String>();
			String line;

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				response.add(line);
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter("output.json"));
			for (int i = 0; i < response.size(); i++) {
				bw.write(response.get(i));
			}

			bw.close();
			connection.disconnect();
		}
	}

}
