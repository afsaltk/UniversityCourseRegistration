package com.capgemini.UniversityCourseSelection.services;

import java.util.List;

import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IUniversityStaffMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityStaffServiceImpl implements IUniversityStaffService{
	
	@Autowired
	private IUniversityStaffMemberRepository staffRepo;
	@Autowired
	private ICourseService courseService;
	
	@Override
	public UniversityStaffMember addStaff(UniversityStaffMember usm) {
		return staffRepo.save(usm);
	}

	@Override
	public UniversityStaffMember updateStaff(UniversityStaffMember usm) {
		if(staffRepo.existsById(usm.getStaffId())) {
			return staffRepo.save(usm);
		}
		else {
			throw new NotFoundException("Staff with id: "+usm.getStaffId()+" not found!");
		}
	}

	@Override
	public UniversityStaffMember viewStaff(int id) {
		if(staffRepo.existsById(id)) {
			return staffRepo.getReferenceById(id);
		}
		else {
			throw new NotFoundException("Staff with id: "+id+" not found!");
		}
	}

	@Override
	public void removeStaff(int id) {
		if(staffRepo.existsById(id)) {
			staffRepo.deleteById(id);
		}
		else {
			throw new NotFoundException("Staff with id: "+id+" not found!");
		}	
	}

	@Override
	public List<UniversityStaffMember> viewAllStaffs() {
		List<UniversityStaffMember> usmList = staffRepo.findAll();
		if(usmList.isEmpty())
			throw new NotFoundException("No staff records found!");
		return usmList;
	}

	@Override
	public Course addCourse(Course course) {
		return courseService.addCourse(course);
	}

	@Override
	public Course removeCourse(Course course) {
		return courseService.removeCourse(course.getCourseId());
	}

	@Override
	public Course updateCourse(Course course) {
		return courseService.updateCourse(course);
	}
  
}
