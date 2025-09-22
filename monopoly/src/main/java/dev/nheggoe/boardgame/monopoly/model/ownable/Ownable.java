package dev.nheggoe.boardgame.monopoly.model.ownable;

/**
 * Represents an entity that can be owned in a Monopoly game. Classes implementing this interface
 * must provide details for a purchase price and the rent associated with the entity.
 *
 * <p>The implementations of this interface include: - {@code Property}: Represents properties with
 * specific colors, upgrades, and varying rent. - {@code Railroad}: Represents a type of ownable
 * entity with fixed rent. - {@code Utility}: Represents utilities where rent is based on game
 * mechanics like dice rolls.
 *
 * <p>Use this interface to implement any game components that can be bought and generate rent.
 */
public sealed interface Ownable permits Property, Railroad, Utility {

  /**
   * Returns the purchase price of the entity.
   *
   * @return the price of the entity as an integer value.
   */
  int price();

  /**
   * Calculates and returns the rent value associated with the entity. The rent may vary based on
   * the type of entity and its specific rules.
   *
   * @return the rent amount as an integer, determined by the type of entity and its current status.
   */
  int rent();
}
