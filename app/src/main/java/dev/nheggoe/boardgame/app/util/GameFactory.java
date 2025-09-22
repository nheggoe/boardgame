package dev.nheggoe.boardgame.app.util;

import dev.nheggoe.boardgame.app.MonopolyGame;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.monopoly.model.board.MonopolyBoard;
import dev.nheggoe.boardgame.monopoly.model.board.MonopolyBoardFactory;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderBoardFactory;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderGame;
import java.io.IOException;

/**
 * Factory class responsible for creating instances of game objects. This class provides static
 * methods to initialize and return specific game objects like MonopolyGame and SnakeAndLadderGame
 * with the necessary configurations. The class is designed to encapsulate the object creation logic
 * for these games.
 *
 * @author Nick Heggø
 * @version 2025.05.21
 */
public class GameFactory {

  private GameFactory() {}

  /**
   * Creates and initializes a new instance of a Monopoly game with the necessary configurations.
   * This includes generating a default Monopoly board layout, loading player data, and binding it
   * to an event bus.
   *
   * @param eventBus the event bus used to handle game events and notifications
   * @param playerManager the player manager responsible for loading player information and
   *     configurations
   * @return a new instance of MonopolyGame configured with the default board and player settings
   * @throws IOException if an error occurs while loading player data
   */
  public static MonopolyGame createMonopolyGame(EventBus eventBus, PlayerManager playerManager)
      throws IOException {
    var board = MonopolyBoardFactory.generateBoard(MonopolyBoard.Layout.NORMAL);
    return new MonopolyGame(eventBus, board, playerManager.loadCsvAsMonopolyPlayers());
  }

  /**
   * Creates and initializes a new instance of a Snake and Ladder game with the required
   * configurations. This includes generating a default Snake and Ladder board layout and loading
   * player data.
   *
   * @param eventBus the event bus used to handle game events and notifications
   * @param playerManager the player manager responsible for loading player information and
   *     configurations
   * @return a new instance of SnakeAndLadderGame configured with the default board and player
   *     settings
   * @throws IOException if an error occurs while loading player data
   */
  public static SnakeAndLadderGame createSnakeGame(EventBus eventBus, PlayerManager playerManager)
      throws IOException {
    var board = SnakeAndLadderBoardFactory.createBoard();
    return new SnakeAndLadderGame(eventBus, board, playerManager.loadCsvAsSnakeAndLadderPlayers());
  }
}
