package com.moe.sdmis.fileservice.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.moe.sdmis.fileservice.db.NativeRepository;
import com.moe.sdmis.fileservice.db.QueryResult;
import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

@Component
public class UserExcelExporter {

//	@Autowired
//	NativeRepository nativeRepository; 
	
	

	

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFSheet desSheet;
//    private List<User> listUsers;

	public UserExcelExporter() {
//        this.listUsers = listUsers;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("UdiseStudentDataCapture");

		sheet.autoSizeColumn(4);
//        Row hiddenRow = sheet.createRow(0);
//        hiddenRow.getZeroHeight();
//        hiddenRow.setZeroHeight(true);  
//        System.out.println(hiddenRow.getZeroHeight());

//        CellStyle hiddenstyle = workbook.createCellStyle();
//        hiddenstyle.setHidden(true);
//        hiddenRow.setRowStyle(hiddenstyle);
//        Cell cell = hiddenRow.createCell(0);
//        cell.setCellValue("udise_student_template");
//        cell.hideRow(1);

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);

		style.setFont(font);

		CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
		// fill foreground color ...
		headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
		// and solid fill pattern produces solid grey cell fill
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setFont(font);

		createCell(row, 0, "CLASS ID", headerCellStyle);
		createCell(row, 1, "SECTION ID", headerCellStyle);
		createCell(row, 2, "NAME", headerCellStyle);
		createCell(row, 3, "GENDER", headerCellStyle);
		createCell(row, 4, "DOB", headerCellStyle);
		createCell(row, 5, "MOTHER NAME", headerCellStyle);
		createCell(row, 6, "FATHER/GUARDIAN'S NAME", headerCellStyle);
		createCell(row, 7, "AADHAR", headerCellStyle);
		createCell(row, 8, "NAME AS PER AADHAR", headerCellStyle);
		createCell(row, 9, "ADDRESS", headerCellStyle);
		createCell(row, 10, "PINCODE", headerCellStyle);
		createCell(row, 11, "MOBILE NUMBER", headerCellStyle);
		createCell(row, 12, "ALTERNATE MOBILE NUMBER", headerCellStyle);
		createCell(row, 13, "EMAIL ID(STUDENT/PARENT/GUARDIAN)", headerCellStyle);
		createCell(row, 14, "MOTHER TONGUE", headerCellStyle);
		createCell(row, 15, "SOCIAL CATEGORY", headerCellStyle);
		createCell(row, 16, "MINORITY GROUP", headerCellStyle);
		createCell(row, 17, "ANTYODAYA/BPL BANEFICIARY", headerCellStyle);
		createCell(row, 18, "BELONGS TO EWS/DISADVANTAGED GROUP", headerCellStyle);
		createCell(row, 19, "CWSN(YES/NO)", headerCellStyle);
		createCell(row, 20, "IMPAIRMENT TYPE", headerCellStyle);
		createCell(row, 21, "CHILD  IS INDIAN NATIONAL", headerCellStyle);
		createCell(row, 22, "CHILD IS OUT-OF-SCHOOL-CHILD", headerCellStyle);
		createCell(row, 23, "ADMISSION NUMBER", headerCellStyle);
		createCell(row, 24, "ADMISSION DATE", headerCellStyle);
		createCell(row, 25, "STUDENT STREAM(For higher secondary only)", headerCellStyle);
		createCell(row, 26, "PREVIOUS ACADEMIC SCHOOLING STATUS", headerCellStyle);
		createCell(row, 27, "CLASS STUDIES INPREVIOUS ACADEMIC", headerCellStyle);
		createCell(row, 28, "ADMITED/ENROLLED UNDER", headerCellStyle);
		createCell(row, 29, "APPEARED FOR EXAM IN PREVIOUS CLASS", headerCellStyle);
		createCell(row, 30, "RESULT FOR PREVIOUS EXAM", headerCellStyle);
		createCell(row, 31, "MARKS % OF PREVIOUS EXAM", headerCellStyle);
		createCell(row, 32, "CLASS ATTENDED DAYS", headerCellStyle);
		createCell(row, 33, "ACADEMIC YEAR ID", headerCellStyle);
		Cell cell = row.createCell(34);
		cell.setCellValue("udise_student_template");
		sheet.setColumnHidden(34, true);
//        createCell(row, 34, "udise_student_template", headerCellStyle);
		sheet.autoSizeColumn(3);

