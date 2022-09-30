package com.capgemini.UniversityCourseSelection.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.AdmissionCommiteeMember;
import com.capgemini.UniversityCourseSelection.entities.AdmissionStatus;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IAdmissionCommiteeMemberRepository;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;

@Component
public class AdmissionCommitteeMemberServiceImpl implements IAdmissionCommiteeMemberService {

	@Autowired
	private IAdmissionCommiteeMemberRepository repo;

	@Autowired
	private IApplicantRepository applicantRepo;

	@Override
	public AdmissionCommiteeMember addCommitteeMember(AdmissionCommiteeMember member) {
		return repo.save(member);
	}

	@Override
	@Transactional
	public AdmissionCommiteeMember updateCommitteeMember(AdmissionCommiteeMember member) {
		if (repo.existsById(member.getAdminId())) {
			return repo.save(member);
		} else {
			throw new NotFoundException("AdmissionCommitteeMember not available");
		}
	}

	@Override
	public AdmissionCommiteeMember viewCommitteeMember(int id) {
		if (repo.existsById(id)) {
			return repo.findById(id).get();
		} else {
			throw new NotFoundException("AdmissionCommittee Member not found !");
		}
	}

	@Override
	@Transactional
	public void removeCommitteeMember(int id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new NotFoundException("No member exists with id:" + id);
		}
	}

	@Override
	public List<AdmissionCommiteeMember> viewAllCommitteeMembers() {
		List<AdmissionCommiteeMember> l = repo.findAll();
		return l;
	}

	@Override
	public AdmissionStatus provideAdmissionResult(Applicant applicant, Admission admission) {

//		 get the course object
		Course course = null;
		int id = admission.getCourseId();
		course = repo.getCourseById(id);

		// if course is not found, return admission status as rejected/pending
		if (course == null) {
			applicant.setStatus(AdmissionStatus.PENDING);
			admission.setStatus(AdmissionStatus.PENDING);
		}

		// criteria 1 (admission date)
		LocalDate admissionDate = admission.getAdmissionDate();
		LocalDate courseStartDate = course.getCourseStartDate();

		if (admissionDate.isAfter(courseStartDate)) {
			applicant.setStatus(AdmissionStatus.REJECTED);
			admission.setStatus(AdmissionStatus.REJECTED);
			
			applicantRepo.save(applicant);
			return applicant.getStatus();
		}
		// criteria 1 satisfied
		
		
		// criteria 2 ( percentage )

		double courseCriteria = course.getCourseCriteria();
		double marks = applicant.getApplicantGraduationPercentage();

		if (marks < courseCriteria) {
			applicant.setStatus(AdmissionStatus.PENDING);
			admission.setStatus(AdmissionStatus.PENDING);
		} else {
			admission.setStatus(AdmissionStatus.CONFIRMED);
			applicant.setStatus(AdmissionStatus.CONFIRMED);
		}

		// criteria 2 satisfied
		applicantRepo.save(applicant);
		return applicant.getStatus();

	}

}
