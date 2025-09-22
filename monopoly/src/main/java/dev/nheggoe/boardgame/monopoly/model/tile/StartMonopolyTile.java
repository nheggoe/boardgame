package dev.nheggoe.boardgame.monopoly.model.tile;

/**
 * Represents the "Start" tile in the Monopoly game, located at one of the corner positions of the
 * board.
 *
 * <p>The "Start" tile marks the starting point of the game, where players begin their movement.
 * Players typically pass this tile during a complete cycle around the board and may collect
 * rewards, such as a specific amount of money, as a result of crossing or landing on it.
 *
 * <p>This class is a final subclass of {@link CornerMonopolyTile} and is explicitly associated with
 * the {@link CornerMonopolyTile.Position#BOTTOM_RIGHT} on the board in the default configuration.
 *
 * <p>It inherits functionality and state related to position validation and representation from the
 * {@link CornerMonopolyTile} class.
 */
public final class StartMonopolyTile extends CornerMonopolyTile {

  /**
   * Constructs a {@code StartMonopolyTile} at the specified board position.
   *
   * @param position the board position of the "Start" tile; must not be null
   * @throws NullPointerException if the specified {@code position} is null
   */
  public StartMonopolyTile(Position position) {
    super(position);
  }
}
