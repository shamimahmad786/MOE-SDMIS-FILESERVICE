package com.moe.sdmis.fileservice.errorhandler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.moe.sdmis.fileservice.modal.UploadExcelStatus;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.repository.UploadExcelStatusRepository;
import com.moe.sdmis.fileservice.repository.UploadHistoryRepository;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@Autowired
	UploadHistoryRepository uploadHistoryRepository;
	
	@Autowired
	UploadExcelStatusRepository  uploadExcelStatusRepository;
	
	@ExceptionHandler(GenericExceptionHandler.class)
//	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleNoRecordFoundException(GenericExceptionHandler ex) 
	{
		
		
		ErrorResponse  erroResponse=new  ErrorResponse();
		erroResponse.setErrorMessage(ex.getErrorMessage());
		erroResponse.setStatus(ex.getErrorCode());
	    
		try {
			UploadHistory  uObj=new UploadHistory();
			uObj.setHost(ex.getAddress());
			uObj.setSchoolId(Integer.parseInt(ex.getSchoolId()));
			uObj.setUploadedBy(ex.getUserId());
			uObj.setStatus("0");
			uObj.setUploadedTime(new Date());
					uploadHistoryRepository.save(uObj);
				}catch(Exception e) {
					ex.printStackTrace();
				}
		
		
		try {
			UploadExcelStatus  uObj=new UploadExcelStatus();
			uObj.setHost(ex.getAddress());
			uObj.setSchoolId(Integer.parseInt(ex.getSchoolId()));
			uObj.setUploadedBy(ex.getUserId());
			uObj.setStatus("0");
			uObj.setUploadedDateTime(new Date());
			uploadExcelStatusRepository.save(uObj);
				}catch(Exception e) {
					ex.printStackTrace();
				}
		
		
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
	
		
		
		return erroResponse;
	}
}
