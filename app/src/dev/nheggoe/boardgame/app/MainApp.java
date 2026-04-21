package dev.nheggoe.boardgame.app;

import dev.nheggoe.boardgame.app.alert.AlertFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Predicate;

import dev.nheggoe.boardgame.app.alert.AlertType;
import javafx.application.Application;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/// The JavaFX application class. Invoked indirectly via [Launcher] so that the JVM entry-point
/// is not an [Application] subclass; see [Launcher] for the rationale.
public class MainApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    setup(primaryStage);
    try {
      new SceneSwitcher(primaryStage).switchTo(SceneSwitcher.SceneName.MAIN_VIEW);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void setup(Stage primaryStage) {
    primaryStage.setTitle("Board Game");
    primaryStage.setMinWidth(1200);
    primaryStage.setMinHeight(940);
    primaryStage.setOnCloseRequest(MainApp::onClose);
  }

  private static void onClose(WindowEvent event) {
    AlertFactory.createAlert(AlertType.CONFIRM, "Are you sure you want to exit the game?")
        .showAndWait()
        .map(ButtonType::getButtonData)
        .filter(Predicate.not(ButtonBar.ButtonData::isDefaultButton))
        .ifPresent(_ -> event.consume());
  }
}
