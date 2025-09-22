package dev.nheggoe.boardgame.app;

import dev.nheggoe.boardgame.app.ui.AlertFactory;
import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
    primaryStage.setOnCloseRequest(
        closeEvent -> {
          if (!isExitConfirmed()) {
            closeEvent.consume();
          }
        });
  }

  private static boolean isExitConfirmed() {
    var result =
        AlertFactory.createAlert(
                Alert.AlertType.CONFIRMATION, "Are you sure you want to exit the game?")
            .showAndWait();
    return result.isPresent() && result.get().getButtonData().isDefaultButton();
  }
}
