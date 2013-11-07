package com.yan.plugins.qq.comands;

import java.awt.Toolkit;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.yan.plugins.qq.Activator;
import com.yan.plugins.qq.model.User;
import com.yan.plugins.qq.services.ILoginService;
import com.yan.plugins.qq.services.LoginServiceImpl;

/**
 * 一定要继承org.eclipse.core.commands.AbstractHandler，不要实现接口IHandler
 * 
 * @author jacobzhang
 * 
 */
public class OpenQQWindowCommand extends AbstractHandler {
	
	private ILoginService loginService = new LoginServiceImpl();
	
	private boolean isLogin = false;
	
	private Display display = null;
	
	private Shell listShell = null;
	
	private Shell loginShell = null;
	
	private Image image = null;
	
	private int openedShellCount = 0;
	
	public void loadImage() {
		
		if(image == null)
			image = new Image(display, Activator.class.getResourceAsStream("/icons/minqq.jpg"));
	}
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		System.out.println("QQ is running.............");
		if(display == null)
			display = Display.getCurrent();
		if(loginShell == null || loginShell.isDisposed()) 
			loginShell = new Shell();
		
		
		
		//TODO Add tray
		//Tray
		Tray tray = display.getSystemTray();
		TrayItem trayItem = new TrayItem(tray, SWT.NONE);
		loadImage();
		trayItem.setImage(image);
		trayItem.setText("QQ");
		trayItem.setToolTipText("QQ");
		trayItem.setVisible(true);
		
		trayItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				if(openedShellCount<=0) 
					openListShell();
				/*Shell listShell = new Shell(display);
				listShell.setSize(300,1000);
				listShell.setText("QQ");
				listShell.setImage(image);
				listShell.setLayout(new FillLayout());
				
				final TableViewer tableViewer = new TableViewer(listShell, SWT.SINGLE|SWT.V_SCROLL);
				
				tableViewer.setContentProvider(new ArrayContentProvider());
				tableViewer.setLabelProvider(new QQTableLabelProvider());
				
				tableViewer.setInput(loginService.getAllFriendArray());
				listShell.open();
				while (!listShell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}*/
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetDefaultSelected(e);
			}
		});
		
		
		loginShell.setSize(330,240);
		loginShell.setText("QQ");
		loginShell.setImage(image);
		loginShell.setLayout(new FormLayout());
		
		
		
		FormData formData = null;
		//login Button
		Button loginButton = new Button(loginShell, SWT.PUSH);
		loginButton.setText("login");
		formData = new FormData();
		formData.left = new FormAttachment(0,60);
		formData.right = new FormAttachment(0,135);
		formData.top = new FormAttachment(0,150);
		formData.bottom = new FormAttachment(0,180);
		loginButton.setLayoutData(formData);
		
		
		
		//register Button
		Button registerButton = new Button(loginShell, SWT.PUSH);
		registerButton.setText("register");
		formData = new FormData();
		formData.left = new FormAttachment(0,195);
		formData.right = new FormAttachment(100,-60);
		formData.top = new FormAttachment(0,150);
		formData.bottom = new FormAttachment(0,180);
		registerButton.setLayoutData(formData);
		
		//password label
		Label passwordLabel = new Label(loginShell, SWT.NORMAL);
		passwordLabel.setText("password");
		formData = new FormData();
		formData.left = new FormAttachment(0,30);
		formData.right = new FormAttachment(0,95);
		formData.top = new FormAttachment(0,105);
		formData.bottom = new FormAttachment(0,135);
		passwordLabel.setLayoutData(formData);
		
		//passwordText
		
		final Text passwordText = new Text(loginShell, SWT.BORDER|SWT.PASSWORD);
		formData = new FormData();
		formData.left = new FormAttachment(0,120);
		formData.right = new FormAttachment(100,-30);
		formData.top = new FormAttachment(0,105);
		formData.bottom = new FormAttachment(0,135);
		passwordText.setLayoutData(formData);

		
		
		//QQNumber label
		Label userLabel = new Label(loginShell, SWT.NORMAL);
		userLabel.setText("QQ");
		formData = new FormData();
		formData.left = new FormAttachment(0,30);
		formData.right = new FormAttachment(0,95);
		formData.top = new FormAttachment(0,60);
		formData.bottom = new FormAttachment(0,90);
		userLabel.setLayoutData(formData);
		
		//QQNumberText
		final Text qqNumberText = new Text(loginShell, SWT.BORDER);
		formData = new FormData();
		formData.left = new FormAttachment(0,120);
		formData.right = new FormAttachment(100,-30);
		formData.top = new FormAttachment(0,60);
		formData.bottom = new FormAttachment(0,90);
		qqNumberText.setLayoutData(formData);
		
		
		

		
		
		
		//main label
		Label mainLabel = new Label(loginShell, SWT.CENTER);
		mainLabel.setText("Welcome to Linux QQ");
		formData = new FormData();
		formData.top = new FormAttachment(0,5);
		formData.left = new FormAttachment(0,5);
		formData.right = new FormAttachment(100,-5);
		formData.bottom = new FormAttachment(userLabel,-5);
		mainLabel.setLayoutData(formData);
		
		//event
		loginButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String qqNumberStr = qqNumberText.getText();
				String password = passwordText.getText();
				User loginUser = new User(qqNumberStr, password, null);
				isLogin = loginService.CheckLoginInfo(loginUser);
				if(isLogin) {
					
					loginShell.dispose();
					openListShell();
					//image.dispose();
					
					/*MessageDialog messageDialog = new MessageDialog(null, "login success", image, "success", 0, new String[]{"do"}, 0);
					messageDialog.open();*/
					
				}
				else {
					MessageDialog messageDialog = new MessageDialog(null, "login error", image, "error", 0, new String[]{"do"}, 0);
					messageDialog.open();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetDefaultSelected(e);
			}
		});
		
		loginShell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {

				openedShellCount--;
			}
		});
		loginShell.open();
		openedShellCount++;
		while (!loginShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//image.dispose();//when to dispose?
		return null;
	}



	public void openListShell(){
		if(null == listShell || listShell.isDisposed())
			listShell = new Shell();
		//change the height to fit the screen height
		int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		listShell.setSize(300,height);
		listShell.setText("QQ");
		//set location
		listShell.setLocation(new Point(0, 0));
		loadImage();
		listShell.setImage(image);
		listShell.setLayout(new FillLayout());
		
		final TableViewer tableViewer = new TableViewer(listShell, SWT.SINGLE|SWT.V_SCROLL);
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new QQTableLabelProvider());
		
		tableViewer.setInput(loginService.getAllFriendArray());
		listShell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				openedShellCount--;
			}
		});
		
		listShell.open();
		openedShellCount++;
		while (!listShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
}
