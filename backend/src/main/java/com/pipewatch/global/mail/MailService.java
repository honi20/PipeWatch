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
	private static final String senderEmail = "paoripipe@gmail.com";
	private final JavaMailSender javaMailSender;

	public MimeMessage createVerifyMail(String email, String verify) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			mimeMessage.setFrom(senderEmail);
			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, email);
			mimeMessage.setSubject("PipeWatch 이메일 인증");

			String body = "";
			body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto; font-family: Arial, sans-serif;'>";
			body += "<div style='text-align: center; margin-bottom: 20px;'>";
			body += "<img src='https://pipewatch.co.kr/assets/logo_header_light-q23IjUN-.png' alt='PipeWatch 로고' style='width: 150px; height: auto;'>";
			body += "</div>";
			body += "<h2 style='text-align: center; color: #333;'>이메일 인증 코드</h2>";
			body += "<div style='background-color: #f7f7f7; padding: 20px; text-align: center; border-radius: 8px;'>";
			body += "<h3 style='color: #333;'>요청하신 인증 코드입니다.</h3>";
			body += "<p style='font-size: 24px; font-weight: bold; color: #333;'>" + verify + "</p>";
			body += "</div>";
			body += "<div style='text-align: center; margin-top: 20px;'>";
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

	public MimeMessage createEnterpriseAccountMail(String managerEmail, String enterpriseName, String email, String password) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			mimeMessage.setFrom(senderEmail);
			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, managerEmail);
			mimeMessage.setSubject("PipeWatch 기업 등록 안내");

			String body = "";
			body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto; font-family: Arial, sans-serif;'>";
			body += "<div style='text-align: center; margin-bottom: 20px;'>";
			body += "<img src='https://pipewatch.co.kr/assets/logo_header_light-q23IjUN-.png' alt='PipeWatch 로고' style='width: 150px; height: auto;'>";
			body += "</div>";
			body += "<h2 style='text-align: center; color: #333;'>" + enterpriseName + " 기업 등록 안내 메일</h2>";
			body += "<div style='background-color: #f7f7f7; padding: 20px; text-align: center; border-radius: 8px;'>";
			body += "<h3 style='color: #333;'>관리자 계정</h3>";
			body += "<div style='text-align: left; display: inline-block; text-align: left;'>";
			body += "<p style='font-size: 16px;'><strong>EMAIL :</strong> " + email + "</p>";
			body += "<p style='font-size: 16px;'><strong>PASSWORD :</strong> " + password + "</p>";
			body += "</div>";
			body += "</div>";
			body += "<div style='text-align: center; margin-top: 20px;'>";
			body += "<a href='https://pipewatch.co.kr/account/auth/login' style='display: inline-block; padding: 12px 25px; background-color: #333; color: white; text-decoration: none; font-size: 16px; border-radius: 5px;'>사이트로 이동</a>";
			body += "</div>";
			body += "</div>";

			mimeMessage.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new BaseException(MAIL_SEND_FAILURE);
		}

		return mimeMessage;
	}

	public void sendEnterpriseAccountEmail(String managerEmail, String enterpriseName, String email, String password) {
		MimeMessage message = createEnterpriseAccountMail(managerEmail, enterpriseName, email, password);
		javaMailSender.send(message);
	}

	public MimeMessage createPasswordResetMail(String email, String pwdUuid) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			mimeMessage.setFrom(senderEmail);
			mimeMessage.setRecipients(MimeMessage.RecipientType.TO, email);
			mimeMessage.setSubject("PipeWatch 비밀번호 재설정");

			String verificationLink = "https://pipewatch.co.kr/account/auth/reset-pw?pwdUuid=" + pwdUuid;
			String body = "";
			body += "<div style='border: 2px solid black; padding: 20px; max-width: 600px; margin: auto; font-family: Arial, sans-serif;'>";
			body += "<div style='text-align: center; margin-bottom: 20px;'>";
			body += "<img src='https://pipewatch.co.kr/assets/logo_header_light-q23IjUN-.png' alt='PipeWatch 로고' style='width: 150px; height: auto;'>";
			body += "</div>";
			body += "<h2 style='text-align: center; color: #333;'>비밀번호 재설정 안내</h2>";
			body += "<div style='padding: 20px; padding-top: 10px; padding-bottom: 10px; text-align: center; border-radius: 8px;'>";
			body += "<h3 style='color: #333;'>아래 링크를 클릭하여 비밀번호를 재설정하세요.</h3>";
			body += "</div>";
			body += "<div style='text-align: center; margin-top: 20px;'>";
			body += "<a href='" + verificationLink + "' style='display: inline-block; padding: 12px 25px; background-color: #333; color: white; text-decoration: none; font-size: 16px; border-radius: 5px;'>비밀번호 재설정</a>";
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

