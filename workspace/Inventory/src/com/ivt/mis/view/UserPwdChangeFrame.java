package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.common.swing.LimitedDocument;
import com.ivt.mis.model.User;
import com.ivt.mis.service.UserService;
import com.ivt.mis.view.validator.UserValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class UserPwdChangeFrame extends BaseJInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6722051188152785053L;
	JLabel label_user, label_old_password, label_new_password,
			label_repassword;
	JTextField loginName;
	JPasswordField old_password, new_password, repassword;
	JButton change, cancel;

	public UserPwdChangeFrame() {
		super("更改密码", true, true, false, true);
		super.setFrameId(Constants.MODULE_USER_PWD);
		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(500, 140);
		this.setResizable(false);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		
		loginName = new JTextField(10);
		loginName.setEditable(false);
		loginName.setText(MainFrame.loginname);

		old_password = new JPasswordField(10);
		old_password.setDocument(new LimitedDocument(32));
		new_password = new JPasswordField(10);
		new_password.setDocument(new LimitedDocument(32));
		repassword = new JPasswordField(10);
		repassword.setDocument(new LimitedDocument(32));
		old_password.setEchoChar('*');
		new_password.setEchoChar('*');
		repassword.setEchoChar('*');

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		// Fill the table with labels and components.
		CellConstraints cc = new CellConstraints();
		builder.append(new JLabel("用户名:"));
		builder.append(loginName);
		builder.append(new JLabel("旧密码:"));
		builder.append(old_password);
		builder.nextLine();
		builder.append(new JLabel("新密码:"));
		builder.append(new_password);
		builder.append(new JLabel("确认密码:"));
		builder.append(repassword);

		change = new JButton("修改");

		change.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String oldPasswordString = String.valueOf(old_password
						.getPassword());
				String newPasswordString = String.valueOf(new_password
						.getPassword());
				String rePasswordString = String.valueOf(repassword
						.getPassword());
				User user = new User();
				user.setLoginName(MainFrame.loginname);
				user.setPasswordOld(oldPasswordString);
				user.setPassword(newPasswordString);
				user.setPasswordConfirm(rePasswordString);

				UserService userService = CommonFactory.getUserService();
				UserValidator userValidator = new UserValidator(user,
						userService, Constants.FORM_TYPE_PWD_CHG);
				String errorString = userValidator.getErrorString();
				if (errorString != null) {
					showWarningMsg(errorString);
					return;
				}

				if (userService.modifyPassword(MainFrame.loginname,
						newPasswordString)) {
					showInfoMsg("恭喜你，密码已修改成功");
					dispose();
				} else {
					showErrorMsg("密码修改失败");
				}

			}
		});
		cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

		this.setLayout(new FlowLayout());
		this.add(builder.getPanel());

		this.add(change);
		this.add(cancel);
	}
}
