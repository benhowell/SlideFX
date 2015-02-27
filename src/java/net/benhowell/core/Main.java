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
import java.io.FileWriter;
import java.time.Instant;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */
public class Main extends Application implements PrevButtonEventListener, NextButtonEventListener {

  Stage stage = null;
  Config config;
  Display display = new Display();
  ControllerLoader loader = new ControllerLoader();
  Store store = new Store();

  // init controllers
  HeadingWithTextController headingWithTextController = new HeadingWithTextController(loader, "Screen.fxml", display);
  PictureWithTextAndTextBoxController pictureWithTextAndTextBoxController = new PictureWithTextAndTextBoxController(loader, "Screen.fxml", display, store);
  /*
  DetailController detailController = new DetailController(loader, "PersonalDetailGridPane.fxml", display);
  LanguageController languageController = new LanguageController(loader, "LanguageGridPane.fxml", display);

  HeadingWithTextController outroController = new HeadingWithTextController(loader, "Screen.fxml", display);
*/


  DoublyLinkedList<Card> cards;
  DLLNode<Card> card;



  public static void main(String[] args) {
    launch(args);
  }


  public static ArrayList<Card> load(Config config, ScreenController sc, String item){
    ArrayList<Card> cards = new ArrayList<>();
    config.getConfigList(item)
      .stream()
      .forEach(c -> cards.add(new Card(c, sc)));
    return cards;
  }

  public ArrayList<Card> loadRandomised(Config config, ScreenController sc, String item){
    ArrayList<Card> cards = new ArrayList<>();
    config.getConfigList(item)
      .stream()
      .forEach(c -> {
        String category = c.getString("category");
        List<String> names = Util.shuffle(config.getStringList("names"));
        ArrayList<Map<String,String>> trials = new ArrayList<>();
        Util.shuffle(config.getConfigList("trials"))
          .stream()
          .forEach(t -> {
            Config trialConfig = (Config) t;
            HashMap<String, String> m = new HashMap<>();
            m.put("id", trialConfig.getString("id"));
            m.put("type", trialConfig.getString("type"));
            m.put("image", trialConfig.getString("image"));
            m.put("text", trialConfig.getString("text"));
            m.put("name", trialConfig.getString("name"));
            if (trialConfig.hasPath("example")) { // config item is an example
              m.put("example", trialConfig.getString("example"));
            }
            trials.add(m);
          });

        for(int i=0;i<trials.size();i++){
          trials.get(i).put("name", names.get(i));
          trials.get(i).put("category", category);
          cards.add(new Card(ConfigFactory.parseMap(trials.get(i)), sc));
        }
      });

    if(config.getBoolean("experiment.randomiseCategoryPresentation"))
      return Util.shuffle(cards);
    return cards;
  }

  @Override
  public void handlePrevButtonEvent(ButtonEvent event) {
    if (cards.hasPrevious(card)) {
      card = cards.getPrevious(card);
      card.getElement().update();
    }
  }

  @Override
  public void handleNextButtonEvent(ButtonEvent event) {
    if (cards.hasNext(card)) {
      card = cards.getNext(card);
      card.getElement().update();
    }
  }










