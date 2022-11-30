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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetails;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetails;
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

	public List<Map<String, HashMap<String, String>>> uploadData(List<CommonBean> lt, String userId, String address,
			String schoolId, StaticReportBean sObj, HashMap<String, String> sectionMap, Map<Integer, Boolean> mTongObj,
			HashMap<String, Boolean> lowerSector, HashMap<String, Boolean> lowerSubSector,
			HashMap<String, Boolean> higherSector, HashMap<String, Boolean> higherSubSector) throws Exception {
		statusFlag = "3";
//		System.out.println("Before save list size--->"+lt.size());

//		List<StudentTempTable> response=studentTempTableRepository.saveAll(lt);

		List<Map<String, HashMap<String, String>>> finalList = new ArrayList<Map<String, HashMap<String, String>>>();

//		System.out.println("List Size--->"+response.size());

		for (CommonBean lt1 : lt) {

//			System.out.println(lt1.getMobileNo_1());
			finalList.add(customFxcelValidator.validateStudent(lt1, sObj, sectionMap, mTongObj, lowerSector,
					lowerSubSector, higherSector, higherSubSector));
		}

		try {

//			long 	statusCount=	finalList.stream().filter((e)->
//		
//				e.entrySet().contains("status=0")
//				).count();
//			
			System.out.println(finalList);
			long statusCount = finalList.stream()
					.filter((e) -> e.get("finalStatus").get("status").equalsIgnoreCase("0")).count();

			if (statusCount > 0) {
				statusFlag = "2";
			}

			System.out.println("final list--->" + finalList);

			System.out.println("statusCount---->" + statusCount);

			UploadHistory uObj = new UploadHistory();
			uObj.setHost(address);
			uObj.setSchoolId(Integer.parseInt(schoolId));

			uObj.setUploadedBy(userId);
			uObj.setStatus(statusFlag);
			uObj.setUploadedTime(new Date());
			uploadHistoryRepository.save(uObj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return finalList;
	}

	public void finalUpdateStudentData(String data) throws Exception {
		System.out.println("called final upload");
		ObjectMapper objectMapper = new ObjectMapper();
		Stream<String> sObj = Files
				.lines(Paths.get(userBucketPath + File.separator + data + File.separator + data + "." + "txt"));
		System.out.println(sObj);
		String string1 = null;
		try {
			string1 = sObj.collect(Collectors.joining());
			System.out.println("String----->" + string1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		LinkedList<StudentBasicProfile> ltStudentObj = new LinkedList<StudentBasicProfile>();
		LinkedList<StudentFacilityDetails> ltStudentFacilityObj = new LinkedList<StudentFacilityDetails>();
		try {
			TypeReference<List<Map<String, HashMap<String, String>>>> typeRef = new TypeReference<List<Map<String, HashMap<String, String>>>>() {
			};
			List<Map<String, HashMap<String, String>>> obj = objectMapper.readValue(string1, typeRef);
			System.out.println(obj.get(0).get("classId").get("value"));

			for (Map<String, HashMap<String, String>> studentObj : obj) {
				StudentBasicProfile studentPojo = new StudentBasicProfile();
				try {
//        	System.out.println(obj.get(0).get("ooscYn"));

					studentPojo.setSchoolId(handleInteger(data));
					if(nullCheck(studentObj.get("classId")).get("value").equalsIgnoreCase("PP1")) {
						studentPojo.setClassId(-1);	
					}else if(nullCheck(studentObj.get("classId")).get("value").equalsIgnoreCase("PP1")) {
						studentPojo.setClassId(-2);
					}else if(nullCheck(studentObj.get("classId")).get("value").equalsIgnoreCase("PP1")) {
						studentPojo.setClassId(-3);
					}else {
					studentPojo.setClassId(handleInteger(nullCheck(studentObj.get("classId")).get("value")));
					}
					studentPojo.setSectionId(handleInteger(nullCheck(studentObj.get("sectionId")).get("value")));
					studentPojo.setStudentName(studentObj.get("studentName").get("value"));
					studentPojo.setGender(handleInteger(studentObj.get("gender").get("value")));
					studentPojo.setDob(new Date(studentObj.get("studentDob").get("value").replaceAll("-", "/")));
					studentPojo.setMotherName(studentObj.get("motherName").get("value"));
					studentPojo.setFatherName(studentObj.get("fatherName").get("value"));
					studentPojo.setAadhaarNo(studentObj.get("aadhaarNo").get("value"));
					studentPojo.setNameAsAadhaar(studentObj.get("nameAsAadhaar").get("value"));
					studentPojo.setAddress(studentObj.get("address").get("value"));
					studentPojo.setPincode(handleInteger(nullCheck(studentObj.get("pincode")).get("value")));
					studentPojo.setPrimaryMobile(studentObj.get("mobileNo_1").get("value"));
					studentPojo.setSecondaryMobile(studentObj.get("mobileNo_2").get("value"));
					studentPojo.setEmail(studentObj.get("emailId").get("value"));
					studentPojo.setMotherTongue(handleInteger(nullCheck(studentObj.get("motherTongue")).get("value")));
					studentPojo.setSocCatId(handleInteger(nullCheck(studentObj.get("socCatId")).get("value")));
					studentPojo.setMinorityId(handleInteger(nullCheck(studentObj.get("minorityId")).get("value")));
					studentPojo.setAayBplYN(handleInteger(nullCheck(studentObj.get("isBplYn")).get("value")));
					studentPojo.setEwsYN(handleInteger(nullCheck(studentObj.get("ewsYn")).get("value")));
					studentPojo.setCwsnYN(handleInteger(nullCheck(studentObj.get("cwsnYn")).get("value")));
					studentPojo.setImpairmentType(
							new GeneralUtility().CustomStringMapper(studentObj.get("impairmentType").get("value")));
					studentPojo.setNatIndYN(handleInteger(nullCheck(studentObj.get("natIndYn")).get("value")));
					studentPojo.setOoscYn(handleInteger(nullCheck(studentObj.get("ooscYn")).get("value")));
					studentPojo.setAdmnNumber(studentObj.get("admnNumber").get("value"));
					studentPojo.setAdmnStartDate(
							new Date(studentObj.get("admnStartDate").get("value").replaceAll("-", "/")));

					studentPojo.setAcademicStream(handleInteger(nullCheck(studentObj.get("acdemicStream")).get("value")));

					studentPojo.setEnrStatusPY(handleInteger(nullCheck(studentObj.get("enrStatusPy")).get("value")));
					studentPojo.setClassPY(handleInteger(nullCheck(studentObj.get("classPy")).get("value")));
					studentPojo.setEnrTypeCy(handleInteger(nullCheck(studentObj.get("enrTypeCy")).get("value")));
					studentPojo.setExamAppearedPyYn(handleInteger(nullCheck(studentObj.get("examAppearedPyYn")).get("value")));
					studentPojo.setExamResultPy(handleInteger(nullCheck(studentObj.get("examResultPy")).get("value")));
					studentPojo.setExamMarksPy(handleInteger(nullCheck(studentObj.get("examMarksPy")).get("value")));
					studentPojo.setAttendancePy(handleInteger(nullCheck(studentObj.get("attendencePy")).get("value")));
//        	studentPojo.setAcYearId(Integer.parseInt(studentObj.get("acYearId").get("value")));
					studentPojo.setAcYearId(10);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				StudentFacilityDetails stdfacility = new StudentFacilityDetails();

				try {
//System.out.println(nullCheck(studentObj.get("schlrshpAmount")).get("value"));
					stdfacility.setStudentBasicProfile(studentPojo);
//        		stdfacility.setStudentId(1L);
					stdfacility.setSchoolId(Integer.parseInt(data));
        		    stdfacility.setFacProvided(jsonCreation(nullCheck(studentObj.get("textBoxFacProvided")).get("value"),nullCheck(studentObj.get("uniformFacProvided")).get("value")));
//					System.out.println(studentObj.get("centralScholarshipId"));
					stdfacility.setCentralScholarshipId(
							handleInteger(nullCheck(studentObj.get("centrlSchlrshpId")).get("value")));
					stdfacility.setCentralScholarshipYn(
							handleInteger(nullCheck(studentObj.get("centrlSchlrshpYn")).get("value")));
					stdfacility
							.setStateScholarshipYn(handleInteger(nullCheck(studentObj.get("stateSchlrshpYn")).get("value")));
					stdfacility
							.setOtherScholarshipYn(handleInteger(nullCheck(studentObj.get("otherSchlrshpYn")).get("value")));
					stdfacility
							.setScholarshipAmount(handleInteger(nullCheck(studentObj.get("schlrshpAmount")).get("value")));
        		stdfacility.setFacProvidedCwsn(jsonCwsn(studentObj.get("facProvidedCwsn").get("value")));
					stdfacility.setScreenedForSld(handleInteger(nullCheck(studentObj.get("scrndFrSld")).get("value")));
					stdfacility.setSldType(handleInteger(nullCheck(studentObj.get("sldType")).get("value")));
					stdfacility.setScreenedForAsd(handleInteger(nullCheck(studentObj.get("scrndFrAsd")).get("value")));
					stdfacility.setScreenedForAdhd(handleInteger(nullCheck(studentObj.get("scrndFrAdhd")).get("value")));
					stdfacility.setIsEcActivity(handleInteger(nullCheck(studentObj.get("isEcActivity")).get("value")));
//        		stdfacility.setGiftedChildren(Integer.parseInt(studentObj.get("").get("value")));
					stdfacility.setMentorProvided(handleInteger(nullCheck(studentObj.get("mentorProvided")).get("value")));
					stdfacility.setNurturanceCmpsState(
							handleInteger(nullCheck(studentObj.get("nurturanceCmpsState")).get("value")));
					stdfacility.setNurturanceCmpsNational(
							handleInteger(nullCheck(studentObj.get("nurturanceCmpsNational")).get("value")));
					stdfacility.setOlympdsNlc(handleInteger(nullCheck(studentObj.get("olympdsNlc")).get("value")));
					stdfacility.setNccNssYn(handleInteger(nullCheck(studentObj.get("nccNssYn")).get("value")));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
				StudentVocationalDetails vocObj=new StudentVocationalDetails();
				try {
					vocObj.setStudentBasicProfile(studentPojo);
					vocObj.setSchoolId(Integer.parseInt(data));
					vocObj.setVocExposure(handleInteger(nullCheck(studentObj.get("vocYn")).get("value")));
					vocObj.setSector(handleInteger(nullCheck(studentObj.get("appVocPy")).get("value")));
					vocObj.setJobRole(handleInteger(nullCheck(studentObj.get("sector")).get("value")));
					vocObj.setAppVocPy(handleInteger(nullCheck(studentObj.get("jobRole")).get("value")));
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				

//				System.out.println(stdfacility);
				studentPojo.setStudentFacilityDetails(stdfacility);
				studentPojo.setStudentVocationalDetails(vocObj);
				ltStudentObj.add(studentPojo);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			studentBasicProfileRepository.deleteAllBySchoolId(Integer.parseInt(data));
			studentFacilityDetailsRepository.deleteAllBySchoolId(Integer.parseInt(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<StudentBasicProfile> stdBasicList = studentBasicProfileRepository.saveAll(ltStudentObj);

//		 studentBasicProfileRepository.copyStudentDataFromTemp(Integer.parseInt(data));

	}

	public List<UploadHistory> getUploadedHistor(Integer schoolId) {
//		System.out.println("SchoolId----------->"+schoolId);
		System.out.println(uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId));

		return uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId);
	}

	public Stream<String> getValidatedData(Integer schoolId) throws IOException {
//		ObjectMapper objectMapper = new ObjectMapper();
		Stream<String> sObj = Files
				.lines(Paths.get(userBucketPath + File.separator + schoolId + File.separator + schoolId + "." + "txt"));

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

	public void updateHistory(String requestHost, String schoolId, String userid, String status) {
		try {
			UploadHistory uObj = new UploadHistory();
			uObj.setHost(requestHost);
			uObj.setSchoolId(Integer.parseInt(schoolId));
			uObj.setUploadedBy(userid);
			uObj.setStatus(status);
			uObj.setUploadedTime(new Date());
			uploadHistoryRepository.save(uObj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Integer handleInteger(String data) {
		if (data == null || data == "") {
			return null;
		}
		return Integer.parseInt(data);
	}

	public String handleString(String data) {
		if (data == null || data == "") {
			return null;
		}
		return data;
	}
	
	public HashMap<String,String> nullCheck(HashMap<String,String> data) {
		if(data==null) {
			return new HashMap<String,String>();
		}
		return data;
	}

	
	public HashMap<Integer,Integer> jsonCreation(String freeText,String uniform) throws JsonProcessingException {
		
		HashMap<Integer,Integer> hs=new HashMap<Integer,Integer>();
		hs.put(3, 2);
		hs.put(4, 2);
		hs.put(5, 2);
		hs.put(6, 2);
		hs.put(7, 2);
		if(freeText !=null && uniform !=null && !freeText.equalsIgnoreCase("") && !uniform.equalsIgnoreCase("")) {
			hs.put(1, Integer.parseInt(freeText));
			hs.put(2, Integer.parseInt(uniform));
		}else if(freeText !=null && !freeText.equalsIgnoreCase("")) {
			hs.put(1, Integer.parseInt(freeText));
		}else if(uniform !=null && !uniform.equalsIgnoreCase("")) {
			hs.put(2, Integer.parseInt(uniform));
		}
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(hs);
		
		
		
		return hs;
	}
	
	public HashMap<Integer,Integer> jsonCwsn(String facProvidedCwsn){
		HashMap<Integer,Integer> hs=new HashMap<Integer,Integer>();
		hs.put(1, 2);
		hs.put(2, 2);
		hs.put(3, 2);
		hs.put(4, 2);
		hs.put(5, 2);
		hs.put(6, 2);
		hs.put(7, 2);
		hs.put(8, 2);
		hs.put(9, 2);
		hs.put(10, 2);
		hs.put(11, 2);
		hs.put(12, 2);
		
		if(facProvidedCwsn !=null && facProvidedCwsn !="") {
			hs.put(Integer.parseInt(facProvidedCwsn), 1);
		}
		
		return hs;
	}
}
