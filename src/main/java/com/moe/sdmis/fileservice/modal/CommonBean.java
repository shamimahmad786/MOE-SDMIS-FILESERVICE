package com.moe.sdmis.fileservice.modal;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonBean {
	private Long id;
	private String udisecode;
	private String studentName;
	private String classId;
	private String sectionId;
	private String gender;
	private String studentDob;
	private String motherName;
	private String fatherName;
	private String aadhaarNo;
	private String nameAsAadhaar;
	private String address;
	private String pincode;
	private String mobileNo_1;
	private String mobileNo_2;
	private String emailId;
	private String motherTongue;
	private String socCatId;
	private String minorityId;
	private String isBplYn;
	private String aayBplYn;
	private String ewsYn;
	private String cwsnYn;
	private String impairmentType;
	private String natIndYn;
	private String ooscYn;
	private String admnNumber;
	private String admnStartDate;
	private String acdemicStream;
	private String enrStatusPy;
	private String classPy;
	private String enrTypeCy;
	private String examAppearedPyYn;
	private String examResultPy;
	private String examMarksPy;
	private String attendencePy;
//	private String acYearId;
	private String rollNo;
	private String guardianName;
	
	private String uniformFacProvided;
	private String textBoxFacProvided;
	private String centrlSchlrshpYn;
	private String centrlSchlrshpId;
	private String stateSchlrshpYn;
	private String otherSchlrshpYn;
	private String schlrshpAmount;
	private String facProvidedCwsn;
	private String scrndFrSld;
	private String sldType;
	private String scrndFrAsd;
	private String scrndFrAdhd;
	private String isEcActivity;
	
	

}
