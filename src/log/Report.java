/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.Constants;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Man of Steel
 */
public class Report {
    
    private static final String EXTENSION = ".pdf";
    private static final Font BIG_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 22,
            Font.BOLD);
    private static final Font SUB_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static final Font SUB_FONT_WITH_UNDERLINE = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD + Font.UNDERLINE);
    
    private static final Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    
    
    public String generateRMPUReport(Map<String, String> dataMap){
        try{
            Date date = new Date();
            String fileName = Constants.PRINT_FOLDER_RMPU + "RMPU-" + date.toString().replace(" ", "-") + EXTENSION;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            addMetaData(document, "RMPU");
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
            String serial_make = String.format("SERIAL : %s      MAKE : %s", serial, make);
            p.add(new Phrase(serial_make, SUB_FONT));
            //p.add(new Phrase("SERIAL    :", SUB_FONT));
            //p.add(new Phrase(serial, SUB_FONT));
            subHeading.add(p);
            subHeading.add(new Paragraph(" "));
            
//            p = new Paragraph();
//            p.add(new Phrase("MAKE      :", SUB_FONT));
//            p.add(new Phrase(make, SUB_FONT));
//            subHeading.add(p);
            
            p = new Paragraph();
            String division_type = String.format("DIVISION : %s      TYPE : %s", division, type);
            p.add(new Phrase(division_type, SUB_FONT));
            //p.add(new Phrase(division, SUB_FONT));
            //p.add(new Phrase(division, SUB_FONT));
            subHeading.add(p);
            subHeading.add(new Paragraph(" "));
            subHeading.add(new Paragraph(" "));
            
            // creating table with row headings
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell = new PdfPCell(new Phrase("Name", SUB_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Value", SUB_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            PdfPCell dataCell;
            for(int i = 0; i < Constants.RMPUKeysOrder.length; i++){
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
            System.out.println(e);
            return null;
        }
    }
    
    public String generateAlternatorReport(Map<String, String> dataMap){
        try{
            Date date = new Date();
            String fileName = Constants.PRINT_FOLDER_ALTERNATOR + "Alternator-" + date.toString().replace(" ", "-") + EXTENSION;
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            addMetaData(document, "Alternator");
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
            PdfPCell cell = new PdfPCell(new Phrase("Name", SUB_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Value", SUB_FONT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            PdfPCell dataCell;
            for(int i = 0; i < Constants.AlternatorKeysOrder.length; i++){
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
            document.add(new Paragraph(" "));
            document.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void addMetaData(Document document, String title) {
        document.addTitle(title + " Report");
        document.addKeywords(title + ", Report");
        document.addCreator("Svachallan");
    }
    
}
