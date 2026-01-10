package dev.nheggoe.boardgame.snake.model.tile;

/// Ladder tile in Snake and Ladder.
///
/// @author Nick Heggø
/// @version 2025.05.08
public record LadderTile(int tilesToSkip) implements SnakeAndLadderTile {

  /// Constructs a LadderTile with a given number of tiles to skip.
  ///
  /// @param tilesToSkip number of tiles to advance
  /// @throws IllegalArgumentException if tilesToSkip is zero, negative, or greater than 100
  public LadderTile {
    if (tilesToSkip == 0) {
      throw new IllegalArgumentException("Tiles to skip must be non-zero");
    }
    if (tilesToSkip < 0) {
      throw new IllegalArgumentException("Tiles to skip must be non-negative");
    }
    if (tilesToSkip > 100) {
      throw new IllegalArgumentException("Tiles to skip exceeds maximum of 100");
    }
  }
}
