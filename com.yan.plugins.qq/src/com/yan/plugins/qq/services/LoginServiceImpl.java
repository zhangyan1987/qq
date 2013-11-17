package com.yan.plugins.qq.services;

import com.yan.plugins.qq.daos.IQQDAO;
import com.yan.plugins.qq.daos.QQDAOFileImpl;
import com.yan.plugins.qq.socket.ClientSocket;
import com.yan.qq.common.User;

public class LoginServiceImpl implements ILoginService{

	private IQQDAO qqDAO;
	@Override
	public User CheckLoginInfo(User user,ClientSocket clientSocket) {
		if(clientSocket.login(user))
			return user;
		else 
			return null;
		/*if(qqDAO == null) {
			qqDAO = new QQDAOFileImpl();
		}
		return qqDAO.CheckUserInfo(user);*/
			
	}
	@Override
	public User[] getAllFriendArray() {
		if(qqDAO == null) {
			qqDAO = new QQDAOFileImpl();
		}
		return qqDAO.getAllUsers().toArray(new User[]{});
	}

}
