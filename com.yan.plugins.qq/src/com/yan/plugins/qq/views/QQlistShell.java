package com.yan.plugins.qq.views;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.yan.plugins.qq.socket.ClientSocket;
import com.yan.qq.common.Message;

public class QQlistShell extends Shell implements Runnable {

	private ClientSocket clientSocket;
	
	public Text recordText = null;

	public Text sendText = null;
	public QQlistShell(ClientSocket clientSocket) {
		this.clientSocket = clientSocket;
	}


	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		// receive message from server
		System.out.println("thread run...");
		while (true) {
			System.out.println("has logined...");
			try {
				ObjectInputStream ois = new ObjectInputStream(
						clientSocket.socket.getInputStream());
				final Message msg = (Message) ois.readObject();
				System.out.println(msg.getSender() + " 给 " + msg.getRecevier()
						+ " 说:" + msg.getMsg());
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						recordText.setText(recordText.getText() + "\n" + msg.getMsg());
					}
				});
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
