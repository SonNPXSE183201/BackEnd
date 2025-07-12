package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.TreatmentSchedule;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlan;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
    void sendPasswordResetEmail(User user, String token);
    void sendAppointmentConfirmationEmail(User customer, User doctor, String serviceName, String appointmentDate, String appointmentTime, String room, String notes, String reminderMessage);

    // Bổ sung cho treatment_schedule
    void sendAppointmentReminder(TreatmentSchedule schedule, int hoursBefore);
    void sendOverdueWarning(TreatmentSchedule schedule);
    void sendTreatmentCancelled(TreatmentSchedule schedule);
    void sendScheduleNotification(User patient, User doctor, TreatmentSchedule schedule);
    
    // Bổ sung cho treatment plan
    void sendTreatmentPhasesEmail(User patient, User doctor, TreatmentPlan plan);
    void sendTreatmentCompletionEmail(User patient, User doctor, TreatmentPlan plan);
    void sendTreatmentCancelled(User patient, User doctor, TreatmentPlan plan, String reason);
}
