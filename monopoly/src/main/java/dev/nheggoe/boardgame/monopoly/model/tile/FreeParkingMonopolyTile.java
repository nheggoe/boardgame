package dev.nheggoe.boardgame.monopoly.model.tile;

/** Represents the "Free Parking" corner tile in the Monopoly game. */
public final class FreeParkingMonopolyTile extends CornerMonopolyTile {
  /**
   * Constructs a {@code FreeParkingMonopolyTile} with the specified position.
   *
   * @param position the position on the Monopoly board where the "Free Parking" tile is located;
   *     must be non-null and one of the predefined corner positions (TOP_LEFT, TOP_RIGHT,
   *     BOTTOM_LEFT, or BOTTOM_RIGHT)
   */
  public FreeParkingMonopolyTile(Position position) {
    super(position);
  }
}
