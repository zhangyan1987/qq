package com.yan.plugins.qq.services;

import com.yan.plugins.qq.model.User;

public interface ILoginService {
	
	public boolean CheckLoginInfo(User user);
	public User[] getAllFriendArray();
}
