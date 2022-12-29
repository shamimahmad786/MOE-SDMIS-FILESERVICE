package com.moe.sdmis.fileservice.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.stereotype.Component;

import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.utility.GeneralUtility;

@Component
public class CustomFxcelValidator {

//	String numberRegex ="^\\d+\\.\\d+$";
	String numberRegex = "^[0-9]{0,12}$";
//	String alphanumericRegax = "^[a-zA-Z0-9-,./]([\\w-,./]*[a-zA-Z0-9,.-])?$";
	String alphanumericRegax = "^[\\-,-./a-zA-Z0-9 ]+$";

	String jsonValidation = "\"([^\"]+)\":[\"]*([^,^\\}^\"]+)";
	String stringNonSpecialRegax = "^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$";
	String mobileRegax = "^[6789]\\d{9}$";
	String emaiRegax = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
	String adharRegex = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
	String dateRegax = "(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])-(0[1-9]|1[0-2]|[1-9])-([0-9]{4})";
	String pinregex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
//	String admisionnumericRegax = "^[a-zA-Z0-9-/]([\\w -,/]*[a-zA-Z0-9-/])?$";
	String admisionnumericRegax = "^[\\-/a-zA-Z0-9 ]+$";
	static Integer validationFlag = null;

	
	public Map<String, HashMap<String, String>> validateStudent(CommonBean stObj, StaticReportBean sObj,
			HashMap<String, String> sectionMap, Map<Integer, Boolean> mTongObj, HashMap<String, Boolean> lowerSector,
			HashMap<String, Boolean> lowerSubSector, HashMap<String, Boolean> higherSector,
			HashMap<String, Boolean> higherSubSector,HashSet<String> adharMach) {

//		System.out.println("Date of birth---->"+stObj.getStudentDob());

		Map<String, HashMap<String, String>> mObject = new LinkedHashMap<String, HashMap<String, String>>();
		HashMap<String, String> fs = new HashMap<String, String>();
		fs.put("s", "1");
		mObject.put("fs", fs);

		numberValidation(mObject, "udiseCode", checkNullandTrim(stObj.getUdisecode()), 99999999999L);
		try {

//			System.out.println("From-->"+Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_frm")))) +"---class----->"+stObj.getClassId()+"-------To-----"+Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_to")))));

			if (stObj.getClassId() != null && stObj.getClassId() != "" && !stObj.getClassId().equalsIgnoreCase("null")
					&& (stObj.getClassId().equalsIgnoreCase("PP1") || stObj.getClassId().equalsIgnoreCase("PP2")
							|| stObj.getClassId().equalsIgnoreCase("PP3"))) {
				blankAndTrueValidation(mObject, "classId", stObj.getClassId());
			} else if (stObj.getClassId() != null && stObj.getClassId() != ""
					&& !stObj.getClassId().equalsIgnoreCase("null")
					&& Integer
							.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_frm")))) <= Integer
									.parseInt(classCheck(checkNull(stObj.getClassId())))
					&& Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_to")))) >= Integer
							.parseInt(classCheck(checkNull(stObj.getClassId())))) {
//		numberValidation(mObject, "classId", checkNullandTrim(stObj.getClassId()), 12);
				blankAndTrueValidation(mObject, "classId", stObj.getClassId());
			} else {
				System.out.println("called class--->" + stObj.getClassId());
				blankAndFalseValidation(mObject, "classId", stObj.getClassId());
			}
		} catch (Exception ex) {
			blankAndFalseValidation(mObject, "classId", stObj.getClassId());
			ex.printStackTrace();
		}

		try {
			if (sectionMap.get(stObj.getClassId()) != null
					&& Integer.parseInt(checkNull(sectionMap.get(checkNull(stObj.getClassId())))) >= Integer
							.parseInt(checkNull(String.valueOf(checkNull(stObj.getSectionId()))))) {
				blankAndTrueValidation(mObject, "sectionId", stObj.getSectionId());
			} else {
				blankAndFalseValidation(mObject, "sectionId", stObj.getSectionId());
			}
		} catch (Exception ex) {
			blankAndFalseValidation(mObject, "sectionId", stObj.getSectionId());
			ex.printStackTrace();
		}

		stringNonSpecialValidation(mObject, "studentName", checkNullandTrim(stObj.getStudentName()), 3, 50);
		
		if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("1") ) {
		if(stObj.getGender().equalsIgnoreCase("1") || stObj.getGender().equalsIgnoreCase("3")) {
//			numberValidation(mObject, "gender", checkNullandTrim(stObj.getGender()), 3);
			blankAndTrueValidation(mObject, "gender", stObj.getGender());
		}else {
			blankAndFalseValidation(mObject, "gender", stObj.getGender());
		}
		}else if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("2") ) {
			if(stObj.getGender().equalsIgnoreCase("2") || stObj.getGender().equalsIgnoreCase("3")) {
				blankAndTrueValidation(mObject, "gender", stObj.getGender());
			}else {
				blankAndFalseValidation(mObject, "gender", stObj.getGender());
			}
		}else if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("3")) {
			if(stObj.getGender().equalsIgnoreCase("2") || stObj.getGender().equalsIgnoreCase("3") || stObj.getGender().equalsIgnoreCase("1") ) {
				blankAndTrueValidation(mObject, "gender", stObj.getGender());
			}else {
				blankAndFalseValidation(mObject, "gender", stObj.getGender());
			}
		}
		
		try {
		if(stObj.getClassId() !=null && stObj.getStudentDob() !=null && sObj.getRowValue().get(0).get("session_start_date") !=null && getAge(stObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date")))>0) {		
//System.out.println("Age chack");
			//			dateValidation(mObject, "studentDob", stObj.getStudentDob());
		if(StudentAgeClassValidation(Integer.parseInt(stObj.getClassId()),Integer.parseInt(String.valueOf(getAge(stObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date"))))))) {
			blankAndTrueValidation(mObject, "studentDob", stObj.getStudentDob());
		}else {
			blankAndFalseValidation(mObject, "studentDob", stObj.getStudentDob());
		}
		}else {
			System.out.println("Age else");
			blankAndFalseValidation(mObject, "studentDob", stObj.getStudentDob());
		}

		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "studentDob", stObj.getStudentDob());
			ex.printStackTrace();
		}
		
		stringNonSpecialValidation(mObject, "motherName", checkNullandTrim(stObj.getMotherName()), 3, 50);
		stringNonSpecialValidation(mObject, "fatherName", checkNullandTrim(stObj.getFatherName()), 3, 50);
		stringNonSpecialValidation(mObject, "guardianName", checkNullandTrim(stObj.getGuardianName()), 3, 50);
		adharValidation(mObject, "aadhaarNo", checkNullandTrim(stObj.getAadhaarNo()),adharMach);
		if (!stObj.getAadhaarNo().equalsIgnoreCase("999999999999")) {
			stringNonSpecialValidation(mObject, "nameAsAadhaar", checkNullandTrim(stObj.getNameAsAadhaar()), 3, 5);
		} else {
			blankAndTrueValidation(mObject, "nameAsAadhaar", "");
		}
		stringValidation(mObject, "address", checkNullandTrim(stObj.getAddress()), 20, 100);
		pincodeValidation(mObject, "pincode", checkNullandTrim(stObj.getPincode()));
		mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stObj.getMobileNo_1()));
		mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stObj.getMobileNo_2()));
		emailValidation(mObject, "emailId", checkNullandTrim(stObj.getEmailId()));

		try {
			if (stObj.getMotherTongue() != null && stObj.getMotherTongue() != ""
					&& !stObj.getMotherTongue().equalsIgnoreCase("null")
					&& mTongObj.get(Integer.parseInt(String.valueOf(stObj.getMotherTongue()))) != null
					&& mTongObj.get(Integer.parseInt(String.valueOf(stObj.getMotherTongue())))) {
				blankAndTrueValidation(mObject, "motherTongue", checkNullandTrim(stObj.getMotherTongue()));
			} else {
				blankAndFalseValidation(mObject, "motherTongue", checkNullandTrim(stObj.getMotherTongue()));
			}
		} catch (Exception ex) {
			blankAndFalseValidation(mObject, "motherTongue", checkNullandTrim(stObj.getMotherTongue()));
			ex.printStackTrace();
		}
		numberValidation(mObject, "socCatId", checkNullandTrim(stObj.getSocCatId()), 4);
		numberValidation(mObject, "minorityId", checkNullandTrim(stObj.getMinorityId()), 7);
		numberValidation(mObject, "isBplYn", checkNullandTrim(stObj.getIsBplYn()), 2);

		if (stObj.getIsBplYn() != null && stObj.getIsBplYn() != "" && !stObj.getIsBplYn().equalsIgnoreCase("null")
				&& stObj.getIsBplYn().equalsIgnoreCase("1")) {
			numberValidation(mObject, "aayBplYn", checkNullandTrim(stObj.getAayBplYn()), 2);
		} else {
			blankAndTrueValidation(mObject, "aayBplYn", "");
//			blankAndFalseValidation(mObject, "aayBplYn", checkNullandTrim(stObj.getAayBplYn()));
		}

		numberValidation(mObject, "ewsYn", checkNullandTrim(stObj.getEwsYn()), 2);
		numberValidation(mObject, "cwsnYn", checkNullandTrim(stObj.getCwsnYn()), 2);
		if (stObj.getImpairmentType() !=null && checkNull(stObj.getCwsnYn()).equalsIgnoreCase("1")) {
			
			System.out.println("check JSON---->"+GeneralUtility.ImperialMapping.get(String.valueOf(stObj.getImpairmentType())));
			
			if(GeneralUtility.ImperialMapping.get(String.valueOf(stObj.getImpairmentType())) !=null && GeneralUtility.ImperialMapping.get(String.valueOf(stObj.getImpairmentType()))) {
			jsonValidation(mObject, "impairmentType", checkNullandTrim(stObj.getImpairmentType()));
			}else {
				blankAndFalseValidation(mObject, "impairmentType", stObj.getImpairmentType());
			}
		} else {
			blankAndTrueValidation(mObject, "impairmentType", "");
		}
		numberValidation(mObject, "natIndYn", checkNullandTrim(stObj.getNatIndYn()), 2);
		numberValidation(mObject, "ooscYn", checkNullandTrim(stObj.getOoscYn()), 2);
		
		if(stObj.getOoscYn() !=null && stObj.getOoscYn().equalsIgnoreCase("1")) {
		numberValidation(mObject, "ooscMainstreamedYn", checkNullandTrim(stObj.getOoscMainstreamedYn()), 2);
		}else {
			blankAndTrueValidation(mObject, "ooscMainstreamedYn", "");
		}
		admisionNumberValidation(mObject, "admnNumber", checkNullandTrim(stObj.getAdmnNumber()));
		
		
		
		if(stObj.getAdmnStartDate() !=null && stObj.getAdmnStartDate() !="" && sObj.getRowValue().get(0).get("session_start_date") !=null && sObj.getRowValue().get(0).get("session_end_date") !=null ) {
		try {
			DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter adminsionformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate sessionStartDate = LocalDate.parse(String.valueOf(sObj.getRowValue().get(0).get("session_start_date")), dbformatter);
			LocalDate sessionEndDate = LocalDate.parse(String.valueOf(sObj.getRowValue().get(0).get("session_end_date")), dbformatter);
			LocalDate todayDate = LocalDate.now();
			LocalDate admisionDate = LocalDate.parse(String.valueOf(stObj.getAdmnStartDate()), adminsionformatter);

		if(! admisionDate.isAfter(todayDate) && ! admisionDate.isAfter(sessionEndDate) &&  (admisionDate.isAfter(sessionStartDate) || admisionDate.isEqual(sessionStartDate))) {
			blankAndTrueValidation(mObject, "admnStartDate", stObj.getAdmnStartDate());
		}else {
			blankAndFalseValidation(mObject, "admnStartDate", stObj.getAdmnStartDate());
		}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "admnStartDate", stObj.getAdmnStartDate());
			ex.printStackTrace();
		}
		
		}
		
		
		if (checkNull(stObj.getClassId()).equalsIgnoreCase("11")
				|| checkNull(stObj.getClassId()).equalsIgnoreCase("12")) {

			if (stObj.getAcdemicStream() != null && stObj.getAcdemicStream().equalsIgnoreCase("1")) {
				if (sObj.getRowValue().get(0).get("strm_arts") != null
						&& sObj.getRowValue().get(0).get("strm_arts") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_arts"))) == 1) {
//			numberValidation(mObject, "acdemicStream", checkNullandTrim(stObj.getAcdemicStream()), 5);
					blankAndTrueValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				} else {
					blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				}
			} else if (stObj.getAcdemicStream() != null && stObj.getAcdemicStream().equalsIgnoreCase("2")) {
				if (sObj.getRowValue().get(0).get("strm_science") != null
						&& sObj.getRowValue().get(0).get("strm_science") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_science"))) == 1) {
					blankAndTrueValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				} else {
					blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				}
			} else if (stObj.getAcdemicStream() != null && stObj.getAcdemicStream().equalsIgnoreCase("3")) {
				if (sObj.getRowValue().get(0).get("strm_commerce") != null
						&& sObj.getRowValue().get(0).get("strm_commerce") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_commerce"))) == 1) {
					blankAndTrueValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				} else {
					blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				}
			} else if (stObj.getAcdemicStream() != null && stObj.getAcdemicStream().equalsIgnoreCase("4")) {
				if (sObj.getRowValue().get(0).get("strm_vocational") != null
						&& sObj.getRowValue().get(0).get("strm_vocational") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_vocational"))) == 1) {
					blankAndTrueValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				} else {
					blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				}
			} else if (stObj.getAcdemicStream() != null && stObj.getAcdemicStream().equalsIgnoreCase("5")) {
				if (sObj.getRowValue().get(0).get("strm_other") != null
						&& sObj.getRowValue().get(0).get("strm_other") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_other"))) == 1) {
					blankAndTrueValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				} else {
					blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
				}
			} else {
				blankAndFalseValidation(mObject, "acdemicStream", stObj.getAcdemicStream());
			}

		} else {
			blankAndTrueValidation(mObject, "acdemicStream", "");
		}

		numberValidation(mObject, "enrStatusPy", checkNullandTrim(stObj.getEnrStatusPy()), 4);
