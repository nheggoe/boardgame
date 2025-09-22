package dev.nheggoe.boardgame.snake.model.tile;

/**
 * Snake tile in Snake and Ladder. When a player lands on this tile, they slide back a fixed number
 * of tiles.
 *
 * @param tilesToSlideBack number of tiles to slide back
 * @author Nick Heggø
 * @version 2025.05.09
 */
public record SnakeTile(int tilesToSlideBack) implements SnakeAndLadderTile {

  /**
   * Constructs a {@code SnakeTile}.
   *
   * @param tilesToSlideBack number of tiles to slide back
   * @throws IllegalArgumentException if zero, negative, or greater than 100
   */
  public SnakeTile {
    if (tilesToSlideBack == 0) {
      throw new IllegalArgumentException("Tiles to slide back must be non-zero");
    }
    if (tilesToSlideBack < 0) {
      throw new IllegalArgumentException("Tiles to slide back must be non-negative");
    }
    if (tilesToSlideBack > 100) {
      throw new IllegalArgumentException("Tiles to slide back exceeds maximum of 100");
    }
  }
}
