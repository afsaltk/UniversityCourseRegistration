package com.capgemini.UniversityCourseSelection.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.capgemini.UniversityCourseSelection.entities.Course;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.ICourseService;
import com.capgemini.UniversityCourseSelection.services.IUniversityStaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StaffControllerTest {
	
	private MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();
	
	@Mock
	private IUniversityStaffService staffService;
	
	@Mock
	private ICourseService courseService;
	
	@InjectMocks
	private StaffController staffController;
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(staffController).build();
	}
	
	UniversityStaffMember STAFF_1 = new UniversityStaffMember(1,"password1","role1");
	UniversityStaffMember STAFF_2 = new UniversityStaffMember(2,"password2","role2");
	UniversityStaffMember STAFF_3 = new UniversityStaffMember(3,"password3","role3");
	
	Course COURSE_1 = new Course(1,"Java Programming","2 months",LocalDate.of(2022,3,10),LocalDate.of(2022,5,10),"700",96.5);
	
	@Test
	void addStaff_successwithLogin() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(staffService.addStaff(STAFF_1)).thenReturn(STAFF_1);
		
		String body = objectWriter.writeValueAsString(STAFF_1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/staff/add")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.password", is("password1")))
		.andExpect(jsonPath("$.role", is("role1")));
	}
	
	@Test
	void addStaff_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		
		String body = objectWriter.writeValueAsString(STAFF_1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/staff/add")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}

	@Test
	void updateStaff_successWithLogin() throws Exception {
		UniversityStaffMember updatedStaff = new UniversityStaffMember(1,"new_pwd","new_role");
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(staffService.updateStaff(updatedStaff)).thenReturn(updatedStaff);
		
		String updatedBody = objectWriter.writeValueAsString(updatedStaff);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/staff/update")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.password", is("new_pwd")))
		.andExpect(jsonPath("$.role", is("new_role")));
	}
	
	@Test
	void updateStaff_failWhenNotFound() throws Exception {
		UniversityStaffMember updatedStaff = new UniversityStaffMember(5,"new_pwd","new_role");
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(staffService.updateStaff(updatedStaff))
		.thenThrow(new NotFoundException("Staff with id: "+updatedStaff.getStaffId()+" not found!"));
		
		String updatedBody = objectWriter.writeValueAsString(updatedStaff);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/staff/update")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.accept(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCause(new NotFoundException("Staff with id: "+updatedStaff.getStaffId()+" not found!"));
	}
	
	@Test
	void updateStaff_failWhenRecordOrIdIsNull() throws Exception {
		UniversityStaffMember updatedStaff = new UniversityStaffMember(null,"new_pwd","new_role");
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		String updatedBody = objectWriter.writeValueAsString(updatedStaff);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/staff/update")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.accept(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCause(new NotFoundException("Staff record or ID cannot be null!"));
	}

	@Test
	void viewStaff_hidePasswordFromOtherStaffs() throws Exception {
		Mockito.when(staffService.viewStaff(STAFF_2.getStaffId())).thenReturn(STAFF_2);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/staff/view/2")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.password", is("*******")))
		.andExpect(jsonPath("$.role", is("role2")));
	}
	
	@Test
	void viewStaff_ShowPasswordIfSameStaff() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 2);
		
		Mockito.when(staffService.viewStaff(STAFF_2.getStaffId())).thenReturn(STAFF_2);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/staff/view/2")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.password", is("password2")))
		.andExpect(jsonPath("$.role", is("role2")));
	}
	
	@Test
	void viewStaff_failWhenNotFound() throws Exception {
		UniversityStaffMember viewStaff = new UniversityStaffMember(5,"password5","role5");
		Mockito.when(staffService.viewStaff(viewStaff.getStaffId()))
		.thenThrow(new NotFoundException("Staff with id: "+viewStaff.getStaffId()+" not found!"));
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/staff/view/5")
				.contentType(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCause(new NotFoundException("Staff with id: "+viewStaff.getStaffId()+" not found!"));
	}

	@Test
	void viewAllStaffs_success() throws Exception {
		List<UniversityStaffMember> list = new ArrayList<>(Arrays.asList(STAFF_1,STAFF_2,STAFF_3));
		
		Mockito.when(staffService.viewAllStaffs()).thenReturn(list);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/staff/view/all")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$[1].role", is("role2")))
		.andExpect(jsonPath("$[2].password", is("*******")));
	}
	
	@Test
	void viewAllStaffs_noRecordsFound() throws Exception {
		List<UniversityStaffMember> list = new ArrayList<>();
		
		Mockito.when(staffService.viewAllStaffs()).thenReturn(list);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/staff/view/all")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is(list) ));
	}

	@Test
	void removeStaff_success() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/staff/delete/3")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk());
	}
	
	@Test
	void removeStaff_doesNotExist() throws Exception {		
		Mockito.doThrow(new NotFoundException("Staff with id: 3 not found!")).when(staffService).removeStaff(3);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/staff/delete/3")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest)).hasRootCauseInstanceOf(NotFoundException.class);
	}
	
	@Test
	void addCourse_success() throws Exception{
		Mockito.when(courseService.addCourse(COURSE_1)).thenReturn(COURSE_1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		String body = objectWriter.writeValueAsString(COURSE_1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/staff/course/add")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.courseName", is(COURSE_1.getCourseName())))
		.andExpect(jsonPath("$.courseId", is(COURSE_1.getCourseId())));
		
	}
	
	@Test
	void addCourse_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		
		String body = objectWriter.writeValueAsString(COURSE_1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/staff/course/add")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	@Test
	void updateCourse_success() throws Exception {
		Course updatedCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022,3,10),LocalDate.of(2022,5,10),"700",96.5);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(courseService.updateCourse(updatedCourse)).thenReturn(updatedCourse);
		
		String updatedBody = objectWriter.writeValueAsString(updatedCourse);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/staff/course/update")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.courseName", is("Python Programming")));
	}
	
	@Test
	void updateCourse_failWhenNotFound() throws Exception {
		Course updatedCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022,3,10),LocalDate.of(2022,5,10),"700",96.5);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(courseService.updateCourse(updatedCourse)).thenThrow(new NotFoundException());
		
		String updatedBody = objectWriter.writeValueAsString(updatedCourse);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/staff/course/update")
				.contentType(MediaType.APPLICATION_JSON)
				.session(session)
				.content(updatedBody)
				.accept(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()->{mockMvc.perform(mockRequest);}).hasRootCauseInstanceOf(NotFoundException.class);
	}
	
	@Test
	void deleteCourse_success() throws Exception {
		Course deletedCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022,3,10),LocalDate.of(2022,5,10),"700",96.5);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(courseService.removeCourse(deletedCourse.getCourseId())).thenReturn(deletedCourse);
		
		String updatedBody = objectWriter.writeValueAsString(deletedCourse);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/staff/course/delete/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.session(session)
				.accept(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$", is("Course with id: 1 deleted successfully!")));
	}
	
	@Test
	void deleteCourse_failWhenNotFound() throws Exception {
		Course deletedCourse = new Course(1,"Python Programming","2 months",LocalDate.of(2022,3,10),LocalDate.of(2022,5,10),"700",96.5);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("staffMember", 1);
		
		Mockito.when(courseService.removeCourse(deletedCourse.getCourseId())).thenThrow(new NotFoundException());
		
		String updatedBody = objectWriter.writeValueAsString(deletedCourse);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/staff/course/delete/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedBody)
				.session(session)
				.accept(MediaType.APPLICATION_JSON);
		
		assertThatThrownBy(()->{mockMvc.perform(mockRequest);}).hasRootCauseInstanceOf(NotFoundException.class);
	}

}
