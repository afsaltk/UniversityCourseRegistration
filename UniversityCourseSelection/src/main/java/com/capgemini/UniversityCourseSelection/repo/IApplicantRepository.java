package com.capgemini.UniversityCourseSelection.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.capgemini.UniversityCourseSelection.entities.Applicant;

@Repository
public interface IApplicantRepository extends JpaRepository<Applicant, Integer> {

	@Query(value = "select * from Applicant where status=?1", nativeQuery = true)
	List<Applicant> viewAllApplicantByCourse(int status);

	@Query(value = "select * from Applicant where id=?1 and password=?2", nativeQuery = true)
	Applicant verifyApplicantCredentials(int id, String password);

}
