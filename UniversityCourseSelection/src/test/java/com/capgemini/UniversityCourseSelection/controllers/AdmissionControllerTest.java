package com.capgemini.UniversityCourseSelection.controllers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.exception.NotLoggedInException;
import com.capgemini.UniversityCourseSelection.services.AdmissionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
class AdmissionControllerTest {
	
    private MockMvc mockMvc;
	
	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();
	
	@Mock
	private AdmissionServiceImpl admission_service;
	
	@InjectMocks
	private AdmissionController admission_ctrl;
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(admission_ctrl).build();
	}
	
	
	DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy")
            .toFormatter();
	Admission add1 = new Admission(1,2,3,LocalDate.parse("10-Sep-2020",dTF));
	Admission add2 = new Admission(2,5,6,LocalDate.parse("10-Sep-2020",dTF));
	Admission add3 = new Admission(3,5,9,LocalDate.parse("12-Sep-2020",dTF));
	
	

	
	@Test
	void addAdmission_success() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("applicant", 3);
		
		Mockito.when(admission_service.addAdmission(add1)).thenReturn(add1);
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/admission/addAdmission").session(session)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(body);
		mockMvc.perform(mockRequest).andExpect(status().isOk());	
	}
	
	

	
	@Test
	void updateAdmission_success() throws Exception {
		Mockito.when(admission_service.updateAdmission(add2)).thenReturn(add2);
		String body = objectWriter.writeValueAsString(add2);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 6);
				
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/admission/updateAdmission").session(session)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(body);
		
		mockMvc.perform(mockRequest).andExpect(status().isOk());		
	}
	

	
	@Test
	void cancelAdmission_success() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		
		Mockito.when(admission_service.cancelAdmission(add1.getAdmissionId())).thenReturn(add1);
				
		String getBody = objectWriter.writeValueAsString(add1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/admission/cancelAdmission/1").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(getBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk());		
	}
	
	

	
	@Test
	void showAllAdmissionByCourseId_success() throws Exception{
		MockHttpSession session = new MockHttpSession();

		session.setAttribute("commitee", 2);
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add2);
		admissionlist.add(add3);
		Mockito.when(admission_service.showAllAdmissionByCourseId(add2.getCourseId())).thenReturn(admissionlist);

		String getBody = objectWriter.writeValueAsString(admissionlist);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyId/5").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(getBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk());
	}
	
	

	
	@Test
	void showAllAdmissionByDate_success() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("commitee", 2);
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add1);
		admissionlist.add(add2);
		Mockito.when(admission_service.showAllAdmissionbyDate(add2.getAdmissionDate())).thenReturn(admissionlist);
		
		String getBody = objectWriter.writeValueAsString(admissionlist);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyDate/10/Sep/2020").session(session)
				.contentType(MediaType.APPLICATION_JSON).content(getBody).accept(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk());	
		
	}
	
	@Test
	void addAdmission_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/admission/addAdmission")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	@Test
	void updateAdmission_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/admission/updateAdmission")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	@Test
	void cancelAdmission_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/admission/cancelAdmission/1")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	@Test
	void showAllAdmissionById_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add2);
		admissionlist.add(add3);
		
		String body = objectWriter.writeValueAsString(admissionlist);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyId/5")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	@Test
	void showAllAdmissionByDate_notLoggedIn() throws Exception {
		MockHttpSession session = new MockHttpSession();
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add1);
		admissionlist.add(add2);
		
		String body = objectWriter.writeValueAsString(admissionlist);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyDate/10/Sep/2020")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		assertThatThrownBy(()-> mockMvc.perform(mockRequest))
		.hasRootCauseInstanceOf(NotLoggedInException.class);
	
	}
	
	
	
	
		
	
	
}
