package com.ivt.mis.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.DataValidator;
import com.ivt.mis.common.MailUtil;
import com.ivt.mis.common.RuntimeUtil;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;
import com.jgoodies.forms.builder.DefaultFormBuilder;

public class ContactMeFrame extends BaseJInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5448264985689329331L;

	public static final Logger logger = Logger
			.getLogger(ContactMeFrame.class);

	JTextArea feedbackTextArea;
	JTextArea feedbackTitle;

	public ContactMeFrame(String editType) {
		super("问题反馈", true, true, false, true);
		this.setFrameId(Constants.MODULE_CONTACTME);
		//this.setLayer(this.getLayer() + 1);
		this.editType = editType;
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(480, 240);
		this.setResizable(true);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);

		this.getContentPane().add(createContactMeJPanel());
	}

	private JPanel createContactMeJPanel() {
		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		/*
		 * 建立客户添加信息页面
		 */
		feedbackTextArea = new JTextArea();
		feedbackTextArea.setRows(5);
		feedbackTextArea.setColumns(20);
		feedbackTitle = new JTextArea();
		feedbackTitle.setRows(1);
		feedbackTitle.setColumns(20);

		JPanel addPanel = new JPanel();

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();

		// Fill the table with labels and components.
		builder.append(new JLabel("标题："));
		builder.append(feedbackTitle, 5);
		builder.nextLine();
		builder.append(new JLabel("问题备注："));
		builder.append(feedbackTextArea, 5);
		builder.nextLine();
		builder.append(new JLabel(""));
		builder.append(new JLabel("提醒：在线问题反馈需要保持网络畅通"), 5);

		// 添加到主Panel内
		addPanel.add(builder.getPanel());

		// 根据编辑类型不同，生成不同按扭
		JPanel buttonsPanel = createCRUDButtons(new AddButtonListener(), null,
				null);

		addPanel.setAlignmentY(10f);

		mainContainer.add(addPanel);
		mainContainer.add(buttonsPanel);

		return mainContainer;
	}

	class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (DataValidator.isBlankOrNull(feedbackTextArea.getText())) {
				showErrorMsg("您未输入任何内容");
			} else {
				new Thread(new DeveloperNotification()).start();
				;
				showInfoMsg("您的问题已经反馈成功！");
				dispose();
			}
		}
	}

	class DeveloperNotification implements Runnable {

		public DeveloperNotification() {
		}

		@Override
		public void run() {
			logger.info("Notification Start");
			CustomizationService customizationService = CommonFactory
					.getCustomizationService();

			Customization customization = customizationService
					.getCustomizationInfo();

			// 发送相关信息至管理员
			MailUtil mail = new MailUtil("smtp.qq.com", 587, "1", "2589021115",
					"Gao840629", "1");
			StringBuffer textBodyBf = new StringBuffer();
			textBodyBf.append("公司名称：" + customization.getCompanyName()
					+ "\n </br>");
			textBodyBf
					.append("联系人：" + customization.getContacts() + "\n </br>");
			textBodyBf.append("联系电话：" + customization.getMobile() + "\n </br>");
			textBodyBf.append("邮箱：" + customization.getEmail() + "\n </br>");
			if (customization.getIsTrial() == 1) {
				textBodyBf.append("版本：试用版\n </br>");
				textBodyBf.append("剩余天数：" + customization.getExpiredDays()
						+ "\n </br>");
			} else {
				textBodyBf.append("版本：正式版\n </br>");
			}
			textBodyBf.append("机器码：" + RuntimeUtil.getLocalMac()
					+ "\n </br></br>");
			textBodyBf
					.append("标题：" + feedbackTitle.getText() + "\n </br></br>");
			textBodyBf
					.append("问题备注：" + feedbackTextArea.getText() + "\n </br>");
			String subject = "库存小帮手问题反馈【" + customization.getCompanyName()
					+ "】";
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
