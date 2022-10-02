package com.capgemini.UniversityCourseSelection.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.AdmissionCommiteeMember;
import com.capgemini.UniversityCourseSelection.entities.AdmissionStatus;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.IAdmissionCommiteeMemberService;

@RestController
@RequestMapping("/uni/committee")
public class AdmissionCommitteMemberController {

	@Autowired
	private IAdmissionCommiteeMemberService committeService;

	private boolean checkSession(HttpServletRequest request, String type) {
		HttpSession session = request.getSession();

		boolean validLogin = true;
		if (session.isNew()) {
			validLogin = false;
		} 
		else if( session.getAttribute(type) == null)
		{
			validLogin = false;
		}
		else 
		{
			int userId = (int) session.getAttribute(type);
			if (userId != 0)
				validLogin = true;
		}
		return validLogin;
	}


	@PostMapping("/add")
	public ResponseEntity<AdmissionCommiteeMember> addAddmissionCommitteeMember(
			@RequestBody AdmissionCommiteeMember member, HttpServletRequest request) {
		AdmissionCommiteeMember mem = null;

		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}
		
		if (member == null) {
			throw new NotFoundException("Committee Member is Null !");
		}
		mem = committeService.addCommitteeMember(member);
		return new ResponseEntity<>(mem, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<AdmissionCommiteeMember> updateCommitteeMember(@RequestBody AdmissionCommiteeMember member, HttpServletRequest request) {
		AdmissionCommiteeMember mem = null;
		
		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}
		
		if (member == null) {
			throw new NotFoundException("Committee Member is Null !");
		}
		mem = committeService.updateCommitteeMember(member);
		return new ResponseEntity<>(mem, null, HttpStatus.OK);
	}

	@GetMapping("/view/{id}")
	public ResponseEntity<AdmissionCommiteeMember> viewCommitteeMemberById( @PathVariable int id, HttpServletRequest request) {
		AdmissionCommiteeMember member = null;
		
		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}
		
		HttpSession session = request.getSession();
		int LoginId = (int) session.getAttribute("commitee");
		
		member = committeService.viewCommitteeMember(id);

		if(LoginId!= id)
		{
			member.setAdminPassword("********");
		}
		
		return new ResponseEntity<>(member, null, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCommitteeMember(@PathVariable int id, HttpServletRequest request) {
		
		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}
		
		try {
			committeService.removeCommitteeMember(id);
		} catch (Exception e) {
			return new ResponseEntity<>("Commitee Member with id: " + id + " not found! ", null, HttpStatus.OK);
		}
		return new ResponseEntity<>("Commitee member deleted successfully!", null, HttpStatus.OK);
	}

	@GetMapping("/viewAll")
	public ResponseEntity<List<AdmissionCommiteeMember>> viewAllCommiteeMembers(HttpServletRequest request) {
		
		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}
		
		List<AdmissionCommiteeMember> l = committeService.viewAllCommitteeMembers();
		l.forEach(s->s.setAdminPassword("*******"));
		return new ResponseEntity<>(l, null, HttpStatus.OK);
	}

	@PostMapping("/getResult")
	public ResponseEntity<AdmissionStatus> getAdmissionResult(@RequestBody Applicant applicant, HttpServletRequest request) {
		
		if(!checkSession(request, "commitee")) {
			String port = String.valueOf(request.getServerPort());			
			throw new NotLoggedInException("Accessible to commitee only. If you are a registered commitee member, click http://localhost:"+port+"/login/commitee to login.");
		}

		Admission admission = applicant.getAdmission();

		AdmissionStatus status = null;

		if (applicant == null || admission == null) {
			throw new NotFoundException("Applicant or Admission is Null !");
		}
		
		status = committeService.provideAdmissionResult(applicant, admission);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

}
