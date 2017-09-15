package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.io.IOUtils;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.LicenseVerify;
import com.ivt.mis.common.RuntimeUtil;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.common.swing.SpringUtilities;
import com.ivt.mis.model.Customization;
import com.ivt.mis.service.CustomizationService;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class LicenseRegisterFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3771685943392837747L;
	JTextArea snTextArea;
	JLabel errorMsg;
	JTextArea signitureTextArea;
	JButton tryBtn;
	JButton registBtn;
	JButton cancelBtn;

	
	protected int rowNbr = 3;

	public LicenseRegisterFrame() {
		super("库存小帮手");
		this.setLayout(new FlowLayout());
		SpringUtilities.initGlobalStyle();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 3, screenSize.height / 3, 480, 325);
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

		this.createFormPanel();

		this.setVisible(true);

	}
	

	public void actionPerformed(ActionEvent e) {
		String sn = snTextArea.getText().trim();
		String signiture = signitureTextArea.getText().trim();

		if (!LicenseVerify.verify(Constants.LICENSE_PUBLICK_KEY.getBytes(), sn,
				signiture.getBytes())) {
			// License无效
			JOptionPane.showMessageDialog(this, "抱歉，注册失败", "警告",
					JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			// 保存信息至文件
			String path = LicenseRegisterFrame.class.getResource("/").getPath();
			try {
				path = java.net.URLDecoder.decode(path,"utf-8");
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}  
			try {
				IOUtils.write(sn.getBytes(), new FileOutputStream(new File(path
						+ "sn.properties")));
				IOUtils.write(signiture.getBytes(), new FileOutputStream(
						new File(path + "signature.properties")));
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "创建证书失败！", "警告",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		String verifyResult = LicenseVerify.verifyLicense();
		String verifyMsg = convertLicenseErrorCodeToMsg(verifyResult);
		if (verifyResult.equals(Constants.LICENSE_VALID)) {
			JOptionPane.showMessageDialog(this, "注册成功", "提醒",
					JOptionPane.WARNING_MESSAGE);
			JFrame.setDefaultLookAndFeelDecorated(true);
			new LoginFrame();
//			this.setVisible(false);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, verifyMsg, "警告",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	class LoginStartThread extends Thread{
		public void run(){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new LoginFrame();	
		}
	}
	
	class TryButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CustomizationService  customizationService = CommonFactory.getCustomizationService();
			Customization customization = customizationService.getCustomizationInfo();
			if (customization != null){
				//计算剩余试用天数，并更新至数据库
			}else{
				//首次开启
			}
		}
		
	}
	
	public String convertLicenseErrorCodeToMsg(String errorCode){
		String verifyMsg = "";
		if (Constants.LICENSE_EXPIRED.equals(errorCode)){
			verifyMsg = "注册码已经过期";
		}else if (Constants.LICENSE_CODE_NOT_MATCH.equals(errorCode)){
			verifyMsg = "机器码不匹配";
		}else if (Constants.LICENSE_NOT_REGIST.equals(errorCode)){
			verifyMsg = "尚未注册";
		}else if (Constants.LICENSE_INVALID.equals(errorCode)){
			verifyMsg = "注册码无效";
		}
		
		return verifyMsg;
	}

	/**
	 * 展现表单使用
	 * 
	 * @return
	 */
	protected int nextRow() {
		rowNbr = rowNbr + 2;
		return rowNbr;
	}
	
	/**
	 * 窗体布局
	 */
	private void createFormPanel() {
		JPanel mainPanel = new JPanel();
		Font font = new Font("Dialog", Font.PLAIN, 11);
		mainPanel.setLayout(new BorderLayout());
		
		String verifyResultCode = LicenseVerify.verifyLicense();
		String verifyMsg = convertLicenseErrorCodeToMsg(verifyResultCode);
		
		errorMsg = new JLabel(verifyMsg);
		errorMsg.setForeground(Color.red);
		
		snTextArea = new JTextArea();
		snTextArea.setColumns(40);
		snTextArea.setRows(2);
		snTextArea.setLineWrap(true);
		snTextArea.setWrapStyleWord(true); 
		snTextArea.setFont(font);
		
		signitureTextArea = new JTextArea();
		signitureTextArea.setColumns(40);
		signitureTextArea.setRows(5);
		signitureTextArea.setOpaque(true);
		signitureTextArea.setLineWrap(true);
		signitureTextArea.setWrapStyleWord(true); 
		signitureTextArea.setFont(font);
		

		JPanel btnPanel = new JPanel();
		registBtn = new JButton("注册");
		registBtn.addActionListener(this);

		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		btnPanel.add(registBtn);
		btnPanel.add(cancelBtn);
		
		
		JLabel note = new JLabel();
		StringBuffer buf = new StringBuffer();
		buf.append("<html><table>");
		buf.append("<tr><td>获取注册码请拨打：18600182885  侯先生  QQ：8083851 或发邮件至houqingchun@hotmail.com 需注明本机机器码</td></tr>");
		buf.append("<tr><td>本机机机器码为：<span style='font-family:arial;color:blue;font-size:10px;'> " + RuntimeUtil.getLocalCPUNbr() + "</span></td></tr>");
		buf.append("<tr><td>可免费试用" + Constants.LICENSE_TRIAL_DAYS + "天，试用期间登录时需确保网络畅通</td></tr>");
		buf.append("<tr><td>系统管理员用户名密码默认均为admin 进入系统后可修改</td></tr>");
		buf.append("</table></html>");
		
		note.setText(buf.toString());
		note.setFont(font);
		
		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		CellConstraints cc = new CellConstraints();
		builder.append(new JLabel(""));
		builder.append(errorMsg, 5);
		builder.nextLine();
		builder.append(new JLabel("序列号:"));
		builder.append(snTextArea, 5);
		builder.nextLine();
		builder.append(new JLabel("数字签名:"));
		builder.append(signitureTextArea, 5);
		builder.nextLine();
		builder.append(new JLabel("使用说明:"));
		builder.append(note, 5);

		mainPanel.add(builder.getPanel(),BorderLayout.CENTER);
		mainPanel.add(btnPanel,BorderLayout.SOUTH);
		
		this.getContentPane().add(mainPanel);
	}
}
