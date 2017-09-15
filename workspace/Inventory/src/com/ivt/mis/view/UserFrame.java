package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.User;
import com.ivt.mis.service.UserService;
import com.ivt.mis.view.validator.UserValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class UserFrame extends BaseJInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7087228002569265793L;
	JTabbedPane power_user;
	JTextField text_login_name;
	JTextField text_user_name;
	JPasswordField text_password;
	JPasswordField text_repassword;
	JButton addBtn;
	JButton addCancelBtn;
	JButton deleteBtn;
	JButton delCancelBtn;
	JButton loginNameCheckBtn;
	int count = 0;
	JPanel add_panel;
	JPanel delete_panel;
	JTextField del_user_name;
	JTextField text_del_power;
	JComboBox jcbpower, jcbname;

	private User buildUser() {
		User user = new User();
		user.setId(text_login_name.getText());
		user.setLoginName(text_login_name.getText());
		user.setName(text_user_name.getText());
		user.setPassword(String.valueOf(text_password.getPassword()));
		user.setPower(jcbpower.getSelectedItem().toString());
		user.setPasswordConfirm(String.valueOf(text_repassword.getPassword()));
		return user;
	}

	public UserFrame() {
		super("操作员管理", true, true, false, true);
		super.setFrameId(Constants.MODULE_USER);
		power_user = new JTabbedPane();
		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(490, 190);
		this.setResizable(false);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		text_login_name = new JTextField(11);
		text_user_name = new JTextField(20);
		text_password = new JPasswordField(20);
		text_repassword = new JPasswordField(20);
		text_password.setEchoChar('*');
		text_repassword.setEchoChar('*');

		jcbpower = new JComboBox();
		jcbpower.addItem("操作员");
		jcbpower.addItem("管理员");

		loginNameCheckBtn = new JButton("检测用户名");
		addBtn = new JButton("添加");
		addCancelBtn = new JButton("取消");

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.

		User fields = new User();
		CellConstraints cc = new CellConstraints();
//		builder.append(new JLabel("登录名:"));
		builder.append(fields.getFieldDefn("loginName").getDescrLong());
		builder.append(text_login_name);
		builder.append(loginNameCheckBtn,2);
		builder.nextLine();
//		builder.append(new JLabel("姓名:"));
		builder.append(fields.getFieldDefn("name").getDescrLong());
		builder.append(text_user_name);
//		builder.append(new JLabel("密码:"));
		builder.append(fields.getFieldDefn("password").getDescrLong());
		builder.append(text_password);
		builder.nextLine();
//		builder.append(new JLabel("确认密码:"));
		builder.append(fields.getFieldDefn("passwordConfirm").getDescrLong());
		builder.append(text_repassword);
//		builder.append(new JLabel("权限设置:"));
		builder.append(fields.getFieldDefn("power").getDescrLong());
		builder.append(jcbpower);
		builder.nextLine();

		add_panel = new JPanel();
		delete_panel = new JPanel();

		jcbname = new JComboBox();

		add_panel.setLayout(new FlowLayout());
		add_panel.add(builder.getPanel());
		add_panel.add(addBtn);
		add_panel.add(addCancelBtn);

		// 删除TAB
		text_del_power = new JTextField(10);
		text_del_power.setEditable(false);
		del_user_name = new JTextField(10);
		del_user_name.setEditable(false);

		deleteBtn = new JButton("删除");
		delCancelBtn = new JButton("取消");

		DefaultFormBuilder builderDel = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.

		CellConstraints ccDel = new CellConstraints();
		builderDel.append(new JLabel("操作员:"));
		builderDel.append(jcbname);
		builderDel.nextLine();
//		builderDel.append(new JLabel("姓名:"));
		builderDel.append(fields.getFieldDefn("name").getDescrLong());
		builderDel.append(del_user_name);
//		builderDel.append(new JLabel("权限:"));
		builderDel.append(fields.getFieldDefn("power").getDescrLong());
		builderDel.append(text_del_power);
		delete_panel.add(builderDel.getPanel());
		delete_panel.add(deleteBtn);
		delete_panel.add(delCancelBtn);

		add_panel.setVisible(true);
		delete_panel.setVisible(true);

		power_user.addTab("添加操作员", add_panel);
		power_user.addTab("删除操作员", delete_panel);
		jcbname.addItem("请选择用户");
		this.getContentPane().add(power_user);

		loginNameCheckBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String loginnameString = text_login_name.getText();
				if (loginnameString.trim().length() == 0) {
					showWarningMsg("用户名不能为空");
				} else {
					UserService user = CommonFactory.getUserService();
					if (user.isExited(loginnameString)) {
						showWarningMsg("用户名已存在");
					} else {
						showInfoMsg("恭喜你，该用户名可用");
					}
				}

			}
		});

		addBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				User user = buildUser();

				UserService userService = CommonFactory.getUserService();
				UserValidator userValidator = new UserValidator(user,
						userService, Constants.FORM_TYPE_ADD);

				String errorString = userValidator.getErrorString();
				if (errorString != null) {
					showWarningMsg(errorString);
					return;
				}
				if (userService.saveUser(user)) {
					showInfoMsg("创建成功！");
				} else {
					showErrorMsg("创建失败，请重新输入！");
				}
			}
		});
		addCancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		deleteBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				UserService user = CommonFactory.getUserService();
				String loginnameString = jcbname.getSelectedItem().toString();
				if (loginnameString.equals("请选择用户")) {
					showWarningMsg("请选择用户！");
				} else {
					if (showConfirmDialog() == JOptionPane.YES_OPTION) {
						if (user.removeUser(loginnameString)) {
							showInfoMsg("删除成功！");
							jcbname.removeAllItems();
							jcbname.addItem("请选择用户");
							del_user_name.setText("");
							text_del_power.setText("");
							Vector<User> usersVector = user.retrieveUsers("操作员");
							for (User o : usersVector) {
								jcbname.addItem(o.getLoginName());
							}
						} else {
							showErrorMsg("对不起，删除失败！");
						}
					} else {
						dispose();
					}
				}

			}
		});
		delCancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		power_user.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPanel = (JTabbedPane) e.getSource();

				if (((JPanel) tabbedPanel.getSelectedComponent())
						.equals(delete_panel)) {
					jcbname.removeAllItems();
					jcbname.addItem("请选择用户");
					UserService user = CommonFactory.getUserService();
					Vector<User> usersVector = user.retrieveUsers("操作员");
					for (User o : usersVector) {
						jcbname.addItem(o.getLoginName());
					}
				}
			}
		});

		jcbname.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				UserService user = CommonFactory.getUserService();
				if (jcbname.getSelectedItem() != null) {
					User oprt = user.getUserInfo(jcbname.getSelectedItem()
							.toString());
					if (oprt != null) {
						del_user_name.setText(oprt.getName());
						text_del_power.setText(oprt.getPower());
					}
				}
			}
		});
	}
}
