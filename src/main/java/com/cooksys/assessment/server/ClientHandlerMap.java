package com.cooksys.assessment.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.cooksys.assessment.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandlerMap {

		private static  ConcurrentHashMap<String, Socket> userMap = new ConcurrentHashMap<String, Socket>();

		
			public static void broadcastMessageToAllUsers(Message message) {
				try {
					PrintWriter writer = null;
					
					for(Socket userSocket : userMap.values()){
						
						writer = new PrintWriter(new OutputStreamWriter(userSocket.getOutputStream()));
						ObjectMapper mapper = new ObjectMapper();
						String connect = mapper.writeValueAsString(MessageFormatMachine.getFormattedMessage(message));
						writer.write(connect);
						writer.flush();
					}
					
					if(writer != null){
						writer.flush();
						//writer.close();
					}
				   
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
						
			}
			
			public static void tellUser(Message message)  {
				try {
					PrintWriter writer = null;
					
					for(String user : userMap.keySet()){
						
						if(user.equalsIgnoreCase(message.getCommand().replace("@", ""))){
							writer = new PrintWriter(new OutputStreamWriter(userMap.get(user).getOutputStream()));
							ObjectMapper mapper = new ObjectMapper();
							String connect = mapper.writeValueAsString(MessageFormatMachine.getFormattedMessage(message));
							writer.write(connect);
							writer.flush();
						}
					}
					
					if(writer != null){
						writer.flush();
						//writer.close();
					}
				   
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public static void addUser(String user, Socket socket)   {
				userMap.put(user, socket);
				
			}
			public static void removeUser(String user)  {
				userMap.remove(user);
				
			}
			
			public static String listUsers(){
				
				return userMap.keySet().toString();
						
			}
			
			public static void userConnection(Message message) {
				
			try {
				PrintWriter writer = null;
				
				for(Socket userSocket : userMap.values()){
					
					writer = new PrintWriter(new OutputStreamWriter(userSocket.getOutputStream()));
					ObjectMapper mapper = new ObjectMapper();
					String connect = mapper.writeValueAsString(MessageFormatMachine.getFormattedMessage(message));
					writer.write(connect);
					writer.flush();
				}
				
				if(writer != null){
					writer.flush();
					//writer.close();
				}
			   
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
}			
