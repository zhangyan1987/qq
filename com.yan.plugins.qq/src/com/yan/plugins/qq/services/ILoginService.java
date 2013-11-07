package com.yan.plugins.qq.services;

import com.yan.plugins.qq.model.User;

public interface ILoginService {
	
	public User CheckLoginInfo(User user);
	public User[] getAllFriendArray();
}
