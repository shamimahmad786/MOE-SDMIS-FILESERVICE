package com.moe.sdmis.fileservice.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.moe.sdmis.fileservice.db.StaticReportBean;
import com.moe.sdmis.fileservice.modal.CommonBean;
import com.moe.sdmis.fileservice.validation.CustomFxcelValidator;

public class TemplateConfiguration1 {
	DataFormatter df = new DataFormatter();
	public CommonBean dataPrepration(Map<Integer, Boolean> mtongObj,CommonBean stdObj,Row currentRow,StaticReportBean sObj,StaticReportBean sectionsObj,StaticReportBean vocationObj,CellStyle cellStyle,CellStyle correctCellStyle,HashMap<String, Boolean> lowerSector,HashMap<String, Boolean> lowerSubSector,HashMap<String, Boolean> higherSector,HashMap<String, Boolean> higherSubSector,HashMap<String, String> sectionMap,Map<String, HashMap<String, String>> mObject ) {
		CustomFxcelValidator customFxcelValidator =new CustomFxcelValidator();
		stdObj.setClassId(df.formatCellValue(currentRow.getCell(0)));
		try {
			if (stdObj.getClassId() != null && !stdObj.getClassId().equalsIgnoreCase("null")
					&& Integer.parseInt(checkNull(
							String.valueOf(sObj.getRowValue().get(0).get("class_frm")))) <= Integer
									.parseInt(classCheck(checkNull(stdObj.getClassId())))
					&& Integer.parseInt(checkNull(
							String.valueOf(sObj.getRowValue().get(0).get("class_to")))) >= Integer
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

		stdObj.setSectionId(df.formatCellValue(currentRow.getCell(1)));
//		if (customFxcelValidator
//				.numberValidation(mObject, "sectionId", checkNullandTrim(stdObj.getSectionId()), 4)
//				.get("sectionId").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->3");
//			currentRow.getCell(1).setCellStyle(cellStyle);
//		}

		try {
			if (sectionMap.get(stdObj.getClassId()) != null && Integer
					.parseInt(checkNull(sectionMap.get(checkNull(stdObj.getClassId())))) >= Integer
							.parseInt(checkNull(String.valueOf(checkNull(stdObj.getSectionId()))))) {
				setCellColors(currentRow, currentRow.getCell(1), 1, correctCellStyle);
			} else {
				setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(1), 1, cellStyle);
		}

		stdObj.setRollNo(df.formatCellValue(currentRow.getCell(2)));
		if (customFxcelValidator
				.numberValidation(mObject, "rollNo", checkNullandTrim(stdObj.getRollNo()), 999)
				.get("rollNo").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->4");
//			currentRow.getCell(2).setCellStyle(cellStyle);
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

			setCellColors(currentRow, currentRow.getCell(3), 3, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(3), 3, correctCellStyle);
		}

		stdObj.setGender(df.formatCellValue(currentRow.getCell(4)));
		if (customFxcelValidator
				.numberValidation(mObject, "gender", checkNullandTrim(stdObj.getGender()), 3)
				.get("gender").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->6");
			setCellColors(currentRow, currentRow.getCell(4), 4, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(4), 4, correctCellStyle);
		}

		DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
//		System.out.println("values33----" + currentRow.getCell(5));

		if (currentRow.getCell(5) != null && checkNull(currentRow.getCell(5).toString()) !="") {
			java.util.Date d = null;
			try {
//				System.out.println("Date Cell Value--->"+currentRow.getCell(5));
				if(currentRow.getCell(5).getCellType()==CellType.NUMERIC) {
					d = currentRow.getCell(5).getDateCellValue();
					stdObj.setStudentDob(dff.format(d));
				}else if(currentRow.getCell(5).getCellType()==CellType.STRING){
					stdObj.setStudentDob(currentRow.getCell(5).getStringCellValue());
				}
		
			
			if (customFxcelValidator
					.dateValidation(mObject, "studentDob", checkNullandTrim(stdObj.getStudentDob()))
					.get("studentDob").get("status").equalsIgnoreCase("0")) {
				setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
				
				if(currentRow.getCell(5).getCellType()==CellType.NUMERIC) {
				
					currentRow.getCell(5).setCellValue(dff.format(d));
				}else if(currentRow.getCell(5).getCellType()==CellType.STRING){
				
					currentRow.getCell(5).setCellValue(currentRow.getCell(5).getStringCellValue());
				}
				
//				currentRow.getCell(5).setCellValue(dff.format(d));
			}else {
				setCellColors(currentRow, currentRow.getCell(5), 5, correctCellStyle);
//				currentRow.getCell(5).setCellValue(dff.format(d));
				if(currentRow.getCell(5).getCellType()==CellType.NUMERIC) {
					
					currentRow.getCell(5).setCellValue(dff.format(d));
				}else if(currentRow.getCell(5).getCellType()==CellType.STRING){
				
					currentRow.getCell(5).setCellValue(currentRow.getCell(5).getStringCellValue());
				}
			}
			}catch(Exception ex) {
				setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
//				currentRow.getCell(5).setCellValue(dff.format(d));
				ex.printStackTrace();
			}
		} else {
			setCellColors(currentRow, currentRow.getCell(5), 5, cellStyle);
		}

		stdObj.setMotherName(
				checkNull(df.formatCellValue(currentRow.getCell(6))).replaceAll("\\s+", " "));
		if (customFxcelValidator
				.stringNonSpecialValidation(mObject, "motherName",
						checkNullandTrim(stdObj.getMotherName()))
				.get("motherName").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->8");
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
//			System.out.println("In set color---->9");
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
//			System.out.println("In set color---->10");
			setCellColors(currentRow, currentRow.getCell(8), 8, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(8), 8, correctCellStyle);
		}

		stdObj.setAadhaarNo(df.formatCellValue(currentRow.getCell(9)));
		if (customFxcelValidator
				.adharValidation(mObject, "aadhaarNo", checkNullandTrim(stdObj.getAadhaarNo()))
				.get("aadhaarNo").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->11");
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
//			System.out.println("In set color---->13");
			setCellColors(currentRow, currentRow.getCell(11), 11, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(11), 11, correctCellStyle);
		}

		stdObj.setPincode(df.formatCellValue(currentRow.getCell(12)));
		if (customFxcelValidator
				.pincodeValidation(mObject, "pincode", checkNullandTrim(stdObj.getPincode()))
				.get("pincode").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->14");
			setCellColors(currentRow, currentRow.getCell(12), 12, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(12), 12, correctCellStyle);
		}

		stdObj.setMobileNo_1(df.formatCellValue(currentRow.getCell(13)));
		if (customFxcelValidator
				.mobileValidation(mObject, "mobileNo_1", checkNullandTrim(stdObj.getMobileNo_1()))
				.get("mobileNo_1").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->15");
			setCellColors(currentRow, currentRow.getCell(13), 13, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(13), 13, correctCellStyle);
		}

		stdObj.setMobileNo_2(df.formatCellValue(currentRow.getCell(14)));
		if (customFxcelValidator
				.mobileValidation(mObject, "mobileNo_2", checkNullandTrim(stdObj.getMobileNo_2()))
				.get("mobileNo_2").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->16");
			setCellColors(currentRow, currentRow.getCell(14), 14, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(14), 14, correctCellStyle);
		}

		stdObj.setEmailId(df.formatCellValue(currentRow.getCell(15)));
		if (customFxcelValidator
				.emailValidation(mObject, "emailId", checkNullandTrim(stdObj.getEmailId()))
				.get("emailId").get("status").equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(15), 15, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(15), 15, correctCellStyle);
		}

		stdObj.setMotherTongue(df.formatCellValue(currentRow.getCell(16)));

		try {
			if (stdObj.getMotherTongue() != null && stdObj.getMotherTongue() != ""
					&& !stdObj.getMotherTongue().equalsIgnoreCase("null")
					&& mtongObj.get(Integer
							.parseInt(String.valueOf(checkNull(stdObj.getMotherTongue())))) != null
					&& !mtongObj.get(
							Integer.parseInt(String.valueOf(checkNull(stdObj.getMotherTongue()))))) {
				setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(16), 16, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(16), 16, cellStyle);
		}

		stdObj.setSocCatId(df.formatCellValue(currentRow.getCell(17)));
		if (customFxcelValidator
				.numberValidation(mObject, "socCatId", checkNullandTrim(stdObj.getSocCatId()), 4)
				.get("socCatId").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->19");
//			currentRow.getCell(17).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(17), 17, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(17), 17, correctCellStyle);
		}

		stdObj.setMinorityId(df.formatCellValue(currentRow.getCell(18)));
		if (customFxcelValidator
				.numberValidation(mObject, "minorityId", checkNullandTrim(stdObj.getMinorityId()), 7)
				.get("minorityId").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->20");
//			currentRow.getCell(18).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(18), 18, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(18), 18, correctCellStyle);
		}

		stdObj.setIsBplYn(df.formatCellValue(currentRow.getCell(19)));
		if (customFxcelValidator
				.numberValidation(mObject, "isBplYn", checkNullandTrim(stdObj.getIsBplYn()), 2)
				.get("isBplYn").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->21");
//			currentRow.getCell(19).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(19), 19, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(19), 19, correctCellStyle);
		}

		stdObj.setAayBplYn(df.formatCellValue(currentRow.getCell(20)));
		if(stdObj.getIsBplYn() !=null && stdObj.getIsBplYn() !="" && !stdObj.getIsBplYn().equalsIgnoreCase("null") && stdObj.getIsBplYn().equalsIgnoreCase("1")) {
		if (customFxcelValidator
				.numberValidation(mObject, "aayBplYn", checkNullandTrim(stdObj.getAayBplYn()), 2)
				.get("aayBplYn").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->22");
//			currentRow.getCell(20).setCellStyle(cellStyle);
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
//			System.out.println("In set color---->23");
//			currentRow.getCell(21).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(21), 21, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(21), 21, correctCellStyle);
		}

		stdObj.setCwsnYn(df.formatCellValue(currentRow.getCell(22)));
		if (customFxcelValidator
				.numberValidation(mObject, "cwsnYn", checkNullandTrim(stdObj.getCwsnYn()), 2)
				.get("cwsnYn").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->24");
//			currentRow.getCell(22).setCellStyle(cellStyle);
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
//				System.out.println("In set color---->25");
//				currentRow.getCell(23).setCellStyle(cellStyle);
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
//			System.out.println("In set color---->26");
//			currentRow.getCell(24).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(24), 24, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(24), 24, correctCellStyle);
		}

		stdObj.setOoscYn(df.formatCellValue(currentRow.getCell(25)));
		if (customFxcelValidator
				.numberValidation(mObject, "ooscYn", checkNullandTrim(stdObj.getOoscYn()), 2)
				.get("ooscYn").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->27");
//			currentRow.getCell(25).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(25), 25, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(25), 25, correctCellStyle);
		}

		stdObj.setAdmnNumber(df.formatCellValue(currentRow.getCell(26)));
		if (customFxcelValidator
				.admisionNumberValidation(mObject, "admnNumber",
						checkNullandTrim(stdObj.getAdmnNumber()))
				.get("admnNumber").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->28");
//			currentRow.getCell(26).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(26), 26, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(26), 26, correctCellStyle);
		}

//		DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");

		if (currentRow.getCell(27) != null &&  checkNull(currentRow.getCell(27).toString()) !="") {
			java.util.Date admisionDate=null;
			try {
				
				if(currentRow.getCell(5).getCellType()==CellType.NUMERIC) {
					admisionDate = currentRow.getCell(27).getDateCellValue();
					stdObj.setStudentDob(dff.format(admisionDate));
				}else if(currentRow.getCell(27).getCellType()==CellType.STRING){
					stdObj.setStudentDob(currentRow.getCell(27).getStringCellValue());
				}
//			 admisionDate = currentRow.getCell(27).getDateCellValue();
//			stdObj.setAdmnStartDate(dff.format(admisionDate));
			if (customFxcelValidator
					.dateValidation(mObject, "admnStartDate",
							checkNullandTrim(stdObj.getAdmnStartDate()))
					.get("admnStartDate").get("status").equalsIgnoreCase("0")) {
				setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
//				currentRow.getCell(27).setCellValue(dff.format(admisionDate));
				if(currentRow.getCell(27).getCellType()==CellType.NUMERIC) {
					
					currentRow.getCell(27).setCellValue(dff.format(admisionDate));
				}else if(currentRow.getCell(27).getCellType()==CellType.STRING){
				
					currentRow.getCell(27).setCellValue(currentRow.getCell(27).getStringCellValue());
				}
			}else {
				setCellColors(currentRow, currentRow.getCell(23), 23, correctCellStyle);
//				currentRow.getCell(27).setCellValue(dff.format(admisionDate));
if(currentRow.getCell(27).getCellType()==CellType.NUMERIC) {
					
					currentRow.getCell(27).setCellValue(dff.format(admisionDate));
				}else if(currentRow.getCell(27).getCellType()==CellType.STRING){
				
					currentRow.getCell(27).setCellValue(currentRow.getCell(27).getStringCellValue());
				}
			}
			}catch(Exception ex) {
				setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
//				currentRow.getCell(27).setCellValue(dff.format(admisionDate));
				ex.printStackTrace();
			}
		} else {
			setCellColors(currentRow, currentRow.getCell(27), 27, cellStyle);
		}

		stdObj.setAcdemicStream(df.formatCellValue(currentRow.getCell(28)));

//		System.out.println("In student stream--->" + stdObj.getAcdemicStream());
		if (checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
				|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12")) {
			if (customFxcelValidator
					.numberValidation(mObject, "acdemicStream",
							checkNullandTrim(stdObj.getAcdemicStream()), 5)
					.get("acdemicStream").get("status").equalsIgnoreCase("0")) {
				System.out.println("In set color---->30");
				System.out.println("Stream coloring");
//				currentRow.getCell(28).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(28), 28, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(28), 28, correctCellStyle);
			}
		}

		stdObj.setEnrStatusPy(df.formatCellValue(currentRow.getCell(29)));

		if (customFxcelValidator
				.numberValidation(mObject, "enrStatusPy", checkNullandTrim(stdObj.getEnrStatusPy()), 4)
				.get("enrStatusPy").get("status").equalsIgnoreCase("0")) {
//			System.out.println("In set color---->31");
//			currentRow.getCell(29).setCellStyle(cellStyle);
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
				setCellColors(currentRow, currentRow.getCell(29), 29, correctCellStyle);
			} else {
				try {
					int stClass = Integer.parseInt(classCheck(stdObj.getClassId()));
					if (stClass > 0 && stClass <= 12
							&& (stClass == Integer.parseInt(stdObj.getClassPy())
									|| stClass == Integer.parseInt(stdObj.getClassPy()) + 1)) {
						setCellColors(currentRow, currentRow.getCell(30), 30, correctCellStyle);
					} else {
//						currentRow.getCell(30).setCellStyle(cellStyle);
						setCellColors(currentRow, currentRow.getCell(30), 30, cellStyle);
					}
				} catch (Exception ex) {
//					currentRow.getCell(30).setCellStyle(cellStyle);
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
//				System.out.println("In set color---->33");
//				currentRow.getCell(31).setCellStyle(cellStyle);
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

//			currentRow.getCell(32).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(32), 32, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(32), 32, correctCellStyle);
		}

		stdObj.setExamResultPy(df.formatCellValue(currentRow.getCell(33)));
		if (customFxcelValidator.numberValidation(mObject, "examResultPy",
				checkNullandTrim(stdObj.getExamResultPy()), 4).get("examResultPy").get("status")
				.equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(33), 33, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(33), 33, correctCellStyle);
		}

		stdObj.setExamMarksPy(df.formatCellValue(currentRow.getCell(34)));
		if (customFxcelValidator.numberValidation(mObject, "examMarksPy",
				checkNullandTrim(stdObj.getExamMarksPy()), 100).get("examMarksPy").get("status")
				.equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(34), 34, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(34), 34, correctCellStyle);
		}

		stdObj.setAttendencePy(df.formatCellValue(currentRow.getCell(35)));
		if (customFxcelValidator.numberValidation(mObject, "attendencePy",
				checkNullandTrim(stdObj.getAttendencePy()), 365).get("attendencePy").get("status")
				.equalsIgnoreCase("0")) {
			setCellColors(currentRow, currentRow.getCell(35), 35, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(35), 35, correctCellStyle);
		}

		stdObj.setUniformFacProvided(df.formatCellValue(currentRow.getCell(36)));
		if (customFxcelValidator
				.numberValidation(mObject, "uniformFacProvided",
						checkNullandTrim(stdObj.getUniformFacProvided()), 2)
				.get("uniformFacProvided").get("status").equalsIgnoreCase("0")) {

//			currentRow.getCell(36).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(36), 36, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(36), 36, correctCellStyle);
		}

		stdObj.setTextBoxFacProvided(df.formatCellValue(currentRow.getCell(37)));
		if (customFxcelValidator
				.numberValidation(mObject, "textBoxFacProvided",
						checkNullandTrim(stdObj.getTextBoxFacProvided()), 2)
				.get("textBoxFacProvided").get("status").equalsIgnoreCase("0")) {

//			currentRow.getCell(37).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(37), 37, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(37), 37, correctCellStyle);
		}

		stdObj.setCentrlSchlrshpYn(df.formatCellValue(currentRow.getCell(38)));
		if (customFxcelValidator
				.numberValidation(mObject, "centrlSchlrshpYn",
						checkNullandTrim(stdObj.getCentrlSchlrshpYn()), 2)
				.get("centrlSchlrshpYn").get("status").equalsIgnoreCase("0")) {

//			currentRow.getCell(38).setCellStyle(cellStyle);
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
//				currentRow.getCell(39).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(39), 39, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(39), 39, correctCellStyle);
			}
		} else {
			if (currentRow.getCell(41) == null) {
				currentRow.createCell(41);
			}
			currentRow.getCell(41).setCellValue("");
			setCellColors(currentRow, currentRow.getCell(41), 41, correctCellStyle);
		}

		stdObj.setStateSchlrshpYn(df.formatCellValue(currentRow.getCell(40)));
		if (customFxcelValidator
				.numberValidation(mObject, "stateSchlrshpYn",
						checkNullandTrim(stdObj.getStateSchlrshpYn()), 2)
				.get("stateSchlrshpYn").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(40).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(40), 40, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(40), 40, correctCellStyle);
		}

