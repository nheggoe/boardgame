package dev.nheggoe.boardgame.monopoly.model.tile;

/// Represents a tax tile in the Monopoly game, where players are required to pay a percentage of
/// their wealth.
///
/// This type of tile is a subtype of [MonopolyTile] and is used to enforce financial
/// penalties during gameplay. The percentage of wealth to be paid is specified at the time of this
/// record's creation.
public record TaxMonopolyTile(int percentage) implements MonopolyTile {
  /// Validates the constructor logic for the `TaxMonopolyTile` class.
  ///
  /// Ensures that the percentage value used to calculate the tax cannot be negative at the time
  /// of object initialization. This restriction is necessary to maintain the integrity of the game
  /// rules.
  ///
  /// @param percentage the percentage of a player's wealth to be paid as tax. Must be a non-negative
  ///     value.
  /// @throws IllegalArgumentException if the specified percentage is negative.
  public TaxMonopolyTile {
    if (percentage < 0) {
      throw new IllegalArgumentException("Amount to pay cannot be negative!");
    }
  }
}
