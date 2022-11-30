package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

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
@Table(name = "stu_pro_enr_details")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class)
})
public class StudentBasicProfile {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "studentIdSeq")
    @SequenceGenerator(name = "studentIdSeq", sequenceName = "student_id_seq", allocationSize = 1)
    private Long studentId;
    
    @OneToOne(mappedBy = "studentBasicProfile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StudentFacilityDetails studentFacilityDetails;
    
    @OneToOne(mappedBy = "studentBasicProfile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StudentVocationalDetails studentVocationalDetails;
    
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
    
    @Column(name = "created_time")
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
    
//    @Convert(converter = HashMapConverter.class)
    @Type(type = "json")
    @Column(name = "impairment_type", columnDefinition = "json")
    private Map<String, String> impairmentType;
    
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
    
    
    
}