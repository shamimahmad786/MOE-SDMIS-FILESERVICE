package com.moe.sdmis.fileservice.modal;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;


//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
//import com.vladmihalcea.hibernate.type.json.JsonStringType;

//import com.fasterxml.jackson.core.io.JsonStringEncoder;

@Entity
@Table(name="MOE_SDMIS_STUDENTTEMPDETAILS", schema = "public")
//@TypeDef(name = "json", typeClass = JsonType.class)
//@TypeDef(name = "json", typeClass = JsonType.class)
//@TypeDef(name = "json", typeClass = JsonType.class)
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class)
})
public class StudentTempTable  implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	private String udisecode;
	@Column(name="student_name")
	private    String studentName;
	
	  @Column(name = "class_id")
	    private String classId;
	  
	  @Column(name = "section_id")
	    private String sectionId;
	
	@Column(name="gender")
	private    String gender;
	
	 @DateTimeFormat(pattern = "DD/MM/YYYY")
//	  @Temporal(TemporalType.DATE)
	@Column(name="student_dob")
	private    String studentDob;
	
	@Column(name="mother_name")
	private    String motherName;
	@Column(name="father_name")
	private    String fatherName;
//	/father_name
	@Column(name="aadhaar_no")
	private    String aadhaarNo;
	@Column(name="name_as_aadhaar")
	private    String nameAsAadhaar;
	@Column(name="address")
	private    String address;
	@Column(name="pincode")
	private    String pincode;
	@Column(name="mobile_no_1")
	private    String mobileNo_1;
	@Column(name="mobile_no_2")
	private    String mobileNo_2;
	@Column(name="email_id")
	private    String emailId;
	@Column(name="mother_tongue")
	private    String motherTongue;
	@Column(name="soc_cat_id")
	private    String socCatId;
	@Column(name="minority_id")
	private    String minorityId;
	@Column(name="is_bpl_yn")
	private    String isBplYn;
//	aay_bpl_yn;
	@Column(name="ews_yn")
	private    String ewsYn;
	@Column(name="cwsn_yn")
	private    String cwsnYn;
//	private    JSON impairment_type; 
	
	@Type(type = "json")
    @Column(name = "impairment_type",columnDefinition = "json")

//    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
	private String impairmentType;
	@Column(name="nat_ind_yn")
	private    String natIndYn;
	@Column(name="oosc_yn")
	private    String ooscYn;
	@Column(name="admn_number")
	private    String admnNumber;
	@Column(name="admn_start_date")
	private    String admnStartDate;
	@Column(name="acdemic_stream")
	private    String acdemicStream;
	@Column(name="enr_status_py")
	private    String enrStatusPy;
	@Column(name="class_py")
	private    String classPy;
	@Column(name="enr_type_cy")
	private    String enrTypeCy;
	@Column(name="exam_appeared_py_yn")
	private    String examAppearedPyYn;
	@Column(name="exam_result_py")
	private    String examResultPy;
	@Column(name="exam_marks_py")
	private    String examMarksPy;
	@Column(name="attendence_py")
	private    String attendencePy;
    @Column(name = "ac_year_id")
    private String acYearId;
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	
	
	
	@Override
	public String toString() {
		return "StudentTempTable [id=" + id + ", studentName=" + studentName + ", gender=" + gender + ", studentDob="
				+ studentDob + ", motherName=" + motherName + ", fatherName=" + fatherName + ", aadhaarNo=" + aadhaarNo
				+ ", nameAsAadhaar=" + nameAsAadhaar + ", address=" + address + ", pincode=" + pincode + ", mobileNo_1="
				+ mobileNo_1 + ", mobileNo_2=" + mobileNo_2 + ", emailId=" + emailId + ", motherTongue=" + motherTongue
				+ ", socCatId=" + socCatId + ", minorityId=" + minorityId + ", isBplYn=" + isBplYn + ", ewsYn=" + ewsYn
				+ ", cwsnYn=" + cwsnYn + ", impairmentType=" + impairmentType + ", natIndYn=" + natIndYn + ", ooscYn="
				+ ooscYn + ", admnNumber=" + admnNumber + ", admnStartDate=" + admnStartDate + ", acdemicStream="
				+ acdemicStream + ", enrStatusPy=" + enrStatusPy + ", classPy=" + classPy + ", enrTypeCy=" + enrTypeCy
				+ ", examAppearedPyYn=" + examAppearedPyYn + ", examResultPy=" + examResultPy + ", examMarksPy="
				+ examMarksPy + ", attendencePy=" + attendencePy + "]";
	}
	public String getUdisecode() {
		return udisecode;
	}
	public void setUdisecode(String udisecode) {
		this.udisecode = udisecode;
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
	public String getAcYearId() {
		return acYearId;
	}
	public void setAcYearId(String acYearId) {
		this.acYearId = acYearId;
	}

	
	
	
	
	
	
	
}
