/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ben Howell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.benhowell.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Howell [ben@benhowell.net] on 17-Feb-2015.
 */
public class Store {

  HashMap<String, Map<String,String>> trials = new HashMap<>();
  Map<String,String> details = new HashMap<>();
  Map<String, String> languages = new HashMap<>();

  public void addTrial(Map<String,String> trial, String result){


    String txt = trial.get("text").replace("${name}", trial.get("name"));
    System.out.println("\nStoring result");
    System.out.println("-------------------------");
    System.out.println(" id: " + trial.get("id"));
    System.out.println(" category: " + trial.get("category"));
    System.out.println(" type: " + trial.get("type"));
    System.out.println(" image: " + trial.get("image"));
    System.out.println(" text: " + trial.get("text"));
    System.out.println(" name: " + trial.get("name"));
    System.out.println(" presented text: " + txt);
    System.out.println(" result: " + result);
    System.out.println("-------------------------\n");
    trials.put(trial.get("id"), trial);
  }

  public Map<String,String> getTrial(String id){
    return trials.remove(id);
  }

  public void setDetails(Map<String, String> detail){
    System.out.println("\nStoring details");
    System.out.println("-------------------------");
    System.out.println(" sex: " + detail.get("sex"));
    System.out.println(" age: " + detail.get("age"));
    System.out.println(" yearsInAustralia: " + detail.get("yearsInAustralia"));
    System.out.println(" monthsInAustralia: " + detail.get("monthsInAustralia"));
    System.out.println(" firstLanguage: " + detail.get("firstLanguage"));
    System.out.println("-------------------------\n");
    details = detail;
  }

  public Map<String, String> getDetails(){
    return details;
  }

  public void setLanguages(Map<String, String> l){
    System.out.println("\nStoring result");
    System.out.println("-------------------------");
    for (Map.Entry<String, String> entry : l.entrySet()) {
      System.out.println(" " + entry.getKey() + ": " + entry.getValue());
    }
    System.out.println("-------------------------\n");

    languages = l;
  }

  public Map<String, String> getLanguages(){
    return languages;
  }

  public void generateXlsx(File file){

    try {
      FileOutputStream out = new FileOutputStream(file.getAbsoluteFile());
      SXSSFWorkbook wb = new SXSSFWorkbook(100);
      Sheet shDetails = wb.createSheet("Details");
      Sheet shLanguages = wb.createSheet("Languages");
      Sheet shResults = wb.createSheet("Results");

      Row row = shDetails.createRow(0);
      Cell cell = row.createCell(0);
      cell.setCellValue("sex");
      cell = row.createCell(1);
      cell.setCellValue("age");
      cell = row.createCell(2);
      cell.setCellValue("years in Australia");
      cell = row.createCell(3);
      cell.setCellValue("months in Australia");
      cell = row.createCell(4);
      cell.setCellValue("first language");

      row = shDetails.createRow(1);
      cell = row.createCell(0);
      cell.setCellValue(details.get("sex"));
      cell = row.createCell(1);
      cell.setCellValue(details.get("age"));
      cell = row.createCell(2);
      cell.setCellValue(details.get("yearsInAustralia"));
      cell = row.createCell(3);
      cell.setCellValue(details.get("monthsInAustralia"));
      cell = row.createCell(4);
      cell.setCellValue(details.get("firstLanguage"));


      int i = 1;
      row = shLanguages.createRow(0);
      cell = row.createCell(0);
      cell.setCellValue("language");
      cell = row.createCell(1);
      cell.setCellValue("fluency");
      for (Map.Entry<String, String> e : languages.entrySet()) {
        row = shLanguages.createRow(i);
        cell = row.createCell(0);
        cell.setCellValue(e.getKey());
        cell = row.createCell(1);
        cell.setCellValue(e.getValue());
        i++;
      }


      i = 1;
      row = shResults.createRow(0);
      cell = row.createCell(0);
      cell.setCellValue("trial_id");
      cell = row.createCell(1);
      cell.setCellValue("category");
      cell = row.createCell(2);
      cell.setCellValue("type");
      cell = row.createCell(3);
      cell.setCellValue("image");
      cell = row.createCell(4);
      cell.setCellValue("text");
      cell = row.createCell(5);
      cell.setCellValue("name");
      cell = row.createCell(6);
      cell.setCellValue("result");
      for (Map.Entry<String, Map<String,String>> e : trials.entrySet()) {
        row = shResults.createRow(i);
        cell = row.createCell(0);
        cell.setCellValue(e.getValue().get("id"));
        cell = row.createCell(1);
        cell.setCellValue(e.getValue().get("category"));
        cell = row.createCell(2);
        cell.setCellValue(e.getValue().get("type"));
        cell = row.createCell(3);
        cell.setCellValue(e.getValue().get("image"));
        cell = row.createCell(4);
        cell.setCellValue(e.getValue().get("text"));
        cell = row.createCell(5);
        cell.setCellValue(e.getValue().get("name"));
        cell = row.createCell(6);
        cell.setCellValue(e.getValue().get("result"));
        i++;
      }

      wb.write(out);
      out.close();

      // dispose of temporary files backing this workbook on disk
      wb.dispose();
    }
    catch (IOException e){
      System.out.println("Exception: " + e);
    }

  }


  public String generateCSV() {
    StringBuilder sb = new StringBuilder();
    char sep = ';';

    sb.append("sex" + sep + "age" + sep + "years in Australia" + sep + "months in Australia" + sep + "first language");
    sb.append('\n');

    sb.append(trials.get("detail").get("sex"));
    sb.append(sep);
    sb.append(trials.get("detail").get("age"));
    sb.append(sep);
    sb.append(trials.get("detail").get("yearsInAustralia"));
    sb.append(sep);
    sb.append(trials.get("detail").get("monthsInAustralia"));
    sb.append(sep);
    sb.append(trials.get("detail").get("firstLanguage"));
    sb.append('\n');
    sb.append('\n');



    sb.append("language" + sep + "fluency");
    sb.append('\n');
    trials.get("languages").entrySet()
        .stream()
        .forEach(e -> {
          sb.append(e.getKey());
          sb.append(sep);
          sb.append(e.getValue());
          sb.append('\n');
        });
    sb.append('\n');


    sb.append("trial_id" + sep + "category" + sep + "type" + sep + "image" + sep + "text" + sep + "name" + sep + "result");
    sb.append('\n');

    trials.entrySet()
      .stream()
      .forEach(e -> {
        if (e.getKey().startsWith("trial")) {
          sb.append(e.getValue().get("id"));
          sb.append(sep);
          sb.append(e.getValue().get("category"));
          sb.append(sep);
          sb.append(e.getValue().get("type"));
          sb.append(sep);
          sb.append(e.getValue().get("image"));
          sb.append(sep);
          sb.append(e.getValue().get("text"));
          sb.append(sep);
          sb.append(e.getValue().get("name"));
          sb.append(sep);
          sb.append(e.getValue().get("result"));
          sb.append('\n');
        }
      });
    return sb.toString();
  }
}
