package com.moe.sdmis.fileservice.validation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.stereotype.Component;

import com.moe.sdmis.fileservice.modal.StudentTempTable;

@Component
public class CustomFxcelValidator {

//	String numberRegex ="^\\d+\\.\\d+$";
	String numberRegex ="^[0-9]{0,9}$";
	String alphanumericRegax= "^[a-zA-Z0-9]([\\w -,]*[a-zA-Z0-9])?$";
	String jsonValidation="\"([^\"]+)\":[\"]*([^,^\\}^\"]+)";
	String stringNonSpecialRegax="^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$";
	String mobileRegax="^[789]\\d{9}$";
	String emaiRegax="^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
	String adharRegex = "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$";   
	String dateRegax="(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])/(0[1-9]|1[0-2]|[1-9])/([0-9]{4})";
	String pinregex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"; 
	static Integer validationFlag=null;
	
	
	
	public Map<String, HashMap<String, String>>  validateStudent(StudentTempTable stObj){
		
		
//		System.out.println("Date of birth---->"+stObj.getStudentDob());
		
		Map<String, HashMap<String, String>> mObject=new LinkedHashMap<String,HashMap<String,String>>();
		
		numberValidation(mObject,"classId",checkNullandTrim(stObj.getClassId()));
		numberValidation(mObject,"sectionId",checkNullandTrim(stObj.getSectionId()));
		stringNonSpecialValidation(mObject,"studentName",checkNullandTrim(stObj.getStudentName()));
		numberValidation(mObject,"gender",checkNullandTrim(stObj.getGender()));
		dateValidation(mObject,"studentDob",checkNullandTrim(stObj.getStudentDob()));
		stringNonSpecialValidation(mObject,"motherName",checkNullandTrim(stObj.getMotherName()));
		stringNonSpecialValidation(mObject,"fatherName",checkNullandTrim(stObj.getFatherName()));
		adharValidation(mObject,"aadhaarNo",checkNullandTrim(stObj.getAadhaarNo()));
		stringNonSpecialValidation(mObject,"nameAsAadhaar",checkNullandTrim(stObj.getNameAsAadhaar()));
		stringValidation(mObject,"address",checkNullandTrim(stObj.getAddress()));
		pincodeValidation(mObject,"pincode",checkNullandTrim(stObj.getPincode()));
		mobileValidation(mObject,"mobileNo_1",checkNullandTrim(stObj.getMobileNo_1()));
		mobileValidation(mObject,"mobileNo_2",checkNullandTrim(stObj.getMobileNo_2()));
		emailValidation(mObject,"emailId",checkNullandTrim(stObj.getEmailId()));
		numberValidation(mObject,"motherTongue",checkNullandTrim(stObj.getMotherTongue()));
		numberValidation(mObject,"socCatId",checkNullandTrim(stObj.getSocCatId()));
		numberValidation(mObject,"minorityId",checkNullandTrim(stObj.getMinorityId()));
		numberValidation(mObject,"isBplYn",checkNullandTrim(stObj.getIsBplYn()));
		numberValidation(mObject,"ewsYn",checkNullandTrim(stObj.getEwsYn()));
		numberValidation(mObject,"cwsnYn",checkNullandTrim(stObj.getCwsnYn()));
		jsonValidation(mObject,"impairmentType",checkNullandTrim(stObj.getImpairmentType()));
		numberValidation(mObject,"natIndYn",checkNullandTrim(stObj.getNatIndYn()));
		numberValidation(mObject,"ooscYn",checkNullandTrim(stObj.getOoscYn()));
		stringValidation(mObject,"admnNumber",checkNullandTrim(stObj.getAdmnNumber()));
		dateValidation(mObject,"admnStartDate",checkNullandTrim(stObj.getAdmnStartDate()));
		numberValidation(mObject,"acdemicStream",checkNullandTrim(stObj.getAcdemicStream()));
		numberValidation(mObject,"enrStatusPy",checkNullandTrim(stObj.getEnrStatusPy()));
		numberValidation(mObject,"classPy",checkNullandTrim(stObj.getClassPy()));
		numberValidation(mObject,"enrTypeCy",checkNullandTrim(stObj.getEnrTypeCy()));
		numberValidation(mObject,"examAppearedPyYn",checkNullandTrim(stObj.getExamAppearedPyYn()));
		numberValidation(mObject,"examResultPy",checkNullandTrim(stObj.getExamResultPy()));
		numberValidation(mObject,"examMarksPy",checkNullandTrim(stObj.getExamMarksPy()));
		numberValidation(mObject,"attendencePy",checkNullandTrim(stObj.getAttendencePy()));
		numberValidation(mObject,"acYearId",checkNullandTrim(stObj.getAcYearId()));
		
		
		return mObject;
		
	}
	
