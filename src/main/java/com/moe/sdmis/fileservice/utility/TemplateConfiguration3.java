package com.moe.sdmis.fileservice.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

public class TemplateConfiguration3 {
	DataFormatter df = new DataFormatter();

	public CommonBean dataPrepration(Map<Integer, Boolean> mtongObj, CommonBean stdObj, Row currentRow,
			StaticReportBean sObj, StaticReportBean sectionsObj, StaticReportBean vocationObj, CellStyle cellStyle,
			CellStyle correctCellStyle, HashMap<String, Boolean> lowerSector, HashMap<String, Boolean> lowerSubSector,
			HashMap<String, Boolean> higherSector, HashMap<String, Boolean> higherSubSector,
			HashMap<String, List<String>> sectionMap, Map<String, HashMap<String, String>> mObject,HashSet<String> adharMach,Workbook  workbook) {
		CustomFxcelValidator customFxcelValidator = new CustomFxcelValidator();
		stdObj.setClassId(getCellValue(currentRow.getCell(0)));
		Integer cwsnFlag=0;
		
//		CellStyle integerCellStyle = workbook.createCellStyle();
		  //integerCellStyle.setDataFormat(format.getFormat("0"));
//		  integerCellStyle.setDataFormat();
//		correctCellStyle.setDataFormat(format.getFormat("#,##0"));
//		 Workbook workbook = new XSSFWorkbook(); 
		  
		  // Create CellStyles
		  DataFormat format = workbook.createDataFormat();
		  CellStyle integerCellStyle = workbook.createCellStyle();
		  integerCellStyle.setDataFormat(format.getFormat("0"));
//		  integerCellStyle.setDataFormat(format.getFormat("#,##0"));
		
		
		try {
			cwsnFlag=Integer.parseInt(df.formatCellValue(currentRow.getCell(22)));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		try {
			if(stdObj.getClassId() != null && (stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
				
				if(sObj.getRowValue().get(0).get("ppsec_yn") !=null && String.valueOf(sObj.getRowValue().get(0).get("ppsec_yn")).equalsIgnoreCase("1")) {
					if(stdObj.getClassId().equalsIgnoreCase("PP1") ) {
						
					if(Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("ppsec_cls_frm"))) <=-1) {
						setCellColors(currentRow, currentRow.getCell(0), 0, correctCellStyle);	
					}else {
						setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
					}
					}
					
					
					if(stdObj.getClassId().equalsIgnoreCase("PP2") ) {
						if(Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("ppsec_cls_frm"))) <=-2) {
							setCellColors(currentRow, currentRow.getCell(0), 0, correctCellStyle);	
						}else {
							setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
						}
						
					}
					
					if(stdObj.getClassId().equalsIgnoreCase("PP3") ) {
						if(Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("ppsec_cls_frm"))) <=-3) {
							setCellColors(currentRow, currentRow.getCell(0), 0, correctCellStyle);	
						}else {
							setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
						}
						
						
					}
					
				}else {
					setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
				}
			}else if (stdObj.getClassId() != null && !stdObj.getClassId().equalsIgnoreCase("null")
					&& Integer
							.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_frm")))) <= Integer
									.parseInt(classCheck(checkNull(stdObj.getClassId())))
					&& Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("class_to")))) >= Integer
							.parseInt(classCheck(checkNull(stdObj.getClassId())))) {
				setCellColors(currentRow, currentRow.getCell(0), 0, correctCellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
//			currentRow.getCell(0).setCellStyle(cellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(0), 0, cellStyle);
			ex.printStackTrace();
		}

		stdObj.setSectionId(getCellValue(currentRow.getCell(1)));
//		if (customFxcelValidator
//				.numberValidation(mObject, "sectionId", checkNullandTrim(stdObj.getSectionId()), 4)
//				.get("sectionId").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->3");
//			currentRow.getCell(1).setCellStyle(cellStyle);
//		}

		try {
			if(stdObj.getClassId() !=null && (stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3") ) ) {
				if(stdObj.getClassId().equalsIgnoreCase("PP1")) {
//					if(Integer.parseInt(checkNull(sectionMap.get("-1"))) >= Integer
//							.parseInt(checkNull(String.valueOf(checkNull(stdObj.getSectionId())))) && Integer.parseInt(String.valueOf(stdObj.getSectionId()))>0) {
					if(stdObj.getSectionId() !=null && sectionMap.get("-1").contains(stdObj.getSectionId().toUpperCase())) {
					setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
					}else {
						setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
					}
				}else if(stdObj.getClassId().equalsIgnoreCase("PP2")) {
					if(stdObj.getSectionId() !=null && sectionMap.get("-2").contains(stdObj.getSectionId().toUpperCase())) {
						setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
					}else {
						setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
					}
				}else if(stdObj.getClassId().equalsIgnoreCase("PP3")) {
					if(stdObj.getSectionId() !=null && sectionMap.get("-3").contains(stdObj.getSectionId().toUpperCase())) {
						setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
					}else {
						setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
					}
				}
			}
			else if
//			(sectionMap.get(stdObj.getClassId()) != null
//					&& Integer.parseInt(checkNull(sectionMap.get(checkNull(stdObj.getClassId())))) >= Integer
//							.parseInt(checkNull(String.valueOf(lcheckNull(stdObj.getSectionId())))) && Integer.parseInt(String.valueOf(stdObj.getSectionId()))>0) {
			(stdObj.getSectionId() !=null && sectionMap.get(checkNull(stdObj.getClassId())).contains(stdObj.getSectionId().toUpperCase())) {
				setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
		}

		stdObj.setRollNo(getCellValue(currentRow.getCell(2)));
		if (customFxcelValidator.numberValidation(mObject, "rollNo", checkNullandTrim(stdObj.getRollNo()), 9999)
				.get("rollNo").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->4");
//			currentRow.getCell(2).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(2), 2, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(2), 2, correctCellStyle);
		}

		stdObj.setStudentName(checkNull(getCellValue(currentRow.getCell(3))).replaceAll("\\s+", " "));
		if (customFxcelValidator
				.stringNonSpecialValidation(mObject, "studentName", checkNullandTrim(stdObj.getStudentName()),3,50)
				.get("studentName").get("s").equalsIgnoreCase("0")) {

			setCellColors(currentRow, currentRow.getCell(3), 3, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(3), 3, correctCellStyle);
		}

		stdObj.setGender(getCellValue(currentRow.getCell(4)));
		
//		System.out.println("School type--->"+sObj.getRowValue().get(0).get("sch_type"));
		
		if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("1") ) {
			if(stdObj.getGender().equalsIgnoreCase("1") || stdObj.getGender().equalsIgnoreCase("3")) {
		
				setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
			}
			}else if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("2") ) {
				if(stdObj.getGender().equalsIgnoreCase("2") || stdObj.getGender().equalsIgnoreCase("3")) {
					
					setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
				}else {
					setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
				}
			}else if(sObj.getRowValue().get(0).get("sch_type") !=null && String.valueOf(sObj.getRowValue().get(0).get("sch_type")).equalsIgnoreCase("3")) {
				if(stdObj.getGender().equalsIgnoreCase("2") || stdObj.getGender().equalsIgnoreCase("3") || stdObj.getGender().equalsIgnoreCase("1") ) {
					
					setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
				}else {
					setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
				}
			}
		
		
//		if (customFxcelValidator.numberValidation(mObject, "gender", checkNullandTrim(stdObj.getGender()), 3)
//				.get("gender").get("s").equalsIgnoreCase("0")) {
//			setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
//		} else {
//			setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
//		}
		
		

		DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
		if (currentRow.getCell(5) != null && checkNull(currentRow.getCell(5).toString()) != "") {
			java.util.Date d = null;
			try {
				if (currentRow.getCell(5).getCellType() == CellType.NUMERIC) {
					d = currentRow.getCell(5).getDateCellValue();
					stdObj.setStudentDob(dff.format(d).trim());
//					System.out.println("date Dob---->"+(currentRow.getCell(5).getStringCellValue()));
				} else if (currentRow.getCell(5).getCellType() == CellType.STRING) {
//<<<<<<< HEAD
//					System.out.println("String Dob---->"+(currentRow.getCell(5).getStringCellValue()));
//					stdObj.setStudentDob(currentRow.getCell(5).getStringCellValue().trim());
//=======
					
					stdObj.setStudentDob(currentRow.getCell(5).getStringCellValue().trim());
//					System.out.println("Date String --->"+stdObj.getStudentDob());
//>>>>>>> c10fd8d144575a79f575f0f48636147f7140c02e
				}
				
				if(stdObj.getClassId() !=null && (stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
					if(stdObj.getClassId().equalsIgnoreCase("PP1") && StudentAgeClassValidation(-1,Integer.parseInt(String.valueOf(getAge(stdObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date"))))),cwsnFlag)) {
					if(d !=null) {
						checkCellAndCreate(currentRow,5,dff.format(d));
					}else {
						checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
					}
						setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
					}else {
						if(d !=null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
					}
					
					if(stdObj.getClassId().equalsIgnoreCase("PP2") && StudentAgeClassValidation(-2,Integer.parseInt(String.valueOf(getAge(stdObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date"))))),cwsnFlag)) {
						if(d !=null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
					}else {
						if(d !=null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
					}
					System.out.println(sObj);
					System.out.println(sObj.getRowValue().get(0).get("school_id"));
					System.out.println(sObj.getRowValue().get(0).get("session_start_date"));
					if(stdObj.getClassId().equalsIgnoreCase("PP3") && StudentAgeClassValidation(-3,Integer.parseInt(String.valueOf(getAge(stdObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date"))))),cwsnFlag)) {
						if(d != null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
					}else {
						if(d !=null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
					}
				}	
				else if(stdObj.getStudentDob() !=null && sObj.getRowValue().get(0).get("session_start_date") !=null &&  getAge(stdObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date")))>0) { 
				if ( !StudentAgeClassValidation(Integer.parseInt(stdObj.getClassId()),Integer.parseInt(String.valueOf(getAge(stdObj.getStudentDob(),String.valueOf(sObj.getRowValue().get(0).get("session_start_date"))))),cwsnFlag)) {
//					System.out.println("In error age");
					if (currentRow.getCell(5).getCellType() == CellType.NUMERIC) {
//						currentRow.getCell(5).setCellValue(dff.format(d));
						if(d !=null) {
							checkCellAndCreate(currentRow,5,dff.format(d));
						}else {
							checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
						}
						setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
						
					} else if (currentRow.getCell(5).getCellType() == CellType.STRING) {
//						currentRow.getCell(5).setCellValue(currentRow.getCell(5).getStringCellValue());
						checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
					}
					setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
				} else {
//					System.out.println("In correct age");
					
					if (currentRow.getCell(5).getCellType() == CellType.NUMERIC) {
//						currentRow.getCell(5).setCellValue(dff.format(d));
						checkCellAndCreate(currentRow,5,dff.format(d));
					} else if (currentRow.getCell(5).getCellType() == CellType.STRING) {

//						currentRow.getCell(5).setCellValue(currentRow.getCell(5).getStringCellValue());
						checkCellAndCreate(currentRow,5,currentRow.getCell(5).getStringCellValue());
					}
					setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
				}
				}else {
					setCellColors(currentRow,currentRow.getCell(5), 5, cellStyle);
				}
			} catch (Exception ex) {
				setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
				ex.printStackTrace();
			}
		} else {
			setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
		}

		stdObj.setMotherName(checkNull(getCellValue(currentRow.getCell(6))).replaceAll("\\s+", " "));
		if (customFxcelValidator
				.stringNonSpecialValidation(mObject, "motherName", checkNullandTrim(stdObj.getMotherName()),3,50)
				.get("motherName").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->8");
			setCellColors(currentRow, currentRow.getCell(6), 6, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(6), 6, correctCellStyle);
		}

		stdObj.setFatherName(checkNull(getCellValue(currentRow.getCell(7))).replaceAll("\\s+", " "));
		if (customFxcelValidator
				.stringNonSpecialValidation(mObject, "fatherName", checkNullandTrim(stdObj.getFatherName()),3,50)
				.get("fatherName").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->9");
			setCellColors(currentRow, currentRow.getCell(7), 7, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(7), 7, correctCellStyle);
		}

		stdObj.setGuardianName(checkNull(getCellValue(currentRow.getCell(8))).replaceAll("\\s+", " "));
		if (customFxcelValidator
				.stringNonSpecialValidation(mObject, "guardianName", checkNullandTrim(stdObj.getGuardianName()),3,50)
				.get("guardianName").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->10");
			setCellColors(currentRow, currentRow.getCell(8), 8, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(8), 8, correctCellStyle);
		}

	if(currentRow.getCell(9) !=null) {
//		System.out.println("General--->"+currentRow.getCell(9).getCellType());
////		System.out.println(currentRow.getCell(9).getNumericCellValue());
//		System.out.println(df.formatCellValue(currentRow.getCell(9)));
//		Object o = df.formatCellValue(currentRow.getCell(9));
//		System.out.println("Adhar--->"+o.toString());
//		System.out.println(new Double(o.toString()));
//		System.out.println("Adhar--->"+new BigDecimal(o.toString(),MathContext.DECIMAL128).toPlainString());
		try {
		stdObj.setAadhaarNo(getCellValue(currentRow.getCell(9)));
		}catch(Exception ex) {
		stdObj.setAadhaarNo(getCellValue(currentRow.getCell(9)));
			ex.printStackTrace();
		}
	}else {
		stdObj.setAadhaarNo("");
	}
	
	try {
		if (customFxcelValidator.adharValidation(mObject, "aadhaarNo", checkNullandTrim(stdObj.getAadhaarNo()),adharMach)
				.get("aadhaarNo").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->11");
			
			System.out.println(stdObj.getAadhaarNo());
			checkCellAndCreate(currentRow,9,stdObj.getAadhaarNo());
			currentRow.getCell(9).setCellStyle(integerCellStyle);
			integerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
			integerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			setCellColors(currentRow, currentRow.getCell(9), 9, integerCellStyle);
		} else {
			System.out.println(stdObj.getAadhaarNo());
			checkCellAndCreate(currentRow,9,stdObj.getAadhaarNo());
			currentRow.getCell(9).setCellStyle(integerCellStyle);
			integerCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
			integerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			setCellColors(currentRow, currentRow.getCell(9), 9, integerCellStyle);
		}

	}catch(Exception ex) {
		ex.printStackTrace();
	}
		
		if (!stdObj.getAadhaarNo().equalsIgnoreCase("999999999999")) {
			stdObj.setNameAsAadhaar(checkNull(getCellValue(currentRow.getCell(10))).replaceAll("\\s+", " "));
			if (customFxcelValidator
					.stringNonSpecialValidation(mObject, "nameAsAadhaar", checkNullandTrim(stdObj.getNameAsAadhaar()),3,50)
					.get("nameAsAadhaar").get("s").equalsIgnoreCase("0")) {
				setCellColors(currentRow, currentRow.getCell(10), 10, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(10), 10, correctCellStyle);
			}
		}else {
			checkCellAndCreate(currentRow,10,"");
			setCellColors(currentRow, currentRow.getCell(10), 10, correctCellStyle);
		}

		stdObj.setAddress(checkNull(getCellValue(currentRow.getCell(11))).replaceAll("\\s+", " "));
		if (customFxcelValidator.stringValidation(mObject, "address", checkNullandTrim(stdObj.getAddress()),5,150)
				.get("address").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->13");
			setCellColors(currentRow, currentRow.getCell(11), 11, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(11), 11, correctCellStyle);
		}

		stdObj.setPincode(getCellValue(currentRow.getCell(12)));
		if (customFxcelValidator.pincodeValidation(mObject, "pincode", checkNullandTrim(stdObj.getPincode()))
				.get("pincode").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->14");
			setCellColors(currentRow, currentRow.getCell(12), 12, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(12), 12, correctCellStyle);
		}

		stdObj.setMobileNo_1(getCellValue(currentRow.getCell(13)));
		if (customFxcelValidator.mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stdObj.getMobileNo_1()))
				.get("mobileNo_1").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->15");
			setCellColors(currentRow, currentRow.getCell(13), 13, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(13), 13, correctCellStyle);
		}

		stdObj.setMobileNo_2(getCellValue(currentRow.getCell(14)));
		if (customFxcelValidator.mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stdObj.getMobileNo_2()))
				.get("mobileNo_2").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->16");
			setCellColors(currentRow, currentRow.getCell(14), 14, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(14), 14, correctCellStyle);
		}

		stdObj.setEmailId(getCellValue(currentRow.getCell(15)));
		if (customFxcelValidator.emailValidation(mObject, "emailId", checkNullandTrim(stdObj.getEmailId()))
				.get("emailId").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(15), 15, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(15), 15, correctCellStyle);
		}

		stdObj.setMotherTongue(getCellValue(currentRow.getCell(16)));

		try {
			if(stdObj.getMotherTongue() == null || stdObj.getMotherTongue() == ""
					|| stdObj.getMotherTongue().equalsIgnoreCase("null")
					|| mtongObj.get(Integer.parseInt(String.valueOf(checkNull(stdObj.getMotherTongue())))) == null) {
				setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
			}
		else if (stdObj.getMotherTongue() != null && stdObj.getMotherTongue() != ""
					&& !stdObj.getMotherTongue().equalsIgnoreCase("null")
					&& mtongObj.get(Integer.parseInt(String.valueOf(checkNull(stdObj.getMotherTongue())))) != null
					&& !mtongObj.get(Integer.parseInt(String.valueOf(checkNull(stdObj.getMotherTongue()))))) {
				setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(16), 16, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
		}

		stdObj.setSocCatId(getCellValue(currentRow.getCell(17)));
		if(String.valueOf(sObj.getRowValue().get(0).get("state_id")).equalsIgnoreCase("110") && customFxcelValidator.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 5)
				.get("socCatId").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(17), 17, cellStyle);
		}else if(String.valueOf(sObj.getRowValue().get(0).get("state_id")).equalsIgnoreCase("127") && customFxcelValidator.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 10)
				.get("socCatId").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(17), 17, cellStyle);
		} else if (customFxcelValidator.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 4)
				.get("socCatId").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(17), 17, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(17), 17, correctCellStyle);
		}

		stdObj.setMinorityId(getCellValue(currentRow.getCell(18)));
		if (customFxcelValidator.numberValidation(mObject, "minorityId", checkNullandTrim(stdObj.getMinorityId()), 7)
				.get("minorityId").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->20");
//			currentRow.getCell(18).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(18), 18, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(18), 18, correctCellStyle);
		}

//		System.out.println("BPL value--->" + getCellValue(currentRow.getCell(19)));
		if (df.formatCellValue(currentRow.getCell(19)) == null || df.formatCellValue(currentRow.getCell(19)) == "") {
			stdObj.setIsBplYn("2");
			checkCellAndCreate(currentRow,19,"2");
		} else {
			stdObj.setIsBplYn(getCellValue(currentRow.getCell(19)));
		}
		
		if (customFxcelValidator.numberValidation(mObject, "isBplYn", checkNullandTrim(stdObj.getIsBplYn()), 2)
				.get("isBplYn").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(19), 19, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(19), 19, correctCellStyle);
		}

//		System.out.println("stdObj.getAayBplYn()--->"+stdObj.getAayBplYn());
		if (stdObj.getIsBplYn() != null && stdObj.getIsBplYn().equalsIgnoreCase("1")) {
			if(df.formatCellValue(currentRow.getCell(20)) ==null || df.formatCellValue(currentRow.getCell(20))=="") {
				stdObj.setAayBplYn("2");	
			}else {
				stdObj.setAayBplYn(getCellValue(currentRow.getCell(20)));
			}
		}else if(stdObj.getIsBplYn() != null && stdObj.getIsBplYn().equalsIgnoreCase("2")) {
			stdObj.setAayBplYn("9");
		}

//		stdObj.setAayBplYn(df.formatCellValue(currentRow.getCell(20)));
		if (stdObj.getIsBplYn() != null && stdObj.getIsBplYn() != "" && !stdObj.getIsBplYn().equalsIgnoreCase("null")
				&& stdObj.getIsBplYn().equalsIgnoreCase("1")) {
//			System.out.println("get value---->"+stdObj.getAayBplYn());
		if(stdObj.getAayBplYn().equalsIgnoreCase("2")) {
			
//			currentRow.getCell(20).setCellValue("2");
			checkCellAndCreate(currentRow,20,"2");
			setCellColors(currentRow, currentRow.getCell(20), 20, correctCellStyle);
		}
		else if (customFxcelValidator.numberValidation(mObject, "aayBplYn", checkNullandTrim(stdObj.getAayBplYn()), 2)
					.get("aayBplYn").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->22");
//			currentRow.getCell(20).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(20), 20, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(20), 20, correctCellStyle);
			}
		} else {
//			setCellColors(currentRow, currentRow.getCell(20), 20, cellStyle);
			
//			currentRow.getCell(20).setCellValue("");
			checkCellAndCreate(currentRow,20,"");
			setCellColors(currentRow, currentRow.getCell(20), 20, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(21)) == null || df.formatCellValue(currentRow.getCell(21)) == "") {
			stdObj.setEwsYn("2");
//			currentRow.getCell(21).setCellValue("2");
			checkCellAndCreate(currentRow,21,"2");
		} else {
			stdObj.setEwsYn(getCellValue(currentRow.getCell(21)));
		}
		
		
		
		if (customFxcelValidator.numberValidation(mObject, "ewsYn", checkNullandTrim(stdObj.getEwsYn()), 2).get("ewsYn")
				.get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->23");
//			currentRow.getCell(21).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(21), 21, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(21), 21, correctCellStyle);
		}

		stdObj.setCwsnYn(getCellValue(currentRow.getCell(22)));
		if (customFxcelValidator.numberValidation(mObject, "cwsnYn", checkNullandTrim(stdObj.getCwsnYn()), 2)
				.get("cwsnYn").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->24");
//			currentRow.getCell(22).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(22), 22, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(22), 22, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(23)) == null || df.formatCellValue(currentRow.getCell(23)) == "") {
			stdObj.setImpairmentType("0");
		} else {
			stdObj.setImpairmentType(getCellValue(currentRow.getCell(23)));
		}
		
		if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
			
//			System.out.println("stdObj.getImpairmentType()---->"+stdObj.getImpairmentType());
//			System.out.println("Check imperial---->"+customFxcelValidator
//					.jsonValidation(mObject, "impairmentType", checkNullandTrim(stdObj.getImpairmentType()))
//					.get("impairmentType").get("s"));
			
			if (customFxcelValidator
					.jsonValidation(mObject, "impairmentType", checkNullandTrim(stdObj.getImpairmentType()))
					.get("impairmentType").get("s").equalsIgnoreCase("0")) {
//				System.out.println("In set color---->25");
				System.out.println("For color");
//				currentRow.getCell(23).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(23), 23, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(23), 23, correctCellStyle);
			}
		} else {
//			customFxcelValidator.blankAndTrueValidation(mObject, "impairmentType", "");
			
//			currentRow.getCell(23).setCellValue("");
			checkCellAndCreate(currentRow,23,"");
			setCellColors(currentRow, currentRow.getCell(23), 23, correctCellStyle);
		}
		if (df.formatCellValue(currentRow.getCell(24)) == null || df.formatCellValue(currentRow.getCell(24)) == "") {
			stdObj.setNatIndYn("2");
//			System.out.println("row number--->"+currentRow.getRowNum());
//			currentRow.getCell(24).setCellValue("2");
			checkCellAndCreate(currentRow,24,"2");
		} else {
			stdObj.setNatIndYn(getCellValue(currentRow.getCell(24)));
		}
		if (customFxcelValidator.numberValidation(mObject, "natIndYn", checkNullandTrim(stdObj.getNatIndYn()), 2)
				.get("natIndYn").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->26");
//			currentRow.getCell(24).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(24), 24, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(24), 24, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(25)) == null || df.formatCellValue(currentRow.getCell(25)) == "") {
			stdObj.setOoscYn("2");
//			currentRow.getCell(25).setCellValue("2");
			checkCellAndCreate(currentRow,25,"2");
		} else {
			stdObj.setOoscYn(getCellValue(currentRow.getCell(25)));
		}
		if (customFxcelValidator.numberValidation(mObject, "ooscYn", checkNullandTrim(stdObj.getOoscYn()), 2)
				.get("ooscYn").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->27");
//			currentRow.getCell(25).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(25), 25, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(25), 25, correctCellStyle);
		}

//		System.out.println("Main streams--->"+currentRow.getCell(26));
		if(stdObj.getOoscYn() !=null && stdObj.getOoscYn() !="" && stdObj.getOoscYn().equalsIgnoreCase("2") ) {
			stdObj.setOoscMainstreamedYn("0");
//			currentRow.getCell(26).setCellValue("0");
			checkCellAndCreate(currentRow,26,"0");
		}
//		else if (df.formatCellValue(currentRow.getCell(26)) == null || df.formatCellValue(currentRow.getCell(26)) == "") {
//			stdObj.setOoscMainstreamedYn("0");
//			currentRow.getCell(26).setCellValue("0");
//		}
		else {
			stdObj.setOoscMainstreamedYn(getCellValue(currentRow.getCell(26)));
		}
		
		if(stdObj.getOoscYn() !=null && stdObj.getOoscYn().equalsIgnoreCase("1")) {
		if (customFxcelValidator
				.numberValidation(mObject, "ooscMainstreamedYn", checkNullandTrim(stdObj.getOoscMainstreamedYn()), 2)
				.get("ooscMainstreamedYn").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(26), 26, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(26), 26, correctCellStyle);
		}
		}else {
			
//			currentRow.getCell(26).setCellValue("");
			checkCellAndCreate(currentRow,26,"");
			setCellColors(currentRow, currentRow.getCell(26), 26, correctCellStyle);
		}

		stdObj.setAdmnNumber(getCellValue(currentRow.getCell(27)));
		if (customFxcelValidator
				.admisionNumberValidation(mObject, "admnNumber", checkNullandTrim(stdObj.getAdmnNumber()))
				.get("admnNumber").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->28");
//			currentRow.getCell(26).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(27), 27, correctCellStyle);
		}

//		DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");

		if (currentRow.getCell(28) != null && checkNull(currentRow.getCell(28).toString()) != "") {
			java.util.Date admisionDate = null;
			try {
				if (currentRow.getCell(28).getCellType() == CellType.NUMERIC) {
					admisionDate = currentRow.getCell(28).getDateCellValue();
					stdObj.setAdmnStartDate(dff.format(admisionDate));
				} else if (currentRow.getCell(28).getCellType() == CellType.STRING) {
					stdObj.setAdmnStartDate(currentRow.getCell(28).getStringCellValue());
				}
				
				
				if(stdObj.getAdmnStartDate() !=null && stdObj.getAdmnStartDate() !="" && sObj.getRowValue().get(0).get("session_start_date") !=null && sObj.getRowValue().get(0).get("session_end_date") !=null ) {
					DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					DateTimeFormatter adminsionformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					LocalDate sessionStartDate = LocalDate.parse(String.valueOf(sObj.getRowValue().get(0).get("session_start_date")), dbformatter);
					LocalDate sessionEndDate = LocalDate.parse(String.valueOf(sObj.getRowValue().get(0).get("session_end_date")), dbformatter);
					LocalDate todayDate = LocalDate.now();
//					System.out.println("stdObj.getAdmnStartDate()--->"+stdObj.getAdmnStartDate());
					LocalDate admisionsDate = LocalDate.parse(String.valueOf(stdObj.getAdmnStartDate()), adminsionformatter);
				if(!admisionsDate.isAfter(todayDate) &&  !admisionsDate.isAfter(sessionEndDate) &&  (admisionsDate.isAfter(sessionStartDate) || admisionsDate.isEqual(sessionStartDate))) {
//					System.out.println("in if condition for admission date");
					if (currentRow.getCell(28).getCellType() == CellType.NUMERIC) {
						checkCellAndCreate(currentRow,28,dff.format(admisionDate));
					} else if (currentRow.getCell(28).getCellType() == CellType.STRING) {

						checkCellAndCreate(currentRow,28,currentRow.getCell(28).getStringCellValue());
					}
					setCellColors(currentRow, currentRow.getCell(28), 28, correctCellStyle);
					
					
					System.out.println("Adminsion date false");
					
				}else {
					if (currentRow.getCell(28).getCellType() == CellType.NUMERIC) {
						checkCellAndCreate(currentRow,28,dff.format(admisionDate));
					} else if (currentRow.getCell(28).getCellType() == CellType.STRING) {
						checkCellAndCreate(currentRow,28,currentRow.getCell(28).getStringCellValue());
					}
					setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
					System.out.println("Adminsion date true");
				}
				}
				
//				if (customFxcelValidator
//						.dateValidation(mObject, "admnStartDate", checkNullandTrim(stdObj.getAdmnStartDate()))
//						.get("admnStartDate").get("s").equalsIgnoreCase("0")) {
//					
//					if (currentRow.getCell(28).getCellType() == CellType.NUMERIC) {
//
//						checkCellAndCreate(currentRow,28,dff.format(admisionDate));
//					} else if (currentRow.getCell(28).getCellType() == CellType.STRING) {
//
//						checkCellAndCreate(currentRow,28,currentRow.getCell(28).getStringCellValue());
//					}
//					setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
//				} else {
//					
//					if (currentRow.getCell(28).getCellType() == CellType.NUMERIC) {
//						checkCellAndCreate(currentRow,28,dff.format(admisionDate));
//					} else if (currentRow.getCell(28).getCellType() == CellType.STRING) {
//						checkCellAndCreate(currentRow,28,currentRow.getCell(28).getStringCellValue());
//					}
//					setCellColors(currentRow, currentRow.getCell(28), 28, correctCellStyle);
//				}
				
				
			} catch (Exception ex) {
				setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
				ex.printStackTrace();
			}
		} else {
			setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
		}

		
		if(stdObj.getClassId() !=null && (stdObj.getClassId().equalsIgnoreCase("11") || stdObj.getClassId().equalsIgnoreCase("12"))) {
		stdObj.setAcdemicStream(getCellValue(currentRow.getCell(29)));
		}else {
			
//			System.out.println();
		stdObj.setAcdemicStream("0");
		checkCellAndCreate(currentRow,29,"0");
		}

//		System.out.println("In student stream--->" + stdObj.getAcdemicStream());
		if (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
				|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) {
//			if (customFxcelValidator
//					.numberValidation(mObject, "acdemicStream",
//							checkNullandTrim(stdObj.getAcdemicStream()), 5)
//					.get("acdemicStream").get("s").equalsIgnoreCase("0")) {
//				setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
//			}else {
//				setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
//			}

			if (stdObj.getAcdemicStream() != null && stdObj.getAcdemicStream().equalsIgnoreCase("1")) {
				if (sObj.getRowValue().get(0).get("strm_arts") != null
						&& sObj.getRowValue().get(0).get("strm_arts") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_arts"))) == 1) {
					setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
				}
			} else if (stdObj.getAcdemicStream() != null && stdObj.getAcdemicStream().equalsIgnoreCase("2")) {
				if (sObj.getRowValue().get(0).get("strm_science") != null
						&& sObj.getRowValue().get(0).get("strm_science") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_science"))) == 1) {
					setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
				}
			} else if (stdObj.getAcdemicStream() != null && stdObj.getAcdemicStream().equalsIgnoreCase("3")) {
				if (sObj.getRowValue().get(0).get("strm_commerce") != null
						&& sObj.getRowValue().get(0).get("strm_commerce") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_commerce"))) == 1) {
					setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
				}
			} else if (stdObj.getAcdemicStream() != null && stdObj.getAcdemicStream().equalsIgnoreCase("4")) {
				if (sObj.getRowValue().get(0).get("strm_vocational") != null
						&& sObj.getRowValue().get(0).get("strm_vocational") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_vocational"))) == 1) {
					setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
				}
			} else if (stdObj.getAcdemicStream() != null && stdObj.getAcdemicStream().equalsIgnoreCase("5")) {
				if (sObj.getRowValue().get(0).get("strm_other") != null
						&& sObj.getRowValue().get(0).get("strm_other") != ""
						&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("strm_other"))) == 1) {
					setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
				}
			} else {
				setCellColors(currentRow, currentRow.getCell(29), 29, cellStyle);
			}
		} else {
			checkCellAndCreate(currentRow,29,"");
//			currentRow.getCell(29).setCellValue("");
		}

		stdObj.setEnrStatusPy(getCellValue(currentRow.getCell(30)));

		
		if(stdObj.getEnrStatusPy() !=null && stdObj.getEnrStatusPy().equalsIgnoreCase("3")) {
			if(stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3") || stdObj.getClassId().equalsIgnoreCase("1")) {
				setCellColors(currentRow, currentRow.getCell(30), 30, correctCellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(30), 30, cellStyle);
			}
		}
		else if (customFxcelValidator.numberValidation(mObject, "enrStatusPy", checkNullandTrim(stdObj.getEnrStatusPy()), 4)
				.get("enrStatusPy").get("s").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->31");
//			currentRow.getCell(29).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(30), 30, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(30), 30, correctCellStyle);
		}

		
		if(stdObj.getClassId() !=null && (stdObj.getEnrStatusPy().equalsIgnoreCase("4") || stdObj.getEnrStatusPy().equalsIgnoreCase("3") || stdObj.getClassId().equalsIgnoreCase("PP") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
			stdObj.setClassPy("");
			checkCellAndCreate(currentRow,31,"");
		}else {
		stdObj.setClassPy(getCellValue(currentRow.getCell(31)));
		}
		
		if (stdObj.getEnrStatusPy().equalsIgnoreCase("1") || stdObj.getEnrStatusPy().equalsIgnoreCase("2")) {
			
			if ((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("PP") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))
					&& (stdObj.getClassPy().equalsIgnoreCase("0") || stdObj.getClassPy().equalsIgnoreCase("PP") || stdObj.getClassPy().equalsIgnoreCase("PP1") || stdObj.getClassPy().equalsIgnoreCase("PP2") || stdObj.getClassPy().equalsIgnoreCase("PP3")   || (stdObj.getClassPy() ==null || stdObj.getClassPy().equalsIgnoreCase("")))) {
				setCellColors(currentRow, currentRow.getCell(31), 31, correctCellStyle);
			}else if(stdObj.getClassId().equalsIgnoreCase("1") ) {
				
				if((stdObj.getClassPy().equalsIgnoreCase("0") || stdObj.getClassPy().equalsIgnoreCase("1") || stdObj.getClassPy().equalsIgnoreCase("PP") || stdObj.getClassPy().equalsIgnoreCase("PP1") || stdObj.getClassPy().equalsIgnoreCase("PP2") || stdObj.getClassPy().equalsIgnoreCase("PP3"))) {
//					checkCellAndCreate(currentRow,31,df.formatCellValue(currentRow.getCell(31)));
					setCellColors(currentRow, currentRow.getCell(31), 31, correctCellStyle);	
				}else {
					setCellColors(currentRow, currentRow.getCell(31), 31, cellStyle);
				}
				
			} else {
				try {
					int stClass = Integer.parseInt(classCheck(stdObj.getClassId()));
					if (stClass > 0 && stClass <= 12 && (stClass == Integer.parseInt(stdObj.getClassPy())
							|| stClass == Integer.parseInt(stdObj.getClassPy()) + 1)) {
						setCellColors(currentRow, currentRow.getCell(31), 31, correctCellStyle);
					} else {
//						currentRow.getCell(30).setCellStyle(cellStyle);
						setCellColors(currentRow, currentRow.getCell(31), 31, cellStyle);
					}
				} catch (Exception ex) {
//					currentRow.getCell(30).setCellStyle(cellStyle);
					setCellColors(currentRow, currentRow.getCell(31), 31, cellStyle);
					ex.printStackTrace();
				}
			}
		}

		stdObj.setEnrTypeCy(getCellValue(currentRow.getCell(32)));
		if (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) == 5) {
			if (customFxcelValidator.numberValidation(mObject, "enrTypeCy", checkNullandTrim(stdObj.getEnrTypeCy()), 5)
					.get("enrTypeCy").get("s").equalsIgnoreCase("0")) {
//				System.out.println("In set color---->33");
//				currentRow.getCell(31).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(32), 32, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(32), 32, correctCellStyle);
			}
		} else {
//			currentRow.getCell(32).setCellValue("");
			
			checkCellAndCreate(currentRow,32,"");
			setCellColors(currentRow, currentRow.getCell(32), 32, correctCellStyle);
		}

		
		
		
		
		
		
		if(stdObj.getEnrStatusPy() !=null &&  (stdObj.getEnrStatusPy().equalsIgnoreCase("3") || stdObj.getEnrStatusPy().equalsIgnoreCase("4")) ) {
			stdObj.setExamAppearedPyYn("9");
			checkCellAndCreate(currentRow,33,"9");
		}else {
			stdObj.setExamAppearedPyYn(getCellValue(currentRow.getCell(33)));	
		}
		
		if(stdObj.getEnrStatusPy() !=null && (stdObj.getEnrStatusPy().equalsIgnoreCase("1") || stdObj.getEnrStatusPy().equalsIgnoreCase("2")) ) {
		
			if((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
				if(stdObj.getExamAppearedPyYn() ==null || stdObj.getExamAppearedPyYn().equalsIgnoreCase("")) {
					setCellColors(currentRow, currentRow.getCell(33), 33, correctCellStyle);
					
				}else {
					if(customFxcelValidator
							.numberValidation(mObject, "examAppearedPyYn", checkNullandTrim(stdObj.getExamAppearedPyYn()), 2)
							.get("examAppearedPyYn").get("s").equalsIgnoreCase("0")) {
						setCellColors(currentRow, currentRow.getCell(33), 33, cellStyle);
					}
				}
			}
			else if (customFxcelValidator
				.numberValidation(mObject, "examAppearedPyYn", checkNullandTrim(stdObj.getExamAppearedPyYn()), 2)
				.get("examAppearedPyYn").get("s").equalsIgnoreCase("0")) {

			setCellColors(currentRow, currentRow.getCell(33), 33, cellStyle);
		} 
			
//			else {
//			setCellColors(currentRow, currentRow.getCell(33), 33, correctCellStyle);
//		}
		}else {
			checkCellAndCreate(currentRow,33,"");
			setCellColors(currentRow, currentRow.getCell(33), 33, correctCellStyle);
//			currentRow.getCell(33).setCellValue("");
			
		}

		
		
		
		if(stdObj.getEnrStatusPy() !=null &&  (stdObj.getEnrStatusPy().equalsIgnoreCase("3") || stdObj.getEnrStatusPy().equalsIgnoreCase("4")) ) {
		stdObj.setExamResultPy("0");
		checkCellAndCreate(currentRow,34,"0");
		}else {
			stdObj.setExamResultPy(getCellValue(currentRow.getCell(34)));
		}
		if (stdObj.getExamAppearedPyYn().equalsIgnoreCase("1")) {
		if(stdObj.getEnrStatusPy() !=null && (stdObj.getEnrStatusPy().equalsIgnoreCase("1") || stdObj.getEnrStatusPy().equalsIgnoreCase("2")) ) {
	if((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
		
		if(stdObj.getExamResultPy()==null || stdObj.getExamResultPy().equalsIgnoreCase("")) {
			setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
		}else {
			if (customFxcelValidator
					.numberValidation(mObject, "examResultPy", checkNullandTrim(stdObj.getExamResultPy()), 4)
					.get("examResultPy").get("s").equalsIgnoreCase("0")) {
				setCellColors(currentRow, currentRow.getCell(34), 34, cellStyle);
			}
		}
	}
			
		else if (customFxcelValidator
				.numberValidation(mObject, "examResultPy", checkNullandTrim(stdObj.getExamResultPy()), 4)
				.get("examResultPy").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(34), 34, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
		}
		}else {
			checkCellAndCreate(currentRow,34,"");
			setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
//			currentRow.getCell(34).setCellValue("");
		
		}
		
	}else {
		checkCellAndCreate(currentRow,34,"");
		setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
	}

		if(stdObj.getEnrStatusPy() !=null && (stdObj.getEnrStatusPy().equalsIgnoreCase("1") || stdObj.getEnrStatusPy().equalsIgnoreCase("2")) ) {
			if(stdObj.getClassPy() !=null &&  (stdObj.getEnrStatusPy().equalsIgnoreCase("3") || stdObj.getEnrStatusPy().equalsIgnoreCase("4")) ) {
			stdObj.setExamMarksPy("0");
			checkCellAndCreate(currentRow,35,"0");
			}else {
				try {
				stdObj.setExamMarksPy(String.valueOf(Math.round(Float.parseFloat(getCellValue(currentRow.getCell(35))))));
				}catch(Exception ex) {
					stdObj.setExamMarksPy(getCellValue(currentRow.getCell(35)));
					ex.printStackTrace();
				}
			}
			
			if (stdObj.getExamAppearedPyYn().equalsIgnoreCase("1")) {	
		if((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
			
			if(stdObj.getExamMarksPy()==null || stdObj.getExamMarksPy().equalsIgnoreCase("")) {
				setCellColors(currentRow, currentRow.getCell(35), 35, correctCellStyle);	
			}else {
			 if (customFxcelValidator
						.numberValidation(mObject, "examMarksPy", checkNullandTrim(stdObj.getExamMarksPy()), 100)
						.get("examMarksPy").get("s").equalsIgnoreCase("0")) {
					setCellColors(currentRow, currentRow.getCell(35), 35, cellStyle);
			 }
			}
			
		}
		else if (customFxcelValidator
				.numberValidation(mObject, "examMarksPy", checkNullandTrim(stdObj.getExamMarksPy()), 100)
				.get("examMarksPy").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(35), 35, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(35), 35, correctCellStyle);
		}
		}
		}else {
			checkCellAndCreate(currentRow,35,"");
			setCellColors(currentRow, currentRow.getCell(35), 35, correctCellStyle);
//			currentRow.getCell(35).setCellValue("");
			
		}
		
		
		
		
		

		if(stdObj.getEnrStatusPy() !=null &&  (stdObj.getEnrStatusPy().equalsIgnoreCase("3") || stdObj.getEnrStatusPy().equalsIgnoreCase("4")) ) {
		stdObj.setAttendencePy("0");
		checkCellAndCreate(currentRow,36,"0");
		}else {
			stdObj.setAttendencePy(getCellValue(currentRow.getCell(36)));
		}
		
		if((stdObj.getClassId().equalsIgnoreCase("0") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3"))) {
			
			
			if(stdObj.getAttendencePy()==null || stdObj.getAttendencePy().equalsIgnoreCase("")) {
				setCellColors(currentRow, currentRow.getCell(36), 36, correctCellStyle);
			}else {
				 if (customFxcelValidator
						.numberValidation(mObject, "attendencePy", checkNullandTrim(stdObj.getAttendencePy()), 365)
						.get("attendencePy").get("s").equalsIgnoreCase("0")) {
					setCellColors(currentRow, currentRow.getCell(36), 36, cellStyle);
				}
			}
		}
		else if (customFxcelValidator
				.numberValidation(mObject, "attendencePy", checkNullandTrim(stdObj.getAttendencePy()), 365)
				.get("attendencePy").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(36), 36, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(36), 36, correctCellStyle);
		}

//		System.out.println("before uniform default check----"+sObj.getRowValue().get(0).get("sch_mgmt_center_id"));
		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		if (df.formatCellValue(currentRow.getCell(37)) == null || df.formatCellValue(currentRow.getCell(37)) == "") {
			stdObj.setUniformFacProvided("2");
			
			System.out.println("uniform default check----");
			checkCellAndCreate(currentRow,37,"2");
		} else {
			stdObj.setUniformFacProvided(getCellValue(currentRow.getCell(37)));
		}
		}
		
		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		

			if (customFxcelValidator
				.numberValidation(mObject, "uniformFacProvided", checkNullandTrim(stdObj.getUniformFacProvided()), 2)
				.get("uniformFacProvided").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(37), 37, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(37), 37, correctCellStyle);
		}
		}else {
			checkCellAndCreate(currentRow,37,"");
			setCellColors(currentRow, currentRow.getCell(37), 37, correctCellStyle);
		}
		
		
		
		

		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		if (df.formatCellValue(currentRow.getCell(38)) == null || df.formatCellValue(currentRow.getCell(38)) == "") {
			stdObj.setTextBoxFacProvided("2");
			checkCellAndCreate(currentRow,38,"2");
		} else {
			stdObj.setTextBoxFacProvided(getCellValue(currentRow.getCell(38)));
		}
		}
		
		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		if (customFxcelValidator
				.numberValidation(mObject, "textBoxFacProvided", checkNullandTrim(stdObj.getTextBoxFacProvided()), 2)
				.get("textBoxFacProvided").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(38), 38, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(38), 38, correctCellStyle);
		}
		}else {
			checkCellAndCreate(currentRow,38,"");
			setCellColors(currentRow, currentRow.getCell(38), 38, correctCellStyle);
		}

