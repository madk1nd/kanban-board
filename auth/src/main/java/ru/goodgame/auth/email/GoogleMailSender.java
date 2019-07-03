package ru.goodgame.auth.email;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;

@Service
@Slf4j
public class GoogleMailSender implements IMailSender {

    @Value("${email.confirm.url}")
    private String confirmUrl;

    @Nonnull private final JavaMailSender emailSender;

    public GoogleMailSender(@Nonnull final JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(@Nonnull final String address,
                     @Nonnull final String token) {
        val url = confirmUrl + "/auth/confirm?token=" + token;

        val mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(address);
            helper.setSubject("Confirm registration");
            mimeMessage.setContent(
                    String.format(
                            "Thank you for sign up with KanbanBoard!<br>"
                            + "Please click on the link to <a href=\"%s\">"
                            + "confirm registration</a>",
                            url
                    ),
                    "text/html"
            );
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Can't send email to confirm registration :: {}, {}",
                    e.getMessage(), e);
        }
    }
}
