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
import com.moe.sdmis.fileservice.db.StaticReportBean;
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
import com.moe.sdmis.fileservice.utility.MotherTongMaster;
import com.moe.sdmis.fileservice.utility.TemplateConfiguration1;
import com.moe.sdmis.fileservice.utility.UserExcelExporter;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = {"http://localhost:4200", "http://10.25.26.251:4200", "http://127.0.0.1:4200"}, allowedHeaders = "*")
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
@RequestMapping("/sdmis/fileoperation")
public class FileCtrl {

	@Autowired
	FileServiceImpl fileServiceImpl;

	@Autowired
	UserExcelExporter UserExcelExporter;

	@Autowired
	CustomFxcelValidator customFxcelValidator;

	@Value("${userBucket.path}")
	private String userBucketPath;

	@Value("${template.path}")
	private String templatePath;

	@Autowired
	NativeRepository nativeRepository;

	@RequestMapping(value = "/uploadDoc", method = RequestMethod.POST)
	public ResponseEntity<?> uploadDoc(@RequestParam("file") MultipartFile multifile,
			@RequestParam("schoolId") String schoolId, @RequestParam("userid") String userid,
			@RequestParam("stateId") String stateId, HttpServletRequest request) throws Exception {
		List<String> headersFromExcel = null;
		Integer numberOfCell = null;
		DataFormatter df = new DataFormatter();
		ObjectMapper objectMapper = new ObjectMapper();
		File cF = new File(userBucketPath + File.separator + schoolId);
		if (!cF.exists()) {
			cF.mkdir();
		}
		File oldFile = new File(userBucketPath + File.separator + schoolId + File.separator + "old");
		if (!oldFile.exists()) {
			oldFile.mkdir();
		}
		File uploadedExcel = new File(
				userBucketPath + File.separator + schoolId + File.separator + schoolId + "." + "xlsm");
		File uploadedResponse = new File(
				userBucketPath + File.separator + schoolId + File.separator + schoolId + "." + "txt");
//		System.out.println("uploaded path--->" + uploadedExcel.toPath());
		if (uploadedExcel.exists()) {
			try {
//				System.out.println("List of file--->" + (oldFile.list().length) / 2);
				Files.copy(uploadedExcel.toPath(),
						new File(userBucketPath + File.separator + schoolId + File.separator + "old" + File.separator
								+ schoolId + "_" + ((oldFile.list().length) / 2 + 1) + "." + "xlsm").toPath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				Files.copy(uploadedResponse.toPath(),
						new File(userBucketPath + File.separator + schoolId + File.separator + "old" + File.separator
								+ schoolId + "_" + ((oldFile.list().length) / 2 + 1) + "." + "txt").toPath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		File tempFile = new File(userBucketPath + File.separator + schoolId + File.separator + schoolId + ".xlsm");
		tempFile.createNewFile();
		uploadedResponse.createNewFile();
		try (OutputStream os = new FileOutputStream(tempFile)) {
			os.write(multifile.getBytes());
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Check basic validation
//		try {
		FileInputStream excelFile = new FileInputStream(tempFile);
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(1);
		Iterator<Row> iterator = datatypeSheet.iterator();
		Integer totalRows = datatypeSheet.getPhysicalNumberOfRows();

		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			CommonBean stdObj = new CommonBean();
			stdObj.setUdisecode(schoolId);
			if (currentRow.getRowNum() == 0) {
				currentRow.setZeroHeight(false);
				datatypeSheet.setColumnHidden(53, false);
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
				}

//					System.out.println(currentRow.getCell(53)+"-----"+schoolId);

				if (!df.formatCellValue(currentRow.getCell(53)).equalsIgnoreCase(schoolId)) {
					System.out.println("ready for exception");
					uploadedExcel.delete();
					uploadedResponse.delete();
					throw new GenericExceptionHandler("Template Unauthenticate", "100001", request.getRemoteAddr(),
							userid, schoolId);
				}
			}

			if (currentRow.getRowNum() == 6) {
//				System.out.println("Number of column-->" + currentRow.getPhysicalNumberOfCells());
				if (stateId.equalsIgnoreCase("116")) {
					numberOfCell = ConfigurableUtility.commonPhysicalColumn_1;

				} else if (stateId.equalsIgnoreCase("112")) {
					numberOfCell = ConfigurableUtility.commonPhysicalColumn_2;
				} else {
					numberOfCell = ConfigurableUtility.commonPhysicalColumn;
				}

				if (currentRow.getPhysicalNumberOfCells() != numberOfCell) {
					uploadedExcel.delete();
					uploadedResponse.delete();
					throw new GenericExceptionHandler("Column Missing", "100002", request.getRemoteAddr(), userid,
							schoolId);
				}

				if (workbook.getNumberOfSheets() != 4) {
					uploadedExcel.delete();
					uploadedResponse.delete();
					throw new GenericExceptionHandler("Template Unauthenticated and excel has not proper sheet",
							"100004", request.getRemoteAddr(), userid, schoolId);
				}

				List<String> cellHeader = new ArrayList<String>();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					cellHeader.add(currentCell.getStringCellValue().replaceAll("\\s+", " "));
//						System.out.println("Cell Header value--->" + currentCell.getStringCellValue());
				}

				Map<String, Integer> headerIndexMap = IntStream.range(0, cellHeader.size()).boxed()
						.collect(Collectors.toMap(i -> cellHeader.get(i), i -> i));

				if (stateId.equalsIgnoreCase("116")) {
					headersFromExcel = ConfigurableUtility.templateHeadersFromExcel_1;
				} else if (stateId.equalsIgnoreCase("112")) {
					headersFromExcel = ConfigurableUtility.templateHeadersFromExcel_2;
				} else {
					headersFromExcel = ConfigurableUtility.commomHeadersFromExcel;
				}

				Integer result = null;
				try {
					for (int i = 0; i < cellHeader.size(); i++) {
						if (!cellHeader.get(i).equalsIgnoreCase(headersFromExcel.get(i))) {
//							System.out.println(cellHeader.get(i) + "-------" + headersFromExcel.get(i));
						}

					}
					result = headersFromExcel.stream().map(headerIndexMap::get).reduce(-1,
							(x, hi) -> x < hi ? hi : cellHeader.size());
				} catch (Exception ex) {
					uploadedExcel.delete();
					uploadedResponse.delete();
					throw new GenericExceptionHandler("Column Sequence changed or Missing column", "100003",
							request.getRemoteAddr(), userid, schoolId);
				}
			}

		}
//		System.out.println("History update--->");

		// Update History

		fileServiceImpl.updateHistory(request.getRemoteHost(), schoolId, userid, "1");
//			
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
		return ResponseEntity.ok(new FinalResponse("", "1", "Success"));
	}

	@RequestMapping(value = "/docValidate", method = RequestMethod.POST)
	public ResponseEntity<?> docValidate(@RequestBody String data, HttpServletRequest request) throws Exception {
		DataFormatter df = new DataFormatter();
		System.out.println("Before call");
		Map<String, Object> map = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			map = new ObjectMapper().readValue(data, Map.class);
			File tempFile = new File(userBucketPath + File.separator + map.get("schoolId") + File.separator
					+ map.get("schoolId") + ".xlsm");
			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Integer totalRows = datatypeSheet.getPhysicalNumberOfRows();
			Map<String, HashMap<String, String>> mObject = new LinkedHashMap<String, HashMap<String, String>>();
			
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			CellStyle correctCellStyle = workbook.createCellStyle();
			correctCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
			correctCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			StaticReportBean sObj = null;
			StaticReportBean sectionsObj = null;
			StaticReportBean vocationObj = null;
			Map<Integer, Boolean> mtongObj = null;
			HashMap<String, String> sectionMap = new HashMap<String, String>();
			List<CommonBean> stdList = new ArrayList<CommonBean>();
			HashMap<String, Boolean> lowerSector = new HashMap<String, Boolean>();
			HashMap<String, Boolean> lowerSubSector = new HashMap<String, Boolean>();
			HashMap<String, Boolean> higherSector = new HashMap<String, Boolean>();
			HashMap<String, Boolean> higherSubSector = new HashMap<String, Boolean>();
			try {
				UserExcelExporter excelExporter = new UserExcelExporter();
				sObj = excelExporter.getSchoolData(nativeRepository, String.valueOf(map.get("schoolId")));
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				UserExcelExporter excelExporter = new UserExcelExporter();
				sectionsObj = excelExporter.getSectionData(nativeRepository, String.valueOf(map.get("schoolId")));

//				System.out.println(sectionsObj.getRowValue().get(0).get("class_id"));
				for (int i = 0; i < sectionsObj.getRowValue().size(); i++) {
					sectionMap.put(String.valueOf(sectionsObj.getRowValue().get(i).get("class_id")),
							String.valueOf(sectionsObj.getRowValue().get(i).get("sections")));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {

				UserExcelExporter excelExporter = new UserExcelExporter();
				vocationObj = excelExporter.getVocationData(nativeRepository, String.valueOf(map.get("schoolId")));
				System.out.println("Vocation detaisl--->"+vocationObj.getRowValue());
				
				for (int i = 0; i < vocationObj.getRowValue().size(); i++) {
					if (String.valueOf(vocationObj.getRowValue().get(i).get("grade_id")).equalsIgnoreCase("1")) {
						lowerSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sector_id")), true);
						lowerSubSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sub_sector_id")), true);
					} else if (String.valueOf(vocationObj.getRowValue().get(i).get("grade_id")).equalsIgnoreCase("2")) {
						higherSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sector_id")), true);
						higherSubSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sub_sector_id")), true);
					}
				}
				
				System.out.println(lowerSector);
				System.out.println(lowerSubSector);
				System.out.println(higherSector);
				System.out.println(higherSubSector);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				CommonBean stdObj = new CommonBean();
				stdObj.setUdisecode(String.valueOf(map.get("schoolId")));
				if (currentRow.getRowNum() > 6) {
//					System.out.println("Row number--->"+currentRow.getRowNum());
					if (String.valueOf(map.get("stateId")).equalsIgnoreCase("116")) {

						mtongObj = MotherTongMaster.motherTongMap_116;
						
						if (currentRow.getRowNum() > 6) {
							TemplateConfiguration1 tcObj=new TemplateConfiguration1();
							stdObj=tcObj.dataPrepration(mtongObj,stdObj,currentRow,sObj,sectionsObj,vocationObj,cellStyle,correctCellStyle,lowerSector,lowerSubSector,higherSector,higherSubSector,sectionMap,mObject);
							stdList.add(stdObj);
						}
						
						

					} else if (String.valueOf(map.get("stateId")).equalsIgnoreCase("112")) {
						mtongObj = MotherTongMaster.motherTongMap_112;
						stdObj.setClassId(df.formatCellValue(currentRow.getCell(0)));
//						if (customFxcelValidator
//								.numberValidation(mObject, "classId", checkNullandTrim(stdObj.getClassId()), 12)
//								.get("classId").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(0).setCellStyle(cellStyle);
//						} 

						try {
							if (Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_frm")))) <= Integer
											.parseInt(classCheck(checkNull(stdObj.getClassId())))
									&& Integer.parseInt(checkNull(
											String.valueOf(sObj.getRowValue().get(0).get("class_to")))) >= Integer
													.parseInt(classCheck(checkNull(stdObj.getClassId())))) {
								setCellColors(currentRow, currentRow.getCell(0), 0, correctCellStyle);
							} else {
								setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
							}
						} catch (Exception ex) {
							setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
							ex.printStackTrace();
						}

						stdObj.setSectionId(df.formatCellValue(currentRow.getCell(1)));
						if (customFxcelValidator
								.numberValidation(mObject, "sectionId", checkNullandTrim(stdObj.getSectionId()), 4)
								.get("sectionId").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->3");
//							currentRow.getCell(1).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
						}

						stdObj.setRollNo(df.formatCellValue(currentRow.getCell(2)));
						if (customFxcelValidator
								.numberValidation(mObject, "rollNo", checkNullandTrim(stdObj.getRollNo()), 999)
								.get("rollNo").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->4");
//							currentRow.getCell(2).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(2), 2, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(2), 2, correctCellStyle);
						}

						stdObj.setStudentName(
								checkNull(df.formatCellValue(currentRow.getCell(3))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "studentName",
										checkNullandTrim(stdObj.getStudentName()))
								.get("studentName").get("status").equalsIgnoreCase("0")) {
							System.out.println("student name---->" + customFxcelValidator
									.stringNonSpecialValidation(mObject, "studentName",
											checkNullandTrim(stdObj.getStudentName()))
									.get("studentName").get("status").equalsIgnoreCase("0"));
							System.out.println("In set color---->5");
//							currentRow.getCell(3).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(3), 3, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(3), 3, correctCellStyle);
						}

						stdObj.setGender(df.formatCellValue(currentRow.getCell(4)));
						if (customFxcelValidator
								.numberValidation(mObject, "gender", checkNullandTrim(stdObj.getGender()), 3)
								.get("gender").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(4).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
						}

						DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");

						if (currentRow.getCell(5) != null &&  checkNull(currentRow.getCell(5).toString()) !="") {
							java.util.Date d = null;
							try {
							d = currentRow.getCell(5).getDateCellValue();
							stdObj.setStudentDob(dff.format(d));
							if (customFxcelValidator
									.dateValidation(mObject, "studentDob", checkNullandTrim(stdObj.getStudentDob()))
									.get("studentDob").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(5).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
								currentRow.getCell(5).setCellValue(dff.format(d));
							}else {
								setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
								currentRow.getCell(5).setCellValue(dff.format(d));
							}
							}catch(Exception ex) {
								setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
//								currentRow.getCell(5).setCellValue(dff.format(d));
								ex.printStackTrace();
							}
						} else {
							setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
							currentRow.getCell(5).setCellValue(dff.format(currentRow.getCell(5).getDateCellValue()));
						}

						stdObj.setMotherName(
								checkNull(df.formatCellValue(currentRow.getCell(6))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "motherName",
										checkNullandTrim(stdObj.getMotherName()))
								.get("motherName").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(6).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(6), 6, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(6), 6, correctCellStyle);
						}

						stdObj.setFatherName(
								checkNull(df.formatCellValue(currentRow.getCell(7))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "fatherName",
										checkNullandTrim(stdObj.getFatherName()))
								.get("fatherName").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(7).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(7), 7, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(7), 7, correctCellStyle);
						}

						stdObj.setGuardianName(
								checkNull(df.formatCellValue(currentRow.getCell(8))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "guardianName",
										checkNullandTrim(stdObj.getGuardianName()))
								.get("guardianName").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(8).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(8), 8, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(8), 8, correctCellStyle);
						}

						stdObj.setAadhaarNo(df.formatCellValue(currentRow.getCell(9)));
						if (customFxcelValidator
								.adharValidation(mObject, "aadhaarNo", checkNullandTrim(stdObj.getAadhaarNo()))
								.get("aadhaarNo").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(9).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(9), 9, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(9), 9, correctCellStyle);
						}

						if (!stdObj.getAadhaarNo().equalsIgnoreCase("999999999999")) {
							stdObj.setNameAsAadhaar(
									checkNull(df.formatCellValue(currentRow.getCell(10))).replaceAll("\\s+", " "));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "nameAsAadhaar",
											checkNullandTrim(stdObj.getNameAsAadhaar()))
									.get("nameAsAadhaar").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(10).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(10), 10, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(10), 10, correctCellStyle);
							}
						}

						stdObj.setAddress(
								checkNull(df.formatCellValue(currentRow.getCell(11))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringValidation(mObject, "address", checkNullandTrim(stdObj.getAddress()))
								.get("address").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(11).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(11), 11, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(11), 11, correctCellStyle);
						}

						stdObj.setPincode(df.formatCellValue(currentRow.getCell(12)));
						if (customFxcelValidator
								.pincodeValidation(mObject, "pincode", checkNullandTrim(stdObj.getPincode()))
								.get("pincode").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(12).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(12), 12, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(12), 12, correctCellStyle);
						}

