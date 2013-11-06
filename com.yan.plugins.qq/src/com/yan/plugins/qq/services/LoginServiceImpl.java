package com.yan.plugins.qq.services;

import com.yan.plugins.qq.daos.IQQDAO;
import com.yan.plugins.qq.daos.QQDAOImpl;
import com.yan.plugins.qq.model.User;

public class LoginServiceImpl implements ILoginService{

	private IQQDAO qqDAO;
	@Override
	public boolean CheckLoginInfo(User user) {
		
		if(qqDAO == null) {
			qqDAO = new QQDAOImpl();
		}
		return qqDAO.CheckUserInfo(user);
			
	}
	@Override
	public User[] getAllFriendArray() {
		if(qqDAO == null) {
			qqDAO = new QQDAOImpl();
		}
		return qqDAO.getAllUsers().toArray(new User[]{});
	}

}