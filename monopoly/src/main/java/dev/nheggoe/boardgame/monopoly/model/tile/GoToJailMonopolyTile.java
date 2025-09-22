package dev.nheggoe.boardgame.monopoly.model.tile;

/**
 * {@code GoToJailTile} represents the tile that sends a player directly to jail when landed on.
 *
 * <p>Extends {@link CornerMonopolyTile}.
 *
 * @author Mihailo
 * @version 2025.04.25
 */
public final class GoToJailMonopolyTile extends CornerMonopolyTile {

  /**
   * Constructs a {@code GoToJailMonopolyTile} at the specified position on the game board. This
   * tile sends a player directly to jail when landed on.
   *
   * @param position the board position of the "Go to Jail" tile; must not be null
   * @throws NullPointerException if the {@code position} is null
   */
  public GoToJailMonopolyTile(Position position) {
    super(position);
  }
}
