package com.capgemini.UniversityCourseSelection.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.repo.IUniversityStaffMemberRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UniversityStaffServiceImplTest {
	
	@Mock
	private IUniversityStaffMemberRepository staffRepo;
	@Mock
	private ICourseService courseService;
	@InjectMocks
	private UniversityStaffServiceImpl staffService;
	
	
	UniversityStaffMember STAFF_1 = new UniversityStaffMember(1,"staff1","password1","role1");
	UniversityStaffMember STAFF_2 = new UniversityStaffMember(2,"staff2","password2","role2");
	UniversityStaffMember STAFF_3 = new UniversityStaffMember(3,"staff3","password3","role3");
	
	Course COURSE_1 = new Course(1,"Java Programming","2 months",LocalDate.of(2022, 6, 25),LocalDate.of(2022, 8, 25),"700",96.5);
	
	@Test
	void addStaff_success() {
		UniversityStaffMember newStaff = new UniversityStaffMember(4,"staff4","password4","role4");
		Mockito.when(staffRepo.save(newStaff)).thenReturn(newStaff);
		assertEquals(4,staffService.addStaff(newStaff).getStaffId());
		assertEquals("password4",staffService.addStaff(newStaff).getPassword());
		assertEquals("role4",staffService.addStaff(newStaff).getRole());
		assertEquals(newStaff, staffService.addStaff(newStaff));
	}

	@Test
	void updateStaff_success() {
		UniversityStaffMember updateStaff = new UniversityStaffMember(1,"new_staff","new_pwd","new_role");
		Mockito.when(staffRepo.existsById(updateStaff.getStaffId())).thenReturn(true);
		Mockito.when(staffRepo.save(updateStaff)).thenReturn(updateStaff);
		assertEquals("new_pwd",staffService.updateStaff(updateStaff).getPassword());
	}

	@Test
	void viewStaff_success() {
	Mockito.when(staffRepo.existsById(STAFF_1.getStaffId())).thenReturn(true);	
	Mockito.when(staffRepo.getReferenceById(STAFF_1.getStaffId())).thenReturn(STAFF_1);
		assertNotNull(staffService.viewStaff(1));
		assertEquals(STAFF_1, staffService.viewStaff(1));
	}

	@Test
	void removeStaff_success() {
		Mockito.when(staffRepo.existsById(STAFF_1.getStaffId())).thenReturn(false);
		boolean success = true;
		try {
			staffService.removeStaff(1);
		} catch (Exception e) {
			success = false;
		}
		assertEquals(false, success);
	}

	@Test
	void viewAllStaffs_success() {
		List<UniversityStaffMember> allStaff = new ArrayList<>(Arrays.asList(STAFF_1,STAFF_2,STAFF_3));
		Mockito.when(staffRepo.findAll()).thenReturn(allStaff);
		assertEquals(3, staffService.viewAllStaffs().size());
		assertEquals("password2", staffService.viewAllStaffs().get(1).getPassword());
		assertEquals("role3", staffService.viewAllStaffs().get(2).getRole());
	}
	
	@Test
	void addCourse_success() {
		
		Mockito.when(courseService.addCourse(COURSE_1)).thenReturn(COURSE_1);
		assertEquals(COURSE_1,staffService.addCourse(COURSE_1));
	}
	
	@Test
	void updateCourse_success() {
		Course updateCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022, 6, 25),LocalDate.of(2022, 8, 25),"700",96.5);
		Mockito.when(courseService.updateCourse(updateCourse)).thenReturn(updateCourse);
		assertEquals(updateCourse,staffService.updateCourse(updateCourse));
	}
	
	@Test
	void updateCourse_failWhenNotFound() {
		Course updateCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022, 6, 25),LocalDate.of(2022, 8, 25),"700",96.5);
		Mockito.when(courseService.updateCourse(updateCourse)).thenThrow(new NotFoundException());
		assertThrows(NotFoundException.class,()->{courseService.updateCourse(updateCourse);});
	}
	
	@Test
	void removeCourse_success() {
		Mockito.when(courseService.removeCourse(COURSE_1.getCourseId())).thenReturn(COURSE_1);
		assertEquals(COURSE_1,staffService.removeCourse(COURSE_1));
	}
	
	@Test
	void removeCourse_failWhenNotFound() {
		Mockito.when(courseService.removeCourse(COURSE_1.getCourseId())).thenThrow(new NotFoundException());
		assertThrows(NotFoundException.class,()->{courseService.removeCourse(COURSE_1.getCourseId());});
	}
		
	@Test
	void updateStaff_failWhenNotFound() {
		UniversityStaffMember newStaff = STAFF_3;
		Mockito.when(staffRepo.existsById(newStaff.getStaffId())).thenReturn(false);
		assertThrows(NotFoundException.class, ()->{staffService.updateStaff(newStaff);});
	}
	
	@Test
	void viewStaff_failWhenNotFound() {
		Mockito.when(staffRepo.existsById(3)).thenReturn(false);
		assertThrows(NotFoundException.class, ()->{staffService.viewStaff(3);});
	}
	
	@Test
	void removeStaff_failWhenNotFound() {
		Mockito.when(staffRepo.existsById(3)).thenReturn(false);
		assertThrows(NotFoundException.class, ()->{staffService.removeStaff(3);});
	}
	
	@Test
	void viewAllStaffs_failWhenNoRecords() {
		List<UniversityStaffMember> res = new ArrayList<>();
		Mockito.when(staffRepo.findAll()).thenReturn(res);
		assertThrows(NotFoundException.class, ()-> staffService.viewAllStaffs());
	}
	
}
