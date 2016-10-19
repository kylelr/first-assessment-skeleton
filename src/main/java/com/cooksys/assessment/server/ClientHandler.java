package com.cooksys.assessment.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	
	private Socket socket;
	///create a concurrentHashMap to hold UserNames for Broadcast and users commands. 
	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	public void run() {
		try {

			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			while (!socket.isClosed()) {
				String raw = reader.readLine();
				Message message = mapper.readValue(raw, Message.class);

				
				switch (message.getCommand()) {
					case "connect":
						log.info("user <{}> connected", message.getUsername());
						break;
					case "disconnect":
						log.info("user <{}> disconnected", message.getUsername());
						this.socket.close();
						break;
					case "echo":
						log.info("user <{}> echoed message <{}>", message.getUsername(), message.getContents());
						String response = mapper.writeValueAsString(message);
						writer.write(response);
						writer.flush();
						break;
						//Add cases for additional commands: broadcast, @user and users. commit 1.4 
					case "broadcast":
						log.info("user <{}> says <{}>", message.getUsername(), message.getContents());
						String tellAll = mapper.writeValueAsString(message);
						writer.write(tellAll);
						writer.flush();
						break;
					case "@user":
						log.info("user <{}> whispered <{}>", message.getUsername(), message.getContents());
						String tell = mapper.writeValueAsString(message);
						writer.write(tell);
						writer.flush();
						break;
					case "users":
						log.info("active users <{}>", message.getUsername(), message.getContents());
						String currentUsers = mapper.writeValueAsString(message);
						writer.write(currentUsers);
						writer.flush();
						break;
				
				}
			}

		} catch (IOException e) {
			log.error("Something went wrong :/", e);
		}
	}

}
