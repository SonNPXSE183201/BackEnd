package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user, RoleType role) {
        // Kiểm tra trùng email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }

        // Kiểm tra trùng số điện thoại
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng.");
        }

        // Mặc định avatar nếu để trống
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            user.setAvatarUrl("https://example.com/default-avatar.png"); // thay bằng URL mặc định của bạn
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lưu user trước để lấy ID
        User savedUser = userRepository.save(user);

        // Cấp quyền
        int roleLevel = switch (role) {
            case ADMIN -> 4;
            case MANAGER -> 3;
            case DOCTOR -> 2;
            default -> 1;
        };

        // Tạo bản ghi Role
        Role newRole = new Role();
        newRole.setUser(savedUser);
        newRole.setRoleType(role.name());
        newRole.setRoleLevel(roleLevel);
        roleRepository.save(newRole);

        return savedUser;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Optional<User> login(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }
}