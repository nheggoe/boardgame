package dev.nheggoe.boardgame.snake.model;

import dev.nheggoe.boardgame.core.model.Player;

/// Represents a player in the Snake and Ladder game. Extends the base [Player] class to
/// provide specific context within the Snake and Ladder implementation.
///
/// @see Player
/// @author Nick Heggø
/// @version 2025.05.21
public class SnakeAndLadderPlayer extends Player {
  /// Constructs a new player with the specified name and places them at the start tile (position 0)
  /// of the board.
  ///
  /// @param name the name of the player
  /// @param figure the figure player has chosen to play as
  public SnakeAndLadderPlayer(String name, Figure figure) {
    super(name, figure);
  }
}
