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
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Howell [ben@benhowell.net] on 17-Feb-2015.
 */
public class Store {

  HashMap<String, Config> results = new HashMap<>();
  Map<String, String> detail;
  Map<String, String> language;



  public Config getTrial(String id){
    return results.get("id");
  }

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

    results.put(newConfig.getString("id"), newConfig);

  }

  public void addDetail(Map<String, String> result){
    System.out.println("\nStoring result");
    System.out.println("-------------------------");
    System.out.println(" sex: " + result.get("sex"));
    System.out.println(" age: " + result.get("age"));
    System.out.println(" yearsInAustralia: " + result.get("yearsInAustralia"));
    System.out.println(" monthsInAustralia: " + result.get("monthsInAustralia"));
    System.out.println(" firstLanguage: " + result.get("firstLanguage"));
    System.out.println("-------------------------\n");
    detail = result;
  }

  public void addLanguage(Map<String, String> result){
    System.out.println("\nStoring result");
    System.out.println("-------------------------");
    for (Map.Entry<String, String> entry : result.entrySet()) {
      System.out.println(" " + entry.getKey() + ": " + entry.getValue());
    }
    System.out.println("-------------------------\n");
    language = result;
  }

  public String generateCSV(){
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("FIXME");
    stringBuilder.append(',');
    stringBuilder.append('\n');

    stringBuilder.append("FIXME");
    stringBuilder.append(',');
    stringBuilder.append('\n');
    return stringBuilder.toString();
  }

}