	public Map<String, HashMap<String, String>> numberValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
//		value="5.0";
		
//		System.out.println("number value--->"+value);
		
		if(value !=null && value.matches(numberRegex)) {
			hs.put("status", "1");	
			
			if(mapKey.equalsIgnoreCase("examMarksPy")) {
				if(Integer.parseInt(value)>100) {
					hs.put("status", "0");
					hs.put("message", "invalid marks");
				}
			}else if(mapKey.equalsIgnoreCase("attendencePy")) {
				if(Integer.parseInt(value)>365) {
					hs.put("status", "0");
					hs.put("message", "invalid attendence days");
				}
			}
		}else {
			hs.put("status", "0");
			hs.put("message", "invalid number formate");
			mp.put("finalStatus", null);
		}
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> stringValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
		if(value !=null && value.matches(alphanumericRegax)) {
			hs.put("status", "1");	
		}else {
			hs.put("status", "0");
			hs.put("message", "invalid string formate");
			mp.put("finalStatus", null);
		}
		mp.put(mapKey, hs);
		
		return mp;
		
	}
	
	public Map<String, HashMap<String, String>> jsonValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
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
	
	
	public Map<String, HashMap<String, String>> alphabetValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> mobileValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
		
		if(value !=null && value.matches(mobileRegax)) {
			hs.put("status", "1");	
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid Mobile Format");
			mp.put("finalStatus", null);
		}
		
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> emailValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
		if(value !=null && value.matches(emaiRegax)) {
			hs.put("status", "1");
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid Email Format");
			mp.put("finalStatus", null);
		}
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> dateValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
//		System.out.println("Student DOB--->"+value);
		
		if(value !=null && value.matches(dateRegax)) {
			hs.put("status", "1");
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid Date Format dd/mm/yyyy");
			mp.put("finalStatus", null);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> stringNonSpecialValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		
		if(value !=null && value.matches(stringNonSpecialRegax)) {
			hs.put("status", "1");
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid String Format");
			mp.put("finalStatus", null);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}
	
	public Map<String, HashMap<String, String>> pincodeValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		System.out.println(mapKey+"   picode validation---->"+value);
		if(value !=null && value.matches(pinregex)) {
			hs.put("status", "1");
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid Pincode Format");
			mp.put("finalStatus", null);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
	
		
		return mp;
	}
	
	public Map<String, HashMap<String, String>> adharValidation(Map<String, HashMap<String, String>> mp,String mapKey, String value){
		HashMap<String,String> hs=new HashMap<String,String>();
		hs.put("value", value);
		
//		System.out.println("value-->"+value);
//		System.out.println(value.matches(adharRegex));
		
		if(value !=null && value.matches(adharRegex)) {
			hs.put("status", "1");
		}else {
			hs.put("status", "0");
			hs.put("message", "Invalid Aadhar Format");
			mp.put("finalStatus", null);
		}
		hs.put("value", value);
		mp.put(mapKey, hs);
		return mp;
	}
	
	
	public String checkNullandTrim(String value) {
		if(value !=null && value !="") {
			return value.trim();
		}
		return value;
	}
	
	
	
	
	
	
	
}
