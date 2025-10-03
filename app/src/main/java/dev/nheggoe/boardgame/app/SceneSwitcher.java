package dev.nheggoe.boardgame.app;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.app.controller.PlayerSetupController;
import dev.nheggoe.boardgame.app.controller.MainController;
import dev.nheggoe.boardgame.app.controller.MonopolyGameController;
import dev.nheggoe.boardgame.app.controller.SnakeGameController;
import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.ui.View;
import dev.nheggoe.boardgame.app.util.GameFactory;
import dev.nheggoe.boardgame.app.util.PlayerManager;
import dev.nheggoe.boardgame.core.GameEngine;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.io.FileUtil;
import dev.nheggoe.boardgame.core.io.csv.CSVHandler;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The SceneSwitcher class manages scene transitions for a JavaFX application. It serves as an
 * intermediary for switching between different application's views, ensuring proper initialization
 * and cleanup of controllers and their associated views.
 *
 * <p>The class is designed to handle various application views (scenes) identified by its nested
 * {@code SceneName} enum. Each view has an associated controller created and managed by this class.
 */
public class SceneSwitcher {

  private static final Logger LOGGER = Logger.getLogger(SceneSwitcher.class.getName());
  private static final Path BOARD_GAME_ICON =
      Path.of("src/main/resources/icons/board-game-icon.png");
  private static final Path MONOPOLY_ICON = Path.of("src/main/resources/icons/monopoly-icon.png");
  private static final Path SNAKE_ICON = Path.of("src/main/resources/icons/snake-icon.png");

  private final EventBus eventBus = new EventBus();
  private final Scene scene;
  private Controller controller;

  /**
   * Initializes a new instance of the {@code SceneSwitcher} class, setting up the primary stage
   * with a default scene and displaying it in the center of the screen.
   *
   * @param primaryStage the {@link Stage} to be used as the primary stage for the application; must
   *     not be {@code null}
   * @throws NullPointerException if {@code primaryStage} is {@code null}
   */
  public SceneSwitcher(Stage primaryStage) {
    requireNonNull(primaryStage, "primaryStage must not be null");
    this.scene = new Scene(new Pane(), primaryStage.getWidth(), primaryStage.getHeight());
    primaryStage.getIcons().add(new Image(BOARD_GAME_ICON.toUri().toString()));
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

  /**
   * Enum representing the names of different scenes within the application.
   *
   * <p>This enumeration serves as an identifier for the various views that the application can
   * transition to, such as the main view, player setup view, or specific game views. These values
   * are used by the {@code SceneSwitcher} class to manage scene transitions and control the
   * associated controllers and views.
   */
  public enum SceneName {
    MAIN_VIEW,
    PLAYER_SETUP_VIEW,
    MONOPOLY_GAME_VIEW,
    SNAKE_GAME_VIEW,
  }

  /**
   * Transitions to a new scene specified by the given {@link SceneName}.
   *
   * <p>The method closes the current controller's associated {@link View}, if one exists, and logs
   * any errors encountered during this process. A new controller is created for the specified
   * scene, and the root of the application's {@link Scene} is updated with the {@link View} of the
   * newly created controller.
   *
   * @param name the {@link SceneName} of the scene to switch to
   * @throws IOException if an error occurs while creating the new controller or loading the scene
   */
  public void switchTo(SceneName name) throws IOException {
    if (controller != null) {
      try {
        controller.getView().close();
      } catch (Exception e) {
        LOGGER.severe(e::getMessage);
        LOGGER.severe(() -> "Failed to close controller: " + controller.getClass().getName());
      }
    }
    this.controller = createController(name);
    setRoot(controller.getView());
    setTaskBarIcon(name);
  }

  /**
   * Resets the current scene by creating a new instance.
   *
   * @throws IOException if an error occurs during the transition or loading of the new scene
   * @throws IllegalStateException if the controller type is not recognized
   */
  public void reset() throws IOException {
    switch (controller) {
      case SnakeGameController s -> switchTo(SceneName.SNAKE_GAME_VIEW);
      case MonopolyGameController m -> switchTo(SceneName.MONOPOLY_GAME_VIEW);
      default -> throw new IllegalStateException("Unexpected value: " + controller);
    }
  }

  private Controller createController(SceneName name) throws IOException {
    return switch (name) {
      case MAIN_VIEW -> createMainController();
      case SNAKE_GAME_VIEW -> createSnakeGameController();
      case PLAYER_SETUP_VIEW -> createPlayerSetupController();
      case MONOPOLY_GAME_VIEW -> createMonopolyGameController();
    };
  }

  private void setTaskBarIcon(SceneName name) {
    switch (name) {
      case MONOPOLY_GAME_VIEW -> setTaskBarIcon(MONOPOLY_ICON);
      case SNAKE_GAME_VIEW -> setTaskBarIcon(SNAKE_ICON);
      default -> setTaskBarIcon(BOARD_GAME_ICON);
    }
  }

  private PlayerSetupController createPlayerSetupController() {
    return new PlayerSetupController(this);
  }

  private MainController createMainController() {
    return new MainController(this);
  }

  private SnakeGameController createSnakeGameController() throws IOException {
    var csvFile = FileUtil.generateFilePath("players", "csv");
    var playerManager = new PlayerManager(eventBus, new CSVHandler(csvFile));
    var game = GameFactory.createSnakeGame(eventBus, playerManager);
    return new SnakeGameController(this, eventBus, new GameEngine<>(game));
  }

  private MonopolyGameController createMonopolyGameController() throws IOException {
    var csvFile = FileUtil.generateFilePath("players", "csv");
    var playerManager = new PlayerManager(eventBus, new CSVHandler(csvFile));
    var game = GameFactory.createMonopolyGame(eventBus, playerManager);
    return new MonopolyGameController(this, eventBus, new GameEngine<>(game));
  }

  /**
   * Retrieves the current {@link Scene} instance managed by this object.
   *
   * @return the {@link Scene} instance currently associated with this object
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Updates the root node of the scene with the specified {@link Parent} object.
   *
   * <p>This method sets the provided {@code root} as the root node of the associated {@link Scene}.
   * The {@code root} cannot be {@code null}; attempting to pass a {@code null} reference will
   * result in a {@link NullPointerException} being thrown. The root node serves as the primary
   * container for all the elements displayed within the scene.
   *
   * @param root the {@link Parent} to set as the root node of the scene, must not be {@code null}
   * @throws NullPointerException if {@code root} is {@code null}
   */
  public void setRoot(Parent root) {
    scene.setRoot(requireNonNull(root, "root must not be null"));
  }

  private static void setTaskBarIcon(Path iconPath) {
    if (Taskbar.isTaskbarSupported()) {
      var taskbar = Taskbar.getTaskbar();
      if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        var dockIcon = defaultToolkit.getImage(iconPath.toString());
        taskbar.setIconImage(dockIcon);
      }
    }
  }
}
