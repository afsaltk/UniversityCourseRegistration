package com.capgemini.UniversityCourseSelection.services;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IAdmissionRepository;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;
import com.capgemini.UniversityCourseSelection.repo.ICourseRepository;

@Component
public class AdmissionServiceImpl implements IAdmissionService {
	
	@Autowired
	private ICourseRepository courseRepo;
	
	@Autowired
	private IAdmissionRepository admissionrepo;
	
	@Autowired
	private IApplicantRepository apprepo;
	
	@Override
	public Admission addAdmission(Admission add) {
		
		if(!apprepo.existsById(add.getApplicantId())) {
			throw new NotFoundException();
		}
		if(!courseRepo.existsById(add.getCourseId()))
			throw new NotFoundException("Course not found");
		
		Admission addadmission = add;
		return admissionrepo.save(addadmission);
	}

	@Override
	public Admission cancelAdmission(int admissionId) {
		Admission deletedadmission = null;
		if(admissionId<=0||!admissionrepo.existsById(admissionId)) {
			throw new NotFoundException();
		}
		deletedadmission = admissionrepo.findById(admissionId).get();
		admissionrepo.delete(deletedadmission);
		return deletedadmission;	
	} 

	@Override
	public Admission updateAdmission(Admission add) {
		Admission updatedadmission = null;
		
		if(!apprepo.existsById(add.getApplicantId())) {
			throw new NotFoundException();
		}
		
		if(!courseRepo.existsById(add.getCourseId()))
			throw new NotFoundException("Course not found");
		
		if(add==null||!admissionrepo.existsById(add.getAdmissionId())) {
			throw new NotFoundException();
		}
		updatedadmission = admissionrepo.save(add);
		return updatedadmission;
	}	

	@Override
	public List<Admission> showAllAdmissionByCourseId(int courseId) {
//		if(courseId<0||!admissionrepo.existsById(courseId)) {
//			throw new NotFoundException();
//		}
		List<Admission> admissionlist = admissionrepo.findAllAdmissionByCourseId(courseId);	
		return admissionlist;
	}

	@Override
	public List<Admission> showAllAdmissionbyDate(LocalDate date) {
//		if(admissionrepo.findAllAdmissionByAdmissionDate(date)==null) {
//			throw new NotFoundException();
//		}
		List<Admission> admissionlist = admissionrepo.findAllAdmissionByAdmissionDate(date);	
		return admissionlist;	
	}

}

