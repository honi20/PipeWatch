package com.pipewatch.global.mail;

import com.pipewatch.global.exception.BaseException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.pipewatch.global.statusCode.ErrorCode.MAIL_SEND_FAILURE;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    private static final String senderEmail= "paoripipe@gmail.com";

    public MimeMessage createVerifyMail(String email, String verify){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, email);
            mimeMessage.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + verify + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            mimeMessage.setText(body,"UTF-8", "html");
        }catch (MessagingException e){
            throw new BaseException(MAIL_SEND_FAILURE);
        }

        return mimeMessage;
    }

    public String sendVerifyEmail(String mail) throws NoSuchAlgorithmException {
        String verify = generateRandomString();
        MimeMessage message = createVerifyMail(mail, verify);
        javaMailSender.send(message);
        return verify;
    }

    //랜덤 인증번호 생성
    private String generateRandomString() throws NoSuchAlgorithmException {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException();
        }
    }
}
