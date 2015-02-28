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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import net.benhowell.core.Display;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net] on 22-Feb-2015.
 */
public class HeadingWithTextController extends ScreenController implements Initializable {

  private TextArea bodyTextArea;
  private Label headingLabel;


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
    r1.setMaxHeight(339.0);
    r1.setMinHeight(29.0);
    r1.setPrefHeight(118.0);
    r1.setVgrow(Priority.NEVER);

    r2.setMaxHeight(414.0);
    r2.setMinHeight(10.0);
    r2.setPrefHeight(332.0);
    r2.setValignment(VPos.TOP);
    r2.setVgrow(Priority.NEVER);

    r3.setMaxHeight(134.0);
    r3.setMinHeight(55.0);
    r3.setPrefHeight(55.0);
    r3.setValignment(VPos.TOP);
    r3.setVgrow(Priority.NEVER);

    r4.setMaxHeight(66.0);
    r4.setMinHeight(10.0);
    r4.setPrefHeight(62.0);
    r4.setValignment(VPos.CENTER);
    r4.setVgrow(Priority.ALWAYS);

    r5.setMaxHeight(37.0);
    r5.setMinHeight(29.0);
    r5.setPrefHeight(33.0);
    r5.setVgrow(Priority.NEVER);

    gridPane.getRowConstraints().addAll(r1, r2, r3, r4, r5);

    this.prevButton.setOnAction(e -> triggerPrevButtonEvent());
    this.nextButton.setOnAction(e -> triggerNextButtonEvent());
  }


  public void load(Config items) {
    System.out.println(headingLabel.getText());
    headingLabel.setText(items.getString("label"));
    bodyTextArea.setText(items.getString("text"));
    super.load();
  }


  public void update(Config items) {
    super.update(() -> {
      headingLabel.setText(items.getString("label"));
      bodyTextArea.setText(items.getString("text"));
    });
    super.update(() -> nextButton.requestFocus());
  }


  public void initialize(URL url, ResourceBundle rb) {
    System.out.println(this.getClass().getSimpleName() + ".initialise");
  }
}
