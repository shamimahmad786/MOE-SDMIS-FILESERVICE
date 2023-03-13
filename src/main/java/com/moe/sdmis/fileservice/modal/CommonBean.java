package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonBean {
	private Long id;
	private String udisecode;
	private String studentName;
	private String classId;
	private String sectionId;
	private String gender;
	private String studentDob;
	private String motherName;
	private String fatherName;
	private String aadhaarNo;
	private String nameAsAadhaar;
	private String address;
	private String pincode;
	private String mobileNo_1;
	private String mobileNo_2;
	private String emailId;
	private String motherTongue;
	private String socCatId;
	private String minorityId;
	private String isBplYn;
	private String aayBplYn;
	private String ewsYn;
	private String cwsnYn;
	private String impairmentType;
	private String natIndYn;
	private String ooscYn;
	private String ooscMainstreamedYn;
	private String admnNumber;
	private String admnStartDate;
	private String acdemicStream;
	private String enrStatusPy;
	private String classPy;
	private String enrTypeCy;
	private String examAppearedPyYn;
	private String examResultPy;
	private String examMarksPy;
	private String attendencePy;
//	private String acYearId;
	private String rollNo;
	private String guardianName;
	
	private String uniformFacProvided;
	private String textBoxFacProvided;
	private String centrlSchlrshpYn;
	private String centrlSchlrshpId;
	private String stateSchlrshpYn;
	private String otherSchlrshpYn;
	private String schlrshpAmount;
	private String facProvidedCwsn;
	private String scrndFrSld;
	private String sldType;
	private String scrndFrAsd;
	private String scrndFrAdhd;
	private String isEcActivity;
	
	private String vocYn;
	private String sector;
	private String jobRole;
	private String appVocPy;
	private String studentStateCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUdisecode() {
		return udisecode;
	}
	public void setUdisecode(String udisecode) {
		this.udisecode = udisecode;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStudentDob() {
		return studentDob;
	}
	public void setStudentDob(String studentDob) {
		this.studentDob = studentDob;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getAadhaarNo() {
		return aadhaarNo;
	}
	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}
	public String getNameAsAadhaar() {
		return nameAsAadhaar;
	}
	public void setNameAsAadhaar(String nameAsAadhaar) {
		this.nameAsAadhaar = nameAsAadhaar;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getMobileNo_1() {
		return mobileNo_1;
	}
	public void setMobileNo_1(String mobileNo_1) {
		this.mobileNo_1 = mobileNo_1;
	}
	public String getMobileNo_2() {
		return mobileNo_2;
	}
	public void setMobileNo_2(String mobileNo_2) {
		this.mobileNo_2 = mobileNo_2;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMotherTongue() {
		return motherTongue;
	}
	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}
	public String getSocCatId() {
		return socCatId;
	}
	public void setSocCatId(String socCatId) {
		this.socCatId = socCatId;
	}
	public String getMinorityId() {
		return minorityId;
	}
	public void setMinorityId(String minorityId) {
		this.minorityId = minorityId;
	}
	public String getIsBplYn() {
		return isBplYn;
	}
	public void setIsBplYn(String isBplYn) {
		this.isBplYn = isBplYn;
	}
	public String getAayBplYn() {
		return aayBplYn;
	}
	public void setAayBplYn(String aayBplYn) {
		this.aayBplYn = aayBplYn;
	}
	public String getEwsYn() {
		return ewsYn;
	}
	public void setEwsYn(String ewsYn) {
		this.ewsYn = ewsYn;
	}
	public String getCwsnYn() {
		return cwsnYn;
	}
	public void setCwsnYn(String cwsnYn) {
		this.cwsnYn = cwsnYn;
	}
	public String getImpairmentType() {
		return impairmentType;
	}
	public void setImpairmentType(String impairmentType) {
		this.impairmentType = impairmentType;
	}
	public String getNatIndYn() {
		return natIndYn;
	}
	public void setNatIndYn(String natIndYn) {
		this.natIndYn = natIndYn;
	}
	public String getOoscYn() {
		return ooscYn;
	}
	public void setOoscYn(String ooscYn) {
		this.ooscYn = ooscYn;
	}
	public String getOoscMainstreamedYn() {
		return ooscMainstreamedYn;
	}
	public void setOoscMainstreamedYn(String ooscMainstreamedYn) {
		this.ooscMainstreamedYn = ooscMainstreamedYn;
	}
	public String getAdmnNumber() {
		return admnNumber;
	}
	public void setAdmnNumber(String admnNumber) {
		this.admnNumber = admnNumber;
	}
	public String getAdmnStartDate() {
		return admnStartDate;
	}
	public void setAdmnStartDate(String admnStartDate) {
		this.admnStartDate = admnStartDate;
	}
	public String getAcdemicStream() {
		return acdemicStream;
	}
	public void setAcdemicStream(String acdemicStream) {
		this.acdemicStream = acdemicStream;
	}
	public String getEnrStatusPy() {
		return enrStatusPy;
	}
	public void setEnrStatusPy(String enrStatusPy) {
		this.enrStatusPy = enrStatusPy;
	}
	public String getClassPy() {
		return classPy;
	}
	public void setClassPy(String classPy) {
		this.classPy = classPy;
	}
	public String getEnrTypeCy() {
		return enrTypeCy;
	}
	public void setEnrTypeCy(String enrTypeCy) {
		this.enrTypeCy = enrTypeCy;
	}
	public String getExamAppearedPyYn() {
		return examAppearedPyYn;
	}
	public void setExamAppearedPyYn(String examAppearedPyYn) {
		this.examAppearedPyYn = examAppearedPyYn;
	}
	public String getExamResultPy() {
		return examResultPy;
	}
	public void setExamResultPy(String examResultPy) {
		this.examResultPy = examResultPy;
	}
	public String getExamMarksPy() {
		return examMarksPy;
	}
	public void setExamMarksPy(String examMarksPy) {
		this.examMarksPy = examMarksPy;
	}
	public String getAttendencePy() {
		return attendencePy;
	}
	public void setAttendencePy(String attendencePy) {
		this.attendencePy = attendencePy;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getGuardianName() {
		return guardianName;
	}
	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}
	public String getUniformFacProvided() {
		return uniformFacProvided;
	}
	public void setUniformFacProvided(String uniformFacProvided) {
		this.uniformFacProvided = uniformFacProvided;
	}
	public String getTextBoxFacProvided() {
		return textBoxFacProvided;
	}
	public void setTextBoxFacProvided(String textBoxFacProvided) {
		this.textBoxFacProvided = textBoxFacProvided;
	}
	public String getCentrlSchlrshpYn() {
		return centrlSchlrshpYn;
	}
	public void setCentrlSchlrshpYn(String centrlSchlrshpYn) {
		this.centrlSchlrshpYn = centrlSchlrshpYn;
	}
	public String getCentrlSchlrshpId() {
		return centrlSchlrshpId;
	}
	public void setCentrlSchlrshpId(String centrlSchlrshpId) {
		this.centrlSchlrshpId = centrlSchlrshpId;
	}
	public String getStateSchlrshpYn() {
		return stateSchlrshpYn;
	}
	public void setStateSchlrshpYn(String stateSchlrshpYn) {
		this.stateSchlrshpYn = stateSchlrshpYn;
	}
	public String getOtherSchlrshpYn() {
		return otherSchlrshpYn;
	}
	public void setOtherSchlrshpYn(String otherSchlrshpYn) {
		this.otherSchlrshpYn = otherSchlrshpYn;
	}
	public String getSchlrshpAmount() {
		return schlrshpAmount;
	}
	public void setSchlrshpAmount(String schlrshpAmount) {
		this.schlrshpAmount = schlrshpAmount;
	}
	public String getFacProvidedCwsn() {
		return facProvidedCwsn;
	}
	public void setFacProvidedCwsn(String facProvidedCwsn) {
		this.facProvidedCwsn = facProvidedCwsn;
	}
	public String getScrndFrSld() {
		return scrndFrSld;
	}
	public void setScrndFrSld(String scrndFrSld) {
		this.scrndFrSld = scrndFrSld;
	}
	public String getSldType() {
		return sldType;
	}
	public void setSldType(String sldType) {
		this.sldType = sldType;
	}
	public String getScrndFrAsd() {
		return scrndFrAsd;
	}
	public void setScrndFrAsd(String scrndFrAsd) {
		this.scrndFrAsd = scrndFrAsd;
	}
	public String getScrndFrAdhd() {
		return scrndFrAdhd;
	}
	public void setScrndFrAdhd(String scrndFrAdhd) {
		this.scrndFrAdhd = scrndFrAdhd;
	}
	public String getIsEcActivity() {
		return isEcActivity;
	}
	public void setIsEcActivity(String isEcActivity) {
		this.isEcActivity = isEcActivity;
	}
	public String getVocYn() {
		return vocYn;
	}
	public void setVocYn(String vocYn) {
		this.vocYn = vocYn;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getJobRole() {
		return jobRole;
	}
	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}
	public String getAppVocPy() {
		return appVocPy;
	}
	public void setAppVocPy(String appVocPy) {
		this.appVocPy = appVocPy;
	}
	
	
	
	

}
