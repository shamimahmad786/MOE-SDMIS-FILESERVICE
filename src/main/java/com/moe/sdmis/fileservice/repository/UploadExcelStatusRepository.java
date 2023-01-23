package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.UploadExcelStatus;
import com.moe.sdmis.fileservice.modal.UploadHistory;

public interface UploadExcelStatusRepository extends JpaRepository<UploadExcelStatus, Integer>{
// @Query(nativeQuery = true, value = "delete from stu_voc_details_tmp WHERE school_id =:schoolId")
 @Query(nativeQuery = true, value = "select * from moe_sdmis_uploadexcelstatus where status='2' order by uploaded_date_time ASC limit 1")
 UploadExcelStatus getSchoolIdForProcess();
 
 @Modifying
 @Transactional
 @Query(nativeQuery = true, value = "update moe_sdmis_uploadexcelstatus set status='9' where school_id =:schoolId")
void updateStatusForProcess(Integer schoolId);
 
 @Modifying
 @Transactional
 @Query(nativeQuery = true, value = "update moe_sdmis_uploadexcelstatus set status=:status where school_id =:schoolId and status='9'")
 void updateStatusAfterValidation(String status,Integer schoolId);
 
}
