package com.capgemini.UniversityCourseSelection.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.capgemini.UniversityCourseSelection.services.ILoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private ILoginService loginService;

	@GetMapping("/applicant/{userName}/{password}")	
	public ResponseEntity<String> applicantLogin(@PathVariable int userName, @PathVariable String password,
			HttpServletRequest request) {
		
		Integer loggedUser = (Integer)request.getSession().getAttribute("applicant");
		if(loggedUser != null && loggedUser == userName) {
			return new ResponseEntity<String>("User already logged in!", HttpStatus.FORBIDDEN);
		}
		
		if (loginService.loginAsApplicant(userName, password))
		{
			HttpSession session = request.getSession(true);
			session.setAttribute("applicant", userName);
			return new ResponseEntity<>("Logged in successfully!",HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.FORBIDDEN);

	}
	@GetMapping("/staffMember/{userName}/{password}")	
	public ResponseEntity<String> staffLogin(@PathVariable int userName, @PathVariable String password,
			HttpServletRequest request) {
		
		Integer loggedUser = (Integer)request.getSession().getAttribute("staffMember");
		if(loggedUser != null && loggedUser == userName) {
			return new ResponseEntity<String>("User already logged in!", HttpStatus.FORBIDDEN);
		}
		
		if (loginService.loginAsUniversityStaffMember(userName, password))
		{
			HttpSession session = request.getSession(true);
			session.setAttribute("staffMember", userName);
			return new ResponseEntity<>("Logged in successfully!",HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.FORBIDDEN);
		
	}
	@GetMapping("/commitee/{userName}/{password}")	
	public ResponseEntity<String> commiteeLogin(@PathVariable int userName, @PathVariable String password,
			HttpServletRequest request) {
		
		Integer loggedUser = (Integer)request.getSession().getAttribute("commitee");
		if(loggedUser != null && loggedUser == userName) {
			return new ResponseEntity<String>("User already logged in!", HttpStatus.FORBIDDEN);
		}
		
		if (loginService.loginAsAdmissionCommiteeMember(userName, password))
		{
			HttpSession session = request.getSession(true);
			session.setAttribute("commitee", userName);
			return new ResponseEntity<>("Logged in successfully!",HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.FORBIDDEN);
		
	}
	
	@GetMapping("/logout")
	public ResponseEntity<String> logoutAll(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		if(!session.getAttributeNames().asIterator().hasNext())
			return new ResponseEntity<String>("You are already logged out!",HttpStatus.METHOD_NOT_ALLOWED);
		
		try {
		session.invalidate();
		return new ResponseEntity<String>("Logged out Successfully!",HttpStatus.OK);
		}
		catch(IllegalStateException ise) {
			return new ResponseEntity<String>("You are already logged out!",HttpStatus.METHOD_NOT_ALLOWED);
		}
		
	}

}
