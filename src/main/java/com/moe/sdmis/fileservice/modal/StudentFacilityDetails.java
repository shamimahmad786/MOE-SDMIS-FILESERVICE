package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private StudentBasicProfile studentBasicProfile;
	
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

	@Override
	public String toString() {
		return "StudentFacilityDetails [studentId=" + studentId + ", studentBasicProfile=" + studentBasicProfile
				+ ", schoolId=" + schoolId + ", facProvided=" + facProvided + ", centralScholarshipYn="
				+ centralScholarshipYn + ", centralScholarshipId=" + centralScholarshipId + ", stateScholarshipYn="
				+ stateScholarshipYn + ", otherScholarshipYn=" + otherScholarshipYn + ", scholarshipAmount="
				+ scholarshipAmount + ", facProvidedCwsn=" + facProvidedCwsn + ", screenedForSld=" + screenedForSld
				+ ", sldType=" + sldType + ", screenedForAsd=" + screenedForAsd + ", screenedForAdhd=" + screenedForAdhd
				+ ", isEcActivity=" + isEcActivity + ", giftedChildren=" + giftedChildren + ", mentorProvided="
				+ mentorProvided + ", nurturanceCmpsState=" + nurturanceCmpsState + ", nurturanceCmpsNational="
				+ nurturanceCmpsNational + ", olympdsNlc=" + olympdsNlc + ", nccNssYn=" + nccNssYn + ", createdBy="
				+ createdBy + ", createdTime=" + createdTime + ", modifiedBy=" + modifiedBy + ", modifiedTime="
				+ modifiedTime + "]";
	}
	
	
	
	
	
	
}

