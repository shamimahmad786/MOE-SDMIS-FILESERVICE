package com.moe.sdmis.fileservice.validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.stereotype.Component;

import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentTempTable;

@Component
public class CustomFxcelValidator {

//	String numberRegex ="^\\d+\\.\\d+$";
	String numberRegex = "^[0-9]{0,12}$";
	String alphanumericRegax = "^[a-zA-Z0-9-,./]([\\w -,./]*[a-zA-Z0-9,.-])?$";
	String jsonValidation = "\"([^\"]+)\":[\"]*([^,^\\}^\"]+)";
	String stringNonSpecialRegax = "^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$";
	String mobileRegax = "^[789]\\d{9}$";
	String emaiRegax = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
	String adharRegex = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";
	String dateRegax = "(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])-(0[1-9]|1[0-2]|[1-9])-([0-9]{4})";
	String pinregex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
	String admisionnumericRegax = "^[a-zA-Z0-9]([\\w -,/]*[a-zA-Z0-9])?$";
	static Integer validationFlag = null;

	public Map<String, HashMap<String, String>> validateStudent(CommonBean stObj, StaticReportBean sObj, HashMap<String,String> sectionMap,Map<Integer,Boolean> mTongObj,HashMap<String,Boolean> lowerSector,HashMap<String,Boolean> lowerSubSector,HashMap<String,Boolean> higherSector,HashMap<String,Boolean> higherSubSector) {

//		System.out.println("Date of birth---->"+stObj.getStudentDob());

		Map<String, HashMap<String, String>> mObject = new LinkedHashMap<String, HashMap<String, String>>();
		HashMap<String, String> fs = new HashMap<String, String>();
		fs.put("status", "1");
		mObject.put("finalStatus", fs);

		numberValidation(mObject, "udiseCode", checkNullandTrim(stObj.getUdisecode()), 99999999999L);
		try {
			if(stObj.getClassId() !=null && stObj.getClassId() !="" && !stObj.getClassId().equalsIgnoreCase("null") &&  (stObj.getClassId().equalsIgnoreCase("PP1") || stObj.getClassId().equalsIgnoreCase("PP2") || stObj.getClassId().equalsIgnoreCase("PP3"))){
				blankAndTrueValidation(mObject, "classId", stObj.getClassId());
			}
		else if(stObj.getClassId() !=null && stObj.getClassId() !="" && !stObj.getClassId().equalsIgnoreCase("null") && Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_frm"))))<=Integer.parseInt(classCheck(checkNull(stObj.getClassId())))  && Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_to"))))>=Integer.parseInt(classCheck(checkNull(stObj.getClassId())))) {
//		numberValidation(mObject, "classId", checkNullandTrim(stObj.getClassId()), 12);
			blankAndTrueValidation(mObject, "classId", stObj.getClassId());
		}else {
			blankAndFalseValidation(mObject, "classId", stObj.getClassId());
		}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "classId", stObj.getClassId());
			ex.printStackTrace();
		}
		
		try {
		if( sectionMap.get(stObj.getClassId()) !=null &&  Integer.parseInt(checkNull(sectionMap.get(checkNull(stObj.getClassId())))) >= Integer.parseInt(checkNull(String.valueOf(checkNull(stObj.getSectionId()))))) {
			blankAndTrueValidation(mObject, "sectionId", stObj.getSectionId());
		}else {
			blankAndFalseValidation(mObject, "sectionId", stObj.getSectionId());
		}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "sectionId", stObj.getSectionId());
			ex.printStackTrace();
		}
		
		stringNonSpecialValidation(mObject, "studentName", checkNullandTrim(stObj.getStudentName()));
		numberValidation(mObject, "gender", checkNullandTrim(stObj.getGender()), 3);
		dateValidation(mObject, "studentDob", checkNullandTrim(stObj.getStudentDob()));
		stringNonSpecialValidation(mObject, "motherName", checkNullandTrim(stObj.getMotherName()));
		stringNonSpecialValidation(mObject, "fatherName", checkNullandTrim(stObj.getFatherName()));
		stringNonSpecialValidation(mObject, "guardianName", checkNullandTrim(stObj.getGuardianName()));
		adharValidation(mObject, "aadhaarNo", checkNullandTrim(stObj.getAadhaarNo()));
		if (!stObj.getAadhaarNo().equalsIgnoreCase("999999999999")) {
			stringNonSpecialValidation(mObject, "nameAsAadhaar", checkNullandTrim(stObj.getNameAsAadhaar()));
		} else {
			blankAndTrueValidation(mObject, "nameAsAadhaar", "");
		}
		stringValidation(mObject, "address", checkNullandTrim(stObj.getAddress()));
		pincodeValidation(mObject, "pincode", checkNullandTrim(stObj.getPincode()));
		mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stObj.getMobileNo_1()));
		mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stObj.getMobileNo_2()));
		emailValidation(mObject, "emailId", checkNullandTrim(stObj.getEmailId()));
		
		try {
		if(stObj.getMotherTongue() !=null && stObj.getMotherTongue() !="" && !stObj.getMotherTongue().equalsIgnoreCase("null") && mTongObj.get(Integer.parseInt(String.valueOf(stObj.getMotherTongue()))) !=null &&mTongObj.get(Integer.parseInt(String.valueOf(stObj.getMotherTongue())))) {
			blankAndTrueValidation(mObject, "motherTongue",  checkNullandTrim(stObj.getMotherTongue()));
		}else {
			blankAndFalseValidation(mObject, "motherTongue", checkNullandTrim(stObj.getMotherTongue()));
		}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "motherTongue", checkNullandTrim(stObj.getMotherTongue()));
			ex.printStackTrace();
		}
		numberValidation(mObject, "socCatId", checkNullandTrim(stObj.getSocCatId()), 4);
		numberValidation(mObject, "minorityId", checkNullandTrim(stObj.getMinorityId()), 7);
		numberValidation(mObject, "isBplYn", checkNullandTrim(stObj.getIsBplYn()), 2);
		numberValidation(mObject, "aayBplYn", checkNullandTrim(stObj.getAayBplYn()), 2);
		numberValidation(mObject, "ewsYn", checkNullandTrim(stObj.getEwsYn()), 2);
		numberValidation(mObject, "cwsnYn", checkNullandTrim(stObj.getCwsnYn()), 2);
		if (checkNull(stObj.getCwsnYn()).equalsIgnoreCase("1")) {
			jsonValidation(mObject, "impairmentType", checkNullandTrim(stObj.getImpairmentType()));
		} else {
			blankAndTrueValidation(mObject, "impairmentType", "");
		}
		numberValidation(mObject, "natIndYn", checkNullandTrim(stObj.getNatIndYn()), 2);
		numberValidation(mObject, "ooscYn", checkNullandTrim(stObj.getOoscYn()), 2);
		admisionNumberValidation(mObject, "admnNumber", checkNullandTrim(stObj.getAdmnNumber()));
		dateValidation(mObject, "admnStartDate", checkNullandTrim(stObj.getAdmnStartDate()));

		if (checkNull(stObj.getClassId()).equalsIgnoreCase("11")
				|| checkNull(stObj.getClassId()).equalsIgnoreCase("12")) {
			numberValidation(mObject, "acdemicStream", checkNullandTrim(stObj.getAcdemicStream()), 5);
		} else {
			blankAndTrueValidation(mObject, "acdemicStream", "");
		}

		numberValidation(mObject, "enrStatusPy", checkNullandTrim(stObj.getEnrStatusPy()), 4);
