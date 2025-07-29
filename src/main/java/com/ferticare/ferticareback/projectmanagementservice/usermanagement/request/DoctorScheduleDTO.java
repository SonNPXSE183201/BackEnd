package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class DoctorScheduleDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String specialty;
    private List<WorkSchedule> workSchedules;

    public DoctorScheduleDTO(UUID id, String name, String email, String phone, String specialty, List<WorkSchedule> workSchedules) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialty = specialty;
        this.workSchedules = workSchedules;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSpecialty() { return specialty; }
    public List<WorkSchedule> getWorkSchedules() { return workSchedules; }

    public static class WorkSchedule {
        private Integer dayOfWeek;
        private String dayName;
        private LocalTime startTime;
        private LocalTime endTime;
        private String room;

        public WorkSchedule(Integer dayOfWeek, String dayName, LocalTime startTime, LocalTime endTime, String room) {
            this.dayOfWeek = dayOfWeek;
            this.dayName = dayName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.room = room;
        }

        // Getters
        public Integer getDayOfWeek() { return dayOfWeek; }
        public String getDayName() { return dayName; }
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public String getRoom() { return room; }
    }
}