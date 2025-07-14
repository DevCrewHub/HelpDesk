package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;

public interface DepartmentService {

	List<DepartmentDto> getAllDepartments();

	DepartmentDto createDepartment(DepartmentDto departmentDto);

	DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);

	void deleteDepartment(Long id);

	DepartmentDto getDepartmentById(Long id);
}