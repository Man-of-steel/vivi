/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svachallan.ui;

import dao.Constants;
import dao.DAO;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import log.Report;
import print.Printer;

/**
 *
 * @author Man of Steel
 */
public class View_Control extends javax.swing.JFrame {

    /**
     * Creates new form View_Control
     */
    
    InputStreamReader inputStreamReader;
    OutputStreamWriter outputStreamWriter;
    
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    
    Map<String, String> wholeData, RMPUData, AlternatorData;
    
    Thread dataThread, dataHandleThread;
    
    public View_Control() {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 20));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 20));
        initComponents();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        View_Control.this.setSize(dimension);
        wholeData = new HashMap<>();
        RMPUData = new HashMap<>();
        AlternatorData = new HashMap<>();
        fillAllKeys();
        //setupAndStartAllThreads();
        
    }
    
    public void setupAndStartAllThreads(){
        setupDataThread();
        setupDataHandlingThread();
        
        if(this.bufferedReader != null && this.printWriter != null){
            dataThread.start();
            dataHandleThread.start();
            System.out.println("All threads started");
        }
        else
        {
            System.out.println("Threads not started bcoz of null stream reader and writer");
        }
    }
    

    public InputStreamReader getInputStreamReader() {
        return inputStreamReader;
    }

    public void setInputStreamReader(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
        this.bufferedReader = new BufferedReader(inputStreamReader);
        System.out.println("Setting inputstreamreader");
    }

    public OutputStreamWriter getOutputStreamWriter() {
        return outputStreamWriter;
    }

    public void setOutputStreamWriter(OutputStreamWriter outputStreamWriter) {
        this.outputStreamWriter = outputStreamWriter;
        this.printWriter = new PrintWriter(outputStreamWriter);
        System.out.println("Setting outputstream writer");
    }
    
    private boolean allDataValidForSavingRMPU(){
        boolean valid = true;
        System.out.println("Trying to save RMPU");
        for(Map.Entry<String, String> entry : RMPUData.entrySet()){
            // Ignoring some of the non required data
            if(entry.getKey().equals(Constants.RMPU_ACTION_INPUT) || entry.getKey().equals(Constants.RMPU_MAKE_INPUT) || entry.getKey().equals(Constants.RMPU_DIVISION_INPUT) || entry.getKey().equals(Constants.RMPU_START_STOP_COMMAND))
                continue;
            if(wholeData.get(entry.getKey()) == null){
                System.out.println("Empty field found -> " + entry.getKey());
                valid = false;
                break;
            }
        }
        return valid;
    }
    
    private boolean allDataValidForSavingAlternator(){
        boolean valid = true;
        System.out.println("Trying to save Alternator");
        for(Map.Entry<String, String> entry : AlternatorData.entrySet()){
            // Ignoring some of the non required data
            if(entry.getKey().equals(Constants.ALTERNATOR_ACTION_INPUT) || entry.getKey().equals(Constants.ALTERNATOR_MAKE_INPUT) || entry.getKey().equals(Constants.ALTERNATOR_START_STOP_COMMAND))
                continue;
            if(wholeData.get(entry.getKey()) == null){
                System.out.println("Empty field found -> " + entry.getKey());
                valid = false;
                break;
            }
        }
        return valid;
    }
        
    private void fillDataRMPU(){
        
        try{
            
            
            // user inputs
            if(wholeData.get(Constants.RMPU_SERIAL_INPUT) != null){
                this.rmpu_serial_input.setText(wholeData.get(Constants.RMPU_SERIAL_INPUT));
            }
            
            if(wholeData.get(Constants.RMPU_DIVISION_INPUT) != null){
                this.rmpu_division_input.setText(wholeData.get(Constants.RMPU_DIVISION_INPUT));
            }
            
            if(wholeData.get(Constants.RMPU_MAKE_INPUT) != null){
                this.rmpu_make_input.setText(wholeData.get(Constants.RMPU_MAKE_INPUT));
            }
            
            //pp and npp
            if(wholeData.get(Constants.RMPU_TYPE_INPUT) != null){
                String pp_npp_data = wholeData.get(Constants.RMPU_TYPE_INPUT).trim();
                if(pp_npp_data.equalsIgnoreCase("pp")){
                    this.rmpu_pp_input.setSelected(true);
                    this.rmpu_pp_input.setEnabled(true);
                    this.rmpu_npp_input.setSelected(false);
                    
                    this.revalidate();
                    this.repaint();
                }
                
                if(pp_npp_data.equalsIgnoreCase("npp")){
                    this.rmpu_pp_input.setSelected(false);
                    this.rmpu_npp_input.setSelected(true);
                    this.rmpu_npp_input.setEnabled(true);
                    
                    this.revalidate();
                    this.repaint();
                }
            }
            
            // blower
            if(wholeData.get(Constants.RMPU_BLOWER_R) != null){
                this.rmpu_blower_r.setText(wholeData.get(Constants.RMPU_BLOWER_R));
            }
            
            if(wholeData.get(Constants.RMPU_BLOWER_Y) != null){
                this.rmpu_blower_y.setText(wholeData.get(Constants.RMPU_BLOWER_Y));
            }
            
            if(wholeData.get(Constants.RMPU_BLOWER_B) != null){
                this.rmpu_blower_b.setText(wholeData.get(Constants.RMPU_BLOWER_B));
            }
            
            //cd1
            if(wholeData.get(Constants.RMPU_CD1_R) != null){
                this.rmpu_cd1_r.setText(wholeData.get(Constants.RMPU_CD1_R));
            }
            
            if(wholeData.get(Constants.RMPU_CD1_Y) != null){
                this.rmpu_cd1_y.setText(wholeData.get(Constants.RMPU_CD1_Y));
            }
            
            if(wholeData.get(Constants.RMPU_CD1_B) != null){
                this.rmpu_cd1_b.setText(wholeData.get(Constants.RMPU_CD1_B));
            }
            
            //cd2
            if(wholeData.get(Constants.RMPU_CD2_R) != null){
                this.rmpu_cd2_r.setText(wholeData.get(Constants.RMPU_CD2_R));
            }
            
            if(wholeData.get(Constants.RMPU_CD2_Y) != null){
                this.rmpu_cd2_y.setText(wholeData.get(Constants.RMPU_CD2_Y));
            }
            
            if(wholeData.get(Constants.RMPU_CD2_B) != null){
                this.rmpu_cd2_b.setText(wholeData.get(Constants.RMPU_CD2_B));
            }
            
            //cp1
            if(wholeData.get(Constants.RMPU_CP1_R) != null){
                this.rmpu_cp1_r.setText(wholeData.get(Constants.RMPU_CP1_R));
            }
            
            if(wholeData.get(Constants.RMPU_CP1_Y) != null){
                this.rmpu_cp1_y.setText(wholeData.get(Constants.RMPU_CP1_Y));
            }
            
            if(wholeData.get(Constants.RMPU_CP1_B) != null){
                this.rmpu_cp1_b.setText(wholeData.get(Constants.RMPU_CP1_B));
            }
            
            //cp2
            if(wholeData.get(Constants.RMPU_CP2_R) != null){
                this.rmpu_cp2_r.setText(wholeData.get(Constants.RMPU_CP2_R));
            }
            
            if(wholeData.get(Constants.RMPU_CP2_Y) != null){
                this.rmpu_cp2_y.setText(wholeData.get(Constants.RMPU_CP2_Y));
            }
            
            if(wholeData.get(Constants.RMPU_CP2_B) != null){
                this.rmpu_cp2_b.setText(wholeData.get(Constants.RMPU_CP2_B));
            }
            
            //total current
            if(wholeData.get(Constants.RMPU_TC_R) != null){
                this.rmpu_tc_r.setText(wholeData.get(Constants.RMPU_TC_R));
            }
            
            if(wholeData.get(Constants.RMPU_TC_Y) != null){
                this.rmpu_tc_y.setText(wholeData.get(Constants.RMPU_TC_Y));
            }
            
            if(wholeData.get(Constants.RMPU_TC_B) != null){
                this.rmpu_tc_b.setText(wholeData.get(Constants.RMPU_TC_B));
            }
            
            //total volage
            if(wholeData.get(Constants.RMPU_TV_R) != null){
                this.rmpu_tv_r.setText(wholeData.get(Constants.RMPU_TV_R));
            }
            
            if(wholeData.get(Constants.RMPU_TV_Y) != null){
                this.rmpu_tv_y.setText(wholeData.get(Constants.RMPU_TV_Y));
            }
            
            if(wholeData.get(Constants.RMPU_TV_B) != null){
                this.rmpu_tv_b.setText(wholeData.get(Constants.RMPU_TV_B));
            }
            
            //temperature
            if(wholeData.get(Constants.GRILL_INPUT) != null){
                this.grill_input.setText(wholeData.get(Constants.GRILL_INPUT));
            }
            
            if(wholeData.get(Constants.AMBIENT_INPUT) != null){
                this.ambient_input.setText(wholeData.get(Constants.AMBIENT_INPUT));
            }
            
            if(wholeData.get(Constants.ROOM_INPUT) != null){
                this.room_input.setText(wholeData.get(Constants.ROOM_INPUT));
            }
            
            //wind speed
            if(wholeData.get(Constants.WIND_SPEED) != null){
                this.wind_speed.setText(wholeData.get(Constants.WIND_SPEED));
            }
        }
        catch (Exception e){
            System.out.println("Error filling RMPU data -> " + e);
        }
        
    }
        
    private void fillDataAlternator(){
        
        try{
            //alternator values
            if(wholeData.get(Constants.ALTERNATOR_SERIAL_INPUT) != null){
                this.alternator_serial_input.setText(wholeData.get(Constants.ALTERNATOR_SERIAL_INPUT));
            }
            
            if(wholeData.get(Constants.ALTERNATOR_MAKE_INPUT) != null){
                this.alternator_make_input.setText(wholeData.get(Constants.ALTERNATOR_MAKE_INPUT));
            }
            
            if(wholeData.get(Constants.ALTERNATOR1_VOLTAGE) != null){
                this.alternator1_voltage.setText(wholeData.get(Constants.ALTERNATOR1_VOLTAGE));
            }
            
            if(wholeData.get(Constants.ALTERNATOR1_CURRENT) != null){
                this.alternator1_current.setText(wholeData.get(Constants.ALTERNATOR1_CURRENT));
            }
            
            if(wholeData.get(Constants.ALTERNATOR2_VOLTAGE) != null){
                this.alternator2_voltage.setText(wholeData.get(Constants.ALTERNATOR2_VOLTAGE));
            }
            
            if(wholeData.get(Constants.ALTERNATOR2_CURRENT) != null){
                this.alternator2_current.setText(wholeData.get(Constants.ALTERNATOR2_CURRENT));
            }
            
            if(wholeData.get(Constants.BATTERY_CHARGING_CURRENT) != null){
                this.battery_charging_current.setText(wholeData.get(Constants.BATTERY_CHARGING_CURRENT));
            }
            
            if(wholeData.get(Constants.BATTERY_VOLTAGE) != null){
                this.battery_voltage.setText(wholeData.get(Constants.BATTERY_VOLTAGE));
            }
        }
        catch ( Exception e ){
            System.out.println(e);
        }
        
    }
    
    private int storeCurrentValues(){
        try{
            DAO currentDao = new DAO();
            
            String currentQuery = String.format("insert into current_tbl values (NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    wholeData.get(Constants.RMPU_BLOWER_R), wholeData.get(Constants.RMPU_CD1_R), wholeData.get(Constants.RMPU_CD2_R), wholeData.get(Constants.RMPU_CP1_R), wholeData.get(Constants.RMPU_CP2_R), 
                    wholeData.get(Constants.RMPU_BLOWER_Y), wholeData.get(Constants.RMPU_CD1_Y), wholeData.get(Constants.RMPU_CD2_Y), wholeData.get(Constants.RMPU_CP1_Y), wholeData.get(Constants.RMPU_CP2_Y),
                    wholeData.get(Constants.RMPU_BLOWER_B), wholeData.get(Constants.RMPU_CD1_B), wholeData.get(Constants.RMPU_CD2_B), wholeData.get(Constants.RMPU_CP1_B), wholeData.get(Constants.RMPU_CP2_B));
            int currentRowsAffected = currentDao.putData(currentQuery);
            
            String maxCurrentIdQuery = "Select max(current_data_id) as latest_entry from current_tbl";
            ResultSet maxCurrentData = currentDao.getData(maxCurrentIdQuery);
            maxCurrentData.next();
            int latestCurrentId = maxCurrentData.getInt("latest_entry");
            
            return latestCurrentId;
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return 0;
    }
    
    private int storeTemperatureValues(){
        try{
            DAO temperatureDao = new DAO();
            
            String temperatureQuery = String.format("insert into temperature_tbl values (NULL, '%s', '%s', '%s')",
                    wholeData.get(Constants.GRILL_INPUT), wholeData.get(Constants.AMBIENT_INPUT), wholeData.get(Constants.ROOM_INPUT));
            int temperatureRowsAffected = temperatureDao.putData(temperatureQuery);
            
            String maxTemperatureIdQuery = "Select max(temperature_id) as latest_entry from temperature_tbl";
            ResultSet maxTemperatureData = temperatureDao.getData(maxTemperatureIdQuery);
            maxTemperatureData.next();
            int latestTemperatureId = maxTemperatureData.getInt("latest_entry");
            
            return latestTemperatureId;
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return 0;
    }
    
    private int storeTotalValues(){
        try{
            DAO totalDao = new DAO();
            
            String totalQuery = String.format("insert into total_values_tbl values (NULL, '%s', '%s', '%s', '%s', '%s', '%s')",
                    wholeData.get(Constants.RMPU_TC_R), wholeData.get(Constants.RMPU_TV_R), wholeData.get(Constants.RMPU_TC_Y), wholeData.get(Constants.RMPU_TV_Y), wholeData.get(Constants.RMPU_TC_B), wholeData.get(Constants.RMPU_TV_B));
            int totalRowsAffected = totalDao.putData(totalQuery);
            
            String maxTotalIdQuery = "Select max(total_values_id) as latest_entry from total_values_tbl";
            ResultSet maxTotalData = totalDao.getData(maxTotalIdQuery);
            maxTotalData.next();
            int latestTotalId = maxTotalData.getInt("latest_entry");
            
            return latestTotalId;
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return 0;
    }
    
    private int storeAlternatorValues(){
        try{
            DAO alternatorDao = new DAO();
            
            String alternatorQuery = String.format("insert into alternator_log(serial_number, make, alternator1_current, alternator1_voltage, alternator2_current, alternator2_voltage, battery_voltage, battery_current) "
                    + "values ('%s', '%s', %s', '%s', '%s', '%s', '%s', '%s')",
                    wholeData.get(Constants.ALTERNATOR_SERIAL_INPUT), wholeData.get(Constants.ALTERNATOR_MAKE_INPUT), wholeData.get(Constants.ALTERNATOR1_CURRENT), wholeData.get(Constants.ALTERNATOR1_VOLTAGE),  wholeData.get(Constants.ALTERNATOR2_CURRENT),  wholeData.get(Constants.ALTERNATOR2_VOLTAGE), wholeData.get(Constants.BATTERY_VOLTAGE), wholeData.get(Constants.BATTERY_CHARGING_CURRENT));
            
            int totalRowsAffected = alternatorDao.putData(alternatorQuery);
            
            String maxAlternatorIdQuery = "Select max(alternator_log_id) as latest_entry from alternator_log";
            ResultSet maxAlternatorData = alternatorDao.getData(maxAlternatorIdQuery);
            maxAlternatorData.next();
            int latestTotalId = maxAlternatorData.getInt("latest_entry");
            
            return latestTotalId;
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        return 0;
    }
    
    private boolean saveDataSnapShotRMPU(){
        try{
            int latest_current_id = storeCurrentValues();
            int latest_total_id = storeTotalValues();
            int latest_temperature_id = storeTemperatureValues();
            
            if(latest_current_id != 0 && latest_total_id != 0 && latest_temperature_id != 0){
                
                String serial_number = wholeData.get(Constants.RMPU_SERIAL_INPUT);
                String division = wholeData.get(Constants.RMPU_DIVISION_INPUT);
                String make = wholeData.get(Constants.RMPU_MAKE_INPUT);
                String pp_npp = wholeData.get(Constants.RMPU_TYPE_INPUT);
                String wind_speed = wholeData.get(Constants.WIND_SPEED);
                
                String query = "insert into rmpu_log(serial_number, division, make, pp_npp, current_data_id, total_values_id, temperature_data_id, wind_speed) "
                        + "values ('"+serial_number+"', '"+division+"', '"+make+"', '"+pp_npp+"', '"+latest_current_id+"', '"+latest_total_id+"', '"+latest_temperature_id+"', '"+wind_speed+"')";
                
                DAO RMPUDao = new DAO();
                RMPUDao.putData(query);
                //return true;
            }
            
            // creating report
            extractRMPUData();
            Report report = new Report();
            String fileName = report.generateRMPUReport(RMPUData);
            System.out.println("RMPU saved");
            
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean saveDataSnapShotAlternator(){
        try{
            storeAlternatorValues();
            
            // creating report
            extractAlternatorData();
            Report report = new Report();
            String fileName = report.generateAlternatorReport(AlternatorData);
            
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    private void fillAllKeys(){
        
        wholeData.put(Constants.RMPU_START_STOP_COMMAND, null);
        wholeData.put(Constants.ALTERNATOR_START_STOP_COMMAND, null);
        wholeData.put(Constants.ALTERNATOR1_CURRENT, "-");
        wholeData.put(Constants.ALTERNATOR_MAKE_INPUT, "-");
        wholeData.put(Constants.ALTERNATOR_ACTION_INPUT, null);
        wholeData.put(Constants.ALTERNATOR_SERIAL_INPUT, null);
        wholeData.put(Constants.ALTERNATOR1_VOLTAGE, "-");
        wholeData.put(Constants.AMBIENT_INPUT, "-");
        wholeData.put(Constants.GRILL_INPUT, "-");
        wholeData.put(Constants.RMPU_BLOWER_B, "-");
        wholeData.put(Constants.RMPU_BLOWER_R, "-");
        wholeData.put(Constants.RMPU_BLOWER_Y, "-");
        wholeData.put(Constants.RMPU_CD1_B, "-");
        wholeData.put(Constants.RMPU_CD1_R, "-");
        wholeData.put(Constants.RMPU_CD1_Y, "-");
        wholeData.put(Constants.RMPU_CD2_B, "-");
        wholeData.put(Constants.RMPU_CD2_R, "-");
        wholeData.put(Constants.RMPU_CD2_Y, "-");
        wholeData.put(Constants.RMPU_CP1_B, "-");
        wholeData.put(Constants.RMPU_CP1_R, "-");
        wholeData.put(Constants.RMPU_CP1_Y, "-");
        wholeData.put(Constants.RMPU_CP2_B, "-");
        wholeData.put(Constants.RMPU_CP2_R, "-");
        wholeData.put(Constants.RMPU_CP2_Y, "-");
        wholeData.put(Constants.RMPU_DIVISION_INPUT, "-");
        wholeData.put(Constants.RMPU_MAKE_INPUT, "-");
        wholeData.put(Constants.RMPU_TYPE_INPUT, null);
        wholeData.put(Constants.RMPU_ACTION_INPUT, null);
        wholeData.put(Constants.RMPU_SERIAL_INPUT, null);
        wholeData.put(Constants.RMPU_TC_B, "-");
        wholeData.put(Constants.RMPU_TC_R, "-");
        wholeData.put(Constants.RMPU_TC_Y, "-");
        wholeData.put(Constants.RMPU_TV_B, "-");
        wholeData.put(Constants.RMPU_TV_R, "-");
        wholeData.put(Constants.RMPU_TV_Y, "-");
        wholeData.put(Constants.ROOM_INPUT, "-");
        wholeData.put(Constants.WIND_SPEED, "-");
        
        wholeData.put(Constants.ALTERNATOR2_CURRENT, "-");
        wholeData.put(Constants.ALTERNATOR2_VOLTAGE, "-");
        wholeData.put(Constants.BATTERY_CHARGING_CURRENT, "-");
        wholeData.put(Constants.BATTERY_VOLTAGE, "-");
        
        //RMPUData fill
        RMPUData.put(Constants.RMPU_START_STOP_COMMAND, null);
        RMPUData.put(Constants.AMBIENT_INPUT, null);
        RMPUData.put(Constants.GRILL_INPUT, null);
        RMPUData.put(Constants.RMPU_BLOWER_B, null);
        RMPUData.put(Constants.RMPU_BLOWER_R, null);
        RMPUData.put(Constants.RMPU_BLOWER_Y, null);
        RMPUData.put(Constants.RMPU_CD1_B, null);
        RMPUData.put(Constants.RMPU_CD1_R, null);
        RMPUData.put(Constants.RMPU_CD1_Y, null);
        RMPUData.put(Constants.RMPU_CD2_B, null);
        RMPUData.put(Constants.RMPU_CD2_R, null);
        RMPUData.put(Constants.RMPU_CD2_Y, null);
        RMPUData.put(Constants.RMPU_CP1_B, null);
        RMPUData.put(Constants.RMPU_CP1_R, null);
        RMPUData.put(Constants.RMPU_CP1_Y, null);
        RMPUData.put(Constants.RMPU_CP2_B, null);
        RMPUData.put(Constants.RMPU_CP2_R, null);
        RMPUData.put(Constants.RMPU_CP2_Y, null);
        RMPUData.put(Constants.RMPU_DIVISION_INPUT, null);
        RMPUData.put(Constants.RMPU_MAKE_INPUT, null);
        RMPUData.put(Constants.RMPU_TYPE_INPUT, null);
        RMPUData.put(Constants.RMPU_ACTION_INPUT, null);
        RMPUData.put(Constants.RMPU_SERIAL_INPUT, null);
        RMPUData.put(Constants.RMPU_TC_B, null);
        RMPUData.put(Constants.RMPU_TC_R, null);
        RMPUData.put(Constants.RMPU_TC_Y, null);
        RMPUData.put(Constants.RMPU_TV_B, null);
        RMPUData.put(Constants.RMPU_TV_R, null);
        RMPUData.put(Constants.RMPU_TV_Y, null);
        RMPUData.put(Constants.ROOM_INPUT, null);
        RMPUData.put(Constants.WIND_SPEED, null);
        
        //Alternator data fill
        AlternatorData.put(Constants.ALTERNATOR_START_STOP_COMMAND, null);
        AlternatorData.put(Constants.ALTERNATOR1_CURRENT, null);
        AlternatorData.put(Constants.ALTERNATOR_MAKE_INPUT, null);
        AlternatorData.put(Constants.ALTERNATOR_ACTION_INPUT, null);
        AlternatorData.put(Constants.ALTERNATOR_SERIAL_INPUT, null);
        AlternatorData.put(Constants.ALTERNATOR1_VOLTAGE, null);
        AlternatorData.put(Constants.ALTERNATOR2_CURRENT, null);
        AlternatorData.put(Constants.ALTERNATOR2_VOLTAGE, null);
        AlternatorData.put(Constants.BATTERY_CHARGING_CURRENT, null);
        AlternatorData.put(Constants.BATTERY_VOLTAGE, null);
    }
    
    private Map.Entry<String, String> getCleanData(String data){
        try{
            for(String unwantedString : Constants.STREAM_UNWANTED_STRINGS){
                data = data.replace(unwantedString, "");
            }

            String[] keyValue = data.split(Constants.STREAM_KEY_VALUE_DELIMITER);
            Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(keyValue[0], keyValue[1]);
            
            return entry;
        }
        catch (Exception e){
            return null;
        }
    }
    
    private void setupDataHandlingThread(){
        dataHandleThread = new Thread(() -> {
            while(true){
                try{
                    fillDataRMPU();
                    fillDataAlternator();
                    //rmpu commands
                    if (wholeData.get(Constants.RMPU_START_STOP_COMMAND) != null) {
                        String command = wholeData.get(Constants.RMPU_START_STOP_COMMAND).trim();
                        
                        
                        if (command.equalsIgnoreCase("start")) {
                            //fillDataRMPU();
                            this.mainTabbedPane.setSelectedIndex(0);
                            this.mainTabbedPane.setEnabled(false);
                            wholeData.put(Constants.ALTERNATOR_START_STOP_COMMAND, null);
                            this.revalidate();
                            this.repaint();
                        }

                        if (command.equalsIgnoreCase("stop")) {
                            this.mainTabbedPane.setEnabled(true);
                            wholeData.put(Constants.RMPU_START_STOP_COMMAND, null);
                            this.revalidate();
                            this.repaint();
                        }
                    }
                    
                    //alternator commands
                    if (wholeData.get(Constants.ALTERNATOR_START_STOP_COMMAND) != null) {
                        String command = wholeData.get(Constants.ALTERNATOR_START_STOP_COMMAND).trim();

                        
                        if (command.equalsIgnoreCase("start")) {
                            //fillDataAlternator();
                            this.mainTabbedPane.setSelectedIndex(1);
                            this.mainTabbedPane.setEnabled(false);
                            wholeData.put(Constants.RMPU_START_STOP_COMMAND, null);
                            this.revalidate();
                            this.repaint();
                        }

                        if (command.equalsIgnoreCase("stop")) {
                            this.mainTabbedPane.setEnabled(true);
                            wholeData.put(Constants.ALTERNATOR_START_STOP_COMMAND, null);
                            this.revalidate();
                            this.repaint();
                        }
                    }
                    
                    //rmpu actions
                    if(wholeData.get(Constants.RMPU_ACTION_INPUT) != null){
                        if(wholeData.get(Constants.RMPU_ACTION_INPUT).equalsIgnoreCase("save")){
                            wholeData.put(Constants.RMPU_ACTION_INPUT, null);
                            if (allDataValidForSavingRMPU()) {
                                boolean saved = saveDataSnapShotRMPU();
                                if (saved) {
                                    JOptionPane.showMessageDialog(null, "Data snapshot saved successfully!!!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Problem while saving data!! Try again..");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "All data not valid to save the snapshot, wait until every field is filled with proper data and them press SAVE button");
                            }
                        }
                        else if(wholeData.get(Constants.RMPU_ACTION_INPUT).equalsIgnoreCase("print")){
                            wholeData.put(Constants.RMPU_ACTION_INPUT, null);
                            if (allDataValidForSavingRMPU()) {
                                boolean printed = printRMPU();

                                if (printed) {
                                    JOptionPane.showMessageDialog(null, "Print successful!!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Could not print! Try again..");
                                }
                            } 
                            else {
                                JOptionPane.showMessageDialog(this, "There seems to be an issue with the printer or printer connection, check and try again");
                            }
                        }
                    }
                    
                    //alternator actions
                    if(wholeData.get(Constants.ALTERNATOR_ACTION_INPUT) != null){
                        if(wholeData.get(Constants.ALTERNATOR_ACTION_INPUT).equalsIgnoreCase("save")){
                            wholeData.put(Constants.ALTERNATOR_ACTION_INPUT, null);
                            if (allDataValidForSavingAlternator()) {
                                boolean saved = saveDataSnapShotAlternator();
                                if (saved) {
                                    JOptionPane.showMessageDialog(null, "Data snapshot saved successfully!!!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Problem while saving data!! Try again..");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "All data not valid to save the snapshot, wait until every field is filled with proper data and them press SAVE button");
                            }
                        }
                        else if(wholeData.get(Constants.ALTERNATOR_ACTION_INPUT).equalsIgnoreCase("print")){
                            wholeData.put(Constants.ALTERNATOR_ACTION_INPUT, null);
                            if (allDataValidForSavingAlternator()) {
                                boolean printed = printAlternator();

                                if (printed) {
                                    JOptionPane.showMessageDialog(null, "Print successful!!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Could not print! Try again..");
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "There seems to be an issue with the printer or printer connection, check and try again");
                            }
                        }
                    }                    
                    
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }
    
    private void setupDataThread(){
        dataThread = new Thread(() -> {
            while (true) {
                try {
                    String data = View_Control.this.bufferedReader.readLine();
                    Map.Entry<String, String> entry = getCleanData(data);

                    if (entry != null) {
                        wholeData.put(entry.getKey(), entry.getValue());
                        System.out.println("Received data -> " + entry);
                    }

                } catch (IOException ex) {
                    System.out.println("Problem while reading data from stream -> " + ex);
                    // JOptionPane.showMessageDialog(null, "Experiencing trouble reading data from remote device!!");
                    Logger.getLogger(View_Control.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    private boolean canPrintRMPU(){
        return false;
    }
    
    private boolean canPrintAlternator(){
        return false;
    }
    
    private void extractRMPUData(){
        for(Map.Entry<String, String> entry : RMPUData.entrySet()){
            RMPUData.put(entry.getKey(), wholeData.get(entry.getKey()));
        }
    }
    
    private void extractAlternatorData(){
        for(Map.Entry<String, String> entry : AlternatorData.entrySet()){
            AlternatorData.put(entry.getKey(), wholeData.get(entry.getKey()));
        }
    }
    
    private boolean printRMPU(){
        try{
            extractRMPUData();
            Report report = new Report();
            String fileName = report.generateRMPUReport(RMPUData);            
            System.out.println("RMPU printing, filename -> " + fileName);
            if(fileName != null){
                Thread.sleep(1000);
                //Runtime runtime = Runtime.getRuntime();
                //runtime.exec(Constants.TERMINAL_PRINT_COMMAND + " " + fileName);
                boolean printed = Printer.printPDF(fileName);
                System.out.println("RMPU report printed");
                return printed;
            }
            else{
                System.out.println("Did not print RMPU bcoz of no file name");
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    
    private boolean printAlternator(){
        try{
            extractAlternatorData();
            Report report = new Report();
            String fileName = report.generateAlternatorReport(AlternatorData);
            if(fileName != null){
                Thread.sleep(1000);
                //Runtime runtime = Runtime.getRuntime();
                //runtime.exec(Constants.TERMINAL_PRINT_COMMAND + " "+ fileName);
                boolean printed = Printer.printPDF(fileName);
                return printed;
            }
            else
                return false;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pp_npp_radio_button_group = new javax.swing.ButtonGroup();
        mainScrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        mainTabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        serial_number_label = new javax.swing.JLabel();
        rmpu_serial_input = new javax.swing.JTextField();
        division_label = new javax.swing.JLabel();
        rmpu_division_input = new javax.swing.JTextField();
        division_label1 = new javax.swing.JLabel();
        rmpu_make_input = new javax.swing.JTextField();
        rmpu_pp_input = new javax.swing.JRadioButton();
        rmpu_npp_input = new javax.swing.JRadioButton();
        rmpu_print_input = new javax.swing.JLabel();
        rmpu_save_input = new javax.swing.JLabel();
        view_reports_rmpu_btn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        rmpu_blower_r = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        rmpu_cd1_r = new javax.swing.JTextField();
        rmpu_cd2_r = new javax.swing.JTextField();
        rmpu_cp1_r = new javax.swing.JTextField();
        rmpu_cp2_r = new javax.swing.JTextField();
        rmpu_blower_y = new javax.swing.JTextField();
        rmpu_cd1_y = new javax.swing.JTextField();
        rmpu_cd2_y = new javax.swing.JTextField();
        rmpu_cp1_y = new javax.swing.JTextField();
        rmpu_cp2_y = new javax.swing.JTextField();
        rmpu_blower_b = new javax.swing.JTextField();
        rmpu_cd1_b = new javax.swing.JTextField();
        rmpu_cd2_b = new javax.swing.JTextField();
        rmpu_cp1_b = new javax.swing.JTextField();
        rmpu_cp2_b = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        grill_input = new javax.swing.JTextField();
        ambient_input = new javax.swing.JTextField();
        room_input = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        wind_speed = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        rmpu_tc_r = new javax.swing.JTextField();
        rmpu_tv_r = new javax.swing.JTextField();
        rmpu_tc_y = new javax.swing.JTextField();
        rmpu_tv_y = new javax.swing.JTextField();
        rmpu_tc_b = new javax.swing.JTextField();
        rmpu_tv_b = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        big_bg_lbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        serial_number_label2 = new javax.swing.JLabel();
        alternator_serial_input = new javax.swing.JTextField();
        division_label4 = new javax.swing.JLabel();
        alternator_make_input = new javax.swing.JTextField();
        alternator_save_input = new javax.swing.JLabel();
        alternator_print_input = new javax.swing.JLabel();
        view_reports_alternator_btn = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        alternator1_voltage = new javax.swing.JTextField();
        alternator2_voltage = new javax.swing.JTextField();
        alternator1_current = new javax.swing.JTextField();
        alternator2_current = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        battery_lbl = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        battery_voltage = new javax.swing.JTextField();
        battery_charging_current = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mainTabbedPane.setBackground(new java.awt.Color(153, 153, 153));
        mainTabbedPane.setForeground(new java.awt.Color(51, 0, 51));
        mainTabbedPane.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N

        jPanel2.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Monospaced", 1, 48)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("RMPU Log");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(790, 0, 232, 50);

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        serial_number_label.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        serial_number_label.setForeground(new java.awt.Color(255, 255, 255));
        serial_number_label.setText("Serial Number");

        rmpu_serial_input.setEditable(false);
        rmpu_serial_input.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N

        division_label.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        division_label.setForeground(new java.awt.Color(255, 255, 255));
        division_label.setText("Division");

        rmpu_division_input.setEditable(false);
        rmpu_division_input.setFont(new java.awt.Font("Tahoma", 0, 26)); // NOI18N

        division_label1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        division_label1.setForeground(new java.awt.Color(255, 255, 255));
        division_label1.setText("Make");

        rmpu_make_input.setEditable(false);
        rmpu_make_input.setFont(new java.awt.Font("Tahoma", 0, 26)); // NOI18N

        rmpu_pp_input.setBackground(new java.awt.Color(0, 0, 0));
        rmpu_pp_input.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        rmpu_pp_input.setForeground(new java.awt.Color(255, 255, 255));
        rmpu_pp_input.setText("PP");
        rmpu_pp_input.setEnabled(false);

        rmpu_npp_input.setBackground(new java.awt.Color(0, 0, 0));
        rmpu_npp_input.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        rmpu_npp_input.setForeground(new java.awt.Color(255, 255, 255));
        rmpu_npp_input.setText("NPP");
        rmpu_npp_input.setEnabled(false);

        rmpu_print_input.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        rmpu_print_input.setForeground(new java.awt.Color(255, 255, 255));
        rmpu_print_input.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_icon_small.png"))); // NOI18N
        rmpu_print_input.setText("Print");
        rmpu_print_input.setToolTipText("Click to print the current data");
        rmpu_print_input.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rmpu_print_input.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rmpu_print_inputMouseClicked(evt);
            }
        });

        rmpu_save_input.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        rmpu_save_input.setForeground(new java.awt.Color(255, 255, 255));
        rmpu_save_input.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon_small.png"))); // NOI18N
        rmpu_save_input.setText("Save");
        rmpu_save_input.setToolTipText("Click to save current data");
        rmpu_save_input.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rmpu_save_input.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rmpu_save_inputMouseClicked(evt);
            }
        });

        view_reports_rmpu_btn.setBackground(new java.awt.Color(204, 204, 204));
        view_reports_rmpu_btn.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        view_reports_rmpu_btn.setText("View Reports");
        view_reports_rmpu_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_reports_rmpu_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(division_label1)
                    .addComponent(view_reports_rmpu_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serial_number_label)
                    .addComponent(division_label)
                    .addComponent(rmpu_pp_input, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_npp_input)
                    .addComponent(rmpu_print_input, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(rmpu_serial_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rmpu_division_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rmpu_make_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rmpu_save_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serial_number_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmpu_serial_input, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(division_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmpu_division_input, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(division_label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmpu_make_input, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmpu_pp_input)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmpu_npp_input)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rmpu_save_input, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rmpu_print_input, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(view_reports_rmpu_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5);
        jPanel5.setBounds(0, 0, 320, 680);

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));
        jPanel4.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Monospaced", 3, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 255));
        jLabel2.setText("EQUIPMENTS");

        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 255));
        jLabel4.setText("R");

        jLabel6.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 255));
        jLabel6.setText("B");

        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 255));
        jLabel5.setText("Y");

        jLabel16.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("BLOWER");

        rmpu_blower_r.setEditable(false);
        rmpu_blower_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_blower_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_blower_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_blower_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel17.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("CONDENSOR(CD1)");

        jLabel18.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("CONDENSOR(CD2)");

        jLabel19.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("COMPRESSOR(CP2)");

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("COMPRESSOR(CP1)");

        rmpu_cd1_r.setEditable(false);
        rmpu_cd1_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd1_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd1_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd1_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cd2_r.setEditable(false);
        rmpu_cd2_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd2_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd2_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd2_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp1_r.setEditable(false);
        rmpu_cp1_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp1_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp1_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp1_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp2_r.setEditable(false);
        rmpu_cp2_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp2_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp2_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp2_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_blower_y.setEditable(false);
        rmpu_blower_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_blower_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_blower_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_blower_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cd1_y.setEditable(false);
        rmpu_cd1_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd1_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd1_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd1_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cd2_y.setEditable(false);
        rmpu_cd2_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd2_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd2_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd2_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp1_y.setEditable(false);
        rmpu_cp1_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp1_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp1_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp1_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp2_y.setEditable(false);
        rmpu_cp2_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp2_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp2_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp2_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_blower_b.setEditable(false);
        rmpu_blower_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_blower_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_blower_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_blower_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cd1_b.setEditable(false);
        rmpu_cd1_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd1_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd1_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd1_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cd2_b.setEditable(false);
        rmpu_cd2_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cd2_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cd2_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cd2_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp1_b.setEditable(false);
        rmpu_cp1_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp1_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp1_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp1_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_cp2_b.setEditable(false);
        rmpu_cp2_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_cp2_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_cp2_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_cp2_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addComponent(jLabel4)
                        .addGap(122, 122, 122)
                        .addComponent(jLabel5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rmpu_cd2_r, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_blower_r, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_cd1_r, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rmpu_blower_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_cd1_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_cd2_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rmpu_cp1_r, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_cp2_r, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rmpu_cp2_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpu_cp1_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rmpu_blower_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmpu_cd1_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmpu_cd2_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmpu_cp1_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmpu_cp2_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(74, 74, 74))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rmpu_blower_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rmpu_blower_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rmpu_blower_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rmpu_cd1_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cd1_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cd1_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rmpu_cd2_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cd2_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cd2_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp1_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp1_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp1_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp2_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp2_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_cp2_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4);
        jPanel4.setBounds(320, 70, 740, 410);

        jPanel7.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("Monospaced", 3, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 255));
        jLabel7.setText("TEMPERATURE(°C)");

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GRILL");

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("AMBIENT");

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("ROOM");

        grill_input.setEditable(false);
        grill_input.setBackground(new java.awt.Color(153, 153, 153));
        grill_input.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        grill_input.setForeground(new java.awt.Color(0, 0, 51));
        grill_input.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        ambient_input.setEditable(false);
        ambient_input.setBackground(new java.awt.Color(153, 153, 153));
        ambient_input.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        ambient_input.setForeground(new java.awt.Color(0, 0, 51));
        ambient_input.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        room_input.setEditable(false);
        room_input.setBackground(new java.awt.Color(153, 153, 153));
        room_input.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        room_input.setForeground(new java.awt.Color(0, 0, 51));
        room_input.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22))
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(58, 58, 58)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(grill_input, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(ambient_input)
                            .addComponent(room_input))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grill_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(ambient_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(room_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel7);
        jPanel7.setBounds(1080, 100, 270, 300);

        jPanel9.setOpaque(false);

        jLabel13.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 255));
        jLabel13.setText("Wind Speed");

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fan_icon_small.png"))); // NOI18N

        wind_speed.setEditable(false);
        wind_speed.setBackground(new java.awt.Color(153, 153, 153));
        wind_speed.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        wind_speed.setForeground(new java.awt.Color(0, 0, 51));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(wind_speed, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wind_speed, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel15)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel9);
        jPanel9.setBounds(1080, 460, 250, 140);

        jPanel10.setOpaque(false);

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("TOTAL CURRENT");

        jLabel24.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("TOTAL VOLTAGE");

        rmpu_tc_r.setEditable(false);
        rmpu_tc_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tc_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tc_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tc_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_tv_r.setEditable(false);
        rmpu_tv_r.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tv_r.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tv_r.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tv_r.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_tc_y.setEditable(false);
        rmpu_tc_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tc_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tc_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tc_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_tv_y.setEditable(false);
        rmpu_tv_y.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tv_y.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tv_y.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tv_y.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_tc_b.setEditable(false);
        rmpu_tc_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tc_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tc_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tc_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        rmpu_tv_b.setEditable(false);
        rmpu_tv_b.setBackground(new java.awt.Color(153, 153, 153));
        rmpu_tv_b.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        rmpu_tv_b.setForeground(new java.awt.Color(0, 0, 51));
        rmpu_tv_b.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addGap(51, 51, 51)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rmpu_tc_r, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_tv_r, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(rmpu_tv_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rmpu_tv_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(rmpu_tc_y, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rmpu_tc_b, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(rmpu_tc_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_tc_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_tc_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rmpu_tv_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_tv_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmpu_tv_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel10);
        jPanel10.setBounds(330, 500, 720, 150);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(1070, 140, 10, 510);
        jPanel2.add(jSeparator2);
        jSeparator2.setBounds(340, 490, 710, 10);

        big_bg_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/main_bg.jpg"))); // NOI18N
        jPanel2.add(big_bg_lbl);
        big_bg_lbl.setBounds(320, 0, 1040, 680);

        mainTabbedPane.addTab("               RMPU               ", jPanel2);

        jPanel3.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Monospaced", 1, 48)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 255));
        jLabel12.setText("ALTERNATOR");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(650, 50, 290, 64);

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));

        serial_number_label2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        serial_number_label2.setForeground(new java.awt.Color(255, 255, 255));
        serial_number_label2.setText("Serial Number");

        alternator_serial_input.setEditable(false);
        alternator_serial_input.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        division_label4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        division_label4.setForeground(new java.awt.Color(255, 255, 255));
        division_label4.setText("Make");

        alternator_make_input.setEditable(false);
        alternator_make_input.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        alternator_save_input.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        alternator_save_input.setForeground(new java.awt.Color(255, 255, 255));
        alternator_save_input.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_icon_small.png"))); // NOI18N
        alternator_save_input.setText("Save");
        alternator_save_input.setToolTipText("Click to save current data");
        alternator_save_input.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        alternator_save_input.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                alternator_save_inputMouseClicked(evt);
            }
        });

        alternator_print_input.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        alternator_print_input.setForeground(new java.awt.Color(255, 255, 255));
        alternator_print_input.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_icon_small.png"))); // NOI18N
        alternator_print_input.setText("Print");
        alternator_print_input.setToolTipText("Click to print the current data");
        alternator_print_input.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        alternator_print_input.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                alternator_print_inputMouseClicked(evt);
            }
        });

        view_reports_alternator_btn.setBackground(new java.awt.Color(204, 204, 204));
        view_reports_alternator_btn.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        view_reports_alternator_btn.setText("View Reports");
        view_reports_alternator_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_reports_alternator_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(view_reports_alternator_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator_serial_input, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serial_number_label2)
                    .addComponent(division_label4)
                    .addComponent(alternator_make_input, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator_save_input, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator_print_input, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serial_number_label2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alternator_serial_input, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(division_label4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alternator_make_input, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(alternator_save_input)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alternator_print_input)
                .addGap(18, 18, 18)
                .addComponent(view_reports_alternator_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(612, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6);
        jPanel6.setBounds(0, 0, 394, 1130);

        jPanel11.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Current");

        jLabel9.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Alternator 2");

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Alternator 1");

        jLabel25.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Voltage");

        alternator1_voltage.setEditable(false);
        alternator1_voltage.setBackground(new java.awt.Color(153, 153, 153));
        alternator1_voltage.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        alternator1_voltage.setForeground(new java.awt.Color(0, 0, 51));
        alternator1_voltage.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        alternator2_voltage.setEditable(false);
        alternator2_voltage.setBackground(new java.awt.Color(153, 153, 153));
        alternator2_voltage.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        alternator2_voltage.setForeground(new java.awt.Color(0, 0, 51));
        alternator2_voltage.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        alternator1_current.setEditable(false);
        alternator1_current.setBackground(new java.awt.Color(153, 153, 153));
        alternator1_current.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        alternator1_current.setForeground(new java.awt.Color(0, 0, 51));
        alternator1_current.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        alternator2_current.setEditable(false);
        alternator2_current.setBackground(new java.awt.Color(153, 153, 153));
        alternator2_current.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        alternator2_current.setForeground(new java.awt.Color(0, 0, 51));
        alternator2_current.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(80, 80, 80)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alternator1_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator2_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(98, 98, 98)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(alternator1_current, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator2_current, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(180, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(154, 154, 154))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator1_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator1_current, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator2_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternator2_current, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
        );

        jPanel3.add(jPanel11);
        jPanel11.setBounds(410, 130, 880, 300);

        jPanel8.setOpaque(false);

        battery_lbl.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        battery_lbl.setForeground(new java.awt.Color(255, 255, 255));
        battery_lbl.setText("Battery");

        jLabel26.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Voltage");

        jLabel27.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Current");

        battery_voltage.setEditable(false);
        battery_voltage.setBackground(new java.awt.Color(153, 153, 153));
        battery_voltage.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        battery_voltage.setForeground(new java.awt.Color(0, 0, 51));
        battery_voltage.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        battery_charging_current.setEditable(false);
        battery_charging_current.setBackground(new java.awt.Color(153, 153, 153));
        battery_charging_current.setFont(new java.awt.Font("Monospaced", 1, 30)); // NOI18N
        battery_charging_current.setForeground(new java.awt.Color(0, 0, 51));
        battery_charging_current.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(battery_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(battery_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(battery_charging_current, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(103, 103, 103))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(battery_voltage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(battery_charging_current, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(battery_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel8);
        jPanel8.setBounds(420, 440, 810, 200);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/main_bg.jpg"))); // NOI18N
        jPanel3.add(jLabel14);
        jLabel14.setBounds(0, 0, 1360, 800);

        mainTabbedPane.addTab("              ALTERNATOR              ", jPanel3);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 139, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 107, Short.MAX_VALUE))
        );

        mainScrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1365, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rmpu_save_inputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rmpu_save_inputMouseClicked
        // TODO add your handling code here:
        if(allDataValidForSavingRMPU()){
            boolean saved = saveDataSnapShotRMPU();
            if(saved)
                JOptionPane.showMessageDialog(null, "Data snapshot saved successfully!!!");
            else
                JOptionPane.showMessageDialog(null, "Problem while saving data!! Try again..");
        }
        else{
            JOptionPane.showMessageDialog(null, "All data not valid to save the snapshot, wait until every field is filled with proper data and them press SAVE button");
        }
    }//GEN-LAST:event_rmpu_save_inputMouseClicked

    private void alternator_save_inputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_alternator_save_inputMouseClicked
        // TODO add your handling code here:
        if (allDataValidForSavingAlternator()) {
            boolean saved = saveDataSnapShotAlternator();
            if (saved) {
                JOptionPane.showMessageDialog(null, "Data snapshot saved successfully!!!");
            } else {
                JOptionPane.showMessageDialog(null, "Problem while saving data!! Try again..");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "All data not valid to save the snapshot, wait until every field is filled with proper data and them press SAVE button");
        }
    }//GEN-LAST:event_alternator_save_inputMouseClicked

    private void alternator_print_inputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_alternator_print_inputMouseClicked
        // TODO add your handling code here:
        if (allDataValidForSavingAlternator()) {
            boolean printed = printAlternator();

            if (printed) {
                JOptionPane.showMessageDialog(null, "Print successful!!");
            } else {
                JOptionPane.showMessageDialog(null, "Could not print! Try again..");
            }
        } else {
            JOptionPane.showMessageDialog(this, "There seems to be an issue with the printer or printer connection, check and try again");
        }
    }//GEN-LAST:event_alternator_print_inputMouseClicked

    private void view_reports_rmpu_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_reports_rmpu_btnActionPerformed
        // TODO add your handling code here:
        try{
            File reportsDir = new File("reports/");
            Desktop desktop = Desktop.getDesktop();
            Robot robot = new Robot();
            
            // opening reports dir in OS file explorer
            desktop.open(reportsDir);
            
            // pressing F9 to hide side bar
            //robot.keyPress(KeyEvent.VK_F9);
            //robot.keyRelease(KeyEvent.VK_F9);          
            
        }
        catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error loading reports. " + e);
        }
    }//GEN-LAST:event_view_reports_rmpu_btnActionPerformed

    private void view_reports_alternator_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_reports_alternator_btnActionPerformed
        // TODO add your handling code here:
        try{
            File reportsDir = new File("reports/");
            Desktop desktop = Desktop.getDesktop();
            Robot robot = new Robot();
            
            // opening reports dir in OS file explorer
            desktop.open(reportsDir);
            
            // pressing F9 to hide side bar
            robot.keyPress(KeyEvent.VK_F9);
            robot.keyRelease(KeyEvent.VK_F9);          
            
        }
        catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error loading reports. " + e);
        }
    }//GEN-LAST:event_view_reports_alternator_btnActionPerformed

    private void rmpu_print_inputMouseClicked(java.awt.event.MouseEvent evt) {                                              
        // TODO add your handling code here:
        if(allDataValidForSavingRMPU()){
            boolean printed = printRMPU();
            
            if(printed)
                JOptionPane.showMessageDialog(null, "Print successful!!");
            else
                JOptionPane.showMessageDialog(null, "Could not print! Try again..");
        }
        else{
            JOptionPane.showMessageDialog(this, "There seems to be an issue with the printer or printer connection, check and try again");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */ 
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View_Control.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_Control.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_Control.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_Control.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new View_Control().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alternator1_current;
    private javax.swing.JTextField alternator1_voltage;
    private javax.swing.JTextField alternator2_current;
    private javax.swing.JTextField alternator2_voltage;
    private javax.swing.JTextField alternator_make_input;
    private javax.swing.JLabel alternator_print_input;
    private javax.swing.JLabel alternator_save_input;
    private javax.swing.JTextField alternator_serial_input;
    private javax.swing.JTextField ambient_input;
    private javax.swing.JTextField battery_charging_current;
    private javax.swing.JLabel battery_lbl;
    private javax.swing.JTextField battery_voltage;
    private javax.swing.JLabel big_bg_lbl;
    private javax.swing.JLabel division_label;
    private javax.swing.JLabel division_label1;
    private javax.swing.JLabel division_label4;
    private javax.swing.JTextField grill_input;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.ButtonGroup pp_npp_radio_button_group;
    private javax.swing.JTextField rmpu_blower_b;
    private javax.swing.JTextField rmpu_blower_r;
    private javax.swing.JTextField rmpu_blower_y;
    private javax.swing.JTextField rmpu_cd1_b;
    private javax.swing.JTextField rmpu_cd1_r;
    private javax.swing.JTextField rmpu_cd1_y;
    private javax.swing.JTextField rmpu_cd2_b;
    private javax.swing.JTextField rmpu_cd2_r;
    private javax.swing.JTextField rmpu_cd2_y;
    private javax.swing.JTextField rmpu_cp1_b;
    private javax.swing.JTextField rmpu_cp1_r;
    private javax.swing.JTextField rmpu_cp1_y;
    private javax.swing.JTextField rmpu_cp2_b;
    private javax.swing.JTextField rmpu_cp2_r;
    private javax.swing.JTextField rmpu_cp2_y;
    private javax.swing.JTextField rmpu_division_input;
    private javax.swing.JTextField rmpu_make_input;
    private javax.swing.JRadioButton rmpu_npp_input;
    private javax.swing.JRadioButton rmpu_pp_input;
    private javax.swing.JLabel rmpu_print_input;
    private javax.swing.JLabel rmpu_save_input;
    private javax.swing.JTextField rmpu_serial_input;
    private javax.swing.JTextField rmpu_tc_b;
    private javax.swing.JTextField rmpu_tc_r;
    private javax.swing.JTextField rmpu_tc_y;
    private javax.swing.JTextField rmpu_tv_b;
    private javax.swing.JTextField rmpu_tv_r;
    private javax.swing.JTextField rmpu_tv_y;
    private javax.swing.JTextField room_input;
    private javax.swing.JLabel serial_number_label;
    private javax.swing.JLabel serial_number_label2;
    private javax.swing.JButton view_reports_alternator_btn;
    private javax.swing.JButton view_reports_rmpu_btn;
    private javax.swing.JTextField wind_speed;
    // End of variables declaration//GEN-END:variables
}
