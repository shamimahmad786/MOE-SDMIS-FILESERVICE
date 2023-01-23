package com.moe.sdmis.fileservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
//import com.moe.sdmis.fileservice.modal.StudentTempTable;

public interface StudentBasicProfileRepository extends JpaRepository<StudentBasicProfile, Long>{
	 @Modifying
	 @Transactional
	@Query(nativeQuery = true, value = "INSERT into stu_pro_enr_details (class_id,student_id,school_id,section_id,ac_year_id,student_name,gender,student_dob,mother_name,father_name,aadhaar_no,name_as_aadhaar,address,pincode,"
			+ " mobile_no_1,mobile_no_2,email_id,mother_tongue,soc_cat_id,minority_id,is_bpl_yn,ews_yn,cwsn_yn,impairment_type,nat_ind_yn,"
			+ " oosc_yn,admn_number,admn_start_date,acdemic_stream,enr_status_py,class_py,enr_type_cy,exam_appeared_py_yn,exam_result_py,"
			+ " exam_marks_py,attendence_py ) SELECT 	class_id,NEXTVAL('student_id_seq'),udisecode,section_id,ac_year_id,student_name,gender,student_dob,mother_name,father_name,aadhaar_no,name_as_aadhaar,address,pincode,"
            + "	mobile_no_1,mobile_no_2,email_id,mother_tongue,soc_cat_id,minority_id,is_bpl_yn,ews_yn,cwsn_yn,impairment_type,nat_ind_yn,"
            + "	oosc_yn,admn_number,admn_start_date,acdemic_stream,enr_status_py,class_py,enr_type_cy,exam_appeared_py_yn,exam_result_py,"
            + "	exam_marks_py,attendence_py from moe_sdmis_studenttempdetails WHERE udisecode = udisecode")
	
//	 WHERE :id > 1
	 void copyStudentDataFromTemp(Integer udisecode);
	 
	 @Modifying
	 @Transactional
	 void deleteAllBySchoolId(Integer schoolId);
	
}
