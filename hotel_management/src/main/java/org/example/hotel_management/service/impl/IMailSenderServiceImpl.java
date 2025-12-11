package org.example.hotel_management.service.impl;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.scene.control.Alert;
import org.example.hotel_management.dao.OTPCodeDAO;
import org.example.hotel_management.entity.OTPCode;
import org.example.hotel_management.service.IMailSenderService;
import org.example.hotel_management.service.IOTPCodeService;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.DotEnvUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IMailSenderServiceImpl implements IMailSenderService {

    private static final IMailSenderServiceImpl INSTANCE = new IMailSenderServiceImpl();
    private static final Logger logger = Logger.getLogger(IMailSenderServiceImpl.class.getName());
    private final IOTPCodeService otpCodeService = IOTPCodeServiceImpl.getInstance();
    private final String myEmail;
    private final String myPassword;

    private IMailSenderServiceImpl() {
        Dotenv dotenv = DotEnvUtil.getDotenv();
        this.myEmail = dotenv.get("MAIL_USERNAME");
        this.myPassword = dotenv.get("MAIL_PASSWORD");

        if (this.myEmail == null || this.myPassword == null) {
            logger.log(Level.SEVERE, "❌ Lỗi: Thiếu cấu hình MAIL_USERNAME hoặc MAIL_PASSWORD trong file .env");
        }
    }

    public static IMailSenderServiceImpl getInstance() {
        return INSTANCE;
    }

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public CompletableFuture<Boolean> sendMail(String toEmail, String subject) {

        return CompletableFuture.supplyAsync(() -> {
            if (myEmail == null || myPassword == null) {
                logger.log(Level.WARNING, "⚠️ Không thể gửi mail vì chưa cấu hình tài khoản.");
                AlertUtil.showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to send email", "Please try again later");
                return false;
            }
            try {
                Properties mailProps = new Properties();
                mailProps.put("mail.smtp.host", "smtp.gmail.com");
                mailProps.put("mail.smtp.port", "587");
                mailProps.put("mail.smtp.auth", "true");
                mailProps.put("mail.smtp.starttls.enable", "true");
                Session session = Session.getInstance(mailProps, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(myEmail, myPassword);
                    }
                });

                String otpCode = generateOTP();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                String htmlBody = loadHtmlTemplate(otpCode);
                message.setContent(htmlBody, "text/html; charset=utf-8");
                message.setSubject(subject);

                Transport.send(message);

                boolean saved = otpCodeService.addOTPCode(otpCode, toEmail);

                if (saved) {
                    logger.info("✅ Email sent & OTP saved for " + toEmail);
                    return true;
                } else {
                    logger.severe("❌ Email sent but failed to save OTP to DB");
                    return false;
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "❌ Failed to send email: " + e.getMessage());
                return false;
            }
        });
    }

    @Override
    public String loadHtmlTemplate(String otpCode) {
        try {
            var resource = getClass().getClassLoader().getResource("org/example/hotel_management/templates/email_template_otp.html");

            if (resource == null) {
                return "<h1>Mã OTP của bạn là: " + otpCode + "</h1>";
            }

            Path path = Paths.get(resource.toURI());
            String content = Files.readString(path);

            return content.replace("{{OTP_CODE}}", otpCode);

        } catch (Exception e) {
            e.printStackTrace();
            return "Mã OTP của bạn là: " + otpCode;
        }
    }
}
