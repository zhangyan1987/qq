package com.yan.plugins.qq.services;

import com.yan.qq.common.User;

public interface ILoginService {
	
	public User CheckLoginInfo(User user);
	public User[] getAllFriendArray();
}