						stdObj.setMobileNo_1(df.formatCellValue(currentRow.getCell(13)));
						if (customFxcelValidator
								.mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stdObj.getMobileNo_1()))
								.get("mobileNo_1").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(13).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(13), 13, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(13), 13, correctCellStyle);
						}

						stdObj.setMobileNo_2(df.formatCellValue(currentRow.getCell(14)));
						if (customFxcelValidator
								.mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stdObj.getMobileNo_2()))
								.get("mobileNo_2").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(14).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(14), 14, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(14), 14, correctCellStyle);
						}

						stdObj.setEmailId(df.formatCellValue(currentRow.getCell(15)));
						if (customFxcelValidator
								.emailValidation(mObject, "emailId", checkNullandTrim(stdObj.getEmailId()))
								.get("emailId").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(15).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(15), 15, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(15), 15, correctCellStyle);
						}

						stdObj.setMotherTongue(df.formatCellValue(currentRow.getCell(16)));
						if (stdObj.getMotherTongue() != null && !stdObj.getMotherTongue().equalsIgnoreCase("null")
								&& mtongObj.get(stdObj.getMotherTongue()) != null
								&& !mtongObj.get(stdObj.getMotherTongue())) {
//							currentRow.getCell(16).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(16), 16, correctCellStyle);
						}
