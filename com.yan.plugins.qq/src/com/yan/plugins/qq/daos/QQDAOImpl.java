package com.yan.plugins.qq.daos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yan.plugins.qq.model.User;

public class QQDAOImpl implements IQQDAO{

	private List<User> users;
	@Override
	public boolean CheckUserInfo(User user) {
		if(null == users)
			getAllUsers();
		if(null ==user.getQQnumber() || user.getQQnumber().equals("") || 
				null ==user.getPasssword() || user.getPasssword().equals(""))
		return false;
		for(User temp :users) {
			if(temp.getQQnumber().equals(user.getQQnumber()) && temp.getPasssword().equals(user.getPasssword()))
				return true;
		}
		return false;
			
	}

	@Override
	public List<User> getAllUsers() {
		
		users = new ArrayList<User>(100);
		Date date = new Date();
		users.add(new User("123456","123","admin"));
		for(int i =0;i<5;i++) {
			users.add(new User(date.getTime()+"","123","qqUser"+i));
		}
		
		return users;
	}

}
