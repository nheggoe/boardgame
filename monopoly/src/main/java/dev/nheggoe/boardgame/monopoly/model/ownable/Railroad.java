package dev.nheggoe.boardgame.monopoly.model.ownable;

/**
 * Represents a Railroad in the Monopoly game that can be owned by a player and generates a fixed
 * rent. A Railroad is one of the types of Ownables in the game.
 *
 * <p>Implements the Ownable interface, which defines the behavior for entities that have a price
 * and generate rent.
 *
 * @param price the purchase cost of the Railroad.
 * @author Mihailo
 * @version 2025.04.26
 */
public record Railroad(int price) implements Ownable {

  @Override
  public int rent() {
    return 25;
  }
}
