package com.automationio.stepdefinitions.ui;

import java.util.stream.Stream;

import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.cucumber.java.en.Given;

public class LoginStep {

    @Given("I launch {string} browser")
    public void i_launch_browser(String string) 
    {
        
    }

    @Given("I read text from excel")
    public void i_read_from_excel() 
    {       
        String filePath =System.getProperty("user.dir")+"/src/test/data/Book1.xlsx";
        try (InputStream is = new FileInputStream(new File(filePath)); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();
            sheet.
            try (Stream<Row> rows = sheet.openStream()) {
                
                rows.forEach(r -> {
                    //BigDecimal num = r.getCellAsNumber(0).orElse(null);
                    String str = r.getCellAsString(1).orElse(null);
                    System.out.println(str);
                    r
                   // LocalDateTime date = r.getCellAsDate(2).orElse(null);
                });
            }
        }catch(Exception e){
            System.out.println("");
            e.printStackTrace();
        }
    }

   
    

}