  public void init(){
    config = ConfigFactory.load();
    cards = new DoublyLinkedList<>();


    headingWithTextController.addEventListener((PrevButtonEventListener)this);
    headingWithTextController.addEventListener((NextButtonEventListener)this);
    pictureWithTextAndTextBoxController.addEventListener((PrevButtonEventListener)this);
    pictureWithTextAndTextBoxController.addEventListener((NextButtonEventListener)this);

    // load cards
    cards.addAll(load(config, headingWithTextController, "experiment.intro"));
    cards.addAll(load(config, pictureWithTextAndTextBoxController, "experiment.examples"));
    cards.addAll(load(config, headingWithTextController, "experiment.interlude"));
    cards.addAll(loadRandomised(config, pictureWithTextAndTextBoxController, "experiment.blocks"));
    // cards.add(details)
    // cards.add(languages)
    //cards.addAll(load(config, ?, "experiment.outro"));

    // get first card node
    card = cards.getFirst();






    /*ArrayList<HashMap<String, String>> trials = Trial.createRandomTrialRun(config, "experiment.blocks");
    System.out.println("trials: " + trials);

    outro = Intro.load(config, "experiment.outro");
    System.out.println("outro: " + outro);



    headingWithTextController.prevButton.setDisable(true);
    headingWithTextController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        if(currentItem == 0)
          headingWithTextController.prevButton.setDisable(true);
        headingWithTextController.update(intro.get(currentItem));
      }
      else {
        System.out.println("no prev!");
      }
    });

    headingWithTextController.nextButton.setOnAction(e -> {
      headingWithTextController.prevButton.setDisable(false);
      if (currentItem < intro.size() - 1) {
        currentItem++;
        headingWithTextController.update(intro.get(currentItem));
      }
      else {
        currentItem = 0;
        pictureWithTextAndTextBoxController.update(examples.get(currentItem));
      }
    });


    pictureWithTextAndTextBoxController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        pictureWithTextAndTextBoxController.update(examples.get(currentItem));
      }
      else {
        currentItem = intro.size() -1;
        //headingWithTextController.update(intro.get(currentItem));
        headingWithTextController.update(intro.get(currentItem));
      }
    });

    pictureWithTextAndTextBoxController.nextButton.setOnAction(e -> {
      if (currentItem < examples.size() - 1) {
        currentItem++;
        pictureWithTextAndTextBoxController.update(examples.get(currentItem));
      }
      else {
        currentItem = 0;
        interludeController.update(interlude.get(currentItem));
      }
    });

    interludeController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        interludeController.update(interlude.get(currentItem));
      }
      else {
        currentItem = examples.size() -1;
        pictureWithTextAndTextBoxController.update(examples.get(currentItem));
      }
    });

    interludeController.nextButton.setOnAction(e -> {
      if (currentItem < interlude.size() - 1) {
        currentItem++;
        interludeController.update(interlude.get(currentItem));
      }
      else {
        currentItem = 0;
        //trialController.update(trials.get(currentItem));
        trialController.update(trials.get(currentItem));
      }
    });

    trialController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        trialController.update(trials.get(currentItem));
      } else {
        currentItem = interlude.size() -1;
        interludeController.update(interlude.get(currentItem));
      }
    });

    trialController.nextButton.setOnAction(e -> {
      if (currentItem < trials.size() - 1) {
        store.addTrial(trials.get(currentItem), trialController.getResult());
        currentItem++;
        trialController.update(trials.get(currentItem));
      } else {
        currentItem = 0;
        detailController.load();
      }
    });


    detailController.prevButton.setOnAction(e -> {
      currentItem = trials.size() - 1;
      //trialController.update(trials.get(currentItem));
      trialController.update(trials.get(currentItem));
    });
    detailController.nextButton.setOnAction(e -> {
      store.addDetail(detailController.getResult());
      languageController.load();
    });

    languageController.prevButton.setOnAction(e -> detailController.load());
    languageController.nextButton.setOnAction(e -> {
      currentItem = 0;
      store.addLanguage(languageController.getResult());
      outroController.update(outro.get(currentItem));
    });

    outroController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        outroController.update(outro.get(currentItem));
      }
      else {
        currentItem = 0;
        languageController.load();
      }
    });

    outroController.nextButton.setOnAction(e -> {
      if (currentItem < outro.size() - 1) {
        currentItem++;
        outroController.update(outro.get(currentItem));
      }
      else {
        currentItem = 0;
        endExercise();
      }
    });*/

  }


  public void start(Stage primaryStage) {
    card.getElement().load();
    init(primaryStage, config.getString("experiment.title"));
    primaryStage.show();
  }

  public void endExercise(){
    System.out.println("Create spreadsheet");
    System.out.println("prompt user with save dialog?");

    Instant now = Instant.now();

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Survey");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.setInitialFileName(now.toString() + ".csv");
    File file = fileChooser.showSaveDialog(stage);
    if(file != null) {
      writeFile(store.generateCSV(), file);
    }
    stop();
  }

  private void writeFile(String content, File file){
    try {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(content);
      fileWriter.flush();
      fileWriter.close();
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
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
      primaryStage.setScene(new Scene(root, 800, 600));
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
  }



