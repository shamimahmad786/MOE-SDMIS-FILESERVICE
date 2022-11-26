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
import java.util.LinkedList;
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
import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetails;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.repository.StudentBasicProfileRepository;
import com.moe.sdmis.fileservice.repository.StudentFacilityDetailsRepository;
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
	@Autowired
	StudentFacilityDetailsRepository studentFacilityDetailsRepository;

	@Value("${userBucket.path}")
	private String userBucketPath;

	String statusFlag;
	
	public List<Map<String, HashMap<String, String>>> uploadData(List<CommonBean> lt,String userId,String address,String schoolId,StaticReportBean sObj) throws Exception {
		statusFlag="3";
//		System.out.println("Before save list size--->"+lt.size());
		
//		List<StudentTempTable> response=studentTempTableRepository.saveAll(lt);
		
		List<Map<String, HashMap<String, String>>> finalList=new ArrayList<Map<String, HashMap<String, String>>>();
		
//		System.out.println("List Size--->"+response.size());
		
		for(CommonBean lt1 : lt) {
			
//			System.out.println(lt1.getMobileNo_1());
			finalList.add(customFxcelValidator.validateStudent(lt1,sObj));	
		}
		
		try {
			
//			long 	statusCount=	finalList.stream().filter((e)->
//		
//				e.entrySet().contains("status=0")
//				).count();
//			
			System.out.println(finalList);
			long 	statusCount=	finalList.stream().filter((e)->
			e.get("finalStatus").get("status").equalsIgnoreCase("0")
		).count();
			
			if(statusCount>0) {
				statusFlag="2";
			}
			
			System.out.println("final list--->"+finalList);
			
			System.out.println("statusCount---->"+statusCount);
			
			
	UploadHistory  uObj=new UploadHistory();
	uObj.setHost(address);
	uObj.setSchoolId(Integer.parseInt(schoolId));
	
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
		
		LinkedList<StudentBasicProfile> ltStudentObj=new LinkedList<StudentBasicProfile>();
		LinkedList<StudentFacilityDetails> ltStudentFacilityObj=new LinkedList<StudentFacilityDetails>();
		try {
		TypeReference<List<Map<String, HashMap<String, String>>> > typeRef 
        = new TypeReference<List<Map<String, HashMap<String, String>>> >() {};
        List<Map<String, HashMap<String, String>>> obj=	 objectMapper.readValue(string1, typeRef);
        System.out.println(obj.get(0).get("classId").get("value"));
        
        for(Map<String, HashMap<String, String>> studentObj:obj) {
        	
        	try {
//        	System.out.println(obj.get(0).get("ooscYn"));
        	StudentBasicProfile  studentPojo=new StudentBasicProfile();
        	studentPojo.setSchoolId(Integer.parseInt(data));
        	studentPojo.setClassId(Integer.parseInt(studentObj.get("classId").get("value")));
        	studentPojo.setSectionId(Integer.parseInt(studentObj.get("sectionId").get("value")));
        	studentPojo.setStudentName(studentObj.get("studentName").get("value"));
        	studentPojo.setGender(Integer.parseInt(studentObj.get("gender").get("value")));
        	studentPojo.setDob(new Date(studentObj.get("studentDob").get("value").replaceAll("-", "/")));
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
        	studentPojo.setAdmnStartDate(new Date(studentObj.get("admnStartDate").get("value").replaceAll("-", "/")));
        	studentPojo.setAcademicStream(Integer.parseInt(studentObj.get("acdemicStream").get("value")));
        	studentPojo.setEnrStatusPY(Integer.parseInt(studentObj.get("enrStatusPy").get("value")));
        	studentPojo.setClassPY(Integer.parseInt(studentObj.get("classPy").get("value")));
        	studentPojo.setEnrTypeCy(Integer.parseInt(studentObj.get("enrTypeCy").get("value")));
        	studentPojo.setExamAppearedPyYn(Integer.parseInt(studentObj.get("examAppearedPyYn").get("value")));
        	studentPojo.setExamResultPy(Integer.parseInt(studentObj.get("examResultPy").get("value")));
        	studentPojo.setExamMarksPy(Integer.parseInt(studentObj.get("examMarksPy").get("value")));
        	studentPojo.setAttendancePy(Integer.parseInt(studentObj.get("attendencePy").get("value")));
//        	studentPojo.setAcYearId(Integer.parseInt(studentObj.get("acYearId").get("value")));
        	studentPojo.setAcYearId(10);
        	
        	
        	
        	
        	ltStudentObj.add(studentPojo);
        	}catch(Exception ex) {
        		ex.printStackTrace();
        	}
        	
        	
        	try {
        		StudentFacilityDetails stdfacility=new StudentFacilityDetails();
        		
        		stdfacility.setStudentId(1L);
        		stdfacility.setSchoolId(Integer.parseInt(data));
        		
//        		stdfacility.setFacProvided(Integer.parseInt(studentObj.get("").get("value")));
        		stdfacility.setCentralScholarshipId(Integer.parseInt(studentObj.get("centralScholarshipId").get("value")));
        		stdfacility.setCentralScholarshipYn(Integer.parseInt(studentObj.get("centralScholarshipYn").get("value")));
        		stdfacility.setStateScholarshipYn(Integer.parseInt(studentObj.get("stateScholarshipYn").get("value")));
        		stdfacility.setOtherScholarshipYn(Integer.parseInt(studentObj.get("otherScholarshipYn").get("value")));
        		stdfacility.setScholarshipAmount(Integer.parseInt(studentObj.get("scholarshipAmount").get("value")));
//        		stdfacility.setFacProvidedCwsn(Integer.parseInt(studentObj.get("").get("value")));
        		stdfacility.setScreenedForSld(Integer.parseInt(studentObj.get("screenedForSld").get("value")));
        		stdfacility.setSldType(Integer.parseInt(studentObj.get("sldType").get("value")));
        		stdfacility.setScreenedForAsd(Integer.parseInt(studentObj.get("screenedForAsd").get("value")));
        		stdfacility.setScreenedForAdhd(Integer.parseInt(studentObj.get("screenedForAdhd").get("value")));
        		stdfacility.setIsEcActivity(Integer.parseInt(studentObj.get("isEcActivity").get("value")));
//        		stdfacility.setGiftedChildren(Integer.parseInt(studentObj.get("").get("value")));
        		stdfacility.setMentorProvided(Integer.parseInt(studentObj.get("mentorProvided").get("value")));
        		stdfacility.setNurturanceCmpsState(Integer.parseInt(studentObj.get("nurturanceCmpsState").get("value")));
        		stdfacility.setNurturanceCmpsNational(Integer.parseInt(studentObj.get("nurturanceCmpsNational").get("value")));
        		stdfacility.setOlympdsNlc(Integer.parseInt(studentObj.get("olympdsNlc").get("value")));
        		stdfacility.setNccNssYn(Integer.parseInt(studentObj.get("nccNssYn").get("value")));
        		
        		
        		
        	}catch(Exception ex) {
        		ex.printStackTrace();
        	}
        }
        
        
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		try {
			studentBasicProfileRepository.deleteAllBySchoolId(Integer.parseInt(data));
			studentFacilityDetailsRepository.deleteAllBySchoolId(Integer.parseInt(data));
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
//		ObjectMapper objectMapper = new ObjectMapper();
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
	
	
  public void	updateHistory(String requestHost, String schoolId, String userid,String status) {
	  try {
			UploadHistory  uObj=new UploadHistory();
			uObj.setHost(requestHost);
			uObj.setSchoolId(Integer.parseInt(schoolId));
			uObj.setUploadedBy(userid);
			uObj.setStatus(status);
			uObj.setUploadedTime(new Date());
					uploadHistoryRepository.save(uObj);
	  }catch(Exception ex) {
		  ex.printStackTrace();
	  }
  }
	
	
	
}
