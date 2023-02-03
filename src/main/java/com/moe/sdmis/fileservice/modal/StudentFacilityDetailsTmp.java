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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stu_fac_othr_details_tmp")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class),
    @TypeDef(name = "integer-array", typeClass = IntArrayType.class)
})
public class StudentFacilityDetailsTmp {
	
	@Id
	@Column(name = "temp_id")
	private Long tempId;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "temp_id")
    private StudentBasicProfileTmp studentBasicProfileTmp;
	
	@Column(name = "school_id")
	private Integer schoolId;
	
//	@Type(type = "json")
//	@Column(name = "fac_provided", columnDefinition = "json")
	@Type(type = "integer-array")
    @Column(name = "fac_provided", columnDefinition = "integer[]")
	private Integer[] facProvided;
	
	@Column(name = "centrl_schlrshp_yn")
	private Integer centralScholarshipYn;
	
	@Column(name = "centrl_schlrshp_id")
	private Integer centralScholarshipId;
	
	@Column(name = "state_schlrshp_yn")
	private Integer stateScholarshipYn;
	
	@Column(name = "othr_schlrshp_yn")
	private Integer otherScholarshipYn;
	
	@Column(name = "schlrshp_amount")
	private Integer scholarshipAmount;
	
//	@Type(type = "json")
//	@Column(name = "fac_provided_cwsn", columnDefinition = "json")
	@Type(type = "integer-array")
    @Column(name = "fac_provided_cwsn", columnDefinition = "integer[]")
	private Integer[] facProvidedCwsn;
	
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
	
//	@Type(type = "json")
//	@Column(name = "gifted_cldrn", columnDefinition = "json")
	@Type(type = "integer-array")
    @Column(name = "gifted_cldrn", columnDefinition = "integer[]")
	private Integer[] giftedChildren;
	
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
	
	 @CreationTimestamp
	 @Column(name = "created_time" , nullable = false, updatable = false)
	private Date createdTime;
	
	@Column(name = "modified_by")
	private String modifiedBy;
	
	@Column(name = "modified_time")
	private Date modifiedTime;
	
	@Column(name = "is_fac_prov")
	private Integer isFacProv;
	
	@Column(name = "is_cwsn_fac_prov")
	private Integer isCwsnFacProv;

	@Override
	public String toString() {
		return "StudentFacilityDetails [studentId=" + tempId + ", studentBasicProfileTmp=" + studentBasicProfileTmp
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

