package com.yan.plugins.qq.model;

public class User {

	private String QQnumber;
	private String password;
	private String nickName;


	public String getPasssword() {
		return password;
	}

	public void setPasssword(String passsword) {
		this.password = passsword;
	}

	public User(String qQnumber, String passsword, String nickName) {
		QQnumber = qQnumber;
		this.password = passsword;
		this.nickName = nickName;
	}

	public String getQQnumber() {
		return QQnumber;
	}

	public void setQQnumber(String qQnumber) {
		QQnumber = qQnumber;
	}

	public User() {
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
