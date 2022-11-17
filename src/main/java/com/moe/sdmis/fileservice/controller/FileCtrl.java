package com.moe.sdmis.fileservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
//import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.ss.usermodel.
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.sdmis.fileservice.db.NativeRepository;
import com.moe.sdmis.fileservice.errorhandler.FinalResponse;
import com.moe.sdmis.fileservice.errorhandler.GenericExceptionHandler;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
//import com.moe.rad.transfer.modal.SurplusSchoolTeacherDetails;
//import com.moe.rad.transfer.util.CustomResponse;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.service.FileServiceImpl;
import com.moe.sdmis.fileservice.utility.ConfigurableUtility;
import com.moe.sdmis.fileservice.utility.UserExcelExporter;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@CrossOrigin(origins = {"http://localhost:4200", "http://10.25.26.251:4200", "http://127.0.0.1:4200"}, allowedHeaders = "*")
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
@RequestMapping("/sdmis/fileoperation")
public class FileCtrl {

	@Autowired
	FileServiceImpl fileServiceImpl;
	
	@Autowired
	UserExcelExporter UserExcelExporter;
	
	@Autowired
	CustomFxcelValidator  customFxcelValidator;
	
	@Value("${userBucket.path}")
	private String userBucketPath;
	
	@Autowired
	NativeRepository nativeRepository;

	@RequestMapping(value = "/uploadData", method = RequestMethod.POST)
//	public ResponseEntity<?> saveSurplusDataBySchool(@RequestBody String data) throws Exception {
	public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile multifile,
			@RequestParam("udisecode") String udisecode, @RequestParam("userid") String userid,@RequestParam("stateId") String stateId,HttpServletRequest request) throws Exception {
//		System.out.println("Upload Data--->" + multifile);
//		System.out.println("udisecode--->" + udisecode);
//		System.out.println(udisecode + "_" + multifile.getInputStream());
		List<String> headersFromExcel = null;
		Integer numberOfCell=null;
		DataFormatter df = new DataFormatter();
		ObjectMapper objectMapper = new ObjectMapper();
		
		File cF=new File(userBucketPath+File.separator+udisecode);
		
		if(!cF.exists()) {
		cF.mkdir();
		}
		File oldFile= new File(userBucketPath+File.separator+udisecode+File.separator+"old");
		if(!oldFile.exists()) {
			oldFile.mkdir();
		}
		File uploadedExcel=new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+"."+"xlsm");
		File uploadedResponse=new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+"."+"txt");
		System.out.println("uploaded path--->"+uploadedExcel.toPath());
		if(uploadedExcel.exists()) {
			try {
				System.out.println("List of file--->"+(oldFile.list().length)/2);
			 Files.copy(uploadedExcel.toPath(), new File(userBucketPath+File.separator+udisecode+File.separator+"old"+File.separator+udisecode+"_"+((oldFile.list().length)/2+1)+"."+"xlsm").toPath());
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				 Files.copy(uploadedResponse.toPath(), new File(userBucketPath+File.separator+udisecode+File.separator+"old"+File.separator+udisecode+"_"+((oldFile.list().length)/2+1)+"."+"txt").toPath());
				}catch(Exception ex) {
					ex.printStackTrace();
				}
		}
		
		
		File tempFile = new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+".xlsm");
		tempFile.createNewFile();
		uploadedResponse.createNewFile();
//	      try(InputStream is = multifile.getInputStream()) {
//	    	 System.out.println("Before copy");
//	        Files.copy(is, convFile.toPath()); 
//	      }
		try (OutputStream os = new FileOutputStream(tempFile)) {
			os.write(multifile.getBytes());
			os.close();
		}catch(Exception ex) {
			
		}
//	      multifile.transferTo(convFile);

