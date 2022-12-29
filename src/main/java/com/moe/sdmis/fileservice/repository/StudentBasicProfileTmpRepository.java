package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.StudentBasicProfileTmp;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetailsTmp;

public interface StudentBasicProfileTmpRepository extends JpaRepository<StudentBasicProfileTmp, Long>{
	 @Modifying
	 @Transactional
	 @Query(nativeQuery = true, value = "delete from stu_pro_enr_details_tmp WHERE school_id =:schoolId")
	 void deleteAllBySchoolId(Integer schoolId);
}
