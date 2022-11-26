package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetails;

public interface StudentFacilityDetailsRepository extends JpaRepository<StudentFacilityDetails, Long>{
	
	 @Modifying
	 @Transactional
	 void deleteAllBySchoolId(Integer schoolId);
}
