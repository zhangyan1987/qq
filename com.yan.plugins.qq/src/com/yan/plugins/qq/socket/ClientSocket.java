package com.yan.plugins.qq.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientSocket {

	private String sendMsg;

	public ClientSocket(String sendMsg) {
		this.sendMsg = sendMsg;
	}

	public String send() {

		String host = "127.0.0.1";
		int port = 8888;
		Socket socket = null;
		StringBuilder sb = null;
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = null;
		try {
			socket = new Socket(host, port);

			// send
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			bw.write(sendMsg);
			bw.flush();
			// receive
			is = socket.getInputStream();
			if(is.available() > 0 ) {
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				line = br.readLine();	
			}
			//br.close();
			// line = br.readLine();//userinfo
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				/*if (bw != null) {
					bw.close();
					bw = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}

				if (os != null) {
					os.close();
					os = null;
				}

				if (isr != null) {
					isr.close();
					isr =null;
				}

				if (is != null) {
					is.close();
					is = null;
				}
*/
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return line == null ? "" : line;
	}

}
