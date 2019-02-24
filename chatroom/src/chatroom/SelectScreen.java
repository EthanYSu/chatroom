package chatroom;

import javax.swing.*;

public class SelectScreen {
	public static void main(String[] args) {
		//Allows user to either set up a server or client
		Object[] choices = { "Server", "Client"};
		Object userSelection = JOptionPane.showInputDialog(null, null, "Multi-User ChatRoom", JOptionPane.PLAIN_MESSAGE,null, choices,null);
			
		if(userSelection.equals("Client")) {
			String IPAddress = JOptionPane.showInputDialog("Enter the IP Address:");
			Client.main(IPAddress);
			
		}else if(userSelection.equals("Server")) {
			
		}
	}
}
