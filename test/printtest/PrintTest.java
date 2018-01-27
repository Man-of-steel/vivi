/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printtest;

import dao.Constants;
import java.io.File;

/**
 *
 * @author manofsteel
 */
public class PrintTest {
    public static void main(String[] args) {
        try{
            String filename = "reports/RMPU-SatJan20-04:04:28PST2018.pdf";
            File file = new File(filename);
            String command = "lpr -o portrait -o fit-to-page -o media=A4 reports/RMPU-SatJan20-04:04:28PST2018.pdf";
            Runtime runtime = Runtime.getRuntime(); 
            Process p = runtime.exec(command);
            Thread.sleep(5000);
            System.out.println("Done!!");
            System.out.println(p.toString());
            System.out.println("File exists -> " + file.exists());
            runtime.exit(0);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
