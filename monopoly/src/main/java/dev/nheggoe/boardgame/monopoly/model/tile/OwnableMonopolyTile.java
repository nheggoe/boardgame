package dev.nheggoe.boardgame.monopoly.model.tile;

import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;

/**
 * Represents a tile in a Monopoly game that can be owned by a player.
 *
 * <p>This class is a specific type of {@link MonopolyTile} designed to hold tiles corresponding to
 * properties, utilities, or other entities that are ownable in the game. It contains an {@link
 * Ownable} that encapsulates the ownership details, such as price and rent.
 *
 * <p>The constructor ensures that the ownable entity associated with this tile is not null. If a
 * null value is provided, an {@link IllegalStateException} is thrown.
 *
 * <p>This record is part of the sealed interface {@link MonopolyTile} and adheres to its
 * constraints and hierarchy.
 *
 * @param ownable the ownable entity associated with this tile
 */
public record OwnableMonopolyTile(Ownable ownable) implements MonopolyTile {

  /**
   * Constructs an {@code OwnableMonopolyTile} ensuring the associated {@link Ownable} entity is not
   * null.
   *
   * <p>This constructor validates that the given {@code ownable} parameter is not null; otherwise,
   * it throws an {@link IllegalStateException}. This is critical to maintain the integrity of the
   * Monopoly game structure, as all tiles that are ownable must have a valid associated {@link
   * Ownable}.
   *
   * @param ownable the ownable entity associated with this tile
   * @throws IllegalStateException if the {@code ownable} parameter is {@code null}
   */
  public OwnableMonopolyTile {
    if (ownable == null) {
      throw new IllegalStateException("Property cannot be null on property tile!");
    }
  }
}
