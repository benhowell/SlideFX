/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Ben Howell
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

import com.typesafe.config.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import net.benhowell.core.Display;
import net.benhowell.core.Store;
import net.benhowell.core.Util;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Ben Howell [ben@benhowell.net]
 */
public class DetailController extends ScreenController {

  @FXML private ComboBox<String> sexComboBox;
  @FXML private ComboBox<Integer> ageComboBox;
  @FXML private ComboBox<Integer> yearComboBox;
  @FXML private ComboBox<Integer> monthComboBox;
  @FXML private ComboBox<String> firstLanguageComboBox;

  private Store store;

  public DetailController(Object parent, Display display, Store store){
    super(parent, "Detail.fxml", display);

    this.store = store;

    sexComboBox.getItems().addAll(
        "Female",
        "Male",
        "Other"
    );
    sexComboBox.setValue("Female");

    ObservableList<Integer> yearList  = FXCollections.observableList(
        IntStream.range(0, 100).boxed().collect(Collectors.toList())
    );

    ageComboBox.setItems(yearList);
    ageComboBox.setValue(20);
    yearComboBox.setItems(yearList);
    yearComboBox.setValue(20);

    ObservableList<Integer> monthList  = FXCollections.observableList(
        IntStream.range(0, 12).boxed().collect(Collectors.toList())
    );

    monthComboBox.setItems(monthList);
    monthComboBox.setValue(0);

    List<String> langList = Util.listFromResource("/iso639-1.txt");

    List<String> fluencyList = new ArrayList<>();
    fluencyList.add("Fluent");
    fluencyList.add("Semi-fluent");
    fluencyList.add("Basic");

    firstLanguageComboBox.getItems().addAll(FXCollections.observableList(langList));
    firstLanguageComboBox.setValue("English");


    this.prevButton.setOnAction(e -> triggerPrevButtonEvent());
    this.nextButton.setOnAction(e -> {
      if(validateForm()) {
        store.setDetails(getResult());
        triggerNextButtonEvent();
      }
    });
  }

  public Map<String, String> getResult(){
    Map<String, String> m = new HashMap<>();
    m.put("sex", sexComboBox.getValue());
    m.put("age", ageComboBox.getValue().toString());
    m.put("yearsInAustralia", yearComboBox.getValue().toString());
    m.put("monthsInAustralia", monthComboBox.getValue().toString());
    m.put("firstLanguage", firstLanguageComboBox.getValue());
    return m;
  }

  public void load(Config items) {

  }

  public void update(Config items) {
    super.update(() -> {
      Map<String, String> m = store.getDetails();
      if(!m.isEmpty()) {
        sexComboBox.setValue(m.get("sex"));
        ageComboBox.setValue(Integer.parseInt(m.get("age")));
        yearComboBox.setValue(Integer.parseInt(m.get("yearsInAustralia")));
        monthComboBox.setValue(Integer.parseInt(m.get("monthsInAustralia")));
        firstLanguageComboBox.setValue(m.get("firstLanguage"));
      }
      nextButton.requestFocus();
    });
  }

  public void initialize(URL url, ResourceBundle rb) {
  }

  private boolean validateForm(){
    if(ageComboBox.getValue() < yearComboBox.getValue()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Oops, something has gone wrong...");
      alert.setContentText("" +
          "Please ensure that your age is not less\n" +
          "than years you've lived in Australia!");

      alert.showAndWait();
      return false;
    }
    else{
      return true;
    }
  }
}
