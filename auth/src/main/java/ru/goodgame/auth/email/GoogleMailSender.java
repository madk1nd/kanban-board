package ru.goodgame.auth.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class GoogleMailSender implements IMailSender {

    @Value("${email.confirm.url}")
    private String confirmUrl;

    @Nonnull private final JavaMailSender emailSender;

    public GoogleMailSender(@Nonnull JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(@Nonnull String address, @Nonnull String token) {
        String url = confirmUrl + "/auth/confirm?token=" + token;

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(address);
            helper.setSubject("Confirm registration");
            mimeMessage.setContent("Thank you for sign up with KanbanBoard!<br>" +
                    "Please click on the link to <a href=\""
                    + url + "\">confirm registration</a>" , "text/html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(mimeMessage);
    }
}
