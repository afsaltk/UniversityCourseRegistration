package com.capgemini.UniversityCourseSelection.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Entity
@ToString
@Data
@Table(name = "admission_committee_member")
public class AdmissionCommiteeMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private int adminId;
	
	@Column(name = "admin_name")
	private String adminName;
	
	@Column(name = "admin_contact")
	private String adminContact;
	
	@Column(name = "admin_username")
	private String adminUsername;
	
	@Column(name = "admin_password")
	private String adminPassword;

	public AdmissionCommiteeMember() {
		super();
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminContact() {
		return adminContact;
	}

	public void setAdminContact(String adminContact) {
		this.adminContact = adminContact;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public AdmissionCommiteeMember(int adminId, String adminName, String adminContact, String adminUsername,
			String adminPassword) {
		super();
		this.adminId = adminId;
		this.adminName = adminName;
		this.adminContact = adminContact;
		this.adminUsername = adminUsername;
		this.adminPassword = adminPassword;
	}

}
