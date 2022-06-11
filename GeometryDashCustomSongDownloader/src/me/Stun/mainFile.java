package me.Stun;

import java.io.IOException;

import me.Stun.InformationProcessor.Diagram;
import me.Stun.InformationProcessor.UserOutput;

public class mainFile {
	
	public static UserOutput outputThread;
	public static Diagram diagramThread;
	
	public static void main(String[]args) throws IOException {
		
		new me.Stun.Console.window();		
		outputThread = new UserOutput();	
		diagramThread = new Diagram();
		
		new me.Stun.InformationProcessor.maxWindow();
		
	}
	
}
