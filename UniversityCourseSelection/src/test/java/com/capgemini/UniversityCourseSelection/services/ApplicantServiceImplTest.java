package com.capgemini.UniversityCourseSelection.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.AdmissionStatus;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;
import com.capgemini.UniversityCourseSelection.repo.ICourseRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceImplTest {

	@Mock
	private IApplicantRepository apprepo;
	
	@Mock
	private ICourseRepository courserepo;

	@InjectMocks
	private ApplicantServiceImpl appservice;

	static Applicant app1 = new Applicant();
	static Applicant app2 = new Applicant();
	static Applicant app3 = new Applicant();

	@BeforeAll
	static void initMethod() {
		app1.setApplicantId(1);
		app1.setAdmission(new Admission());
		app2.setApplicantId(2);
		app2.setAdmission(new Admission());
		app3.setApplicantId(3);
		app3.setAdmission(new Admission());
		app3.setStatus(AdmissionStatus.CONFIRMED);

	}

	@Test
	void testAddApplicant_success() {
		Mockito.when(apprepo.save(app1)).thenReturn(app1);
		Mockito.when(courserepo.existsById(1)).thenReturn(true);
		app1.getAdmission().setCourseId(1);
		
		

		assertEquals(app1, appservice.addApplicant(app1));
	}

	@Test
	void testUpdateApplicant_success() {
		Mockito.when(apprepo.existsById(2)).thenReturn(true);
		Mockito.when(courserepo.existsById(1)).thenReturn(true);
		
		app2.getAdmission().setCourseId(1);
		Mockito.when(apprepo.save(app2)).thenReturn(app2);
		assertEquals(app2, appservice.updateApplicant(app2));
	}

	@Test
	void testDeleteApplicant_success() {
		Mockito.when(apprepo.existsById(1)).thenReturn(true);
		boolean success = true;
		try {
			appservice.deleteApplicant(app1);
		} catch (Exception e) {
			success = false;
		}
		assertEquals(true, success);
	}

	@Test
	void testViewApplicant_success() {
		Mockito.when(apprepo.existsById(1)).thenReturn(true);
		Mockito.when(apprepo.findById(1)).thenReturn(Optional.ofNullable(app1));
		assertTrue(appservice.viewApplicant(1).isPresent());

	}

	@Test
	void testViewAllApplicantsByStatus_success() {
		List<Applicant> applied = new ArrayList<>();
		applied.add(app1);
		applied.add(app2);
		List<Applicant> confirmed = new ArrayList<>();
		confirmed.add(app3);

		Mockito.when(apprepo.viewAllApplicantByCourse(0)).thenReturn(applied);
		Mockito.when(apprepo.viewAllApplicantByCourse(1)).thenReturn(confirmed);

		assertEquals(applied, appservice.viewAllApplicantsByStatus(0));
		assertEquals(confirmed, appservice.viewAllApplicantsByStatus(1));
	}

	@Test
	void testAddApplicant_failure() {
//		Mockito.when(apprepo.save(app3)).thenThrow(new IllegalArgumentException());

		assertThrows(NotFoundException.class, () -> {
			appservice.addApplicant(app3);
		});
	}

	@Test
	void testUpdateApplicant_failure() {
//		Mockito.when(apprepo.save(app3)).thenThrow(new NotFoundException());

		assertThrows(NotFoundException.class, () -> {
			appservice.addApplicant(app3);
		});
	}
	
	@Test
	void testDeleteApplicant_failure() {
		Applicant app4=new Applicant();
		app4.setApplicantId(4);
		Mockito.when(apprepo.existsById(4)).thenReturn(false);
		assertThrows(NotFoundException.class,()->appservice.deleteApplicant(app4));
	}
	
	@Test
	void testViewApplicant_failure() {
//		Applicant app4=null;
//		Mockito.when(apprepo.findById(4)).thenReturn(Optional.ofNullable(app4));
		Mockito.when(apprepo.existsById(4)).thenReturn(false);
		assertThrows(NotFoundException.class,()->appservice.viewApplicant(4));
	}
	
	@Test
	void testViewAllApplicantsByStatus_failure() {
		List<Applicant> list=null;
		Mockito.when(apprepo.viewAllApplicantByCourse(3)).thenReturn(list);
		assertEquals(list, appservice.viewAllApplicantsByStatus(3));
	}

}
