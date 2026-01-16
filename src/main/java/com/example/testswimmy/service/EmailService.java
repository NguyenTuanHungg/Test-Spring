package com.example.testswimmy.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.name}")
    private String fromName;

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML content

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to: {}. Error: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendWorkReminderEmail(String to, String candidateName) {
        String subject = "Nhắc nhở: Đi làm hôm nay";
        String content = buildWorkReminderEmailContent(candidateName);
        sendEmail(to, subject, content);
    }

    private String buildWorkReminderEmailContent(String candidateName) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }
                    .content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }
                    .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }
                    .button { background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 4px; display: inline-block; margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🌅 Chào buổi sáng!</h1>
                    </div>
                    <div class="content">
                        <p>Xin chào <strong>%s</strong>,</p>
                        <p>Đây là lời nhắc nhở thân thiện để bạn nhớ đi làm hôm nay!</p>
                        <p>Chúc bạn có một ngày làm việc hiệu quả và tràn đầy năng lượng! 💪</p>
                        <p><strong>Một số lưu ý quan trọng:</strong></p>
                        <ul>
                            <li>✓ Kiểm tra email công việc</li>
                            <li>✓ Xem lại lịch họp trong ngày</li>
                            <li>✓ Chuẩn bị tài liệu cần thiết</li>
                            <li>✓ Đến văn phòng đúng giờ</li>
                        </ul>
                        <p>Hãy bắt đầu ngày mới với tinh thần tốt nhất!</p>
                    </div>
                    <div class="footer">
                        <p>Email được gửi tự động từ hệ thống Test Swimmy</p>
                        <p>© 2026 Test Swimmy. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, candidateName != null && !candidateName.isEmpty() ? candidateName : "Bạn");
    }
}

