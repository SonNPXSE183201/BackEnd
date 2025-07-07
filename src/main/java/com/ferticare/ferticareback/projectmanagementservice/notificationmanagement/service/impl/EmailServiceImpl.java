package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlan;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private UserRepository userRepository;

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
                    + "<p style='color: #333; font-weight: bold;'>📧 Email: ferticaretreatment@gmail.com</p>"
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

    @Override
    public void sendAppointmentReminder(TreatmentSchedule schedule, int hoursBefore) {
        Optional<User> patientOpt = userRepository.findById(schedule.getPatientId());
        Optional<User> doctorOpt = userRepository.findById(schedule.getDoctorId());
        if (patientOpt.isEmpty()) return;
        User patient = patientOpt.get();
        User doctor = doctorOpt.orElse(null);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Nhắc nhở] Lịch điều trị tại Fertix");
            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background: linear-gradient(90deg, #ff6b9d 0%, #ff758c 100%); color: white; padding: 20px; text-align: center; border-radius: 12px 12px 0 0;'>"
                    + "<h1 style='margin: 0;'>Fertix</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống điều trị hiếm muộn và chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #fff6fa;'>"
                    + "<h2 style='color: #ff6b9d;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #666; font-size: 16px;'>Bạn có một lịch điều trị sẽ diễn ra trong <b>" + hoursBefore + " giờ nữa</b>.</p>"
                    + "<div style='background-color: #ffebf2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ff6b9d;'>"
                    + "<ul style='list-style: none; padding: 0; color: #ff6b9d;'>"
                    + "<li><b>Thời gian:</b> " + schedule.getScheduledDate() + "</li>"
                    + "<li><b>Bước điều trị:</b> " + schedule.getStepName() + "</li>"
                    + (doctor != null ? "<li><b>Bác sĩ:</b> " + doctor.getFullName() + "</li>" : "")
                    + (schedule.getRoomId() != null ? "<li><b>Phòng:</b> " + schedule.getRoomId() + "</li>" : "")
                    + (schedule.getNotes() != null ? "<li><b>Ghi chú:</b> " + schedule.getNotes() + "</li>" : "")
                    + "</ul>"
                    + "</div>"
                    + "<p style='color: #ff6b9d;'>Vui lòng đến đúng giờ để đảm bảo quá trình điều trị diễn ra thuận lợi.</p>"
                    + "<br/><p style='color: #ff758c;'>Trân trọng,<br/>Đội ngũ Fertix</p>"
                    + "</div>"
                    + "</div>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email nhắc nhở điều trị: " + e.getMessage());
        }
    }

    @Override
    public void sendOverdueWarning(TreatmentSchedule schedule) {
        Optional<User> patientOpt = userRepository.findById(schedule.getPatientId());
        if (patientOpt.isEmpty()) return;
        User patient = patientOpt.get();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Cảnh báo] Bạn đã trễ lịch điều trị tại Fertix");
            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background: linear-gradient(90deg, #ff6b9d 0%, #ff758c 100%); color: white; padding: 20px; text-align: center; border-radius: 12px 12px 0 0;'>"
                    + "<h1 style='margin: 0;'>Fertix</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống điều trị hiếm muộn và chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #fff6fa;'>"
                    + "<h2 style='color: #ff6b9d;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #ff6b9d; font-size: 16px;'><b>Bạn đã trễ lịch điều trị hơn 30 phút.</b> Vui lòng liên hệ phòng khám hoặc đến ngay để không bị hủy kế hoạch điều trị.</p>"
                    + "<div style='background-color: #ffebf2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ff6b9d;'>"
                    + "<ul style='list-style: none; padding: 0; color: #ff6b9d;'>"
                    + "<li><b>Thời gian dự kiến:</b> " + schedule.getScheduledDate() + "</li>"
                    + "<li><b>Bước điều trị:</b> " + schedule.getStepName() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<br/><p style='color: #ff758c;'>Trân trọng,<br/>Đội ngũ Fertix</p>"
                    + "</div>"
                    + "</div>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email cảnh báo trễ lịch: " + e.getMessage());
        }
    }

    @Override
    public void sendTreatmentCancelled(TreatmentSchedule schedule) {
        Optional<User> patientOpt = userRepository.findById(schedule.getPatientId());
        if (patientOpt.isEmpty()) return;
        User patient = patientOpt.get();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Hủy kế hoạch] Kế hoạch điều trị của bạn đã bị hủy tại Fertix");
            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background: linear-gradient(90deg, #ff6b9d 0%, #ff758c 100%); color: white; padding: 20px; text-align: center; border-radius: 12px 12px 0 0;'>"
                    + "<h1 style='margin: 0;'>Fertix</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống điều trị hiếm muộn và chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #fff6fa;'>"
                    + "<h2 style='color: #ff6b9d;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #ff6b9d; font-size: 16px;'><b>Kế hoạch điều trị của bạn đã bị hủy do không đến đúng giờ theo quy định.</b></p>"
                    + "<div style='background-color: #ffebf2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ff6b9d;'>"
                    + "<ul style='list-style: none; padding: 0; color: #ff6b9d;'>"
                    + "<li><b>Bước điều trị:</b> " + schedule.getStepName() + "</li>"
                    + "<li><b>Thời gian dự kiến:</b> " + schedule.getScheduledDate() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<p style='color: #ff6b9d;'>Nếu có thắc mắc, vui lòng liên hệ phòng khám để được hỗ trợ.</p>"
                    + "<br/><p style='color: #ff758c;'>Trân trọng,<br/>Đội ngũ Fertix</p>"
                    + "</div>"
                    + "</div>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email hủy kế hoạch điều trị: " + e.getMessage());
        }
    }

    @Override
    public void sendTreatmentCompletionEmail(User patient, User doctor, TreatmentPlan plan) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Hoàn thành] Kế hoạch điều trị tại Fertix");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background: linear-gradient(90deg, #ff6b9d 0%, #ff758c 100%); color: white; padding: 20px; text-align: center; border-radius: 12px 12px 0 0;'>"
                    + "<h1 style='margin: 0;'>Fertix</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống điều trị hiếm muộn và chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #fff6fa;'>"
                    + "<h2 style='color: #ff6b9d;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #666; font-size: 16px;'>Chúc mừng! Kế hoạch điều trị của bạn đã được hoàn thành thành công!</p>"
                    + "<div style='background-color: #ffebf2; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ff6b9d;'>"
                    + "<h3 style='color: #ff6b9d; margin-top: 0;'>🎉 Thông tin hoàn thành:</h3>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Kế hoạch:</td><td style='padding: 8px 0; color: #333;'>" + plan.getPlanName() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Loại điều trị:</td><td style='padding: 8px 0; color: #333;'>" + plan.getTreatmentType() + "</td></tr>"
                    + (doctor != null ? "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Bác sĩ:</td><td style='padding: 8px 0; color: #333;'>" + doctor.getFullName() + "</td></tr>" : "")
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Ngày bắt đầu:</td><td style='padding: 8px 0; color: #333;'>" + plan.getStartDate() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Ngày hoàn thành:</td><td style='padding: 8px 0; color: #333;'>" + plan.getEndDate() + "</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #fff3e0; border: 1px solid #ffcc02; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                    + "<h4 style='margin: 0 0 10px 0; color: #e65100;'>✅ Lời khuyên sau điều trị:</h4>"
                    + "<ul style='margin: 0; padding-left: 20px; color: #e65100;'>"
                    + "<li>Tiếp tục theo dõi sức khỏe theo hướng dẫn của bác sĩ</li>"
                    + "<li>Duy trì lối sống lành mạnh và chế độ ăn uống hợp lý</li>"
                    + "<li>Tái khám định kỳ theo lịch hẹn</li>"
                    + "<li>Liên hệ ngay nếu có dấu hiệu bất thường</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px;'>"
                    + "<p style='color: #666;'>Nếu có thắc mắc, vui lòng liên hệ:</p>"
                    + "<p style='color: #333; font-weight: bold;'>📞 Hotline: 1900-xxxx</p>"
                    + "<p style='color: #333; font-weight: bold;'>📧 Email: ferticaretreatment@gmail.com</p>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>"
                    + "<p style='color: #999; font-size: 14px;'>Trân trọng,<br/>Đội ngũ Fertix</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Đã gửi email hoàn thành điều trị cho: " + patient.getEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email hoàn thành điều trị: " + e.getMessage());
        }
    }

    @Override
    public void sendTreatmentPhasesEmail(User patient, User doctor, TreatmentPlan plan) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Kế hoạch điều trị] Fertix");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background: linear-gradient(90deg, #ff6b9d 0%, #ff758c 100%); color: white; padding: 20px; text-align: center; border-radius: 12px 12px 0 0;'>"
                    + "<h1 style='margin: 0;'>Fertix</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống điều trị hiếm muộn và chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #fff6fa;'>"
                    + "<h2 style='color: #ff6b9d;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #ff6b9d; font-size: 16px;'>Kế hoạch điều trị của bạn đã được tạo thành công!</p>"
                    + "<div style='background-color: #ffe3ed; border: 1.5px solid #ff6b9d; border-radius: 8px; padding: 20px; margin: 20px 0;'>"
                    + "<h3 style='color: #ff6b9d; margin-top: 0; font-weight: bold;'>📋 Thông tin kế hoạch:</h3>"
                    + "<table style='width: 100%; color: #ff6b9d;'>"
                    + "<tr><td style='font-weight: bold;'>Kế hoạch:</td><td>" + plan.getPlanName() + "</td></tr>"
                    + "<tr><td style='font-weight: bold;'>Loại điều trị:</td><td>" + plan.getTreatmentType() + "</td></tr>"
                    + (doctor != null ? "<tr><td style='font-weight: bold;'>Bác sĩ:</td><td>" + doctor.getFullName() + "</td></tr>" : "")
                    + "<tr><td style='font-weight: bold;'>Ngày bắt đầu:</td><td>" + plan.getStartDate() + "</td></tr>"
                    + "<tr><td style='font-weight: bold;'>Thời gian dự kiến:</td><td>" + plan.getEstimatedDurationDays() + " ngày</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #ffe3ed; border: 1.5px solid #ff6b9d; border-radius: 8px; padding: 20px; margin: 20px 0;'>"
                    + "<h3 style='color: #ff6b9d; margin-top: 0; font-weight: bold;'>🗂️ Các bước tiếp theo:</h3>"
                    + "<ul style='color: #ff6b9d; margin: 0; padding-left: 20px;'>"
                    + "<li>Bạn sẽ nhận được thông báo về lịch hẹn cụ thể cho từng giai đoạn</li>"
                    + "<li>Vui lòng đến đúng giờ theo lịch hẹn</li>"
                    + "<li>Mang theo giấy tờ tùy thân và thẻ bảo hiểm (nếu có)</li>"
                    + "<li>Liên hệ ngay nếu có thay đổi về lịch trình</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px;'>"
                    + "<p style='color: #ff758c;'>Nếu có thắc mắc, vui lòng liên hệ:</p>"
                    + "<p style='color: #ff6b9d; font-weight: bold;'>📞 Hotline: 1900-xxxx</p>"
                    + "<p style='color: #ff6b9d; font-weight: bold;'>📧 Email: ferticaretreatment@gmail.com</p>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #ffe3ed;'>"
                    + "<p style='color: #ff758c; font-size: 14px;'>Trân trọng,<br/>Đội ngũ Fertix</p>"
                    + "</div>"
                    + "</div>";
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Đã gửi email kế hoạch điều trị cho: " + patient.getEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email kế hoạch điều trị: " + e.getMessage());
        }
    }

    @Override
    public void sendScheduleNotification(User patient, User doctor, TreatmentSchedule schedule) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(patient.getEmail());
            helper.setSubject("[Lịch hẹn điều trị] FertiCare - " + schedule.getStepName());

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<div style='background-color: #FF9800; color: white; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0;'>FertiCare</h1>"
                    + "<p style='margin: 5px 0 0 0;'>Hệ thống chăm sóc sức khỏe sinh sản</p>"
                    + "</div>"
                    + "<div style='padding: 30px; background-color: #f9f9f9;'>"
                    + "<h2 style='color: #333;'>Xin chào " + patient.getFullName() + ",</h2>"
                    + "<p style='color: #666; font-size: 16px;'>Lịch hẹn điều trị của bạn đã được sắp xếp!</p>"
                    + "<div style='background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #FF9800;'>"
                    + "<h3 style='color: #333; margin-top: 0;'>📅 Thông tin lịch hẹn:</h3>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Bước điều trị:</td><td style='padding: 8px 0; color: #333;'>" + schedule.getStepName() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Thứ tự:</td><td style='padding: 8px 0; color: #333;'>Bước " + schedule.getStepNumber() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Thời gian:</td><td style='padding: 8px 0; color: #333;'>" + schedule.getScheduledDate() + "</td></tr>"
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Phòng:</td><td style='padding: 8px 0; color: #333;'>" + schedule.getRoomId() + "</td></tr>"
                    + (doctor != null ? "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Bác sĩ:</td><td style='padding: 8px 0; color: #333;'>" + doctor.getFullName() + "</td></tr>" : "")
                    + "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Loại điều trị:</td><td style='padding: 8px 0; color: #333;'>" + schedule.getTreatmentType() + "</td></tr>"
                    + (schedule.getNotes() != null ? "<tr><td style='padding: 8px 0; font-weight: bold; color: #555;'>Ghi chú:</td><td style='padding: 8px 0; color: #333;'>" + schedule.getNotes() + "</td></tr>" : "")
                    + "</table>"
                    + "</div>"
                    + "<div style='background-color: #fff3e0; border: 1px solid #ffcc02; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                    + "<h4 style='margin: 0 0 10px 0; color: #e65100;'>⚠️ Lưu ý quan trọng:</h4>"
                    + "<ul style='margin: 0; padding-left: 20px; color: #e65100;'>"
                    + "<li>Vui lòng đến trước 15 phút so với giờ hẹn</li>"
                    + "<li>Mang theo giấy tờ tùy thân và thẻ bảo hiểm (nếu có)</li>"
                    + "<li>Nhịn ăn theo hướng dẫn của bác sĩ (nếu cần)</li>"
                    + "<li>Liên hệ ngay nếu không thể đến đúng giờ</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px;'>"
                    + "<p style='color: #666;'>Nếu có thắc mắc, vui lòng liên hệ:</p>"
                    + "<p style='color: #333; font-weight: bold;'>📞 Hotline: 1900-xxxx</p>"
                    + "<p style='color: #333; font-weight: bold;'>📧 Email: ferticaretreatment@gmail.com</p>"
                    + "</div>"
                    + "<div style='text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>"
                    + "<p style='color: #999; font-size: 14px;'>Trân trọng,<br/>Đội ngũ FertiCare</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            System.out.println("✅ Đã gửi email thông báo lịch hẹn cho: " + patient.getEmail());
        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email thông báo lịch hẹn: " + e.getMessage());
        }
    }
} 