package dev.nheggoe.boardgame.monopoly.model.tile;

import static java.util.Objects.requireNonNull;

/**
 * Represents a specific type of tile in the Monopoly game that occupies a corner position on the
 * board.
 *
 * <p>This class is an abstract sealed type, permitting only specific subclasses to model the
 * distinct functionalities of various corner tiles, such as Free Parking, Go to Jail, Jail, and
 * Start. The use of sealed typing ensures precise subclass control, enhancing type safety and
 * maintainability.
 *
 * <p>A CornerMonopolyTile has an associated position, constrained to one of four predefined corner
 * positions on the board: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, or BOTTOM_RIGHT. The {@link Position}
 * enum defines these positions.
 *
 * <p>This class provides: - Validation to ensure that the position cannot be null during
 * construction. - A method to access the tile's position. - A default string representation that
 * includes the class name and position.
 *
 * <p>Permitted subclasses modeling different corner tiles are: {@link FreeParkingMonopolyTile},
 * {@link GoToJailMonopolyTile}, {@link JailMonopolyTile}, and {@link StartMonopolyTile}.
 *
 * @author Nick Heggø and Mihailo Hranisavljevic
 * @version 2025.05.06
 */
public abstract sealed class CornerMonopolyTile implements MonopolyTile
    permits FreeParkingMonopolyTile, GoToJailMonopolyTile, JailMonopolyTile, StartMonopolyTile {

  private final Position position;

  /**
   * Constructs a {@link CornerMonopolyTile} with a specified board position.
   *
   * @param position the board position of the corner tile; must not be null
   * @throws NullPointerException if the {@code position} is null
   */
  protected CornerMonopolyTile(Position position) {
    this.position = requireNonNull(position, "Positon of corner tile cannot be null!");
  }

  /**
   * Retrieves the position of this corner tile on the Monopoly board.
   *
   * @return the {@code Position} associated with this corner tile, representing one of the four
   *     predefined corner positions: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, or BOTTOM_RIGHT.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Defines the possible corner positions on a Monopoly board.
   *
   * <p>This enum is used to represent the specific locations occupied by corner tiles on the board.
   * Each corner tile, such as Start, Jail, Free Parking, or Go to Jail, is associated with one of
   * the four predefined positions.
   */
  public enum Position {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + "position=" + position + ']';
  }
}
