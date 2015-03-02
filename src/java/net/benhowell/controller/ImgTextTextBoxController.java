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
import com.typesafe.config.ConfigException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import net.benhowell.core.Display;
import net.benhowell.core.Store;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net] on 23-Feb-2015.
 */
public class ImgTextTextBoxController extends ScreenController implements Initializable {

  private Store store;
  private Config config;
  @FXML private ImageView imageView;
  @FXML private Label label;
  @FXML private TextField textField;
  //@FXML private BorderPane borderPane;
  private Label validationLabel;
  @FXML private HBox hBox;


  public ImgTextTextBoxController(Object parent, Display display, Store store){
    super(parent, "ImgTextTextBox.fxml", display);

    this.store = store;

    validationLabel = new Label();
    validationLabel.setPrefHeight(30.0);
    validationLabel.setPrefWidth(568.0000999999975);
    validationLabel.setText("Error: Please enter a name containing alphabetical characters only!");
    validationLabel.setVisible(false);

    nextButton.setDisable(true);

    hBox.getChildren().add(0,validationLabel);

    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue.matches("^[a-zA-Z\\s]+$"))
        validInput();
      else if(newValue.matches("^$"))
        emptyInput();
      else
        invalidInput();
    });


    this.prevButton.setOnAction(e -> triggerPrevButtonEvent());

    this.nextButton.setOnAction(e -> {
      if(!config.hasPath("example"))
        store.addTrial(config, getResult());
      triggerNextButtonEvent();
    });

  }


  public String getResult(){
    return textField.getText();
  }


  public void update(Config config) {
    this.config = config;
    String txt = config.getString("text").replace("${name}", config.getString("name"));
    Image img = new Image(this.getClass().getResource("/img/").toString() + config.getString("image"));

    super.update(() -> {
      textField.clear();
      label.setText(txt);
      imageView.setImage(img);

      if(config.hasPath("example")) { // config item is an example
        textField.setText(config.getString("example"));
        textField.setEditable(false);
        nextButton.requestFocus();
      }
      else { // config item is a trial
        Config trial = store.getTrial(config.getString("id"));
        if(trial != null) {
          textField.setText(trial.getString("result"));
        }
        textField.setEditable(true);
        textField.requestFocus();
      }

    });
  }


  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");
  }


  private void reset(){
    textField.requestFocus();
  }


  private void emptyInput(){
    nextButton.setDisable(true);
    switchStyle(validationLabel, "validation_error", "validation_valid");
    switchStyle(textField, "validation_error", "validation_valid");
    textField.requestFocus();
  }


  private void invalidInput(){
    nextButton.setDisable(true);
    switchStyle(validationLabel, "validation_valid", "validation_error");
    switchStyle(textField, "validation_valid", "validation_error");
    textField.requestFocus();
  }


  private void validInput(){
    nextButton.setDisable(false);
    switchStyle(validationLabel, "validation_error", "validation_valid");
    switchStyle(textField, "validation_error", "validation_valid");
    textField.requestFocus();
  }
}

