package com.vti.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/departments")
public class DepartmentController {

	@GetMapping()
	public ResponseEntity<?> getAllSalaries() {

		// fake data
		List<String> departments = new ArrayList<>();
		departments.add("Sales");
		departments.add("Marketing");
		departments.add("BOD");

		return new ResponseEntity<>(departments, HttpStatus.OK);
	}
}