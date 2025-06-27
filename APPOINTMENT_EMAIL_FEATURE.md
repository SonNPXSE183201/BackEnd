# Chức năng Gửi Email Lịch Hẹn - FertiCare

## Tổng quan
Chức năng này tự động gửi email xác nhận lịch hẹn cho khách hàng ngay sau khi họ hoàn tất form đăng ký dịch vụ thành công. **Hệ thống tự động lấy số phòng từ lịch làm việc của bác sĩ.**

## Các tính năng

### 1. Email Xác nhận Lịch Hẹn
- **Kích hoạt**: Tự động gửi sau khi đăng ký lịch hẹn thành công
- **Nội dung**: 
  - Thông tin lịch hẹn (dịch vụ, bác sĩ, ngày giờ, **số phòng**)
  - Ghi chú (nếu có)
  - Lưu ý quan trọng cho bệnh nhân
  - Thông tin liên hệ

### 2. Lịch Làm Việc Bác Sĩ
- **Bảng `doctor_work_schedule`**: Lưu lịch làm việc của từng bác sĩ
- **Tự động gán số phòng**: Dựa trên thời gian và ngày trong tuần
- **Quản lý theo chuyên ngành**: IVF, IUI, v.v.

### 3. Email Nhắc nhở (Planned)
- Gửi email nhắc nhở trước lịch hẹn 24h
- Gửi email nhắc nhở trước lịch hẹn 2h

### 4. Email Hủy Lịch (Planned)
- Gửi email xác nhận khi hủy lịch hẹn

## Cấu trúc Code

### Files đã tạo/cập nhật:

1. **DoctorWorkSchedule.java** - Entity cho bảng lịch làm việc bác sĩ
2. **DoctorWorkScheduleRepository.java** - Repository để query lịch làm việc
3. **DoctorScheduleDTO.java** - DTO cho lịch làm việc
4. **AppointmentEmailDTO.java** - Cập nhật thêm trường `room`
5. **ServiceRequestService.java** - Cập nhật logic lấy số phòng
6. **AppointmentEmailServiceImpl.java** - Cập nhật template email có số phòng
7. **DoctorScheduleController.java** - Controller quản lý lịch làm việc

## API Endpoints

### Lịch Làm Việc Bác Sĩ
```
GET /api/doctor-schedules/specialty/{specialty}     # Lấy lịch theo chuyên ngành
GET /api/doctor-schedules/doctor/{doctorId}         # Lấy lịch bác sĩ cụ thể
GET /api/doctor-schedules/available-doctors/{serviceId}  # Bác sĩ rảnh theo dịch vụ
GET /api/doctor-schedules/doctor/{doctorId}/available-times?date=yyyy-MM-dd  # Thời gian rảnh
```

### Email Test
```
POST /api/notifications/appointment/test-confirmation  # Test gửi email
```

## Database Schema

### Bảng `doctor_work_schedule`
```sql
CREATE TABLE doctor_work_schedule (
    schedule_id UNIQUEIDENTIFIER PRIMARY KEY,
    doctor_id UNIQUEIDENTIFIER NOT NULL,
    day_of_week INT NOT NULL,         -- 2=Thứ 2, 3=Thứ 3, ..., 8=Chủ nhật
    start_time TIME NOT NULL,         -- Giờ bắt đầu ca
    end_time TIME NOT NULL,           -- Giờ kết thúc ca
    room NVARCHAR(100) NOT NULL,      -- Số phòng
    effective_from DATE NULL,         -- Ngày bắt đầu hiệu lực
    effective_to DATE NULL,           -- Ngày kết thúc hiệu lực
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);
```

## Luồng Hoạt Động

### 1. Đăng Ký Lịch Hẹn
1. Khách hàng chọn dịch vụ (IVF/IUI)
2. Hệ thống tìm bác sĩ phù hợp
3. **Tự động lấy số phòng** từ `doctor_work_schedule`
4. Tạo appointment với số phòng
5. Gửi email xác nhận có số phòng

### 2. Lấy Số Phòng
```java
private String getRoomForDoctor(UUID doctorId, LocalDateTime appointmentTime) {
    int dayOfWeek = appointmentTime.getDayOfWeek().getValue();
    LocalTime time = appointmentTime.toLocalTime();
    
    List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository
        .findAvailableSlots(doctorId, dayOfWeek, time);
    
    return schedules.isEmpty() ? "Phòng chờ" : schedules.get(0).getRoom();
}
```

## Cách Sử Dụng

### 1. Thêm Lịch Làm Việc Bác Sĩ
```sql
INSERT INTO doctor_work_schedule (doctor_id, day_of_week, start_time, end_time, room)
VALUES 
  ('9A1F4D4D-12E4-4E11-AE50-1EA3E3111001', 2, '08:00', '12:00', N'Phòng 101'),
  ('9A1F4D4D-12E4-4E11-AE50-1EA3E3111001', 4, '13:00', '17:00', N'Phòng 102');
```

### 2. Test API
```bash
# Lấy lịch làm việc bác sĩ IVF
curl -X GET "http://localhost:8080/api/doctor-schedules/specialty/IVF"

# Lấy lịch bác sĩ cụ thể
curl -X GET "http://localhost:8080/api/doctor-schedules/doctor/9A1F4D4D-12E4-4E11-AE50-1EA3E3111001"

# Test gửi email
curl -X POST "http://localhost:8080/api/notifications/appointment/test-confirmation" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Nguyễn Văn A",
    "customerEmail": "test@example.com",
    "doctorName": "Dr. Nguyễn Thị Mai",
    "serviceName": "IVF",
    "appointmentDate": "25/06/2025",
    "appointmentTime": "09:00",
    "room": "Phòng 101",
    "notes": "Khám lần đầu"
  }'
```

## Lưu Ý Quan Trọng

1. **Số phòng tự động**: Hệ thống tự động lấy số phòng từ lịch làm việc
2. **Fallback**: Nếu không tìm thấy lịch làm việc, gán "Phòng chờ"
3. **Email template**: Số phòng được thêm vào phần ghi chú của email
4. **Database**: Cần có dữ liệu trong bảng `doctor_work_schedule`

## Troubleshooting

### Lỗi thường gặp:
1. **Không tìm thấy số phòng**: Kiểm tra dữ liệu trong `doctor_work_schedule`
2. **Email không gửi được**: Kiểm tra cấu hình SMTP trong `application.properties`
3. **Lỗi JPA**: Đảm bảo entity `DoctorWorkSchedule` được scan

### Debug:
```java
// Log để debug
System.out.println(">>> Lấy số phòng cho bác sĩ: " + doctorId);
System.out.println(">>> Ngày trong tuần: " + dayOfWeek);
System.out.println(">>> Thời gian: " + time);
```

## Mở rộng trong tương lai

1. **Email Reminder**: Tự động gửi email nhắc nhở trước lịch hẹn
2. **Email Cancellation**: Gửi email khi hủy lịch
3. **SMS Integration**: Gửi SMS nhắc nhở
4. **Push Notification**: Thông báo qua app mobile
5. **Email Template**: Sử dụng template engine (Thymeleaf) thay vì hardcode HTML

## Lưu ý

- Đảm bảo cấu hình SMTP đúng trong application.properties
- Test email trước khi deploy production
- Monitor log để đảm bảo email được gửi thành công
- Có thể customize template email theo yêu cầu 