package com.capgemini.UniversityCourseSelection.entities;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//Admission class
@Entity
@Getter
@Setter
@ToString
@Table(name = "admission_details")
public class Admission {	


	@Id
	@Column(name = "admission_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int admissionId;

	@Column(name = "course_id")
	private int courseId;

    @Column(name = "applicant_id")
    private int applicantId;


	@Column(name = "admission_date")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private LocalDate admissionDate;
	

	private AdmissionStatus status;
	
	public Admission() {
		status=AdmissionStatus.PENDING;
	}

	public Admission(int admissionId, int courseId, int applicantId, LocalDate admissionDate) {
		super();
		this.admissionId = admissionId;
		this.courseId = courseId;
		this.applicantId = applicantId;
		this.admissionDate = admissionDate;
		this.status=AdmissionStatus.PENDING;
	}

	@Override
	public int hashCode() {
		return Objects.hash(admissionId, courseId,  applicantId, admissionDate, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Admission other = (Admission) obj;
		if (admissionDate == null) {
			if (other.admissionDate != null)
				return false;
		} else if (!admissionDate.equals(other.getAdmissionDate()))
			return false;
		if (admissionId != other.getAdmissionId())
			return false;
		if (applicantId != other.getApplicantId())
			return false;
		if (courseId != other.getCourseId())
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
}