//System.out.println(stObj.getClassId());
//		System.out.println(stObj.getClassPy());
		if (stObj.getEnrStatusPy().equalsIgnoreCase("1") || stObj.getEnrStatusPy().equalsIgnoreCase("2")) {
			if (((stObj.getClassId().equalsIgnoreCase("0") ||  stObj.getClassId().equalsIgnoreCase("1"))
					&& (stObj.getClassPy().equalsIgnoreCase("0") || stObj.getClassPy().equalsIgnoreCase("PP"))) || stObj.getClassId().equalsIgnoreCase("PP")) {
				blankAndTrueValidation(mObject, "classPy", stObj.getClassPy());
			} else {
				try {
					int stClass = Integer.parseInt(classCheck(stObj.getClassId()));
					if (stClass > 0 && stClass <= 12 && (stClass == Integer.parseInt(stObj.getClassPy())
							|| stClass == Integer.parseInt(stObj.getClassPy()) + 1)) {
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
			
			if(stObj.getExamAppearedPyYn().equalsIgnoreCase("1")) {
			numberValidation(mObject, "examResultPy", checkNullandTrim(stObj.getExamResultPy()), 4);
			numberValidation(mObject, "examMarksPy", checkNullandTrim(stObj.getExamMarksPy()), 100);
			}else {
				blankAndTrueValidation(mObject, "examResultPy", "");
				blankAndTrueValidation(mObject, "examMarksPy", "");
			}
			
		}else {
			blankAndTrueValidation(mObject, "examAppearedPyYn", "");
			blankAndTrueValidation(mObject, "examResultPy", "");
			blankAndTrueValidation(mObject, "examMarksPy", "");
		}

		numberValidation(mObject, "attendencePy", checkNullandTrim(stObj.getAttendencePy()), 365);
//		numberValidation(mObject,"acYearId",checkNullandTrim(stObj.getAcYearId()));
		numberValidation(mObject, "rollNo", checkNullandTrim(stObj.getRollNo()), 999);

		numberValidation(mObject, "uniformFacProvided", checkNullandTrim(stObj.getUniformFacProvided()), 2);
		numberValidation(mObject, "textBoxFacProvided", checkNullandTrim(stObj.getTextBoxFacProvided()), 2);
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

		if (checkNull(stObj.getCwsnYn()).equalsIgnoreCase("1")) {
			numberValidation(mObject, "facProvidedCwsn", checkNullandTrim(stObj.getFacProvidedCwsn()), 12);
		} else {
			blankAndTrueValidation(mObject, "facProvidedCwsn", "");
		}

		numberValidation(mObject, "scrndFrSld", checkNullandTrim(stObj.getScrndFrSld()), 2);

		numberValidation(mObject, "sldType", checkNullandTrim(stObj.getSldType()), 3);
		numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stObj.getScrndFrAsd()), 2);
		numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stObj.getScrndFrAdhd()), 2);
		numberValidation(mObject, "isEcActivity", checkNullandTrim(stObj.getIsEcActivity()), 2);
	
		numberValidation(mObject, "vocYn", checkNullandTrim(stObj.getVocYn()), 2);
		