//	    final String FILE_NAME = "E:\\UdiseExcell\\EC0DFB51.xlsm";
		List<CommonBean> stdList = new ArrayList<CommonBean>();
		try {
//	    	File file=new File(FILE_NAME);
//	    	System.out.println(file.exists());

//			System.out.println("convFile--->" + tempFile);

			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Integer totalRows = datatypeSheet.getPhysicalNumberOfRows();
			
//			FileInputStream inputStream = new FileInputStream(new File(excelFileLocation));
//			XSSFWorkbook resultWorkbook = new XSSFWorkbook(inputStream);
		
			
			
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				CommonBean stdObj = new CommonBean();
				stdObj.setUdisecode(udisecode);
//   First Validate to Authenticate Excel
				if (currentRow.getRowNum() == 0) {
					currentRow.setZeroHeight(false);
					datatypeSheet.setColumnHidden(51, false); 
//					System.out.println("last cell value--->"+currentRow.getCell(36).getStringCellValue());
//					System.out.println(cellIterator.hasNext());
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
//						System.out.println(currentCell.getColumnIndex()+"----"+currentCell.getNumericCellValue());
					}
					System.out.println("School Id---->"+df.formatCellValue(currentRow.getCell(51))+"------"+udisecode);
					
					if (!df.formatCellValue(currentRow.getCell(51)).equalsIgnoreCase(udisecode)) {
						System.out.println("ready for exception");
						throw new GenericExceptionHandler("Template Unauthenticate", "100001",request.getRemoteAddr(),userid,udisecode);
					}
				}

				// Header Test
				if (currentRow.getRowNum() == 4) {
					System.out.println("Number of column-->"+currentRow.getPhysicalNumberOfCells());
					if(stateId.equalsIgnoreCase("116")){
						numberOfCell=ConfigurableUtility.state116commonPhysicalColumn;
						
					}else {
						numberOfCell=ConfigurableUtility.commonPhysicalColumn;
					}
					
					if (currentRow.getPhysicalNumberOfCells() != numberOfCell) {
						throw new GenericExceptionHandler("Column Missing", "100002",request.getRemoteAddr(),userid,udisecode);
					}

					List<String> cellHeader = new ArrayList<String>();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						cellHeader.add(currentCell.getStringCellValue().replaceAll("\\s+"," "));
						System.out.println("Cell Header value--->"+currentCell.getStringCellValue());
					}

					Map<String, Integer> headerIndexMap = IntStream.range(0, cellHeader.size()).boxed()
							.collect(Collectors.toMap(i -> cellHeader.get(i), i -> i));

