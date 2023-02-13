package com.moe.sdmis.fileservice.modal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name="MOE_SDMIS_UPLOADEXCELSTATUS", schema = "public")
public class UploadExcelStatus {

    @Id
	@Column(name="school_id")
	private Integer schoolId;
    @Column(name="udise_code")
    private String udiseCode;
    @Column(name="status")
	private String status;
	@Column(name="host")
	private String host;
	@Column(name="uploaded_by")
	private String uploadedBy;
	@Column(name="uploaded_date_time")
	private Date uploadedDateTime;
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getUdiseCode() {
		return udiseCode;
	}
	public void setUdiseCode(String udiseCode) {
		this.udiseCode = udiseCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Date getUploadedDateTime() {
		return uploadedDateTime;
	}
	public void setUploadedDateTime(Date uploadedDateTime) {
		this.uploadedDateTime = uploadedDateTime;
	}
	
	
	
}
