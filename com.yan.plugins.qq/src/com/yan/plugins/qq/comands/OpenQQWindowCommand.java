package com.yan.plugins.qq.comands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yan.plugins.qq.main.QQMain;

/**
 * 一定要继承org.eclipse.core.commands.AbstractHandler，不要实现接口IHandler
 * 
 * @author jacobzhang
 * 
 */
public class OpenQQWindowCommand extends AbstractHandler {

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		QQMain qqMain = new QQMain();
		qqMain.run();
		
		return null;
	}

	
	

}
