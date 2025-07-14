package com.helpdesk.services.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.entities.Department;
import com.helpdesk.repositories.DepartmentRepository;

import lombok.RequiredArgsConstructor;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@PostConstruct
    public void seedDepartments() {
        if (departmentRepository.count() == 0) {
            departmentRepository.save(new Department("Technical"));
            departmentRepository.save(new Department("Finance"));
			departmentRepository.save(new Department("Marketing"));
            departmentRepository.save(new Department("Others"));
        }
    }

	@Override
	public List<DepartmentDto> getAllDepartments() {
		return departmentRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public DepartmentDto createDepartment(DepartmentDto departmentDto) {
		Department department = new Department();
		department.setName(departmentDto.getName());
		Department savedDepartment = departmentRepository.save(department);
		return convertToDto(savedDepartment);
	}

	@Override
	public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
		Optional<Department> optionalDepartment = departmentRepository.findById(id);
		if (optionalDepartment.isPresent()) {
			Department department = optionalDepartment.get();
			department.setName(departmentDto.getName());
			Department updatedDepartment = departmentRepository.save(department);
			return convertToDto(updatedDepartment);
		}
		return null;
	}

	@Override
	public void deleteDepartment(Long id) {
		departmentRepository.deleteById(id);
	}

	@Override
	public DepartmentDto getDepartmentById(Long id) {
		Optional<Department> optionalDepartment = departmentRepository.findById(id);
		return optionalDepartment.map(this::convertToDto).orElse(null);
	}

	private DepartmentDto convertToDto(Department department) {
		DepartmentDto dto = new DepartmentDto();
		dto.setId(department.getId());
		dto.setName(department.getName());
		return dto;
	}
}