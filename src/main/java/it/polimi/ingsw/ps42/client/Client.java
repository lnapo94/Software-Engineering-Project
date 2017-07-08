package it.polimi.ingsw.ps42.client;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;


public class Client {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String userInput;
		String host;
		Logger logger = Logger.getLogger(Client.class);
		
		do {
			System.out.println("Insert the Server IP/WEB Address: ");
			
			userInput = scanner.nextLine();
		} while (userInput.equals(""));
		
		host = userInput;
		
		do {
			System.out.println("Which kind of connection do you like? [Socket : S], [Remote Method Invocation (RMI) : R]");
			
			userInput = scanner.nextLine();
			userInput = userInput.toUpperCase();
			
		} while((!userInput.equals("S") && !userInput.equals("R")));
		
		if(userInput.equals("S")) {
			try {
				ClientSocket socket = new ClientSocket(host);
				socket.run();
			} catch (IOException e) {
				logger.fatal("Network Error");
				logger.info(e);
			}

			scanner.close();
		} else {
			try {
				RMIClient rmi = new RMIClient(host);
				rmi.run();
			} catch (IOException e) {
				logger.fatal("Network Error");
				logger.info(e);
			}

			scanner.close();
		}
	}

}
