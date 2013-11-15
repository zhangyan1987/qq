package com.yan.plugins.qq.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.yan.qq.common.Message;
import com.yan.qq.common.User;

public class ClientSocket {

	private String sendMsg;

	public ClientSocket(String sendMsg) {
		this.sendMsg = sendMsg;
	}

	public ClientSocket() {
	}
	
	public String send() {

		String host = "127.0.0.1";
		int port = 8888;
		Socket socket = null;
		StringBuilder sb = null;
		OutputStream os = null;
		//OutputStreamWriter osw = null;
		PrintWriter pw = null;
		BufferedWriter bw = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = null;
		try {
			socket = new Socket(host, port);

			// send
			os = socket.getOutputStream();
			pw = new PrintWriter(os);
			pw.write(sendMsg);
			pw.flush();
			// receive
			is = socket.getInputStream();
			if(is.available() > 0 ) {
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				line = br.readLine();
				br.close();
			}
			
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
	
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return line == null ? "" : line;
	}
	
	public boolean login(User u) {
		boolean result = false;
		String host = "127.0.0.1";
		int port = 8888;
		Socket socket = null;

		try {
			socket = new Socket(host, port);
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
	

}
