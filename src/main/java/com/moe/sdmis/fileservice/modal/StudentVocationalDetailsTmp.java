package com.moe.sdmis.fileservice.modal;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stu_voc_details_tmp")
public class StudentVocationalDetailsTmp {
	
	@Id
	@Column(name = "temp_id")
	private Long tempId;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "temp_id")
    private StudentBasicProfileTmp studentBasicProfileTmp;
	
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
	
	 @CreationTimestamp
	 @Column(name = "created_time" , nullable = false, updatable = false)
	private Date createdTime;
	
	@Column(name = "modified_by")
	private String modifiedBy;
	
	@Column(name = "modified_time")
	private Date modifiedTime;

}
