package com.moe.sdmis.fileservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.UploadHistory;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory, Long>{
List<UploadHistory>  getBySchoolIdOrderByUploadedTimeDesc(Integer schoolId);
}