//					List<String> headersFromExcel = Arrays.asList("CLASS ID", "SECTION ID", "NAME", "GENDER", "DOB",
//							"MOTHER NAME", "FATHER/GUARDIAN'S NAME", "AADHAR", "NAME AS PER AADHAR", "ADDRESS",
//							"PINCODE", "MOBILE NUMBER", "ALTERNATE MOBILE NUMBER", "EMAIL ID(STUDENT/PARENT/GUARDIAN)",
//							"MOTHER TONGUE", "SOCIAL CATEGORY", "MINORITY GROUP", "ANTYODAYA/BPL BANEFICIARY",
//							"BELONGS TO EWS/DISADVANTAGED GROUP", "CWSN(YES/NO)", "IMPAIRMENT TYPE",
//							"CHILD  IS INDIAN NATIONAL", "CHILD IS OUT-OF-SCHOOL-CHILD", "ADMISSION NUMBER",
//							"ADMISSION DATE", "STUDENT STREAM(For higher secondary only)",
//							"PREVIOUS ACADEMIC SCHOOLING STATUS", "CLASS STUDIES INPREVIOUS ACADEMIC",
//							"ADMITED/ENROLLED UNDER", "APPEARED FOR EXAM IN PREVIOUS CLASS", "RESULT FOR PREVIOUS EXAM",
//							"MARKS % OF PREVIOUS EXAM", "CLASS ATTENDED DAYS", "ACADEMIC YEAR ID","udise_student_template");
					
					if(stateId.equalsIgnoreCase("116")){
						 headersFromExcel = ConfigurableUtility.state116CommomHeadersFromExcel;
					}else {
						 headersFromExcel = ConfigurableUtility.commomHeadersFromExcel;
					}
					
					Integer result = null;
					try {
						System.out.println(cellHeader.size()+"---"+headersFromExcel.size());
//						System.out.println(cellHeader);
//						System.out.println(headerIndexMap.size()+"-----"+cellHeader.size());
//						
						for(int i=0;i<cellHeader.size();i++) {
							
							if(!cellHeader.get(i).equalsIgnoreCase(headersFromExcel.get(i))) {
								System.out.println(cellHeader.get(i)+"-----"+headersFromExcel.get(i));
							}
							
							
						}
					
						
						result = headersFromExcel.stream().map(headerIndexMap::get).reduce(-1,
								(x, hi) -> x < hi ? hi : cellHeader.size());
					} catch (Exception ex) {
//						ex.printStackTrace();
						throw new GenericExceptionHandler("Column Sequence changed or Missing column", "100003",request.getRemoteAddr(),userid,udisecode);
					}
				}
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(IndexedColors.RED.index);
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Map<String, HashMap<String, String>> mObject=new LinkedHashMap<String,HashMap<String,String>>();
				if (currentRow.getRowNum() > 4) {
//					Cell currentCell=currentRow.getCell(1).getCel
					int i=0;
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("UDISE Code of School")) {
							stdObj.setUdisecode(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"udiseCode",checkNullandTrim(stdObj.getUdisecode())).get("udiseCode").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Grade/Class")) {
							stdObj.setClassId(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"classId",checkNullandTrim(stdObj.getClassId())).get("classId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("SECTION")) {
							stdObj.setSectionId(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"sectionId",checkNullandTrim(stdObj.getSectionId())).get("sectionId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Roll no")) {
							stdObj.setRollNo(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"rollNo",checkNullandTrim(stdObj.getRollNo())).get("rollNo").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Name of Student")) {
							stdObj.setStudentName(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringNonSpecialValidation(mObject,"studentName",checkNullandTrim(stdObj.getStudentName())).get("studentName").get("status").equalsIgnoreCase("0")) {
								System.out.println("student name---->"+customFxcelValidator.stringNonSpecialValidation(mObject,"studentName",checkNullandTrim(stdObj.getStudentName())).get("studentName").get("status").equalsIgnoreCase("0"));
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("GENDER")) {
							stdObj.setGender(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"gender",checkNullandTrim(stdObj.getGender())).get("gender").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("DOB (DD-MM-YYYY)")) {
							stdObj.setStudentDob(df.formatCellValue(currentCell));
							if(customFxcelValidator.dateValidation(mObject,"studentDob",checkNullandTrim(stdObj.getStudentDob())).get("studentDob").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOTHER NAME")) {
							stdObj.setMotherName(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringNonSpecialValidation(mObject,"motherName",checkNullandTrim(stdObj.getMotherName())).get("motherName").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("FATHER Name")) {
							stdObj.setFatherName(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringNonSpecialValidation(mObject,"fatherName",checkNullandTrim(stdObj.getFatherName())).get("fatherName").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("GUARDIAN'S NAME")) {
							stdObj.setGuardianName(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringNonSpecialValidation(mObject,"guardianName",checkNullandTrim(stdObj.getGuardianName())).get("guardianName").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Aadhaar No of Child :")) {
							stdObj.setAadhaarNo(df.formatCellValue(currentCell));
							if(customFxcelValidator.adharValidation(mObject,"aadhaarNo",checkNullandTrim(stdObj.getAadhaarNo())).get("aadhaarNo").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("NAME AS PER AADHAR")) {
							stdObj.setNameAsAadhaar(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringNonSpecialValidation(mObject,"nameAsAadhaar",checkNullandTrim(stdObj.getNameAsAadhaar())).get("nameAsAadhaar").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ADDRESS")) {
							stdObj.setAddress(df.formatCellValue(currentCell));
							if(customFxcelValidator.stringValidation(mObject,"address",checkNullandTrim(stdObj.getAddress())).get("address").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("PINCODE")) {
							stdObj.setPincode(df.formatCellValue(currentCell));
							if(customFxcelValidator.pincodeValidation(mObject,"pincode",checkNullandTrim(stdObj.getPincode())).get("pincode").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOBILE NUMBER")) {
							stdObj.setMobileNo_1(df.formatCellValue(currentCell));
							if(customFxcelValidator.mobileValidation(mObject,"mobileNo_1",checkNullandTrim(stdObj.getMobileNo_1())).get("mobileNo_1").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ALTERNATE MOBILE NUMBER")) {
							stdObj.setMobileNo_2(df.formatCellValue(currentCell));
							if(customFxcelValidator.mobileValidation(mObject,"mobileNo_2",checkNullandTrim(stdObj.getMobileNo_2())).get("mobileNo_2").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("EMAIL ID (STUDENT/PARENT/GUARDIAN)")) {
							stdObj.setEmailId(df.formatCellValue(currentCell));
							if(customFxcelValidator.emailValidation(mObject,"emailId",checkNullandTrim(stdObj.getEmailId())).get("emailId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOTHER TONGUE")) {
							stdObj.setMotherTongue(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"motherTongue",checkNullandTrim(stdObj.getMotherTongue())).get("motherTongue").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("SOCIAL CATEGORY")) {
							stdObj.setSocCatId(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"socCatId",checkNullandTrim(stdObj.getSocCatId())).get("socCatId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MINORITY GROUP")) {
							stdObj.setMinorityId(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"minorityId",checkNullandTrim(stdObj.getMinorityId())).get("minorityId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("BPL BANEFICIARY")) {
							stdObj.setIsBplYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"isBplYn",checkNullandTrim(stdObj.getIsBplYn())).get("isBplYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ANTYODAYA")) {
							stdObj.setAayBplYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"aayBplYn",checkNullandTrim(stdObj.getAayBplYn())).get("aayBplYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("BELONGS TO EWS/DISADVANTAGED GROUP")) {
							stdObj.setEwsYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"ewsYn",checkNullandTrim(stdObj.getEwsYn())).get("ewsYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CWSN(YES/NO)")) {
							stdObj.setCwsnYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"cwsnYn",checkNullandTrim(stdObj.getCwsnYn())).get("cwsnYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("IMPAIRMENT TYPE")) {
							stdObj.setImpairmentType(df.formatCellValue(currentCell));
							if(customFxcelValidator.jsonValidation(mObject,"impairmentType",checkNullandTrim(stdObj.getImpairmentType())).get("impairmentType").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CHILD IS INDIAN NATIONAL")) {
							stdObj.setNatIndYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"natIndYn",checkNullandTrim(stdObj.getNatIndYn())).get("natIndYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CHILD IS OUT-OF-SCHOOL-CHILD")) {
							stdObj.setOoscYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"ooscYn",checkNullandTrim(stdObj.getOoscYn())).get("ooscYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Admission Number")) {
							stdObj.setAdmnNumber(df.formatCellValue(currentCell));
							if(customFxcelValidator.admisionNumberValidation(mObject,"admnNumber",checkNullandTrim(stdObj.getAdmnNumber())).get("admnNumber").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ADMISSION DATE (DD-MM-YYYY)")) {
							stdObj.setAdmnStartDate(df.formatCellValue(currentCell));
							if(customFxcelValidator.dateValidation(mObject,"admnStartDate",checkNullandTrim(stdObj.getAdmnStartDate())).get("admnStartDate").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("STUDENT STREAM (For higher secondary only)")) {
							stdObj.setAcdemicStream(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"acdemicStream",checkNullandTrim(stdObj.getAcdemicStream())).get("acdemicStream").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("PREVIOUS ACADEMIC SCHOOLING STATUS")) {
							stdObj.setEnrStatusPy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"enrStatusPy",checkNullandTrim(stdObj.getEnrStatusPy())).get("enrStatusPy").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CLASS STUDIES IN PREVIOUS ACADEMIC")) {
							stdObj.setClassPy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"classPy",checkNullandTrim(stdObj.getClassPy())).get("classPy").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ADMITED/ ENROLLED UNDER")) {
							stdObj.setEnrTypeCy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"enrType",checkNullandTrim(stdObj.getEnrTypeCy())).get("enrType").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("APPEARED FOR EXAM IN PREVIOUS CLASS")) {
							stdObj.setExamAppearedPyYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"examAppearedPyYn",checkNullandTrim(stdObj.getExamAppearedPyYn())).get("examAppearedPyYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("RESULT FOR PREVIOUS EXAM")) {
							stdObj.setExamResultPy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"examResultPy",checkNullandTrim(stdObj.getExamResultPy())).get("examResultPy").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MARKS % OF PREVIOUS EXAM")) {
							stdObj.setExamMarksPy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"examMarksPy",checkNullandTrim(stdObj.getExamMarksPy())).get("examMarksPy").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CLASS ATTENDED DAYS")) {
							stdObj.setAttendencePy(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"attendencePy",checkNullandTrim(stdObj.getAttendencePy())).get("attendencePy").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Facility - Free Uniform")) {
							stdObj.setUniformFacProvided(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"uniformFacProvided",checkNullandTrim(stdObj.getUniformFacProvided())).get("uniformFacProvided").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Facility - Free TextBook")) {
							stdObj.setTextBoxFacProvided(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"textBoxFacProvided",checkNullandTrim(stdObj.getTextBoxFacProvided())).get("textBoxFacProvided").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Central Scholarship")) {
							stdObj.setCentrlSchlrshpYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"centrlSchlrshpYn",checkNullandTrim(stdObj.getCentrlSchlrshpYn())).get("centrlSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Name of Scholarship (provide code only)")) {
							stdObj.setCentrlSchlrshpId(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"centrlSchlrshpId",checkNullandTrim(stdObj.getCentrlSchlrshpId())).get("centrlSchlrshpId").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("State Scholarship")) {
							stdObj.setStateSchlrshpYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"stateSchlrshpYn",checkNullandTrim(stdObj.getStateSchlrshpYn())).get("stateSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Other Scholarship")) {
							stdObj.setOtherSchlrshpYn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"otherSchlrshpYn",checkNullandTrim(stdObj.getOtherSchlrshpYn())).get("otherSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Scholarship Amount")) {
							stdObj.setSchlrshpAmount(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"schlrshpAmount",checkNullandTrim(stdObj.getSchlrshpAmount())).get("schlrshpAmount").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Facility provided to the CWSN")) {;
							stdObj.setFacProvidedCwsn(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"facProvidedCwsn",checkNullandTrim(stdObj.getFacProvidedCwsn())).get("facProvidedCwsn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Specific Learning Disability (SLD)")) {
							stdObj.setScrndFrSld(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"scrndFrSld",checkNullandTrim(stdObj.getScrndFrSld())).get("scrndFrSld").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Type of Specific Learning Disability (SLD)")) {
							stdObj.setSldType(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"sldType",checkNullandTrim(stdObj.getSldType())).get("sldType").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Autism Spectrum Disorder")) {
							stdObj.setScrndFrAsd(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"scrndFrAsd",checkNullandTrim(stdObj.getScrndFrAsd())).get("scrndFrAsd").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Attention Deficit Hyperactive Disorder (ADHD)")) {
							stdObj.setScrndFrAdhd(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"scrndFrAdhd",checkNullandTrim(stdObj.getScrndFrAdhd())).get("scrndFrAdhd").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
						if(headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("extra curricular activity")) {
							stdObj.setIsEcActivity(df.formatCellValue(currentCell));
							if(customFxcelValidator.numberValidation(mObject,"isEcActivity",checkNullandTrim(stdObj.getIsEcActivity())).get("isEcActivity").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}else {
								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
								currentCell.setCellStyle(cellStyle);
							}
						}
						
					
					
						
						
						
//						if (currentCell.getColumnIndex() == 0) {
//							stdObj.setClassId(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 1) {
//							stdObj.setSectionId(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 2) {
////							System.out.println(currentCell.getStringCellValue());
//							stdObj.setStudentName(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 3) {
//						
//							stdObj.setGender(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 4) {
//							
//							if(currentCell.getCellType()==CellType.NUMERIC) {
//								
////								System.out.println("dated--->"+currentCell.getDateCellValue());
//								
////								 DataFormatter df = new DataFormatter();
//								 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//								 df.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
//								 String value = df.formatCellValue(currentCell);
////								 System.out.println("Original Date--->"+value);
////								 System.out.println("Original Date--->"+currentCell.getStringCellValue());
//								 
////							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
////							java.util.Date d =  currentCell.getDateCellValue();
////							String formatedDate = df.format(d);
//							stdObj.setStudentDob(value);
//							}else {
//								stdObj.setStudentDob(UserExcelExporter.getExcelValue(currentCell));	
//							}
//							
////							System.out.println("DOB---->"+currentCell.getDateCellValue().getDate()+"/"+currentCell.getDateCellValue().getMonth()+"/"+currentCell.getDateCellValue().getYear());
//							
//						} else if ( currentCell.getColumnIndex() == 5) {
//							stdObj.setMotherName(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 6) {
//							stdObj.setFatherName(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 7) {
//							if(currentCell.getCellType()==CellType.NUMERIC) {
//								stdObj.setAadhaarNo(String.valueOf(Double.valueOf(currentCell.getNumericCellValue()).longValue()));	
//							}else {
//								stdObj.setAadhaarNo(UserExcelExporter.getExcelValue(currentCell));
//							}
//							
//						} else if ( currentCell.getColumnIndex() == 8) {
//							stdObj.setNameAsAadhaar(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 9) {
//							stdObj.setAddress(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 10) {
//							stdObj.setPincode(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 11) {
//							String phone = new DataFormatter().formatCellValue(currentCell);
//							stdObj.setMobileNo_1(phone);
//						} else if (currentCell.getColumnIndex() == 12) {
//							String phone = new DataFormatter().formatCellValue(currentCell);	
//							stdObj.setMobileNo_2(phone);
//						} else if (currentCell.getColumnIndex() == 13) {
//							stdObj.setEmailId(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 14) {
//							stdObj.setMotherTongue(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 15) {
//							stdObj.setSocCatId(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 16) {
//							stdObj.setMinorityId(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 17) {
//							stdObj.setIsBplYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 18) {
//							stdObj.setEwsYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 19) {
//							stdObj.setCwsnYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 20) {
//							stdObj.setImpairmentType(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 21) {
//							stdObj.setNatIndYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 22) {
//							stdObj.setOoscYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 23) {
//							stdObj.setAdmnNumber(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 24) {
//							
//							if(currentCell.getCellType()==CellType.NUMERIC) {
////							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
////							java.util.Date d =  currentCell.getDateCellValue();
////							String formatedDate = df.format(d);
////								 DataFormatter df = new DataFormatter();
//								 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//								 df.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
//								 String value = df.formatCellValue(currentCell);
//							stdObj.setAdmnStartDate(value);
//							}else {
//								stdObj.setAdmnStartDate(UserExcelExporter.getExcelValue(currentCell));
//							}
//							
//							
//						} else if (currentCell.getColumnIndex() == 25) {
//							stdObj.setAcdemicStream(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 26) {
//							stdObj.setEnrStatusPy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 27) {
//							stdObj.setClassPy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 28) {
//							stdObj.setEnrTypeCy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 29) {
//							stdObj.setExamAppearedPyYn(UserExcelExporter.getExcelValue(currentCell));
//						} else if ( currentCell.getColumnIndex() == 30) {
//							stdObj.setExamResultPy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 31) {
//							stdObj.setExamMarksPy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 32) {
//							stdObj.setAttendencePy(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 33) {
//							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 34) {
//							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
//						} else if (currentCell.getColumnIndex() == 35) {
//							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
//						}
//						
						++i;
					}

				}
		
				if(currentRow.getRowNum()>4) {
				stdList.add(stdObj);
				}
//				if (stdObj.getStudentName() != null) {
//					stdList.add(stdObj);
//				}else {
//					
//				}
			}
			File uploadedExcel1=new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+"_validated."+"xlsm");
			FileOutputStream outFile =new FileOutputStream(uploadedExcel1);
			workbook.write(outFile);
//			FileOutputStream outFile =new FileOutputStream(uploadedExcel);
//			workbook.write(outFile);
//			outFile.close();
//            for(int x = 1; x<=totalRows; x++){
////            	 ReportRow rr = new ReportRow(); //Data structure to hold the data from the xls file.
//            	 Row dataRow =  datatypeSheet.getRow(x); //get row 1 to row n (rows containing data)
//
////            	 int idxForColumn1 = map.get("Column1"); //get the column index for the column with header name = "Column1"
////            	 int idxForColumn2 = map.get("Column2"); //get the column index for the column with header name = "Column2"
////            	 int idxForColumn3 = map.get("Column3"); //get the column index for the column with header name = "Column3"
//
//            	 Cell cell1 = dataRow.getCell(2); //Get the cells for each of the indexes
////            	 HSSFCell cell2 = dataRow.getCell(idxForColumn2) 
////            	 HSSFCell cell3 = dataRow.getCell(idxForColumn3)  
////System.out.println(cell1.ce);
//System.out.println(cell1.getNumericCellValue());
//            	 //NOTE THAT YOU HAVE TO KNOW THE DATA TYPES OF THE DATA YOU'RE EXTRACTING.
//            	 //FOR EXAMPLE I DON'T THINK YOU CAN USE cell.getStringCellValue IF YOU'RE TRYING TO GET A NUMBER
////            	 rr.setColumn1(cell1.getStringCellValue()); //Get the values out of those cells and put them into the report row object
////            	 rr.setColumn2(cell2.getStringCellValue());
////            	 rr.setColumn3(cell3.getStringCellValue());
//
////            	 listOfDataFromReport.add(rr);
//
//            	}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			tempFile.deleteOnExit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		System.out.println("stdList--->"+stdList);
//		try {
		List<Map<String, HashMap<String, String>>>  finalResponse= fileServiceImpl.uploadData(stdList,userid,request.getRemoteAddr(),udisecode);
//		System.out.println(objectMapper.writeValueAsString(finalResponse));
//		uploadedResponse.wr
		try {
		PrintWriter printWriter = new PrintWriter(uploadedResponse);
		printWriter.print(objectMapper.writeValueAsString(finalResponse));
		printWriter.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
//		
//		if(finalResponse !=null) {
//			FinalResponse
//		}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
		
		
		System.out.println("finalResponse---->"+finalResponse);
		
		try {
		long 	statusCount=	finalResponse.stream().filter((e)->
		e.entrySet().contains("staus=0")
//		e.entrySet().stream().filter(a->a.getValue().entrySet().contains("staus=0").count())
		).count();
			

		
		System.out.println("Status count--->"+statusCount);
		if(statusCount>0) {
			return  ResponseEntity.ok(new FinalResponse("","1","Uploaded Successfully but Some data Error check and resolve"));
		}else {
			return  ResponseEntity.ok(new FinalResponse("","1","Uploaded Successfully"));
		}
		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return  ResponseEntity.ok(new FinalResponse("","1","Success"));
	}

	@RequestMapping(value = "/downloadUdiseTemplate", method = RequestMethod.GET)
	public void downloadUdiseTemplate(@RequestParam("schoolId") String schoolId,@RequestParam("stateId") String stateId,HttpServletResponse response) throws Exception {
		
		System.out.println("autowire---->"+nativeRepository);
		System.out.println("schoolId---->"+schoolId);
		String templateName=null;
		if(stateId.equalsIgnoreCase("116")) {
			templateName="Bulk_Student_Data_template_statewise_116.xlsm";
		}else {
			templateName="Bulk_Student_Data_template_common.xlsm";
		}
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SchoolBulkTemplate_" + currentDateTime + ".xlsm";
		response.setHeader(headerKey, headerValue);
		UserExcelExporter excelExporter = new UserExcelExporter();
		excelExporter.export(response,schoolId,nativeRepository,templateName);

	}

	@RequestMapping(value = "/finalUpdateStudentData", method = RequestMethod.POST)
	public void finalUpdateStudentData(@RequestBody String data) throws Exception {
		System.out.println(data);
		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> map = new ObjectMapper().readValue(data, Map.class);

//		 Map<String, String> map = objectMapper.convertValue(data,Map.class);
		fileServiceImpl.finalUpdateStudentData(String.valueOf(map.get("udiseCode")));

	}
	
	@RequestMapping(value = "/getUploadedHistor", method = RequestMethod.POST)
	public List<UploadHistory>  getUploadedHistor(@RequestBody Integer data) throws Exception {
		return fileServiceImpl.getUploadedHistor(data);
	}
	
	
	@RequestMapping(value = "/getValidatedData", method = RequestMethod.POST)
//	public Stream<String>  getValidatedData(@RequestBody String data) throws Exception {
		public Map<String, Object>  getValidatedData(@RequestBody String data) throws Exception {
		System.out.println("data---->"+data);
		Map<String, Object> map=null;
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			map = new ObjectMapper().readValue(data, Map.class);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		Map<String,Object> mp=new HashMap<String,Object>();
		mp.put("value", fileServiceImpl.getValidatedData((int)map.get("schoolId")));
//		System.out.println(ConfigurableUtility.state111HeaderKey);
		if((int)map.get("stateId")==116) {
			mp.put("header", ConfigurableUtility.state116HeaderKey);	
		}else {
			mp.put("header", ConfigurableUtility.commonHeaderKey);
		}

//		return fileServiceImpl.getValidatedData((int)map.get("schoolId"));
return mp;
	}
	
	
	@RequestMapping(path = "/downloadUploadedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadUploadedExcel(String param) throws IOException {

	    // ...
		File uploadedExcel=new File(userBucketPath+File.separator+param+File.separator+param+"."+"xlsm");

	    InputStreamResource resource = new InputStreamResource(new FileInputStream(uploadedExcel));
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+param+".xlsm");
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(uploadedExcel.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	
	
	@RequestMapping(path = "/downloadValidatedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadValidatedExcel(@RequestParam("schoolId") String schoolId,@RequestParam("schoolId") String stateId,HttpServletResponse response) throws IOException {
		System.out.println("called");
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SchoolBulkTemplate_" + currentDateTime + ".xlsm";
		response.setHeader(headerKey, headerValue);
		UserExcelExporter excelExporter = new UserExcelExporter();
		List<String> commomHeadersFromValidation=null;
		if(stateId.equalsIgnoreCase("116")) {
			commomHeadersFromValidation=ConfigurableUtility.state116CommomHeadersFromExcel;
		}else {
			commomHeadersFromValidation=ConfigurableUtility.commomHeadersFromValidation;
		}
		excelExporter.exportValidatedExcel(response,schoolId,commomHeadersFromValidation,userBucketPath);
		return null;
		
	}
	

	public String checkNullandTrim(String value) {
		if(value !=null && value !="" ) {
			return value.trim();
		}
		return value;
	}
	
	
	
	

}