//System.out.println(stObj.getClassId());
//		System.out.println(stObj.getClassPy());
		if (stObj.getEnrStatusPy().equalsIgnoreCase("1") || stObj.getEnrStatusPy().equalsIgnoreCase("2")) {
			if ((( stObj.getClassId().equalsIgnoreCase("1"))
					&& ( stObj.getClassPy().equalsIgnoreCase("PP")))
					|| stObj.getClassId().equalsIgnoreCase("PP")) {
				blankAndTrueValidation(mObject, "classPy", stObj.getClassPy());
			} else {
				try {
					System.out.println("Previous Academic class-->"+stObj.getClassPy());
					int stClass = Integer.parseInt(classCheck(stObj.getClassId()));
					if (stClass > 0 && stClass <= 12 && (stClass == Integer.parseInt(stObj.getClassPy())
							|| stClass == Integer.parseInt(stObj.getClassPy()) + 1)) {
						System.out.println("in if condition");
						blankAndTrueValidation(mObject, "classPy", stObj.getClassPy());
					} else {
						blankAndFalseValidation(mObject, "classPy", stObj.getClassPy());
					}
				} catch (Exception ex) {
					blankAndFalseValidation(mObject, "classPy", stObj.getClassPy());
					ex.printStackTrace();
				}
			}
		} else {
			blankAndTrueValidation(mObject, "classPy", "");
		}

		if (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) == 5) {
			numberValidation(mObject, "enrTypeCy", checkNullandTrim(stObj.getEnrTypeCy()), 5);
		} else {
			blankAndTrueValidation(mObject, "enrTypeCy", "");
		}

		if (stObj.getEnrStatusPy().equalsIgnoreCase("1") || stObj.getEnrStatusPy().equalsIgnoreCase("2")) {
			numberValidation(mObject, "examAppearedPyYn", checkNullandTrim(stObj.getExamAppearedPyYn()), 2);

			if (stObj.getExamAppearedPyYn().equalsIgnoreCase("1")) {
				numberValidation(mObject, "examResultPy", checkNullandTrim(stObj.getExamResultPy()), 4);
				numberValidation(mObject, "examMarksPy", checkNullandTrim(stObj.getExamMarksPy()), 100);
			} else {
				blankAndTrueValidation(mObject, "examResultPy", "");
				blankAndTrueValidation(mObject, "examMarksPy", "");
			}

		} else {
			blankAndTrueValidation(mObject, "examAppearedPyYn", "");
			blankAndTrueValidation(mObject, "examResultPy", "");
			blankAndTrueValidation(mObject, "examMarksPy", "");
		}

		numberValidation(mObject, "attendencePy", checkNullandTrim(stObj.getAttendencePy()), 365);
