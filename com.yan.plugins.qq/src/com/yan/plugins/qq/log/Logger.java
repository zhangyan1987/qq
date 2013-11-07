package com.yan.plugins.qq.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

	public static void log(String str){
		StringBuilder sb = new StringBuilder();
		DateFormat df=new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss",Locale.CHINESE); 
		sb.append("====== QQ LOG: [ ").append(df.format(new Date())).append("  : ").append(String.format("%-80s",str)).append(" ]======");
		System.err.println(sb.toString());
	}
}
