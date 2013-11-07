package com.yan.plugins.qq.log;

import java.util.Date;

public class Logger {

	public static void log(String str){
		StringBuilder sb = new StringBuilder();
		sb.append("====== QQ LOG: [ ").append(new Date().toString()).append("  : ").append(String.format("%-80s",str)).append(" ]======");
		System.err.println(sb.toString());
	}
}
