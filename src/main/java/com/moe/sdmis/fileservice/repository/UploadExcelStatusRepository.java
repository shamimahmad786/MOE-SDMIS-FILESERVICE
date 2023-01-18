package com.moe.sdmis.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moe.sdmis.fileservice.modal.UploadExcelStatus;
import com.moe.sdmis.fileservice.modal.UploadHistory;

public interface UploadExcelStatusRepository extends JpaRepository<UploadExcelStatus, Integer>{

}
