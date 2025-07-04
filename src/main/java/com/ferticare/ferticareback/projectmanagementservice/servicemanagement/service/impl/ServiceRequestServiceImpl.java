package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.AppointmentEmailDTO;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.AppointmentEmailService;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.ReminderLog;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository.ReminderLogRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceRequestService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.DoctorWorkSchedule;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DoctorWorkScheduleRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceRequestDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.ServiceRequest;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRequestRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ProfileRepository profileRepository;
    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;
    private final JwtUtil jwtUtil;
    private final AppointmentEmailService appointmentEmailService;
    private final ReminderLogRepository reminderLogRepository;

    @Override
    public ResponseEntity<?> handleRequest(UUID userId, ServiceRequestDTO dto) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println(">>> Người dùng không tồn tại: " + userId);
            return ResponseEntity.status(404).body("Người dùng không tồn tại");
        }
        User user = userOpt.get();

        // Lấy tên chuyên môn từ ID dịch vụ
        String specialty = serviceRepository.findById(dto.getServiceId())
                .map(s -> s.getName())
                .orElse(null);

        if (specialty == null) {
            System.out.println(">>> Không tìm thấy dịch vụ: " + dto.getServiceId());
            return ResponseEntity.badRequest().body("Không tìm thấy dịch vụ.");
        }

        System.out.println(">>> Tên chuyên môn từ dịch vụ: " + specialty);

        // Log danh sách bác sĩ theo chuyên môn (đã active)
        List<Object[]> doctorRows = userRepository.findDoctorWithScheduleBySpecialty(specialty);
        System.out.println(">>> Tổng số bác sĩ có chuyên môn " + specialty + ": " + doctorRows.size());
        for (Object[] row : doctorRows) {
            System.out.println("Doctor ID: " + row[0] + ", Name: " + row[1] + ", Schedule: " + row[2]);
        }

        LocalDateTime preferredTime = dto.getAppointmentTime();

        // Validate ngày đặt lịch (phải đặt trước 1 ngày)
        if (!isValidAppointmentDate(preferredTime)) {
            System.out.println(">>> Ngày đặt lịch không hợp lệ: " + preferredTime);
            return ResponseEntity.badRequest().body("Vui lòng đặt lịch trước ít nhất 1 ngày!");
        }

        // Kiểm tra duplicate request (cùng customer, service, thời gian)
        boolean duplicateExists = requestRepository.existsByCustomerIdAndServiceIdAndPreferredDatetime(
                userId, dto.getServiceId(), preferredTime);
        
        if (duplicateExists) {
            System.out.println(">>> Request trùng lặp: customer=" + userId + ", service=" + dto.getServiceId() + ", time=" + preferredTime);
            return ResponseEntity.badRequest().body("Bạn đã có lịch hẹn cho dịch vụ này vào thời gian này rồi!");
        }

        // Tạo bản ghi yêu cầu
        ServiceRequest request = new ServiceRequest();
        request.setCustomerId(userId);
        request.setServiceId(dto.getServiceId());
        request.setPreferredDatetime(preferredTime);
        request.setNote(dto.getNote());
        request.setDoctorSelection(dto.getDoctorSelection());
        request.setPreferredDoctorId("manual".equalsIgnoreCase(dto.getDoctorSelection()) ? dto.getDoctorId() : null);
        request.setStatus("PendingAssignment");
        requestRepository.save(request);

        UUID assignedDoctorId;

        if ("manual".equalsIgnoreCase(dto.getDoctorSelection())) {
            System.out.println(">>> Chế độ chọn bác sĩ: THỦ CÔNG");
            boolean isBusy = appointmentRepository.existsByDoctorIdAndAppointmentTime(dto.getDoctorId(), preferredTime);
            System.out.println(">>> Bác sĩ " + dto.getDoctorId() + " có bị trùng giờ " + preferredTime + ": " + isBusy);
            if (isBusy) {
                return ResponseEntity.badRequest().body("Bác sĩ đã bị trùng lịch. Vui lòng chọn giờ khác");
            }
            assignedDoctorId = dto.getDoctorId();
        } else {
            System.out.println(">>> Chế độ chọn bác sĩ: TỰ ĐỘNG");
            List<UUID> busyDoctorIds = appointmentRepository.findDoctorIdsBusyAt(preferredTime);
            System.out.println(">>> Bác sĩ bận tại " + preferredTime + ": " + busyDoctorIds);

            // Gọi danh sách bác sĩ không bận
            PageRequest pageRequest = PageRequest.of(0, 1);
            List<User> availableDoctors = userRepository.findFirstAvailableDoctorExcluding(
                    busyDoctorIds.isEmpty() ? List.of(UUID.randomUUID()) : busyDoctorIds,
                    specialty,
                    pageRequest
            );

            System.out.println(">>> Số bác sĩ rảnh: " + availableDoctors.size());
            availableDoctors.forEach(doc -> System.out.println("Rảnh: " + doc.getId() + " - " + doc.getFullName()));

            if (availableDoctors.isEmpty()) {
                return ResponseEntity.badRequest().body("Không có bác sĩ phù hợp rảnh vào giờ này.");
            }
            assignedDoctorId = availableDoctors.get(0).getId();
        }

        // Cập nhật preferredDoctorId trong ServiceRequest với bác sĩ đã được assign
        request.setPreferredDoctorId(assignedDoctorId);
        request.setStatus("Scheduled");
        requestRepository.save(request);

        // Tạo lịch hẹn
        createAppointment(assignedDoctorId, userId, preferredTime, request.getRequestId());

        // Gửi email xác nhận lịch hẹn
        try {
            User doctor = userRepository.findById(assignedDoctorId).orElse(null);
            if (doctor != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                
                String appointmentDate = preferredTime.toLocalDate().format(dateFormatter);
                String appointmentTime = preferredTime.toLocalTime().format(timeFormatter);
                
                // Lấy số phòng từ lịch làm việc của bác sĩ
                String room = getRoomForDoctor(assignedDoctorId, preferredTime);
                
                AppointmentEmailDTO emailDTO = AppointmentEmailDTO.builder()
                    .customerName(user.getFullName())
                    .customerEmail(user.getEmail())
                    .doctorName(doctor.getFullName())
                    .serviceName(specialty)
                    .appointmentDate(appointmentDate)
                    .appointmentTime(appointmentTime)
                    .room(room)
                    .notes(dto.getNote())
                    .status("Scheduled")
                    .build();
                
                appointmentEmailService.sendAppointmentConfirmation(emailDTO);
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email xác nhận lịch hẹn: " + e.getMessage());
            // Không throw exception để không ảnh hưởng đến quá trình đăng ký lịch
        }

        // Lấy thông tin đầy đủ từ customer profile
        Optional<Profile> customerProfileOpt = profileRepository.findByUser_Id(userId);
        Profile customerProfile = customerProfileOpt.orElse(null);
        
        // Lấy thông tin đầy đủ từ doctor profile  
        User doctor = userRepository.findById(assignedDoctorId).orElse(null);
        Optional<Profile> doctorProfileOpt = doctor != null ? profileRepository.findByUser_Id(doctor.getId()) : Optional.empty();
        Profile doctorProfile = doctorProfileOpt.orElse(null);
        
        // Tạo response với thông tin đầy đủ
        Map<String, Object> response = new HashMap<>();
        response.put("id", request.getRequestId());
        
        // Customer info từ profile
        Map<String, Object> customerInfo = new HashMap<>();
        customerInfo.put("fullName", user.getFullName());
        customerInfo.put("email", user.getEmail());
        customerInfo.put("phone", user.getPhone());
        customerInfo.put("address", user.getAddress());
        customerInfo.put("gender", user.getGender() != null ? user.getGender().name() : null);
        customerInfo.put("dateOfBirth", user.getDateOfBirth());
        customerInfo.put("avatarUrl", user.getAvatarUrl());
        
        if (customerProfile != null) {
            customerInfo.put("maritalStatus", customerProfile.getMaritalStatus());
            customerInfo.put("healthBackground", customerProfile.getHealthBackground());
        }
        response.put("customer", customerInfo);
        
        // Service info
        response.put("service", Map.of("id", dto.getServiceId(), "name", specialty));
        
        // Doctor info từ profile
        if (doctor != null) {
            Map<String, Object> doctorInfo = new HashMap<>();
            doctorInfo.put("id", doctor.getId());
            doctorInfo.put("fullName", doctor.getFullName());
            doctorInfo.put("email", doctor.getEmail());
            doctorInfo.put("phone", doctor.getPhone());
            doctorInfo.put("gender", doctor.getGender() != null ? doctor.getGender().name() : null);
            doctorInfo.put("avatarUrl", doctor.getAvatarUrl());
            
            if (doctorProfile != null) {
                doctorInfo.put("specialty", doctorProfile.getSpecialty());
                doctorInfo.put("qualification", doctorProfile.getQualification());
                doctorInfo.put("experienceYears", doctorProfile.getExperienceYears());
                doctorInfo.put("rating", doctorProfile.getRating());
                doctorInfo.put("caseCount", doctorProfile.getCaseCount());
            }
            response.put("doctor", doctorInfo);
        } else {
            response.put("doctor", null);
        }
        
        // Appointment info
        response.put("appointmentDate", preferredTime != null ? preferredTime.toLocalDate() : null);
        response.put("appointmentTime", preferredTime != null ? preferredTime.toLocalTime() : null);
        response.put("notes", dto.getNote());
        response.put("status", request.getStatus());
        response.put("doctorSelection", dto.getDoctorSelection());
        response.put("createdAt", request.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    private void createAppointment(UUID doctorId, UUID customerId, LocalDateTime time, UUID requestId) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(UUID.randomUUID());
        appointment.setCustomerId(customerId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentTime(time);
        appointment.setRequestId(requestId);
        
        // Lấy số phòng từ lịch làm việc của bác sĩ
        String room = getRoomForDoctor(doctorId, time);
        appointment.setRoom(room);
        
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setCheckInStatus("Pending");
        appointmentRepository.save(appointment);
        
        // Tạo ReminderLog entries cho 24h và 2h reminders
        createReminderLogs(appointment.getAppointmentId(), time);
    }

    /**
     * Tạo ReminderLog entries cho appointment
     */
    private void createReminderLogs(UUID appointmentId, LocalDateTime appointmentTime) {
        try {
            // Tạo reminder 24 giờ trước
            ReminderLog reminder24h = ReminderLog.builder()
                .reminderId(UUID.randomUUID())
                .appointmentId(appointmentId)
                .reminderTime(appointmentTime.minusHours(24))
                .channel("EMAIL")
                .status("PENDING")
                .build();
            reminderLogRepository.save(reminder24h);
            
            // Tạo reminder 2 giờ trước
            ReminderLog reminder2h = ReminderLog.builder()
                .reminderId(UUID.randomUUID())
                .appointmentId(appointmentId)
                .reminderTime(appointmentTime.minusHours(2))
                .channel("EMAIL")
                .status("PENDING")
                .build();
            reminderLogRepository.save(reminder2h);
            
            System.out.println("✅ Đã tạo ReminderLog cho appointment " + appointmentId + 
                             " - 24h: " + reminder24h.getReminderTime() + 
                             " - 2h: " + reminder2h.getReminderTime());
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo ReminderLog: " + e.getMessage());
        }
    }

    /**
     * Lấy số phòng cho bác sĩ dựa trên lịch làm việc
     */
    private String getRoomForDoctor(UUID doctorId, LocalDateTime appointmentTime) {
        try {
            // Lấy ngày trong tuần (2=Thứ 2, 3=Thứ 3, ..., 8=Chủ nhật)
            int dayOfWeek = appointmentTime.getDayOfWeek().getValue();
            LocalTime time = appointmentTime.toLocalTime();
            
            System.out.println("🔍 Tìm phòng cho bác sĩ " + doctorId + " vào " + dayOfWeek + " lúc " + time);
            
            // Tìm lịch làm việc của bác sĩ trong ngày này
            List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
            
            System.out.println("📅 Tìm thấy " + schedules.size() + " lịch làm việc");
            
            // Tìm lịch phù hợp với thời gian
            for (DoctorWorkSchedule schedule : schedules) {
                if (time.isAfter(schedule.getStartTime().minusMinutes(1)) && 
                    time.isBefore(schedule.getEndTime().plusMinutes(1))) {
                    System.out.println("✅ Tìm thấy phòng: " + schedule.getRoom());
                    return schedule.getRoom();
                }
            }
            
            // Nếu không tìm thấy, trả về phòng mặc định
            System.out.println("⚠️ Không tìm thấy phòng, dùng phòng mặc định");
            return "Phòng chờ";
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy số phòng cho bác sĩ " + doctorId + ": " + e.getMessage());
            return "Phòng chờ";
        }
    }

    /**
     * Validate ngày đặt lịch (phải đặt trước 1 ngày)
     */
    private boolean isValidAppointmentDate(LocalDateTime appointmentTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        
        return appointmentTime.isAfter(tomorrow) || appointmentTime.toLocalDate().isAfter(now.toLocalDate());
    }

    /**
     * Lấy lịch làm việc của bác sĩ cụ thể
     */
    @Override
    public ResponseEntity<?> getDoctorSchedule(UUID doctorId) {
        try {
            List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorId(doctorId);
            
            User doctor = userRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("doctorId", doctorId);
            result.put("doctorName", doctor.getFullName());
            
            List<Map<String, Object>> scheduleList = schedules.stream()
                .map(schedule -> {
                    Map<String, Object> scheduleMap = new HashMap<>();
                    scheduleMap.put("dayOfWeek", schedule.getDayOfWeek());
                    scheduleMap.put("dayName", getDayName(schedule.getDayOfWeek()));
                    scheduleMap.put("startTime", schedule.getStartTime());
                    scheduleMap.put("endTime", schedule.getEndTime());
                    scheduleMap.put("room", schedule.getRoom());
                    return scheduleMap;
                })
                .collect(Collectors.toList());
            
            result.put("schedules", scheduleList);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy lịch làm việc của bác sĩ: " + e.getMessage());
            return ResponseEntity.status(500).body("Lỗi khi lấy lịch làm việc");
        }
    }

    /**
     * Chuyển đổi số ngày thành tên ngày
     */
    private String getDayName(Integer dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return "Thứ 2";
            case 3: return "Thứ 3";
            case 4: return "Thứ 4";
            case 5: return "Thứ 5";
            case 6: return "Thứ 6";
            case 7: return "Thứ 7";
            case 8: return "Chủ nhật";
            default: return "Không xác định";
        }
    }

    private String getServiceNameById(UUID serviceId) {
        return serviceRepository.findById(serviceId)
                .map(s -> s.getName())  // hoặc s -> ((com.ferticare...)s).getName()
                .orElse("Unknown Service");
    }

    /**
     * Lấy danh sách bác sĩ rảnh theo dịch vụ
     */
    @Override
    public ResponseEntity<?> getAvailableDoctorsByService(UUID serviceId) {
        try {
            // Lấy tên chuyên môn từ ID dịch vụ
            String specialty = serviceRepository.findById(serviceId)
                    .map(s -> s.getName())
                    .orElse(null);
                    
            if (specialty == null) {
                return ResponseEntity.badRequest().body("Không tìm thấy dịch vụ.");
            }
            
            // Lấy tất cả bác sĩ có chuyên ngành này và đang active
            List<Object[]> doctorRows = userRepository.findDoctorWithScheduleBySpecialty(specialty);
            
            List<Map<String, Object>> doctors = new ArrayList<>();
            
            for (Object[] row : doctorRows) {
                UUID doctorId = (UUID) row[0];
                String doctorName = (String) row[1];
                
                // Lấy lịch làm việc mới từ bảng doctor_work_schedule
                List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorId(doctorId);
                
                if (!schedules.isEmpty()) {
                    Map<String, Object> doctor = new HashMap<>();
                    doctor.put("id", doctorId);
                    doctor.put("name", doctorName);
                    doctor.put("specialty", specialty);
                    
                    // Chuyển đổi lịch làm việc thành format mới
                    List<Map<String, Object>> workSchedule = schedules.stream()
                        .map(schedule -> {
                            Map<String, Object> scheduleMap = new HashMap<>();
                            scheduleMap.put("dayOfWeek", schedule.getDayOfWeek());
                            scheduleMap.put("dayName", getDayName(schedule.getDayOfWeek()));
                            scheduleMap.put("startTime", schedule.getStartTime().toString());
                            scheduleMap.put("endTime", schedule.getEndTime().toString());
                            scheduleMap.put("room", schedule.getRoom());
                            return scheduleMap;
                        })
                        .collect(Collectors.toList());
                    
                    doctor.put("workSchedule", workSchedule);
                    doctors.add(doctor);
                }
            }
            
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách bác sĩ: " + e.getMessage());
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách bác sĩ");
        }
    }
    
    /**
     * Lấy thời gian rảnh của bác sĩ trong ngày
     */
    @Override
    public ResponseEntity<?> getDoctorAvailableTimes(UUID doctorId, LocalDate date) {
        if (date != null) {
            // Logic cũ: trả về slot của 1 ngày
            try {
                int dayOfWeek = date.getDayOfWeek().getValue();
                List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
                if (schedules.isEmpty()) {
                    return ResponseEntity.ok(List.of());
                }
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(23, 59, 59);
                List<LocalDateTime> bookedTimes = appointmentRepository.findAppointmentTimesByDoctorIdAndDateRange(
                        doctorId, startOfDay, endOfDay);
                List<Map<String, Object>> availableTimes = new ArrayList<>();
                for (DoctorWorkSchedule schedule : schedules) {
                    LocalTime startTime = schedule.getStartTime();
                    LocalTime endTime = schedule.getEndTime();
                    for (LocalTime currentTime = startTime; currentTime.isBefore(endTime); currentTime = currentTime.plusHours(1)) {
                        LocalDateTime dateTime = date.atTime(currentTime);
                        final LocalTime finalCurrentTime = currentTime;
                        boolean isAvailable = bookedTimes.stream()
                                .noneMatch(bookedTime -> bookedTime.toLocalTime().equals(finalCurrentTime));
                        if (isAvailable) {
                            Map<String, Object> slot = new HashMap<>();
                            slot.put("time", currentTime.toString());
                            slot.put("dateTime", dateTime.toString());
                            slot.put("room", schedule.getRoom());
                            availableTimes.add(slot);
                        }
                    }
                }
                return ResponseEntity.ok(availableTimes);
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi lấy thời gian rảnh: " + e.getMessage());
                return ResponseEntity.status(500).body("Lỗi khi lấy thời gian rảnh");
            }
        } else {
            // Logic mới: trả về slot của 30 ngày tới (bắt đầu từ ngày mai)
            try {
                List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorId(doctorId);
                if (schedules.isEmpty()) {
                    return ResponseEntity.ok(List.of());
                }
                LocalDate today = LocalDate.now();
                LocalDate endDate = today.plusDays(30);
                List<Map<String, Object>> allAvailableSlots = new ArrayList<>();
                for (LocalDate d = today.plusDays(1); !d.isAfter(endDate); d = d.plusDays(1)) {
                    int dayOfWeek = d.getDayOfWeek().getValue();
                    List<DoctorWorkSchedule> daySchedules = schedules.stream()
                        .filter(sch -> sch.getDayOfWeek().equals(dayOfWeek))
                        .toList();
                    if (daySchedules.isEmpty()) continue;
                    LocalDateTime startOfDay = d.atStartOfDay();
                    LocalDateTime endOfDay = d.atTime(23, 59, 59);
                    List<LocalDateTime> bookedTimes = appointmentRepository.findAppointmentTimesByDoctorIdAndDateRange(
                            doctorId, startOfDay, endOfDay);
                    for (DoctorWorkSchedule schedule : daySchedules) {
                        LocalTime startTime = schedule.getStartTime();
                        LocalTime endTime = schedule.getEndTime();
                        for (LocalTime currentTime = startTime; currentTime.isBefore(endTime); currentTime = currentTime.plusHours(1)) {
                            LocalDateTime dateTime = d.atTime(currentTime);
                            final LocalTime finalCurrentTime = currentTime;
                            boolean isAvailable = bookedTimes.stream()
                                    .noneMatch(bookedTime -> bookedTime.toLocalTime().equals(finalCurrentTime));
                            if (isAvailable) {
                                Map<String, Object> slot = new HashMap<>();
                                slot.put("date", d.toString());
                                slot.put("time", currentTime.toString());
                                slot.put("dateTime", dateTime.toString());
                                slot.put("room", schedule.getRoom());
                                allAvailableSlots.add(slot);
                            }
                        }
                    }
                }
                return ResponseEntity.ok(allAvailableSlots);
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi lấy thời gian rảnh nhiều ngày: " + e.getMessage());
                return ResponseEntity.status(500).body("Lỗi khi lấy thời gian rảnh nhiều ngày");
            }
        }
    }

    // New method with JWT authentication handling
    @Override
    public ResponseEntity<?> handleRequestWithAuth(ServiceRequestDTO dto, HttpServletRequest request) {
        try {
            String jwt = jwtUtil.extractJwtFromRequest(request);
            String userId = jwtUtil.extractUserId(jwt);
            return handleRequest(UUID.fromString(userId), dto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Yêu cầu không hợp lệ: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách ngày có thể đặt lịch trong 30 ngày tới
     */
    @Override
    public ResponseEntity<?> getAvailableDates(UUID doctorId) {
        try {
            // Lấy lịch làm việc của bác sĩ
            List<DoctorWorkSchedule> schedules = doctorWorkScheduleRepository.findByDoctorId(doctorId);
            
            if (schedules.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(30);
            
            List<Map<String, Object>> availableDates = new ArrayList<>();
            
            // Duyệt qua 30 ngày tới
            for (LocalDate date = today.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
                int dayOfWeek = date.getDayOfWeek().getValue();
                
                // Kiểm tra xem bác sĩ có làm việc vào ngày này không
                boolean hasSchedule = schedules.stream()
                    .anyMatch(schedule -> schedule.getDayOfWeek().equals(dayOfWeek));
                
                if (hasSchedule) {
                    Map<String, Object> dateInfo = new HashMap<>();
                    dateInfo.put("date", date.toString());
                    dateInfo.put("dayOfWeek", dayOfWeek);
                    dateInfo.put("dayName", getDayName(dayOfWeek));
                    
                    // Lấy thông tin lịch làm việc trong ngày
                    List<DoctorWorkSchedule> daySchedules = schedules.stream()
                        .filter(schedule -> schedule.getDayOfWeek().equals(dayOfWeek))
                        .collect(Collectors.toList());
                    
                    List<Map<String, Object>> timeSlots = daySchedules.stream()
                        .map(schedule -> {
                            Map<String, Object> slot = new HashMap<>();
                            slot.put("startTime", schedule.getStartTime().toString());
                            slot.put("endTime", schedule.getEndTime().toString());
                            slot.put("room", schedule.getRoom());
                            return slot;
                        })
                        .collect(Collectors.toList());
                    
                    dateInfo.put("timeSlots", timeSlots);
                    availableDates.add(dateInfo);
                }
            }
            
            return ResponseEntity.ok(availableDates);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy danh sách ngày có thể đặt lịch: " + e.getMessage());
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách ngày");
        }
    }

    /**
     * Lấy lịch hẹn thực tế của bác sĩ theo ngày
     */
    @Override
    public ResponseEntity<?> getDoctorAppointments(UUID doctorId, LocalDate date) {
        try {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            
            // Lấy danh sách appointment của bác sĩ trong ngày
            List<LocalDateTime> appointmentTimes = appointmentRepository.findAppointmentTimesByDoctorIdAndDateRange(
                    doctorId, startOfDay, endOfDay);
            
            List<Map<String, Object>> appointments = new ArrayList<>();
            
            for (LocalDateTime appointmentTime : appointmentTimes) {
                // Lấy thông tin chi tiết appointment
                List<Appointment> appointmentList = appointmentRepository.findAll().stream()
                    .filter(a -> a.getDoctorId().equals(doctorId) && a.getAppointmentTime().equals(appointmentTime))
                    .collect(Collectors.toList());
                
                if (!appointmentList.isEmpty()) {
                    Appointment appointment = appointmentList.get(0);
                    
                    // Lấy thông tin khách hàng
                    User customer = userRepository.findById(appointment.getCustomerId()).orElse(null);
                    
                    Map<String, Object> appointmentInfo = new HashMap<>();
                    appointmentInfo.put("appointmentId", appointment.getAppointmentId());
                    appointmentInfo.put("appointmentTime", appointment.getAppointmentTime());
                    appointmentInfo.put("room", appointment.getRoom());
                    appointmentInfo.put("checkInStatus", appointment.getCheckInStatus());
                    
                    if (customer != null) {
                        Map<String, Object> customerInfo = new HashMap<>();
                        customerInfo.put("name", customer.getFullName());
                        customerInfo.put("phone", customer.getPhone());
                        customerInfo.put("email", customer.getEmail());
                        appointmentInfo.put("customer", customerInfo);
                    }
                    
                    appointments.add(appointmentInfo);
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("date", date.toString());
            result.put("doctorId", doctorId);
            result.put("totalAppointments", appointments.size());
            result.put("appointments", appointments);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy lịch hẹn của bác sĩ: " + e.getMessage());
            return ResponseEntity.status(500).body("Lỗi khi lấy lịch hẹn");
        }
    }
}