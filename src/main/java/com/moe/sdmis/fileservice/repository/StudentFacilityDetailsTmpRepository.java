package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.StudentFacilityDetailsTmp;
//import com.moe.sdmis.fileservice.modal.StudentVocationalDetailsTmp;

public interface StudentFacilityDetailsTmpRepository extends JpaRepository<StudentFacilityDetailsTmp, Long>{
	@Modifying
	 @Transactional
	 @Query(nativeQuery = true, value = "delete from stu_fac_othr_details_tmp WHERE school_id =:schoolId")
	 void deleteAllBySchoolId(Integer schoolId);
}
