	package com.capgemini.UniversityCourseSelection.services;

import java.util.List;
import java.util.Optional;

import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;
import com.capgemini.UniversityCourseSelection.repo.ICourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ApplicantServiceImpl implements IApplicantService {
	
	@Autowired
	IApplicantRepository repo;
	
	@Autowired
	ICourseRepository courseRepo;

	@Override
	public Applicant addApplicant(Applicant app) {
		if(app.getAdmission()==null || app.getAdmission().getCourseId()==0) {
			throw new NotFoundException("Please Enter admission details!");
		}
		if(!courseRepo.existsById(app.getAdmission().getCourseId()))
			throw new NotFoundException("Course not found");
			
		Applicant temp=repo.save(app);
		temp.getAdmission().setApplicantId(temp.getApplicantId());
		return repo.save(temp);
	}

	@Override
	public Applicant updateApplicant(Applicant app) {
		if(app==null ||!repo.existsById(app.getApplicantId()))
			throw new NotFoundException("Applicant does'nt exist!");
		if(app.getAdmission()==null || app.getAdmission().getCourseId()==0) {
			throw new NotFoundException("Please Enter admission details!");
		}
		if(!courseRepo.existsById(app.getAdmission().getCourseId()))
			throw new NotFoundException("Course not found");
		return repo.save(app);
	}

	@Override
	public Applicant deleteApplicant(Applicant app) {
		if(app==null ||!repo.existsById(app.getApplicantId()))
			throw new NotFoundException("Applicant does'nt exist!");
		repo.delete(app);
		return app;
	}

	@Override
	public Optional<Applicant> viewApplicant(int id) {
		if(!repo.existsById(id))
			throw new NotFoundException("Applicant does'nt exist!");
	   Optional<Applicant> res= repo.findById(id);
	   return res;
	}

	@Override
	public List<Applicant> viewAllApplicantsByStatus(int status) {
		  return repo.viewAllApplicantByCourse(status);
	}

}
