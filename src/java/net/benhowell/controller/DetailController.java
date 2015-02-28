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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import net.benhowell.core.Display;
import net.benhowell.core.Store;
import net.benhowell.core.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Ben Howell [ben@benhowell.net] on 28-Feb-2015.
 */
public class DetailController extends ScreenController implements Initializable {

  @FXML private ComboBox<String> sexComboBox;
  @FXML private ComboBox<Integer> ageComboBox;
  @FXML private ComboBox<Integer> yearComboBox;
  @FXML private ComboBox<Integer> monthComboBox;
  @FXML private ComboBox<String> firstLanguageComboBox;
  private Node child;

  public DetailController(ControllerLoader loader, Display display, Store store){
    super(loader, "Detail.fxml", display);




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
      store.addDetail(getResult());
      triggerNextButtonEvent();
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
      nextButton.requestFocus();
    });
  }

  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");


  }


}