//		System.out.println("Is central scholor ship"+df.formatCellValue(currentRow.getCell(39)));
		if (df.formatCellValue(currentRow.getCell(39)) == null || df.formatCellValue(currentRow.getCell(39)) == "") {
			stdObj.setCentrlSchlrshpYn("2");
			checkCellAndCreate(currentRow,39,"2");
//			System.out.println("in true");
		} else {
//			System.out.println("in false");
			checkCellAndCreate(currentRow,39,getCellValue(currentRow.getCell(39)));
			stdObj.setCentrlSchlrshpYn(getCellValue(currentRow.getCell(39)));
		}
		
		
		if (customFxcelValidator
				.numberValidation(mObject, "centrlSchlrshpYn", checkNullandTrim(stdObj.getCentrlSchlrshpYn()), 2)
				.get("centrlSchlrshpYn").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(39), 39, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(39), 39, correctCellStyle);
		}

		if(stdObj.getCentrlSchlrshpYn().equalsIgnoreCase("2")) {
			stdObj.setCentrlSchlrshpId("0");
			checkCellAndCreate(currentRow,40,"0");
		}else {
		if (df.formatCellValue(currentRow.getCell(40)) == null || df.formatCellValue(currentRow.getCell(40)) == "") {
			stdObj.setCentrlSchlrshpId("0");
			checkCellAndCreate(currentRow,40,"0");
		} else {
			stdObj.setCentrlSchlrshpId(getCellValue(currentRow.getCell(40)));
		}
		}
		
		
		if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")) {
			if (customFxcelValidator
					.numberValidation(mObject, "centrlSchlrshpId", checkNullandTrim(stdObj.getCentrlSchlrshpId()), 15)
					.get("centrlSchlrshpId").get("s").equalsIgnoreCase("0")) {
//				currentRow.getCell(39).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(40), 40, cellStyle);
			} else {
//				currentRow.getCell(40).setCellValue("");
				checkCellAndCreate(currentRow,40,df.formatCellValue(currentRow.getCell(40)));
				setCellColors(currentRow, currentRow.getCell(40), 40, correctCellStyle);
				
			}
		} else {
			if (currentRow.getCell(40) == null) {
				currentRow.createCell(40);
			}
//			currentRow.getCell(41).setCellValue("");
			checkCellAndCreate(currentRow,40,"");
			setCellColors(currentRow, currentRow.getCell(40), 40, correctCellStyle);
			
		}

		if (df.formatCellValue(currentRow.getCell(41)) == null || df.formatCellValue(currentRow.getCell(41)) == "") {
			stdObj.setStateSchlrshpYn("2");
			checkCellAndCreate(currentRow,41,"2");
		} else {
			stdObj.setStateSchlrshpYn(getCellValue(currentRow.getCell(41)));
		}
		if (customFxcelValidator
				.numberValidation(mObject, "stateSchlrshpYn", checkNullandTrim(stdObj.getStateSchlrshpYn()), 2)
				.get("stateSchlrshpYn").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(40).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(41), 41, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(41), 41, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(42)) == null || df.formatCellValue(currentRow.getCell(42)) == "") {
			stdObj.setOtherSchlrshpYn("2");
			checkCellAndCreate(currentRow,42,"2");
		} else {
			stdObj.setOtherSchlrshpYn(getCellValue(currentRow.getCell(42)));
		}
		if (customFxcelValidator
				.numberValidation(mObject, "otherSchlrshpYn", checkNullandTrim(stdObj.getOtherSchlrshpYn()), 2)
				.get("otherSchlrshpYn").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(41).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(42), 42, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(42), 42, correctCellStyle);
		}

		stdObj.setSchlrshpAmount(getCellValue(currentRow.getCell(43)));
		if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
			if (customFxcelValidator
					.numberValidation(mObject, "schlrshpAmount", checkNullandTrim(stdObj.getSchlrshpAmount()), 50000)
					.get("schlrshpAmount").get("s").equalsIgnoreCase("0")) {
//				currentRow.getCell(42).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(43), 43, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(43), 43, correctCellStyle);
			}
		} else {
			if (currentRow.getCell(43) == null) {
				currentRow.createCell(43);
			}
//			currentRow.getCell(43).setCellValue("");
			checkCellAndCreate(currentRow,43,"");
			setCellColors(currentRow, currentRow.getCell(43), 43, correctCellStyle);
			
		}

		if (sObj.getRowValue().get(0).get("sch_mgmt_center_id") !=null && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
		stdObj.setFacProvidedCwsn(getCellValue(currentRow.getCell(44)));
		}
		
		
		
		if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1") && (Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 5 && Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("sch_mgmt_center_id"))) != 8)) {
