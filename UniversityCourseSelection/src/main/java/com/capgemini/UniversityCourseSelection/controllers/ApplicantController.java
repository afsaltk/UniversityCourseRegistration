package com.capgemini.UniversityCourseSelection.controllers;

import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.IApplicantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/applicant")
public class ApplicantController {

	@Autowired
	private IApplicantService service;

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

	@PostMapping("/apply")
	public ResponseEntity<Applicant> applyForCourse(@RequestBody Applicant applicant) {

		Applicant temp = service.addApplicant(applicant);

		return new ResponseEntity<>(temp, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<Applicant> updateApplication(@RequestBody Applicant applicant,HttpServletRequest request) {
		
		boolean valid=checkSession(request,"applicant");
		String host = String.valueOf(request.getServerPort());
		if(!valid) {
			throw new NotLoggedInException("Please Login to update details, click " + host
					+ "/login/applicant to login");
		}
		
		if (applicant == null || applicant.getApplicantId() == null) {
			throw new NotFoundException("Applicant or Id can't be null!");
		}
		HttpSession session=request.getSession();
		if(applicant.getApplicantId()!=(int)session.getAttribute("applicant")){
			throw new NotLoggedInException("You can only update your own details");
		}
		

		Applicant temp = service.updateApplicant(applicant);

		return new ResponseEntity<>(temp, HttpStatus.OK);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<Applicant> deleteApplication(@RequestBody Applicant applicant, HttpServletRequest request) {
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click " + host
							+ "/login/commitee to login.");

		}
		if (applicant == null || applicant.getApplicantId() == null) {
			throw new NotFoundException("Applicant or Id can't be null!");
		}
		Applicant temp = service.deleteApplicant(applicant);
		return new ResponseEntity<>(temp, HttpStatus.OK);

	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Applicant> getById(@PathVariable int id, HttpServletRequest request) {

		boolean valid = checkSession(request, "applicant") || checkSession(request, "commitee")
				|| checkSession(request, "staffMember");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Kindly login to view your details.  click " + host + "/login/applicant to login.");

		}
		HttpSession session = request.getSession();

		Optional<Applicant> temp = service.viewApplicant(id);
		if (temp.isEmpty()) {
			throw new NotFoundException("No user with given Id is present");

		}
		if (checkSession(request, "applicant")) {
			int attId = (int) session.getAttribute("applicant");
			if (attId != id) {
				temp.get().setPassword("******");
			}
		} else {
			temp.get().setPassword("******");
		}
		return new ResponseEntity<>(temp.get(), HttpStatus.OK);
	}

	@GetMapping("/getAll/{status}")
	public ResponseEntity<List<Applicant>> getAllApplicants(@PathVariable int status, HttpServletRequest request) {
		boolean valid = checkSession(request, "commitee");
		String host = String.valueOf(request.getServerPort());
		if (!valid) {
			throw new NotLoggedInException(
					"Accessible to commitee members only. If you are a registered commitee member, click " + host
							+ "/login/commitee to login.");

		}

		List<Applicant> res = service.viewAllApplicantsByStatus(status);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
