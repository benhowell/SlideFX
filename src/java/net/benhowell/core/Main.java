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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Howell [ben@benhowell.net] on 19-Aug-2014.
 */
public class Main extends Application{

  Stage stage = null;
  Config config;

  int currentItem = 0;

  Display display = new Display();
  ControllerLoader loader = new ControllerLoader();
  Store store = new Store();

  // init controllers
  IntroController introController = new IntroController(loader, "IntroGridPane.fxml", display);
  ExampleController exampleController = new ExampleController(loader, "TrialGridPane.fxml", display);
  IntroController interludeController = new IntroController(loader, "IntroGridPane.fxml", display);
  TrialController trialController = new TrialController(loader, "TrialGridPane.fxml", display);
  DetailController detailController = new DetailController(loader, "PersonalDetailGridPane.fxml", display);
  LanguageController languageController = new LanguageController(loader, "LanguageGridPane.fxml", display);
  IntroController outroController = new IntroController(loader, "IntroGridPane.fxml", display);

  ArrayList<Map<String, String>> intro;
  ArrayList<Map<String, String>> interlude;
  ArrayList<Map<String, String>> outro;


  public static void main(String[] args) {
    launch(args);
  }



  public void init(){
    config = Configuration.loadConfig();

    // init data sets
    intro = Intro.load(config, "experiment.intro");
    System.out.println("intro: " + intro);

    ArrayList<Map<String, String>> examples = Trial.loadExampleRun(config, "experiment.examples");
    System.out.println("examples: " + examples);

    interlude = Intro.load(config, "experiment.interlude");
    System.out.println("interlude: " + interlude);

    ArrayList<HashMap<String, String>> trials = Trial.createRandomTrialRun(config, "experiment.blocks");
    System.out.println("trials: " + trials);

    outro = Intro.load(config, "experiment.outro");
    System.out.println("outro: " + outro);

    introController.prevButton.setDisable(true);
    introController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        if(currentItem == 0)
          introController.prevButton.setDisable(true);
        introController.update(intro.get(currentItem));
      }
      else {
        System.out.println("no prev!");
      }
    });

    introController.nextButton.setOnAction(e -> {
      introController.prevButton.setDisable(false);
      if (currentItem < intro.size() - 1) {
        currentItem++;
        introController.update(intro.get(currentItem));
      }
      else {
        currentItem = 0;
        exampleController.update(examples.get(currentItem));
      }
    });


    exampleController.prevButton.setOnAction(e -> {
      if (currentItem > 0) {
        currentItem--;
        exampleController.update(examples.get(currentItem));
      }
      else {
        currentItem = intro.size() -1;
        introController.update(intro.get(currentItem));
      }
    });

    exampleController.nextButton.setOnAction(e -> {
      if (currentItem < examples.size() - 1) {
        currentItem++;
        exampleController.update(examples.get(currentItem));
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
        exampleController.update(examples.get(currentItem));
      }
    });

    interludeController.nextButton.setOnAction(e -> {
      if (currentItem < interlude.size() - 1) {
        currentItem++;
        interludeController.update(interlude.get(currentItem));
      }
      else {
        currentItem = 0;
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
    });

  }


  public void start(Stage primaryStage) {
    introController.load(intro.get(currentItem));
    init(primaryStage, Configuration.getConfigString(config, "experiment.title"));
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



