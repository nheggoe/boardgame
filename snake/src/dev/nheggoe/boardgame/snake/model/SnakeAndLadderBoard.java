package dev.nheggoe.boardgame.snake.model;

import dev.nheggoe.boardgame.core.model.Board;
import dev.nheggoe.boardgame.core.model.InvalidBoardLayoutException;
import dev.nheggoe.boardgame.snake.model.tile.LadderTile;
import dev.nheggoe.boardgame.snake.model.tile.NormalTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeTile;
import java.util.List;

/// Represents the board layout for a Snake and Ladder game.
///
/// @param tiles the list of tiles composing the board
/// @author Nick Heggø, Mihailo Hranisavljevic
/// @version 2025.05.21
public record SnakeAndLadderBoard(List<SnakeAndLadderTile> tiles)
    implements Board<SnakeAndLadderTile> {

  /// Constructs a new SnakeAndLadderBoard.
  ///
  /// @param tiles the tile list to initialise the board with
  /// @throws InvalidBoardLayoutException if any tile is null or logically invalid
  public SnakeAndLadderBoard {
    validateTiles(tiles);
    tiles = List.copyOf(tiles);
  }

  /// Returns the tile at the given index.
  ///
  /// @param index index of the tile (0-based)
  /// @return the tile at that index
  @Override
  public SnakeAndLadderTile getTileAtIndex(int index) {
    return tiles.get(index);
  }

  /// Returns the total number of tiles.
  ///
  /// @return board size
  @Override
  public int size() {
    return tiles.size();
  }

  /// Validates all tiles for null entries and logical consistency.
  ///
  /// @param tiles the tile list to check
  /// @throws InvalidBoardLayoutException on validation failure
  private void validateTiles(List<SnakeAndLadderTile> tiles) {
    for (int index = 0; index < tiles.size(); index++) {
      var tile = tiles.get(index);
      switch (tile) {
        case null ->
            throw new InvalidBoardLayoutException(
                "Board cannot contain null tiles. Index: %d".formatted(index));
        case SnakeTile snakeTile -> assertSnakeTile(index, snakeTile);
        case LadderTile ladderTile -> assertLadderTile(index, ladderTile);
        case NormalTile ignored -> {
          // valid by default
        }
      }
    }
  }

  /// Validates a SnakeTile.
  ///
  /// @param index current index
  /// @param tile tile to check
  /// @throws InvalidBoardLayoutException if snake causes underflow
  private void assertSnakeTile(int index, SnakeTile tile) {
    if (index - tile.tilesToSlideBack() < 0) {
      throw new InvalidBoardLayoutException(
          "Snake tile at index %d slide back more than the the index of the tile."
              .formatted(index));
    }
  }

  /// Validates a LadderTile to ensure it does not move the player before the first tile.
  /// Overshooting the end is allowed and will simply land the player on the last square.
  ///
  /// @param index the index of the tile
  /// @param tile the LadderTile instance
  /// @throws InvalidBoardLayoutException if skip is negative
  private void assertLadderTile(int index, LadderTile tile) {
    if (tile.tilesToSkip() < 0) {
      throw new InvalidBoardLayoutException(
          "Ladder tile at index %d must have a non-negative skip.".formatted(index));
    }
  }
}
