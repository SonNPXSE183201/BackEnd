package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service;

public interface ReminderService {
    
    /**
     * Process reminders for upcoming appointments
     * This method is scheduled to run every 5 minutes
     */
    void processReminders();
} 