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


import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import net.benhowell.core.Display;
import net.benhowell.core.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */




public class LanguageController extends ControllerLoader implements Initializable {

  @FXML private GridPane languageGridPane = null;
  @FXML public Button nextButton = null;
  @FXML public Button prevButton = null;

  @FXML private TableView<LangControllerRow> languageTableView = null;
  @FXML private TableColumn<LangControllerRow, String> languageColumn = null;
  @FXML private TableColumn<LangControllerRow, String> fluencyColumn = null;
  @FXML private TableColumn<LangControllerRow, Boolean> removeColumn = null;
  @FXML private ComboBox<String> languageComboBox = null;
  @FXML private ComboBox<String> fluencyComboBox = null;
  @FXML private Button addButton = null;

  private Display display;
  private Node node;
  private ObservableList<LangControllerRow> data;

  public LanguageController(ControllerLoader loader, String resource, Display display){
    this.display = display;
    try {
      node = loader.controllerLoader(this, resource);
    }
    catch (IOException e) {
      System.out.println("Controller loader failed to load view: " + e);
    }
  }

  public Map<String, String> getResult(){
    Map<String, String> m = new HashMap<>();
    for(LangControllerRow r : data){
      m.put(r.getLanguage(), r.getFluency());
    }
    return m;
  }

  public void load() {
    display.fxRun( () -> {
      display.loadScreen(node);
      nextButton.requestFocus();
    } );
  }

  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");

    List<String> langList = Util.listFromResource("/iso639-1.txt");

    List<String> fluencyList = new ArrayList<>();
    fluencyList.add("Fluent");
    fluencyList.add("Semi-fluent");
    fluencyList.add("Basic");

     data = FXCollections.observableArrayList();

    languageComboBox.getItems().addAll(FXCollections.observableList(langList));
    languageComboBox.setValue("Chinese (Simplified)");

    fluencyComboBox.getItems().addAll(FXCollections.observableList(fluencyList));
    fluencyComboBox.setValue("Basic");

    languageColumn.setCellValueFactory(
        new PropertyValueFactory<>("language")
    );
    languageColumn.setEditable(true);

    fluencyColumn.setCellValueFactory(
        new PropertyValueFactory<>("fluency")
    );
    fluencyColumn.setEditable(true);


    TableColumn<LangControllerRow, Boolean> colRemove =
        (TableColumn<LangControllerRow, Boolean>)languageTableView.getColumns().get(2);

    colRemove.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
    colRemove.setCellFactory(p -> new ButtonCell());
    removeColumn.setEditable(false);

    languageTableView.setEditable(true);
    languageTableView.setItems(data);

    addButton.setOnAction(e ->
        data.add(new LangControllerRow(languageComboBox.getValue(), fluencyComboBox.getValue())));


  }


  private class ButtonCell extends TableCell<LangControllerRow, Boolean> {
    final Button cellButton = new Button("Delete");
    ButtonCell(){
      cellButton.setOnAction(e -> {
        LangControllerRow curRow = ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
        data.remove(curRow);
      });
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
      super.updateItem(t, empty);
      if(!empty)
        setGraphic(cellButton);
      else
        setGraphic(null);
    }
  }
}
