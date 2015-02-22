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

package net.benhowell.controller;


import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Ben Howell [ben@benhowell.net] on 11-Feb-2015.
 */
public class LangControllerRow{

  private SimpleStringProperty language;
  private SimpleStringProperty fluency;

  public LangControllerRow(String l, String f) {
    language = new SimpleStringProperty(l);
    fluency = new SimpleStringProperty(f);
  }

  public String getLanguage(){
    return language.get();
  }

  public void setLanguage(String l){
    language.set(l);
  }

  public String getFluency(){
    return fluency.get();
  }

  public void setFluency(String f) {
    fluency.set(f);
  }

}