//						if (customFxcelValidator
//								.numberValidation(mObject, "motherTongue",
//										checkNullandTrim(stdObj.getMotherTongue()), 999)
//								.get("motherTongue").get("status").equalsIgnoreCase("0")) {
//							System.out.println("In set color---->18");
//							currentRow.getCell(16).setCellStyle(cellStyle);
//						} 

						stdObj.setSocCatId(df.formatCellValue(currentRow.getCell(17)));
						if (customFxcelValidator
								.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 4)
								.get("socCatId").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(17).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(17), 17, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(17), 17, correctCellStyle);
						}

						stdObj.setMinorityId(df.formatCellValue(currentRow.getCell(18)));
						if (customFxcelValidator
								.numberValidation(mObject, "minorityId", checkNullandTrim(stdObj.getMinorityId()), 7)
								.get("minorityId").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(18).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(18), 18, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(18), 18, correctCellStyle);
						}

						stdObj.setIsBplYn(df.formatCellValue(currentRow.getCell(19)));
						if (customFxcelValidator
								.numberValidation(mObject, "isBplYn", checkNullandTrim(stdObj.getIsBplYn()), 2)
								.get("isBplYn").get("status").equalsIgnoreCase("0")) {

//							currentRow.getCell(19).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(19), 19, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(19), 19, correctCellStyle);
						}

						stdObj.setAayBplYn(df.formatCellValue(currentRow.getCell(20)));
						if(stdObj.getIsBplYn() !=null && stdObj.getIsBplYn() !="" && !stdObj.getIsBplYn().equalsIgnoreCase("null") && stdObj.getIsBplYn().equalsIgnoreCase("1")) {
						if (customFxcelValidator
								.numberValidation(mObject, "aayBplYn", checkNullandTrim(stdObj.getAayBplYn()), 2)
								.get("aayBplYn").get("status").equalsIgnoreCase("0")) {
//							System.out.println("In set color---->22");
//							currentRow.getCell(20).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(20), 20, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(20), 20, correctCellStyle);
						}
						}else {
							setCellColors(currentRow, currentRow.getCell(20), 20, cellStyle);
						}

						stdObj.setEwsYn(df.formatCellValue(currentRow.getCell(21)));
						if (customFxcelValidator
								.numberValidation(mObject, "ewsYn", checkNullandTrim(stdObj.getEwsYn()), 2).get("ewsYn")
								.get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(21).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(21), 21, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(21), 21, correctCellStyle);
						}

						stdObj.setCwsnYn(df.formatCellValue(currentRow.getCell(22)));
						if (customFxcelValidator
								.numberValidation(mObject, "cwsnYn", checkNullandTrim(stdObj.getCwsnYn()), 2)
								.get("cwsnYn").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(22).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(22), 22, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(22), 22, correctCellStyle);
						}

						stdObj.setImpairmentType(df.formatCellValue(currentRow.getCell(23)));
						if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.jsonValidation(mObject, "impairmentType",
											checkNullandTrim(stdObj.getImpairmentType()))
									.get("impairmentType").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(23).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(23), 23, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(23), 23, correctCellStyle);
							}
						} else {
							customFxcelValidator.blankAndTrueValidation(mObject, "impairmentType", "");
							setCellColors(currentRow, currentRow.getCell(23), 23, correctCellStyle);
						}

						stdObj.setNatIndYn(df.formatCellValue(currentRow.getCell(24)));
						if (customFxcelValidator
								.numberValidation(mObject, "natIndYn", checkNullandTrim(stdObj.getNatIndYn()), 2)
								.get("natIndYn").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->26");
//							currentRow.getCell(24).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(24), 24, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(24), 24, correctCellStyle);
						}

						stdObj.setOoscYn(df.formatCellValue(currentRow.getCell(25)));
						if (customFxcelValidator
								.numberValidation(mObject, "ooscYn", checkNullandTrim(stdObj.getOoscYn()), 2)
								.get("ooscYn").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->27");
//							currentRow.getCell(25).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(25), 25, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(25), 25, correctCellStyle);
						}

						stdObj.setAdmnNumber(df.formatCellValue(currentRow.getCell(26)));
						if (customFxcelValidator
								.admisionNumberValidation(mObject, "admnNumber",
										checkNullandTrim(stdObj.getAdmnNumber()))
								.get("admnNumber").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->28");
//							currentRow.getCell(26).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(26), 26, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(26), 26, correctCellStyle);
						}

						if (currentRow.getCell(27) != null && checkNull(currentRow.getCell(27).toString()) !="") {
							java.util.Date admisionDate=null;
							try {
							admisionDate = currentRow.getCell(27).getDateCellValue();
							stdObj.setAdmnStartDate(dff.format(admisionDate));
							if (customFxcelValidator
									.dateValidation(mObject, "admnStartDate",
											checkNullandTrim(stdObj.getAdmnStartDate()))
									.get("admnStartDate").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->29");
//							currentRow.getCell(27).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
								currentRow.getCell(27).setCellValue(admisionDate);
							}else {
								setCellColors(currentRow, currentRow.getCell(27), 27, correctCellStyle);
								currentRow.getCell(27).setCellValue(admisionDate);
							}
							}catch(Exception ex) {
								setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
								currentRow.getCell(27).setCellValue(admisionDate);
								ex.printStackTrace();
							}
						} else {
							setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
							currentRow.getCell(27).setCellValue(currentRow.getCell(27).getDateCellValue());
						}

						stdObj.setAcdemicStream(df.formatCellValue(currentRow.getCell(28)));

						System.out.println("In student stream--->" + stdObj.getAcdemicStream());
						if (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
								|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) {
							if (customFxcelValidator
									.numberValidation(mObject, "acdemicStream",
											checkNullandTrim(stdObj.getAcdemicStream()), 5)
									.get("acdemicStream").get("status").equalsIgnoreCase("0")) {
								System.out.println("Stream coloring");
//								currentRow.getCell(28).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(28), 28, correctCellStyle);
							}
						}

						stdObj.setEnrStatusPy(df.formatCellValue(currentRow.getCell(29)));

						if (customFxcelValidator
								.numberValidation(mObject, "enrStatusPy", checkNullandTrim(stdObj.getEnrStatusPy()), 4)
								.get("enrStatusPy").get("status").equalsIgnoreCase("0")) {
							System.out.println("In set color---->31");
//							currentRow.getCell(29).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
						}

						stdObj.setClassPy(df.formatCellValue(currentRow.getCell(30)));

						if (stdObj.getEnrStatusPy().equalsIgnoreCase("1")
								|| stdObj.getEnrStatusPy().equalsIgnoreCase("2")) {
							if ((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("1"))
									&& (stdObj.getClassPy().equalsIgnoreCase("0")
											|| stdObj.getClassPy().equalsIgnoreCase("PP"))) {

							} else {
								try {
									int stClass = Integer.parseInt(stdObj.getClassId());
									if (stClass > 0 && stClass <= 12
											&& (stClass == Integer.parseInt(stdObj.getClassPy())
													|| stClass == Integer.parseInt(stdObj.getClassPy()) + 1)) {
										setCellColors(currentRow, currentRow.getCell(30), 30, correctCellStyle);
									} else {
//										currentRow.getCell(30).setCellStyle(cellStyle);
										setCellColors(currentRow, currentRow.getCell(30), 30, cellStyle);
									}
								} catch (Exception ex) {
//									currentRow.getCell(30).setCellStyle(cellStyle);
									setCellColors(currentRow, currentRow.getCell(30), 30, cellStyle);
									ex.printStackTrace();
								}
							}
						}

						stdObj.setEnrTypeCy(df.formatCellValue(currentRow.getCell(31)));
						if (Integer
								.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) == 5) {
							if (customFxcelValidator
									.numberValidation(mObject, "enrTypeCy", checkNullandTrim(stdObj.getEnrTypeCy()), 5)
									.get("enrTypeCy").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(31).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(31), 31, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(31), 31, correctCellStyle);
							}
						} else {
							customFxcelValidator.blankAndTrueValidation(mObject, "enrTypeCy", "");
							setCellColors(currentRow, currentRow.getCell(31), 31, correctCellStyle);
						}

						stdObj.setExamAppearedPyYn(df.formatCellValue(currentRow.getCell(32)));
						if (customFxcelValidator
								.numberValidation(mObject, "examAppearedPyYn",
										checkNullandTrim(stdObj.getExamAppearedPyYn()), 2)
								.get("examAppearedPyYn").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(32).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(32), 32, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(32), 32, correctCellStyle);
						}

						stdObj.setExamResultPy(df.formatCellValue(currentRow.getCell(33)));
						if (customFxcelValidator.numberValidation(mObject, "examResultPy",
								checkNullandTrim(stdObj.getExamResultPy()), 4).get("examResultPy").get("status")
								.equalsIgnoreCase("0")) {
//							currentRow.getCell(33).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(33), 33, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(33), 33, correctCellStyle);
						}

						stdObj.setExamMarksPy(df.formatCellValue(currentRow.getCell(34)));
						if (customFxcelValidator.numberValidation(mObject, "examMarksPy",
								checkNullandTrim(stdObj.getExamMarksPy()), 100).get("examMarksPy").get("status")
								.equalsIgnoreCase("0")) {
//							currentRow.getCell(34).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(34), 34, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
						}

						stdObj.setAttendencePy(df.formatCellValue(currentRow.getCell(35)));
						if (customFxcelValidator.numberValidation(mObject, "attendencePy",
								checkNullandTrim(stdObj.getAttendencePy()), 365).get("attendencePy").get("status")
								.equalsIgnoreCase("0")) {
//							currentRow.getCell(35).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(35), 35, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(35), 35, correctCellStyle);
						}

						stdObj.setUniformFacProvided(df.formatCellValue(currentRow.getCell(36)));
						if (customFxcelValidator
								.numberValidation(mObject, "uniformFacProvided",
										checkNullandTrim(stdObj.getUniformFacProvided()), 2)
								.get("uniformFacProvided").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(36).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(36), 36, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(36), 36, correctCellStyle);
						}

						stdObj.setTextBoxFacProvided(df.formatCellValue(currentRow.getCell(37)));
						if (customFxcelValidator
								.numberValidation(mObject, "textBoxFacProvided",
										checkNullandTrim(stdObj.getTextBoxFacProvided()), 2)
								.get("textBoxFacProvided").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(37).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(37), 37, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(37), 37, correctCellStyle);
						}

						stdObj.setCentrlSchlrshpYn(df.formatCellValue(currentRow.getCell(38)));
						if (customFxcelValidator
								.numberValidation(mObject, "centrlSchlrshpYn",
										checkNullandTrim(stdObj.getCentrlSchlrshpYn()), 2)
								.get("centrlSchlrshpYn").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(38).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(38), 38, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(38), 38, correctCellStyle);
						}

						stdObj.setCentrlSchlrshpId(df.formatCellValue(currentRow.getCell(39)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "centrlSchlrshpId",
											checkNullandTrim(stdObj.getCentrlSchlrshpId()), 15)
									.get("centrlSchlrshpId").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(39).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(39), 39, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(39), 39, correctCellStyle);
							}
						} else {
							currentRow.getCell(41).setCellValue("");
							setCellColors(currentRow, currentRow.getCell(41), 41, correctCellStyle);
						}

						stdObj.setStateSchlrshpYn(df.formatCellValue(currentRow.getCell(40)));
						if (customFxcelValidator
								.numberValidation(mObject, "stateSchlrshpYn",
										checkNullandTrim(stdObj.getStateSchlrshpYn()), 2)
								.get("stateSchlrshpYn").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(40).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(40), 40, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(40), 40, correctCellStyle);
						}

						stdObj.setOtherSchlrshpYn(df.formatCellValue(currentRow.getCell(41)));
						if (customFxcelValidator
								.numberValidation(mObject, "otherSchlrshpYn",
										checkNullandTrim(stdObj.getOtherSchlrshpYn()), 2)
								.get("otherSchlrshpYn").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(41).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(41), 41, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(41), 41, correctCellStyle);
						}

						stdObj.setSchlrshpAmount(df.formatCellValue(currentRow.getCell(42)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "schlrshpAmount",
											checkNullandTrim(stdObj.getSchlrshpAmount()), 50000)
									.get("schlrshpAmount").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(42).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(42), 42, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(42), 42, correctCellStyle);
							}
						} else {
							currentRow.getCell(42).setCellValue("");
						}

						stdObj.setFacProvidedCwsn(df.formatCellValue(currentRow.getCell(43)));
						if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "facProvidedCwsn",
											checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
									.get("facProvidedCwsn").get("status").equalsIgnoreCase("0")) {
//								currentRow.getCell(43).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(43), 43, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(43), 43, correctCellStyle);
							}
						}

						stdObj.setScrndFrSld(df.formatCellValue(currentRow.getCell(44)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator.numberValidation(mObject, "scrndFrSld",
									checkNullandTrim(stdObj.getScrndFrSld()), 2).get("scrndFrSld").get("status")
									.equalsIgnoreCase("0")) {
//								currentRow.getCell(44).setCellStyle(cellStyle);
								setCellColors(currentRow, currentRow.getCell(44), 44, cellStyle);
							}else {
								setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
							}
						}

						stdObj.setSldType(df.formatCellValue(currentRow.getCell(45)));
						if (customFxcelValidator
								.numberValidation(mObject, "sldType", checkNullandTrim(stdObj.getSldType()), 3)
								.get("sldType").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(45).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(45), 45, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(45), 45, correctCellStyle);
						}

						stdObj.setScrndFrAsd(df.formatCellValue(currentRow.getCell(46)));
						if (customFxcelValidator
								.numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stdObj.getScrndFrAsd()), 2)
								.get("scrndFrAsd").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(46).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(46), 46, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(46), 46, correctCellStyle);
						}

						stdObj.setScrndFrAdhd(df.formatCellValue(currentRow.getCell(47)));
						if (customFxcelValidator
								.numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stdObj.getScrndFrAdhd()), 2)
								.get("scrndFrAdhd").get("status").equalsIgnoreCase("0")) {
//							currentRow.getCell(47).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(47), 47, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(47), 47, correctCellStyle);
						}

						stdObj.setIsEcActivity(df.formatCellValue(currentRow.getCell(48)));
						if (customFxcelValidator.numberValidation(mObject, "isEcActivity",
								checkNullandTrim(stdObj.getIsEcActivity()), 2).get("isEcActivity").get("status")
								.equalsIgnoreCase("0")) {
//							currentRow.getCell(48).setCellStyle(cellStyle);
							setCellColors(currentRow, currentRow.getCell(48), 48, cellStyle);
						}else {
							setCellColors(currentRow, currentRow.getCell(48), 48, correctCellStyle);
						}

						if (currentRow.getRowNum() > 6) {
							stdList.add(stdObj);
						}

					} else {

						stdObj.setUdisecode(df.formatCellValue(currentRow.getCell(0)));
						if (customFxcelValidator.numberValidation(mObject, "udiseCode",
								checkNullandTrim(stdObj.getUdisecode()), 99999999999L).get("udiseCode").get("status")
								.equalsIgnoreCase("0")) {
							currentRow.getCell(0).setCellStyle(cellStyle);
						}

						stdObj.setClassId(df.formatCellValue(currentRow.getCell(2)));
						if (customFxcelValidator
								.numberValidation(mObject, "classId", checkNullandTrim(stdObj.getClassId()), 12)
								.get("classId").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(0).setCellStyle(cellStyle);
						}

						stdObj.setSectionId(df.formatCellValue(currentRow.getCell(3)));
						if (customFxcelValidator
								.numberValidation(mObject, "sectionId", checkNullandTrim(stdObj.getSectionId()), 4)
								.get("sectionId").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(3).setCellStyle(cellStyle);
						}

						stdObj.setRollNo(df.formatCellValue(currentRow.getCell(4)));
						if (customFxcelValidator
								.numberValidation(mObject, "rollNo", checkNullandTrim(stdObj.getRollNo()), 999)
								.get("rollNo").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(4).setCellStyle(cellStyle);
						}

						stdObj.setStudentName(
								checkNull(df.formatCellValue(currentRow.getCell(5))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "studentName",
										checkNullandTrim(stdObj.getStudentName()))
								.get("studentName").get("status").equalsIgnoreCase("0")) {
							System.out.println("student name---->" + customFxcelValidator
									.stringNonSpecialValidation(mObject, "studentName",
											checkNullandTrim(stdObj.getStudentName()))
									.get("studentName").get("status").equalsIgnoreCase("0"));
							currentRow.getCell(5).setCellStyle(cellStyle);
						}

						stdObj.setGender(df.formatCellValue(currentRow.getCell(6)));
						if (customFxcelValidator
								.numberValidation(mObject, "gender", checkNullandTrim(stdObj.getGender()), 3)
								.get("gender").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(6).setCellStyle(cellStyle);
						}

						stdObj.setStudentDob(df.formatCellValue(currentRow.getCell(7)));
						if (customFxcelValidator
								.dateValidation(mObject, "studentDob", checkNullandTrim(stdObj.getStudentDob()))
								.get("studentDob").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(7).setCellStyle(cellStyle);
						}

						stdObj.setMotherName(
								checkNull(df.formatCellValue(currentRow.getCell(8))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "motherName",
										checkNullandTrim(stdObj.getMotherName()))
								.get("motherName").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(8).setCellStyle(cellStyle);
						}

						stdObj.setFatherName(
								checkNull(df.formatCellValue(currentRow.getCell(9))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "fatherName",
										checkNullandTrim(stdObj.getFatherName()))
								.get("fatherName").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(9).setCellStyle(cellStyle);
						}

						stdObj.setGuardianName(
								checkNull(df.formatCellValue(currentRow.getCell(10))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringNonSpecialValidation(mObject, "guardianName",
										checkNullandTrim(stdObj.getGuardianName()))
								.get("guardianName").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(10).setCellStyle(cellStyle);
						}

						stdObj.setAadhaarNo(df.formatCellValue(currentRow.getCell(11)));
						if (customFxcelValidator
								.adharValidation(mObject, "aadhaarNo", checkNullandTrim(stdObj.getAadhaarNo()))
								.get("aadhaarNo").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(11).setCellStyle(cellStyle);
						}

						if (!stdObj.getAadhaarNo().equalsIgnoreCase("999999999999")) {
							stdObj.setNameAsAadhaar(
									checkNull(df.formatCellValue(currentRow.getCell(12))).replaceAll("\\s+", " "));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "nameAsAadhaar",
											checkNullandTrim(stdObj.getNameAsAadhaar()))
									.get("nameAsAadhaar").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(12).setCellStyle(cellStyle);
							}
						}

						stdObj.setAddress(
								checkNull(df.formatCellValue(currentRow.getCell(13))).replaceAll("\\s+", " "));
						if (customFxcelValidator
								.stringValidation(mObject, "address", checkNullandTrim(stdObj.getAddress()))
								.get("address").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(13).setCellStyle(cellStyle);
						}

						stdObj.setPincode(df.formatCellValue(currentRow.getCell(14)));
						if (customFxcelValidator
								.pincodeValidation(mObject, "pincode", checkNullandTrim(stdObj.getPincode()))
								.get("pincode").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(14).setCellStyle(cellStyle);
						}

						stdObj.setMobileNo_1(df.formatCellValue(currentRow.getCell(15)));
						if (customFxcelValidator
								.mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stdObj.getMobileNo_1()))
								.get("mobileNo_1").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(15).setCellStyle(cellStyle);
						}

						stdObj.setMobileNo_2(df.formatCellValue(currentRow.getCell(16)));
						if (customFxcelValidator
								.mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stdObj.getMobileNo_2()))
								.get("mobileNo_2").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(16).setCellStyle(cellStyle);
						}

						stdObj.setEmailId(df.formatCellValue(currentRow.getCell(17)));
						if (customFxcelValidator
								.emailValidation(mObject, "emailId", checkNullandTrim(stdObj.getEmailId()))
								.get("emailId").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(17).setCellStyle(cellStyle);
						}

						stdObj.setMotherTongue(df.formatCellValue(currentRow.getCell(18)));
						if (!mtongObj.get(stdObj.getMotherTongue())) {
							currentRow.getCell(16).setCellStyle(cellStyle);
						}
