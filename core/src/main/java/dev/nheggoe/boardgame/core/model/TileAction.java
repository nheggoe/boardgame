package dev.nheggoe.boardgame.core.model;

/**
 * Represents a functional interface that defines an action to be performed on a player within a
 * board game context. Actions implemented using this interface typically represent a specific
 * tile-related effect that alters the state of a player when they land on or interact with a tile.
 *
 * @param <T> the type of player the action applies to, extending the {@link Player} class
 * @author Mihailo Hranisavljevic, Nick Heggø
 * @version 2025.05.08
 */
@FunctionalInterface
public interface TileAction<T extends Player> {

  /**
   * Executes the action associated with the given player.
   *
   * @param t the player on whom the action is executed, must not be null
   */
  void execute(T t);
}
