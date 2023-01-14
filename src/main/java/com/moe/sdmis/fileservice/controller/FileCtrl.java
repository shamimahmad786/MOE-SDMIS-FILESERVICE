package com.moe.sdmis.fileservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
import java.util.HashSet;
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
import com.moe.sdmis.fileservice.utility.TemplateConfiguration3;
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
		StaticReportBean schoolObj = null;
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
		if (uploadedExcel.exists()) {
			try {
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

		try {
			schoolObj = fileServiceImpl.getSchoolDetails(Integer.parseInt(schoolId));
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
				datatypeSheet.setColumnHidden(54, false);
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
				}

//				if (!df.formatCellValue(currentRow.getCell(54)).equalsIgnoreCase(schoolId)) {
//					System.out.println("ready for exception");
//					uploadedExcel.delete();
//					uploadedResponse.delete();
//					throw new GenericExceptionHandler("UDISE CODE is not matched with the this School. Please download template for crossponding school", "100001", request.getRemoteAddr(),
//							userid, schoolId);
//				}
			}

			if (currentRow.getRowNum() == 2) {
//				System.out.println(schoolObj.getRowValue().get(0).get("udise_sch_code"));
				if (schoolObj.getRowValue() != null && schoolObj.getRowValue().size() > 0
						&& !String.valueOf(schoolObj.getRowValue().get(0).get("udise_sch_code"))
								.equalsIgnoreCase(df.formatCellValue(currentRow.getCell(4)))) {
					throw new GenericExceptionHandler("Template Unauthenticated and this is other school excel",
							"100005", request.getRemoteAddr(), userid, schoolId);
				}

			}

			if (currentRow.getRowNum() == 6) {
//				if (stateId.equalsIgnoreCase("116")) {
					numberOfCell = ConfigurableUtility.commonPhysicalColumn_3;

//				} else if (stateId.equalsIgnoreCase("112")) {
//					numberOfCell = ConfigurableUtility.commonPhysicalColumn_2;
//				} else {
//					numberOfCell = ConfigurableUtility.commonPhysicalColumn;
//				}

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
					cellHeader.add(currentCell.getStringCellValue().replaceAll("\\s+", ""));
				}

				Map<String, Integer> headerIndexMap = IntStream.range(0, cellHeader.size()).boxed()
						.collect(Collectors.toMap(i -> cellHeader.get(i), i -> i));

//				if (stateId.equalsIgnoreCase("116")) {
					headersFromExcel = ConfigurableUtility.templateHeadersFromExcel_3;
//				} else if (stateId.equalsIgnoreCase("112")) {
//					headersFromExcel = ConfigurableUtility.templateHeadersFromExcel_2;
//				} else {
//					headersFromExcel = ConfigurableUtility.commomHeadersFromExcel;
//				}

				Integer result = null;
				try {
					for (int i = 0; i < cellHeader.size(); i++) {
						if (!cellHeader.get(i).equalsIgnoreCase(headersFromExcel.get(i))) {
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

		// Update History

		fileServiceImpl.updateHistory(request.getRemoteHost(), schoolId, userid, "1");
		
		return ResponseEntity.ok(new FinalResponse(new HashMap<String, Object>(), "1", "Success"));
	}

	@RequestMapping(value = "/docValidate", method = RequestMethod.POST)
	public ResponseEntity<?> docValidate(@RequestBody String data, HttpServletRequest request) throws Exception {
//		System.out.println("Called");
		DataFormatter df = new DataFormatter();
//		System.out.println("Before call");
		Map<String, Object> map = null;
		HashMap<String, Object> response = new HashMap<String, Object>();
		long statusCount = 0;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			map = new ObjectMapper().readValue(data, Map.class);
			File tempFile = new File(userBucketPath + File.separator + map.get("schoolId") + File.separator
					+ map.get("schoolId") + ".xlsm");
			FileInputStream excelFile = new FileInputStream(tempFile);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);
			Iterator<Row> iterator = datatypeSheet.iterator();
//			Integer totalRows = datatypeSheet.getPhysicalNumberOfRows();
			Integer totalRows =0;
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
			HashMap<String, List<String>> sectionMap = new HashMap<String, List<String>>();
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
				for (int i = 0; i < sectionsObj.getRowValue().size(); i++) {
					
//					try {
					sectionMap.put(String.valueOf(sectionsObj.getRowValue().get(i).get("class_id")), new ObjectMapper().readValue(String.valueOf(sectionsObj.getRowValue().get(i).get("section_name")), List.class));
//						sectionMap.put(String.valueOf(sectionsObj.getRowValue().get(i).get("class_id")),new ArrayList<String>().add(String.valueOf(sectionsObj.getRowValue().get(i).get("section_name"))));
//				}catch() {}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {

				UserExcelExporter excelExporter = new UserExcelExporter();
				vocationObj = excelExporter.getVocationData(nativeRepository, String.valueOf(map.get("schoolId")));
//				System.out.println("Vocation detaisl--->" + vocationObj.getRowValue());

				for (int i = 0; i < vocationObj.getRowValue().size(); i++) {
					if (String.valueOf(vocationObj.getRowValue().get(i).get("grade_id")).equalsIgnoreCase("1")) {
						lowerSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sector_id")), true);
						lowerSubSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sub_sector_id")), true);
					} else if (String.valueOf(vocationObj.getRowValue().get(i).get("grade_id")).equalsIgnoreCase("2")) {
						higherSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sector_id")), true);
						higherSubSector.put(String.valueOf(vocationObj.getRowValue().get(i).get("sub_sector_id")),
								true);
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			HashSet<String> adharMach=new HashSet<String>();
			HashSet<String> excelAdharMach=new HashSet<String>();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				CommonBean stdObj = new CommonBean();
				stdObj.setUdisecode(String.valueOf(map.get("schoolId")));
				if (currentRow.getRowNum() > 6) {
//					if (String.valueOf(map.get("stateId")).equalsIgnoreCase("116")) {
						mtongObj = MotherTongMaster.motherTongMap_116;
						Cell classCell = currentRow.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						Cell sectionCell = currentRow.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						Cell nameCell = currentRow.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						Cell genderCell = currentRow.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			
					
						if (currentRow.getRowNum() > 6 && (classCell !=null || sectionCell !=null || nameCell !=null || genderCell !=null )) {
							TemplateConfiguration3 tcObj = new TemplateConfiguration3();
							stdObj = tcObj.dataPrepration(mtongObj, stdObj, currentRow, sObj, sectionsObj, vocationObj,
									cellStyle, correctCellStyle, lowerSector, lowerSubSector, higherSector,
									higherSubSector, sectionMap, mObject,excelAdharMach);
							stdList.add(stdObj);
							++totalRows; 
						}
//					} else if (String.valueOf(map.get("stateId")).equalsIgnoreCase("112")) {
//						mtongObj = MotherTongMaster.motherTongMap_112;
//					} else {
//
//					}
				} // if end
			} // while end

			File uploadedExcel1 = new File(userBucketPath + File.separator + map.get("schoolId") + File.separator
					+ String.valueOf(map.get("schoolId")) + "_validated." + "xlsm");
			FileOutputStream outFile = new FileOutputStream(uploadedExcel1);
			workbook.write(outFile);
			outFile.close();
			List<Map<String, HashMap<String, String>>> finalResponse = fileServiceImpl.uploadData(stdList,
					String.valueOf(map.get("userId")), request.getRemoteAddr(), String.valueOf(map.get("schoolId")),
					sObj, sectionMap, mtongObj, lowerSector, lowerSubSector, higherSector, higherSubSector,adharMach);
		
			// temporary comment
			File uploadedResponse = new File(userBucketPath + File.separator + String.valueOf(map.get("schoolId"))
					+ File.separator + String.valueOf(map.get("schoolId")) + "." + "txt");
			
//			try {
//				// only for test purpose'
//				FileOutputStream f = new FileOutputStream(new File("E:\\UDISE\\bulkExcel\\3103026\\3103026.txt"));
//				ObjectOutputStream o = new ObjectOutputStream(f);
//
//				// Write objects to file
//				o.writeObject(finalResponse);
////				o.writeObject(p2);
//
//				o.close();
//				f.close();
//			}catch(Exception ex) {
//				ex.printStackTrace();
//			}
			
			
			
			try {
				PrintWriter printWriter = new PrintWriter(uploadedResponse);
				printWriter.print(objectMapper.writeValueAsString(finalResponse));
				printWriter.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				statusCount = finalResponse.stream()
						.filter((e) -> e.get("fs").get("s").equalsIgnoreCase("0")).count();
				response.put("noOfErrorRecords", statusCount);
//				response.put("noOfRecords", totalRows-7);
				response.put("noOfRecords", totalRows);
				if (statusCount > 0) {
					return ResponseEntity.ok(new FinalResponse(response, "1",
							"Uploaded Successfully but Some data Error check and resolve"));
				} else {
					return ResponseEntity
							.ok(new FinalResponse(response, "1", "Uploaded Successfully"));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(new FinalResponse(response, "1", "Success"));

	}



	@RequestMapping(value = "/downloadUdiseTemplate", method = RequestMethod.GET)
	public void downloadUdiseTemplate(@RequestParam("schoolId") String schoolId,
			@RequestParam("stateId") String stateId, HttpServletResponse response) throws Exception {

		String templateName = null;
//		if (stateId.equalsIgnoreCase("116")) {
			templateName = "Bulk_Student_Data_tem3.xlsm";
//		} else if (stateId.equalsIgnoreCase("112")) {
//			templateName = "Bulk_Student_Data_tem2.xlsm";
//		} else {
//			templateName = "Bulk_Student_Data_template_common.xlsm";
//		}
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		UserExcelExporter excelExporter = new UserExcelExporter();
		excelExporter.export(response, schoolId, nativeRepository, templateName, templatePath);

	}

	@RequestMapping(value = "/finalUpdateStudentData", method = RequestMethod.POST)
	public ResponseEntity<?> finalUpdateStudentData(@RequestBody String data) throws Exception {
//		System.out.println(data);
		ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> mp=new HashMap<String,String>();
		Map<String, Object> map = new ObjectMapper().readValue(data, Map.class);
		 mp=fileServiceImpl.finalUpdateStudentData(String.valueOf(map.get("udiseCode")));
		
		 if(mp.get("status").equalsIgnoreCase("0")) {
			 throw new GenericExceptionHandler("Somthing Event Wrong and Contact with Administrator", "100006","", "",null);
		 }
		 
		return ResponseEntity.ok(mp);
		

	}

	@RequestMapping(value = "/getUploadedHistor", method = RequestMethod.POST)
	public List<UploadHistory> getUploadedHistor(@RequestBody Integer data) throws Exception {
		return fileServiceImpl.getUploadedHistor(data);
	}

	@RequestMapping(value = "/getValidatedData", method = RequestMethod.POST)
//	public Stream<String>  getValidatedData(@RequestBody String data) throws Exception {
	public Map<String, Object> getValidatedData(@RequestBody String data) throws Exception {
//		System.out.println("data---->" + data);
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
//		System.out.println(map.get("stateId"));
//		if ((int) map.get("stateId") == 116) {
			mp.put("header", ConfigurableUtility.commonHeaderKey_3);
//		} else {
//			mp.put("header", ConfigurableUtility.commonHeaderKey);
//		}

//		return fileServiceImpl.getValidatedData((int)map.get("schoolId"));
		return mp;
	}

	@RequestMapping(path = "/downloadUploadedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadUploadedExcel(@RequestParam("schoolId") String param) throws IOException {

		// ...
		File uploadedExcel = new File(userBucketPath + File.separator + param + File.separator + param + "." + "xlsm");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
//		System.out.println(uploadedExcel.getAbsolutePath());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(uploadedExcel));
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Original_SchoolBulkTemplate_" + currentDateTime + ".xlsm");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(uploadedExcel.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@RequestMapping(path = "/downloadValidatedExcel", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadValidatedExcel(@RequestParam("schoolId") String schoolId,
			@RequestParam("schoolId") String stateId, HttpServletResponse response) throws IOException {
//		System.out.println("called");
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Validated_SchoolBulkTemplate_" + currentDateTime + ".xlsm";
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

	@RequestMapping(value = "/getSchoolDetails", method = RequestMethod.POST)
	public StaticReportBean getSchoolDetails(@RequestBody String schoolId) throws Exception {
		return fileServiceImpl.getSchoolDetails(Integer.parseInt(schoolId));
	}

	public String checkNullandTrim(String value) {
		if (value != null && value != "") {
			return value.trim();
		}
		return value;
	}

	public String checkNull(String value) {
		if (value == null || value.equalsIgnoreCase("null")) {
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
