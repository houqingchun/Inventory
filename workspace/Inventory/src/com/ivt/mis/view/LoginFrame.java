package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.LicenseVerify;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;
import com.ivt.mis.service.UserService;

public class LoginFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3771685943392837747L;
	JTextField loginname;
	JPasswordField password;
	JButton submit;
	JButton cancel;

	public LoginFrame() {
		super("库存小帮手");		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 3, screenSize.height / 3, 330, 250);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Image image = null;
		try {
			image = ImageIO.read(LoginFrame.class
					.getResource(Constants.TASK_ICON_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		} // 创建图片对象
		this.setIconImage(image);// 设置图标

		submit = new JButton("确定");
		submit.addActionListener(this);

		cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		loginLayout();
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		String loginnameString = loginname.getText();
		String passwordString = String.valueOf(password.getPassword());

		if (loginnameString.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "用户名不能为空！", "警告",
					JOptionPane.WARNING_MESSAGE);
		} else if (passwordString.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "密码不能为空！", "警告",
					JOptionPane.WARNING_MESSAGE);
		} else {
			UserService user = CommonFactory.getUserService();
			boolean isPass = user.loginCheck(loginnameString, passwordString);
			if (isPass) {
				Constants.LICENSE_IS_TRIAL = Boolean.valueOf(LicenseVerify
						.getSNValue()[3]);

				if (Constants.LICENSE_IS_TRIAL) {
					Constants.TOOLS_TITLE_PREFIX = "试用版-";
				}

				// 取得客户相关信息，公司名称显示在本软件标题栏
				CustomizationService customizationService = CommonFactory
						.getCustomizationService();
				Customization customization = customizationService
						.getCustomizationInfo();
				if (customization != null) {
					Constants.COMPANY_INFO_IS_MAINTAINED = true;
					Constants.TOOLS_TITLE_SUFFIX = "-"
							+ customization.getCompanyName();
				}

				MainFrame.loginname = loginnameString;
				MainFrame.power = user.getPower(loginnameString);
				MainFrame.getMainFrame().setVisible(true);
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, "登陆失败,用户名或密码错误！", "警告",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/**
	 * 窗体布局
	 */
	private void loginLayout() {
		JLabel name;
		JLabel passwordLabel;
		JPanel panel_center, panel_south;
		JLabel label;
		name = new JLabel("用户名:  ", JLabel.RIGHT);
		name.setForeground(new Color(0, 128, 255));
		passwordLabel = new JLabel("密码:  ", JLabel.RIGHT);
		passwordLabel.setForeground(new Color(0, 128, 255));
		loginname = new JTextField();
		loginname.setColumns(10);
		password = new JPasswordField();
		password.setColumns(10);
		password.setEchoChar('*');
		panel_center = new JPanel();
		panel_center.setLayout(new GridLayout(3, 1));
		panel_south = new JPanel();
		this.setLayout(new BorderLayout());
		this.setContentPane(new JPanel() {
			public void paintComponent(Graphics g) {
				setDoubleBuffered(true);
				g.drawImage(
						new ImageIcon(
								LoginFrame.class
										.getResource("/com/ivt/mis/view/images/login.jpg"))
								.getImage(), 0, 0, null);
			}
		});
		for (int i = 0; i < 13; i++) {
			label = new JLabel();
			label.setPreferredSize(new Dimension(600, 1));
			this.getContentPane().add(label, BorderLayout.NORTH);
		}

		// 注入回车事件登录
		password.registerKeyboardAction(this,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
				JComponent.WHEN_FOCUSED);

		panel_center.add(name);
		panel_center.add(loginname);
		panel_center.add(passwordLabel);
		panel_center.add(password);
		label = new JLabel();
		label.setPreferredSize(new Dimension(70, 1));
		panel_south.add(label);
		panel_south.add(submit);
		panel_south.add(cancel);
		panel_center.setOpaque(false);
		panel_south.setOpaque(false);
		this.getContentPane().add(panel_center, BorderLayout.EAST);
		this.getContentPane().add(panel_south, BorderLayout.SOUTH);
	}
}
