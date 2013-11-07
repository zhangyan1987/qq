package com.yan.plugins.qq.daos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.yan.plugins.qq.Activator;
import com.yan.plugins.qq.model.User;

/*
 * use local file to implement database
 */




public class QQDAOFileImpl implements IQQDAO{

	
	//database fileName
	private String databaseFileName = "/database.txt";
	
	
	
	private List<User> users;
	
	private FileReader fr = null;
	
	private BufferedReader br = null;
	
	private FileWriter fw = null;
	
	private BufferedWriter bw = null;
	
	
	
	@Override
	public User CheckUserInfo(User user) {
		if(null == users)
			getAllUsers();
		if(null ==user.getQQnumber() || user.getQQnumber().equals("") || 
				null ==user.getPasssword() || user.getPasssword().equals(""))
		return null;
		for(User temp :users) {
			if(temp.getQQnumber().equals(user.getQQnumber()) && temp.getPasssword().equals(user.getPasssword()))
				return temp;
		}
		return null;
			
	}

	@Override
	public List<User> getAllUsers() {
		
		
		InputStream is = Activator.class.getResourceAsStream(databaseFileName);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		users = new ArrayList<User>(100);
		try {
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#"))
					continue;
				String[] array = line.split("::");
				User user = new User(array[0],array[1],array[2]);
				users.add(user);
			}
			br.close();
			isr.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return users;
	}
	
	
	
	
	

}
