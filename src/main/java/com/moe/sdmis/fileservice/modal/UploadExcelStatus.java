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
	
}
