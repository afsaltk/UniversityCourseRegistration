package com.capgemini.UniversityCourseSelection.repo;

import com.capgemini.UniversityCourseSelection.entities.AdmissionCommiteeMember;
import com.capgemini.UniversityCourseSelection.entities.Applicant;
import com.capgemini.UniversityCourseSelection.entities.UniversityStaffMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class LoginRepositoryImpl implements ILoginRepository {
	
	
	@Autowired
	private IApplicantRepository appRepo;
	
	@Autowired
	private IAdmissionCommiteeMemberRepository addRepo;
	
	@Autowired
	private IUniversityStaffMemberRepository uniRepo;
	
	private void insertDefaultRecord(String type) {
		if (type.equalsIgnoreCase("staff")) {
			UniversityStaffMember usm = new UniversityStaffMember(1, "password", "role");
			uniRepo.resetStaffIdSequence();
			uniRepo.save(usm);
		}
		if(type.equalsIgnoreCase("commitee")) {
			AdmissionCommiteeMember acm = new AdmissionCommiteeMember(1,"adminName","989789687","username","password");					
			addRepo.resetCommitteeIdSequence();
			addRepo.save(acm);
		}
	}

	@Override
	public boolean verifyApplicantCredentials(int id, String password) {
		Applicant app = appRepo.verifyApplicantCredentials(id, password);
		if(app!=null)
			return true;
		else return false;
	}

	@Override
	public boolean verifyAdmissionCommiteeMemberCredentials(int id, String password) {
		if(addRepo.count()==0) {
			insertDefaultRecord("commitee");
		}
		
		AdmissionCommiteeMember acm= addRepo.verifyAdmissionCommiteeMemberCred(id, password);
		if(acm!=null)
			return true;
		return false;
	}

	@Override
	public boolean verifyUniversityStaffMemberCredentials(int id, String password) {
		if(uniRepo.count()==0) {
			insertDefaultRecord("staff");
		}
		
		UniversityStaffMember ucm=uniRepo.verifyUniversityStaffMemberCredentials(id, password);
		if(ucm!=null) {
			return true;
		}
		return false;
	}

}
