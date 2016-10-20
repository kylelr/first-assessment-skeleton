package com.cooksys.assessment.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.cooksys.assessment.model.Message;

public class ClientHandlerMap {

		private static  ConcurrentHashMap<String, Socket> userMap = new ConcurrentHashMap<String, Socket>();

		
			public static void broadcastMessageToAllUsers(Message message) {
							
			}
			
			public static void tellUser(String user, String message)  {
				
			}
			
			public static void addUser(String user, Socket socket)   {
				userMap.put(user, socket);
				
			}
			public static void removeUser(String user)  {
				userMap.remove(user);
				
			}
			
			public static void listUsers(String user){
				
				System.out.println(userMap.keySet().toString());
			}
			
			public static void userConnection(Socket socket, String message) {
				
			try {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
}			
