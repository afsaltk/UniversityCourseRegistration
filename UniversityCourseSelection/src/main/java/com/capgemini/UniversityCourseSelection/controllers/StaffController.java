package com.capgemini.UniversityCourseSelection.controllers;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.ICourseService;
import com.capgemini.UniversityCourseSelection.services.IUniversityStaffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uni/staff")
public class StaffController {
	
	@Autowired
	private IUniversityStaffService staffService;
	@Autowired
	private ICourseService courseService;
	
	public boolean checkSession(HttpServletRequest request,String type) {
		HttpSession session =  request.getSession();

		boolean validLogin = true;
		if(session.isNew()) {
			validLogin = false;
		}
		else {
			int userId = (int) session.getAttribute(type);
            if(userId!=0)
            	validLogin=true;
		}
		return validLogin;
	}
	
	@PostMapping("/add")
	public ResponseEntity<UniversityStaffMember> addStaff(@RequestBody UniversityStaffMember staff, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
		
		if(staff == null) {
			throw new NotFoundException("Staff record or ID cannot be null!");
		}
		UniversityStaffMember ref = staffService.addStaff(staff);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<UniversityStaffMember> updateStaff(@RequestBody UniversityStaffMember staff, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
		
		if(staff == null || staff.getStaffId() == null) {
			throw new NotFoundException("Staff record or ID cannot be null!");
		}
		UniversityStaffMember ref = staffService.updateStaff(staff);
		return new ResponseEntity<>(ref, HttpStatus.OK);
	}
	
	@GetMapping("/view/{id}")
	public ResponseEntity<UniversityStaffMember> viewStaff(@PathVariable int id, HttpServletRequest request) {		
		HttpSession session = request.getSession(true);
		Integer loginStaffId = null;
		
		if(!session.isNew()) {
			loginStaffId = (int)session.getAttribute("staffMember");
		}
		
		if(loginStaffId!=null && loginStaffId==id) {
			UniversityStaffMember ref = staffService.viewStaff(id);
			return new ResponseEntity<>(ref, HttpStatus.OK);
		}
		
		UniversityStaffMember ref = staffService.viewStaff(id);
		ref.setPassword("*******");
		return new ResponseEntity<>(ref, HttpStatus.OK);	
	}
	
	@GetMapping("/view/all")
	public ResponseEntity<List<UniversityStaffMember>> viewAllStaffs() {
		List<UniversityStaffMember> ref = staffService.viewAllStaffs();
		ref.forEach(s->s.setPassword("*******"));
		return new ResponseEntity<>(ref, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> removeStaff(@PathVariable int id, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
			staffService.removeStaff(id);
			return new ResponseEntity<>("Staff with id: "+id+" deleted successfully!", HttpStatus.OK);
	}
	
	@PostMapping("/course/add")
	public ResponseEntity<Course> addCourse(@RequestBody Course course, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
		
		if(course == null) {
			throw new NotFoundException("Course record or ID cannot be null!");
		}
		return new ResponseEntity<>(courseService.addCourse(course),HttpStatus.OK);
	}
	
	@PutMapping("/course/update")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
		
		if(course == null || course.getCourseId() == null) {
			throw new NotFoundException("Course record or ID cannot be null!");
		}
		return new ResponseEntity<>(courseService.updateCourse(course),HttpStatus.OK);
	}
		
	@DeleteMapping("/course/delete/{id}")
	public ResponseEntity<String> deleteCourse(@PathVariable int id, HttpServletRequest request) {
		if(!checkSession(request, "staffMember")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to staff only. If you are a registered staff member, click http://localhost:"+port+"/login/staffMember to login.");
		}
		
		courseService.removeCourse(id);
		return new ResponseEntity<>("Course with id: "+id+" deleted successfully!",HttpStatus.OK);
	}
}
