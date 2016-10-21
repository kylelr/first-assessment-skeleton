package com.cooksys.assessment.server;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.assessment.model.Message;

public class MessageFormatMachine {
	//private static String DATEFORMAT
	private static final Logger log = LoggerFactory.getLogger(MessageFormatMachine.class);
	public synchronized static Message getFormattedMessage(Message message) {
		
		Message newMessage = new Message();
		
		String UserName = message.getUsername();
       	String Command = message.getCommand();
		 String Content = message.getContents();
		 
		if (Command != null && !Command.isEmpty())   {
			
			StringBuilder sb = new StringBuilder();
			sb.append("${");
			Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
			sb.append(timeStamp.toString());
			sb.append("}");
			
			if (Command.equals("connect") || Command.equals("users") || Command.equals("disconnect"))  {
				sb.append(": ");
			}
			
			
			log.info("COMMAND:" + Command);
			if (!Command.equals("users"))  {
				
				if (Command.equals("connect") || Command.equals("disconnect"))  {
					
					String action = Command.equals("connect") ? "has connected " : "has disconnected ";
					sb.append("<${"  + UserName + "}>" + action + "\n");
				}else if(Command.equals("broadcast")){
					sb.append("<${"  + UserName + "}>" + "(all):" + message.getContents() + "\n");
				}else if(Command.startsWith("@")){ 
					sb.append("<${"  + UserName + "}>" + "(whisper):" + message.getContents() + "\n");
					
				}
				
				newMessage.setUsername(UserName);
				newMessage.setCommand(Command);
				newMessage.setContents(sb.toString());
				
				
				
			}  else {
				newMessage.setUsername(UserName);
				newMessage.setCommand(Command);
				sb.append( "currently connected users: \n");
				Content = Content.replace('[', ' ').trim(); 
				Content = Content.replace(']', ' ').trim(); 
				String[] users = Content.split(",");
				
				for (int i = 0; i < users.length; i++)  {
					sb.append("<${" + users[i] +"}>\n");
				}
				
				newMessage.setContents(sb.toString());
				
			}
			
			//String timeStamp = DateFormat.getTimeInstance().toString();
			log.info("Command: " + newMessage.getCommand());
			
	
			
			
		}
		
		return newMessage;
	}
	
}
/*
echo:
`${timestamp} <${username}> (echo): ${contents}`

broadcast:
`${timestamp} <${username}> (all): ${contents}`

direct message:
`${timestamp} <${username}> (whisper): ${contents}`

connection alert:
`${timestamp}: <${username}> has connected`
`${timestamp}: <${username}> has disconnected`

users:
`${timestamp}: currently connected users:`
(repeated)

`<${username}>`
```
*/
