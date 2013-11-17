package com.yan.plugins.qq.services;

import com.yan.plugins.qq.socket.ClientSocket;
import com.yan.qq.common.User;

public interface ILoginService {
	
	public User CheckLoginInfo(User user,ClientSocket clientSocket);
	public User[] getAllFriendArray();
}
