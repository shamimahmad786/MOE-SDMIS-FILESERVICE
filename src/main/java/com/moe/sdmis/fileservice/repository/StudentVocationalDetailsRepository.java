package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

//import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetails;

public interface StudentVocationalDetailsRepository extends JpaRepository<StudentVocationalDetails, Long>{
	@Modifying
	 @Transactional
	 void deleteAllBySchoolId(Integer schoolId);
}
