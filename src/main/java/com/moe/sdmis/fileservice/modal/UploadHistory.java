package com.moe.sdmis.fileservice.modal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="MOE_SDMIS_EXCELUPLOADHISTORY", schema = "public")
public class UploadHistory {

@Id
//@GeneratedValue(strategy = GenerationType.AUTO)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exceluploadhistory")
@SequenceGenerator(name = "exceluploadhistory", sequenceName = "excel_upload_history_id_seq")
@Column(name="id")
private Long id;
@Column(name="schoolId")
private Integer schoolId;
@Column(name="host")
private String host;
@Column(name="uploadedBy")
private String uploadedBy;
@Column(name="status")
private String status;
//@Transient
@Temporal(TemporalType.TIMESTAMP)
@Column(name="uploadedTime",nullable = false)
private Date uploadedTime;


public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Integer getSchoolId() {
	return schoolId;
}
public void setSchoolId(Integer schoolId) {
	this.schoolId = schoolId;
}
public String getHost() {
	return host;
}
public void setHost(String host) {
	this.host = host;
}
public String getUploadedBy() {
	return uploadedBy;
}
public void setUploadedBy(String uploadedBy) {
	this.uploadedBy = uploadedBy;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public Date getUploadedTime() {
	return uploadedTime;
}
public void setUploadedTime(Date uploadedTime) {
	this.uploadedTime = uploadedTime;
}


}
