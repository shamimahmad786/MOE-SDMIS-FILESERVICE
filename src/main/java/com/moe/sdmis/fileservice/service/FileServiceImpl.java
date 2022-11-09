package com.moe.sdmis.fileservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.repository.StudentBasicProfileRepository;
import com.moe.sdmis.fileservice.repository.StudentTempTableRepository;
import com.moe.sdmis.fileservice.repository.UploadHistoryRepository;
import com.moe.sdmis.fileservice.utility.GeneralUtility;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

@Service
public class FileServiceImpl {

	@Autowired
	StudentTempTableRepository studentTempTableRepository;
	
	@Autowired
	StudentBasicProfileRepository studentBasicProfileRepository;
	@Autowired
	CustomFxcelValidator customFxcelValidator;
	@Autowired
	UploadHistoryRepository uploadHistoryRepository;

	@Value("${userBucket.path}")
	private String userBucketPath;

	String statusFlag;
	
	public List<Map<String, HashMap<String, String>>> uploadData(List<StudentTempTable> lt,String userId,String address) throws Exception {
		statusFlag="2";
//		System.out.println("Before save list size--->"+lt.size());
		
		List<StudentTempTable> response=studentTempTableRepository.saveAll(lt);
		
		List<Map<String, HashMap<String, String>>> finalList=new ArrayList<Map<String, HashMap<String, String>>>();
		
//		System.out.println("List Size--->"+response.size());
		
		for(StudentTempTable lt1 : response) {
			
//			System.out.println(lt1.getMobileNo_1());
			finalList.add(customFxcelValidator.validateStudent(lt1));	
		}
		
		try {
			
			long 	statusCount=	finalList.stream().filter((e)->
				e.keySet().contains("finalStatus")
				).count();
			
			if(statusCount>0) {
				statusFlag="1";
			}
			
	UploadHistory  uObj=new UploadHistory();
	uObj.setHost(address);
	uObj.setSchoolId(Integer.parseInt(lt.get(0).getUdisecode()));
	uObj.setUploadedBy(userId);
	uObj.setStatus(statusFlag);
	uObj.setUploadedTime(new Date());
			uploadHistoryRepository.save(uObj);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		return finalList;
	}
	
	public void finalUpdateStudentData(String data) throws Exception {
		System.out.println("called final upload");
		ObjectMapper objectMapper = new ObjectMapper();
		Stream<String> sObj= Files.lines(Paths.get(userBucketPath+File.separator+data+File.separator+data+"."+"txt"));
		System.out.println(sObj);
		String string1=null;
		try {
			 string1 = sObj.collect(Collectors.joining());
			System.out.println("String----->"+string1);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		List<StudentBasicProfile> ltStudentObj=new ArrayList<StudentBasicProfile>(); 
		try {
		TypeReference<List<Map<String, HashMap<String, String>>> > typeRef 
        = new TypeReference<List<Map<String, HashMap<String, String>>> >() {};
        List<Map<String, HashMap<String, String>>> obj=	 objectMapper.readValue(string1, typeRef);
        System.out.println(obj.get(0).get("classId").get("value"));
        
        for(Map<String, HashMap<String, String>> studentObj:obj) {
//        	System.out.println(obj.get(0).get("ooscYn"));
        	StudentBasicProfile  studentPojo=new StudentBasicProfile();
        	studentPojo.setSchoolId(Integer.parseInt(data));
        	studentPojo.setClassId(Integer.parseInt(studentObj.get("classId").get("value")));
        	studentPojo.setSectionId(Integer.parseInt(studentObj.get("sectionId").get("value")));
        	studentPojo.setStudentName(studentObj.get("studentName").get("value"));
        	studentPojo.setGender(Integer.parseInt(studentObj.get("gender").get("value")));
        	studentPojo.setDob(new Date(studentObj.get("studentDob").get("value")));
        	studentPojo.setMotherName(studentObj.get("motherName").get("value"));
        	studentPojo.setFatherName(studentObj.get("fatherName").get("value"));
        	studentPojo.setAadhaarNo(studentObj.get("aadhaarNo").get("value"));
        	studentPojo.setNameAsAadhaar(studentObj.get("nameAsAadhaar").get("value"));
        	studentPojo.setAddress(studentObj.get("address").get("value"));
        	studentPojo.setPincode(Integer.parseInt(studentObj.get("pincode").get("value")));
        	studentPojo.setPrimaryMobile(studentObj.get("mobileNo_1").get("value"));
        	studentPojo.setSecondaryMobile(studentObj.get("mobileNo_2").get("value"));
        	studentPojo.setEmail(studentObj.get("emailId").get("value"));
        	studentPojo.setMotherTongue(Integer.parseInt(studentObj.get("motherTongue").get("value")));
        	studentPojo.setSocCatId(Integer.parseInt(studentObj.get("socCatId").get("value")));
        	studentPojo.setMinorityId(Integer.parseInt(studentObj.get("minorityId").get("value")));
        	studentPojo.setAayBplYN(Integer.parseInt(studentObj.get("isBplYn").get("value")));
        	studentPojo.setEwsYN(Integer.parseInt(studentObj.get("ewsYn").get("value")));
        	studentPojo.setCwsnYN(Integer.parseInt(studentObj.get("cwsnYn").get("value")));
        	studentPojo.setImpairmentType(new GeneralUtility().CustomStringMapper(studentObj.get("impairmentType").get("value")));
        	studentPojo.setNatIndYN(Integer.parseInt(studentObj.get("natIndYn").get("value")));
        	studentPojo.setOoscYn(Integer.parseInt(studentObj.get("ooscYn").get("value")));
        	studentPojo.setAdmnNumber(studentObj.get("admnNumber").get("value"));
        	studentPojo.setAdmnStartDate(new Date(studentObj.get("admnStartDate").get("value")));
        	studentPojo.setAcademicStream(Integer.parseInt(studentObj.get("acdemicStream").get("value")));
        	studentPojo.setEnrStatusPY(Integer.parseInt(studentObj.get("enrStatusPy").get("value")));
        	studentPojo.setClassPY(Integer.parseInt(studentObj.get("classPy").get("value")));
        	studentPojo.setEnrTypeCy(Integer.parseInt(studentObj.get("enrTypeCy").get("value")));
        	studentPojo.setExamAppearedPyYn(Integer.parseInt(studentObj.get("examAppearedPyYn").get("value")));
        	studentPojo.setExamResultPy(Integer.parseInt(studentObj.get("examResultPy").get("value")));
        	studentPojo.setExamMarksPy(Integer.parseInt(studentObj.get("examMarksPy").get("value")));
        	studentPojo.setAttendancePy(Integer.parseInt(studentObj.get("attendencePy").get("value")));
        	studentPojo.setAcYearId(Integer.parseInt(studentObj.get("acYearId").get("value")));
        	ltStudentObj.add(studentPojo);
        }
        
        
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		try {
			studentBasicProfileRepository.deleteAllBySchoolId(Integer.parseInt(data));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		studentBasicProfileRepository.saveAll(ltStudentObj);
		
//		 studentBasicProfileRepository.copyStudentDataFromTemp(Integer.parseInt(data));
		
	}
	
	public List<UploadHistory> getUploadedHistor(Integer schoolId){
//		System.out.println("SchoolId----------->"+schoolId);
		System.out.println(uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId));
		
		
		
		
		return uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId);
	}
	
	public Stream<String> getValidatedData(Integer schoolId) throws IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		Stream<String> sObj= Files.lines(Paths.get(userBucketPath+File.separator+schoolId+File.separator+schoolId+"."+"txt"));
		
//		System.out.println(sObj);
//		String string1=null;
//		try {
//			 string1 = sObj.collect(Collectors.joining());
//			System.out.println("String----->"+string1);
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
		
//		try {
//		TypeReference<List<Map<String, HashMap<String, String>>> > typeRef 
//        = new TypeReference<List<Map<String, HashMap<String, String>>> >() {};
//        List<Map<String, HashMap<String, String>>> obj=	 objectMapper.readValue(string1, typeRef);
//        System.out.println(obj.get(0).get("classId").get("value"));
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
		return sObj;

	}
}