//		numberValidation(mObject,"acYearId",checkNullandTrim(stObj.getAcYearId()));
		numberValidation(mObject, "rollNo", checkNullandTrim(stObj.getRollNo()), 999);

		
		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		numberValidation(mObject, "uniformFacProvided", checkNullandTrim(stObj.getUniformFacProvided()), 2);
		numberValidation(mObject, "textBoxFacProvided", checkNullandTrim(stObj.getTextBoxFacProvided()), 2);
		}
		
		numberValidation(mObject, "centrlSchlrshpYn", checkNullandTrim(stObj.getCentrlSchlrshpYn()), 2);
		if (checkNull(stObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")) {
			numberValidation(mObject, "centrlSchlrshpId", checkNullandTrim(stObj.getCentrlSchlrshpId()), 15);
		} else {
			blankAndTrueValidation(mObject, "centrlSchlrshpId", "");
		}
		numberValidation(mObject, "stateSchlrshpYn", checkNullandTrim(stObj.getStateSchlrshpYn()), 2);
		numberValidation(mObject, "otherSchlrshpYn", checkNullandTrim(stObj.getOtherSchlrshpYn()), 2);

		if (checkNull(stObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
			numberValidation(mObject, "schlrshpAmount", checkNullandTrim(stObj.getSchlrshpAmount()), 50000);
		} else {
			blankAndTrueValidation(mObject, "schlrshpAmount", "");
		}

		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		if (checkNull(stObj.getCwsnYn()).equalsIgnoreCase("1")) {
			numberValidation(mObject, "facProvidedCwsn", checkNullandTrim(stObj.getFacProvidedCwsn()), 12);
		} else {
			blankAndTrueValidation(mObject, "facProvidedCwsn", "");
		}
		}

		numberValidation(mObject, "scrndFrSld", checkNullandTrim(stObj.getScrndFrSld()), 2);

		numberValidation(mObject, "sldType", checkNullandTrim(stObj.getSldType()), 3);
		numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stObj.getScrndFrAsd()), 2);
		numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stObj.getScrndFrAdhd()), 2);
		numberValidation(mObject, "isEcActivity", checkNullandTrim(stObj.getIsEcActivity()), 2);



		if (stObj.getClassId() != null && sObj.getRowValue() != null
				&& sObj.getRowValue().get(0).get("is_vocational_active") != null
			
				&& Integer.parseInt(
						checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) {
			if((checkNull(stObj.getClassId()).equalsIgnoreCase("9")
						|| checkNull(stObj.getClassId()).equalsIgnoreCase("10")) && (Integer.parseInt(
								checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 || Integer.parseInt(
										checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)) {
			numberValidation(mObject, "vocYn", checkNullandTrim(stObj.getVocYn()), 2);
			}else if((checkNull(stObj.getClassId()).equalsIgnoreCase("11")
					|| checkNull(stObj.getClassId()).equalsIgnoreCase("12")) && (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)){
				numberValidation(mObject, "vocYn", checkNullandTrim(stObj.getVocYn()), 2);
			}else {
				blankAndTrueValidation(mObject, "vocYn", "");
			}
		} else {
			System.out.println("Vocational false condition-->");
			blankAndTrueValidation(mObject, "vocYn", "");
		}

		try {
		
			if(Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
			if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null 
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) 
					&& ((Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 ) ||  Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3 ) && (checkNull(stObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stObj.getClassId()).equalsIgnoreCase("10"))) {
				System.out.println("lower sector true");
				try {
				if (lowerSector.get(String.valueOf(checkNull(stObj.getSector())))) {
					blankAndTrueValidation(mObject, "sector", stObj.getSector());
				} else {
					blankAndFalseValidation(mObject, "sector", stObj.getSector());
				}
				}catch(Exception ex) {
					Map<String, HashMap<String, String>> mp=blankAndFalseValidation(mObject, "sector", stObj.getSector());
					mp.get("sector").put("message", "Sector Id not available");
					ex.printStackTrace();
				}

			} else if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1)
				&& (Integer.parseInt(
						checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
								checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)	&& (checkNull(stObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stObj.getClassId()).equalsIgnoreCase("12"))) {
				System.out.println("higher sector true");
				try {
				if (higherSector.get(String.valueOf(checkNull(stObj.getSector())))) {
					blankAndTrueValidation(mObject, "sector", stObj.getSector());
				} else {
					blankAndFalseValidation(mObject, "sector", stObj.getSector());
				}
				}catch(Exception ex) {
					Map<String, HashMap<String, String>> mp=blankAndFalseValidation(mObject, "sector", stObj.getSector());
					mp.get("sector").put("message", "Sector Id not available");
					
				}
			}
			else {
				System.out.println("4");
				blankAndTrueValidation(mObject, "sector","");
			}
			}else {
				System.out.println("in else sector");
				blankAndTrueValidation(mObject, "sector", "");
			}

		} catch (Exception ex) {
			blankAndFalseValidation(mObject, "sector", stObj.getSector());
			ex.printStackTrace();
		}



		try {
			
			
			if(Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
			if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) 
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3) && (checkNull(stObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stObj.getClassId()).equalsIgnoreCase("10"))
					&& stObj.getJobRole() != null) {
				System.out.println("JOb-1");
				try {
				if (lowerSubSector.get(String.valueOf(checkNull(stObj.getJobRole())))) {
					blankAndTrueValidation(mObject, "jobRole", stObj.getJobRole());
				} else {
					blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
				}
				}catch(Exception ex) {
					Map<String, HashMap<String, String>> mp=blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
					mp.get("jobRole").put("message", "Role Id not available");
					ex.printStackTrace();
				}
			} else if (sObj.getRowValue().get(0).get("is_vocational_active") != null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1 )
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)  && (checkNull(stObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stObj.getClassId()).equalsIgnoreCase("12"))
					&& stObj.getJobRole() != null) {
				try {
					System.out.println("Job-2");
				if (higherSubSector.get(String.valueOf(checkNull(stObj.getJobRole())))) {
					blankAndTrueValidation(mObject, "jobRole", stObj.getJobRole());
				} else {
					blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
				}
				}catch(Exception ex) {
					Map<String, HashMap<String, String>> mp=blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
					mp.get("jobRole").put("message", "Role Id not available");
					ex.printStackTrace();
				}
			}
			}else {
				System.out.println("in else job");
				blankAndTrueValidation(mObject, "jobRole", "");
			}
		} catch (Exception ex) {
			blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
			ex.printStackTrace();
		}

		numberValidation(mObject, "appVocPy", checkNullandTrim(stObj.getAppVocPy()), 2);

		return mObject;

	}

	public Map<String, HashMap<String, String>> blankAndTrueValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);
		hs.put("s", "1");
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> blankAndFalseValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> hs = new HashMap<String, String>();
		HashMap<String, String> fs = new HashMap<String, String>();
		fs.put("s", "0");
		mp.put("fs", fs);
		hs.put("v", value);
		hs.put("s", "0");
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> numberValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value, long valueRange) {

		HashMap<String, String> hs = new HashMap<String, String>();
		HashMap<String, String> fs = new HashMap<String, String>();
		hs.put("v", value);

		long originalValue = 0;

		if (value != "" && value != null && !value.equalsIgnoreCase("PP1") && !value.equalsIgnoreCase("PP2")
				&& !value.equalsIgnoreCase("PP3") && value.matches(numberRegex)) {
			originalValue = Long.parseLong(value);
		}

		if (value != null && value.matches(numberRegex) && value != "" && originalValue <= valueRange) {
			hs.put("s", "1");

			if (mapKey.equalsIgnoreCase("examMarksPy")) {
				if (Integer.parseInt(value) > 100) {
					hs.put("s", "0");
					hs.put("m", "invalid marks");
				}
			} else if (mapKey.equalsIgnoreCase("attendencePy")) {
				if (Integer.parseInt(value) > 250) {
					hs.put("s", "0");
					hs.put("m", "invalid attendence days");
				}
			} else if ((mapKey.equalsIgnoreCase("centrlSchlrshpYn") || mapKey.equalsIgnoreCase("textBoxFacProvided")|| mapKey.equalsIgnoreCase("uniformFacProvided") || mapKey.equalsIgnoreCase("examAppearedPyYn") || mapKey.equalsIgnoreCase("enrStatusPy") || mapKey.equalsIgnoreCase("socCatId") || mapKey.equalsIgnoreCase("cwsnYn")
					|| mapKey.equalsIgnoreCase("ewsYn") || mapKey.equalsIgnoreCase("minorityId")
					|| mapKey.equalsIgnoreCase("isBplYn") || mapKey.equalsIgnoreCase("natIndYn") || mapKey.equalsIgnoreCase("ooscYn") || mapKey.equalsIgnoreCase("ooscMainstreamedYn")) && originalValue < 1) {
				hs.put("s", "0");
				hs.put("m", "invalid value");
			}else if(mapKey.equalsIgnoreCase("gender")) {
//				try {
//				if(GeneralUtility.typeMapping.containsKey(value)) {
//					
//				}
//				}catch(Exception ex) {
//					
//				}
			}
		} else if (mapKey.equalsIgnoreCase("classId")
				&& (value.equalsIgnoreCase("PP1") || value.equalsIgnoreCase("PP2") || value.equalsIgnoreCase("PP3"))) {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "invalid number formate");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		mp.put(mapKey, hs);

		return mp;
	}

	public Map<String, HashMap<String, String>> stringValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value, Integer minRange, Integer maxRange) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);
		if (value != null && (value.length() < minRange || value.length() > maxRange)) {
			hs.put("s", "0");
			hs.put("m", "Invalid string length");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else if (value != null && value.matches(alphanumericRegax) && value != "") {
			hs.put("s", "1");
		} else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		}else {
			hs.put("s", "0");
			hs.put("m", "invalid string formate");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		mp.put(mapKey, hs);

		return mp;

	}

	public Map<String, HashMap<String, String>> jsonValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		System.out.println(GeneralUtility.ImperialMapping.get(value));
		hs.put("v", value);
		if (value != null && GeneralUtility.ImperialMapping.get(value) !=null && !GeneralUtility.ImperialMapping.get(value)) {
			hs.put("s", "0");
			hs.put("m", "Invalid Imperial Type");
			fs.put("s", "0");
			mp.put("fs", fs);
		}else if(GeneralUtility.ImperialMapping.get(value)==null) {
			hs.put("s", "0");
			hs.put("m", "Invalid Imperial Type");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "1");
		
		}
		mp.put(mapKey, hs);

		return mp;

	}

	public Map<String, HashMap<String, String>> alphabetValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> mobileValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);
		if (value != null && GeneralUtility.INVALID_MOBILE_NUMBERS.contains(value)) {
			hs.put("s", "0");
			hs.put("m", "Invalid Mobile Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else if (value != null && value.matches(mobileRegax) && value != "") {
			hs.put("s", "1");
		} else if (mapKey.equalsIgnoreCase("mobileNo_2") && (value == null || value == "")) {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "Invalid Mobile Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}

		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> emailValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();

//		System.out.println("email value--->" + value);
		hs.put("v", value);
		if (value != null && value != "") {
			hs.put("s", "1");
		}
		if (value.matches(emaiRegax)) {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
//			System.out.println("Email Status");
			hs.put("s", "0");
			hs.put("m", "Invalid Email Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> dateValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
//		System.out.println("Student DOB--->"+value);
		try {
			
		
		if (value != null && value.matches(dateRegax) && value != "") {
			hs.put("s", "1");
			
			
		} else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		}else {
			hs.put("s", "0");
			hs.put("m", "Invalid Date Format dd/mm/yyyy");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		hs.put("v", value);
		mp.put(mapKey, hs);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return mp;
	}

	public Map<String, HashMap<String, String>> stringNonSpecialValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value, Integer minRange, Integer maxRange) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();

		System.out.println("lenth--->" + value);
		if (value != null && (value.length() < 3 || value.length() > 50) ) {
			if(mapKey.equalsIgnoreCase("guardianName") && value.length()==0) {
				hs.put("s", "1");	
				hs.put("m", "");
			}else {
				hs.put("s", "0");
				hs.put("m", "Invalid Name length");
				fs.put("s", "0");
				mp.put("fs", fs);
			}
			
			
		
			
		} else if (value != null && value.matches(stringNonSpecialRegax) && value != "") {
			hs.put("s", "1");
		} else if (mapKey.equalsIgnoreCase("guardianName") && (value == null || value == "")) {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "Invalid String Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		hs.put("v", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> pincodeValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
//		System.out.println(mapKey + "   picode validation---->" + value);
		if (value != null && value.matches(pinregex) && value != "") {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "Invalid Pincode Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		hs.put("v", value);
		mp.put(mapKey, hs);

		return mp;
	}

	public Map<String, HashMap<String, String>> adharValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value,HashSet<String> adharMach) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);

//		System.out.println("value-->"+value);
//		System.out.println(value.matches(adharRegex));

		if (value != null && value.matches(adharRegex) && value != "") {
			hs.put("s", "1");
			
			if(!adharMach.add(value) && !value.equalsIgnoreCase("999999999999")) {
				hs.put("s", "0");
				hs.put("m", "Duplicate Adhar");
				fs.put("s", "0");
				mp.put("fs", fs);
			}
		} else if (value.equalsIgnoreCase("999999999999")) {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "Invalid Aadhar Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		hs.put("v", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> admisionNumberValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("v", value);

//		System.out.println("value-->"+value);
//		System.out.println(value.matches(adharRegex));
if(value != null &&  (value.length()<=3 || value.length()>20)) {
	hs.put("s", "0");
	hs.put("m", "Invalid Admision Number");
	fs.put("s", "0");
	mp.put("fs", fs);
}
else	if (value != null && value.matches(admisionnumericRegax) && value != "") {
			hs.put("s", "1");
		}else if(value==null || value =="") {
			hs.put("s", "0");
			hs.put("m", "Required");
			fs.put("s", "0");
			mp.put("fs", fs);
		} else {
			hs.put("s", "0");
			hs.put("m", "Invalid Admision Format");
			fs.put("s", "0");
			mp.put("fs", fs);
		}
		hs.put("v", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public String checkNullandTrim(String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		if (value != null && value != "") {
			return value.trim();
		}
		return value;
	}

	public String checkNull(String value) {
		if (value == null) {
			value = "";
		}
		return value;
	}

	public String classCheck(String classId) {
		if (classId.equalsIgnoreCase("PP1")) {
			return "-1";
		} else if (classId.equalsIgnoreCase("PP2")) {
			return "-2";
		} else if (classId.equalsIgnoreCase("PP3")) {
			return "-3";
		} else {
			return classId;
		}
	}

	public long getAge(String dob,String dbDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		System.out.println("month--->"+dbDate);
//		String months="";
//		if(month.length()==1) {
//			months="0"+month;
//		}
//		String date = "01/"+months+"/2022".replaceAll("-", "/");
		String date = dob.replaceAll("-", "/");
//		dbDate=dbDate.replaceAll("-", "/");
	
		LocalDate localDate = LocalDate.parse(date, formatter);
		LocalDate today = LocalDate.parse(dbDate, dbformatter);
		
		System.out.println("today--->"+today);
		
		long age = ChronoUnit.YEARS.between(localDate, today);
		System.out.println("Age cal--->"+age);
		return age;
	}

	public static boolean StudentAgeClassValidation(Integer grade, Integer age) {
		System.out.println("grad---->"+grade +"age---->"+age);
		boolean status = false;
		if (grade != null && age != null) {
			switch (grade) {
			case -3:
				if (age >= 3 && age <= 5) {
					status = true;
				}
				break;
			case -2:
				if (age >= 3 && age <= 6) {
					status = true;
				}
				break;
			case -1:
				if (age >= 4 && age <= 8) {
					status = true;
				}
				break;

			case 1:
				if (age >= 5 && age <= 12) {
					status = true;
				}
				break;

			case 2:
				if (age >= 5 && age <= 13) {
					status = true;
				}
				break;

			case 3:
				if (age >= 6 && age <= 14) {
					status = true;
				}
				break;

			case 4:
				if (age >= 7 && age <= 15) {
					status = true;
				}
				break;

			case 5:
				if (age >= 8 && age <= 16) {
					status = true;
				}
				break;

			case 6:
				if (age >= 9 && age <= 17) {
					status = true;
				}
				break;

			case 7:
				if (age >= 10 && age <= 18) {
					status = true;
				}
				break;

			case 8:
				if (age >= 11 && age <= 19) {
					status = true;
				}
				break;

			case 9:
				if (age >= 12 && age <= 20) {
					status = true;
				}
				break;

			case 10:
				if (age >= 13) {
					status = true;
				}
				break;

			case 11:
				if (age >= 14) {
					status = true;
				}
				break;

			case 12:
				if (age >= 15) {
					status = true;
				}
				break;
			}
		}
		return status;
	}

}
