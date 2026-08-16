package me.mallahajay43.campaignflow.template.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import me.mallahajay43.campaignflow.common.exceptions.EmailDeliveryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SmtpEmailProvider implements EmailProvider {

    private final JavaMailSender mailSender;

    @Override
    public void send(String email, String subject, String html, String from) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception exception) {
            throw new EmailDeliveryException("Failed sending email to " + email, exception);
        }
    }
}