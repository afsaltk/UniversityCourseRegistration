package com.capgemini.UniversityCourseSelection.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.AdmissionCommiteeMember;
import com.capgemini.UniversityCourseSelection.entities.AdmissionStatus;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IAdmissionCommiteeMemberRepository;
import com.capgemini.UniversityCourseSelection.repo.IAdmissionRepository;
import com.capgemini.UniversityCourseSelection.repo.IApplicantRepository;
import com.capgemini.UniversityCourseSelection.repo.ICourseRepository;

@ExtendWith(MockitoExtension.class)
class AdmissionCommitteeMemberServiceImplTest {

	@Mock
	private IAdmissionCommiteeMemberRepository repo;
	
	@Mock
	private IApplicantRepository ApplicantRepo;
	
	@Mock
	private IAdmissionRepository AdmissionRepo;
	
	@Mock
	private ICourseRepository CourseRepo;
	
	@InjectMocks
	private AdmissionCommitteeMemberServiceImpl service;
	
	public AdmissionCommiteeMember mem = new AdmissionCommiteeMember(1,"adesh","1234123412","amedhe","pass");

	
	@Test
	public void testAddCommitteeMember_success()
	{
		Mockito.when(repo.save(mem)).thenReturn(mem);
		assertEquals(mem, service.addCommitteeMember(mem));
	}
	
	@Test
	public void testUpdateCommitteeMember_success()
	{
		Mockito.when(repo.save(mem)).thenReturn(mem);
		Mockito.when(repo.existsById(mem.getAdminId())).thenReturn(true);
		assertEquals(mem, service.updateCommitteeMember(mem));
	}
	
	@Test
	public void testViewCommitteeMember_success()
	{
		Mockito.when(repo.existsById(mem.getAdminId())).thenReturn(true);
		Mockito.when(repo.findById(mem.getAdminId())).thenReturn(Optional.ofNullable(mem));
		assertEquals(mem, service.viewCommitteeMember(1));
	}
	
	@Test
	public void testRemoveCommitteeMember_success()
	{
		Mockito.when(repo.existsById(mem.getAdminId())).thenReturn(true);
		boolean success = true;
		try {
			service.removeCommitteeMember(1);
		}catch(Exception e)
		{
			success = false;
		}
		assertEquals(true,success);
	}
	
	
	@Test
	public void testAddCommitteeMember_failure()
	{
		Mockito.when(repo.save(mem)).thenThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> {
			service.addCommitteeMember(mem);
		});
	}
	
//	@Test
//	public void testUpdateCommitteeMember_failureWhenNull()
//	{
//		Mockito.when(repo.existsById(mem.getAdminId())).thenThrow(new IllegalArgumentException());
//		assertThrows(IllegalArgumentException.class, () -> {
//			service.updateCommitteeMember(mem);
//		});
//	}
	
	@Test
	public void testUpdateCommitteeMember_failureWhenNotFound()
	{
		Mockito.when(repo.existsById(mem.getAdminId())).thenReturn(false);
		assertThrows(NotFoundException.class, () -> {
			service.updateCommitteeMember(mem);
		});
	}
	
	@Test
	public void testViewCommitteeMember_failureWhenNotFound()
	{
		Mockito.when(repo.existsById(1)).thenReturn(false);
		assertThrows(NotFoundException.class, () -> {
			service.viewCommitteeMember(1);
		});
	}

	@Test
	public void testRemoveCommitteeMember_failureWhenNotFound()
	{
		Mockito.when(repo.existsById(1)).thenReturn(false);
		assertThrows(NotFoundException.class, () -> {
			service.removeCommitteeMember(1);
		});
	}
	
	
	@Test
	public void testProvideAdmissionResult_Confirmed()
	{
		Course course = new Course(1,"Java Programming","2 months",LocalDate.of(2022, 6, 25),LocalDate.of(2022, 8, 25),"700",90.5);
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 91, "pass1", admission);
		
		Mockito.when(ApplicantRepo.existsById(applicant.getApplicantId())).thenReturn(true);
		Mockito.when(ApplicantRepo.findById(applicant.getApplicantId())).thenReturn(Optional.of(applicant));

		Mockito.when(AdmissionRepo.existsById(admission.getAdmissionId())).thenReturn(true);
		Mockito.when(AdmissionRepo.findById(admission.getAdmissionId())).thenReturn(Optional.of(admission));

		Mockito.when(CourseRepo.existsById(course.getCourseId())).thenReturn(true);
		Mockito.when(CourseRepo.findById(course.getCourseId())).thenReturn(Optional.of(course));
		
		Mockito.when(ApplicantRepo.save(applicant)).thenReturn(applicant);
				
		assertEquals(AdmissionStatus.CONFIRMED, service.provideAdmissionResult(applicant, admission));
	}
	
	@Test
	public void testProvideAdmissionResult_Pending()
	{
		Course course = new Course(1,"Java Programming","2 months",LocalDate.of(2022, 6, 25),LocalDate.of(2022, 8, 25),"700",90.5);
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 89, "pass1", admission);
		
		Mockito.when(ApplicantRepo.existsById(applicant.getApplicantId())).thenReturn(true);
		Mockito.when(ApplicantRepo.findById(applicant.getApplicantId())).thenReturn(Optional.of(applicant));

		Mockito.when(AdmissionRepo.existsById(admission.getAdmissionId())).thenReturn(true);
		Mockito.when(AdmissionRepo.findById(admission.getAdmissionId())).thenReturn(Optional.of(admission));

		Mockito.when(CourseRepo.existsById(course.getCourseId())).thenReturn(true);
		Mockito.when(CourseRepo.findById(course.getCourseId())).thenReturn(Optional.of(course));
		
		Mockito.when(ApplicantRepo.save(applicant)).thenReturn(applicant);
		
		assertEquals(AdmissionStatus.PENDING, service.provideAdmissionResult(applicant, admission));
	}
	
	@Test
	public void testProvideAdmissionResult_Rejected()
	{
		Course course = new Course(1,"Java Programming","2 months",LocalDate.of(2022, 2, 25),LocalDate.of(2022, 8, 25),"700",90.5);
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 91, "pass1", admission);
		
		Mockito.when(ApplicantRepo.existsById(applicant.getApplicantId())).thenReturn(true);
		Mockito.when(ApplicantRepo.findById(applicant.getApplicantId())).thenReturn(Optional.of(applicant));

		Mockito.when(AdmissionRepo.existsById(admission.getAdmissionId())).thenReturn(true);
		Mockito.when(AdmissionRepo.findById(admission.getAdmissionId())).thenReturn(Optional.of(admission));

		Mockito.when(CourseRepo.existsById(course.getCourseId())).thenReturn(true);
		Mockito.when(CourseRepo.findById(course.getCourseId())).thenReturn(Optional.of(course));
		
		Mockito.when(ApplicantRepo.save(applicant)).thenReturn(applicant);
		
		assertEquals(AdmissionStatus.REJECTED, service.provideAdmissionResult(applicant, admission));
	}
	
}
