package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.common.exception.AppException;
import com.ferticare.ferticareback.common.utils.DateTimeUtil;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.DepartmentDTO;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.DepartmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentDTO> getDepartmentsBySearch(String search, Pageable pageable) {
        try {
            return departmentRepository.findByNameOrDescriptionContaining(search, pageable)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            // Handle sorting issues by creating unsorted pageable
            Pageable unsortedPageable = PageRequest.of(
                pageable.getPageNumber(), 
                pageable.getPageSize()
            );
            
            // Get all departments and filter manually
            List<Department> allDepartments = departmentRepository.findAll();
            List<Department> filteredDepartments = allDepartments.stream()
                .filter(dept -> search == null || search.trim().isEmpty() ||
                    dept.getName().toLowerCase().contains(search.toLowerCase()) ||
                    (dept.getDescription() != null && dept.getDescription().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());
            
            // Apply pagination manually
            int start = (int) unsortedPageable.getOffset();
            int end = Math.min(start + unsortedPageable.getPageSize(), filteredDepartments.size());
            
            List<Department> paginatedDepartments = start < filteredDepartments.size() ? 
                filteredDepartments.subList(start, end) : 
                new ArrayList<>();
            
            List<DepartmentDTO> dtoList = paginatedDepartments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
            return new PageImpl<>(dtoList, unsortedPageable, filteredDepartments.size());
        }
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = findDepartmentById(id);
        return convertToDTO(department);
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // Check if department with same name exists
        departmentRepository.findByName(departmentDTO.getName())
                .ifPresent(d -> {
                    throw new AppException("DEPARTMENT_DUPLICATE", "Phòng ban với tên này đã tồn tại");
                });

        Department department = Department.builder()
                .name(departmentDTO.getName())
                .description(departmentDTO.getDescription())
                .location(departmentDTO.getLocation())
                .headDoctor(departmentDTO.getHeadDoctor())
                .contactInfo(departmentDTO.getContactInfo())
                .isActive(departmentDTO.getIsActive() != null ? departmentDTO.getIsActive() : true)
                .capacity(departmentDTO.getCapacity())
                .doctorCount(departmentDTO.getDoctorCount() != null ? departmentDTO.getDoctorCount() : 0)
                .patientCount(departmentDTO.getPatientCount() != null ? departmentDTO.getPatientCount() : 0)
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department existingDepartment = findDepartmentById(id);

        // Check if updated name conflicts with another department
        departmentRepository.findByName(departmentDTO.getName())
                .ifPresent(d -> {
                    if (!d.getId().equals(id)) {
                        throw new AppException("DEPARTMENT_DUPLICATE", "Phòng ban với tên này đã tồn tại");
                    }
                });

        // Update fields
        existingDepartment.setName(departmentDTO.getName());
        existingDepartment.setDescription(departmentDTO.getDescription());
        existingDepartment.setLocation(departmentDTO.getLocation());
        existingDepartment.setHeadDoctor(departmentDTO.getHeadDoctor());
        existingDepartment.setContactInfo(departmentDTO.getContactInfo());
        existingDepartment.setIsActive(departmentDTO.getIsActive());
        existingDepartment.setCapacity(departmentDTO.getCapacity());
        existingDepartment.setDoctorCount(departmentDTO.getDoctorCount());
        existingDepartment.setPatientCount(departmentDTO.getPatientCount());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return convertToDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = findDepartmentById(id);
        departmentRepository.delete(department);
    }

    @Override
    public List<DepartmentDTO> getActiveDepartments() {
        return departmentRepository.findByIsActive(true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO toggleDepartmentStatus(Long id) {
        Department department = findDepartmentById(id);
        department.setIsActive(!department.getIsActive());
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }

    @Override
    public DepartmentDTO updateDoctorCount(Long departmentId, int change) {
        Department department = findDepartmentById(departmentId);
        
        int currentCount = department.getDoctorCount() != null ? department.getDoctorCount() : 0;
        int newCount = Math.max(0, currentCount + change); // Đảm bảo không âm
        
        department.setDoctorCount(newCount);
        Department updatedDepartment = departmentRepository.save(department);
        
        return convertToDTO(updatedDepartment);
    }

    private Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new AppException("DEPARTMENT_NOT_FOUND", "Không tìm thấy phòng ban với ID: " + id));
    }

    private DepartmentDTO convertToDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .location(department.getLocation())
                .headDoctor(department.getHeadDoctor())
                .contactInfo(department.getContactInfo())
                .isActive(department.getIsActive())
                .capacity(department.getCapacity())
                .doctorCount(department.getDoctorCount())
                .patientCount(department.getPatientCount())
                .createdAt(DateTimeUtil.formatDateTime(department.getCreatedDate()))
                .updatedAt(DateTimeUtil.formatDateTime(department.getUpdatedDate()))
                .build();
    }
} 