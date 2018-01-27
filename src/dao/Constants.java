/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Man of Steel
 */
public class Constants {
    
    //<editor-fold defaultstate="collapsed" desc="DB constants region">
    public static final String DB_URL = "jdbc:mysql://localhost:3306/railway_automation";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="All keys region">
    public static final String RMPU_START_STOP_COMMAND = "rm:cmd";
    
    public static final String RMPU_SERIAL_INPUT = "rm:sr";
    public static final String RMPU_DIVISION_INPUT = "rm:div";
    public static final String RMPU_MAKE_INPUT = "rm:mk";
    public static final String RMPU_TYPE_INPUT = "rm:type";
    //public static final String RMPU_NPP_INPUT = "rm:type";
    
    public static final String RMPU_ACTION_INPUT = "rm:action";
    //public static final String RMPU_PRINT_INPUT = "rm:action";
    
    public static final String RMPU_BLOWER_R = "blR.txt";
    public static final String RMPU_BLOWER_Y = "blY.txt";
    public static final String RMPU_BLOWER_B = "blB.txt";
    
    public static final String RMPU_CD1_R = "cd1R.txt";
    public static final String RMPU_CD1_Y = "cd1Y.txt";
    public static final String RMPU_CD1_B = "cd1B.txt";
    
    public static final String RMPU_CD2_R = "cd2R.txt";
    public static final String RMPU_CD2_Y = "cd2Y.txt";
    public static final String RMPU_CD2_B = "cd2B.txt";
    
    public static final String RMPU_CP1_R = "cp1R.txt";
    public static final String RMPU_CP1_Y = "cp1Y.txt";
    public static final String RMPU_CP1_B = "cp1B.txt";
    
    public static final String RMPU_CP2_R = "cp2R.txt";
    public static final String RMPU_CP2_Y = "cp2Y.txt";
    public static final String RMPU_CP2_B = "cp2B.txt";
    
    public static final String RMPU_TC_R = "rmCR.txt";
    public static final String RMPU_TC_Y = "rmCY.txt";
    public static final String RMPU_TC_B = "rmCB.txt";
    
    public static final String RMPU_TV_R = "rmVR.txt";
    public static final String RMPU_TV_Y = "rmVY.txt";
    public static final String RMPU_TV_B = "rmVB.txt";
    
    public static final String GRILL_INPUT = "glT.txt";
    public static final String AMBIENT_INPUT = "amT.txt";
    public static final String ROOM_INPUT = "rmT.txt";
    
    public static final String WIND_SPEED = "wS.txt";
    
    //alternator start
    public static final String ALTERNATOR_START_STOP_COMMAND = "al:cmd";
    
    public static final String ALTERNATOR_SERIAL_INPUT = "al:sr";
    public static final String ALTERNATOR_MAKE_INPUT = "al:mk";
    public static final String ALTERNATOR_ACTION_INPUT = "al:action";
    //public static final String ALTERNATOR_PRINT_INPUT = "al:action";
    
    public static final String ALTERNATOR1_CURRENT = "al1C.txt";
    public static final String ALTERNATOR1_VOLTAGE = "al1V.txt";
    
    public static final String ALTERNATOR2_CURRENT = "al2C.txt";
    public static final String ALTERNATOR2_VOLTAGE = "al2V.txt";
    public static final String BATTERY_CHARGING_CURRENT = "chC.txt";
    public static final String BATTERY_VOLTAGE = "btV.txt";
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Stream data region">
    public static final String STREAM_FILE_WITH_PATH = "/dev/ttyUSB0";
    
    public static final List<String> STREAM_UNWANTED_STRINGS = Arrays.asList("/x0000", "/r000");
    public static final String STREAM_KEY_VALUE_DELIMITER = "=";
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Print region">
    public static final String TERMINAL_PRINT_COMMAND = "lpr -o portrait -o fit-to-page -o media=A4";
    public static final String PRINT_FOLDER_RMPU = "reports/";
    public static final String PRINT_FOLDER_ALTERNATOR = "reports/";
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Label region">
    public static String[] RMPUKeysOrder = {Constants.RMPU_BLOWER_R, Constants.RMPU_BLOWER_Y, Constants.RMPU_BLOWER_B,
        Constants.RMPU_CD1_R, Constants.RMPU_CD1_Y, Constants.RMPU_CD1_B,
        Constants.RMPU_CD2_R, Constants.RMPU_CD2_Y, Constants.RMPU_CD2_B,
        Constants.RMPU_CP1_R, Constants.RMPU_CP1_Y, Constants.RMPU_CP1_B,
        Constants.RMPU_CP2_R, Constants.RMPU_CP2_Y, Constants.RMPU_CP2_B,
        Constants.RMPU_TC_R, Constants.RMPU_TC_Y, Constants.RMPU_TC_B,
        Constants.RMPU_TV_R, Constants.RMPU_TV_Y, Constants.RMPU_TV_B,
        Constants.GRILL_INPUT, Constants.AMBIENT_INPUT, Constants.ROOM_INPUT,
        Constants.WIND_SPEED
    };
    
    
    public static String[] AlternatorKeysOrder = {Constants.ALTERNATOR1_VOLTAGE, Constants.ALTERNATOR1_CURRENT,
        Constants.ALTERNATOR2_VOLTAGE, Constants.ALTERNATOR2_CURRENT,
        Constants.BATTERY_VOLTAGE, Constants.BATTERY_CHARGING_CURRENT
    };
    
    public static String[] RMPULabelsInOrder = {"Blower-R", "Blower-Y","Blower-B",
                                        "Condensor1(CD1)-R", "Condensor1(CD1)-Y","Condensor1(CD1)-B",
                                        "Condensor2(CD2)-R", "Condensor2(CD2)-Y","Condensor2(CD2)-B",
                                        "Compressor1(CP1)-R", "Compressor1(CP1)-Y","Compressor1(CP1)-B",
                                        "Compressor2(CP2)-R", "Compressor2(CP2)-Y","Compressor2(CP2)-B",
                                        "Total Current-R", "Total Current-Y","Total Current-B",
                                        "Total Voltage-R", "Total Voltage-Y","Total Voltage-B",
                                        "Grill Temperature", "Ambient Temperature", "Room Temperature",
                                        "Wind Speed"
                                        };
    public static String[] AlternatorLabelsInOrder = {"Alternator1-Voltage", "Alternator1-Current",
                                                      "Alternator2-Voltage", "Alternator2-Current",
                                                      "Battery Voltage", "Battery Charging Current"};
    
    
//</editor-fold>
}
