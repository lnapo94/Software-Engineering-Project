package it.polimi.ingsw.ps42.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);
		String userInput;
		String host = "localhost";
		
		do {
			System.out.println("Which kind of connection do you like? [Socket : S], [Remote Method Invocation (RMI) : R]");
			
			userInput = scanner.nextLine();
			userInput = userInput.toUpperCase();
			
		} while((!userInput.equals("S") && !userInput.equals("R")));
		
		if(userInput.equals("S")) {
			ClientSocket socket = new ClientSocket(host);
			socket.run();

			scanner.close();
		} else {
			RMIClient rmi = new RMIClient(host);
			rmi.run();

			scanner.close();
		}
	}

}
