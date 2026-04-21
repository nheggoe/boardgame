package dev.nheggoe.boardgame.monopoly.model.ownable;

import dev.nheggoe.boardgame.monopoly.model.upgrade.Upgrade;
import dev.nheggoe.boardgame.monopoly.model.upgrade.UpgradeType;
import java.util.ArrayList;
import java.util.List;

/// Represents a property in a Monopoly game. A property has a name, a color category, a purchase
/// price, and may have upgrades such as houses or hotels. Properties generate rent based on their
/// price and upgrades.
///
/// This class implements the `Ownable` interface, allowing it to be owned and managed in
/// the context of the game's mechanics. Properties can be upgraded, which influences their rent
/// value.
public final class Property implements Ownable {

  private final String name;
  private final Color color;
  private final int price;
  private final List<Upgrade> upgrades;

  /// Constructs a new `Property` instance with the specified name, color, and price. Validates
  /// that the name is not null or blank, the color is not null, and the price is not negative.
  ///
  /// @param name the name of the property. Must not be null or blank.
  /// @param color the color category of the property. Must not be null.
  /// @param price the purchase price of the property. Must be a non-negative integer.
  /// @throws IllegalArgumentException if the name is null, blank, or if the color is null, or if the
  ///     price is negative.
  public Property(String name, Color color, int price) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Property name cannot be empty.");
    }
    if (color == null) {
      throw new IllegalArgumentException("Property must have a color.");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Price of property must be a positive number.");
    }
    this.name = name;
    this.color = color;
    this.price = price;
    this.upgrades = new ArrayList<>();
  }

  @Override
  public int price() {
    return price;
  }

  @Override
  public int rent() {
    int baseRent = (int) (price * 0.7);
    int bonusRent = upgrades.stream().mapToInt(Upgrade::rentMultiplierPercentage).sum();
    return baseRent + (baseRent * bonusRent / 100);
  }

  /// Adds an upgrade to this property. Upgrades can affect the rent value of the property and
  /// include types such as houses and hotels.
  ///
  /// @param upgrade the upgrade to be added to the property. Must not be null.
  public void addUpgrade(Upgrade upgrade) {
    upgrades.add(upgrade);
  }

  /// Checks whether this property has an upgrade of type hotel.
  ///
  /// @return true if the property has a hotel upgrade, false otherwise.
  public boolean hasHotel() {
    return upgrades.stream().anyMatch(u -> u.type() == UpgradeType.HOTEL);
  }

  /// Determines whether a house can be built on this property. A house can be built if the property
  /// does not already have a hotel and the total number of houses currently on the property is less
  /// than 4.
  ///
  /// @return true if a house can be built on this property, false otherwise.
  public boolean canBuildHouse() {
    return !hasHotel() && (countHouses() < 4);
  }

  /// Counts the number of house upgrades currently present on this property.
  ///
  /// @return the total count of upgrades of type HOUSE.
  public int countHouses() {
    return (int) upgrades.stream().filter(u -> u.type() == UpgradeType.HOUSE).count();
  }

  /// Retrieves an unmodifiable list of upgrades currently associated with this property. Upgrades
  /// may include types such as houses and hotels, which can influence the property’s rent value or
  /// other characteristics.
  ///
  /// @return an unmodifiable list of `Upgrade` instances representing the upgrades on this
  ///     property.
  public List<Upgrade> getUpgrades() {
    return List.copyOf(upgrades);
  }

  /// Retrieves the name of this property.
  ///
  /// @return the name of the property as a string.
  public String getName() {
    return name;
  }

  /// Retrieves the color associated with this property. The color represents a category in the
  /// Monopoly game used for grouping properties.
  ///
  /// @return the color of the property as a `Color` enum value.
  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "Property[name=%s, price=%d, color=%s]".formatted(name, price, color);
  }

  /// Represents the color categories for properties in a Monopoly game. Each color is associated
  /// with a specific set of properties on the board and dictates gameplay rules such as group-based
  /// rent multipliers.
  ///
  /// These categories are used for grouping properties, determining upgrade possibilities, and
  /// other gameplay mechanics.
  public enum Color {
    BROWN,
    DARK_BLUE,
    GREEN,
    LIGHT_BLUE,
    ORANGE,
    PINK,
    RED,
    YELLOW
  }
}
