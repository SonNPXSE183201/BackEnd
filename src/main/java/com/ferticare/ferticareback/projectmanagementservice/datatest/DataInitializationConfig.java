package com.ferticare.ferticareback.projectmanagementservice.datatest;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializationConfig {

    private final UserService userService;
    private final ProfileRepository profileRepository;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            // Check if admin already exists
            if (userService.findByEmail("admin@example.com").isEmpty()) {
                // 1. ADMIN
                User admin = User.builder()
                        .fullName("System Admin")
                        .gender(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1980, 1, 1))
                        .email("admin@example.com")
                        .password("admin123") // nên mã hóa trong userService
                        .phone("0900000001")
                        .address("Admin HQ")
                        .build();
                User savedAdmin = userService.save(admin, RoleType.ADMIN);

                Profile adminProfile = Profile.builder()
                        .user(savedAdmin)
                        .status("active")
                        .extraPermissions("manage_users,view_reports,full_access")
                        .build();
                profileRepository.save(adminProfile);
                log.info("✅ Created admin user");
            } else {
                log.info("ℹ️ Admin user already exists, skipping creation");
            }

            // Check if customer already exists
            if (userService.findByEmail("customer@example.com").isEmpty()) {
                // 2. CUSTOMER
                User customer = User.builder()
                        .fullName("Customer A")
                        .gender(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1992, 5, 20))
                        .email("customer@example.com")
                        .password("cust123")
                        .phone("0900000002")
                        .address("Customer City")
                        .build();
                User savedCustomer = userService.save(customer, RoleType.CUSTOMER);

                Profile customerProfile = Profile.builder()
                        .user(savedCustomer)
                        .status("active")
                        .maritalStatus("Single")
                        .healthBackground("No known issues")
                        .build();
                profileRepository.save(customerProfile);
                log.info("✅ Created customer user");
            } else {
                log.info("ℹ️ Customer user already exists, skipping creation");
            }

            // Check if doctor already exists
            if (userService.findByEmail("doctor@example.com").isEmpty()) {
                // 3. DOCTOR
                User doctor = User.builder()
                        .fullName("Dr. John Doe")
                        .gender(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1985, 3, 15))
                        .email("doctor@example.com")
                        .password("doctor123")
                        .phone("0900000003")
                        .address("Doctor's Office")
                        .build();
                User savedDoctor = userService.save(doctor, RoleType.DOCTOR);

                Profile doctorProfile = Profile.builder()
                        .user(savedDoctor)
                        .status("active")
                        .specialty("Infertility Treatment")
                        .qualification("MD, PhD")
                        .experienceYears(12)
                        .rating(4.9)
                        .caseCount(150)
                        .notes("Expert in IVF and IUI procedures")
                        .build();
                profileRepository.save(doctorProfile);
                log.info("✅ Created doctor user");
            } else {
                log.info("ℹ️ Doctor user already exists, skipping creation");
            }

            // Check if manager already exists
            if (userService.findByEmail("manager@example.com").isEmpty()) {
                // 4. MANAGER
                User manager = User.builder()
                        .fullName("Manager Jane")
                        .gender(Gender.MALE)
                        .dateOfBirth(LocalDate.of(1983, 9, 10))
                        .email("manager@example.com")
                        .password("manager123")
                        .phone("0900000004")
                        .address("Manager Department")
                        .build();
                User savedManager = userService.save(manager, RoleType.MANAGER);

                Profile managerProfile = Profile.builder()
                        .user(savedManager)
                        .status("active")
                        .assignedDepartment("Fertility Services")
                        .extraPermissions("view_reports,assign_doctors")
                        .build();
                profileRepository.save(managerProfile);
                log.info("✅ Created manager user");
            } else {
                log.info("ℹ️ Manager user already exists, skipping creation");
            }

            log.info("✅ Test data initialization completed successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize test data: {}", e.getMessage(), e);
        }
    }
}