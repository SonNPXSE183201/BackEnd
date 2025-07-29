package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    
    Page<DepartmentDTO> getDepartmentsBySearch(String search, Pageable pageable);
    
    DepartmentDTO getDepartmentById(Long id);
    
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    
    void deleteDepartment(Long id);
    
    List<DepartmentDTO> getActiveDepartments();
    
    DepartmentDTO toggleDepartmentStatus(Long id);
    
    /**
     * Cập nhật số lượng bác sĩ trong phòng ban
     * 
     * @param departmentId ID của phòng ban
     * @param change Số lượng thay đổi (+1 để thêm, -1 để bớt)
     * @return DepartmentDTO sau khi cập nhật
     */
    DepartmentDTO updateDoctorCount(Long departmentId, int change);
} 