package com.moe.sdmis.fileservice.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
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
import com.moe.sdmis.fileservice.db.NativeRepository;
import com.moe.sdmis.fileservice.db.QueryResult;
import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.errorhandler.GenericExceptionHandler;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
import com.moe.sdmis.fileservice.modal.StudentBasicProfileTmp;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetails;
import com.moe.sdmis.fileservice.modal.StudentFacilityDetailsTmp;
//import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetails;
import com.moe.sdmis.fileservice.modal.StudentVocationalDetailsTmp;
import com.moe.sdmis.fileservice.modal.UploadExcelStatus;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.repository.StudentBasicProfileRepository;
import com.moe.sdmis.fileservice.repository.StudentBasicProfileTmpRepository;
import com.moe.sdmis.fileservice.repository.StudentFacilityDetailsRepository;
import com.moe.sdmis.fileservice.repository.StudentFacilityDetailsTmpRepository;
//import com.moe.sdmis.fileservice.repository.StudentTempTableRepository;
import com.moe.sdmis.fileservice.repository.StudentVocationalDetailsRepository;
import com.moe.sdmis.fileservice.repository.StudentVocationalDetailsTmpRepository;
import com.moe.sdmis.fileservice.repository.UploadExcelStatusRepository;
import com.moe.sdmis.fileservice.repository.UploadHistoryRepository;
import com.moe.sdmis.fileservice.utility.GeneralUtility;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

@Service
public class FileServiceImpl {

//	@Autowired
//	StudentTempTableRepository studentTempTableRepository;

	@Autowired
	StudentBasicProfileRepository studentBasicProfileRepository;
	@Autowired
	CustomFxcelValidator customFxcelValidator;
	@Autowired
	UploadHistoryRepository uploadHistoryRepository;
	@Autowired
	StudentFacilityDetailsRepository studentFacilityDetailsRepository;

	@Autowired
	StudentVocationalDetailsRepository studentVocationalDetailsRepository;

	@Autowired
	StudentBasicProfileTmpRepository studentBasicProfileTmpRepository;
	@Autowired
	StudentFacilityDetailsTmpRepository studentFacilityDetailsTmpRepository;
	@Autowired
	StudentVocationalDetailsTmpRepository studentVocationalDetailsTmpRepository;

	@Value("${userBucket.path}")
	private String userBucketPath;

	@Autowired
	NativeRepository nativeRepository;

	@Autowired
	UploadExcelStatusRepository uploadExcelStatusRepository;

	String statusFlag;