//			if (customFxcelValidator
//					.numberValidation(mObject, "facProvidedCwsn", checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
//					.get("facProvidedCwsn").get("s").equalsIgnoreCase("0")) {
//				setCellColors(currentRow, currentRow.getCell(44), 44, cellStyle);
//			} else {
//				setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
//			}
				
				if(stdObj.getFacProvidedCwsn().equalsIgnoreCase("")) {
					setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
				}else if(customFxcelValidator
						.numberValidation(mObject, "facProvidedCwsn", checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
						.get("facProvidedCwsn").get("s").equalsIgnoreCase("0")){
					setCellColors(currentRow, currentRow.getCell(44), 44, cellStyle);
				}else {
					setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
				}
				
				
		}else if(checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("2")) {
			checkCellAndCreate(currentRow,44,"");
//			setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
		}
		

		if (df.formatCellValue(currentRow.getCell(45)) == null || df.formatCellValue(currentRow.getCell(45)) == "") {
			stdObj.setScrndFrSld("2");
			checkCellAndCreate(currentRow,45,"2");
		} else {
			stdObj.setScrndFrSld(getCellValue(currentRow.getCell(45)));
		}
		if (checkNull(stdObj.getCentrlSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stdObj.getStateSchlrshpYn()).equalsIgnoreCase("1")
				|| checkNull(stdObj.getOtherSchlrshpYn()).equalsIgnoreCase("1")) {
			if (customFxcelValidator
					.numberValidation(mObject, "scrndFrSld", checkNullandTrim(stdObj.getScrndFrSld()), 2)
					.get("scrndFrSld").get("s").equalsIgnoreCase("0")) {
//				currentRow.getCell(44).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(45), 45, cellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(45), 45, correctCellStyle);
			}
		}
		
		if (df.formatCellValue(currentRow.getCell(46)) == null || df.formatCellValue(currentRow.getCell(46)) == "" ) {
			stdObj.setSldType("0");
			checkCellAndCreate(currentRow,46,"0");
		} else {
			stdObj.setSldType(getCellValue(currentRow.getCell(46)));
		}
		
		
		if(stdObj.getScrndFrSld().equalsIgnoreCase("2")) {
			checkCellAndCreate(currentRow,46,"");
			setCellColors(currentRow, currentRow.getCell(46), 46, correctCellStyle);
		}else if (customFxcelValidator.numberValidation(mObject, "sldType", checkNullandTrim(stdObj.getSldType()), 3)
				.get("sldType").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(45).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(46), 46, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(46), 46, correctCellStyle);
		}
		if (df.formatCellValue(currentRow.getCell(47)) == null || df.formatCellValue(currentRow.getCell(47)) == "") {
			stdObj.setScrndFrAsd("2");
			checkCellAndCreate(currentRow,47,"2");
		} else {
			stdObj.setScrndFrAsd(getCellValue(currentRow.getCell(47)));
		}
		if (customFxcelValidator.numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stdObj.getScrndFrAsd()), 2)
				.get("scrndFrAsd").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(46).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(47), 47, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(47), 47, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(48)) == null || df.formatCellValue(currentRow.getCell(48)) == "") {
			stdObj.setScrndFrAdhd("2");
			checkCellAndCreate(currentRow,48,"2");
		} else {
			stdObj.setScrndFrAdhd(getCellValue(currentRow.getCell(48)));
		}
		if (customFxcelValidator.numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stdObj.getScrndFrAdhd()), 2)
				.get("scrndFrAdhd").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(47).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(48), 48, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(48), 48, correctCellStyle);
		}

		if (df.formatCellValue(currentRow.getCell(49)) == null || df.formatCellValue(currentRow.getCell(49)) == "") {
			stdObj.setIsEcActivity("2");
		} else {
			stdObj.setIsEcActivity(getCellValue(currentRow.getCell(49)));
		}
		if (customFxcelValidator
				.numberValidation(mObject, "isEcActivity", checkNullandTrim(stdObj.getIsEcActivity()), 2)
				.get("isEcActivity").get("s").equalsIgnoreCase("0")) {
//			currentRow.getCell(48).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(49), 49, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(49), 49, correctCellStyle);
		}

		
		
		if ((df.formatCellValue(currentRow.getCell(50)) == null || df.formatCellValue(currentRow.getCell(50)) == "")
				&& sObj.getRowValue().get(0).get("is_vocational_active") != null
				&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))) == 1) {
			stdObj.setVocYn("2");
			checkCellAndCreate(currentRow,50,"2");
		} else if ((df.formatCellValue(currentRow.getCell(50)) == null
				|| df.formatCellValue(currentRow.getCell(50)) == "")
				&& sObj.getRowValue().get(0).get("is_vocational_active") != null
				&& Integer.parseInt(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))) == 2) {
			stdObj.setVocYn("9");
		} else {
			stdObj.setVocYn(getCellValue(currentRow.getCell(50)));
		}
		
		
		if (stdObj.getClassId() != null && sObj.getRowValue() != null
				&& sObj.getRowValue().get(0).get("is_vocational_active") != null
				&& Integer.parseInt(
						checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) {
			if((checkNull(stdObj.getClassId()).equalsIgnoreCase("9")
						|| checkNull(stdObj.getClassId()).equalsIgnoreCase("10")) && (Integer.parseInt(
								checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 || Integer.parseInt(
										checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)) {
				if (customFxcelValidator.numberValidation(mObject, "vocYn", checkNullandTrim(stdObj.getVocYn()), 2)
						.get("vocYn").get("s").equalsIgnoreCase("0")) {
					if (currentRow.getCell(50) == null) {
						currentRow.createCell(50);
					}
					setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);
				}
			}else if((checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
					|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) && (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)){
				if (customFxcelValidator.numberValidation(mObject, "vocYn", checkNullandTrim(stdObj.getVocYn()), 2)
						.get("vocYn").get("s").equalsIgnoreCase("0")) {
					if (currentRow.getCell(50) == null) {
						currentRow.createCell(50);
					}
					setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
				} else {
					setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);
				}
			}else {
				checkCellAndCreate(currentRow,50,"");
				setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);
			}
		} else {
			checkCellAndCreate(currentRow,50,"");
			setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);	
		}
		
