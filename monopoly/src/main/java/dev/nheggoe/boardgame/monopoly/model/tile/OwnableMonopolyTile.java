package dev.nheggoe.boardgame.monopoly.model.tile;

import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;

/// Represents a tile in a Monopoly game that can be owned by a player.
///
/// This class is a specific type of [MonopolyTile] designed to hold tiles corresponding to
/// properties, utilities, or other entities that are ownable in the game. It contains an
/// [Ownable] that encapsulates the ownership details, such as price and rent.
///
/// The constructor ensures that the ownable entity associated with this tile is not null. If a
/// null value is provided, an [IllegalStateException] is thrown.
///
/// This record is part of the sealed interface [MonopolyTile] and adheres to its
/// constraints and hierarchy.
///
/// @param ownable the ownable entity associated with this tile
public record OwnableMonopolyTile(Ownable ownable) implements MonopolyTile {

  /// Constructs an `OwnableMonopolyTile` ensuring the associated [Ownable] entity is not
  /// null.
  ///
  /// This constructor validates that the given `ownable` parameter is not null; otherwise,
  /// it throws an [IllegalStateException]. This is critical to maintain the integrity of the
  /// Monopoly game structure, as all tiles that are ownable must have a valid associated
  /// [Ownable].
  ///
  /// @param ownable the ownable entity associated with this tile
  /// @throws IllegalStateException if the `ownable` parameter is `null`
  public OwnableMonopolyTile {
    if (ownable == null) {
      throw new IllegalStateException("Property cannot be null on property tile!");
    }
  }
}
