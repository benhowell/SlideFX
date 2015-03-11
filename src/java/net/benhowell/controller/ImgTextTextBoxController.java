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
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.benhowell.core.Display;
import net.benhowell.core.Store;
import net.benhowell.core.Util;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net]
 */
public class ImgTextTextBoxController extends ScreenController {

  private Store store;
  private Config config;
  @FXML private ImageView imageView;
  @FXML private Label label;
  @FXML private TextField textField;
  @FXML private Label validationLabel;
  @FXML private HBox hBox;


  public ImgTextTextBoxController(Object parent, Display display, Store store){
    super(parent, "ImgTextTextBox.fxml", display);

    this.store = store;

    validationLabel.setText("Error: Please enter a name containing alphabetical characters only!");
    validationLabel.setVisible(false);

    nextButton.setDisable(true);

    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue.matches("^[a-zA-Z\\s]+$"))
        validInput();
      else if(newValue.matches("^$"))
        emptyInput();
      else
        invalidInput();
    });


    this.prevButton.setOnAction(e -> {
      if(!config.hasPath("example") && getResult() != null)
        store.addTrial(Util.ConfigToMap(config), getResult());
      triggerPrevButtonEvent();
    });

    this.nextButton.setOnAction(e -> {
      if(!config.hasPath("example"))
        store.addTrial(Util.ConfigToMap(config), getResult());
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
        Map<String,String> trial = store.getTrial(config.getString("id"));
        if(trial != null) {
          textField.setText(trial.get("result"));
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
    System.out.println("empty");
    nextButton.setDisable(true);
    validationLabel.setStyle("visibility: hidden;");
    textField.setStyle("-fx-font-size: 30.0;");
    textField.requestFocus();
  }


  private void invalidInput(){
    System.out.println("invalid");
    nextButton.setDisable(true);

    validationLabel.setStyle("-fx-graphic: url(\"/net/benhowell/view/icon/delete.png\");\n" +
        "    -fx-text-fill: red;\n" +
        "    visibility: visible;");

    textField.setStyle("-fx-font-size: 30.0;\n" +
        "-fx-background-color: red,\n" +
        "    linear-gradient(\n" +
        "    to bottom,\n" +
        "    derive(red,70%) 5%,\n" +
        "    derive(red,90%) 40%\n" +
        "    );");

    textField.requestFocus();
  }


  private void validInput(){
    System.out.println("valid");
    nextButton.setDisable(false);
    validationLabel.setStyle("visibility: hidden;");
    textField.setStyle("-fx-font-size: 30.0;");
    textField.requestFocus();
  }
}