if(stdObj.getVocYn().equalsIgnoreCase("1")) {
		stdObj.setSector(getCellValue(currentRow.getCell(51)));
}else {
	stdObj.setSector("");
}
		System.out.println("vocational true---->"+sObj.getRowValue().get(0).get("is_vocational_active"));
//		System.out.println("Vocational--->"+stdObj.getVocYn());
		
		try {
			if(stdObj.getVocYn() !=null && stdObj.getVocYn() !="" && stdObj.getVocYn().equalsIgnoreCase("1") && Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
			if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null 
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) 
					&& ((Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 ) ||  Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3 ) && (checkNull(stdObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("10"))) {
//				System.out.println("lower sector true");
				try {
				if (lowerSector.get(String.valueOf(checkNull(stdObj.getSector())))) {
					checkCellAndCreate(currentRow,51,stdObj.getSector());
					setCellColors(currentRow, currentRow.getCell(51), 51, correctCellStyle);
					
				} else {
					setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
				}
				}catch(Exception ex) {
					setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
					ex.printStackTrace();
				}

			} else if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1)
				&& (Integer.parseInt(
						checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
								checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)	&& (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12"))) {
//				System.out.println("higher sector true");
				try {
				if (higherSector.get(String.valueOf(checkNull(stdObj.getSector())))) {
					checkCellAndCreate(currentRow,51,stdObj.getSector());
					setCellColors(currentRow, currentRow.getCell(51), 51, correctCellStyle);	
				} else {
					setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
				}
				}catch(Exception ex) {
					setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
					
				}
			}
			else {
				checkCellAndCreate(currentRow,51,"");
//				setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			}
			}else {
//				System.out.println("sector else");
				checkCellAndCreate(currentRow,51,"");
//				setCellColors(currentRow, currentRow.getCell(51), 51, correctCellStyle);	
//				setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			}

		} catch (Exception ex) {
			
			setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			ex.printStackTrace();
		}
		
		if(stdObj.getVocYn().equalsIgnoreCase("1")) {
		stdObj.setJobRole(getCellValue(currentRow.getCell(52)));
		}else {
			stdObj.setJobRole("");
		}
	try {
			if(stdObj.getVocYn() !=null && stdObj.getVocYn() !="" && stdObj.getVocYn().equalsIgnoreCase("1") && Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
			if (sObj.getRowValue().get(0).get("is_vocational_active") != null && sObj.getRowValue().get(0).get("vocational_grade") !=null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1) 
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 1 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3) && (checkNull(stdObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("10"))
					&& stdObj.getJobRole() != null &&  stdObj.getJobRole() !="") {
//				System.out.println("JOb-1");
				try {
				if (lowerSubSector.get(String.valueOf(checkNull(stdObj.getJobRole())))) {
					checkCellAndCreate(currentRow,52,stdObj.getJobRole());
					setCellColors(currentRow, currentRow.getCell(52), 52, correctCellStyle);
				
				} else {
					System.out.println("In job false");
					setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
				}
				}catch(Exception ex) {
					setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
					ex.printStackTrace();
				}
			} else if (sObj.getRowValue().get(0).get("is_vocational_active") != null
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active")))) == 1 )
					&& (Integer.parseInt(
							checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 2 || Integer.parseInt(
									checkNull(String.valueOf(sObj.getRowValue().get(0).get("vocational_grade")))) == 3)  && (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12"))
					&& stdObj.getJobRole() != null && stdObj.getJobRole() != "") {
				try {
//					System.out.println("Job-2-->"+stdObj.getJobRole());
				if (stdObj.getJobRole() !=null && higherSubSector.get(String.valueOf(checkNull(stdObj.getJobRole())))) {
					checkCellAndCreate(currentRow,52,stdObj.getJobRole());
					setCellColors(currentRow, currentRow.getCell(52), 52, correctCellStyle);
					
				} else {
					setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
				}
				}catch(Exception ex) {
					setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
					ex.printStackTrace();
				}
			}else {
				checkCellAndCreate(currentRow,52,"");
			}

			}else {
//				System.out.println("in else job");
				checkCellAndCreate(currentRow,52,"");
//				setCellColors(currentRow, currentRow.getCell(52), 52, correctCellStyle);			
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
			ex.printStackTrace();
		}
		
		
		
		
		
try {
	
		if (stdObj.getClassId() !=null && df.formatCellValue(currentRow.getCell(53)) == null || df.formatCellValue(currentRow.getCell(53)) == "" || (stdObj.getClassId().equalsIgnoreCase("PP") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3") || (Integer.parseInt((String.valueOf(stdObj.getClassId())))>0 && Integer.parseInt((String.valueOf(stdObj.getClassId())))<9))) {
			stdObj.setAppVocPy("0");
			checkCellAndCreate(currentRow,53,"0");
		} else {
			stdObj.setAppVocPy(df.formatCellValue(currentRow.getCell(53)));
		}
}catch(Exception ex) {
	stdObj.setAppVocPy(df.formatCellValue(currentRow.getCell(53)));
	ex.printStackTrace();
}	
		try {
			if(Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
				
			if(stdObj.getVocYn() !=null && stdObj.getVocYn() !="" && stdObj.getVocYn().equalsIgnoreCase("1")) {	
		if(stdObj.getClassId() !=null && df.formatCellValue(currentRow.getCell(53)) == null || df.formatCellValue(currentRow.getCell(53)) == "" || (stdObj.getClassId().equalsIgnoreCase("PP") || stdObj.getClassId().equalsIgnoreCase("PP1") || stdObj.getClassId().equalsIgnoreCase("PP2") || stdObj.getClassId().equalsIgnoreCase("PP3") || (Integer.parseInt((String.valueOf(stdObj.getClassId())))>0 && Integer.parseInt((String.valueOf(stdObj.getClassId())))<9))) {
			setCellColors(currentRow, currentRow.getCell(53), 53, correctCellStyle);
		}else if (customFxcelValidator.numberValidation(mObject, "appVocPy", checkNullandTrim(stdObj.getAppVocPy()), 4)
				.get("appVocPy").get("s").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(53), 53, cellStyle);
		} else {
			setCellColors(currentRow, currentRow.getCell(53), 53, correctCellStyle);
		}
			}else {
				checkCellAndCreate(currentRow,53,"0");
				setCellColors(currentRow, currentRow.getCell(53), 53, correctCellStyle);
			}
			
			}else {
				checkCellAndCreate(currentRow,53,"0");
				setCellColors(currentRow, currentRow.getCell(53), 53, correctCellStyle);	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		
		stdObj.setStudentStateCode(getCellValue(currentRow.getCell(54)));
		
		try {
			if(stdObj.getStudentStateCode() !=null && stdObj.getStudentStateCode() !="" && !stdObj.getStudentStateCode().isEmpty()) {
			if(customFxcelValidator.stringValidation(mObject, "studentStateCode", checkNullandTrim(stdObj.getAddress()),1,25)
					.get("studentStateCode").get("s").equalsIgnoreCase("0")) {
				setCellColors(currentRow, currentRow.getCell(54), 54, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(54), 54, correctCellStyle);
			}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		

		return stdObj;

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
	
	public long getAge(String dob,String dbDate) {
		System.out.println("dob--->"+dob+"-------"+dbDate);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dob.replaceAll("-", "/");
		LocalDate localDate = LocalDate.parse(date, formatter);
		LocalDate today = LocalDate.parse(dbDate, dbformatter);
		long age = ChronoUnit.YEARS.between(localDate, today);
		return age;
	}


	public static boolean StudentAgeClassValidation(Integer grade, Integer age, Integer cwsnYn) {
		
		 boolean status = false;
			if(age >= 2 && age <= 80) {
	            status = true;
	        }
			return status;
		
//		if(cwsnYn ==1) {
//			 boolean status = false;
//		        if(grade != null) {
//		            switch(grade) {
//		            case -3:
//		                if(age >= 2 && age <= 5) {
//		                    status = true;
//		                }
//		                break;
//		            case -2:
//		                if(age >= 2 && age <= 6) {
//		                    status = true;
//		                }
//		                break;
//		            case -1:
//		                if(age >= 3 && age <= 8) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 1:
//		                if(age >= 4 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 2:
//		                if(age >= 5 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 3:
//		                if(age >= 6 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 4:
//		                if(age >= 7 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 5:
//		                if(age >= 8 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 6:
//		                if(age >= 9 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 7:
//		                if(age >= 10 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 8:
//		                if(age >= 11 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 9:
//		                if(age >= 12 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 10:
//		                if(age >= 13 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 11:
//		                if(age >= 14 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		               
//		            case 12:
//		                if(age >= 15 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		            }
//		        }
//		        return status;
//		}else {
//			   boolean status = false;
//		        if(grade != null) {
//		            switch(grade) {
//		            case -3:
//		                if(age >= 2 && age <= 5) {
//		                    status = true;
//		                }
//		                break;
//		            case -2:
//		                if(age >= 2 && age <= 6) {
//		                    status = true;
//		                }
//		                break;
//		            case -1:
//		                if(age >= 3 && age <= 8) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 1:
//		                if(age >= 3 && age <= 15) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 2:
//		                if(age >= 4 && age <= 16) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 3:
//		                if(age >= 5 && age <= 17) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 4:
//		                if(age >= 6 && age <= 18) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 5:
//		                if(age >= 7 && age <= 19) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 6:
//		                if(age >= 8 && age <= 20) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 7:
//		                if(age >= 9 && age <= 21) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 8:
//		                if(age >= 10 && age <= 22) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 9:
//		                if(age >= 11 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 10:
//		                if(age >= 12 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 11:
//		                if(age >= 13 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		                 
//		            case 12:
//		                if(age >= 14 && age <= 30) {
//		                    status = true;
//		                }
//		                break;
//		            }
//		        }
//		        return status;
//		}
	}
	
	public void checkCellAndCreate(Row currentRow,Integer cellNumber,String value){
//		if(currentRow.getCell(cellNumber)==null) {
			currentRow.createCell(cellNumber).setCellValue(value);
//		}
	}
	
//	public Object getCellValue(Cell cell) {
//        if (cell != null) {
//            switch (cell.getCellType()) {
//            case cell.CELL_TYPE_STRING:
//                return cell.getStringCellValue();
//
//            case cell.CELL_TYPE_BOOLEAN:
//                return cell.getBooleanCellValue();
//
//            case cell.CELL_TYPE_NUMERIC:
//                return cell.getNumericCellValue();
//            }
//        }
//        return null;
//    }
	
	
	
	public String getCellValue(Cell cell) {
        if (cell != null) {
            switch (String.valueOf(cell.getCellType())) {
            case "STRING":
                return String.valueOf(df.formatCellValue(cell)).trim();

            case "BOOLEAN":
                return String.valueOf(df.formatCellValue(cell)).trim();

            case "NUMERIC":
            	try {
            			return df.formatCellValue(cell).trim();
            	}catch(Exception ex) {
            		ex.printStackTrace();
            		return df.formatCellValue(cell).trim();
            	}
            	
            case "FORMULA":
            	try {
            	return String.valueOf(cell.getStringCellValue()).trim();
            	}catch(Exception ex) {
            		ex.printStackTrace();
            		try {
            		return String.valueOf(cell.getNumericCellValue()).trim();
            		}catch(Exception e) {
            			return  df.formatCellValue(cell).trim();
            		}
            	}
            }
        }
        return "";
    }
	
	
	
}
