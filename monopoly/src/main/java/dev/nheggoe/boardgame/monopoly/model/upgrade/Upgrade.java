package dev.nheggoe.boardgame.monopoly.model.upgrade;

/**
 * Represents an upgrade for a property in the Monopoly game, which can be of a specific type and
 * affects the rent multiplier of the property it is applied to.
 *
 * @param type the type of the upgrade, such as a house or hotel. Must not be null.
 * @param rentMultiplierPercentage the additional percentage multiplier that this upgrade
 *     contributes to the rent of the property. Must be non-negative.
 * @author Mihailo
 * @version 2025.04.26
 */
public record Upgrade(UpgradeType type, int rentMultiplierPercentage) {

  /**
   * Constructor for the {@code Upgrade} record, ensuring the rent multiplier percentage is valid.
   *
   * @param type the type of the upgrade, such as HOUSE or HOTEL. Must not be null.
   * @param rentMultiplierPercentage the additional percentage multiplier provided by the upgrade.
   *     Must be non-negative.
   * @throws IllegalArgumentException if {@code rentMultiplierPercentage} is negative.
   */
  public Upgrade {
    if (rentMultiplierPercentage < 0) {
      throw new IllegalArgumentException("Rent multiplier must be positive!");
    }
  }
}
