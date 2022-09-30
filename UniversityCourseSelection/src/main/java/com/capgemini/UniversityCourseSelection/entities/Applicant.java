package com.capgemini.UniversityCourseSelection.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Applicant {

	@Override
	public String toString() {
		return "Applicant [applicantId=" + applicantId + ", applicantName=" + applicantName + ", mobileNumber="
				+ mobileNumber + ", applicantDegree=" + applicantDegree + ", applicantGraduationPercentage="
				+ applicantGraduationPercentage + ", status=" + status + ", admission=" + admission + "]";
	}

	public Applicant(Integer applicantId, String applicantName, long mobileNumber, String applicantDegree,
			double applicantGraduationPercentage, String password, Admission admission) {
		super();
		this.applicantId = applicantId;
		this.applicantName = applicantName;
		this.mobileNumber = mobileNumber;
		this.applicantDegree = applicantDegree;
		this.applicantGraduationPercentage = applicantGraduationPercentage;
		this.password = password;
		this.admission = admission;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer applicantId;

	@Column(name = "name")
	private String applicantName;

	@Column(name = "mobile_number")
	private long mobileNumber;

	@Column(name = "Degree")
	private String applicantDegree;

	@Column(name = "grad_percentage")
	private double applicantGraduationPercentage;


		
	private String password; // added field

	private AdmissionStatus status;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "admission_id", referencedColumnName = "admission_id")
	private Admission admission;

	public Applicant() {
		super();
		status = AdmissionStatus.APPLIED;
	}

	@Override
	public int hashCode() {
		return Objects.hash(applicantDegree, applicantGraduationPercentage, applicantId, applicantName, mobileNumber,
				password, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Applicant other = (Applicant) obj;
		if(this.getApplicantId()==other.getApplicantId())
			return true;
		return Objects.equals(applicantDegree, other.applicantDegree)
				&& Double.doubleToLongBits(applicantGraduationPercentage) == Double
						.doubleToLongBits(other.applicantGraduationPercentage)
				&& Objects.equals(applicantId, other.applicantId) && Objects.equals(applicantName, other.applicantName)
				&& mobileNumber == other.mobileNumber && Objects.equals(password, other.password)
				&& status == other.status;
	}
	
	

	// this is a comment to check integration

}