//		System.out.println(checkNull(stObj.getSector()));
//		System.out.println(lowerSector.get(checkNull(stObj.getSector())));
//		System.out.println(lowerSector.get(1));
		try {
		if((checkNull(stObj.getClassId()).equalsIgnoreCase("9") || checkNull(stObj.getClassId()).equalsIgnoreCase("10")) && lowerSector.get(String.valueOf(checkNull(stObj.getSector())))) {
			blankAndTrueValidation(mObject, "sector", stObj.getSector());
		}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "sector", stObj.getSector());
			ex.printStackTrace();
		}
		
		try {
		if((checkNull(stObj.getClassId()).equalsIgnoreCase("11") || checkNull(stObj.getClassId()).equalsIgnoreCase("12")) && higherSector.get(checkNull(stObj.getSector()))) {
//			numberValidation(mObject, "sector", checkNullandTrim(stObj.getSector()), 2);
				blankAndTrueValidation(mObject, "sector", stObj.getSector());
			}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "sector", stObj.getSector());
			ex.printStackTrace();
		}
		
		try {
		if((checkNull(stObj.getClassId()).equalsIgnoreCase("9") || checkNull(stObj.getClassId()).equalsIgnoreCase("10")) && lowerSubSector.get(checkNull(stObj.getJobRole()))) {
//			numberValidation(mObject, "sector", checkNullandTrim(stObj.getSector()), 2);
				blankAndTrueValidation(mObject, "jobRole", stObj.getJobRole());
			}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
			ex.printStackTrace();
		}	
		
		try {
			if((checkNull(stObj.getClassId()).equalsIgnoreCase("11") || checkNull(stObj.getClassId()).equalsIgnoreCase("12")) && higherSubSector.get(checkNull(stObj.getJobRole()))) {
//				numberValidation(mObject, "sector", checkNullandTrim(stObj.getSector()), 2);
					blankAndTrueValidation(mObject, "jobRole", stObj.getJobRole());
				}
		}catch(Exception ex) {
			blankAndFalseValidation(mObject, "jobRole", stObj.getJobRole());
		}
