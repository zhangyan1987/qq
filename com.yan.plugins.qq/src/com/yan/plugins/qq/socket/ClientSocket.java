package com.yan.plugins.qq.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yan.qq.common.Message;
import com.yan.qq.common.User;

public class ClientSocket{
	String host = "127.0.0.1";
	int port = 8888;
	public  Socket socket = null;
	
	
	
	

	public ClientSocket() {
		try {
			socket =  new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public  boolean login(User u) {
		boolean result = false;
		
		

		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(u);
			Message msg = (Message)ois.readObject();
			if(msg.getMessageType()==1) 
				result = true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

		}
		return result;
	}
	
	
	public  void send(Message msg) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(msg);

		} catch (IOException e) {
			e.printStackTrace();
		}  finally {

		}
	}
	

}
