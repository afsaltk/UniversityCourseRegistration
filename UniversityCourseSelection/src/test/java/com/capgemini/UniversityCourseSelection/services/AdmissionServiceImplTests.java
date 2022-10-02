package com.capgemini.UniversityCourseSelection.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IAdmissionRepository;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;
import com.capgemini.UniversityCourseSelection.repo.ICourseRepository;

@ExtendWith(MockitoExtension.class)
class AdmissionServiceImplTests {
	
	@Mock
	private IAdmissionRepository admission_repo;
	
	@Mock
	private ICourseRepository course_repo;
	
	@Mock
	private IApplicantRepository applicant_repo;
	
	@InjectMocks
	private AdmissionServiceImpl admission_service;
	
	static Admission add1 = new Admission();
	static Admission add2 = new Admission();
	static Admission add3 = new Admission();
	static DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
		                                  .appendPattern("dd-MMM-yyyy")
		                                  .toFormatter();
	@BeforeAll
	static void initMethod() {
	add1.setAdmissionId(1);
	add1.setApplicantId(2);
	add1.setCourseId(3);
	add1.setAdmissionDate(LocalDate.parse("10-Sep-2020",dTF));
	add2.setAdmissionId(4);
	add2.setApplicantId(5);
	add2.setCourseId(100);
	add2.setAdmissionDate(LocalDate.parse("10-Sep-2020",dTF));
	add3.setAdmissionId(7);
	add3.setApplicantId(8);
	add3.setCourseId(6);
	add3.setAdmissionDate(LocalDate.parse("12-Sep-2020",dTF));
	}

	@Test
	void testAddAdmission_success() {
		Mockito.when(applicant_repo.existsById(add1.getApplicantId())).thenReturn(true);
		Mockito.when(course_repo.existsById(add1.getCourseId())).thenReturn(true);
		Mockito.when(admission_repo.save(add1)).thenReturn(add1);
		assertEquals(add1, admission_service.addAdmission(add1));
	}
	
	
	@Test
	void testUpdateAdmission_success() {
		Mockito.when(applicant_repo.existsById(add2.getApplicantId())).thenReturn(true);
		Mockito.when(course_repo.existsById(add2.getCourseId())).thenReturn(true);
		Mockito.when(admission_repo.save(add2)).thenReturn(add2);
		Mockito.when(admission_repo.existsById(add2.getAdmissionId())).thenReturn(true);
		assertEquals(add2, admission_service.updateAdmission(add2));
	}
	
	
	@Test
	void testCancelAdmission_success() {
		boolean success = true;
		Mockito.when(admission_repo.existsById(add1.getAdmissionId())).thenReturn(true);
		Mockito.when(admission_repo.findById(add1.getAdmissionId())).thenReturn(Optional.ofNullable(add1));
		try {
			admission_service.cancelAdmission(add1.getAdmissionId());
		}
		catch(NotFoundException ex) {
			success = false;
		}
		assertEquals(true,success);
	}
	
	
	@Test
	void testShowAllAdmissionByCourseId_success() {
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add2);
		admissionlist.add(add3);
				
		Mockito.when(admission_repo.findAllAdmissionByCourseId(add2.getCourseId())).thenReturn(admissionlist);
		assertEquals(admissionlist,admission_service.showAllAdmissionByCourseId(add2.getCourseId()));
	}
	
	@Test
	void testShowAllAdmissionByDate_success() {
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add1);
		admissionlist.add(add2);
		
		Mockito.when(admission_repo.findAllAdmissionByAdmissionDate(add1.getAdmissionDate())).thenReturn(admissionlist);
		assertEquals(admissionlist,admission_service.showAllAdmissionbyDate(add1.getAdmissionDate()));
	}
	
	
	@Test
	void testAddAdmission_failure() {
		Mockito.when(applicant_repo.existsById(add2.getApplicantId())).thenReturn(false);
		assertThrows(NotFoundException.class,()->{admission_service.addAdmission(add2);});
	}
	
	
	@Test
	void testUpdateAdmission_failure() {
		//Mockito.when(admission_repo.save(add2)).thenThrow(new NotFoundException());
		Mockito.when(applicant_repo.existsById(add2.getApplicantId())).thenReturn(true);
		Mockito.when(course_repo.existsById(add2.getCourseId())).thenReturn(false);
		assertThrows(NotFoundException.class,()->{admission_service.updateAdmission(add2);});
	}
	 
	@Test
	void testCancelAdmission_failure() {
		Mockito.when(admission_repo.existsById(add1.getAdmissionId())).thenReturn(false);
		//Mockito.when(admission_repo.findById(add1.getAdmissionId())).thenReturn(Optional.ofNullable(add1));
		assertThrows(NotFoundException.class,()->{admission_service.cancelAdmission(add1.getAdmissionId());});
	}
	
	
	
	@Test
	void testShowAllAdmissionByCourseId_failure() {
		List<Admission> admissionlist = null;
		Mockito.when(admission_repo.findAllAdmissionByCourseId(add1.getCourseId())).thenReturn(admissionlist);
		assertEquals(admissionlist,admission_service.showAllAdmissionByCourseId(add1.getCourseId()));
		
	}
	
	@Test
	void testShowAllAdmissionByDate_failure() {
		List<Admission> admissionlist = null;
		Mockito.when(admission_repo.findAllAdmissionByAdmissionDate(add1.getAdmissionDate())).thenReturn(admissionlist);
		assertEquals(admissionlist,admission_service.showAllAdmissionbyDate(add1.getAdmissionDate()));
	}

}







