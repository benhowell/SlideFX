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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import net.benhowell.core.Display;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net] on 22-Feb-2015.
 */
public class HeadingWithTextController extends ScreenController implements Initializable {

  @FXML private TextArea bodyTextArea;
  @FXML private Label headingLabel;


  public HeadingWithTextController(ControllerLoader loader, String resource, Display display) {
    super(loader, resource, display);

    headingLabel = new Label();
    headingLabel.setPrefWidth(800.0);
    headingLabel.setPrefHeight(-1.0);
    headingLabel.setMaxHeight(-1.0);
    headingLabel.setMaxWidth(-1.0);
    headingLabel.setMinHeight(-1.0);
    headingLabel.setMinWidth(-1.0);
    headingLabel.setScaleX(1.0);
    headingLabel.setScaleY(1.0);
    headingLabel.setAlignment(Pos.TOP_CENTER);
    headingLabel.setFont(new Font(38.0));
    headingLabel.setTextAlignment(TextAlignment.CENTER);
    headingLabel.setWrapText(true);
    headingLabel.setStyle("-fx-padding: 0 70 0 70;");
    gridPane.getChildren().add(headingLabel);

    bodyTextArea = new TextArea();
    bodyTextArea.setPrefHeight(400.00);
    bodyTextArea.setPrefWidth(800.00);
    bodyTextArea.setEditable(false);
    bodyTextArea.setFocusTraversable(false);
    bodyTextArea.setWrapText(true);
    bodyTextArea.getStyleClass().add("paragraph");

    TextFlow textFlow = new TextFlow(bodyTextArea);
    textFlow.getStyleClass().add("paragraph");
    GridPane.setRowIndex(textFlow, 1);
    gridPane.getChildren().add(textFlow);
  }


  public void load(Map<String, String> intro) {
    System.out.println(headingLabel.getText());
    headingLabel.setText(intro.get("label"));
    bodyTextArea.setText(intro.get("text"));
    super.load();
  }


  public void update(Map<String, String> intro) {
    super.update(() -> {
      headingLabel.setText(intro.get("label"));
      bodyTextArea.setText(intro.get("text"));
    });
    super.update(() -> nextButton.requestFocus());
  }


  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");
  }
}



