package model.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.User;
import model.exceptions.user.UserException;

public class SendEmail {

	private static String host = "smtp.gmail.com";
	private static String port = "465";
	private static String from = "e@mail.com";
	private static String pass = "xxx";

	private static String subject = "Wellcome to www.itvideo.com";

	public static void to(User u) {
		Properties props = new Properties();
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		//props.put("mail.transport.protocol", "smtps");
		// props.put("mail.password", pass);
		// props.put("mail.smtp.debug", "true");
		// props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.smtp.socketFactory.port", port);
		// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// props.put("mail.smtp.socketFactory.fallback", "false");
		Session session = Session.getDefaultInstance(props);
		
		Message msg = new MimeMessage(session);
		try {
			
			msg.setFrom(new InternetAddress(from));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
			msg.setSubject(subject);
			
			String emailText = String.format("%s, wellcome to <strong>www.itvideo.com</strong><br>"
					+ "Your email is <strong>%s</strong><br>"
					+ "Your hashed password is <strong>%s</strong>",
					u.getUsername(),u.getEmail(), u.getPassword());
			
			msg.setContent(emailText, "text/html; charset=utf-8");

			Transport transport = session.getTransport("smtps");
			transport.connect(host, from, pass);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UserException {
		SendEmail.to(new User("Hristo", "password", "i40.Penev@gmail.com"));
	}
}
