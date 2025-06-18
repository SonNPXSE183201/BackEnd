package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

public class DoctorScheduleDTO {
    private UUID id;
    private String name;
    private List<WorkDay> schedule;

    public DoctorScheduleDTO(UUID id, String name, String rawJson) {
        this.id = id;
        this.name = name;
        this.schedule = parseJsonSchedule(rawJson);
    }

    private List<WorkDay> parseJsonSchedule(String rawJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(rawJson, new TypeReference<List<WorkDay>>() {});
        } catch (Exception e) {
            return List.of(); // fallback empty nếu lỗi
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<WorkDay> getSchedule() { return schedule; }

    public static class WorkDay {
        public String date;
        public List<String> slots;
    }
}