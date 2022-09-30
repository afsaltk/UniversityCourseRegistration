package com.capgemini.UniversityCourseSelection.entities;

import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Getter;
import lombok.Setter;
//Admission class
@Entity
@Getter
@Setter
@Table(name = "admission_details")
public class Admission {	


	@Id
	@Column(name = "admission_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer admissionId;

	@Column(name = "course_id")
	private int courseId;

    @Column(name = "applicant_id")
    private int applicantId;


	@Column(name = "admission_date")
	@JsonFormat(pattern = "dd-MMM-yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate admissionDate;
	

	private AdmissionStatus status;
	
	public Admission() {
		status=AdmissionStatus.PENDING;
	}

	public Admission(Integer admissionId, int courseId, int applicantId, LocalDate admissionDate) {
		super();
		this.admissionId = admissionId;
		this.courseId = courseId;
		this.applicantId = applicantId;
		this.admissionDate = admissionDate;
	}
	


}
