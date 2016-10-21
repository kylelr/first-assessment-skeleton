package com.cooksys.assessment.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
		DateTimeFormatter timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
		
	
	}

	public void run() {
		try {

			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (!socket.isClosed()) {
				String raw = reader.readLine();
				Message message = mapper.readValue(raw, Message.class);

				String command = message.getCommand();
				
				if (command.startsWith("@"))   {
					command = "@";
				}
				
				switch (command) {
					case "connect":
						log.info("user <{}> connected", message.getUsername());
						ClientHandlerMap.addUser(message.getUsername(), socket);
						ClientHandlerMap.userConnection(message);
						break;
					case "disconnect":
						log.info("user <{}> disconnected", message.getUsername());		
						ClientHandlerMap.removeUser(message.getUsername());
						ClientHandlerMap.userConnection( message);
						this.socket.close();
						break;
					case "echo":
						log.info("user <{}> echoed message <{}>", message.getUsername(), message.getContents());
						String response = mapper.writeValueAsString(message);
						writer.write(response);
						writer.flush();
						break;
						//Added cases for additional commands: broadcast, @user and users. commit which still need to be accurately implemented 1.4 
					case "broadcast":
						log.info("user <{}> says <{}>", message.getUsername(), message.getContents());
						ClientHandlerMap.broadcastMessageToAllUsers(message);
						break;
					case "@":
						log.info("user <{}> whispered <{}>", message.getUsername(), "\nContents: " + message.getContents() + "!!!");
						
						ClientHandlerMap.tellUser(message);
						break;
					case "users":
						log.info("currently connected users: <{}>", message.getUsername(), message.getContents());
						message.setContents(ClientHandlerMap.listUsers());
						writer.write(mapper.writeValueAsString(MessageFormatMachine.getFormattedMessage(message)).toString());
						//String currentUsers = mapper.writeValueAsString(message);
						//writer.write(currentUsers);
						writer.flush();
						break;
				
				}
			}

		} catch (IOException e) {
			log.error("Something went wrong :/", e);
		}
	}

}
