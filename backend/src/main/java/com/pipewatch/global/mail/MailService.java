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
import java.util.UUID;

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
            mimeMessage.setSubject("PipeWatch 이메일 인증");

            // 인증 링크 구성
            String verificationLink = "http://localhost:8080/api/auth/verify-email-code?token=" + verify;
            String body = "<h1>이메일 인증</h1>";
            body += "<p>회원가입을 완료하려면 아래 링크를 클릭해 주세요:</p>";
            body += "<a href=\"" + verificationLink + "\">이메일 인증하기</a>";

            mimeMessage.setText(body,"UTF-8", "html");
        }catch (MessagingException e){
            throw new BaseException(MAIL_SEND_FAILURE);
        }

        return mimeMessage;
    }

    public String sendVerifyEmail(String mail) throws NoSuchAlgorithmException {
        String verify = UUID.randomUUID().toString();
        MimeMessage message = createVerifyMail(mail, verify);
        javaMailSender.send(message);
        return verify;
    }
}