		stdObj.setOtherSchlrshpYn(df.formatCellValue(currentRow.getCell(41)));
		if (customFxcelValidator
				.numberValidation(mObject, "otherSchlrshpYn",
						checkNullandTrim(stdObj.getOtherSchlrshpYn()), 2)
				.get("otherSchlrshpYn").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(41).setCellStyle(cellStyle);
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
//				currentRow.getCell(42).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(42), 42, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(42), 42, correctCellStyle);
			}
		} else {
			if (currentRow.getCell(42) == null) {
				currentRow.createCell(42);
			}
			currentRow.getCell(42).setCellValue("");
			setCellColors(currentRow, currentRow.getCell(42), 42, correctCellStyle);
		}

		stdObj.setFacProvidedCwsn(df.formatCellValue(currentRow.getCell(43)));
		if (checkNull(stdObj.getCwsnYn()).equalsIgnoreCase("1")) {
			if (customFxcelValidator
					.numberValidation(mObject, "facProvidedCwsn",
							checkNullandTrim(stdObj.getFacProvidedCwsn()), 12)
					.get("facProvidedCwsn").get("status").equalsIgnoreCase("0")) {
//				currentRow.getCell(43).setCellStyle(cellStyle);
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
//				currentRow.getCell(44).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(44), 44, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(44), 44, correctCellStyle);
			}
		}

		stdObj.setSldType(df.formatCellValue(currentRow.getCell(45)));
		if (customFxcelValidator
				.numberValidation(mObject, "sldType", checkNullandTrim(stdObj.getSldType()), 3)
				.get("sldType").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(45).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(45), 45, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(45), 45, correctCellStyle);
		}

		stdObj.setScrndFrAsd(df.formatCellValue(currentRow.getCell(46)));
		if (customFxcelValidator
				.numberValidation(mObject, "scrndFrAsd", checkNullandTrim(stdObj.getScrndFrAsd()), 2)
				.get("scrndFrAsd").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(46).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(46), 46, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(46), 46, correctCellStyle);
		}

		stdObj.setScrndFrAdhd(df.formatCellValue(currentRow.getCell(47)));
		if (customFxcelValidator
				.numberValidation(mObject, "scrndFrAdhd", checkNullandTrim(stdObj.getScrndFrAdhd()), 2)
				.get("scrndFrAdhd").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(47).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(47), 47, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(47), 47, correctCellStyle);
		}

		stdObj.setIsEcActivity(df.formatCellValue(currentRow.getCell(48)));
		if (customFxcelValidator.numberValidation(mObject, "isEcActivity",
				checkNullandTrim(stdObj.getIsEcActivity()), 2).get("isEcActivity").get("status")
				.equalsIgnoreCase("0")) {
//			currentRow.getCell(48).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(48), 48, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(48), 48, correctCellStyle);
		}

		stdObj.setVocYn(df.formatCellValue(currentRow.getCell(49)));
		if(stdObj.getClassId() != null && sObj.getRowValue() !=null && sObj.getRowValue().get(0).get("is_vocational_active")!=null && (checkNull(stdObj.getClassId()).equalsIgnoreCase("9") || checkNull(stdObj.getClassId()).equalsIgnoreCase("10") || checkNull(stdObj.getClassId()).equalsIgnoreCase("11") || checkNull(stdObj.getClassId()).equalsIgnoreCase("12"))  && Integer.parseInt(checkNull(String.valueOf(sObj.getRowValue().get(0).get("is_vocational_active"))))==1) {
		if (customFxcelValidator
				.numberValidation(mObject, "vocYn", checkNullandTrim(stdObj.getVocYn()), 2).get("vocYn")
				.get("status").equalsIgnoreCase("0")) {
			if (currentRow.getCell(49) == null) {
				currentRow.createCell(49);
			}
//			currentRow.getCell(49).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(49), 49, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(49), 49, correctCellStyle);
		}
		}
		
		
		stdObj.setSector(df.formatCellValue(currentRow.getCell(50)));

		try {
			if (stdObj.getSector() != null && stdObj.getSector() != ""
					&& !(checkNull(stdObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("10"))
					&& lowerSector.get(String.valueOf(checkNull(stdObj.getSector()))) != null
					&& lowerSector.get(String.valueOf(checkNull(stdObj.getSector())))) {
//			currentRow.getCell(50).setCellStyle(cellStyle);		
				setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
			ex.printStackTrace();
		}

		try {
			if (stdObj.getSector() != null && stdObj.getSector() != ""
					&& !(checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12"))
					&& higherSector.get(String.valueOf(checkNull(stdObj.getSector()))) != null
					&& higherSector.get(String.valueOf(checkNull(stdObj.getSector())))) {
//			currentRow.getCell(50).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(50), 50, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(50), 50, cellStyle);
			ex.printStackTrace();
		}

		try {

			if (stdObj.getJobRole() != null && stdObj.getJobRole() != ""
					&& !(checkNull(stdObj.getClassId()).equalsIgnoreCase("9")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("10"))
					&& lowerSubSector.get(String.valueOf(checkNull(stdObj.getJobRole()))) != null
					&& lowerSubSector.get(String.valueOf(checkNull(stdObj.getJobRole())))) {
//			currentRow.getCell(51).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(51), 51, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			ex.printStackTrace();
		}

		try {
			if (stdObj.getJobRole() != null && stdObj.getJobRole() != ""
					&& !(checkNull(stdObj.getClassId()).equalsIgnoreCase("11")
							|| checkNull(stdObj.getClassId()).equalsIgnoreCase("12"))
					&& higherSubSector.get(String.valueOf(checkNull(stdObj.getJobRole()))) != null
					&& higherSubSector.get(String.valueOf(checkNull(stdObj.getJobRole())))) {
//			currentRow.getCell(51).setCellStyle(cellStyle);
				setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			}else {
				setCellColors(currentRow, currentRow.getCell(51), 51, correctCellStyle);
			}
		} catch (Exception ex) {
			setCellColors(currentRow, currentRow.getCell(51), 51, cellStyle);
			ex.printStackTrace();
		}

//		if (customFxcelValidator
//				.numberValidation(mObject, "sector", checkNullandTrim(stdObj.getSector()), 2)
//				.get("sector").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(50).setCellStyle(cellStyle);
//		}
//		stdObj.setJobRole(df.formatCellValue(currentRow.getCell(51)));
//		if (customFxcelValidator
//				.numberValidation(mObject, "jobRole", checkNullandTrim(stdObj.getJobRole()), 2)
//				.get("jobRole").get("status").equalsIgnoreCase("0")) {
//			currentRow.getCell(51).setCellStyle(cellStyle);
//		}

		stdObj.setAppVocPy(df.formatCellValue(currentRow.getCell(52)));

		if (customFxcelValidator
				.numberValidation(mObject, "appVocPy", checkNullandTrim(stdObj.getAppVocPy()), 2)
				.get("appVocPy").get("status").equalsIgnoreCase("0")) {

//			currentRow.getCell(52).setCellStyle(cellStyle);
			setCellColors(currentRow, currentRow.getCell(52), 52, cellStyle);
		}else {
			setCellColors(currentRow, currentRow.getCell(52), 52, correctCellStyle);
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
}
