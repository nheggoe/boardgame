package dev.nheggoe.boardgame.app;

import dev.nheggoe.boardgame.app.util.AlertFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Predicate;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The {@link Launcher} class is the entry point of the program.
 *
 * @author Nick Heggø
 * @version 2025.04.15
 */
public class Launcher extends Application {
  /** The main method creates a new instance of the Game class and runs it. */
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
    primaryStage.setOnCloseRequest(Launcher::onClose);
  }

  private static void onClose(WindowEvent event) {
    AlertFactory.createAlert(
            Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the game?")
        .showAndWait()
        .map(ButtonType::getButtonData)
        .filter(Predicate.not(ButtonBar.ButtonData::isDefaultButton))
        .ifPresent(_ -> event.consume());
  }
}
