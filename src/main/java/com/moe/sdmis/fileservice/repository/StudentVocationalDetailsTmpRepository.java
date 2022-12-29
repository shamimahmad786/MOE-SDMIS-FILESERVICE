package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.StudentVocationalDetails;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetailsTmp;

public interface StudentVocationalDetailsTmpRepository extends JpaRepository<StudentVocationalDetailsTmp, Long>{
	@Modifying
	 @Transactional
	 @Query(nativeQuery = true, value = "delete from stu_voc_details_tmp WHERE school_id =:schoolId")
	 void deleteAllBySchoolId(Integer schoolId);
}
