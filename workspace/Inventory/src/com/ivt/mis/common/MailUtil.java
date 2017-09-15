package com.ivt.mis.common;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.sun.mail.smtp.SMTPTransport;

/**
 * 提供发送邮件的工具类，内置内部类完成调用线程进行邮件的队列发送，以保证邮件服务器的安全
 *@author 侯青春
 *          
 */

public class MailUtil {
	private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private String host = null;
	private int port = 25;
	private String auth = null;
	private String username = null;
	private String password = null;
	private boolean mailusername = false;
	private Session session = null;
	private String encoding = "utf-8";

	private Logger logger = Logger.getLogger(this.getClass());
	ThreadPoolExecutor executor = null;
	{
		executor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));
	}

	/**
	 * 初始化邮件工具类，并设定相关的系统参数，邮件服务器等
	 * 
	 * @param host
	 * @param port
	 * @param auth
	 * @param username
	 * @param password
	 * @param mailusername
	 */
	public MailUtil(String host, int port, String auth, String username,
			String password, String mailusername) {
		this.host = host;
		if (port > 0) {
			this.port = port;
		}
		this.auth = auth;
		this.username = username;
		this.password = password;

		if ("1".equals(mailusername)) {
			this.mailusername = true;
		}
	}

	/**
	 * 建立session，准备发送
	 */
	private synchronized void createSession() {
		Properties mailProps = new Properties();
		mailProps.setProperty("mail.transport.protocol", "smtp");
		mailProps.setProperty("mail.smtp.host", host);
		mailProps.setProperty("mail.smtp.port", String.valueOf(port));
		if ("smtp.gmail.com".equals(host)) {
			mailProps.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			mailProps.setProperty("mail.smtp.socketFactory.fallback", "false");
			mailProps.setProperty("mail.smtp.socketFactory.port", String
					.valueOf(port));
		}
		if ("1".equals(auth)) {
			mailProps.put("mail.smtp.auth", "true");
			mailProps.put("mail.smtp.starttls.enable", "true");
		}
		session = Session.getDefaultInstance(mailProps, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	/**
	 * 创建发送消息
	 * 
	 * @return
	 */
	private MimeMessage createMimeMessage() {
		if (session == null) {
			createSession();
		}
		return new MimeMessage(session);
	}

	/**
	 * 执行发送
	 * 
	 * @param from
	 * @param toEmail
	 * @param subject
	 * @param textBody
	 * @param htmlBody
	 * @return
	 * @throws Exception 
	 */
	public String sendMessage(String from, String toEmail, String subject,
			String textBody, String htmlBody) throws Exception {
		String result = null;
		try {
			MimeMessage message = createMimeMessage();
			String toEmails[] = toEmail.split(",");
			Address to[] = new Address[toEmails.length];
			for (int i = 0; i < toEmails.length; i++) {
				String sTo = toEmails[i];
				if (sTo.matches("^.*<.*>$")) {
					int index = sTo.indexOf("<");
					to[i] = new InternetAddress(sTo.substring(index + 1, sTo
							.length() - 1), mailusername ? sTo.substring(0,
							index) : "", encoding);
				} else {
					to[i] = new InternetAddress(sTo, "", encoding);
				}
			}
			String fromName = null;
			String fromEmail;
			if (from.matches("^.*<.*>$")) {
				int index = from.indexOf("<");
				if (mailusername) {
					fromName = from.substring(0, index);
				}
				fromEmail = from.substring(index + 1, from.length() - 1);
			} else {
				fromEmail = from;
			}
			Address fromAddress = new InternetAddress(fromEmail,
					fromName != null ? fromName : "", encoding);

			message.setHeader("Content-Transfer-Encoding", "8bit");
			message.setRecipients(Message.RecipientType.TO, to);
			message.setFrom(fromAddress);
			message.setSubject(subject, encoding);
			MimeMultipart content = new MimeMultipart("alternative");
			if (textBody != null && htmlBody != null) {
				MimeBodyPart text = new MimeBodyPart();
				text.setText(textBody, encoding);
				text.setDisposition(Part.INLINE);
				content.addBodyPart(text);
				MimeBodyPart html = new MimeBodyPart();
				html.setContent(htmlBody, "text/html;charset=" + encoding);
				html.setDisposition(Part.INLINE);
				content.addBodyPart(html);
			} else if (textBody != null) {
				MimeBodyPart text = new MimeBodyPart();
				text.setText(textBody, encoding);
				text.setDisposition(Part.INLINE);
				content.addBodyPart(text);
			} else if (htmlBody != null) {
				MimeBodyPart html = new MimeBodyPart();
				html.setContent(htmlBody, "text/html;charset=" + encoding);
				html.setDisposition(Part.INLINE);
				content.addBodyPart(html);
			}
			message.setContent(content);
			message.setDisposition(Part.INLINE);
			sendMessages(message);
		} catch (Exception e) {
			result = e.getMessage();
			throw e;
		}
		return result;
	}

	/**
	 * 调用发送小类
	 * 
	 * @param message
	 * @throws MessagingException
	 */
	private void sendMessages(MimeMessage message) throws MessagingException {
		Transport transport = null;
		try {
			URLName url = new URLName("smtp", host, port, "", username,
					password);
			transport = new SMTPTransport(session, url);
			transport.connect(host, port, username, password);
			transport.sendMessage(message, message
					.getRecipients(MimeMessage.RecipientType.TO));

		} catch (MessagingException ex) {
			logger.error("邮件发送失败，请检查邮件设定，且这笔邮件将保存在发件箱里，继续等待发送，邮件的主题为："
					+ message.getSubject() + "，邮件发送错误：" + ex.getMessage());
			throw ex;

		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
//	public static void main(String[] args){
//		MailUtil mail = new MailUtil("smtp.gmail.com",465,"1","houqingchunpublic","Gao840629","0");
//		try {
//			mail.sendMessage("", "8083851@qq.com", "进销存用户信息", "进销存试用license申请",  "进销存试用license申请");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		
//	}
//	public static void main(String[] args){
//		MailUtil mail = new MailUtil("smtp.qq.com",587,"1","2589021115","Gao840629","1");
//		try {
//			mail.sendMessage("2589021115@qq.com", "houqingchun@hotmail.com", "进销存用户信息", "进销存试用license申请",  "进销存试用license申请");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		
//	}
}