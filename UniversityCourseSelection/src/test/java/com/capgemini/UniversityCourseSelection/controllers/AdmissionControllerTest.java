package com.capgemini.UniversityCourseSelection.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capgemini.UniversityCourseSelection.entities.Admission;
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
		Mockito.when(admission_service.addAdmission(add1)).thenReturn(add1);
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/admission/addAdmission")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.courseId", is(2)))
		.andExpect(jsonPath("$.applicantId", is(3)))
		.andExpect(jsonPath("$.admissionDate", is("10-Sep-2020")));	
	}
	
	
	@Test
	void updateAdmission_success() throws Exception {
		Mockito.when(admission_service.updateAdmission(add2)).thenReturn(add2);
		String body = objectWriter.writeValueAsString(add2);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/admission/updateAdmission")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.courseId", is(5)))
		.andExpect(jsonPath("$.applicantId", is(6)))
		.andExpect(jsonPath("$.admissionDate", is("10-Sep-2020")));		
	}
	
	@Test
	void cancelAdmission_success() throws Exception {
		Mockito.when(admission_service.cancelAdmission(add1.getAdmissionId())).thenReturn(add1);
		String body = objectWriter.writeValueAsString(add1);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/admission/cancelAdmission/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(body);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$.courseId", is(2)))
		.andExpect(jsonPath("$.applicantId", is(3)))
		.andExpect(jsonPath("$.admissionDate", is("10-Sep-2020")));		
	}
	
	
	@Test
	void showAllAdmissionByCourseId_success() throws Exception {
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add2);
		admissionlist.add(add3);
		Mockito.when(admission_service.showAllAdmissionByCourseId(add2.getCourseId())).thenReturn(admissionlist);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyId/5")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$[0].courseId", is(5)))
		.andExpect(jsonPath("$[1].applicantId", is(9)))
		.andExpect(jsonPath("$[1].admissionDate", is("12-Sep-2020")));	
	}
	
	
	@Test
	void showAllAdmissionByDate_success() throws Exception {
		List<Admission> admissionlist = new ArrayList<>();
		admissionlist.add(add1);
		admissionlist.add(add2);
		Mockito.when(admission_service.showAllAdmissionbyDate(add2.getAdmissionDate())).thenReturn(admissionlist);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/admission/alladmissionbyDate/10/Sep/2020")
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", notNullValue()))
		.andExpect(jsonPath("$[0].courseId", is(2)))
		.andExpect(jsonPath("$[1].applicantId", is(6)))
		.andExpect(jsonPath("$[1].admissionDate", is("10-Sep-2020")));	
		
	}
		
	
	
}
