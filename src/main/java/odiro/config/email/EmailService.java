package odiro.config.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {
    @Autowired
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                          String title,
                          String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw e;
        }
    }

    // 발신할 이메일 데이터 세팅
    private MimeMessage createEmailForm(String toEmail,
                                        String title,
                                        String text) throws MessagingException, UnsupportedEncodingException {
//        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(text);

        helper.setFrom("ohjuyeon2000@gmail.com","ODIRO@gmail.com");

        // 실제 발신 주소 설정 (envelope-from)
//        message.setEnvelopeFrom("ohjuyeon2000@gmail.com");

        return message;
    }
}