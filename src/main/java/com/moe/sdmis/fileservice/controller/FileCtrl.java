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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.sdmis.fileservice.errorhandler.FinalResponse;
import com.moe.sdmis.fileservice.errorhandler.GenericExceptionHandler;
import com.moe.sdmis.fileservice.modal.StudentBasicProfile;
//import com.moe.rad.transfer.modal.SurplusSchoolTeacherDetails;
//import com.moe.rad.transfer.util.CustomResponse;
import com.moe.sdmis.fileservice.modal.StudentTempTable;
import com.moe.sdmis.fileservice.modal.UploadHistory;
import com.moe.sdmis.fileservice.service.FileServiceImpl;
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

	@RequestMapping(value = "/uploadData", method = RequestMethod.POST)
//	public ResponseEntity<?> saveSurplusDataBySchool(@RequestBody String data) throws Exception {
	public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile multifile,
			@RequestParam("udisecode") String udisecode, @RequestParam("userid") String userid,HttpServletRequest request) throws Exception {
//		System.out.println("Upload Data--->" + multifile);
//		System.out.println("udisecode--->" + udisecode);
//		System.out.println(udisecode + "_" + multifile.getInputStream());
		ObjectMapper objectMapper = new ObjectMapper();
		File cF=new File(userBucketPath+File.separator+udisecode);
		
		if(!cF.exists()) {
		cF.mkdir();
		}
		File oldFile= new File(userBucketPath+File.separator+udisecode+File.separator+"old");
		if(!oldFile.exists()) {
			oldFile.mkdir();
		}
		File uploadedExcel=new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+"."+"xlsx");
		File uploadedResponse=new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+"."+"txt");
		System.out.println("uploaded path--->"+uploadedExcel.toPath());
		if(uploadedExcel.exists()) {
			try {
				System.out.println("List of file--->"+(oldFile.list().length)/2);
			 Files.copy(uploadedExcel.toPath(), new File(userBucketPath+File.separator+udisecode+File.separator+"old"+File.separator+udisecode+"_"+((oldFile.list().length)/2+1)+"."+"xlsx").toPath());
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				 Files.copy(uploadedResponse.toPath(), new File(userBucketPath+File.separator+udisecode+File.separator+"old"+File.separator+udisecode+"_"+((oldFile.list().length)/2+1)+"."+"txt").toPath());
				}catch(Exception ex) {
					ex.printStackTrace();
				}
		}
		
		
		File tempFile = new File(userBucketPath+File.separator+udisecode+File.separator+udisecode+".xlsx");
		tempFile.createNewFile();
		uploadedResponse.createNewFile();
//	      try(InputStream is = multifile.getInputStream()) {
//	    	 System.out.println("Before copy");
//	        Files.copy(is, convFile.toPath()); 
//	      }
		try (OutputStream os = new FileOutputStream(tempFile)) {
			os.write(multifile.getBytes());
		}
//	      multifile.transferTo(convFile);

