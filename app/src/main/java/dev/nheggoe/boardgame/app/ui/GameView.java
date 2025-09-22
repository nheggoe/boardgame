package dev.nheggoe.boardgame.app.ui;

import dev.nheggoe.boardgame.app.component.MessagePanel;
import dev.nheggoe.boardgame.app.component.PlayerDashboard;
import dev.nheggoe.boardgame.app.component.RollDiceButton;
import dev.nheggoe.boardgame.app.component.SettingButton;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.model.Tile;
import java.util.List;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Represents an abstract base class for a game view in the board-game.
 *
 * <p>This class defines the components and layout for a game view, including the game board, player
 * interaction controls, and a message panel. It is designed to be extended by specific
 * implementations for different games.
 *
 * @param <T> the type of tiles used in the game
 * @param <P> the type of players participating in the game
 * @author Nick Heggø
 * @version 2025.05.21
 */
public abstract class GameView<T extends Tile, P extends Player> extends View {

  /**
   * Constructs a new {@link GameView} instance by initializing its layout, including the game
   * board, the right pane for player interactions, and the bottom pane for displaying messages.
   *
   * @param sceneSwitcher an instance of {@link SceneSwitcher} used to switch between scenes in the
   *     application
   * @param eventBus the {@link EventBus} for communication between different components and events
   * @param tilesSupplier a supplier providing the list of tiles to be displayed in the game board
   * @param playersSupplier a supplier providing the list of players to be displayed in the game
   *     view
   * @param rollDiceAction an {@link EventHandler} for handling roll dice actions in the game
   */
  protected GameView(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      Supplier<List<T>> tilesSupplier,
      Supplier<List<P>> playersSupplier,
      EventHandler<ActionEvent> rollDiceAction) {

    var root = new BorderPane();
    setRoot(root);

    // the game board that will show the tiles and the players
    root.setCenter(createCenterPane(eventBus, tilesSupplier, playersSupplier));

    // the right pane that contains the player dashboard, roll dice button, and the setting button
    root.setRight(createRightPane(sceneSwitcher, eventBus, playersSupplier, rollDiceAction));

    // the bottom pane that contains the message panel
    root.setBottom(createBottomPane(eventBus));
  }

  /**
   * Creates and returns the center pane of the game view, which displays the game board consisting
   * of tiles and players.
   *
   * <p>Classes extending this abstract class should implement this method to provide the center
   * pane
   *
   * @param eventBus the event bus used for handling events and communication between components
   * @param tiles a supplier providing the list of tiles to be displayed on the game board
   * @param players a supplier providing the list of players to be displayed on the game board
   * @return a {@link Pane} representing the center of the game view
   */
  protected abstract Pane createCenterPane(
      EventBus eventBus, Supplier<List<T>> tiles, Supplier<List<P>> players);

  /**
   * Creates and returns the right pane of the game view, which is used to manage player
   * interactions and provides settings and controls like rolling dice.
   *
   * @param sceneSwitcher an instance of {@link SceneSwitcher} used to handle scene transitions
   * @param eventBus the {@link EventBus} for communication and event handling between components
   * @param players a {@link Supplier} providing the list of players displayed in the player
   *     dashboard
   * @param rollDiceHandler an {@link EventHandler} for handling actions related to rolling dice
   * @return a {@link Pane} representing the right section of the game view
   */
  protected Pane createRightPane(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      Supplier<List<P>> players,
      EventHandler<ActionEvent> rollDiceHandler) {
    var right = new VBox();
    right.prefWidthProperty().bind(this.widthProperty().multiply(0.2));
    right.prefHeightProperty().bind(this.heightProperty().multiply(0.7));

    var settingButton = new SettingButton(sceneSwitcher);
    settingButton.isInGame(true);
    var playerDashboard = new PlayerDashboard<>(eventBus, players);
    var rollDiceButton = new RollDiceButton(rollDiceHandler);
    addComponents(settingButton, playerDashboard, rollDiceButton);
    right.getChildren().addAll(settingButton, playerDashboard, rollDiceButton);
    return right;
  }

  /**
   * Creates and returns the bottom pane of the game view, which contains a {@link MessagePanel} for
   * displaying messages or logs related to the game. The {@link MessagePanel} is set up to take
   * approximately 28% of the height of the {@link GameView} and is added as a component to the game
   * view.
   *
   * @param eventBus the {@link EventBus} used for communication and event handling between various
   *     components of the application
   * @return a {@link Pane} representing the bottom section of the game view
   */
  protected Pane createBottomPane(EventBus eventBus) {
    var messagePanel = new MessagePanel(eventBus);
    messagePanel.prefHeightProperty().bind(this.heightProperty().multiply(0.28));
    addComponents(messagePanel);
    return messagePanel;
  }
}
