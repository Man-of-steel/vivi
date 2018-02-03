/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printtest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.Constants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.Report;

/**
 *
 * @author Man of Steel
 */
public class ReportTest {
    private static final String EXTENSION = ".pdf";
    private static final Font BIG_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 19,
            Font.BOLD);
    private static final Font SUB_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 13,
            Font.BOLD);
    private static final Font SUB_FONT_WITH_UNDERLINE = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD + Font.UNDERLINE);
    
    private static final Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    
    public String generateRMPUReport(Map<String, String> dataMap){
        try{
            Date date = new Date();
            String fileName = Constants.PRINT_FOLDER_RMPU + "RMPU-" + date.toString().replace(" ", "-").replace(":", "_") + EXTENSION;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.addTitle("RMPU Report");
            
            // creating main heading with timestamp
            Paragraph mainHeading = new Paragraph();
            mainHeading.add(new Paragraph("RMPU Log", BIG_FONT));
            mainHeading.add(new Paragraph("Generated at " + date, SMALL_BOLD));
            mainHeading.add(new Paragraph(" "));
            
            // creating sub headings
            String serial = dataMap.get(Constants.RMPU_SERIAL_INPUT);
            String make = dataMap.get(Constants.RMPU_MAKE_INPUT);
            String division = dataMap.get(Constants.RMPU_DIVISION_INPUT);
            String type = dataMap.get(Constants.RMPU_TYPE_INPUT);
            
            Paragraph subHeading = new Paragraph();
            
            Paragraph p;
            p = new Paragraph();
            String serial_make = String.format("SERIAL      : %s      MAKE       : %s", serial, make);
            p.add(new Phrase(serial_make, SUB_FONT));
            subHeading.add(p);
            
            p = new Paragraph();
            String division_type = String.format("DIVISION  : %s      TYPE        : %s", division, type);
            p.add(new Phrase(division_type, SUB_FONT));
            subHeading.add(p);
            subHeading.add(new Paragraph(" "));
            
            // creating table with row headings
            PdfPTable table = new PdfPTable(2);
            
            PdfPCell dataCell;
            for(int i = 0; i < Constants.RMPUKeysOrder.length; i++){
                if(i%3 == 0){
                    dataCell = new PdfPCell(new Phrase(" "));
                    dataCell.setGrayFill(0.2f);
                    table.addCell(dataCell);
                    
                    dataCell = new PdfPCell(new Phrase(" "));
                    dataCell.setGrayFill(0.2f);
                    table.addCell(dataCell);
                }
                dataCell = new PdfPCell(new Phrase(Constants.RMPULabelsInOrder[i], SUB_FONT));
                table.addCell(dataCell);
                
                dataCell = new PdfPCell(new Phrase(dataMap.get(Constants.RMPUKeysOrder[i]), SUB_FONT));
                table.addCell(dataCell);
            }

            // adding all elements to the document
            document.add(mainHeading);
            document.add(subHeading);
            document.add(table);
            
            addCheckedByClause(document);
            
            document.close();
            System.out.println("New RMPU report generated successfully -> " + fileName);
            return fileName;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    private void addCheckedByClause(Document document){
        try {
            
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            
            PdfPCell cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.addElement(new Phrase("--Checked By--", SUB_FONT));
            
            table.addCell(cell);
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String generateAlternatorReport(Map<String, String> dataMap){
        try{
            Date date = new Date();
            String fileName = Constants.PRINT_FOLDER_ALTERNATOR + "Alternator-" + date.toString().replace(" ", "-").replace(":", "_") + EXTENSION;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.addTitle("Alternator Report");
            
            // creating main heading with timestamp
            Paragraph mainHeading = new Paragraph();
            mainHeading.add(new Paragraph("Alternator Log", BIG_FONT));
            mainHeading.add(new Paragraph(" "));
            mainHeading.add(new Paragraph("Generated at " + date, SMALL_BOLD));
            mainHeading.add(new Paragraph(" "));
            
            // creating sub headings
            String serial = dataMap.get(Constants.ALTERNATOR_SERIAL_INPUT);
            String make = dataMap.get(Constants.ALTERNATOR_MAKE_INPUT);
            
            Paragraph subHeading = new Paragraph();
            subHeading.add(new Paragraph("SERIAL : " + serial, SUB_FONT));
            subHeading.add(new Paragraph("MAKE : " + make, SUB_FONT));
            subHeading.add(new Paragraph(" "));
            subHeading.add(new Paragraph(" "));
            
            // creating table with row headings
            PdfPTable table = new PdfPTable(2);
            
            PdfPCell dataCell;
            for(int i = 0; i < Constants.AlternatorKeysOrder.length; i++){
                if(i%2 == 0){
                    dataCell = new PdfPCell(new Phrase(" "));
                    dataCell.setGrayFill(0.2f);
                    table.addCell(dataCell);
                    
                    dataCell = new PdfPCell(new Phrase(" "));
                    dataCell.setGrayFill(0.2f);
                    table.addCell(dataCell);
                }
                dataCell = new PdfPCell(new Phrase(Constants.AlternatorLabelsInOrder[i], SUB_FONT));
                table.addCell(dataCell);
                
                dataCell = new PdfPCell(new Phrase(dataMap.get(Constants.AlternatorKeysOrder[i]), SUB_FONT));
                table.addCell(dataCell);
            }

            // adding all elements to the document
            document.add(mainHeading);
            document.add(subHeading);
            document.add(table);
            
            addCheckedByClause(document);
            
            document.close();
            return fileName;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
    
    public static void main(String[] args) {
//        Map<String, String> data = new HashMap<>();
//        for(String label:Constants.RMPULabelsInOrder){
//            data.put(label, "test");
//        }
        
        Map<String, String> data = new HashMap<>();
        for(String label:Constants.AlternatorLabelsInOrder){
            data.put(label, "test");
        }
        
        ReportTest test = new ReportTest();
        test.generateAlternatorReport(data);
        System.out.println("Done");
        
        
    }
    
}
