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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import net.benhowell.core.Display;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */
public class ExampleController implements Initializable {
  @FXML
  private GridPane trialGridPane  = null;
  @FXML public Button nextButton = null;
  @FXML public Button prevButton = null;
  @FXML private ImageView imageView = null;
  @FXML private Label label = null;
  @FXML private TextField textField = null;
  @FXML private BorderPane borderPane = null;
  @FXML private Label validationLabel = null;

  private Display display;
  private Node node;
  private String example;

  public ExampleController(ControllerLoader loader, String resource, Display display){
    this.display = display;
    try {
      node = loader.controllerLoader(this, resource);
    }
    catch (IOException e) {
      System.out.println("Controller loader failed to load view: " + e);
    }
  }


  public void update(Map<String, String> trial) {
    System.out.println("example: " + trial);
    String txt = trial.get("text").replace("${name}", trial.get("name"));
    Image img = new Image(trial.get("imagePath") + trial.get("imageName"));
    example = trial.get("example");

    display.fxRun(() -> {
      textField.clear();
      label.setText(txt);
      imageView.setImage(img);
      display.loadScreen(node);
      textField.setText(example);
      textField.setEditable(false);
      nextButton.requestFocus();
    });

  }


  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");
    borderPane.setCenter(imageView);
    GridPane.setHalignment(borderPane, HPos.CENTER);
    GridPane.setValignment(borderPane, VPos.CENTER);
  }
}
