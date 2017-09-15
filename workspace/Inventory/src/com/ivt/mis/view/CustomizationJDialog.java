package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ivt.mis.common.BasicTypeUtils;
import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.LicenseVerify;
import com.ivt.mis.common.MailUtil;
import com.ivt.mis.common.RuntimeUtil;
import com.ivt.mis.common.swing.IvtJTextField;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;
import com.ivt.mis.view.validator.CustomizationValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class CustomizationJDialog extends BaseJDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8719497764925469726L;
	public static final Logger logger = Logger
			.getLogger(CustomizationJDialog.class);

	Customization customization;
	JTextField companyNameField;
	JTextField expiredDaysField;
	JTextField addressField;
	JTextField logoPathField;
	JTextField contactsField;
	JTextField mobileField;
	JTextField emailField;
	JTextField faxField;
	JTextField signatureField;
	JTextField commentsField;
	JTextField snField;
	JTextField createTimeField;
	JCheckBox isTrialBox;

	public CustomizationJDialog() {
		super(new JFrame(), "配置信息管理", true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(
					Constants.TASK_ICON_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		} // 创建图片对象
		this.setIconImage(image);// 设置图标

		CustomizationJPanel customizationJPanel = new CustomizationJPanel();
		this.getContentPane().add(customizationJPanel);
		customizationJPanel.setOpaque(true);
		
		this.setSize(450, 400);
		setLocationRelativeTo(null);

		// Display the window.
		pack();
		setVisible(true);
	}

	class CustomizationJPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4237195457685974182L;

		public CustomizationJPanel() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			/*
			 * 建立客户添加信息页面
			 */
			companyNameField = new IvtJTextField(50);
			addressField = new IvtJTextField(100);
			logoPathField = new IvtJTextField(100);
			snField = new IvtJTextField(6);
			contactsField = new IvtJTextField(50);
			mobileField = new IvtJTextField(20);
			faxField = new IvtJTextField(20);
			emailField = new IvtJTextField(100);
			commentsField = new IvtJTextField(100);
			expiredDaysField = new IvtJTextField(10);
			createTimeField = new IvtJTextField(20);
			snField = new IvtJTextField(255);
			signatureField = new IvtJTextField(255);
			isTrialBox = new JCheckBox("试用版");

			JPanel addPanel = new JPanel();

			DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();

			// Fill the table with labels and components.
			CellConstraints cc = new CellConstraints();
			builder.append(new JLabel("版本性质"));
			builder.append(isTrialBox);
			builder.nextLine();
			builder.append(new JLabel("公司名称"));
			builder.append(companyNameField,5);
			builder.nextLine();
			builder.append(new JLabel("联系地址"));
			builder.append(addressField,5);
			builder.nextLine();
			builder.append(new JLabel("联系人"));
			builder.append(contactsField);
			builder.append(new JLabel("手机"));
			builder.append(mobileField);
			builder.nextLine();
			builder.append(new JLabel("邮箱"));
			builder.append(emailField);
			builder.append(new JLabel("传真"));
			builder.append(faxField);
			builder.nextLine();
			builder.append(new JLabel("业务备注"));
			builder.append(commentsField,5);

			// 添加到主Panel内
			addPanel.add(builder.getPanel());

			// 装载已有数据
			CustomizationService customizationService = CommonFactory
					.getCustomizationService();
			if (customizationService.isExited()) {
				customization = customizationService.getCustomizationInfo();
				editType = Constants.FORM_TYPE_UPDATE;
			} else {
				customization = new Customization();
				editType = Constants.FORM_TYPE_ADD;
			}

			// 根据编辑类型不同，生成不同按扭
			JPanel buttonsPanel = createCRUDButtons(new AddButtonListener(),
					new UpdateButtonListener(), null);

			// 装载数据
			populateForm();

			addPanel.setAlignmentY(10f);

			add(addPanel);
			add(buttonsPanel);
		}

	}

	public void populateForm() {
		expiredDaysField.setEditable(false);
		isTrialBox.setEnabled(false);
		if (Constants.FORM_TYPE_UPDATE.equals(editType)) {
			companyNameField.setText(customization.getCompanyName());
			expiredDaysField.setText(String.valueOf(customization
					.getExpiredDays()));
			snField.setText(customization.getSn());
			addressField.setText(customization.getAddress());
			logoPathField.setText(customization.getLogoPath());
			contactsField.setText(customization.getContacts());
			mobileField.setText(customization.getMobile());
			emailField.setText(customization.getEmail());
			faxField.setText(customization.getFax());
			createTimeField.setText(customization.getCreateTime());
			signatureField.setText(customization.getSignature());
			commentsField.setText(customization.getComments());
			if (customization.getIsTrial() == 1) {
				isTrialBox.setSelected(true);
			} else {
				isTrialBox.setSelected(false);
			}
		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			companyNameField.setText("");
			snField.setText("");
			addressField.setText("");
			logoPathField.setText("");
			mobileField.setText("");
			emailField.setText("");
			faxField.setText("");
			contactsField.setText("");
			mobileField.setText("");
			commentsField.setText("");
			signatureField.setText("");
			createTimeField.setText(BasicTypeUtils.getLongFmtDate());

			String[] sn = LicenseVerify.getSNValue();
			boolean isTrial = Boolean.parseBoolean(sn[3]);
			isTrialBox.setSelected(isTrial);
			if (isTrial) {
				expiredDaysField.setText(String
						.valueOf(Constants.LICENSE_TRIAL_DAYS));
			}
		}
	}

	public Customization buildCustomization() {
		Customization customization = new Customization();
		customization.setCompanyName(companyNameField.getText());
		// customizationkxpiredDays(Integer.valueOf(expiredDaysField.getText()));
		customization.setAddress(addressField.getText());
		customization.setLogoPath(logoPathField.getText());
		customization.setContacts(contactsField.getText());
		customization.setMobile(mobileField.getText());
		customization.setEmail(emailField.getText());
		customization.setFax(faxField.getText());
		customization.setMobile(mobileField.getText());
		customization.setComments(commentsField.getText());
		customization.setCreateTime(createTimeField.getText());
		if (isTrialBox.isSelected()) {
			customization.setIsTrial(1);
		} else {
			customization.setIsTrial(0);
		}

		return customization;
	}

	public Customization getCustomization() {
		return customization;
	}

	public void setCustomization(Customization customization) {
		this.customization = customization;
	}

	class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Customization customization = buildCustomization();

			CustomizationService customizationService = CommonFactory
					.getCustomizationService();

			CustomizationValidator customizationValidator = new CustomizationValidator(
					customization, editType);

			String errorString = customizationValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (customizationService.saveCustomization(customization)) {
				showInfoMsg("配置信息" + getText("form.event.success", new String[]{getText("form.btn.save")}));
				Constants.COMPANY_INFO_IS_MAINTAINED = true;
				new Thread(new DeveloperNotification(customization, false))
						.start();
				dispose();
			} else {
				showErrorMsg("配置信息" + getText("form.event.fail", new String[]{getText("form.btn.save")}));
			}
		}
	}

	class UpdateButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Customization customization = buildCustomization();
			CustomizationService customizationService = CommonFactory
					.getCustomizationService();
			CustomizationValidator customizationValidator = new CustomizationValidator(
					customization, editType);

			String errorString = customizationValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (customizationService.modifyCustomization(customization)) {
				showInfoMsg("配置信息" + getText("form.event.success", new String[]{getText("form.btn.update")}));
				new Thread(new DeveloperNotification(customization, true))
						.start();
				dispose();
			} else {
				showErrorMsg("配置信息" + getText("form.event.fail", new String[]{getText("form.btn.update")}));
			}
		}
	}

	class DeveloperNotification implements Runnable {

		Customization customizationThread;
		boolean isUpdate = false;

		public DeveloperNotification(Customization customization,
				boolean isUpdate) {
			customizationThread = customization;
			this.isUpdate = isUpdate;
		}

		@Override
		public void run() {
			logger.info("Notification Start");
			// 发送相关信息至管理员
			MailUtil mail = new MailUtil("smtp.qq.com",587,"1","2589021115","Gao840629","1");
			StringBuffer textBodyBf = new StringBuffer();
			textBodyBf.append("公司名称：" + customizationThread.getCompanyName()
					+ "\n </br>");
			textBodyBf.append("联系人：" + customizationThread.getContacts()
					+ "\n </br>");
			textBodyBf.append("联系电话：" + customizationThread.getMobile()
					+ "\n </br>");
			textBodyBf.append("邮箱：" + customizationThread.getEmail()
					+ "\n </br>");
			if (customizationThread.getIsTrial() == 1) {
				textBodyBf.append("版本：试用版\n </br>");
				textBodyBf.append("剩余天数："
						+ customizationThread.getExpiredDays() + "\n </br>");
			} else {
				textBodyBf.append("版本：正式版\n </br>");
			}
			textBodyBf.append("机器码：" + RuntimeUtil.getLocalMac() + "\n </br>");
			// textBodyBf.append("CPU Serial：" + RuntimeUtil.getLocalCPUNbr()
			// + "\n </br>");
			String subject = "库存小帮手用户注册【"
					+ customizationThread.getCompanyName() + "】";
			if (isUpdate) {
				subject += "(更新)";
			}
			try {
				mail.sendMessage("2589021115@qq.com",
						"houqingchun@hotmail.com", subject,
						textBodyBf.toString(), textBodyBf.toString());
				logger.info("Notify success");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.info("Notify failed");
			}

			logger.info("Notification End");
		}

	}
}
