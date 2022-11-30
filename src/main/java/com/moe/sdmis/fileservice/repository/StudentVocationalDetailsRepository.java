package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetails;

public interface StudentVocationalDetailsRepository extends JpaRepository<StudentVocationalDetails, Long>{

}
