package chatroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client {
	static class ChatClient {
		static Socket socket;
		static OutputStream outputStream;
		
		public void initializeSocket(String server, int port) throws UnknownHostException, IOException {
			// create Socket and read info
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();
			
			// creating a thread for receiving messages
			Thread incomingThread = new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						//String currentLine;
						while (bufferedReader.readLine() != null) {
							notifyAll();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			incomingThread.start();

		}
		
		public void closeSocket() {
			try {
				socket.close();
			}catch(IOException err) {
				System.out.println("Error: " + err);
			}
		}
		
		public void sendMessage(String message) {
			try {
				outputStream.write((message+"\n").getBytes());
				outputStream.flush();
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error:" + e);
			}
		}
	}
	
	static class ChatGUI extends JFrame{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextArea textArea;
		private JTextField inputField;
		private JButton sendButton;
		private ChatClient chatClient;
		
		public ChatGUI(ChatClient chatClient) {
			this.chatClient = chatClient;
			createChatRoom();
		}
		
		private void createChatRoom() {
			textArea = new JTextArea(40,40);
			
			add(new JScrollPane(textArea), BorderLayout.CENTER);
			Box box = Box.createHorizontalBox();
            add(box, BorderLayout.SOUTH);
            inputField = new JTextField();
            sendButton = new JButton("Send");
            box.add(inputField);
            box.add(sendButton);

            // Action for the inputTextField and the goButton
            ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String str = inputField.getText();
                    if (str != null && str.trim().length() > 0)
                        chatClient.sendMessage(str);
                    inputField.selectAll();
                    inputField.requestFocus();
                    inputField.setText("");
                }
            };
            inputField.addActionListener(sendListener);
            sendButton.addActionListener(sendListener);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    chatClient.closeSocket();
                }
            });
		}
	}
	
	public static void main(String args) {
        String server = args;
        int port = 3000;
        ChatClient access = new ChatClient();
        System.out.println("hello");
        JFrame frame = new ChatGUI(access);
        frame.setTitle("MyChatApp - connected to " + server + ":" + port);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        try {
            access.initializeSocket(server, port);
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server + ":" + port);
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
