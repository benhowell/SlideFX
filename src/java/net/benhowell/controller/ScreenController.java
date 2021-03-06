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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import net.benhowell.core.ButtonEvent;
import net.benhowell.core.Display;
import net.benhowell.core.NextButtonEventListener;
import net.benhowell.core.PrevButtonEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ben Howell [ben@benhowell.net]
 */
public class ScreenController implements Initializable {

  @FXML protected GridPane gridPane;
  @FXML public Button nextButton;
  @FXML public Button prevButton;

  private Display display;
  private Node node;
  private Node child;
  private final List<PrevButtonEventListener> prevButtonEventListeners;
  private final List<NextButtonEventListener> nextButtonEventListeners;

  ControllerLoader loader = new ControllerLoader();

  public ScreenController(Object parent, String resource, Display display) {
    this.display = display;

    this.prevButtonEventListeners = new CopyOnWriteArrayList<>();
    this.nextButtonEventListeners = new CopyOnWriteArrayList<>();

    this.addEventListener((PrevButtonEventListener)parent);
    this.addEventListener((NextButtonEventListener)parent);

    // load screen shell...
    this.node = loadView("Screen.fxml");

    // load child and insert into screen shell
    this.child = loadView(resource);
    GridPane.setRowIndex(child, 0);
    gridPane.getChildren().add(0, child);

    this.prevButton.defaultButtonProperty().bind(prevButton.focusedProperty());
    this.nextButton.defaultButtonProperty().bind(nextButton.focusedProperty());

  }

  public void load(Config c){}
  public void update(Config c){}

  public void addEventListener(PrevButtonEventListener listener) {
    prevButtonEventListeners.add(listener);
  }


  public void addEventListener(NextButtonEventListener listener) {
    nextButtonEventListeners.add(listener);
  }


  public void removeEventListener(PrevButtonEventListener listener) {
    prevButtonEventListeners.remove(listener);
  }


  public void removeEventListener(NextButtonEventListener listener) {
    nextButtonEventListeners.remove(listener);
  }


  protected void load(){
    display.loadScreen(node);
  }


  protected void update(final Runnable f) {
    Platform.runLater(f);
    Platform.runLater(() -> load());
  }

  protected void triggerPrevButtonEvent() {
    ButtonEvent event = new ButtonEvent(this);
    prevButtonEventListeners
      .stream()
      .forEach(l -> l.handlePrevButtonEvent(event));
  }


  protected void triggerNextButtonEvent() {
    ButtonEvent event = new ButtonEvent(this);
    nextButtonEventListeners
      .stream()
      .forEach(l -> l.handleNextButtonEvent(event));
  }


  private Node loadView(String view){
    Node n = null;
    try {
      n = loader.controllerLoader(this, view);
    }
    catch (IOException e) {
      System.out.println("Controller loader failed to load view: " + view + " | " + e);
    }
    return n;
  }

  public void initialize(URL url, ResourceBundle rb) {
  }
}
