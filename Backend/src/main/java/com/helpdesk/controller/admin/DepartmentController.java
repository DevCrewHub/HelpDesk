package com.helpdesk.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.services.admin.DepartmentService;

import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/department")
public class DepartmentController {

	private final DepartmentService departmentService;

	@GetMapping
	public ResponseEntity<?> getAllDepartments() {
		return ResponseEntity.ok(departmentService.getAllDepartments());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
		DepartmentDto department = departmentService.getDepartmentById(id);
		if (department != null) {
			return ResponseEntity.ok(department);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(Map.of("error", "Department not found", "message", "No department found with ID: " + id));
	}

	@PostMapping
	public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto) {
		DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
		if (createdDepartment == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Creation failed", "message", "Department could not be created"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) {
		DepartmentDto updatedDepartment = departmentService.updateDepartment(id, departmentDto);
		if (updatedDepartment != null) {
			return ResponseEntity.ok(updatedDepartment);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(Map.of("error", "Update failed", "message", "No department found with ID: " + id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
		try {
			departmentService.deleteDepartment(id);
			return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Delete failed", "message", e.getMessage()));
		}
	}
}