	public List<Map<String, HashMap<String, String>>> uploadData(List<CommonBean> lt, String userId, String address,
			String schoolId, StaticReportBean sObj, HashMap<String, List<String>> sectionMap,
			Map<Integer, Boolean> mTongObj, HashMap<String, Boolean> lowerSector,
			HashMap<String, Boolean> lowerSubSector, HashMap<String, Boolean> higherSector,
			HashMap<String, Boolean> higherSubSector, HashSet<String> adharMach) throws Exception {
		statusFlag = "4";
//		System.out.println("Before save list size--->"+lt.size());

//		List<StudentTempTable> response=studentTempTableRepository.saveAll(lt);

		List<Map<String, HashMap<String, String>>> finalList = new ArrayList<Map<String, HashMap<String, String>>>();

//		System.out.println("List Size--->"+response.size());

		for (CommonBean lt1 : lt) {

//			System.out.println(lt1.getMobileNo_1());
			finalList.add(customFxcelValidator.validateStudent(lt1, sObj, sectionMap, mTongObj, lowerSector,
					lowerSubSector, higherSector, higherSubSector, adharMach));
		}

		try {

//			long 	statusCount=	finalList.stream().filter((e)->
//		
//				e.entrySet().contains("status=0")
//				).count();
//			
//			System.out.println(finalList);
			long statusCount = finalList.stream().filter((e) -> e.get("fs").get("s").equalsIgnoreCase("0")).count();

//			System.out.println("Status count--->"+statusCount);
			
			if (statusCount > 0) {
				statusFlag = "3";
			}

//			System.out.println("final list--->" + finalList);
//
//			System.out.println("statusCount---->" + statusCount);

			UploadHistory uObj = new UploadHistory();
			uObj.setHost(address);
			uObj.setSchoolId(Integer.parseInt(schoolId));
			uObj.setUploadedBy(userId);
			uObj.setStatus(statusFlag);
			uObj.setUploadedTime(new Date());
			uploadHistoryRepository.save(uObj);

//			UploadExcelStatus statusObj = new UploadExcelStatus();
//			statusObj.setHost(address);
//			statusObj.setSchoolId(Integer.parseInt(schoolId));
//			statusObj.setUploadedBy(userId);
//			statusObj.setStatus(statusFlag);
//			statusObj.setUploadedDateTime(new Date());
			
			System.out.println("Before update status--->"+statusFlag);
			
			uploadExcelStatusRepository.updateStatusAfterValidation(statusFlag, Integer.parseInt(schoolId));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return finalList;
	}

	StaticReportBean schoolObj = null;

//	@Transactional
	public Map<String, String> finalUpdateStudentData(String data,String userid) throws Exception {
//		System.out.println("called final upload");
		String status;
		ObjectMapper objectMapper = new ObjectMapper();
		Stream<String> sObj = Files
				.lines(Paths.get(userBucketPath + File.separator + data + File.separator + data + "." + "txt"));

		String string1 = null;
		DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

		Map<String, String> resObj = new HashMap<String, String>();

//		System.out.println("sObj--->"+sObj.);

//		try {
//			FileInputStream fis = new FileInputStream(userBucketPath + File.separator + data + File.separator + data + "." + "txt");
//			byte[] buffer = new byte[10];
//			StringBuilder sb = new StringBuilder();
//			while (fis.read(buffer) != -1) {
//				sb.append(new String(buffer));
//				buffer = new byte[10];
//			}
//			fis.close();
//			 string1 = sb.toString();

//			FileInputStream fis = new FileInputStream(userBucketPath + File.separator + data + File.separator + data + "." + "txt");
//			string1 = IOUtils.toString(fis, "UTF-8");
//			System.out.println("string1--->"+string1);

//			 FileReader fileReader
//	            = new FileReader(
//	            		userBucketPath + File.separator + data + File.separator + data + "." + "txt");
//	  
//	        // Convert fileReader to
//	        // bufferedReader
//	        BufferedReader buffReader
//	            = new BufferedReader(
//	                fileReader);
//	  
//	        while (buffReader.ready()) {
//	            System.out.println(
//	                buffReader.readLine());
//	        }

//			 string1 = new String(Files.readAllLines(Paths.get(userBucketPath + File.separator + data + File.separator + data + "." + "txt")));
//			 
//			 System.out.println("Read string--->"+string1);

//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}

		try {
			string1 = sObj.collect(Collectors.joining());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			System.out.println("befor query");
			schoolObj = getSchoolDetails(Integer.parseInt(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			Optional<UploadExcelStatus> obj=	getUploadStatus(Integer.parseInt(data));
			status=obj.get().getStatus();
			
			if(!status.equalsIgnoreCase("4")) {
				resObj.put("status", "6");
				return resObj;
			}else if(status.equalsIgnoreCase("4")) {
				nativeRepository.updateQueries(
						"update moe_sdmis_uploadexcelstatus set status=6 where school_id=" + Integer.parseInt(data));
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		LinkedList<StudentBasicProfileTmp> ltStudentObj = new LinkedList<StudentBasicProfileTmp>();
		LinkedList<StudentFacilityDetailsTmp> ltStudentFacilityObj = new LinkedList<StudentFacilityDetailsTmp>();
		try {
//			System.out.println("before set get");
			TypeReference<List<Map<String, HashMap<String, String>>>> typeRef = new TypeReference<List<Map<String, HashMap<String, String>>>>() {
			};
			List<Map<String, HashMap<String, String>>> obj = objectMapper.readValue(string1, typeRef);

//			FileInputStream fi = new FileInputStream(new File("E:\\UDISE\\bulkExcel\\3103026\\3103026.txt"));
//			ObjectInputStream oi = new ObjectInputStream(fi);
//
//			List<Map<String, HashMap<String, String>>> obj= (List<Map<String, HashMap<String, String>>>) oi.readObject();
//			
//			
//			System.out.println(obj.get(0).get("classId").get("value"));

//			System.out.println("After set get----->"+obj.size());
//	        ExecutorService executorService = Executors.newFixedThreadPool(obj.size());  

//			ExecutorService executor = Executors.newCachedThreadPool();
//			 List<Future<List<Map<String, HashMap<String, String>>>>> futures = executor.invokeAll(obj);

//			obj.parallelStream().forEach(e->{

//				 executorService.execute(new Runnable() {  
//		              
//			            @Override  
//			            public void run() {  

//				Map<String, HashMap<String, String>> studentObj =e;
//			});

			for (Map<String, HashMap<String, String>> studentObj : obj) {
				StudentBasicProfileTmp studentPojo = new StudentBasicProfileTmp();
//				try {
				studentPojo.setSchoolId(handleInteger(data));
				if (nullCheck(studentObj.get("classId")).get("v").equalsIgnoreCase("PP1")) {
					studentPojo.setClassId(-1);
				} else if (nullCheck(studentObj.get("classId")).get("v").equalsIgnoreCase("PP2")) {
					studentPojo.setClassId(-2);
				} else if (nullCheck(studentObj.get("classId")).get("v").equalsIgnoreCase("PP3")) {
					studentPojo.setClassId(-3);
				} else {
					studentPojo.setClassId(handleInteger(nullCheck(studentObj.get("classId")).get("v")));
				}
				studentPojo.setRollNo(handleInteger(studentObj.get("rollNo").get("v")));
				studentPojo.setGuardianName(studentObj.get("guardianName").get("v"));
//					studentPojo.setSectionId(handleInteger(nullCheck(studentObj.get("sectionId")).get("v")));
				studentPojo.setSectionId(
						new GeneralUtility().sectionId(studentObj.get("sectionId").get("v").toUpperCase()));
				studentPojo.setStudentName(studentObj.get("studentName").get("v"));
				studentPojo.setGender(handleInteger(studentObj.get("gender").get("v")));
				studentPojo.setDob(sourceFormat.parse(studentObj.get("studentDob").get("v").replaceAll("-", "/")));
				studentPojo.setMotherName(studentObj.get("motherName").get("v"));
				studentPojo.setFatherName(studentObj.get("fatherName").get("v"));
				studentPojo.setAadhaarNo(studentObj.get("aadhaarNo").get("v"));
				studentPojo.setNameAsAadhaar(studentObj.get("nameAsAadhaar").get("v"));
				studentPojo.setAddress(studentObj.get("address").get("v"));
				studentPojo.setPincode(handleInteger(nullCheck(studentObj.get("pincode")).get("v")));
				studentPojo.setPrimaryMobile(studentObj.get("mobileNo_1").get("v"));
				studentPojo.setSecondaryMobile(studentObj.get("mobileNo_2").get("v"));
				studentPojo.setEmail(studentObj.get("emailId").get("v"));
				studentPojo.setMotherTongue(handleInteger(nullCheck(studentObj.get("motherTongue")).get("v")));
				studentPojo.setSocCatId(handleInteger(nullCheck(studentObj.get("socCatId")).get("v")));
				studentPojo.setMinorityId(handleInteger(nullCheck(studentObj.get("minorityId")).get("v")));
				studentPojo.setIsBplYN(handleInteger(nullCheck(studentObj.get("isBplYn")).get("v")));
				studentPojo.setCreatedBy(userid);
				if (studentPojo.getIsBplYN() == 2) {
					studentPojo.setAayBplYN(9);
				} else {
					studentPojo.setAayBplYN(handleInteger(nullCheck(studentObj.get("aayBplYn")).get("v")));
				}
				studentPojo.setEwsYN(handleInteger(nullCheck(studentObj.get("ewsYn")).get("v")));
				studentPojo.setCwsnYN(handleInteger(nullCheck(studentObj.get("cwsnYn")).get("v")));
				if (studentPojo.getCwsnYN() != null && studentPojo.getCwsnYN() == 2) {
				} else {
					studentPojo.setImpairmentType(
							new GeneralUtility().CustomStringMapper(studentObj.get("impairmentType").get("v")));
				}
				studentPojo.setNatIndYN(handleInteger(nullCheck(studentObj.get("natIndYn")).get("v")));
				studentPojo.setOoscYn(handleInteger(nullCheck(studentObj.get("ooscYn")).get("v")));

				if (studentPojo.getOoscYn() == 2) {
					studentPojo.setOoscMainstreamedYn(9);
				} else {
					studentPojo.setOoscMainstreamedYn(
							handleInteger(nullCheck(studentObj.get("ooscMainstreamedYn")).get("v")));
				}

				studentPojo.setAdmnNumber(studentObj.get("admnNumber").get("v"));
				studentPojo.setAdmnStartDate(
						sourceFormat.parse(studentObj.get("admnStartDate").get("v").replaceAll("-", "/")));

				if (studentPojo.getClassId() == 11 || studentPojo.getClassId() == 12) {
					studentPojo.setAcademicStream(handleInteger(nullCheck(studentObj.get("acdemicStream")).get("v")));
				} else {
					studentPojo.setAcademicStream(0);
				}

				studentPojo.setEnrStatusPY(handleInteger(nullCheck(studentObj.get("enrStatusPy")).get("v")));

				if (studentPojo.getEnrStatusPY() == 3 || studentPojo.getEnrStatusPY() == 4) {

					studentPojo.setClassPY(0);
				} else {
					if (studentPojo.getClassId() == -1 || studentPojo.getClassId() == -2
							|| studentPojo.getClassId() == -3) {
						studentPojo.setClassPY(0);
					} else if (studentPojo.getClassId() == 1) {
						if (nullCheck(studentObj.get("classPy")).get("v").equalsIgnoreCase("PP")
								|| nullCheck(studentObj.get("classPy")).get("v").equalsIgnoreCase("PP1")
								|| nullCheck(studentObj.get("classPy")).get("v").equalsIgnoreCase("PP2")
								|| nullCheck(studentObj.get("classPy")).get("v").equalsIgnoreCase("PP3")) {
							studentPojo.setClassPY(-1);
						} else {
							studentPojo.setClassPY(handleInteger(nullCheck(studentObj.get("classPy")).get("v")));
						}
					} else {
						if (nullCheck(studentObj.get("classPy")).get("v").equalsIgnoreCase("PP")) {
							studentPojo.setClassPY(0);
						} else {
							studentPojo.setClassPY(handleInteger(nullCheck(studentObj.get("classPy")).get("v")));
						}
					}
				}
				studentPojo.setEnrTypeCy(handleInteger(nullCheck(studentObj.get("enrTypeCy")).get("v")));
				if (studentPojo.getEnrStatusPY() == 3 || studentPojo.getEnrStatusPY() == 4) {
					studentPojo.setExamAppearedPyYn(9);
				} else {
					studentPojo
							.setExamAppearedPyYn(handleInteger(nullCheck(studentObj.get("examAppearedPyYn")).get("v")));
				}

				if (studentPojo.getEnrStatusPY() == 3 || studentPojo.getEnrStatusPY() == 4
						|| studentPojo.getExamAppearedPyYn() == 2) {
					studentPojo.setExamResultPy(0);
					studentPojo.setExamMarksPy(0);
				} else {
					studentPojo.setExamResultPy(handleInteger(nullCheck(studentObj.get("examResultPy")).get("v")));
					studentPojo.setExamMarksPy(handleInteger(nullCheck(studentObj.get("examMarksPy")).get("v")));
				}

				studentPojo.setAttendancePy(handleInteger(nullCheck(studentObj.get("attendencePy")).get("v")));
				studentPojo.setAcYearId(9);
				studentPojo.setIsBulkUploaded(1);
				studentPojo.setIsProfileActive(1);

				StudentFacilityDetailsTmp stdfacility = new StudentFacilityDetailsTmp();

//					try {
				stdfacility.setStudentBasicProfileTmp(studentPojo);
				stdfacility.setCreatedBy(userid);
//	        		stdfacility.setStudentId(1L);
				stdfacility.setSchoolId(Integer.parseInt(data));
				stdfacility.setFacProvided(jsonCreation(nullCheck(studentObj.get("textBoxFacProvided")).get("v"),
						nullCheck(studentObj.get("uniformFacProvided")).get("value")));
//						System.out.println(studentObj.get("centralScholarshipId"));

				if (stdfacility.getFacProvided() != null && stdfacility.getFacProvided().length > 0) {
					stdfacility.setIsFacProv(1);
				} else {
					stdfacility.setIsFacProv(9);
				}

				stdfacility
						.setCentralScholarshipId(handleInteger(nullCheck(studentObj.get("centrlSchlrshpId")).get("v")));
				stdfacility
						.setCentralScholarshipYn(handleInteger(nullCheck(studentObj.get("centrlSchlrshpYn")).get("v")));
				stdfacility.setStateScholarshipYn(handleInteger(nullCheck(studentObj.get("stateSchlrshpYn")).get("v")));
				stdfacility.setOtherScholarshipYn(handleInteger(nullCheck(studentObj.get("otherSchlrshpYn")).get("v")));

				stdfacility.setScholarshipAmount(handleInteger(nullCheck(studentObj.get("schlrshpAmount")).get("v")));
				if ((studentObj.get("facProvidedCwsn") != null && studentObj.get("facProvidedCwsn").get("v") != null)) {
					stdfacility.setFacProvidedCwsn(jsonCwsn(studentObj.get("facProvidedCwsn").get("v")));
				}

				if (stdfacility.getFacProvidedCwsn() != null && stdfacility.getFacProvidedCwsn().length > 0) {
					stdfacility.setIsCwsnFacProv(1);
				} else {
					stdfacility.setIsCwsnFacProv(9);
				}

				stdfacility.setScreenedForSld(handleInteger(nullCheck(studentObj.get("scrndFrSld")).get("v")));
				stdfacility.setSldType(handleInteger(nullCheck(studentObj.get("sldType")).get("v")));
				stdfacility.setScreenedForAsd(handleInteger(nullCheck(studentObj.get("scrndFrAsd")).get("v")));
				stdfacility.setScreenedForAdhd(handleInteger(nullCheck(studentObj.get("scrndFrAdhd")).get("v")));
				stdfacility.setIsEcActivity(handleInteger(nullCheck(studentObj.get("isEcActivity")).get("v")));
//	        		stdfacility.setGiftedChildren(Integer.parseInt(studentObj.get("").get("value")));
				stdfacility.setMentorProvided(handleInteger(nullCheck(studentObj.get("mentorProvided")).get("v")));
				stdfacility.setNurturanceCmpsState(
						handleInteger(nullCheck(studentObj.get("nurturanceCmpsState")).get("v")));
				stdfacility.setNurturanceCmpsNational(
						handleInteger(nullCheck(studentObj.get("nurturanceCmpsNational")).get("v")));
				stdfacility.setOlympdsNlc(handleInteger(nullCheck(studentObj.get("olympdsNlc")).get("v")));
				stdfacility.setNccNssYn(handleInteger(nullCheck(studentObj.get("nccNssYn")).get("v")));

//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}

				StudentVocationalDetailsTmp vocObj = new StudentVocationalDetailsTmp();
//					try {
				vocObj.setStudentBasicProfileTmp(studentPojo);
				vocObj.setCreatedBy(userid);
				vocObj.setSchoolId(Integer.parseInt(data));
				vocObj.setVocExposure(handleInteger(nullCheck(studentObj.get("vocYn")).get("v")));
				vocObj.setSector(handleInteger(nullCheck(studentObj.get("sector")).get("v")));
				vocObj.setJobRole(handleInteger(nullCheck(studentObj.get("jobRole")).get("v")));
				vocObj.setAppVocPy(handleInteger(nullCheck(studentObj.get("appVocPy")).get("v")));
//					}catch(Exception ex) {
//						ex.printStackTrace();
//					}
//					

				studentPojo.setStudentFacilityDetailsTmp(stdfacility);
				if (schoolObj.getRowValue().get(0).get("is_vocational_active") != null && String
						.valueOf(schoolObj.getRowValue().get(0).get("is_vocational_active")).equalsIgnoreCase("1")) {
					studentPojo.setStudentVocationalDetailsTmp(vocObj);
				}
				ltStudentObj.add(studentPojo);

//				} catch (Exception ex) {
//					ex.printStackTrace();
//					
//				}

//				System.out.println(stdfacility);

//			}

			}

			List<StudentBasicProfileTmp> stdBasicList = studentBasicProfileTmpRepository.saveAll(ltStudentObj);
//			nativeRepository.updateQueries(
//					"update stu_pro_enr_details set is_profile_active=2 where school_id=" + Integer.parseInt(data));
			nativeRepository.insertQueries(
					"insert into stu_pro_enr_details  (student_id,school_id, class_id, section_id, ac_year_id, roll_no, student_name, gender, student_dob, mother_name, father_name, guardian_name, aadhaar_no, name_as_aadhaar, address, pincode, mobile_no_1, mobile_no_2, email_id, mother_tongue, soc_cat_id, minority_id, is_bpl_yn, aay_bpl_yn, ews_yn, cwsn_yn, impairment_type, nat_ind_yn, oosc_yn, oosc_mainstreamed_yn, admn_number, admn_start_date, admn_end_date, acdemic_stream, enr_status_py, class_py, enr_type_cy, exam_appeared_py_yn, exam_result_py, exam_marks_py, attendence_py, created_by, created_time, profile_status, enrol_status, pro_modified_time, pro_modified_by, enr_modified_by, enr_modified_time, student_code_nat, is_profile_active, inactive_reason, form_status, is_bulk_uploaded, temp_id)\r\n"
							+ "select nextval('student_id_seq'), school_id, class_id, section_id, ac_year_id, roll_no, student_name, gender, student_dob, mother_name, father_name, guardian_name, aadhaar_no, name_as_aadhaar, address, pincode, mobile_no_1, mobile_no_2, email_id, mother_tongue, soc_cat_id, minority_id, is_bpl_yn, aay_bpl_yn, ews_yn, cwsn_yn, impairment_type, nat_ind_yn, oosc_yn, oosc_mainstreamed_yn, admn_number, admn_start_date, admn_end_date, acdemic_stream, enr_status_py, class_py, enr_type_cy, exam_appeared_py_yn, exam_result_py, exam_marks_py, attendence_py, created_by, created_time, profile_status, enrol_status, pro_modified_time, pro_modified_by, enr_modified_by, enr_modified_time, student_code_nat, is_profile_active, inactive_reason, form_status, is_bulk_uploaded, temp_id\r\n"
							+ "from stu_pro_enr_details_tmp where school_id=" + Integer.parseInt(data));
			nativeRepository.insertQueries("INSERT INTO public.stu_fac_othr_details\r\n"
					+ "(student_id, school_id, fac_provided, centrl_schlrshp_yn, centrl_schlrshp_id, state_schlrshp_yn, othr_schlrshp_yn, schlrshp_amount, fac_provided_cwsn, scrnd_fr_sld, sld_type, scrnd_fr_asd, scrnd_fr_adhd, is_ec_activity, gifted_cldrn, mentor_prov, nurturance_cmps_state, nurturance_cmps_national, olympds_nlc, ncc_nss_yn, created_by, created_time, modified_by, modified_time,is_fac_prov,is_cwsn_fac_prov)\r\n"
					+ "select sped.student_id, sfodt.school_id, sfodt.fac_provided, sfodt.centrl_schlrshp_yn, sfodt.centrl_schlrshp_id, sfodt.state_schlrshp_yn, sfodt.othr_schlrshp_yn, sfodt.schlrshp_amount, sfodt.fac_provided_cwsn, sfodt.scrnd_fr_sld, sfodt.sld_type, sfodt.scrnd_fr_asd, sfodt.scrnd_fr_adhd, sfodt.is_ec_activity, sfodt.gifted_cldrn, sfodt.mentor_prov, sfodt.nurturance_cmps_state, sfodt.nurturance_cmps_national, sfodt.olympds_nlc, sfodt.ncc_nss_yn, sfodt.created_by, sfodt.created_time, sfodt.modified_by, sfodt.modified_time,sfodt.is_fac_prov,sfodt.is_cwsn_fac_prov from stu_fac_othr_details_tmp sfodt, stu_pro_enr_details sped where sfodt.temp_id =sped.temp_id and sfodt.school_id ="
					+ Integer.parseInt(data));

			nativeRepository.insertQueries("INSERT INTO public.stu_voc_details\r\n"
					+ "(student_id, school_id, voc_yn, sector, job_role, app_voc_py, marks_obt, created_by, created_time, modified_by, modified_time)\r\n"
					+ "select sped.student_id, sfodt.school_id, sfodt.voc_yn, sfodt.sector, sfodt.job_role, sfodt.app_voc_py, sfodt.marks_obt, sfodt.created_by, sfodt.created_time, sfodt.modified_by, sfodt.modified_time from stu_voc_details_tmp sfodt, stu_pro_enr_details sped where sfodt.temp_id =sped.temp_id and sfodt.school_id ="
					+ Integer.parseInt(data));

			studentFacilityDetailsTmpRepository.deleteAllBySchoolId(Integer.parseInt(data));
			studentVocationalDetailsTmpRepository.deleteAllBySchoolId(Integer.parseInt(data));
			studentBasicProfileTmpRepository.deleteAllBySchoolId(Integer.parseInt(data));

			nativeRepository.updateQueries(
					"update school_master_live set is_exl_active=2 where school_id=" + Integer.parseInt(data));
			nativeRepository.updateQueries(
					"update moe_sdmis_uploadexcelstatus set status=5 where school_id=" + Integer.parseInt(data));

			resObj.put("status", "1");
		} catch (Exception ex) {
			ex.printStackTrace();
			resObj.put("status", "0");
		}

//		try {
//			studentFacilityDetailsRepository.deleteAllBySchoolId(Integer.parseInt(data));
//			studentVocationalDetailsRepository.deleteAllBySchoolId(Integer.parseInt(data));
//			studentBasicProfileRepository.deleteAllBySchoolId(Integer.parseInt(data));
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

//		System.out.println(ltStudentObj);
//		System.out.println("facility size--->"+ltStudentObj.size());

//		try {
//			studentFacilityDetailsRepository.deleteAllBySchoolId(Integer.parseInt(data));
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}

//		 studentBasicProfileRepository.copyStudentDataFromTemp(Integer.parseInt(data));

		return resObj;

	}

	public StaticReportBean getSchoolDetails(Integer schoolId) {
		QueryResult qrObj = null;
		StaticReportBean sobj = new StaticReportBean();
		try {

//			System.out.println(nativeRepository);

			qrObj = nativeRepository.executeQueries(
					"select is_exl_active,is_sec_com,udise_sch_code,is_vocational_active from public.school_master_live where school_id="
							+ schoolId);
			sobj.setColumnName(qrObj.getColumnName());
			sobj.setRowValue(qrObj.getRowValue());
			sobj.setColumnDataType(qrObj.getColumnDataType());
			sobj.setStatus("1");
//    		return sobj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sobj;
	}

	public List<UploadHistory> getUploadedHistor(Integer schoolId) {
//		System.out.println("SchoolId----------->"+schoolId);
//		System.out.println(uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId));

		return uploadHistoryRepository.getBySchoolIdOrderByUploadedTimeDesc(schoolId);
	}

	public Optional<UploadExcelStatus> getUploadStatus(Integer schoolId) {
		return uploadExcelStatusRepository.findById(schoolId);
	}
	
	
	public Map<String,String>  docProcess(Integer schoolId,String userId,String requestAddr){
		
		Map<String,String> mp=new HashMap<String,String>();
		
		try {
			UploadHistory hisObj=new UploadHistory();
			UploadExcelStatus staObj=new UploadExcelStatus();
			
			hisObj.setHost(requestAddr);
			hisObj.setSchoolId(schoolId);
			hisObj.setStatus("2");
			hisObj.setUploadedBy(userId);
			hisObj.setUploadedTime(new Date());
			uploadHistoryRepository.save(hisObj);
			
			
			staObj.setHost(requestAddr);
			staObj.setSchoolId(schoolId);
			staObj.setStatus("2");
			staObj.setUploadedBy(userId);
			staObj.setUploadedDateTime(new Date());
			uploadExcelStatusRepository.save(staObj);
			
			mp.put("status", "1");
			mp.put("message", "Excel will be process shortly. Please check status after 15 minutes");
		}catch(Exception ex) {
			mp.put("status", "0");
			mp.put("message", "Error in processing. Please contact with admin");
			ex.printStackTrace();
		}
		
		return mp;
		
	}

	public Stream<String> getValidatedData(Integer schoolId) throws IOException, ClassNotFoundException {
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

//		FileInputStream fi = new FileInputStream(new File("E:\\UDISE\\bulkExcel\\3103026\\3103026.txt"));
//		ObjectInputStream oi = new ObjectInputStream(fi);
//		
//		List<Map<String, HashMap<String, String>>> ps2= (List<Map<String, HashMap<String, String>>>) oi.readObject();
//		
//		System.out.println(ps2.size());

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

	public void updateExcelStatus(String requestHost, String schoolId, String userid, String status) {
		try {
			UploadExcelStatus uObj = new UploadExcelStatus();
			uObj.setHost(requestHost);
			uObj.setSchoolId(Integer.parseInt(schoolId));
			uObj.setUploadedBy(userid);
			uObj.setStatus(status);
			uObj.setUploadedDateTime(new Date());
			uploadExcelStatusRepository.save(uObj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Integer handleInteger(String data) {
		if (data == null || data == "" || data.equalsIgnoreCase("")) {
			return null;
		}
		return Integer.parseInt(data);
	}

	public String handleString(String data) {
		if (data == null || data == "" || data.equalsIgnoreCase("")) {
			return null;
		}
		return data;
	}

	public HashMap<String, String> nullCheck(HashMap<String, String> data) {
//		System.out.println("data---->"+data);
		if (data == null) {
			return new HashMap<String, String>();
		}
		return data;
	}

	public Integer[] jsonCreation(String freeText, String uniform) throws JsonProcessingException {

		Integer[] intArray = null;

//		HashMap<Integer,Integer> hs=new HashMap<Integer,Integer>();
//		hs.put(3, 2);
//		hs.put(4, 2);
//		hs.put(5, 2);
//		hs.put(6, 2);
//		hs.put(7, 2);
		if (freeText != null && uniform != null) {

			intArray = new Integer[] { 1, 2 };
//			hs.put(1, Integer.parseInt(freeText));
//			hs.put(2,  Integer.parseInt(uniform));
		} else if (freeText != null) {
			intArray = new Integer[] { 1 };
//			hs.put(1, Integer.parseInt(freeText));
		} else if (uniform != null) {
			intArray = new Integer[] { 2 };
//			hs.put(2, Integer.parseInt(uniform));
		}
//		
//		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//		String json = ow.writeValueAsString(hs);

		return intArray;
	}

	public Integer[] jsonCwsn(String facProvidedCwsn) {
//		HashMap<Integer,Integer> hs=new HashMap<Integer,Integer>();
//		hs.put(1, 2);
//		hs.put(2, 2);
//		hs.put(3, 2);
//		hs.put(4, 2);
//		hs.put(5, 2);
//		hs.put(6, 2);
//		hs.put(7, 2);
//		hs.put(8, 2);
//		hs.put(9, 2);
//		hs.put(10, 2);
//		hs.put(11, 2);
//		hs.put(12, 2);

		Integer[] intArray = null;

		if (facProvidedCwsn != null && facProvidedCwsn != "") {
			intArray = new Integer[] { Integer.parseInt(facProvidedCwsn) };
//			hs.put(Integer.parseInt(facProvidedCwsn), 1);
		}

		return intArray;
	}
}
