/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

/**
 *
 * @author Man of Steel
 */

import dao.Constants;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class Printer {
    public static boolean printPDF(String filePath){
        try {
            PDDocument document = PDDocument.load(new File(filePath));
            String printerName = System.getenv(Constants.PRINTER_SYSTEM_ENVIRONMENT_VARIABLE_NAME);
            PrintService myPrintService = findPrintService(printerName);
            //PrintService myPrintService = PrintServiceLookup.lookupPrintServices(null, null)[0];
            
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.setPrintService(myPrintService);
            job.print();
            System.out.println("Done");
            return true;
            
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    } 

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("All printers available are");
        System.out.println(java.util.Arrays.toString(printServices));
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
    
    public static void main(String args[]) throws Exception {

        PDDocument document = PDDocument.load(new File("C:\\Users\\Chethan\\Desktop\\RMPU-Fri-Feb-02-12_38_45-IST-2018.pdf"));

        PrintService myPrintService = findPrintService("HP LaserJet 1020");

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
        System.out.println("Done");

    }  
}
