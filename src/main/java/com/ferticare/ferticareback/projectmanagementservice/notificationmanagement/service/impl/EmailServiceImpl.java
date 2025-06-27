package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationEmail(User user, String token) {
        try {
            String link = "http://localhost:3000/verify-email?token=" + token;

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Xác thực Email");

            String htmlContent = "<h2>Xin chào " + user.getFullName() + ",</h2>"
                    + "<p>Vui lòng nhấn vào liên kết sau để xác thực địa chỉ email của bạn:</p>"
                    + "<p><a href=\"" + link + "\">Xác thực ngay</a></p>"
                    + "<br/><p>Trân trọng,<br/>Đội ngũ FertiCare</p>";

            helper.setText(htmlContent, true); // ✅ true để kích hoạt HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email xác thực: " + e.getMessage());
            throw new RuntimeException("Đăng ký thất bại: Could not parse mail", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(User user, String token) {
        try {
            String link = "http://localhost:3000/reset-password?token=" + token;

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("Đặt lại mật khẩu");

            String htmlContent = "<h2>Xin chào " + user.getFullName() + ",</h2>"
                    + "<p>Bạn vừa yêu cầu đặt lại mật khẩu. Vui lòng nhấn vào liên kết sau để đặt lại mật khẩu mới:</p>"
                    + "<p><a href=\"" + link + "\">Đặt lại mật khẩu</a></p>"
                    + "<br/><p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>"
                    + "<br/><p>Trân trọng,<br/>Đội ngũ FertiCare</p>";

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email đặt lại mật khẩu: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu", e);
        }
    }

    @Override
    public void sendAppointmentConfirmationEmail(User customer, User doctor, String serviceName, String appointmentDate, String appointmentTime, String room, String notes, String reminderMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(customer.getEmail());
            helper.setSubject("Xác nhận lịch hẹn - FertiCare");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background-color: #4CAF50; color: white; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0;'>FertiCare</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #f9f9f9;'>"
                    + "<h2 style='color: #333;'>Xin chào " + customer.getFullName() + ",</h2>"
                    + "<p style='color: #666; font-size: 16px;'>Lịch hẹn của bạn đã được xác nhận thành công!</p>"
                    + "<div style='background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #4CAF50;'>"
                    + "<h3 style='color: #333; margin-top: 0;'>📅 Thông tin lịch hẹn:</h3>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Dịch vụ:</td><td style='padding: 8px 0; color: #333;'>" + serviceName + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Bác sĩ:</td><td style='padding: 8px 0; color: #333;'>" + doctor.getFullName() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Ngày:</td><td style='padding: 8px 0; color: #333;'>" + appointmentDate + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Giờ:</td><td style='padding: 8px 0; color: #333;'>" + appointmentTime + "</td></tr>";

            // Thêm dòng số phòng riêng biệt
            if (room != null && !room.isEmpty()) {
                htmlContent += "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Số phòng:</td><td style='padding: 8px 0; color: #333;'>" + room + "</td></tr>";
            }
            htmlContent += "</table>";

            // Ghi chú riêng
            if (notes != null && !notes.trim().isEmpty()) {
                htmlContent += "<div style='margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee;'>"
                        + "<p style='margin: 0;'><strong>Ghi chú:</strong> " + notes + "</p>"
                        + "</div>";
            }
            // Nhắc nhở riêng
            if (reminderMessage != null && !reminderMessage.trim().isEmpty()) {
                htmlContent += "<div style='margin-top: 10px;'><span style='color: #e67e22; font-weight: bold;'>" + reminderMessage + "</span></div>";
            }

            htmlContent += "</div>"
                    + "<div style='background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                    + "<h4 style='margin: 0 0 10px 0; color: #856404;'>⚠️ Lưu ý quan trọng:</h4>"
                    + "<ul style='margin: 0; padding-left: 20px; color: #856404;'>"
                    + "<li>Vui lòng đến trước 15 phút so với giờ hẹn</li>"
                    + "<li>Mang theo giấy tờ tùy thân và thẻ bảo hiểm (nếu có)</li>"
                    + "<li>Nếu cần hủy lịch, vui lòng liên hệ ít nhất 24 giờ trước</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px;'>"
                    + "<p style='color: #666;'>Nếu có thắc mắc, vui lòng liên hệ:</p>"
                    + "<p style='color: #333; font-weight: bold;'>📞 Hotline: 1900-xxxx</p>"
                    + "<p style='color: #333; font-weight: bold;'>📧 Email: support@ferticare.com</p>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>"
                    + "<p style='color: #999; font-size: 14px;'>Trân trọng,<br/>Đội ngũ FertiCare</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Đã gửi email xác nhận lịch hẹn cho: " + customer.getEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email xác nhận lịch hẹn: " + e.getMessage());
            // Không throw exception để không ảnh hưởng đến quá trình đăng ký lịch
        }
    }
} 