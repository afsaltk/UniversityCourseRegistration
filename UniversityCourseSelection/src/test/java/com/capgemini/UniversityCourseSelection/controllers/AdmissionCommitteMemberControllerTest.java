package com.capgemini.UniversityCourseSelection.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.AdmissionCommiteeMember;
import com.capgemini.UniversityCourseSelection.entities.AdmissionStatus;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;
import com.capgemini.UniversityCourseSelection.exception.NotFoundException;
import com.capgemini.UniversityCourseSelection.services.AdmissionCommitteeMemberServiceImpl;
import com.capgemini.UniversityCourseSelection.services.UniversityStaffServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
class AdmissionCommitteMemberControllerTest {

	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@Mock
	private AdmissionCommitteeMemberServiceImpl committeeService;

	@InjectMocks
	private AdmissionCommitteMemberController committeeController;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(committeeController).build();
	}

	AdmissionCommiteeMember member1 = new AdmissionCommiteeMember(1, "member_1", "1111", "mem@1", "pass1");
	AdmissionCommiteeMember member2 = new AdmissionCommiteeMember(2, "member_2", "2222", "mem@2", "pass2");
	AdmissionCommiteeMember member3 = new AdmissionCommiteeMember(3, "member_3", "3333", "mem@3", "pass3");

	@Test
	public void addAddmissionCommitteeMember_success() throws Exception {
		Mockito.when(committeeService.addCommitteeMember(member1)).thenReturn(member1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);

		String body = objectWriter.writeValueAsString(member1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/committee/add").session(session)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(body);

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.adminName", is("member_1"))).andExpect(jsonPath("$.adminContact", is("1111")))
				.andExpect(jsonPath("$.adminUsername", is("mem@1")))
				.andExpect(jsonPath("$.adminPassword", is("pass1")));
	}

	@Test
	void updateCommitteeMember_success() throws Exception {
		AdmissionCommiteeMember updated_member = new AdmissionCommiteeMember(1, "new_member", "8888", "new@1",
				"pass10");

		Mockito.when(committeeService.updateCommitteeMember(updated_member)).thenReturn(updated_member);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);

		String updatedBody = objectWriter.writeValueAsString(updated_member);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/committee/update").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(updatedBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.adminContact", is("8888"))).andExpect(jsonPath("$.adminPassword", is("pass10")));
	}
	
	@Test
	void updateCommitteeMember_failureWhenNotFound() throws Exception {
		AdmissionCommiteeMember updated_member = new AdmissionCommiteeMember(1, "new_member", "8888", "new@1",
				"pass10");

		Mockito.when(committeeService.updateCommitteeMember(updated_member)).thenThrow(new NotFoundException("Committee member with "+updated_member.getAdminId()+ " not found"));
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);

		String updatedBody = objectWriter.writeValueAsString(updated_member);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/uni/committee/update").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(updatedBody).accept(MediaType.APPLICATION_JSON);

		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCause(new NotFoundException("Committee member with "+updated_member.getAdminId()+ " not found"));
	}

	
	
	@Test
	void viewCommitteeMemberById_success() throws Exception {
		AdmissionCommiteeMember updated_member = new AdmissionCommiteeMember(1, "new_member", "8888", "new@1",
				"pass10");

		Mockito.when(committeeService.viewCommitteeMember(updated_member.getAdminId())).thenReturn(updated_member);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/committee/view/1").session(session)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.adminPassword", is("********")))
				.andExpect(jsonPath("$.adminName", is("new_member")));
	}
	
	
	@Test
	void viewCommitteeMemberById_failureWhenNotFound() throws Exception {
		AdmissionCommiteeMember updated_member = new AdmissionCommiteeMember(1, "new_member", "8888", "new@1",
				"pass10");

		Mockito.when(committeeService.viewCommitteeMember(updated_member.getAdminId())).thenThrow(new NotFoundException("Committee member with id "+updated_member.getAdminId()+" not found !"));
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/committee/view/1").session(session)
				.contentType(MediaType.APPLICATION_JSON);

		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCause(new NotFoundException("Committee member with id "+updated_member.getAdminId()+" not found !"));
	}
	
	
	@Test
	void viewAllCommiteeMembers_success() throws Exception {
		List<AdmissionCommiteeMember> list = new ArrayList<>(Arrays.asList(member1, member2, member3));
		
		Mockito.when(committeeService.viewAllCommitteeMembers()).thenReturn(list);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/committee/viewAll").session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$[0].adminName", is("member_1")))
		.andExpect(jsonPath("$[1].adminPassword", is("*******")))
		.andExpect(jsonPath("$[2].adminContact", is("3333")));
	}
	
	
	@Test
	void viewAllCommiteeMembers_NoRecordsFound() throws Exception {
		List<AdmissionCommiteeMember> list = new ArrayList<>(Arrays.asList());
		
		Mockito.when(committeeService.viewAllCommitteeMembers()).thenReturn(list);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/uni/committee/viewAll").session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is(list)));
	}

	
	@Test
	void deleteCommitteeMember_success() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/committee/delete/3").session(session)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk());
	}
	

//	@Test
//	void deleteCommitteeMember_DoesNotExists() throws Exception {
//		
//		MockHttpSession session = new MockHttpSession();
//		session.setAttribute("commitee", 2);
//		
//		Mockito.doThrow(NotFoundException.class).when(committeeService).removeCommitteeMember(30);
//
//		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/uni/committee/delete/30").session(session)
//				.contentType(MediaType.APPLICATION_JSON);
//		
//		assertThatThrownBy(()-> mockMvc.perform(mockRequest)).hasRootCauseInstanceOf(NotFoundException.class);
//
//	}
	
	
	@Test
	void testGetAdmissionResult_Confirmed() throws Exception
	{
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 91, "pass1", admission);
		
		Mockito.when(committeeService.provideAdmissionResult(applicant, admission)).thenReturn(AdmissionStatus.CONFIRMED);

		String updatedBody = objectWriter.writeValueAsString(applicant);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/committee/getResult").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(updatedBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", is("CONFIRMED")));
	}
	
	
	@Test
	void testGetAdmissionResult_Pending() throws Exception
	{
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 91, "pass1", admission);
		
		Mockito.when(committeeService.provideAdmissionResult(applicant, admission)).thenReturn(AdmissionStatus.PENDING);

		String updatedBody = objectWriter.writeValueAsString(applicant);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/committee/getResult").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(updatedBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", is("PENDING")));
	}
	
	
	@Test
	void testGetAdmissionResult_Rejected() throws Exception
	{
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		Admission admission = new Admission(1, 1, 1, LocalDate.of(2022, 3, 10));
		Applicant applicant = new Applicant(1, "Adesh", 12312312L, "B.Tech", 91, "pass1", admission);
		
		Mockito.when(committeeService.provideAdmissionResult(applicant, admission)).thenReturn(AdmissionStatus.REJECTED);

		String updatedBody = objectWriter.writeValueAsString(applicant);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/uni/committee/getResult").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(updatedBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", is("REJECTED")));
	}
}