//		numberValidation(mObject, "jobRole", checkNullandTrim(stObj.getJobRole()), 2);
		numberValidation(mObject, "appVocPy", checkNullandTrim(stObj.getAppVocPy()), 2);

		return mObject;

	}

	public Map<String, HashMap<String, String>> blankAndTrueValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);
		hs.put("status", "1");
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> blankAndFalseValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> hs = new HashMap<String, String>();
		HashMap<String, String> fs = new HashMap<String, String>();
		fs.put("status", "0");
		mp.put("finalStatus", fs);
		hs.put("value", value);
		hs.put("status", "0");
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> numberValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value, long valueRange) {

		HashMap<String, String> hs = new HashMap<String, String>();
		HashMap<String, String> fs = new HashMap<String, String>();
		hs.put("value", value);

		long originalValue = 0;

		if (value != "" && value != null && !value.equalsIgnoreCase("PP1") && !value.equalsIgnoreCase("PP2")
				&& !value.equalsIgnoreCase("PP3") && value.matches(numberRegex)) {
			originalValue = Long.parseLong(value);
		}

		if (value != null && value.matches(numberRegex) && value != "" && originalValue <= valueRange) {
			hs.put("status", "1");

			if (mapKey.equalsIgnoreCase("examMarksPy")) {
				if (Integer.parseInt(value) > 100) {
					hs.put("status", "0");
					hs.put("message", "invalid marks");
				}
			} else if (mapKey.equalsIgnoreCase("attendencePy")) {
				if (Integer.parseInt(value) > 365) {
					hs.put("status", "0");
					hs.put("message", "invalid attendence days");
				}
			}
		} else if (mapKey.equalsIgnoreCase("classId")
				&& (value.equalsIgnoreCase("PP1") || value.equalsIgnoreCase("PP2") || value.equalsIgnoreCase("PP3"))) {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "invalid number formate");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		mp.put(mapKey, hs);

		return mp;
	}

	public Map<String, HashMap<String, String>> stringValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);
		if (value != null && value.matches(alphanumericRegax) && value != "") {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "invalid string formate");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		mp.put(mapKey, hs);

		return mp;

	}

	public Map<String, HashMap<String, String>> jsonValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);
//		System.out.println("value----->"+value.toString());
//		if(jsonValidation.matches(value.toString())) {
		hs.put("status", "1");
//		}else {
//			hs.put("status", "0");
//			hs.put("message", "invalid number formate");
//		}
		mp.put(mapKey, hs);

		return mp;

	}

	public Map<String, HashMap<String, String>> alphabetValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> mobileValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);

		if (value != null && value.matches(mobileRegax) && value != "") {
			hs.put("status", "1");
		} else if (mapKey.equalsIgnoreCase("mobileNo_2") && (value == null || value == "")) {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid Mobile Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}

		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> emailValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();

//		System.out.println("email value--->" + value);
		hs.put("value", value);
		if (value != null && value != "") {
			hs.put("status", "1");
		}
		if (value.matches(emaiRegax)) {
			hs.put("status", "1");
		} else {
//			System.out.println("Email Status");
			hs.put("status", "0");
			hs.put("message", "Invalid Email Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> dateValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
//		System.out.println("Student DOB--->"+value);

		if (value != null && value.matches(dateRegax) && value != "") {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid Date Format dd/mm/yyyy");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> stringNonSpecialValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();

		if (value != null && value.matches(stringNonSpecialRegax) && value != "") {
			hs.put("status", "1");
		} else if (mapKey.equalsIgnoreCase("guardianName") && (value == null || value == "")) {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid String Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> pincodeValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
//		System.out.println(mapKey + "   picode validation---->" + value);
		if (value != null && value.matches(pinregex) && value != "") {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid Pincode Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);

		return mp;
	}

	public Map<String, HashMap<String, String>> adharValidation(Map<String, HashMap<String, String>> mp, String mapKey,
			String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);

//		System.out.println("value-->"+value);
//		System.out.println(value.matches(adharRegex));

		if (value != null && value.matches(adharRegex) && value != "") {
			hs.put("status", "1");
		} else if (value.equalsIgnoreCase("999999999999")) {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid Aadhar Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}

	public Map<String, HashMap<String, String>> admisionNumberValidation(Map<String, HashMap<String, String>> mp,
			String mapKey, String value) {
		HashMap<String, String> fs = new HashMap<String, String>();
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("value", value);

//		System.out.println("value-->"+value);
//		System.out.println(value.matches(adharRegex));

		if (value != null && value.matches(admisionnumericRegax) && value != "") {
			hs.put("status", "1");
		} else {
			hs.put("status", "0");
			hs.put("message", "Invalid Admision Format");
			fs.put("status", "0");
			mp.put("finalStatus", fs);
		}
		hs.put("value", value);
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
		if(classId.equalsIgnoreCase("PP1")) {
			return "-1";
		}else if(classId.equalsIgnoreCase("PP2")) {
			return "-2";
		}else if(classId.equalsIgnoreCase("PP3")) {
			return "-3";
		}else {
			return classId;
		}
	}

}
