package dev.nheggoe.boardgame.app.controller;

import dev.nheggoe.boardgame.app.alert.AlertFactory;
import dev.nheggoe.boardgame.app.alert.AlertType;
import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import dev.nheggoe.boardgame.app.view.MainView;
import dev.nheggoe.boardgame.core.io.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/// The MainController class is responsible for initializing and managing the main functionality of
/// the application, including the preparation of default data files, handling the game scene
/// transitions, and offering file repair functionality. It extends the base `Controller` class
/// to integrate with the application’s scene management.
public class MainController extends Controller {

  private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

  private Path playersCsv;
  private Path snakeAndLadderCsv;
  private Path monopolyCsv;

  /// Constructs a MainController instance, a specific implementation of [Controller],
  /// responsible for managing the main view of the application and initializing necessary data
  /// files.
  ///
  /// @param sceneSwitcher the [SceneSwitcher] responsible for managing scene transitions; must
  ///     not be null
  public MainController(SceneSwitcher sceneSwitcher) {
    super(sceneSwitcher, createMainView(sceneSwitcher));
    initializeDataFiles();
  }

  /// Initializes data files, creating them from defaults if necessary.
  private void initializeDataFiles() {
    try {
      // Initialize players CSV
      this.playersCsv = FileUtil.generateFilePath("players", "csv");
      FileUtil.ensureFileWithDefault(playersCsv, "/csv/players_default.csv");

      // Initialize Snake and Ladder CSV
      this.snakeAndLadderCsv = FileUtil.generateFilePath("snakeAndLadder", "csv");
      FileUtil.ensureFileWithDefault(snakeAndLadderCsv, "/csv/snake_and_ladder_default.csv");

      // Initialize Monopoly CSV
      this.monopolyCsv = FileUtil.generateFilePath("monopoly", "csv");
      FileUtil.ensureFileWithDefault(monopolyCsv, "/csv/monopoly_default.csv");

      LOGGER.info("All data files initialized successfully");

    } catch (IOException e) {
      LOGGER.severe("Failed to initialize data files: " + e.getMessage());
      showFileInitializationError(e);
    }
  }

  /// Shows an error dialog when file initialization fails.
  private void showFileInitializationError(IOException e) {
    AlertFactory.createAlert(
            AlertType.ERROR,
            """
            Failed to initialize game data files: %s
            Please check your file permissions and try again."""
                .formatted(e.getMessage()))
        .showAndWait();
  }

  /// Attempts to repair corrupted data files by copying from defaults.
  public void repairDataFiles() {
    try {
      // Check and repair each file
      if (!FileUtil.isFileValid(playersCsv)) {
        FileUtil.copyFromResource("/csv/players_default.csv", playersCsv);
        LOGGER.info("Repaired players.csv from default");
      }

      if (!FileUtil.isFileValid(snakeAndLadderCsv)) {
        FileUtil.copyFromResource("/csv/snake_and_ladder_default.csv", snakeAndLadderCsv);
        LOGGER.info("Repaired snakeAndLadder.csv from default");
      }

      if (!FileUtil.isFileValid(monopolyCsv)) {
        FileUtil.copyFromResource("/csv/monopoly_default.csv", monopolyCsv);
        LOGGER.info("Repaired monopoly.csv from default");
      }

      AlertFactory.createAlert(AlertType.INFO, "Data files have been repaired successfully.")
          .showAndWait();

    } catch (IOException e) {
      LOGGER.severe("Failed to repair data files: %s".formatted(e.getMessage()));
      AlertFactory.createAlert(
              AlertType.ERROR, "Failed to repair data files: %s".formatted(e.getMessage()))
          .showAndWait();
    }
  }

  private static MainView createMainView(SceneSwitcher sceneSwitcher) {
    return new MainView(
        sceneSwitcher, startSnakeGame(sceneSwitcher), startMonopolyGame(sceneSwitcher));
  }

  private static EventHandler<ActionEvent> startSnakeGame(SceneSwitcher sceneSwitcher) {
    return _ -> {
      try {
        sceneSwitcher.switchTo(SceneSwitcher.SceneName.SNAKE_GAME_VIEW);
      } catch (IOException e) {
        LOGGER.severe("Failed to switch to Snake game view: " + e.getMessage());
        AlertFactory.createAlert(
                AlertType.ERROR, "Failed to start Snake & Ladder game: " + e.getMessage())
            .showAndWait();
      }
    };
  }

  private static EventHandler<ActionEvent> startMonopolyGame(SceneSwitcher sceneSwitcher) {
    return _ -> {
      try {
        sceneSwitcher.switchTo(SceneSwitcher.SceneName.MONOPOLY_GAME_VIEW);
      } catch (IOException e) {
        LOGGER.severe("Failed to switch to Monopoly game view: " + e.getMessage());
        AlertFactory.createAlert(
                AlertType.ERROR, "Failed to start Monopoly game: " + e.getMessage())
            .showAndWait();
      }
    };
  }

  // Getters for file paths
  public Path getPlayersCsv() {
    return playersCsv;
  }

  public Path getSnakeAndLadderCsv() {
    return snakeAndLadderCsv;
  }

  public Path getMonopolyCsv() {
    return monopolyCsv;
  }
}
