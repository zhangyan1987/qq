package com.yan.plugins.qq.daos;

import java.util.List;

import com.yan.qq.common.User;

public interface IQQDAO {
	
	public User CheckUserInfo(User user);
	
	public List<User> getAllUsers();
}
