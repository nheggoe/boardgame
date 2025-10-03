package dev.nheggoe.boardgame.app.controller;

import dev.nheggoe.boardgame.app.component.EndDialog;
import dev.nheggoe.boardgame.app.ui.AlertFactory;
import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import dev.nheggoe.boardgame.app.view.SnakeGameView;
import dev.nheggoe.boardgame.core.GameEngine;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.EventListener;
import dev.nheggoe.boardgame.core.event.UnhandledEventException;
import dev.nheggoe.boardgame.core.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.core.event.type.UserInterfaceEvent;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;
import dev.nheggoe.boardgame.snake.SnakeAndLadderEvent;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Controller that connects the Snake and Ladder game model to the JavaFX view.
 *
 * <p>It initialises the {@link SnakeGameView}, wires user actions to the {@link GameEngine}, and
 * triggers the end-game dialogue when appropriate.
 *
 * <p>The controller implements {@link EventListener} and orchestrates all UI components by
 * listening to events and calling component public APIs directly.
 *
 * @author Nick Heggø, Mihailo Hranisavljevic
 * @version 2025.05.21
 */
public class SnakeGameController extends Controller implements EventListener {

  private final EventBus eventBus;
  private final SnakeGameView view;

  /**
   * Constructs a new SnakeGameController and sets up the game view.
   *
   * @param sceneSwitcher global scene switcher
   * @param eventBus event bus for event publishing
   * @param engine the core game engine
   */
  public SnakeGameController(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      GameEngine<SnakeAndLadderTile, SnakeAndLadderPlayer> engine) {

    super(sceneSwitcher, createView(sceneSwitcher, eventBus, engine));
    this.eventBus = eventBus;
    this.view = (SnakeGameView) getView();

    // Register as listener for all relevant events

    eventBus.addListener(CoreEvent.DiceRolled.class, this);
    eventBus.addListener(CoreEvent.PlayerMoved.class, this);
    eventBus.addListener(CoreEvent.PlayerRemoved.class, this);
    eventBus.addListener(CoreEvent.GameEnded.class, this);
    eventBus.addListener(SnakeAndLadderEvent.SnakeEncountered.class, this);
    eventBus.addListener(SnakeAndLadderEvent.LadderEncountered.class, this);
    eventBus.addListener(UserInterfaceEvent.Output.class, this);
    eventBus.addListener(UserInterfaceEvent.Alert.class, this);
  }

  @Override
  public void onEvent(Event event) {
    switch (event) {
      case CoreEvent coreEvent -> onCoreEvent(coreEvent);
      case SnakeAndLadderEvent snakeEvent -> onSnakeAndLadderEvent(snakeEvent);
      case UserInterfaceEvent uiEvent -> onUIEvent(uiEvent);
      default -> throw new UnhandledEventException(event);
    }
  }

  private void onCoreEvent(CoreEvent coreEvent) {
    switch (coreEvent) {
      case CoreEvent.DiceRolled(DiceRoll diceRoll) -> {
        // Snake game doesn't show dice animation, but we still listen to the event
        // for potential future enhancements
      }

      case CoreEvent.PlayerMoved(Player player) -> {
        view.getPlayerRender().handlePlayerMoved();
        view.getPlayerDashboard().highlightPlayer(player);
      }

      case CoreEvent.PlayerRemoved(Player player) ->
          view.getPlayerDashboard().handlePlayerRemoval(player);

      case CoreEvent.GameEnded(Player winner) ->
          AlertFactory.createAlert(
                  Alert.AlertType.INFORMATION, "%s has won the game!".formatted(winner.getName()))
              .showAndWait();
    }
  }

  private void onSnakeAndLadderEvent(SnakeAndLadderEvent snakeEvent) {
    switch (snakeEvent) {
      case SnakeAndLadderEvent.SnakeEncountered(int position) ->
          AlertFactory.createAlert(
                  Alert.AlertType.INFORMATION,
                  "Snake encountered at position %d!".formatted(position))
              .showAndWait();

      case SnakeAndLadderEvent.LadderEncountered(int position) ->
          AlertFactory.createAlert(
                  Alert.AlertType.INFORMATION,
                  "Ladder encountered at position %d!".formatted(position))
              .showAndWait();
    }
  }

  private void onUIEvent(UserInterfaceEvent uiEvent) {
    switch (uiEvent) {
      case UserInterfaceEvent.Output(String message) ->
          view.getMessagePanel().animateMessage(message);

      case UserInterfaceEvent.Alert(String message, var callback) -> {
        var result = AlertFactory.createAlert(Alert.AlertType.CONFIRMATION, message).showAndWait();
        callback.accept(
            result
                .map(ButtonType::getButtonData)
                .map(ButtonBar.ButtonData::isDefaultButton)
                .orElse(false));
      }
    }
  }

  @Override
  public void close() {
    eventBus.removeListener(CoreEvent.DiceRolled.class, this);
    eventBus.removeListener(CoreEvent.PlayerMoved.class, this);
    eventBus.removeListener(CoreEvent.PlayerRemoved.class, this);
    eventBus.removeListener(CoreEvent.GameEnded.class, this);
    eventBus.removeListener(SnakeAndLadderEvent.SnakeEncountered.class, this);
    eventBus.removeListener(SnakeAndLadderEvent.LadderEncountered.class, this);
    eventBus.removeListener(UserInterfaceEvent.Output.class, this);
    eventBus.removeListener(UserInterfaceEvent.Alert.class, this);
  }

  /**
   * Constructs and returns the JavaFX view for the game.
   *
   * @param sceneSwitcher scene switcher for navigation
   * @param eventBus event bus for communication
   * @param engine the core game engine
   * @return fully constructed game view
   */
  private static SnakeGameView createView(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      GameEngine<SnakeAndLadderTile, SnakeAndLadderPlayer> engine) {
    return new SnakeGameView(
        sceneSwitcher,
        eventBus,
        engine::getTiles,
        engine::getPlayers,
        nextTurnEventHandler(sceneSwitcher, engine));
  }

  /**
   * Creates the event handler responsible for rolling the dice and checking for the game end.
   *
   * @param sceneSwitcher scene switcher for UI transitions
   * @param gameEngine the game engine to invoke next turns
   * @return JavaFX action event handler
   */
  private static EventHandler<ActionEvent> nextTurnEventHandler(
      SceneSwitcher sceneSwitcher,
      GameEngine<SnakeAndLadderTile, SnakeAndLadderPlayer> gameEngine) {
    return unused -> {
      gameEngine.nextTurn();
      if (gameEngine.isEnded()) {
        new EndDialog(sceneSwitcher).showAndWait();
      }
    };
  }
}
