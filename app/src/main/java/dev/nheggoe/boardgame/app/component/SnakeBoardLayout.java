package dev.nheggoe.boardgame.app.component;

import javafx.geometry.Point2D;

/**
 * Helper class for converting between linear tile numbers and 2D grid coordinates. Used for
 * rendering a classic Snake and Ladder board where rows alternate in a direction.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.05.21
 */
public final class SnakeBoardLayout {

  /** Private constructor to prevent instantiation. */
  private SnakeBoardLayout() {}

  /**
   * Converts a 1-based tile number to a 2D grid coordinate ({@code col}, {@code row}).
   *
   * <p>The conversion assumes a square board where rows alternate a direction—left-to-right and
   * right-to-left—starting from the bottom row.
   *
   * @param tileNumber the tile number must be ≥ 1
   * @param dimension the length of one side of the board
   * @return {@link Point2D} representing the column and row indices
   */
  public static Point2D toGrid(int tileNumber, int dimension) {
    int n = tileNumber - 1;

    int rowFromBottom = n / dimension;
    int row = dimension - 1 - rowFromBottom;

    boolean leftToRight = (rowFromBottom % 2 == 0);
    int colInRow = n % dimension;
    int col = leftToRight ? colInRow : dimension - 1 - colInRow;

    return new Point2D(col, row);
  }
}