		columnDescription(style);
	}

	private void columnDescription(CellStyle style) {
		desSheet = workbook.createSheet("columnDescription");
		List<Row> rowList = new ArrayList<Row>();
		for (int i = 0; i < 34; i++) {
			Row row = desSheet.createRow(i);
			rowList.add(row);
		}

		List<String> rowDesLabel = Arrays.asList("CLASS ID", "SECTION ID", "NAME", "GENDER", "DOB", "MOTHER NAME",
				"FATHER/GUARDIAN'S NAME", "AADHAR", "NAME AS PER AADHAR", "ADDRESS", "PINCODE", "MOBILE NUMBER",
				"ALTERNATE MOBILE NUMBER", "EMAIL ID(STUDENT/PARENT/GUARDIAN)", "MOTHER TONGUE", "SOCIAL CATEGORY",
				"MINORITY GROUP", "ANTYODAYA/BPL BANEFICIARY", "BELONGS TO EWS/DISADVANTAGED GROUP", "CWSN(YES/NO)",
				"IMPAIRMENT TYPE", "CHILD  IS INDIAN NATIONAL", "CHILD IS OUT-OF-SCHOOL-CHILD", "ADMISSION NUMBER",
				"ADMISSION DATE", "STUDENT STREAM(For higher secondary only)", "PREVIOUS ACADEMIC SCHOOLING STATUS",
				"CLASS STUDIES INPREVIOUS ACADEMIC", "ADMITED/ENROLLED UNDER", "APPEARED FOR EXAM IN PREVIOUS CLASS",
				"RESULT FOR PREVIOUS EXAM", "MARKS % OF PREVIOUS EXAM", "CLASS ATTENDED DAYS", "ACADEMIC YEAR ID");

		List<String> rowDes = Arrays.asList("CLASS ID should be in digit", "SECTION ID should be in digit",
				"NAME should be in character", "GENDER should be in digit", "DOB should be in DD/MM/YYYY formate",
				"MOTHER NAME should be in character", "FATHER/GUARDIAN'S NAME should be in character",
				"AADHAR should be in digit", "NAME AS PER AADHAR should be in character",
				"ADDRESS should be in character with comma allow", "PINCODE should be in digit",
				"MOBILE NUMBER should be in digit", "ALTERNATE MOBILE NUMBER should be in digit",
				"EMAIL ID(STUDENT/PARENT/GUARDIAN) it should be proper email id", "MOTHER TONGUE should be in digit",
				"SOCIAL CATEGORY should be in digit", "MINORITY GROUP should be in digit",
				"ANTYODAYA/BPL BANEFICIARY should be in digit", "BELONGS TO EWS/DISADVANTAGED GROUP should be in digit",
				"CWSN(YES/NO) should be in digit", "IMPAIRMENT TYPE should be in Json",
				"CHILD  IS INDIAN NATIONAL should be in digit", "CHILD IS OUT-OF-SCHOOL-CHILD should be in digit",
				"ADMISSION NUMBER should be in Alphanumeric characters", "ADMISSION DATE should be DD/MM/YYYY formate",
				"STUDENT STREAM(For higher secondary only)should be in digit",
				"PREVIOUS ACADEMIC SCHOOLING STATUS should be in digit",
				"CLASS STUDIES INPREVIOUS ACADEMIC should be in digit", "ADMITED/ENROLLED UNDER should be in digit",
				"APPEARED FOR EXAM IN PREVIOUS CLASS should be in digit", "RESULT FOR PREVIOUS EXAM should be in digit",
				"MARKS % OF PREVIOUS EXAM should be in digit", "CLASS ATTENDED DAYS should be in digit",
				"ACADEMIC YEAR ID should be in digit");

		for (int i = 0; i < rowList.size(); i++) {
			Cell cell = rowList.get(i).createCell(0);
			Cell cellValue = rowList.get(i).createCell(1);
			cell.setCellValue((String) rowDesLabel.get(i));
			cellValue.setCellValue((String) rowDes.get(i));
			cell.setCellStyle(style);

		}

		for (int i = 0; i < 34; i++) {
			desSheet.autoSizeColumn(i);
		}
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {

		Cell cell = row.createCell(columnCount);
		CellStyle cellStyle = cell.getCellStyle();
//        cellStyle.setFillBackgroundColor(IndexedColors.BLACK.index);
//        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		cell.setCellStyle(cellStyle);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
		cell.setCellType(CellType.STRING);
		sheet.autoSizeColumn(columnCount);
//        cell.set
	}

//    private void writeDataLines() {
//        int rowCount = 1;
// 
//        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setFontHeight(14);
//        style.setFont(font);
//                 
//        for (User user : listUsers) {
//            Row row = sheet.createRow(rowCount++);
//            int columnCount = 0;
//             
//            createCell(row, columnCount++, user.getId(), style);
//            createCell(row, columnCount++, user.getEmail(), style);
//            createCell(row, columnCount++, user.getFullName(), style);
//            createCell(row, columnCount++, user.getRoles().toString(), style);
//            createCell(row, columnCount++, user.isEnabled(), style);
//             
//        }
//    }

	public void export(HttpServletResponse response, String schoolId, NativeRepository nativeRepository,String templateName,String tempPath)
			throws IOException {
//        writeHeaderLine();
//        writeDataLines();
		readAndDownloadExcel(response,schoolId, nativeRepository,templateName,tempPath);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	public void exportValidatedExcel(HttpServletResponse response, String schoolId, List<String> validationLists,String userBucketPath)
			throws IOException {
//      writeHeaderLine();
//      writeDataLines();
		CustomFxcelValidator cfvObj = new CustomFxcelValidator();
		FileInputStream excelFile = new FileInputStream(new File(userBucketPath+File.separator+schoolId+File.separator+schoolId+"_validated.xlsm"));
		workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(1);
		Iterator<Row> iterator = datatypeSheet.iterator();
	
//		while (iterator.hasNext()) {
//			
//			Row currentRow = iterator.next();
//
////        	              System.out.println("Get Row Number--->"+currentRow.getRowNum());
//
//			Iterator<Cell> cellIterator = currentRow.iterator();
//
//			if (currentRow.getRowNum() > 4) {
//			
//				System.out.println("Row--->"+currentRow.getRowNum());
//				int i = 0;

//				while (cellIterator.hasNext()) {
//					Map<String, HashMap<String, String>> mp =new HashMap<String, HashMap<String, String>>();
//					Cell currentCell = cellIterator.next();
//					DataFormatter df = new DataFormatter();
//					String value = df.formatCellValue(currentCell);
////					System.out.println(currentCell.getColumnIndex() + "-------" + value);
////System.out.println("i---->"+i);
//					
//  System.out.println(validationLists.get(i)+"----"+value);
//					if (validationLists.get(i).equalsIgnoreCase("numberValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.numberValidation(mp, "key", value);
//						
//						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
////							System.out.println(hs.get("key"));
//							Cell colorCurrentCell=currentRow.getCell(i);
//							  CellStyle cellStyle = workbook.createCellStyle();
//							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
//							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//							colorCurrentCell.setCellStyle(cellStyle);
////							colorCurrentCell.setCellValue(11);
//							
//						}
//					} else if (validationLists.get(i).equalsIgnoreCase("stringNonSpecialValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.stringNonSpecialValidation(mp, "key", value);
//					
//						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
////							System.out.println(hs.get("key"));
//							CellStyle cellStyle = workbook.createCellStyle();
//							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
//							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//							currentCell.setCellStyle(cellStyle);
//						}
//					} else if (validationLists.get(i).equalsIgnoreCase("dateValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.dateValidation(mp, "key", value);
//					
//						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
////							System.out.println(hs.get("key"));
//							CellStyle cellStyle = workbook.createCellStyle();
//							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
//							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//							currentCell.setCellStyle(cellStyle);
//						}
//					} else if (validationLists.get(i).equalsIgnoreCase("adharValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.adharValidation(mp, "key", value);
//					
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle = workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					} else if (validationLists.get(i).equalsIgnoreCase("stringValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.stringValidation(mp, "key", value);
//						
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle = workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					} else if (validationLists.get(i).equalsIgnoreCase("pincodeValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.pincodeValidation(mp, "key", value);
//						
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle = workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					} else if (validationLists.get(i).equalsIgnoreCase("mobileValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.mobileValidation(mp, "key", value);
//					
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle = workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					} else if (validationLists.get(i).equalsIgnoreCase("emailValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.emailValidation(mp, "key", value);
//				
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle =workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					} else if (validationLists.get(i).equalsIgnoreCase("jsonValidation")) {
//						Map<String, HashMap<String, String>> hs = cfvObj.jsonValidation(mp, "key", value);
//				
////						if (hs.get("key").get("status").equalsIgnoreCase("0")) {
//////							System.out.println(hs.get("key"));
////							CellStyle cellStyle = workbook.createCellStyle();
////							cellStyle.setFillForegroundColor(IndexedColors.RED.index);
////							cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////							currentCell.setCellStyle(cellStyle);
////						}
//					}
//					++i;
//				}
//			}
//
//		}

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}
	
	public StaticReportBean getSchoolData(NativeRepository nativeRepository,String schoolId) {
		QueryResult qrObj = null;
		StaticReportBean sobj = new StaticReportBean();
		try {
	
			System.out.println(nativeRepository);

			qrObj = nativeRepository.executeQueries(
					"select * from school_master_live sm left join  mst_district md on sm.district_id =md.district_id  left join mst_state ms on md.state_id=ms.state_id where  sm.school_id ="
							+ schoolId + "  limit 1");
    		sobj.setColumnName(qrObj.getColumnName());
    		sobj.setRowValue(qrObj.getRowValue());
    		sobj.setColumnDataType(qrObj.getColumnDataType());
    		sobj.setStatus("1");
//    		return sobj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sobj;
	}

	public StaticReportBean getSectionData(NativeRepository nativeRepository,String schoolId) {
		QueryResult qrObj = null;
		StaticReportBean sobj = new StaticReportBean();
		try {
//			qrObj = nativeRepository.executeQueries("select class_id,max(section_id) sections from school_section_details where school_id ="+schoolId+" group by class_id");
			qrObj = nativeRepository.executeQueries("select json_agg(section_name) section_name,class_id  from (\r\n"
					+ "select class_id, section_name  from public.school_section_details where school_id="+schoolId+" group by class_id, section_name order by section_name\r\n"
					+ ") sec\r\n"
					+ "group by class_id");
    		sobj.setColumnName(qrObj.getColumnName());
    		sobj.setRowValue(qrObj.getRowValue());
    		sobj.setColumnDataType(qrObj.getColumnDataType());
    		sobj.setStatus("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sobj;
	}
	
	public StaticReportBean getVocationData(NativeRepository nativeRepository,String schoolId) {
		QueryResult qrObj = null;
		StaticReportBean sobj = new StaticReportBean();
		try {
			qrObj = nativeRepository.executeQueries("select sector_id,sub_sector_id,grade_id from  nsqf_school_mapping nsm where school_id ="+schoolId);
    		sobj.setColumnName(qrObj.getColumnName());
    		sobj.setRowValue(qrObj.getRowValue());
    		sobj.setColumnDataType(qrObj.getColumnDataType());
    		sobj.setStatus("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sobj;
	}
	
	
	
	public void readAndDownloadExcel(HttpServletResponse response,String schoolId, NativeRepository nativeRepository,String templateName,String tempPath) {
		QueryResult qrObj = null;
		try {
			StaticReportBean sobj = new StaticReportBean();

			System.out.println(nativeRepository);

			qrObj = nativeRepository.executeQueries(
					"select * from school_master_live sm left join  mst_district md on sm.district_id =md.district_id  left join mst_state ms on md.state_id=ms.state_id where  sm.school_id ="
							+ schoolId + "  limit 1");
//    		sobj.setColumnName(qrObj.getColumnName());
//    		sobj.setRowValue(qrObj.getRowValue());
//    		sobj.setColumnDataType(qrObj.getColumnDataType());
//    		sobj.setStatus("1");
//    		return sobj;
			System.out.println(qrObj.getRowValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=SDMS_Excel_" + (String) (qrObj.getRowValue().get(0).get("udise_sch_code")) + ".xlsm";
		response.setHeader(headerKey, headerValue);
		try {
			FileInputStream excelFile = new FileInputStream(new File(tempPath+File.separator+templateName));
			workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row thirdrow = datatypeSheet.getRow(2);
			Cell fouthCel = thirdrow.getCell(4);
			Cell secondCel = thirdrow.getCell(1);
			Cell sixCell = thirdrow.getCell(6);
			secondCel.setCellValue((String) qrObj.getRowValue().get(0).get("state_name"));
			fouthCel.setCellValue((String) (qrObj.getRowValue().get(0).get("udise_sch_code")));
			sixCell.setCellValue((String) (qrObj.getRowValue().get(0).get("school_name")));
			Row fourthrow = datatypeSheet.getRow(3);
			Cell fourthRowfouthCel = fourthrow.getCell(4);
			Cell fourthRowsecondCel = fourthrow.getCell(1);
			Cell fourthRowsixCell = fourthrow.getCell(6);			
			fourthRowsecondCel.setCellValue((String) (qrObj.getRowValue().get(0).get("district_cd")));
			fourthRowfouthCel.setCellValue(GeneralUtility.typeMapping.get(String.valueOf((qrObj.getRowValue().get(0).get("sch_type")))));
			fourthRowsixCell.setCellValue(Integer.parseInt(String.valueOf( (qrObj.getRowValue().get(0).get("sch_mgmt_center_id")))));
			
			Row fifthrow = datatypeSheet.getRow(4);
			
			Cell fifthRowfouthCel = fifthrow.getCell(4);
			Cell fifthRowsecondCel = fifthrow.getCell(1);
			Cell fifthRowsixCell = fifthrow.getCell(6);			
			fifthRowsecondCel.setCellValue((String) (qrObj.getRowValue().get(0).get("block_cd")));
			fifthRowfouthCel.setCellValue(Integer.parseInt(String.valueOf( (qrObj.getRowValue().get(0).get("class_frm")))) +" to "+Integer.parseInt(String.valueOf( (qrObj.getRowValue().get(0).get("class_to")))));
			fifthRowsixCell.setCellValue(GeneralUtility.catMapping.get(String.valueOf( (qrObj.getRowValue().get(0).get("sch_category_id")))));
			Row zerorow = datatypeSheet.getRow(0);
			Cell zeroRowFiftyOnecell = zerorow.createCell(54);
			zeroRowFiftyOnecell.setCellValue((int) qrObj.getRowValue().get(0).get("school_id"));
			datatypeSheet.setColumnHidden(54, true);

//         while (iterator.hasNext()) {
//
//             Row currentRow = iterator.next();
//             
//             System.out.println("Get Row Number--->"+currentRow.getRowNum());
//             
//             Iterator<Cell> cellIterator = currentRow.iterator();
//
//             
//             if(currentRow.getRowNum()==1) {
//            	 while (cellIterator.hasNext()) {
//
//                     Cell currentCell = cellIterator.next();
//                     
////                     if(currentCell.getColumnIndex()==8) {
//////                    	 currentCell.setCellValue("Udise Code :"); 
////                     }
//                     
//                     if(currentCell.getColumnIndex()==9) {
//                    	 currentCell.setCellValue(123456); 
//                     }
//                     //getCellTypeEnum shown as deprecated for version 3.15
//                     //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//                     if (currentCell.getCellType() == CellType.STRING) {
//                         System.out.print("cell number---->"+currentCell.getColumnIndex()  +"-----"+currentCell.getStringCellValue() + "--");
//                     } else if (currentCell.getCellType() == CellType.NUMERIC) {
//                		 DataFormatter df = new DataFormatter();
//				
//						 String value = df.formatCellValue(currentCell);
//                         System.out.print("cell number---->"+currentCell.getColumnIndex()  +"-----"+value + "--");
//                     }
//
//                 }
//             }
//             
////             while (cellIterator.hasNext()) {
////
////                 Cell currentCell = cellIterator.next();
////                 //getCellTypeEnum shown as deprecated for version 3.15
////                 //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
////                 if (currentCell.getCellType() == CellType.STRING) {
////                     System.out.print(currentCell.getStringCellValue() + "--");
////                 } else if (currentCell.getCellType() == CellType.NUMERIC) {
////                     System.out.print(currentCell.getNumericCellValue() + "--");
////                 }
////
////             }
//             System.out.println();
//
//         }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getExcelValue(Cell currentCell) {
		DataFormatter formatter = new DataFormatter();
		if (currentCell.getCellType() == CellType.NUMERIC) {
			DataFormatter formatters = new DataFormatter();
			// CellReference cr = new CellRefence(currentCell);
//    		return String.valueOf(currentCell.getNumericCellValue());  
			return String.valueOf(formatters.formatCellValue(currentCell));
		} else if (currentCell.getCellType() == CellType.STRING) {
			return String.valueOf(currentCell.getStringCellValue());
		} else if (currentCell.getCellType() == CellType.BOOLEAN) {
			return String.valueOf(currentCell.getBooleanCellValue());
		}
		return "";
	}

}