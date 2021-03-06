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


package net.benhowell.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.benhowell.controller.*;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ben Howell [ben@benhowell.net]
 */
public class Main extends Application implements PrevButtonEventListener, NextButtonEventListener {

  LinkedList<Card> cards;
  Node<Card> card;

  Stage stage = null;
  Config config;
  Display display = new Display();
  Store store = new Store();

  // init controllers
  HeadingWithTextController headingWithTextController = new HeadingWithTextController(this, display);
  ImgTextTextBoxController imgTextTextBoxController = new ImgTextTextBoxController(this, display, store);
  DetailController detailController = new DetailController(this, display, store);
  LanguageController languageController = new LanguageController(this, display, store);

  public static void main(String[] args) {
    launch(args);
  }


  public void init(){
    config = ConfigFactory.load();
    cards = new LinkedList<>();

    // load cards
    cards.addAll(load(config, headingWithTextController, "experiment.intro"));
    cards.addAll(load(config, imgTextTextBoxController, "experiment.examples"));
    cards.addAll(load(config, headingWithTextController, "experiment.interlude"));
    cards.addAll(loadRandomisedTrial(config, imgTextTextBoxController, "experiment.blocks"));
    cards.add(new Card(detailController));
    cards.add(new Card(languageController));
    cards.addAll(load(config, headingWithTextController, "experiment.outro"));

    // get first card node
    card = cards.getFirst();
  }


  public void start(Stage primaryStage) {
    card.element().load();
    card.element().getController().prevButton.setDisable(true);
    init(primaryStage, config.getString("experiment.title"));
    primaryStage.show();
  }


  @Override
  public void handlePrevButtonEvent(ButtonEvent event) {
    if(card.hasPrevious()){
      card = card.previous();
      card.element().update();
      if(!card.hasPrevious()){
        card.element().getController().prevButton.setDisable(true);
      }
    }
  }

  @Override
  public void handleNextButtonEvent(ButtonEvent event) {
    if (card.hasNext()) {
      card = card.next();
      card.element().update();
      card.element().getController().prevButton.setDisable(false);
    }
    else{
      endExercise();
    }
  }

  public void endExercise(){
    // create unique file name
    String fileName = Instant.now().toString().replace(":","") + ".xlsx";

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Survey");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.setInitialFileName(fileName);
    File file = fileChooser.showSaveDialog(stage);
    if(file != null) {
      store.generateXlsx(file);
      stop();
    }

  }

  public void stop(){
      stage.close();
    }


  public void init(Stage primaryStage, String title) {
    stage = primaryStage;
    Group root = new Group();
    root.getChildren().addAll(display);
    primaryStage.setTitle(title);
    primaryStage.centerOnScreen();
    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    //setMaximised(primaryStage)
  }


  public void setMaximised(Stage stage){
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());

  }

  public static ArrayList<Card> load(Config config, ScreenController sc, String item){
    ArrayList<Card> cards = new ArrayList<>();
    config.getConfigList(item)
        .stream()
        .forEach(c -> cards.add(new Card(c, sc)));
    return cards;
  }

  public ArrayList<Card> loadRandomisedTrial(Config config, ScreenController sc, String item){
    ArrayList<Card> cards = new ArrayList<>();
    config.getConfigList(item)
        .stream()
        .forEach(c -> {
          String category = c.getString("category");
          List<String> terms = Util.shuffle(c.getStringList("terms"));
          ArrayList<Map<String,String>> trials = new ArrayList<>();
          Util.shuffle(c.getConfigList("trials"))
              .stream()
              .forEach(t -> {
                Config trialConfig = (Config) t;
                HashMap<String, String> m = new HashMap<>();
                m.put("id", trialConfig.getString("id"));
                m.put("type", trialConfig.getString("type"));
                m.put("image", trialConfig.getString("image"));
                m.put("text", trialConfig.getString("text"));
                m.put("term", trialConfig.getString("term"));
                if (trialConfig.hasPath("example")) { // config item is an example
                  m.put("example", trialConfig.getString("example"));
                }
                trials.add(m);
              });

          for(int i=0;i<trials.size();i++){
            trials.get(i).put("term", terms.get(i));
            trials.get(i).put("category", category);
            cards.add(new Card(ConfigFactory.parseMap(trials.get(i)), sc));
          }
        });

    if(config.getBoolean("experiment.randomiseCategoryPresentation"))
      return Util.shuffle(cards);
    return cards;
  }
}



