package com.publicratings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private JavaMailSender javaMailSender;

	@Autowired
	public NotificationService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendNotification(String to, String subject, String text) {
		// send email
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setFrom("publicrating@web.de");
		mail.setSubject(subject);
		mail.setText(text);

		try{
            this.javaMailSender.send(mail);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }

	}
}