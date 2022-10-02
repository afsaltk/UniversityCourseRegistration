package com.capgemini.UniversityCourseSelection.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.IAdmissionService;

@RestController
@RequestMapping("/admission")
public class AdmissionController {
	
	@Autowired
	public IAdmissionService service;
	
	public boolean checkSession(HttpServletRequest request, String type) {
		HttpSession session = request.getSession();

		boolean validLogin = true;
		if (session.isNew())
			return false;
		if (session.getAttribute(type) == null)
			return false;
		int userId = (int) session.getAttribute(type);
		if (userId == 0)
			validLogin = false;

		return validLogin;
	}
	
	
	@PostMapping("/addAdmission")
	public ResponseEntity<Admission> addAdmission(@RequestBody Admission admission, HttpServletRequest request) {
		boolean valid=checkSession(request,"applicant");
		String host = String.valueOf(request.getServerPort());
		if(!valid) {
			throw new NotLoggedInException("Please Login to update details, click http://localhost:" + host
					+ "/login/applicant to login");
		}
		
		if (admission == null || admission.getApplicantId() == 0) {
			throw new NotFoundException("Applicant or Id can't be null!");
		}
		HttpSession session=request.getSession();
		if(admission.getApplicantId()!=(int)session.getAttribute("applicant")){
			throw new NotLoggedInException("You can only update your own details");
		}
		
		Admission ref = service.addAdmission(admission);
		return new ResponseEntity<>(ref,HttpStatus.OK);	
	}
	
	
	@PutMapping("/updateAdmission")
    public ResponseEntity<Admission> updateAdmission(@RequestBody Admission admission,HttpServletRequest request){
		
		if (admission == null || admission.getApplicantId() == 0) {
			throw new NotFoundException("Applicant or Id can't be null!");
		} 
		
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click http://localhost: " + host
							+ "/login/commitee to login.");

		}
		
		Admission ref = service.updateAdmission(admission);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	
	@DeleteMapping("/cancelAdmission/{admissionId}")
    public ResponseEntity<Admission> cancelAdmission(@PathVariable int admissionId,HttpServletRequest request){
		
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click http://localhost:" + host
							+ "/login/commitee to login.");

		}
		
		Admission ref = service.cancelAdmission(admissionId);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	
	@GetMapping("/alladmissionbyId/{courseId}")
    public ResponseEntity<List<Admission>> showAllAdmissionByCourseId(@PathVariable int courseId,HttpServletRequest request ){
		
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click http://localhost:" + host
							+ "/login/commitee to login.");

		}
		
		List<Admission> ref = service.showAllAdmissionByCourseId(courseId);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	

	
	@GetMapping("/alladmissionbyDate/{date}/{month}/{year}")
    public ResponseEntity<List<Admission>> showAllAdmissionByDate(@PathVariable int date,@PathVariable String month,@PathVariable int year,HttpServletRequest request){
		
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click http://localhost:" + host
							+ "/login/commitee to login.");

		}
		
		DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
	            .appendPattern("dd-MMM-yyyy")
	            .toFormatter();
		String datestring  = String.valueOf(date)+"-"+month+"-"+String.valueOf(year);
		LocalDate localdate  = LocalDate.parse(datestring,dTF);
		List<Admission> ref = service.showAllAdmissionbyDate(localdate);
		return new ResponseEntity<>(ref, HttpStatus.OK);
	} 
    
}


