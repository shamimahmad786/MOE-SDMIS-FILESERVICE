package com.moe.sdmis.fileservice.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
 

@Component
public class UserExcelExporter {
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
    	  List<Row> rowList=new ArrayList<Row>();
    	  for(int i=0;i<34;i++) {
    		  Row row = desSheet.createRow(i);
    		  rowList.add(row);
    	  }
    	  
    	  List<String> rowDesLabel=Arrays.asList("CLASS ID","SECTION ID","NAME","GENDER","DOB","MOTHER NAME","FATHER/GUARDIAN'S NAME","AADHAR","NAME AS PER AADHAR","ADDRESS",
    			  "PINCODE","MOBILE NUMBER","ALTERNATE MOBILE NUMBER","EMAIL ID(STUDENT/PARENT/GUARDIAN)","MOTHER TONGUE","SOCIAL CATEGORY","MINORITY GROUP",
    			  "ANTYODAYA/BPL BANEFICIARY","BELONGS TO EWS/DISADVANTAGED GROUP","CWSN(YES/NO)","IMPAIRMENT TYPE","CHILD  IS INDIAN NATIONAL","CHILD IS OUT-OF-SCHOOL-CHILD",
    			  "ADMISSION NUMBER","ADMISSION DATE","STUDENT STREAM(For higher secondary only)","PREVIOUS ACADEMIC SCHOOLING STATUS","CLASS STUDIES INPREVIOUS ACADEMIC",
    			  "ADMITED/ENROLLED UNDER","APPEARED FOR EXAM IN PREVIOUS CLASS","RESULT FOR PREVIOUS EXAM","MARKS % OF PREVIOUS EXAM","CLASS ATTENDED DAYS","ACADEMIC YEAR ID");
    

    	  List<String> rowDes= Arrays.asList("CLASS ID should be in digit","SECTION ID should be in digit","NAME should be in character","GENDER should be in digit","DOB should be in DD/MM/YYYY formate","MOTHER NAME should be in character","FATHER/GUARDIAN'S NAME should be in character","AADHAR should be in digit","NAME AS PER AADHAR should be in character","ADDRESS should be in character with comma allow",
"PINCODE should be in digit","MOBILE NUMBER should be in digit","ALTERNATE MOBILE NUMBER should be in digit","EMAIL ID(STUDENT/PARENT/GUARDIAN) it should be proper email id","MOTHER TONGUE should be in digit","SOCIAL CATEGORY should be in digit",
"MINORITY GROUP should be in digit",
"ANTYODAYA/BPL BANEFICIARY should be in digit","BELONGS TO EWS/DISADVANTAGED GROUP should be in digit","CWSN(YES/NO) should be in digit","IMPAIRMENT TYPE should be in Json","CHILD  IS INDIAN NATIONAL should be in digit","CHILD IS OUT-OF-SCHOOL-CHILD should be in digit",
"ADMISSION NUMBER should be in Alphanumeric characters","ADMISSION DATE should be DD/MM/YYYY formate","STUDENT STREAM(For higher secondary only)should be in digit","PREVIOUS ACADEMIC SCHOOLING STATUS should be in digit","CLASS STUDIES INPREVIOUS ACADEMIC should be in digit",
"ADMITED/ENROLLED UNDER should be in digit","APPEARED FOR EXAM IN PREVIOUS CLASS should be in digit","RESULT FOR PREVIOUS EXAM should be in digit","MARKS % OF PREVIOUS EXAM should be in digit","CLASS ATTENDED DAYS should be in digit",
"ACADEMIC YEAR ID should be in digit");
    	  
    	  for(int i=0;i<rowList.size();i++) {
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
        }else {
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
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
//        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
    
    public String getExcelValue(Cell currentCell) {
    	DataFormatter formatter = new DataFormatter();
    	  if(currentCell.getCellType()==CellType.NUMERIC) {
    		  DataFormatter formatters = new DataFormatter();
    		 // CellReference cr = new CellRefence(currentCell);
//    		return String.valueOf(currentCell.getNumericCellValue());  
    		return String.valueOf(formatters.formatCellValue(currentCell));
    	  }else if(currentCell.getCellType()==CellType.STRING) {
    		return  String.valueOf(currentCell.getStringCellValue());    
    	  }else if(currentCell.getCellType()==CellType.BOOLEAN) {
    		return  String.valueOf(currentCell.getBooleanCellValue());  
    	  }
		return "";
    }
    
}