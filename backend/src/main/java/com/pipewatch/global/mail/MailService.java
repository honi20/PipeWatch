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
import java.util.UUID;

import static com.pipewatch.global.statusCode.ErrorCode.MAIL_SEND_FAILURE;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    private static final String senderEmail= "paoripipe@gmail.com";

    public MimeMessage createVerifyMail(String email, String verify) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, email);
            mimeMessage.setSubject("PipeWatch 이메일 인증");

            String body = "";
            body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto;'>";
            body += "<h2 style='text-align: center;'>이메일 인증 코드</h2>";
            body += "<div style='background-color: #f0f0f0; padding: 20px; text-align: center;'>";
            body += "<h3>요청하신 인증 코드입니다.</h3>";
            body += "<p style='font-size: 24px; font-weight: bold;'>" + verify + "</p>";
            body += "</div>";
            body += "<div style='text-align: center; margin-top: 20px;'>";
            body += "<p>감사합니다.</p>";
            body += "</div>";
            body += "</div>";

            mimeMessage.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
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

    public MimeMessage createEnterpriseAccountMail(String managerEmail, String email, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, managerEmail);
            mimeMessage.setSubject("PipeWatch 기업 등록 안내");

            String body = "";
            body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto;'>";
            body += "<h2 style='text-align: center;'>기업 등록 안내 메일</h2>";
            body += "<div style='background-color: #f0f0f0; padding: 20px; text-align: center;'>";
            body += "<h3>관리자 계정</h3>";
            body += "<p><strong>EMAIL :</strong> " + email + "</p>";
            body += "<p><strong>PW :</strong> " + password + "</p>";
            body += "</div>";
            body += "<div style='text-align: center; margin-top: 20px;'>";
            body += "<a href='http://localhost:5173' style='display: inline-block; padding: 10px 20px; background-color: #f0f0f0; color: black; text-decoration: none; border-radius: 5px;'>PAORI 사이트로 이동</a>";
            body += "</div>";
            body += "</div>";

            mimeMessage.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new BaseException(MAIL_SEND_FAILURE);
        }

        return mimeMessage;
    }

    public void sendEnterpriseAccountEmail(String managerEmail, String email, String password) {
        MimeMessage message = createEnterpriseAccountMail(managerEmail, email, password);
        javaMailSender.send(message);
    }

    public MimeMessage createPasswordResetMail(String email, String pwdUuid) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, email);
            mimeMessage.setSubject("PipeWatch 비밀번호 재설정");

            String verificationLink = "http://localhost:5173/reset-password/" + pwdUuid;
            String body = "";
            body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto;'>";
            body += "<h2 style='text-align: center;'>비밀번호 재설정 메일</h2>";
            body += "<div style='background-color: #f0f0f0; padding: 20px; text-align: center;'>";
            body += "<h3>아래 링크를 클릭하여 비밀번호를 재설정하세요.</h3>";
            body += "</div>";
            body += "<div style='text-align: center; margin-top: 20px;'>";
            body += "<a href=\"" + verificationLink + "\" style='display: inline-block; padding: 10px 20px; background-color: #f0f0f0; color: black; text-decoration: none; border-radius: 5px;'>PAORI 비밀번호 재설정</a>";
            body += "</div>";
            body += "</div>";

            mimeMessage.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new BaseException(MAIL_SEND_FAILURE);
        }

        return mimeMessage;
    }

	public String sendPasswordResetEmail(String email) {
        String pwdUuid = String.valueOf(UUID.randomUUID());
        MimeMessage message = createPasswordResetMail(email, pwdUuid);
        javaMailSender.send(message);
        return pwdUuid;
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