//	    final String FILE_NAME = "E:\\UdiseExcell\\EC0DFB51.xlsx";
		List<StudentTempTable> stdList = new ArrayList<StudentTempTable>();
		try {
//	    	File file=new File(FILE_NAME);
//	    	System.out.println(file.exists());

//			System.out.println("convFile--->" + tempFile);

			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Integer totalRows = datatypeSheet.getPhysicalNumberOfRows();

			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				StudentTempTable stdObj = new StudentTempTable();
				stdObj.setUdisecode(udisecode);
//   First Validate to Authenticate Excel
				if (currentRow.getRowNum() == 0) {
					currentRow.setZeroHeight(false);
					datatypeSheet.setColumnHidden(34, false); 
//					System.out.println("last cell value--->"+currentRow.getCell(36).getStringCellValue());
					if (!currentRow.getCell(34).getStringCellValue().equalsIgnoreCase("udise_student_template")) {
						System.out.println("ready for exception");
						throw new GenericExceptionHandler("Template Unauthenticate", "100001",request.getRemoteAddr(),userid,udisecode);
					}
				}

				// Header Test
				if (currentRow.getRowNum() == 0) {
					System.out.println("Number of column-->"+currentRow.getPhysicalNumberOfCells());
					if (currentRow.getPhysicalNumberOfCells() != 35) {
						throw new GenericExceptionHandler("Column Missing", "100002",request.getRemoteAddr(),userid,udisecode);
					}

					List<String> cellHeader = new ArrayList<String>();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						cellHeader.add(currentCell.getStringCellValue());
					}

					Map<String, Integer> headerIndexMap = IntStream.range(0, cellHeader.size()).boxed()
							.collect(Collectors.toMap(i -> cellHeader.get(i), i -> i));

					List<String> headersFromExcel = Arrays.asList("CLASS ID", "SECTION ID", "NAME", "GENDER", "DOB",
							"MOTHER NAME", "FATHER/GUARDIAN'S NAME", "AADHAR", "NAME AS PER AADHAR", "ADDRESS",
							"PINCODE", "MOBILE NUMBER", "ALTERNATE MOBILE NUMBER", "EMAIL ID(STUDENT/PARENT/GUARDIAN)",
							"MOTHER TONGUE", "SOCIAL CATEGORY", "MINORITY GROUP", "ANTYODAYA/BPL BANEFICIARY",
							"BELONGS TO EWS/DISADVANTAGED GROUP", "CWSN(YES/NO)", "IMPAIRMENT TYPE",
							"CHILD  IS INDIAN NATIONAL", "CHILD IS OUT-OF-SCHOOL-CHILD", "ADMISSION NUMBER",
							"ADMISSION DATE", "STUDENT STREAM(For higher secondary only)",
							"PREVIOUS ACADEMIC SCHOOLING STATUS", "CLASS STUDIES INPREVIOUS ACADEMIC",
							"ADMITED/ENROLLED UNDER", "APPEARED FOR EXAM IN PREVIOUS CLASS", "RESULT FOR PREVIOUS EXAM",
							"MARKS % OF PREVIOUS EXAM", "CLASS ATTENDED DAYS", "ACADEMIC YEAR ID","udise_student_template");
					Integer result = null;
					try {
						
//						System.out.println(cellHeader);
//						System.out.println(headerIndexMap.size()+"-----"+cellHeader.size());
//						
//						for(int i=0;i<cellHeader.size();i++) {
////							System.out.println(cellHeader.get(i)+"-----"+headersFromExcel.get(i));
//						}
						
						result = headersFromExcel.stream().map(headerIndexMap::get).reduce(-1,
								(x, hi) -> x < hi ? hi : cellHeader.size());
					} catch (Exception ex) {
//						ex.printStackTrace();
						throw new GenericExceptionHandler("Column Sequence changed or Missing column", "100003",request.getRemoteAddr(),userid,udisecode);
					}
				}

				if (currentRow.getRowNum() > 0) {

					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						if (currentCell.getColumnIndex() == 0) {
							stdObj.setClassId(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 1) {
							stdObj.setSectionId(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 2) {
//							System.out.println(currentCell.getStringCellValue());
							stdObj.setStudentName(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 3) {
						
							stdObj.setGender(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 4) {
							
							if(currentCell.getCellType()==CellType.NUMERIC) {
								
//								System.out.println("dated--->"+currentCell.getDateCellValue());
								
								 DataFormatter df = new DataFormatter();
								 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
								 df.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
								 String value = df.formatCellValue(currentCell);
//								 System.out.println("Original Date--->"+value);
//								 System.out.println("Original Date--->"+currentCell.getStringCellValue());
								 
//							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//							java.util.Date d =  currentCell.getDateCellValue();
//							String formatedDate = df.format(d);
							stdObj.setStudentDob(value);
							}else {
								stdObj.setStudentDob(UserExcelExporter.getExcelValue(currentCell));	
							}
							
//							System.out.println("DOB---->"+currentCell.getDateCellValue().getDate()+"/"+currentCell.getDateCellValue().getMonth()+"/"+currentCell.getDateCellValue().getYear());
							
						} else if ( currentCell.getColumnIndex() == 5) {
							stdObj.setMotherName(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 6) {
							stdObj.setFatherName(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 7) {
							if(currentCell.getCellType()==CellType.NUMERIC) {
								stdObj.setAadhaarNo(String.valueOf(Double.valueOf(currentCell.getNumericCellValue()).longValue()));	
							}else {
								stdObj.setAadhaarNo(UserExcelExporter.getExcelValue(currentCell));
							}
							
						} else if ( currentCell.getColumnIndex() == 8) {
							stdObj.setNameAsAadhaar(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 9) {
							stdObj.setAddress(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 10) {
							stdObj.setPincode(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 11) {
							String phone = new DataFormatter().formatCellValue(currentCell);
							stdObj.setMobileNo_1(phone);
						} else if (currentCell.getColumnIndex() == 12) {
							String phone = new DataFormatter().formatCellValue(currentCell);	
							stdObj.setMobileNo_2(phone);
						} else if (currentCell.getColumnIndex() == 13) {
							stdObj.setEmailId(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 14) {
							stdObj.setMotherTongue(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 15) {
							stdObj.setSocCatId(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 16) {
							stdObj.setMinorityId(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 17) {
							stdObj.setIsBplYn(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 18) {
							stdObj.setEwsYn(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 19) {
							stdObj.setCwsnYn(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 20) {
							stdObj.setImpairmentType(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 21) {
							stdObj.setNatIndYn(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 22) {
							stdObj.setOoscYn(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 23) {
							stdObj.setAdmnNumber(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 24) {
							
							if(currentCell.getCellType()==CellType.NUMERIC) {
//							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//							java.util.Date d =  currentCell.getDateCellValue();
//							String formatedDate = df.format(d);
								 DataFormatter df = new DataFormatter();
								 FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
								 df.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
								 String value = df.formatCellValue(currentCell);
							stdObj.setAdmnStartDate(value);
							}else {
								stdObj.setAdmnStartDate(UserExcelExporter.getExcelValue(currentCell));
							}
							
							
						} else if (currentCell.getColumnIndex() == 25) {
							stdObj.setAcdemicStream(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 26) {
							stdObj.setEnrStatusPy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 27) {
							stdObj.setClassPy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 28) {
							stdObj.setEnrTypeCy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 29) {
							stdObj.setExamAppearedPyYn(UserExcelExporter.getExcelValue(currentCell));
						} else if ( currentCell.getColumnIndex() == 30) {
							stdObj.setExamResultPy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 31) {
							stdObj.setExamMarksPy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 32) {
							stdObj.setAttendencePy(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 33) {
							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 34) {
							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
						} else if (currentCell.getColumnIndex() == 35) {
							stdObj.setAcYearId(UserExcelExporter.getExcelValue(currentCell));
						}
						
					}

				}
				if(currentRow.getRowNum()!=0) {
				stdList.add(stdObj);
				}
//				if (stdObj.getStudentName() != null) {
//					stdList.add(stdObj);
//				}else {
//					
//				}
			}

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
		List<Map<String, HashMap<String, String>>>  finalResponse= fileServiceImpl.uploadData(stdList,userid,request.getRemoteAddr());
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
		try {
		long 	statusCount=	finalResponse.stream().filter((e)->
		e.keySet().contains("finalStatus")
		).count();
		
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
	public void downloadUdiseTemplate(HttpServletResponse response) throws Exception {

//		 throw new GenericExceptionHandler("template not found","406");

		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SchoolBulkTemplate_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

//	        List<User> listUsers = service.listAll();

		UserExcelExporter excelExporter = new UserExcelExporter();

		excelExporter.export(response);

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
	public Stream<String>  getValidatedData(@RequestBody Integer data) throws Exception {
		return fileServiceImpl.getValidatedData(data);
	}
	
	
	@RequestMapping(path = "/downloadUploadedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadUploadedExcel(String param) throws IOException {

	    // ...
		File uploadedExcel=new File(userBucketPath+File.separator+param+File.separator+param+"."+"xlsx");

	    InputStreamResource resource = new InputStreamResource(new FileInputStream(uploadedExcel));
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+param+".xlsx");
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(uploadedExcel.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}
	

}
