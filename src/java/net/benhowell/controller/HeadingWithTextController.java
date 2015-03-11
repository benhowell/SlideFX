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
import javafx.scene.control.TextArea;
import net.benhowell.core.Display;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ben Howell [ben@benhowell.net]
 */
public class HeadingWithTextController extends ScreenController {

  @FXML private TextArea bodyTextArea;
  @FXML private Label headingLabel;

  public HeadingWithTextController(Object parent, Display display) {
    super(parent, "HeadingWithText.fxml", display);

    this.prevButton.setOnAction(e -> triggerPrevButtonEvent());
    this.nextButton.setOnAction(e -> triggerNextButtonEvent());

  }


  public void load(Config items) {
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
