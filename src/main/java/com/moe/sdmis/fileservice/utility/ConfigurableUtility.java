package com.moe.sdmis.fileservice.utility;

import java.util.Arrays;
import java.util.List;

public class ConfigurableUtility {

public static	List<String> commomHeadersFromExcel = Arrays.asList("UDISE Code of School","Student State Code","Grade/Class","SECTION","Roll no","Name of Student","GENDER","DOB (DD-MM-YYYY)",
			"MOTHER NAME","FATHER Name","GUARDIAN'S NAME","Aadhaar No of Child :","NAME AS PER AADHAR","ADDRESS",
			"PINCODE","MOBILE NUMBER","ALTERNATE MOBILE NUMBER","EMAIL ID (STUDENT/PARENT/GUARDIAN)","MOTHER TONGUE",
			"SOCIAL CATEGORY","MINORITY GROUP","BPL BANEFICIARY","ANTYODAYA","BELONGS TO EWS/DISADVANTAGED GROUP","CWSN(YES/NO)",
			"IMPAIRMENT TYPE","CHILD IS INDIAN NATIONAL","CHILD IS OUT-OF-SCHOOL-CHILD","Admission Number","ADMISSION DATE (DD-MM-YYYY)",
			"STUDENT STREAM (For higher secondary only)","PREVIOUS ACADEMIC SCHOOLING STATUS","CLASS STUDIES IN PREVIOUS ACADEMIC","ADMITED/ ENROLLED UNDER",
			"APPEARED FOR EXAM IN PREVIOUS CLASS","RESULT FOR PREVIOUS EXAM","MARKS % OF PREVIOUS EXAM","CLASS ATTENDED DAYS","Facility - Free Uniform",
			"Facility - Free TextBook","Central Scholarship","Name of Scholarship (provide code only)","State Scholarship","Other Scholarship",
			"Scholarship Amount","Facility provided to the CWSN","Specific Learning Disability (SLD)","Type of Specific Learning Disability (SLD)",
			"Autism Spectrum Disorder","Attention Deficit Hyperactive Disorder (ADHD)","extra curricular activity","");
  public static Integer commonPhysicalColumn=52;

public static	List<String> commomHeadersFromValidation = Arrays.asList("numberValidation","numberValidation","nonValidation", "numberValidation","numberValidation","numberValidation","numberValidation","stringNonSpecialValidation","numberValidation","stringNonSpecialValidation","dateValidation",
"stringNonSpecialValidation","stringNonSpecialValidation","stringNonSpecialValidation","adharValidation","stringNonSpecialValidation","stringValidation",
"pincodeValidation","mobileValidation","mobileValidation","emailValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","numberValidation","numberValidation","numberValidation",
"jsonValidation","numberValidation","numberValidation","numberValidation","dateValidation",
"numberValidation","numberValidation","numberValidation",  
"numberValidation", "numberValidation","numberValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","numberValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","nonValidation");





//public static  List<String> commonHeaderKey=Arrays.asList("udiseCode","classId","sectionId","studentName","gender","studentDob","motherName","fatherName","aadhaarNo","nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue","socCatId","minorityId","isBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream",
//		"enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","acYearId","rollNo","aayBplYn","guardianName","textBoxFacProvided",
//		"centrlSchlrshpYn","centrlSchlrshpId","stateSchlrshpYn","otherSchlrshpYn","schlrshpAmount","facProvidedCwsn","scrndFrSld","sldType","scrndFrAsd","scrndFrAdhd","isEcActivity");

public static  List<String> commonHeaderKey=Arrays.asList("udiseCode","classId","sectionId","rollNo","studentName","gender","studentDob","motherName","fatherName","guardianName","aadhaarNo","nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue","socCatId","minorityId","isBplYn","aayBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream",
		"enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","textBoxFacProvided",
		"centrlSchlrshpYn","centrlSchlrshpId","stateSchlrshpYn","otherSchlrshpYn","schlrshpAmount","facProvidedCwsn","scrndFrSld","sldType","scrndFrAsd","scrndFrAdhd","isEcActivity");



public static	List<String> state116CommomHeadersFromExcel = Arrays.asList("UDISE Code of School","Student State Code","Grade/Class","SECTION","Roll no","Name of Student","GENDER","DOB (DD-MM-YYYY)",
		"MOTHER NAME","FATHER Name","GUARDIAN'S NAME","Aadhaar No of Child :","NAME AS PER AADHAR","ADDRESS",
		"PINCODE","MOBILE NUMBER","ALTERNATE MOBILE NUMBER","EMAIL ID (STUDENT/PARENT/GUARDIAN)","MOTHER TONGUE",
		"SOCIAL CATEGORY","MINORITY GROUP","BPL BANEFICIARY","ANTYODAYA","BELONGS TO EWS/DISADVANTAGED GROUP","CWSN(YES/NO)",
		"IMPAIRMENT TYPE","CHILD IS INDIAN NATIONAL","CHILD IS OUT-OF-SCHOOL-CHILD","Admission Number","ADMISSION DATE (DD-MM-YYYY)",
		"STUDENT STREAM (For higher secondary only)","PREVIOUS ACADEMIC SCHOOLING STATUS","CLASS STUDIES IN PREVIOUS ACADEMIC","ADMITED/ ENROLLED UNDER",
		"APPEARED FOR EXAM IN PREVIOUS CLASS","RESULT FOR PREVIOUS EXAM","MARKS % OF PREVIOUS EXAM","CLASS ATTENDED DAYS");
public static Integer state116commonPhysicalColumn=38;

public static	List<String> state116HeadersFromValidation = Arrays.asList("numberValidation","numberValidation","nonValidation","numberValidation","numberValidation","numberValidation","stringNonSpecialValidation","numberValidation","dateValidation",
"stringNonSpecialValidation","stringNonSpecialValidation","stringNonSpecialValidation","adharValidation","stringNonSpecialValidation","stringValidation",
"pincodeValidation","mobileValidation","mobileValidation","emailValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","numberValidation","numberValidation","numberValidation",
"jsonValidation","numberValidation","numberValidation","numberValidation","dateValidation",
"numberValidation","numberValidation","numberValidation","numberValidation",
"numberValidation","numberValidation","numberValidation","numberValidation");

//public static List<String> state116HeaderKey=Arrays.asList("udiseCode","classId","sectionId","studentName","gender","studentDob","motherName","fatherName","aadhaarNo","nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue","socCatId","minorityId","isBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream","enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","acYearId","rollNo","aayBplYn","guardianName");
public static List<String> state116HeaderKey=Arrays.asList("udiseCode","classId","sectionId","studentName","gender","studentDob","motherName","fatherName","aadhaarNo","nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue","socCatId","minorityId","isBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream","enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","rollNo","aayBplYn","guardianName");



public static	List<String> templateHeadersFromExcel_1 =Arrays.asList("Grade/ Class","SECTION","Roll no","Name of Student","GENDER","DOB (DD-MM-YYYY)","MOTHER NAME","FATHER Name","GUARDIAN NAME","Aadhaar No of Child :"
,"NAME AS PER AADHAR","ADDRESS","PINCODE","MOBILE NUMBER","ALTERNATE MOBILE NUMBER","EMAIL ID (STUDENT/PARENT/GUARDIAN)","MOTHER TONGUE",
"SOCIAL CATEGORY","MINORITY GROUP","BPL BANEFICIARY","ANTYODAYA","BELONGS TO EWS/DISADVANTAGED GROUP","CWSN(YES/NO)","IMPAIRMENT TYPE","CHILD IS INDIAN NATIONAL"
,"CHILD IS OUT-OF-SCHOOL-CHILD","Admission Number","ADMISSION DATE (DD-MM-YYYY)","STUDENT STREAM (For higher secondary only)","PREVIOUS ACADEMIC SCHOOLING STATUS"
,"CLASS STUDIES IN PREVIOUS ACADEMIC","ADMITED/ ENROLLED UNDER","APPEARED FOR EXAM IN PREVIOUS CLASS","RESULT FOR PREVIOUS EXAM","MARKS % OF PREVIOUS EXAM"	
,"CLASS ATTENDED DAYS","Facility - Free Uniform","Facility - Free TextBook","Central Scholarship","Name of Scholarship (provide code only)"
,"State Scholarship","Other Scholarship","Scholarship Amount","Facility provided to the CWSN","Specific Learning Disability (SLD)",
"Type of Specific Learning Disability (SLD)","Autism Spectrum Disorder","Attention Deficit Hyperactive Disorder (ADHD)","extra curricular activity"	
,"Vocational Courses (Yes-1, No-2)","Trade/Sector ID","Job Role ID","Appeared for Examination in Previous Class for Vocational Subject");


public static Integer commonPhysicalColumn_1=53;

public static  List<String> commonHeaderKey_1=Arrays.asList("classId","sectionId","rollNo","studentName","gender","studentDob","motherName","fatherName","guardianName","aadhaarNo",
		"nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue",
		"socCatId","minorityId","isBplYn","aayBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream",
		"enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","uniformFacProvided","textBoxFacProvided",
		"centrlSchlrshpYn","centrlSchlrshpId","stateSchlrshpYn","otherSchlrshpYn","schlrshpAmount","facProvidedCwsn","scrndFrSld","sldType","scrndFrAsd","scrndFrAdhd","isEcActivity","vocYn","sector","jobRole","appVocPy");



public static	List<String> templateHeadersFromExcel_2 =Arrays.asList("Grade/ Class","SECTION","Roll no","Name of Student","GENDER","DOB (DD-MM-YYYY)","MOTHER NAME","FATHER Name","GUARDIAN NAME","Aadhaar No of Child :"
,"NAME AS PER AADHAR","ADDRESS","PINCODE","MOBILE NUMBER","ALTERNATE MOBILE NUMBER","EMAIL ID (STUDENT/PARENT/GUARDIAN)","MOTHER TONGUE",
"SOCIAL CATEGORY","MINORITY GROUP","BPL BANEFICIARY","ANTYODAYA","BELONGS TO EWS/DISADVANTAGED GROUP","CWSN(YES/NO)","IMPAIRMENT TYPE","CHILD IS INDIAN NATIONAL"
,"CHILD IS OUT-OF-SCHOOL-CHILD","Admission Number","ADMISSION DATE (DD-MM-YYYY)","STUDENT STREAM (For higher secondary only)","PREVIOUS ACADEMIC SCHOOLING STATUS"
,"CLASS STUDIES IN PREVIOUS ACADEMIC","ADMITED/ ENROLLED UNDER","APPEARED FOR EXAM IN PREVIOUS CLASS","RESULT FOR PREVIOUS EXAM","MARKS % OF PREVIOUS EXAM"	
,"CLASS ATTENDED DAYS","Facility - Free Uniform","Facility - Free TextBook","Central Scholarship","Name of Scholarship (provide code only)"
,"State Scholarship","Other Scholarship","Scholarship Amount","Facility provided to the CWSN","Specific Learning Disability (SLD)",
"Type of Specific Learning Disability (SLD)","Autism Spectrum Disorder","Attention Deficit Hyperactive Disorder (ADHD)","extra curricular activity");


public static Integer commonPhysicalColumn_2=49;

public static  List<String> commonHeaderKey_2=Arrays.asList("classId","sectionId","rollNo","studentName","gender","studentDob","motherName","fatherName","guardianName","aadhaarNo",
		"nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue",
		"socCatId","minorityId","isBplYn","aayBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","admnNumber","admnStartDate","acdemicStream",
		"enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","uniformFacProvided","textBoxFacProvided",
		"centrlSchlrshpYn","centrlSchlrshpId","stateSchlrshpYn","otherSchlrshpYn","schlrshpAmount","facProvidedCwsn","scrndFrSld","sldType","scrndFrAsd","scrndFrAdhd","isEcActivity");


public static	List<String> templateHeadersFromExcel_3 =Arrays.asList("Grade/Class","SECTION","Rollno","NameofStudent","GENDER","DOB(DD-MM-YYYY)","MOTHERNAME","FATHERNAME","GUARDIANNAME(Optional)","AadhaarNoofChild","STUDENTNAMEASPERAADHAR",
		"PRESENTADDRESS","PINCODE","MOBILENUMBER(ofStudent/Parent/Guardian)","ALTERNATEMOBILENUMBER(Optional)","EMAILID(STUDENT/PARENT/GUARDIAN)(Optional)","MOTHERTONGUE",
		"SOCIALCATEGORY","MINORITYGROUP","IsBPLBANEFICIARY?(Yes-1,No-2)","WhetherAntyodayaAnnaYojana(AAY)beneficiary?(Yes-1,No-2)","BELONGSTOEWS/DISADVANTAGEDGROUP?(Yes-1,No-2)",
		"ISCWSN?(Yes-1,No-2)","CWSNIMPAIRMENTTYPE","CHILDISINDIANNATIONAL?(Yes-1,No-2)","CHILDISOUT-OF-SCHOOL-CHILD?(Yes-1,No-2)","Whenthechildismainstreamed?","AdmissionNumber",
		"ADMISSIONDATE(DD-MM-YYYY)","STUDENTSTREAM(Forhighersecondaryonly)","PREVIOUSACADEMICYEARSCHOOLINGSTATUS","CLASSSTUDIESINPREVIOUSACADEMICYEAR","ADMITED/ENROLLEDUNDERRTE/EWS?(forprivateunaidedonly)",
		"APPEAREDFOREXAMINPREVIOUSCLASS(1-Appeared,2-NotAppeared)","RESULTFORPREVIOUSEXAM","MARKS%OFPREVIOUSEXAM","CLASSATTENDEDDAYS(PreviousYear)","FacilityReceivedFreeUniform(Yes-1,No-2)",	
		"FacilityReceivedFreeTextBook(Yes-1,No-2)","ReceivedCentralScholarship(Yes-1,No-2)","NameofCentralScholarship(providecodeonly)","ReceivedStateScholarship(Yes-1,No-2)",
		"ReceivedOtherScholarship(Yes-1,No-2)","ScholarshipAmount","FacilityprovidedtotheCWSN","SpecificLearningDisability(SLD)(Yes-1,No-2)","TypeofSpecificLearningDisability(SLD)",
		"AutismSpectrumDisorder(Yes-1,No-2)","AttentionDeficitHyperactiveDisorder(ADHD)(Yes-1,No-2)","Involvedinextracurricularactivity?(Yes-1,No-2)","VocationalCourses(Yes-1,No-2)",
		"Trade/SectorID","JobRoleID","AppearedforExaminationinPreviousClassforVocationalSubject");


public static Integer commonPhysicalColumn_3=54;

public static	List<String> templateHeadersFromExcel_3A =Arrays.asList("Grade/Class","SECTION","Rollno","NameofStudent","GENDER","DOB(DD-MM-YYYY)","MOTHERNAME","FATHERNAME","GUARDIANNAME(Optional)","AadhaarNoofChild","STUDENTNAMEASPERAADHAR",
		"PRESENTADDRESS","PINCODE","MOBILENUMBER(ofStudent/Parent/Guardian)","ALTERNATEMOBILENUMBER(Optional)","EMAILID(STUDENT/PARENT/GUARDIAN)(Optional)","MOTHERTONGUE",
		"SOCIALCATEGORY","MINORITYGROUP","IsBPLBANEFICIARY?(Yes-1,No-2)","WhetherAntyodayaAnnaYojana(AAY)beneficiary?(Yes-1,No-2)","BELONGSTOEWS/DISADVANTAGEDGROUP?(Yes-1,No-2)",
		"ISCWSN?(Yes-1,No-2)","CWSNIMPAIRMENTTYPE","CHILDISINDIANNATIONAL?(Yes-1,No-2)","CHILDISOUT-OF-SCHOOL-CHILD?(Yes-1,No-2)","Whenthechildismainstreamed?","AdmissionNumber",
		"ADMISSIONDATE(DD-MM-YYYY)","STUDENTSTREAM(Forhighersecondaryonly)","PREVIOUSACADEMICYEARSCHOOLINGSTATUS","CLASSSTUDIESINPREVIOUSACADEMICYEAR","ADMITED/ENROLLEDUNDERRTE/EWS?(forprivateunaidedonly)",
		"APPEAREDFOREXAMINPREVIOUSCLASS(1-Appeared,2-NotAppeared)","RESULTFORPREVIOUSEXAM","MARKS%OFPREVIOUSEXAM","CLASSATTENDEDDAYS(PreviousYear)","FacilityReceivedFreeUniform(Yes-1,No-2)",	
		"FacilityReceivedFreeTextBook(Yes-1,No-2)","ReceivedCentralScholarship(Yes-1,No-2)","NameofCentralScholarship(providecodeonly)","ReceivedStateScholarship(Yes-1,No-2)",
		"ReceivedOtherScholarship(Yes-1,No-2)","ScholarshipAmount","FacilityprovidedtotheCWSN","SpecificLearningDisability(SLD)(Yes-1,No-2)","TypeofSpecificLearningDisability(SLD)",
		"AutismSpectrumDisorder(Yes-1,No-2)","AttentionDeficitHyperactiveDisorder(ADHD)(Yes-1,No-2)","Involvedinextracurricularactivity?(Yes-1,No-2)","VocationalCourses(Yes-1,No-2)",
		"Trade/SectorID","JobRoleID","AppearedforExaminationinPreviousClassforVocationalSubject","StudentStateCode");




public static  List<String> commonHeaderKey_3=Arrays.asList("classId","sectionId","rollNo","studentName","gender","studentDob","motherName","fatherName","guardianName","aadhaarNo",
		"nameAsAadhaar","address","pincode","mobileNo_1","mobileNo_2","emailId","motherTongue",
		"socCatId","minorityId","isBplYn","aayBplYn","ewsYn","cwsnYn","impairmentType","natIndYn","ooscYn","ooscMainstreamedYn","admnNumber","admnStartDate","acdemicStream",
		"enrStatusPy","classPy","enrTypeCy","examAppearedPyYn","examResultPy","examMarksPy","attendencePy","uniformFacProvided","textBoxFacProvided",
		"centrlSchlrshpYn","centrlSchlrshpId","stateSchlrshpYn","otherSchlrshpYn","schlrshpAmount","facProvidedCwsn","scrndFrSld","sldType","scrndFrAsd","scrndFrAdhd","isEcActivity","vocYn","sector","jobRole","appVocPy","studentStateCode");



public static void main(String[] arr) {
	System.out.println(templateHeadersFromExcel_3.size());
}



	
}
