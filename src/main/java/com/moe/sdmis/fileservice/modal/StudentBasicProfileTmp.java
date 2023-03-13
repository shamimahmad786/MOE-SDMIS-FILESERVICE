package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stu_pro_enr_details_tmp")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class),
    @TypeDef(name = "integer-array", typeClass = IntArrayType.class)
})
public class StudentBasicProfileTmp {

    @Id
    @Column(name = "temp_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stuProEnrDetailsTmpTempIdSeq")
    @SequenceGenerator(name = "stuProEnrDetailsTmpTempIdSeq", sequenceName = "stu_pro_enr_details_tmp_temp_id_seq", allocationSize = 500)
    private Long temp_id;
    
    @OneToOne(mappedBy = "studentBasicProfileTmp", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private StudentFacilityDetailsTmp studentFacilityDetailsTmp;
    
    @OneToOne(mappedBy = "studentBasicProfileTmp", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private StudentVocationalDetailsTmp studentVocationalDetailsTmp;
    
    @Column(name = "student_code_nat")
    private String studentCodeNat;
    
    @Column(name = "school_id")
    private Integer schoolId;
    
    @Column(name = "student_name")
    private String studentName;
    
    @Column(name = "gender")
    private Integer gender;
    
    @Column(name = "soc_cat_id")
    private Integer socCatId;
    
    @Column(name = "minority_id")
    private Integer minorityId;
    
    @Column(name = "is_bpl_yn")
    private Integer isBplYN;
    
    @Column(name = "aadhaar_no")
    private String aadhaarNo;
    
    @Column(name = "name_as_aadhaar")
    private String nameAsAadhaar;
    
    @Column(name = "student_dob")
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dob;
    
    @Column(name = "guardian_name")
    private String guardianName;
    
    @Column(name = "father_name")
    private String fatherName;
    
    @Column(name = "mother_name")
    private String motherName;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "pincode")
    private Integer pincode;
    
    @Column(name = "mobile_no_1")
    private String primaryMobile;
    
    @Column(name = "mobile_no_2")
    private String secondaryMobile;

    @Column(name = "aay_bpl_yn")
    private Integer aayBplYN;
    
    @Column(name = "ews_yn")
    private Integer ewsYN;
    
    @Column(name = "cwsn_yn")
    private Integer cwsnYN;
    
    @Column(name = "nat_ind_yn")
    private Integer natIndYN;
    
    
    
    @Column(name = "mother_tongue")
    private Integer motherTongue;
    
    @Column(name = "email_id")
    private String email;
    
//    @Column(name = "status")
//    private Integer status;
    
    @Column(name = "is_profile_active")
    private Integer isProfileActive;
    
    @Column(name = "inactive_reason")
    private Integer inactiveReason;
    
    
    
    @Column(name = "created_by")
    private String createdBy;
    
   
    @CreationTimestamp
    @Column(name = "created_time" , nullable = false, updatable = false)
    private Date createdTime;
    
    @Column(name = "pro_modified_by")
    private String modifiedBy;
    
    @Column(name = "pro_modified_time")
    private Date modifiedTime;
    
    @Column(name = "ac_year_id")
    private Integer acYearId;
    
    @Column(name = "class_id")
    private Integer classId;
    
    @Column(name = "section_id")
    private Integer sectionId;
    
    @Column(name = "roll_no")
    private Integer rollNo;
    
    @Column(name = "acdemic_stream")
    private Integer academicStream;
    
    @Column(name = "attendence_py")
    private Integer attendancePy;
    
    @Column(name = "enr_type_cy")
    private Integer enrTypeCy;
    
    @Column(name = "enrol_status")
    private Integer enrolStatus;
    
    @Column(name = "exam_appeared_py_yn")
    private Integer examAppearedPyYn;
    
    @Column(name = "exam_marks_py")
    private Integer examMarksPy;
    
    @Column(name = "exam_result_py")
    private Integer examResultPy;
    
    @Column(name = "is_bulk_uploaded")
    private Integer isBulkUploaded;
    
//    @Convert(converter = HashMapConverter.class)
//    @Type(type = "json")
//    @Column(name = "impairment_type", columnDefinition = "json")
    @Type(type = "integer-array")
    @Column(name = "impairment_type", columnDefinition = "integer[]")
    private Integer[] impairmentType;
    
    @Column(name = "oosc_mainstreamed_yn")
    private Integer ooscMainstreamedYn;
    
    @Column(name = "oosc_yn")
    private Integer ooscYn;
    
    @Column(name = "profile_status")
    private Integer profileStatus;
    
    @Column(name = "admn_number")
    private String admnNumber;
    
    @Column(name = "admn_start_date")
    private Date admnStartDate;
    
    @Column(name = "admn_end_date")
    private Date admnEndDate;
    
    @Column(name = "enr_status_py")
    private Integer enrStatusPY;
    
    @Column(name = "class_py")
    private Integer classPY;
    
    
    @Column(name = "enr_modified_by")
    private String enrModifiedBy;
    
    @Column(name = "enr_modified_time")
    private Date enrModifiedTime;
    
    @Column(name = "student_state_code")
    private String studentStateCode;

	public Long getTemp_id() {
		return temp_id;
	}

	public void setTemp_id(Long temp_id) {
		this.temp_id = temp_id;
	}

	public StudentFacilityDetailsTmp getStudentFacilityDetailsTmp() {
		return studentFacilityDetailsTmp;
	}

	public void setStudentFacilityDetailsTmp(StudentFacilityDetailsTmp studentFacilityDetailsTmp) {
		this.studentFacilityDetailsTmp = studentFacilityDetailsTmp;
	}

	public StudentVocationalDetailsTmp getStudentVocationalDetailsTmp() {
		return studentVocationalDetailsTmp;
	}

	public void setStudentVocationalDetailsTmp(StudentVocationalDetailsTmp studentVocationalDetailsTmp) {
		this.studentVocationalDetailsTmp = studentVocationalDetailsTmp;
	}

	public String getStudentCodeNat() {
		return studentCodeNat;
	}

	public void setStudentCodeNat(String studentCodeNat) {
		this.studentCodeNat = studentCodeNat;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getSocCatId() {
		return socCatId;
	}

	public void setSocCatId(Integer socCatId) {
		this.socCatId = socCatId;
	}

	public Integer getMinorityId() {
		return minorityId;
	}

	public void setMinorityId(Integer minorityId) {
		this.minorityId = minorityId;
	}

	public Integer getIsBplYN() {
		return isBplYN;
	}

	public void setIsBplYN(Integer isBplYN) {
		this.isBplYN = isBplYN;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGuardianName() {
		return guardianName;
	}

	public void setGuardianName(String guardianName) {
		this.guardianName = guardianName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getPrimaryMobile() {
		return primaryMobile;
	}

	public void setPrimaryMobile(String primaryMobile) {
		this.primaryMobile = primaryMobile;
	}

	public String getSecondaryMobile() {
		return secondaryMobile;
	}

	public void setSecondaryMobile(String secondaryMobile) {
		this.secondaryMobile = secondaryMobile;
	}

	public Integer getAayBplYN() {
		return aayBplYN;
	}

	public void setAayBplYN(Integer aayBplYN) {
		this.aayBplYN = aayBplYN;
	}

	public Integer getEwsYN() {
		return ewsYN;
	}

	public void setEwsYN(Integer ewsYN) {
		this.ewsYN = ewsYN;
	}

	public Integer getCwsnYN() {
		return cwsnYN;
	}

	public void setCwsnYN(Integer cwsnYN) {
		this.cwsnYN = cwsnYN;
	}

	public Integer getNatIndYN() {
		return natIndYN;
	}

	public void setNatIndYN(Integer natIndYN) {
		this.natIndYN = natIndYN;
	}

	public Integer getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(Integer motherTongue) {
		this.motherTongue = motherTongue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsProfileActive() {
		return isProfileActive;
	}

	public void setIsProfileActive(Integer isProfileActive) {
		this.isProfileActive = isProfileActive;
	}

	public Integer getInactiveReason() {
		return inactiveReason;
	}

	public void setInactiveReason(Integer inactiveReason) {
		this.inactiveReason = inactiveReason;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getAcYearId() {
		return acYearId;
	}

	public void setAcYearId(Integer acYearId) {
		this.acYearId = acYearId;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getRollNo() {
		return rollNo;
	}

	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}

	public Integer getAcademicStream() {
		return academicStream;
	}

	public void setAcademicStream(Integer academicStream) {
		this.academicStream = academicStream;
	}

	public Integer getAttendancePy() {
		return attendancePy;
	}

	public void setAttendancePy(Integer attendancePy) {
		this.attendancePy = attendancePy;
	}

	public Integer getEnrTypeCy() {
		return enrTypeCy;
	}

	public void setEnrTypeCy(Integer enrTypeCy) {
		this.enrTypeCy = enrTypeCy;
	}

	public Integer getEnrolStatus() {
		return enrolStatus;
	}

	public void setEnrolStatus(Integer enrolStatus) {
		this.enrolStatus = enrolStatus;
	}

	public Integer getExamAppearedPyYn() {
		return examAppearedPyYn;
	}

	public void setExamAppearedPyYn(Integer examAppearedPyYn) {
		this.examAppearedPyYn = examAppearedPyYn;
	}

	public Integer getExamMarksPy() {
		return examMarksPy;
	}

	public void setExamMarksPy(Integer examMarksPy) {
		this.examMarksPy = examMarksPy;
	}

	public Integer getExamResultPy() {
		return examResultPy;
	}

	public void setExamResultPy(Integer examResultPy) {
		this.examResultPy = examResultPy;
	}

	public Integer getIsBulkUploaded() {
		return isBulkUploaded;
	}

	public void setIsBulkUploaded(Integer isBulkUploaded) {
		this.isBulkUploaded = isBulkUploaded;
	}

	public Integer[] getImpairmentType() {
		return impairmentType;
	}

	public void setImpairmentType(Integer[] impairmentType) {
		this.impairmentType = impairmentType;
	}

	public Integer getOoscMainstreamedYn() {
		return ooscMainstreamedYn;
	}

	public void setOoscMainstreamedYn(Integer ooscMainstreamedYn) {
		this.ooscMainstreamedYn = ooscMainstreamedYn;
	}

	public Integer getOoscYn() {
		return ooscYn;
	}

	public void setOoscYn(Integer ooscYn) {
		this.ooscYn = ooscYn;
	}

	public Integer getProfileStatus() {
		return profileStatus;
	}

	public void setProfileStatus(Integer profileStatus) {
		this.profileStatus = profileStatus;
	}

	public String getAdmnNumber() {
		return admnNumber;
	}

	public void setAdmnNumber(String admnNumber) {
		this.admnNumber = admnNumber;
	}

	public Date getAdmnStartDate() {
		return admnStartDate;
	}

	public void setAdmnStartDate(Date admnStartDate) {
		this.admnStartDate = admnStartDate;
	}

	public Date getAdmnEndDate() {
		return admnEndDate;
	}

	public void setAdmnEndDate(Date admnEndDate) {
		this.admnEndDate = admnEndDate;
	}

	public Integer getEnrStatusPY() {
		return enrStatusPY;
	}

	public void setEnrStatusPY(Integer enrStatusPY) {
		this.enrStatusPY = enrStatusPY;
	}

	public Integer getClassPY() {
		return classPY;
	}

	public void setClassPY(Integer classPY) {
		this.classPY = classPY;
	}

	public String getEnrModifiedBy() {
		return enrModifiedBy;
	}

	public void setEnrModifiedBy(String enrModifiedBy) {
		this.enrModifiedBy = enrModifiedBy;
	}

	public Date getEnrModifiedTime() {
		return enrModifiedTime;
	}

	public void setEnrModifiedTime(Date enrModifiedTime) {
		this.enrModifiedTime = enrModifiedTime;
	}
    
	
	
//	@Column(name = "is_fac_prov")
//	private Integer isFacProv;
//	
//	@Column(name = "is_cwsn_fac_prov")
//	private Integer isCwsnFacProv;
    
    
    
}
