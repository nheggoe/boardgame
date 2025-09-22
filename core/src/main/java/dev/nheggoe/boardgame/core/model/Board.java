package dev.nheggoe.boardgame.core.model;

import java.util.List;

/**
 * Represents a game board which consists of multiple tiles. This interface provides methods to
 * access and interact with the tiles on the board.
 *
 * @param <T> the type of tile used in this board, which must implement the {@link Tile} interface
 * @author Nick Heggø
 * @version 2025.05.08
 */
public interface Board<T extends Tile> {

  /**
   * Retrieves a list of tiles that make up the game board.
   *
   * @return a list of tiles of type {@code T} present on the board
   */
  List<T> tiles();

  /**
   * Retrieves the tile at the specified index on the board.
   *
   * @param index the position of the tile to retrieve, must be within the valid range of the board
   * @return the tile of type {@code T} located at the specified index
   */
  T getTileAtIndex(int index);

  /**
   * Retrieves the total number of tiles on the board.
   *
   * @return the number of tiles present on the board
   */
  int size();
}
