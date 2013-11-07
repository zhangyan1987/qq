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
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.yan.plugins.qq.Activator;
import com.yan.plugins.qq.log.Logger;
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

	// add all to member
	private ILoginService loginService = new LoginServiceImpl();

	private boolean isLogin = false;

	private Display display = null;

	private Shell listShell = null;

	private Shell loginShell = null;

	private Image image = null;

	private int openedShellCount = 0;

	private TrayItem trayItem;
	public void loadImage() {

		if (image == null) {
			Logger.log("load image from /icons/minqq.jpg ");
			image = new Image(display,
					Activator.class.getResourceAsStream("/icons/minqq.jpg"));
		}
			
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		Logger.log("QQ start.......");
		if (display == null)
			display = Display.getCurrent();
		loadImage();
		

		// Tray
		addTray();
		openLoginShell();

		// image.dispose();//when to dispose?
		return null;
	}

	public void openListShell() {
		
		if (null == listShell || listShell.isDisposed()) {
			listShell = new Shell();
			// change the height to fit the screen height
			int height = (int) Toolkit.getDefaultToolkit().getScreenSize()
					.getHeight();
			listShell.setSize(300, height);
			listShell.setText("QQ");
			// set location
			listShell.setLocation(new Point(0, 0));
			listShell.setImage(image);
			listShell.setLayout(new FillLayout());

			final TableViewer tableViewer = new TableViewer(listShell,
					SWT.SINGLE | SWT.V_SCROLL);

			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setLabelProvider(new QQTableLabelProvider());

			tableViewer.setInput(loginService.getAllFriendArray());

			listShell.addShellListener(new ShellAdapter() {

				@Override
				public void shellClosed(ShellEvent e) {
					if(openedShellCount > 0)
						openedShellCount--;
					Logger.log("hide listShell...active shell: "+openedShellCount);
					e.doit = false;
					listShell.setVisible(false);

				}
			});
			listShell.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					if(openedShellCount > 0)
						openedShellCount--;
					Logger.log("listShell disposed active shell: "+openedShellCount);
					

				}
			});
			openedShellCount++;
			Logger.log("open listShell...active shell: "+openedShellCount);
			listShell.open();
			
			while (!listShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} else {
			
			openedShellCount++;
			Logger.log("open listShell...active shell: "+openedShellCount);
			listShell.setVisible(true);
			
		}

	}

	public void openLoginShell() {
		if (loginShell == null || loginShell.isDisposed()) {
			loginShell = new Shell();
			loginShell.setSize(330, 240);
			loginShell.setText("QQ");
			loginShell.setImage(image);
			loginShell.setLayout(new FormLayout());

			FormData formData = null;
			// login Button
			Button loginButton = new Button(loginShell, SWT.PUSH);
			loginButton.setText("login");
			formData = new FormData();
			formData.left = new FormAttachment(0, 60);
			formData.right = new FormAttachment(0, 135);
			formData.top = new FormAttachment(0, 150);
			formData.bottom = new FormAttachment(0, 180);
			loginButton.setLayoutData(formData);

			// register Button
			Button registerButton = new Button(loginShell, SWT.PUSH);
			registerButton.setText("register");
			formData = new FormData();
			formData.left = new FormAttachment(0, 195);
			formData.right = new FormAttachment(100, -60);
			formData.top = new FormAttachment(0, 150);
			formData.bottom = new FormAttachment(0, 180);
			registerButton.setLayoutData(formData);

			// password label
			Label passwordLabel = new Label(loginShell, SWT.NORMAL);
			passwordLabel.setText("password");
			formData = new FormData();
			formData.left = new FormAttachment(0, 30);
			formData.right = new FormAttachment(0, 95);
			formData.top = new FormAttachment(0, 105);
			formData.bottom = new FormAttachment(0, 135);
			passwordLabel.setLayoutData(formData);

			// passwordText

			final Text passwordText = new Text(loginShell, SWT.BORDER
					| SWT.PASSWORD);
			formData = new FormData();
			formData.left = new FormAttachment(0, 120);
			formData.right = new FormAttachment(100, -30);
			formData.top = new FormAttachment(0, 105);
			formData.bottom = new FormAttachment(0, 135);
			passwordText.setLayoutData(formData);

			// QQNumber label
			Label userLabel = new Label(loginShell, SWT.NORMAL);
			userLabel.setText("QQ");
			formData = new FormData();
			formData.left = new FormAttachment(0, 30);
			formData.right = new FormAttachment(0, 95);
			formData.top = new FormAttachment(0, 60);
			formData.bottom = new FormAttachment(0, 90);
			userLabel.setLayoutData(formData);

			// QQNumberText
			final Text qqNumberText = new Text(loginShell, SWT.BORDER);
			formData = new FormData();
			formData.left = new FormAttachment(0, 120);
			formData.right = new FormAttachment(100, -30);
			formData.top = new FormAttachment(0, 60);
			formData.bottom = new FormAttachment(0, 90);
			qqNumberText.setLayoutData(formData);

			// main label
			Label mainLabel = new Label(loginShell, SWT.CENTER);
			mainLabel.setText("Welcome to Linux QQ");
			formData = new FormData();
			formData.top = new FormAttachment(0, 5);
			formData.left = new FormAttachment(0, 5);
			formData.right = new FormAttachment(100, -5);
			formData.bottom = new FormAttachment(userLabel, -5);
			mainLabel.setLayoutData(formData);

			// event
			loginButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					String qqNumberStr = qqNumberText.getText();
					String password = passwordText.getText();
					User loginUser = new User(qqNumberStr, password, null);
					isLogin = loginService.CheckLoginInfo(loginUser);
					if (isLogin) {
						loginShell.dispose();
						openListShell();
						
					} else {
						MessageDialog messageDialog = new MessageDialog(null,
								"login error", image, "error", 0,
								new String[] { "ok" }, 0);
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
					if(openedShellCount > 0)
						openedShellCount--;
					Logger.log("loginShell..dispose..active shell: "+openedShellCount);
					
				}
			});
			openedShellCount++;
			Logger.log("open loginShell...active shell: "+openedShellCount);
			loginShell.open();
			
			/*while (!loginShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}*/
		}

	}
	
	
	public void addTray() {
		
		Tray tray = display.getSystemTray();
		if(null == trayItem || trayItem.isDisposed())
			trayItem = new TrayItem(tray, SWT.NONE);
		
		trayItem.setImage(image);
		trayItem.setText("linuxQQ");
		trayItem.setToolTipText("linuxQQ");
		trayItem.setVisible(true);

		// click tray,show a login shell if not login else show list shell
		trayItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Logger.log("click trayItem...active shell: "+openedShellCount);
				// no shell has been opened
				if (openedShellCount <= 0) {

					if (isLogin) {
						openListShell();
					}
					else {
						openLoginShell();
					}

				}
			}
			
		});
		final Shell trayShell = new Shell();
		final Menu trayMenu = new Menu(trayShell,SWT.POP_UP);
		
		MenuItem menuItem = new MenuItem(trayMenu, SWT.PUSH);
		menuItem.setText("quit");
		menuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(loginShell!=null && !loginShell.isDisposed()) {
					loginShell.dispose();
				}
					
				if(listShell!=null && !listShell.isDisposed()) {
					listShell.dispose();
				}
					
				if(trayItem!=null && !trayItem.isDisposed()) 
					trayItem.dispose();
				
				Logger.log("QQ quit...active shell: "+openedShellCount);
			}

			
			
		});
		
		
		trayItem.addMenuDetectListener(new MenuDetectListener() {
			
			@Override
			public void menuDetected(MenuDetectEvent e) {
				trayMenu.setVisible(true);
			}
		});
		
		
	}

}
