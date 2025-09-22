package dev.nheggoe.boardgame.app.controller;

import dev.nheggoe.boardgame.app.component.EndDialog;
import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import dev.nheggoe.boardgame.app.view.SnakeGameView;
import dev.nheggoe.boardgame.core.GameEngine;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Controller that connects the Snake and Ladder game model to the JavaFX view.
 *
 * <p>It initialises the {@link SnakeGameView}, wires user actions to the {@link GameEngine}, and
 * triggers the end-game dialogue when appropriate.
 *
 * @author Nick Heggø, Mihailo Hranisavljevic
 * @version 2025.05.21
 */
public class SnakeGameController extends Controller {

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
  }

  /**
   * Constructs and returns the JavaFX view for the game.
   *
   * @param sceneSwitcher scene switcher for navigation
   * @param eventBus event bus for communication
   * @param engine the core game engine
   * @param board the board model
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