//						if (customFxcelValidator
//								.numberValidation(mObject, "motherTongue",
//										checkNullandTrim(stdObj.getMotherTongue()), 999)
//								.get("motherTongue").get("status").equalsIgnoreCase("0")) {
//							System.out.println("In set color---->18");
//							currentRow.getCell(18).setCellStyle(cellStyle);
//						} 

						stdObj.setSocCatId(df.formatCellValue(currentRow.getCell(19)));
						if (customFxcelValidator
								.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 4)
								.get("socCatId").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(19).setCellStyle(cellStyle);
						}

						stdObj.setMinorityId(df.formatCellValue(currentRow.getCell(20)));
						if (customFxcelValidator
								.numberValidation(mObject, "minorityId", checkNullandTrim(stdObj.getMinorityId()), 7)
								.get("minorityId").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(20).setCellStyle(cellStyle);
						}

						stdObj.setIsBplYn(df.formatCellValue(currentRow.getCell(21)));
						if (customFxcelValidator
								.numberValidation(mObject, "isBplYn", checkNullandTrim(stdObj.getIsBplYn()), 2)
								.get("isBplYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(21).setCellStyle(cellStyle);
						}

						stdObj.setAayBplYn(df.formatCellValue(currentRow.getCell(22)));
						if (customFxcelValidator
								.numberValidation(mObject, "aayBplYn", checkNullandTrim(stdObj.getAayBplYn()), 2)
								.get("aayBplYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(22).setCellStyle(cellStyle);
						}

						stdObj.setEwsYn(df.formatCellValue(currentRow.getCell(23)));
						if (customFxcelValidator
								.numberValidation(mObject, "ewsYn", checkNullandTrim(stdObj.getEwsYn()), 2).get("ewsYn")
								.get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(23).setCellStyle(cellStyle);
						}

						stdObj.setCwsnYn(df.formatCellValue(currentRow.getCell(24)));
						if (customFxcelValidator
								.numberValidation(mObject, "cwsnYn", checkNullandTrim(stdObj.getCwsnYn()), 2)
								.get("cwsnYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(24).setCellStyle(cellStyle);
						}

						stdObj.setImpairmentType(df.formatCellValue(currentRow.getCell(25)));
						if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.jsonValidation(mObject, "impairmentType",
											checkNullandTrim(stdObj.getImpairmentType()))
									.get("impairmentType").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(25).setCellStyle(cellStyle);
							}
						} else {
							customFxcelValidator.blankAndTrueValidation(mObject, "impairmentType", "");
						}

						stdObj.setNatIndYn(df.formatCellValue(currentRow.getCell(26)));
						if (customFxcelValidator
								.numberValidation(mObject, "natIndYn", checkNullandTrim(stdObj.getNatIndYn()), 2)
								.get("natIndYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(26).setCellStyle(cellStyle);
						}

						stdObj.setOoscYn(df.formatCellValue(currentRow.getCell(27)));
						if (customFxcelValidator
								.numberValidation(mObject, "ooscYn", checkNullandTrim(stdObj.getOoscYn()), 2)
								.get("ooscYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(27).setCellStyle(cellStyle);
						}

						stdObj.setAdmnNumber(df.formatCellValue(currentRow.getCell(28)));
						if (customFxcelValidator
								.admisionNumberValidation(mObject, "admnNumber",
										checkNullandTrim(stdObj.getAdmnNumber()))
								.get("admnNumber").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(28).setCellStyle(cellStyle);
						}

						stdObj.setAdmnStartDate(df.formatCellValue(currentRow.getCell(29)));
						if (customFxcelValidator
								.dateValidation(mObject, "admnStartDate", checkNullandTrim(stdObj.getAdmnStartDate()))
								.get("admnStartDate").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(29).setCellStyle(cellStyle);
						}

						stdObj.setAcdemicStream(df.formatCellValue(currentRow.getCell(30)));

						System.out.println("In student stream--->" + stdObj.getAcdemicStream());
						if (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
								|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) {
							if (customFxcelValidator
									.numberValidation(mObject, "acdemicStream",
											checkNullandTrim(stdObj.getAcdemicStream()), 5)
									.get("acdemicStream").get("status").equalsIgnoreCase("0")) {
								System.out.println("Stream coloring");
								currentRow.getCell(30).setCellStyle(cellStyle);
							}
						}

						stdObj.setEnrStatusPy(df.formatCellValue(currentRow.getCell(31)));

						if (customFxcelValidator
								.numberValidation(mObject, "enrStatusPy", checkNullandTrim(stdObj.getEnrStatusPy()), 4)
								.get("enrStatusPy").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(31).setCellStyle(cellStyle);
						}

						stdObj.setClassPy(df.formatCellValue(currentRow.getCell(32)));

						if (stdObj.getEnrStatusPy().equalsIgnoreCase("1")
								|| stdObj.getEnrStatusPy().equalsIgnoreCase("2")) {
							if (stdObj.getClassId().equalsIgnoreCase("0") && (stdObj.getClassPy().equalsIgnoreCase("0")
									|| stdObj.getClassPy().equalsIgnoreCase("PP"))) {

							} else {
								try {
									int stClass = Integer.parseInt(stdObj.getClassId());
									if (stClass > 0 && stClass <= 12
											&& (stClass == Integer.parseInt(stdObj.getClassPy())
													|| stClass == Integer.parseInt(stdObj.getClassPy()) + 1)) {
									} else {
										currentRow.getCell(32).setCellStyle(cellStyle);
									}
								} catch (Exception ex) {
									currentRow.getCell(32).setCellStyle(cellStyle);
									ex.printStackTrace();
								}
							}
						}

						stdObj.setEnrTypeCy(df.formatCellValue(currentRow.getCell(33)));
						if (Integer
								.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) == 5) {
							if (customFxcelValidator
									.numberValidation(mObject, "enrTypeCy", checkNullandTrim(stdObj.getEnrTypeCy()), 5)
									.get("enrTypeCy").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(33).setCellStyle(cellStyle);
							}
						} else {
							customFxcelValidator.blankAndTrueValidation(mObject, "enrTypeCy", "");
						}

						stdObj.setExamAppearedPyYn(df.formatCellValue(currentRow.getCell(34)));
						if (customFxcelValidator
								.numberValidation(mObject, "examAppearedPyYn",
										checkNullandTrim(stdObj.getExamAppearedPyYn()), 2)
								.get("examAppearedPyYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(34).setCellStyle(cellStyle);
						}

						stdObj.setExamResultPy(df.formatCellValue(currentRow.getCell(35)));
						if (customFxcelValidator.numberValidation(mObject, "examResultPy",
								checkNullandTrim(stdObj.getExamResultPy()), 4).get("examResultPy").get("status")
								.equalsIgnoreCase("0")) {
							currentRow.getCell(35).setCellStyle(cellStyle);
						}

						stdObj.setExamMarksPy(df.formatCellValue(currentRow.getCell(36)));
						if (customFxcelValidator.numberValidation(mObject, "examMarksPy",
								checkNullandTrim(stdObj.getExamMarksPy()), 100).get("examMarksPy").get("status")
								.equalsIgnoreCase("0")) {
							currentRow.getCell(36).setCellStyle(cellStyle);
						}

						stdObj.setAttendencePy(df.formatCellValue(currentRow.getCell(37)));
						if (customFxcelValidator.numberValidation(mObject, "attendencePy",
								checkNullandTrim(stdObj.getAttendencePy()), 365).get("attendencePy").get("status")
								.equalsIgnoreCase("0")) {
							currentRow.getCell(37).setCellStyle(cellStyle);
						}

						stdObj.setUniformFacProvided(df.formatCellValue(currentRow.getCell(38)));
						if (customFxcelValidator
								.numberValidation(mObject, "uniformFacProvided",
										checkNullandTrim(stdObj.getUniformFacProvided()), 2)
								.get("uniformFacProvided").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(38).setCellStyle(cellStyle);
						}

						stdObj.setTextBoxFacProvided(df.formatCellValue(currentRow.getCell(39)));
						if (customFxcelValidator
								.numberValidation(mObject, "textBoxFacProvided",
										checkNullandTrim(stdObj.getTextBoxFacProvided()), 2)
								.get("textBoxFacProvided").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(39).setCellStyle(cellStyle);
						}

						stdObj.setCentrlSchlrshpYn(df.formatCellValue(currentRow.getCell(40)));
						if (customFxcelValidator
								.numberValidation(mObject, "centrlSchlrshpYn",
										checkNullandTrim(stdObj.getCentrlSchlrshpYn()), 2)
								.get("centrlSchlrshpYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(40).setCellStyle(cellStyle);
						}

						stdObj.setCentrlSchlrshpId(df.formatCellValue(currentRow.getCell(41)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "centrlSchlrshpId",
											checkNullandTrim(stdObj.getCentrlSchlrshpId()), 15)
									.get("centrlSchlrshpId").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(41).setCellStyle(cellStyle);
							}
						} else {
							currentRow.getCell(41).setCellValue("");
						}

						stdObj.setStateSchlrshpYn(df.formatCellValue(currentRow.getCell(42)));
						if (customFxcelValidator
								.numberValidation(mObject, "stateSchlrshpYn",
										checkNullandTrim(stdObj.getStateSchlrshpYn()), 2)
								.get("stateSchlrshpYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(42).setCellStyle(cellStyle);
						}

						stdObj.setOtherSchlrshpYn(df.formatCellValue(currentRow.getCell(43)));
						if (customFxcelValidator
								.numberValidation(mObject, "otherSchlrshpYn",
										checkNullandTrim(stdObj.getOtherSchlrshpYn()), 2)
								.get("otherSchlrshpYn").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(43).setCellStyle(cellStyle);
						}

						stdObj.setSchlrshpAmount(df.formatCellValue(currentRow.getCell(44)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "schlrshpAmount",
											checkNullandTrim(stdObj.getSchlrshpAmount()), 50000)
									.get("schlrshpAmount").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(44).setCellStyle(cellStyle);
							}
						} else {
							currentRow.getCell(44).setCellValue("");
						}

						stdObj.setFacProvidedCwsn(df.formatCellValue(currentRow.getCell(45)));
						if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator
									.numberValidation(mObject, "facProvidedCwsn",
											checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
									.get("facProvidedCwsn").get("status").equalsIgnoreCase("0")) {
								currentRow.getCell(45).setCellStyle(cellStyle);
							}
						}

						stdObj.setScrndFrSld(df.formatCellValue(currentRow.getCell(46)));
						if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
								|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
							if (customFxcelValidator.numberValidation(mObject, "scrndFrSld",
									checkNullandTrim(stdObj.getScrndFrSld()), 2).get("scrndFrSld").get("status")
									.equalsIgnoreCase("0")) {
								currentRow.getCell(46).setCellStyle(cellStyle);
							}
						}

						stdObj.setSldType(df.formatCellValue(currentRow.getCell(47)));
						if (customFxcelValidator
								.numberValidation(mObject, "sldType", checkNullandTrim(stdObj.getSldType()), 3)
								.get("sldType").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(47).setCellStyle(cellStyle);
						}

						stdObj.setScrndFrAsd(df.formatCellValue(currentRow.getCell(48)));
						if (customFxcelValidator
								.numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stdObj.getScrndFrAsd()), 2)
								.get("scrndFrAsd").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(48).setCellStyle(cellStyle);
						}

						stdObj.setScrndFrAdhd(df.formatCellValue(currentRow.getCell(49)));
						if (customFxcelValidator
								.numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stdObj.getScrndFrAdhd()), 2)
								.get("scrndFrAdhd").get("status").equalsIgnoreCase("0")) {
							currentRow.getCell(49).setCellStyle(cellStyle);
						}

						stdObj.setIsEcActivity(df.formatCellValue(currentRow.getCell(50)));
						if (customFxcelValidator.numberValidation(mObject, "isEcActivity",
								checkNullandTrim(stdObj.getIsEcActivity()), 2).get("isEcActivity").get("status")
								.equalsIgnoreCase("0")) {
							currentRow.getCell(50).setCellStyle(cellStyle);
						}

						if (currentRow.getRowNum() > 6) {
							stdList.add(stdObj);
						}

					}
				} // if end
			} // while end

			File uploadedExcel1 = new File(userBucketPath + File.separator + map.get("schoolId") + File.separator
					+ String.valueOf(map.get("schoolId")) + "_validated." + "xlsm");
			FileOutputStream outFile = new FileOutputStream(uploadedExcel1);
			workbook.write(outFile);
			outFile.close();
			List<Map<String, HashMap<String, String>>> finalResponse = fileServiceImpl.uploadData(stdList,
					String.valueOf(map.get("userId")), request.getRemoteAddr(), String.valueOf(map.get("schoolId")),
					sObj, sectionMap, mtongObj, lowerSector, lowerSubSector, higherSector, higherSubSector);
			File uploadedResponse = new File(userBucketPath + File.separator + String.valueOf(map.get("schoolId"))
					+ File.separator + String.valueOf(map.get("schoolId")) + "." + "txt");
			try {
				PrintWriter printWriter = new PrintWriter(uploadedResponse);
				printWriter.print(objectMapper.writeValueAsString(finalResponse));
				printWriter.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out.println("finalResponse---->" + finalResponse);

			try {
				long statusCount = finalResponse.stream()
						.filter((e) -> e.get("finalStatus").get("status").equalsIgnoreCase("0")).count();

				if (statusCount > 0) {
					return ResponseEntity.ok(
							new FinalResponse("", "1", "Uploaded Successfully but Some data Error check and resolve"));
				} else {
					return ResponseEntity.ok(new FinalResponse("", "1", "Uploaded Successfully"));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(new FinalResponse("", "1", "Success"));

	}

	@RequestMapping(value = "/uploadData", method = RequestMethod.POST)
//	public ResponseEntity<?> saveSurplusDataBySchool(@RequestBody String data) throws Exception {
	public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile multifile,
			@RequestParam("udisecode") String udisecode, @RequestParam("userid") String userid,
			@RequestParam("stateId") String stateId, HttpServletRequest request) throws Exception {
//		System.out.println("Upload Data--->" + multifile);
//		System.out.println("udisecode--->" + udisecode);
//		System.out.println(udisecode + "_" + multifile.getInputStream());
		List<String> headersFromExcel = null;
		Integer numberOfCell = null;
		DataFormatter df = new DataFormatter();
		ObjectMapper objectMapper = new ObjectMapper();

		File cF = new File(userBucketPath + File.separator + udisecode);

		if (!cF.exists()) {
			cF.mkdir();
		}
		File oldFile = new File(userBucketPath + File.separator + udisecode + File.separator + "old");
		if (!oldFile.exists()) {
			oldFile.mkdir();
		}
		File uploadedExcel = new File(
				userBucketPath + File.separator + udisecode + File.separator + udisecode + "." + "xlsm");
		File uploadedResponse = new File(
				userBucketPath + File.separator + udisecode + File.separator + udisecode + "." + "txt");
		System.out.println("uploaded path--->" + uploadedExcel.toPath());
		if (uploadedExcel.exists()) {
			try {
				System.out.println("List of file--->" + (oldFile.list().length) / 2);
				Files.copy(uploadedExcel.toPath(),
						new File(userBucketPath + File.separator + udisecode + File.separator + "old" + File.separator
								+ udisecode + "_" + ((oldFile.list().length) / 2 + 1) + "." + "xlsm").toPath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				Files.copy(uploadedResponse.toPath(),
						new File(userBucketPath + File.separator + udisecode + File.separator + "old" + File.separator
								+ udisecode + "_" + ((oldFile.list().length) / 2 + 1) + "." + "txt").toPath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		File tempFile = new File(userBucketPath + File.separator + udisecode + File.separator + udisecode + ".xlsm");
		tempFile.createNewFile();
		uploadedResponse.createNewFile();
//	      try(InputStream is = multifile.getInputStream()) {
//	    	 System.out.println("Before copy");
//	        Files.copy(is, convFile.toPath()); 
//	      }
		try (OutputStream os = new FileOutputStream(tempFile)) {
			os.write(multifile.getBytes());
			os.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//	      multifile.transferTo(convFile);

		StaticReportBean sObj = null;
		try {
			UserExcelExporter excelExporter = new UserExcelExporter();
			sObj = excelExporter.getSchoolData(nativeRepository, udisecode);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

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

			System.out.println("get number of sheets---->" + workbook.getNumberOfSheets());

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
					System.out.println(
							"School Id---->" + df.formatCellValue(currentRow.getCell(51)) + "------" + udisecode);

					if (!df.formatCellValue(currentRow.getCell(51)).equalsIgnoreCase(udisecode)) {
						System.out.println("ready for exception");
						throw new GenericExceptionHandler("Template Unauthenticate", "100001", request.getRemoteAddr(),
								userid, udisecode);
					}
				}

				// Header Test
				if (currentRow.getRowNum() == 4) {
					System.out.println("Number of column-->" + currentRow.getPhysicalNumberOfCells());
					if (stateId.equalsIgnoreCase("116")) {
						numberOfCell = ConfigurableUtility.state116commonPhysicalColumn;

					} else {
						numberOfCell = ConfigurableUtility.commonPhysicalColumn;
					}

					if (currentRow.getPhysicalNumberOfCells() != numberOfCell) {
						throw new GenericExceptionHandler("Column Missing", "100002", request.getRemoteAddr(), userid,
								udisecode);
					}

					if (workbook.getNumberOfSheets() != 4) {
						throw new GenericExceptionHandler("Template Unauthenticated and excel has not proper sheet",
								"100004", request.getRemoteAddr(), userid, udisecode);
					}

					List<String> cellHeader = new ArrayList<String>();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						cellHeader.add(currentCell.getStringCellValue().replaceAll("\\s+", " "));
						System.out.println("Cell Header value--->" + currentCell.getStringCellValue());
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

					if (stateId.equalsIgnoreCase("116")) {
						headersFromExcel = ConfigurableUtility.state116CommomHeadersFromExcel;
					} else {
						headersFromExcel = ConfigurableUtility.commomHeadersFromExcel;
					}

					Integer result = null;
					try {
						System.out.println(cellHeader.size() + "---" + headersFromExcel.size());
//						System.out.println(cellHeader);
//						System.out.println(headerIndexMap.size()+"-----"+cellHeader.size());
//						
						for (int i = 0; i < cellHeader.size(); i++) {

							if (!cellHeader.get(i).equalsIgnoreCase(headersFromExcel.get(i))) {
								System.out.println(cellHeader.get(i) + "-----" + headersFromExcel.get(i));
							}

						}

						result = headersFromExcel.stream().map(headerIndexMap::get).reduce(-1,
								(x, hi) -> x < hi ? hi : cellHeader.size());
					} catch (Exception ex) {
//						ex.printStackTrace();
						throw new GenericExceptionHandler("Column Sequence changed or Missing column", "100003",
								request.getRemoteAddr(), userid, udisecode);
					}
				}
				CellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(IndexedColors.RED.index);
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				Map<String, HashMap<String, String>> mObject = new LinkedHashMap<String, HashMap<String, String>>();
				if (currentRow.getRowNum() > 4) {
//					Cell currentCell=currentRow.getCell(1).getCel
					int i = 0;
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("UDISE Code of School")) {
							stdObj.setUdisecode(df.formatCellValue(currentCell));
							if (customFxcelValidator.numberValidation(mObject, "udiseCode",
									checkNullandTrim(stdObj.getUdisecode()), 99999999999L).get("udiseCode")
									.get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->1");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Grade/Class")) {
							stdObj.setClassId(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "classId", checkNullandTrim(stdObj.getClassId()), 12)
									.get("classId").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->2");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}
						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("SECTION")) {
							stdObj.setSectionId(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "sectionId", checkNullandTrim(stdObj.getSectionId()), 4)
									.get("sectionId").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->3");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Roll no")) {
							stdObj.setRollNo(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "rollNo", checkNullandTrim(stdObj.getRollNo()), 999)
									.get("rollNo").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->4");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Name of Student")) {
							stdObj.setStudentName(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "studentName",
											checkNullandTrim(stdObj.getStudentName()))
									.get("studentName").get("status").equalsIgnoreCase("0")) {
								System.out.println("student name---->" + customFxcelValidator
										.stringNonSpecialValidation(mObject, "studentName",
												checkNullandTrim(stdObj.getStudentName()))
										.get("studentName").get("status").equalsIgnoreCase("0"));
								System.out.println("In set color---->5");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("GENDER")) {
							stdObj.setGender(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "gender", checkNullandTrim(stdObj.getGender()), 3)
									.get("gender").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->6");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("DOB (DD-MM-YYYY)")) {
							stdObj.setStudentDob(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.dateValidation(mObject, "studentDob", checkNullandTrim(stdObj.getStudentDob()))
									.get("studentDob").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->7");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOTHER NAME")) {
							stdObj.setMotherName(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "motherName",
											checkNullandTrim(stdObj.getMotherName()))
									.get("motherName").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->8");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("FATHER Name")) {
							stdObj.setFatherName(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "fatherName",
											checkNullandTrim(stdObj.getFatherName()))
									.get("fatherName").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->9");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("GUARDIAN'S NAME")) {
							stdObj.setGuardianName(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "guardianName",
											checkNullandTrim(stdObj.getGuardianName()))
									.get("guardianName").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->10");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Aadhaar No of Child :")) {
							stdObj.setAadhaarNo(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.adharValidation(mObject, "aadhaarNo", checkNullandTrim(stdObj.getAadhaarNo()))
									.get("aadhaarNo").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->11");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("NAME AS PER AADHAR")) {
							stdObj.setNameAsAadhaar(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringNonSpecialValidation(mObject, "nameAsAadhaar",
											checkNullandTrim(stdObj.getNameAsAadhaar()))
									.get("nameAsAadhaar").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->12");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ADDRESS")) {
							stdObj.setAddress(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.stringValidation(mObject, "address", checkNullandTrim(stdObj.getAddress()))
									.get("address").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->13");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("PINCODE")) {
							stdObj.setPincode(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.pincodeValidation(mObject, "pincode", checkNullandTrim(stdObj.getPincode()))
									.get("pincode").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->14");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOBILE NUMBER")) {
							stdObj.setMobileNo_1(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stdObj.getMobileNo_1()))
									.get("mobileNo_1").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->15");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("ALTERNATE MOBILE NUMBER")) {
							stdObj.setMobileNo_2(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stdObj.getMobileNo_2()))
									.get("mobileNo_2").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->16");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("EMAIL ID (STUDENT/PARENT/GUARDIAN)")) {
							stdObj.setEmailId(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.emailValidation(mObject, "emailId", checkNullandTrim(stdObj.getEmailId()))
									.get("emailId").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->17");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MOTHER TONGUE")) {
							stdObj.setMotherTongue(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "motherTongue",
											checkNullandTrim(stdObj.getMotherTongue()), 999)
									.get("motherTongue").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->18");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("SOCIAL CATEGORY")) {
							stdObj.setSocCatId(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 4)
									.get("socCatId").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->19");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("MINORITY GROUP")) {
							stdObj.setMinorityId(df.formatCellValue(currentCell));
							if (customFxcelValidator.numberValidation(mObject, "minorityId",
									checkNullandTrim(stdObj.getMinorityId()), 7).get("minorityId").get("status")
									.equalsIgnoreCase("0")) {
								System.out.println("In set color---->20");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("BPL BANEFICIARY")) {
							stdObj.setIsBplYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "isBplYn", checkNullandTrim(stdObj.getIsBplYn()), 2)
									.get("isBplYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->21");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("ANTYODAYA")) {
							stdObj.setAayBplYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "aayBplYn", checkNullandTrim(stdObj.getAayBplYn()), 2)
									.get("aayBplYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->22");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("BELONGS TO EWS/DISADVANTAGED GROUP")) {
							stdObj.setEwsYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "ewsYn", checkNullandTrim(stdObj.getEwsYn()), 2)
									.get("ewsYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->23");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("CWSN(YES/NO)")) {
							stdObj.setCwsnYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "cwsnYn", checkNullandTrim(stdObj.getCwsnYn()), 2)
									.get("cwsnYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->24");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("IMPAIRMENT TYPE")) {
							stdObj.setImpairmentType(df.formatCellValue(currentCell));
							if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
								if (customFxcelValidator
										.jsonValidation(mObject, "impairmentType",
												checkNullandTrim(stdObj.getImpairmentType()))
										.get("impairmentType").get("status").equalsIgnoreCase("0")) {
									System.out.println("In set color---->25");
									currentCell.setCellStyle(cellStyle);
								} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
								}
							} else {
								customFxcelValidator.blankAndTrueValidation(mObject, "impairmentType", "");
							}

						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("CHILD IS INDIAN NATIONAL")) {
							stdObj.setNatIndYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "natIndYn", checkNullandTrim(stdObj.getNatIndYn()), 2)
									.get("natIndYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->26");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("CHILD IS OUT-OF-SCHOOL-CHILD")) {
							stdObj.setOoscYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "ooscYn", checkNullandTrim(stdObj.getOoscYn()), 2)
									.get("ooscYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->27");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Admission Number")) {
							stdObj.setAdmnNumber(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.admisionNumberValidation(mObject, "admnNumber",
											checkNullandTrim(stdObj.getAdmnNumber()))
									.get("admnNumber").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->28");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("ADMISSION DATE (DD-MM-YYYY)")) {
							stdObj.setAdmnStartDate(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.dateValidation(mObject, "admnStartDate",
											checkNullandTrim(stdObj.getAdmnStartDate()))
									.get("admnStartDate").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->29");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("STUDENT STREAM (For higher secondary only)")) {
							stdObj.setAcdemicStream(df.formatCellValue(currentCell));

							System.out.println("In student stream--->" + stdObj.getAcdemicStream());
							if (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
									|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) {
								if (customFxcelValidator
										.numberValidation(mObject, "acdemicStream",
												checkNullandTrim(stdObj.getAcdemicStream()), 5)
										.get("acdemicStream").get("status").equalsIgnoreCase("0")) {
									System.out.println("In set color---->30");
									System.out.println("Stream coloring");
									currentCell.setCellStyle(cellStyle);
								} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
								}
							}

						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("PREVIOUS ACADEMIC SCHOOLING STATUS")) {
							stdObj.setEnrStatusPy(df.formatCellValue(currentCell));

							if (customFxcelValidator.numberValidation(mObject, "enrStatusPy",
									checkNullandTrim(stdObj.getEnrStatusPy()), 4).get("enrStatusPy").get("status")
									.equalsIgnoreCase("0")) {
								System.out.println("In set color---->31");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("CLASS STUDIES IN PREVIOUS ACADEMIC")) {
							stdObj.setClassPy(df.formatCellValue(currentCell));
//							if(customFxcelValidator.numberValidation(mObject,"classPy",checkNullandTrim(stdObj.getClassPy()),2).get("classPy").get("status").equalsIgnoreCase("0")) {
//								currentCell.setCellStyle(cellStyle);
//							}

							if (stdObj.getEnrStatusPy().equalsIgnoreCase("1")
									|| stdObj.getEnrStatusPy().equalsIgnoreCase("2")) {
								if (stdObj.getClassId().equalsIgnoreCase("0")
										&& (stdObj.getClassPy().equalsIgnoreCase("0")
												|| stdObj.getClassPy().equalsIgnoreCase("PP"))) {
//									customFxcelValidator.blankAndTrueValidation(mObject,"classPy",stdObj.getClassPy());
								} else {
									try {
										int stClass = Integer.parseInt(stdObj.getClassId());
										if (stClass > 0 && stClass <= 12
												&& (stClass == Integer.parseInt(stdObj.getClassPy())
														|| stClass == Integer.parseInt(stdObj.getClassPy()) + 1)) {
//											customFxcelValidator.blankAndTrueValidation(mObject,"classPy",stdObj.getClassPy());
										} else {
											currentCell.setCellStyle(cellStyle);
										}
									} catch (Exception ex) {
										currentCell.setCellStyle(cellStyle);
										ex.printStackTrace();
									}
								}
							} else {
//								customFxcelValidator.blankAndTrueValidation(mObject,"classPy","");
							}

						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("ADMITED/ ENROLLED UNDER")) {
							stdObj.setEnrTypeCy(df.formatCellValue(currentCell));
							if (Integer.parseInt(
									String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) == 5) {
								if (customFxcelValidator.numberValidation(mObject, "enrTypeCy",
										checkNullandTrim(stdObj.getEnrTypeCy()), 5).get("enrTypeCy").get("status")
										.equalsIgnoreCase("0")) {
									System.out.println("In set color---->33");
									currentCell.setCellStyle(cellStyle);
								} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
								}
							} else {
								customFxcelValidator.blankAndTrueValidation(mObject, "enrTypeCy", "");
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("APPEARED FOR EXAM IN PREVIOUS CLASS")) {
							stdObj.setExamAppearedPyYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "examAppearedPyYn",
											checkNullandTrim(stdObj.getExamAppearedPyYn()), 2)
									.get("examAppearedPyYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->34");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("RESULT FOR PREVIOUS EXAM")) {
							stdObj.setExamResultPy(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "examResultPy",
											checkNullandTrim(stdObj.getExamResultPy()), 4)
									.get("examResultPy").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->35");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("MARKS % OF PREVIOUS EXAM")) {
							stdObj.setExamMarksPy(df.formatCellValue(currentCell));
							if (customFxcelValidator.numberValidation(mObject, "examMarksPy",
									checkNullandTrim(stdObj.getExamMarksPy()), 100).get("examMarksPy").get("status")
									.equalsIgnoreCase("0")) {
								System.out.println("In set color---->36");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("CLASS ATTENDED DAYS")) {
							stdObj.setAttendencePy(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "attendencePy",
											checkNullandTrim(stdObj.getAttendencePy()), 365)
									.get("attendencePy").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->37");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Facility - Free Uniform")) {
							stdObj.setUniformFacProvided(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "uniformFacProvided",
											checkNullandTrim(stdObj.getUniformFacProvided()), 2)
									.get("uniformFacProvided").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->38");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Facility - Free TextBook")) {
							stdObj.setTextBoxFacProvided(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "textBoxFacProvided",
											checkNullandTrim(stdObj.getTextBoxFacProvided()), 2)
									.get("textBoxFacProvided").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->39");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Central Scholarship")) {
							stdObj.setCentrlSchlrshpYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "centrlSchlrshpYn",
											checkNullandTrim(stdObj.getCentrlSchlrshpYn()), 2)
									.get("centrlSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								System.out.println("In set color---->40");
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Name of Scholarship (provide code only)")) {
							stdObj.setCentrlSchlrshpId(df.formatCellValue(currentCell));
							if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")) {
								if (customFxcelValidator
										.numberValidation(mObject, "centrlSchlrshpId",
												checkNullandTrim(stdObj.getCentrlSchlrshpId()), 15)
										.get("centrlSchlrshpId").get("status").equalsIgnoreCase("0")) {
									currentCell.setCellStyle(cellStyle);
								}
							} else {
								currentCell.setCellValue("");
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("State Scholarship")) {
							stdObj.setStateSchlrshpYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "stateSchlrshpYn",
											checkNullandTrim(stdObj.getStateSchlrshpYn()), 2)
									.get("stateSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Other Scholarship")) {
							stdObj.setOtherSchlrshpYn(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "otherSchlrshpYn",
											checkNullandTrim(stdObj.getOtherSchlrshpYn()), 2)
									.get("otherSchlrshpYn").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							}

						}

						if (headersFromExcel.get(currentCell.getColumnIndex()).equalsIgnoreCase("Scholarship Amount")) {
							stdObj.setSchlrshpAmount(df.formatCellValue(currentCell));
							if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
									|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
									|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
								if (customFxcelValidator
										.numberValidation(mObject, "schlrshpAmount",
												checkNullandTrim(stdObj.getSchlrshpAmount()), 50000)
										.get("schlrshpAmount").get("status").equalsIgnoreCase("0")) {
									currentCell.setCellStyle(cellStyle);
								}
							} else {
								currentCell.setCellValue("");
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Facility provided to the CWSN")) {
							;
							stdObj.setFacProvidedCwsn(df.formatCellValue(currentCell));
							if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
								if (customFxcelValidator
										.numberValidation(mObject, "facProvidedCwsn",
												checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
										.get("facProvidedCwsn").get("status").equalsIgnoreCase("0")) {
									currentCell.setCellStyle(cellStyle);
								} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
								}
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Specific Learning Disability (SLD)")) {
							stdObj.setScrndFrSld(df.formatCellValue(currentCell));
							if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
									|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
									|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
								if (customFxcelValidator
										.numberValidation(mObject, "scrndFrSld",
												checkNullandTrim(stdObj.getScrndFrSld()), 2)
										.get("scrndFrSld").get("status").equalsIgnoreCase("0")) {
									currentCell.setCellStyle(cellStyle);
								} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
								}
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Type of Specific Learning Disability (SLD)")) {
							stdObj.setSldType(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "sldType", checkNullandTrim(stdObj.getSldType()), 3)
									.get("sldType").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Autism Spectrum Disorder")) {
							stdObj.setScrndFrAsd(df.formatCellValue(currentCell));
							if (customFxcelValidator.numberValidation(mObject, "scrndFrAsd",
									checkNullandTrim(stdObj.getScrndFrAsd()), 2).get("scrndFrAsd").get("status")
									.equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("Attention Deficit Hyperactive Disorder (ADHD)")) {
							stdObj.setScrndFrAdhd(df.formatCellValue(currentCell));
							if (customFxcelValidator.numberValidation(mObject, "scrndFrAdhd",
									checkNullandTrim(stdObj.getScrndFrAdhd()), 2).get("scrndFrAdhd").get("status")
									.equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
							}
						}

						if (headersFromExcel.get(currentCell.getColumnIndex())
								.equalsIgnoreCase("extra curricular activity")) {
							stdObj.setIsEcActivity(df.formatCellValue(currentCell));
							if (customFxcelValidator
									.numberValidation(mObject, "isEcActivity",
											checkNullandTrim(stdObj.getIsEcActivity()), 2)
									.get("isEcActivity").get("status").equalsIgnoreCase("0")) {
								currentCell.setCellStyle(cellStyle);
							} else {
//								cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
//								currentCell.setCellStyle(cellStyle);
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

				if (currentRow.getRowNum() > 4) {
					stdList.add(stdObj);
				}
//				if (stdObj.getStudentName() != null) {
//					stdList.add(stdObj);
//				}else {
//					
//				}
			}
			File uploadedExcel1 = new File(
					userBucketPath + File.separator + udisecode + File.separator + udisecode + "_validated." + "xlsm");
			FileOutputStream outFile = new FileOutputStream(uploadedExcel1);
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
		List<Map<String, HashMap<String, String>>> finalResponse = fileServiceImpl.uploadData(stdList, userid,
				request.getRemoteAddr(), udisecode, sObj, new HashMap<String, String>(),
				new HashMap<Integer, Boolean>(), new HashMap<String, Boolean>(), new HashMap<String, Boolean>(),
				new HashMap<String, Boolean>(), new HashMap<String, Boolean>());
//		System.out.println(objectMapper.writeValueAsString(finalResponse));
//		uploadedResponse.wr
		try {
			PrintWriter printWriter = new PrintWriter(uploadedResponse);
			printWriter.print(objectMapper.writeValueAsString(finalResponse));
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		
//		if(finalResponse !=null) {
//			FinalResponse
//		}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}

		System.out.println("finalResponse---->" + finalResponse);

		try {
//		long 	statusCount=	finalResponse.stream().filter((e)->
//		e.entrySet().contains("staus=0")
////		e.entrySet().stream().filter(a->a.getValue().entrySet().contains("staus=0").count())
//		).count();

			long statusCount = finalResponse.stream()
					.filter((e) -> e.get("finalStatus").get("status").equalsIgnoreCase("0")).count();

			System.out.println("Status count--->" + statusCount);
			if (statusCount > 0) {
				return ResponseEntity
						.ok(new FinalResponse("", "1", "Uploaded Successfully but Some data Error check and resolve"));
			} else {
				return ResponseEntity.ok(new FinalResponse("", "1", "Uploaded Successfully"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ResponseEntity.ok(new FinalResponse("", "1", "Success"));
	}

	@RequestMapping(value = "/downloadUdiseTemplate", method = RequestMethod.GET)
	public void downloadUdiseTemplate(@RequestParam("schoolId") String schoolId,
			@RequestParam("stateId") String stateId, HttpServletResponse response) throws Exception {

		System.out.println("autowire---->" + nativeRepository);
		System.out.println("schoolId---->" + schoolId);
		String templateName = null;
		if (stateId.equalsIgnoreCase("116")) {
			templateName = "Bulk_Student_Data_tem1.xlsm";
		} else if (stateId.equalsIgnoreCase("112")) {
			templateName = "Bulk_Student_Data_tem2.xlsm";
		} else {
			templateName = "Bulk_Student_Data_template_common.xlsm";
		}
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		UserExcelExporter excelExporter = new UserExcelExporter();
		excelExporter.export(response, schoolId, nativeRepository, templateName, templatePath);

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
	public List<UploadHistory> getUploadedHistor(@RequestBody Integer data) throws Exception {
		return fileServiceImpl.getUploadedHistor(data);
	}

	@RequestMapping(value = "/getValidatedData", method = RequestMethod.POST)
//	public Stream<String>  getValidatedData(@RequestBody String data) throws Exception {
	public Map<String, Object> getValidatedData(@RequestBody String data) throws Exception {
		System.out.println("data---->" + data);
		Map<String, Object> map = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			map = new ObjectMapper().readValue(data, Map.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("value", fileServiceImpl.getValidatedData((int) map.get("schoolId")));
//		System.out.println(ConfigurableUtility.state111HeaderKey);
		System.out.println(map.get("stateId"));
		if ((int) map.get("stateId") == 116) {
			mp.put("header", ConfigurableUtility.commonHeaderKey_1);
		} else {
			mp.put("header", ConfigurableUtility.commonHeaderKey);
		}

//		return fileServiceImpl.getValidatedData((int)map.get("schoolId"));
		return mp;
	}

	@RequestMapping(path = "/downloadUploadedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadUploadedExcel(String param) throws IOException {

		// ...
		File uploadedExcel = new File(userBucketPath + File.separator + param + File.separator + param + "." + "xlsm");

		InputStreamResource resource = new InputStreamResource(new FileInputStream(uploadedExcel));
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + param + ".xlsm");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(uploadedExcel.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@RequestMapping(path = "/downloadValidatedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadValidatedExcel(@RequestParam("schoolId") String schoolId,
			@RequestParam("schoolId") String stateId, HttpServletResponse response) throws IOException {
		System.out.println("called");
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SchoolBulkTemplate_" + currentDateTime + ".xlsm";
		response.setHeader(headerKey, headerValue);
		UserExcelExporter excelExporter = new UserExcelExporter();
		List<String> commomHeadersFromValidation = null;
		if (stateId.equalsIgnoreCase("116")) {
			commomHeadersFromValidation = ConfigurableUtility.state116CommomHeadersFromExcel;
		} else {
			commomHeadersFromValidation = ConfigurableUtility.commomHeadersFromValidation;
		}
		excelExporter.exportValidatedExcel(response, schoolId, commomHeadersFromValidation, userBucketPath);
		return null;

	}

	public String checkNullandTrim(String value) {
		if (value != null && value != "") {
			return value.trim();
		}
		return value;
	}

	public String checkNull(String value) {
		if (value == null || value.equalsIgnoreCase("null")) {
//			System.out.println("value---->"+value);
			value = "";
		}
		return value;
	}

	public String classCheck(String classId) {
		if (classId.equalsIgnoreCase("PP1")) {
			return "-1";
		} else if (classId.equalsIgnoreCase("PP2")) {
			return "-2";
		} else if (classId.equalsIgnoreCase("PP3")) {
			return "-3";
		} else {
			return classId;
		}
	}

	public void setCellColors(Row r, Cell cl, int cellNumber, CellStyle cellStyle) {
		try {
			if (cl == null) {
				r.createCell(cellNumber);
			}
			r.getCell(cellNumber).setCellStyle(cellStyle);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
