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
import org.apache.poi.ss.util.CellReference;
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

  HashMap<String, Config> trials = new HashMap<>();
  Config details;
  Map<String, String> languages = new HashMap<>();

  public void addTrial(Config trial, String result){
    Config newConfig = trial.withValue("result",  ConfigValueFactory.fromAnyRef(result));

    String txt = newConfig.getString("text").replace("${name}", newConfig.getString("name"));
    System.out.println("\nStoring result");
    System.out.println("-------------------------");
    System.out.println(" id: " + newConfig.getString("id"));
    System.out.println(" category: " + newConfig.getString("category"));
    System.out.println(" type: " + newConfig.getString("type"));
    System.out.println(" image: " + newConfig.getString("image"));
    System.out.println(" text: " + newConfig.getString("text"));
    System.out.println(" name: " + newConfig.getString("name"));
    System.out.println(" presented text: " + txt);
    System.out.println(" result: " + result);
    System.out.println("-------------------------\n");
    trials.put(newConfig.getString("id"), newConfig);
  }

  public Config getTrial(String id){
    return trials.remove(id);
  }

  public void addDetails(Map<String, String> detail){
    System.out.println("\nStoring details");
    System.out.println("-------------------------");
    System.out.println(" sex: " + detail.get("sex"));
    System.out.println(" age: " + detail.get("age"));
    System.out.println(" yearsInAustralia: " + detail.get("yearsInAustralia"));
    System.out.println(" monthsInAustralia: " + detail.get("monthsInAustralia"));
    System.out.println(" firstLanguage: " + detail.get("firstLanguage"));
    System.out.println("-------------------------\n");
    details = ConfigValueFactory.fromMap(detail).toConfig();
  }

  public Config getDetails(){
    return details;
  }

  public void addLanguages(Map<String, String> l){
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
      cell.setCellValue(details.getString("sex"));
      cell = row.createCell(1);
      cell.setCellValue(details.getString("age"));
      cell = row.createCell(2);
      cell.setCellValue(details.getString("yearsInAustralia"));
      cell = row.createCell(3);
      cell.setCellValue(details.getString("monthsInAustralia"));
      cell = row.createCell(4);
      cell.setCellValue(details.getString("firstLanguage"));


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
      for (Map.Entry<String, Config> e : trials.entrySet()) {
        row = shResults.createRow(i);
        cell = row.createCell(0);
        cell.setCellValue(e.getValue().getString("id"));
        cell = row.createCell(1);
        cell.setCellValue(e.getValue().getString("category"));
        cell = row.createCell(2);
        cell.setCellValue(e.getValue().getString("type"));
        cell = row.createCell(3);
        cell.setCellValue(e.getValue().getString("image"));
        cell = row.createCell(4);
        cell.setCellValue(e.getValue().getString("text"));
        cell = row.createCell(5);
        cell.setCellValue(e.getValue().getString("name"));
        cell = row.createCell(6);
        cell.setCellValue(e.getValue().getString("result"));
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

    sb.append(trials.get("detail").getString("sex"));
    sb.append(sep);
    sb.append(trials.get("detail").getString("age"));
    sb.append(sep);
    sb.append(trials.get("detail").getString("yearsInAustralia"));
    sb.append(sep);
    sb.append(trials.get("detail").getString("monthsInAustralia"));
    sb.append(sep);
    sb.append(trials.get("detail").getString("firstLanguage"));
    sb.append('\n');
    sb.append('\n');



    sb.append("language" + sep + "fluency");
    sb.append('\n');
    trials.get("languages").entrySet()
        .stream()
        .forEach(e -> {
          sb.append(e.getKey().toString());
          sb.append(sep);
          sb.append(e.getValue().unwrapped().toString());
          sb.append('\n');
        });
    sb.append('\n');


    sb.append("trial_id" + sep + "category" + sep + "type" + sep + "image" + sep + "text" + sep + "name" + sep + "result");
    sb.append('\n');

    trials.entrySet()
      .stream()
      .forEach(e -> {
        if (e.getKey().startsWith("trial")) {
          sb.append(e.getValue().getString("id"));
          sb.append(sep);
          sb.append(e.getValue().getString("category"));
          sb.append(sep);
          sb.append(e.getValue().getString("type"));
          sb.append(sep);
          sb.append(e.getValue().getString("image"));
          sb.append(sep);
          sb.append(e.getValue().getString("text"));
          sb.append(sep);
          sb.append(e.getValue().getString("name"));
          sb.append(sep);
          sb.append(e.getValue().getString("result"));
          sb.append('\n');
        }
      });
    return sb.toString();
  }
}
