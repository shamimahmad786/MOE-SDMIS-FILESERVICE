package com.moe.sdmis.fileservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.moe.sdmis.fileservice.controller.FileCtrl;
import com.moe.sdmis.fileservice.modal.UploadExcelStatus;
import com.moe.sdmis.fileservice.repository.UploadExcelStatusRepository;

@SpringBootApplication
@EnableScheduling
public class MoeSdmisFileserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoeSdmisFileserviceApplication.class, args);
	}
	
//	@Bean
//	public MultipartResolver multipartResolver() {
//	    CommonsMultipartResolver multipartResolver
//	      = new CommonsMultipartResolver();
//	    multipartResolver.setMaxUploadSize(5242880);
//	    return multipartResolver;
//	}
	
	
	@Autowired
	UploadExcelStatusRepository uploadExcelStatusRepository;
	
	@Autowired
	FileCtrl fileCtrl;
	
	Map<String,String> schoolMap=new HashMap<String,String>();  
	@Scheduled(fixedRate = 30000)
    public void startService() throws Exception {
        UploadExcelStatus schoolObj=  uploadExcelStatusRepository.getSchoolIdForProcess();
        if(schoolObj !=null) {
        uploadExcelStatusRepository.updateStatusForProcess(Integer.parseInt(String.valueOf(schoolObj.getSchoolId())));
        schoolMap.put("schoolId", String.valueOf(schoolObj.getSchoolId()));
        schoolMap.put("userId", schoolObj.getUploadedBy());
        System.out.println("SchoolId for validation--->"+schoolObj.getSchoolId());
        fileCtrl.docValidate(schoolMap, "10.247.141.227");
        }else {
        	System.out.println("Validation completed");
        }
    }
	
}
