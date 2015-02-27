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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import net.benhowell.core.Display;
import net.benhowell.core.Store;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net] on 23-Feb-2015.
 */

/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */
public class PictureWithTextAndTextBoxController extends ScreenController implements Initializable {

  private Store store;
  private Config config;
  private ImageView imageView = null;
  private Label label = null;
  private TextField textField = null;
  private BorderPane borderPane = null;
  private Label validationLabel = null;
  @FXML private HBox hBox;

  public PictureWithTextAndTextBoxController(ControllerLoader loader, String resource, Display display, Store store){
    super(loader, resource, display);

    this.store = store;

    borderPane = new BorderPane();
    borderPane.prefHeight(200.0);
    borderPane.prefWidth(200.0);
    GridPane.setHalignment(borderPane, HPos.CENTER);
    GridPane.setValignment(borderPane, VPos.CENTER);
    GridPane.setRowIndex(borderPane, 0);

    gridPane.getChildren().add(borderPane);

    imageView = new ImageView();
    imageView.setFitHeight(300.0);
    imageView.setFitWidth(400.0);
    imageView.setPickOnBounds(true);
    imageView.setPreserveRatio(true);
    BorderPane.setAlignment(imageView, Pos.CENTER);
    borderPane.setCenter(imageView);

    label = new Label();
    label.setPrefWidth(800.0);
    label.setPrefHeight(-1.0);
    label.setMaxHeight(-1.0);
    label.setMaxWidth(-1.0);
    label.setMinHeight(-1.0);
    label.setMinWidth(-1.0);
    label.setScaleX(1.0);
    label.setScaleY(1.0);
    label.setAlignment(Pos.TOP_CENTER);
    label.setFont(new Font(30.0));
    label.setTextAlignment(TextAlignment.CENTER);
    label.setWrapText(true);
    label.setStyle("-fx-padding: 0 70 0 70;");
    GridPane.setRowIndex(label, 2);
    gridPane.getChildren().add(label);

    textField = new TextField();
    textField.setPrefWidth(640.0);
    textField.setMaxWidth(Double.NEGATIVE_INFINITY);
    textField.setMinHeight(-1.0);
    textField.setMinWidth(Double.NEGATIVE_INFINITY);

    textField.setAlignment(Pos.CENTER_LEFT);
    textField.setStyle("-fx-font-size: 30.0;");
    GridPane.setHalignment(textField, HPos.CENTER);
    GridPane.setRowIndex(textField, 3);
    gridPane.getChildren().add(textField);

    validationLabel = new Label();
    validationLabel.setPrefHeight(30.0);
    validationLabel.setPrefWidth(568.0000999999975);
    validationLabel.setText("Error: Please enter a name containing alphabetical characters only!");
    validationLabel.setVisible(false);
    hBox.getChildren().add(0,validationLabel);

    nextButton.setDisable(true);

    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue.matches("^[a-zA-Z\\s]+$"))
        validInput();
      else if(newValue.matches("^$"))
        emptyInput();
      else
        invalidInput();
    });

    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setHgrow(Priority.NEVER);
    columnConstraints.setMaxWidth(800.0);
    columnConstraints.setMinWidth(800.0);
    columnConstraints.setPrefWidth(800.0);
    gridPane.getColumnConstraints().addAll(columnConstraints);

    RowConstraints r1 = new RowConstraints();
    RowConstraints r2 = new RowConstraints();
    RowConstraints r3 = new RowConstraints();
    RowConstraints r4 = new RowConstraints();
    RowConstraints r5 = new RowConstraints();
    r1.setPrefHeight(350.0);
    r1.setVgrow(Priority.NEVER);

    r2.setMinHeight(10.0);
    r2.setPrefHeight(30.0);
    r2.setValignment(VPos.TOP);
    r2.setVgrow(Priority.NEVER);

    r3.setMinHeight(140.0);
    r3.setPrefHeight(140.0);
    r3.setValignment(VPos.TOP);
    r3.setVgrow(Priority.NEVER);

    r4.setMinHeight(10.0);
    r4.setPrefHeight(60.0);
    r4.setValignment(VPos.CENTER);
    r4.setVgrow(Priority.ALWAYS);

    r5.setMinHeight(20.0);
    r5.setPrefHeight(35.0);
    r5.setVgrow(Priority.NEVER);

    gridPane.getRowConstraints().addAll(r1, r2, r3, r4, r5);


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
        if(trial != null)
          textField.setText(trial.getString("result"));
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

