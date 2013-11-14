package com.yan.plugins.qq.main;

import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.yan.plugins.qq.comands.QQTableLabelProvider;
import com.yan.plugins.qq.log.Logger;
import com.yan.plugins.qq.services.ILoginService;
import com.yan.plugins.qq.services.LoginServiceImpl;
import com.yan.plugins.qq.socket.ClientSocket;
import com.yan.plugins.qq.util.ImageManager;
import com.yan.qq.common.User;

//TDO: 最大化最小化的问题
public class QQMain {
	// add all to member
	private ILoginService loginService = new LoginServiceImpl();

	private boolean isLogin = false;

	private Display display = null;

	private Shell listShell = null;

	private Shell loginShell = null;

	private Shell msgShell = null;

	private int openedShellCount = 0;

	private TrayItem trayItem;

	private User loginUser;

	// use a Set to manage all shells

	private Set<Shell> ShellManager = new HashSet<Shell>();

	public void run() throws ExecutionException {
		// TODO Auto-generated method stub

		Logger.log("QQ start.......");
		if (display == null)
			display = Display.getCurrent();

		// Tray
		addTray();
		openLoginShell();

		// image.dispose();//when to dispose?
	}

	public void openListShell() {

		if (null == listShell || listShell.isDisposed()) {
			listShell = new Shell();
			ShellManager.add(listShell);
			// change the height to fit the screen height
			int height = (int) Toolkit.getDefaultToolkit().getScreenSize()
					.getHeight();
			listShell.setSize(300, height);
			listShell.setText("QQ :" + loginUser.getNickName());
			// set location
			listShell.setLocation(new Point(0, 0));
			listShell.setImage(ImageManager.getImage(ImageManager.MINI_QQ));
			listShell.setLayout(new FillLayout());

			final TableViewer tableViewer = new TableViewer(listShell,
					SWT.SINGLE | SWT.V_SCROLL);

			tableViewer.setContentProvider(new ArrayContentProvider());
			tableViewer.setLabelProvider(new QQTableLabelProvider());

			tableViewer.setInput(loginService.getAllFriendArray());

			/*
			 * tableViewer.addSelectionChangedListener(new
			 * ISelectionChangedListener() {
			 * 
			 * @Override public void selectionChanged(SelectionChangedEvent
			 * event) { //Logger.log("selectchanged : "+event.getSelection());
			 * 
			 * } });
			 */
			tableViewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					// TODO Auto-generated method stub
					StructuredSelection ss = (StructuredSelection) event
							.getSelection();
					User receiver = (User) ss.getFirstElement();
					Logger.log(receiver.getNickName().trim() + " : "
							+ receiver.getQQnumber().trim());
					openMsgShell(receiver);
				}
			});

			listShell.addShellListener(new ShellAdapter() {

				@Override
				public void shellClosed(ShellEvent e) {
					if (openedShellCount > 0)
						openedShellCount--;
					Logger.log("hide listShell...active shell: "
							+ openedShellCount);
					e.doit = false;
					listShell.setVisible(false);

				}
			});
			listShell.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (openedShellCount > 0)
						openedShellCount--;
					Logger.log("listShell disposed active shell: "
							+ openedShellCount);

				}
			});
			openedShellCount++;
			Logger.log("open listShell...active shell: " + openedShellCount);
			listShell.open();

			while (!listShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} else {

			openedShellCount++;
			Logger.log("open listShell...active shell: " + openedShellCount);
			listShell.setVisible(true);

		}

	}

	// TODO:loginShell location should be in the middle
	public void openLoginShell() {
		if (loginShell == null || loginShell.isDisposed()) {
			loginShell = new Shell();
			loginShell.setSize(330, 240);
			loginShell.setText("QQ");
			loginShell.setImage(ImageManager.getImage(ImageManager.MINI_QQ));
			loginShell.setLayout(new FormLayout());
			ShellManager.add(loginShell);

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
			final Combo qqNumberText = new Combo(loginShell, SWT.BORDER);
			
			//final Text qqNumberText = new Text(loginShell, SWT.BORDER);
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
					loginUser = new User(qqNumberStr, password, null);
					loginUser = loginService.CheckLoginInfo(loginUser);
					if (loginUser != null) {
						isLogin = true;
						loginShell.dispose();
						trayItem.setToolTipText(loginUser.getQQnumber());
						trayItem.setText(loginUser.getQQnumber());
						openListShell();

					} else {
						MessageDialog messageDialog = new MessageDialog(null,
								"login error", ImageManager
										.getImage(ImageManager.MINI_QQ),
								"error", 0, new String[] { "ok" }, 0);
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
					if (openedShellCount > 0)
						openedShellCount--;
					Logger.log("loginShell..dispose..active shell: "
							+ openedShellCount);

				}
			});
			openedShellCount++;
			Logger.log("open loginShell...active shell: " + openedShellCount);
			loginShell.open();

		}

	}

	public void addTray() {

		Tray tray = display.getSystemTray();
		if (null == trayItem || trayItem.isDisposed())
			trayItem = new TrayItem(tray, SWT.NONE);

		trayItem.setImage(ImageManager.getImage(ImageManager.MINI_QQ));
		trayItem.setText("linuxQQ");
		trayItem.setToolTipText("linuxQQ");
		trayItem.setVisible(true);

		// click tray,show a login shell if not login else show list shell
		trayItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Logger.log("click trayItem...active shell: " + openedShellCount);
				// no shell has been opened
				if (openedShellCount <= 0) {

					if (isLogin) {
						openListShell();
					} else {
						openLoginShell();
					}

				}
			}

		});
		final Shell trayShell = new Shell();
		final Menu trayMenu = new Menu(trayShell, SWT.POP_UP);

		MenuItem menuItem = new MenuItem(trayMenu, SWT.PUSH);
		menuItem.setText("quit");
		menuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				for (Shell shell : ShellManager) {
					if (shell != null && !shell.isDisposed())
						shell.dispose();

				}
				if (trayItem != null && !trayItem.isDisposed())
					trayItem.dispose();
				isLogin = false;
				Logger.log("QQ quit...active shell: " + openedShellCount);
			}

		});

		trayItem.addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent e) {
				trayMenu.setVisible(true);
			}
		});

	}

	public void openMsgShell(final User receiver) {
		if (msgShell == null || msgShell.isDisposed()) {
			msgShell = new Shell();
			msgShell.setSize(600, 600);
			StringBuilder sb = new StringBuilder(50);
			sb.append("与 ").append(receiver.getNickName()).append("(")
					.append(receiver.getQQnumber()).append(")").append(" 交谈中");
			msgShell.setText(sb.toString().trim());
			msgShell.setImage(ImageManager.getImage(ImageManager.MINI_QQ));
			msgShell.setLayout(new FormLayout());
			ShellManager.add(msgShell);

			FormData formData = null;
			// send Button
			final Button sendButton = new Button(msgShell, SWT.PUSH);
			sendButton.setText("send");
			formData = new FormData();
			formData.left = new FormAttachment(100, -340);
			formData.right = new FormAttachment(100, -280);
			formData.top = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(100, 0);
			sendButton.setLayoutData(formData);

			// cancel Button
			Button cancelButton = new Button(msgShell, SWT.PUSH);
			cancelButton.setText("cancel");
			formData = new FormData();
			formData.left = new FormAttachment(100, -420);
			formData.right = new FormAttachment(100, -360);
			formData.top = new FormAttachment(100, -30);
			formData.bottom = new FormAttachment(100, 0);
			cancelButton.setLayoutData(formData);

			// sendText//SWT.WRAP自动换行
			final Text sendText = new Text(msgShell, SWT.BORDER | SWT.MULTI
					| SWT.WRAP | SWT.V_SCROLL | SWT.BORDER_SOLID);
			formData = new FormData();
			formData.left = new FormAttachment(0, 30);
			formData.right = new FormAttachment(100, -270);
			formData.top = new FormAttachment(0, 300);
			formData.bottom = new FormAttachment(0, 540);
			sendText.setLayoutData(formData);
			
			// recordText
			final Text recordText = new Text(msgShell, SWT.BORDER | SWT.MULTI
					| SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
			formData = new FormData();
			formData.left = new FormAttachment(0, 30);
			formData.right = new FormAttachment(100, -270);
			formData.top = new FormAttachment(0, 39);
			formData.bottom = new FormAttachment(0, 279);
			recordText.setLayoutData(formData);

			final Label imageSend = new Label(msgShell, SWT.CENTER);
			formData = new FormData();
			formData.left = new FormAttachment(100, -250);
			formData.right = new FormAttachment(100, -20);
			formData.top = new FormAttachment(0, 300);
			formData.bottom = new FormAttachment(0, 540);
			imageSend.setImage(ImageManager.getImage(ImageManager.QQ));
			imageSend.setLayoutData(formData);

			final Label imageRecv = new Label(msgShell, SWT.CENTER);
			formData = new FormData();
			formData.left = new FormAttachment(100, -250);
			formData.right = new FormAttachment(100, -20);
			formData.top = new FormAttachment(0, 39);
			formData.bottom = new FormAttachment(0, 279);
			imageRecv.setImage(ImageManager.getImage(ImageManager.QQ));
			imageRecv.setLayoutData(formData);

			// main label
			Label mainLabel = new Label(msgShell, SWT.CENTER);
			mainLabel.setText(sb.toString().trim());
			mainLabel.setAlignment(SWT.LEFT);
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			formData.bottom = new FormAttachment(0, 18);
			mainLabel.setLayoutData(formData);

			// event

			final SelectionListener sendButtonListener = new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					String sendStr = sendText.getText();
					if (null == sendStr || sendStr.trim().equals("")
							|| sendStr.trim().length() == 0) {
						e.doit = false;
						return;
					}
					
					
					StringBuilder sendMsg = new StringBuilder();
					
					DateFormat df = new SimpleDateFormat("HH:mm:ss",
							Locale.CHINESE);
					
					//make sendMsg
					sendMsg.append(loginUser.getQQnumber()).append("::").append(receiver.getQQnumber()).append("\n");
					sendMsg.append(loginUser.getNickName()).append("(")
					.append(loginUser.getQQnumber()).append(")  ")
					.append(df.format(new Date()));
					sendMsg.append("\n").append(sendStr);
					
					ClientSocket cs = new ClientSocket(sendMsg.toString());
					String result = cs.send();
					StringBuilder sb = new StringBuilder();
					sb.append(recordText.getText()).append("\n");
					sb.append(loginUser.getNickName()).append("(")
							.append(loginUser.getQQnumber()).append(")  ")
							.append(df.format(new Date()));
					sb.append("\n").append(sendStr).append("\n").append(result);
					sendText.setText("");
					recordText.setText(sb.toString());
				}
			};

			sendButton.addSelectionListener(sendButtonListener);
			sendText.addKeyListener(new KeyListener() {

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// if enter
					System.out.println(e.keyCode);
					if (e.keyCode == 13) {
						e.doit = false;
						String sendStr = sendText.getText();
						if (null == sendStr || sendStr.trim().equals("")
								|| sendStr.trim().length() == 0) {
							e.doit = false;
							return;
						}

						DateFormat df = new SimpleDateFormat("HH:mm:ss",
								Locale.CHINESE);
						StringBuilder sb = new StringBuilder();
						sb.append(recordText.getText()).append("\n");
						sb.append(loginUser.getNickName()).append("(")
								.append(loginUser.getQQnumber()).append(")  ")
								.append(df.format(new Date()));
						sb.append("\n").append(sendStr);
						sendText.setText("");
						recordText.setText(sb.toString());
					}

				}
			});

			openedShellCount++;
			Logger.log("open msgShell...active shell: " + openedShellCount);
			msgShell.open();
			msgShell.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					Logger.log("msgShell..disposed..active shell: "
							+ openedShellCount);
					if (openedShellCount > 0)
						openedShellCount--;
				}
			});
		}

	}

}
