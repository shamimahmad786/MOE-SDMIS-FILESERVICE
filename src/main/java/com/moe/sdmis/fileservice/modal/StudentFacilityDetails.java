package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stu_fac_othr_details")
public class StudentFacilityDetails {
	
	@Id
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "school_id")
	private Integer schoolId;
	
	@Type(type = "json")
	@Column(name = "fac_provided", columnDefinition = "json")
	private Map<Integer, Integer> facProvided;
	
	@Column(name = "centrl_schlrshp_yn")
	private Integer centralScholarshipYn;
	
	@Column(name = "centrl_schlrshp_id")
	private Integer centralScholarshipId;
	
	@Column(name = "state_schlrshp_yn")
	private Integer stateScholarshipYn;
	
	@Column(name = "other_schlrshp_yn")
	private Integer otherScholarshipYn;
	
	@Column(name = "schlrshp_amount")
	private Integer scholarshipAmount;
	
	@Type(type = "json")
	@Column(name = "fac_provided_cwsn", columnDefinition = "json")
	private Map<Integer, Integer> facProvidedCwsn;
	
	@Column(name = "scrnd_fr_sld")
	private Integer screenedForSld;
	
	@Column(name = "sld_type")
	private Integer sldType;
	
	@Column(name = "scrnd_fr_asd")
	private Integer screenedForAsd;
	
	@Column(name = "scrnd_fr_adhd")
	private Integer screenedForAdhd;
	
	@Column(name = "is_ec_activity")
	private Integer isEcActivity;
	
	@Type(type = "json")
	@Column(name = "gifted_cldrn", columnDefinition = "json")
	private Map<Integer, Integer> giftedChildren;
	
	@Column(name = "mentor_prov")
	private Integer mentorProvided;
	
	@Column(name = "nurturance_cmps_state")
	private Integer nurturanceCmpsState;
	
	@Column(name = "nurturance_cmps_national")
	private Integer nurturanceCmpsNational;
	
	@Column(name = "olympds_nlc")
	private Integer olympdsNlc;
	
	@Column(name = "ncc_nss_yn")
	private Integer nccNssYn;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "modified_by")
	private String modifiedBy;
	
	@Column(name = "modified_time")
	private Date modifiedTime;
	
	
	
	
	
	
}

