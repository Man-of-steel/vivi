/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Man of Steel
 */
public class ExcelReport {

    public static void main(String[] args) {
        try {
            String fileName = "C:\\Users\\Chethan\\Desktop\\format.xlsx";
            //Read the spreadsheet that needs to be updated
            FileInputStream fsIP = new FileInputStream(new File(fileName));
            //Access the workbook
            XSSFWorkbook wb = new XSSFWorkbook(fsIP);
            //Access the worksheet, so that we can update / modify it.
            Sheet worksheet = wb.getSheetAt(0);
            // declare a Cell object
            Cell cell = null;
            // Access the second cell in second row to update the value
            cell = worksheet.getRow(1).getCell(1);
            // Get current cell value value and overwrite the value
            cell.setCellValue("OverRide existing value");
            //Close the InputStream
            fsIP.close();
            //Open FileOutputStream to write updates
            FileOutputStream output_file = new FileOutputStream(new File(fileName));
            //write changes
            wb.write(output_file);
            //close the stream
            output_file.close();
        } catch (IOException ex) {
            Logger.getLogger(ExcelReport.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
    }

}
