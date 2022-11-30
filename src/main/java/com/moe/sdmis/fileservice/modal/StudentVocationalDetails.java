package com.moe.sdmis.fileservice.modal;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stu_voc_details")
public class StudentVocationalDetails {
	
	@Id
	@Column(name = "student_id")
	private Long studentId;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private StudentBasicProfile studentBasicProfile;
	
	@Column(name = "school_id")
	private Integer schoolId;
	
	@Column(name = "voc_yn")
	private Integer vocExposure;
	
	@Column(name = "sector")
	private Integer sector;
	
	@Column(name = "job_role")
	private Integer jobRole;
	
	@Column(name = "app_voc_py")
	private Integer appVocPy;
	
	@Column(name = "marks_obt")
	private Integer marksObt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "modified_by")
	private String modifiedBy;
	
	@Column(name = "modified_time")
	private Date modifiedTime;

}
