package dev.nheggoe.boardgame.monopoly.model.ownable;

/// Represents a Utility that charges rent based on a new dice roll.
///
/// @author Mihailo Hranisavljevic
/// @version 2025.04.26
public record Utility(String name, int price) implements Ownable {

  /// Constructs a `Utility` instance with a specified name and price. Validates that the name
  /// is not null or blank to ensure a proper representation of the Utility entity.
  ///
  /// @param name the name of the utility. Must not be null or blank.
  /// @param price the purchase price of the utility.
  /// @throws IllegalArgumentException if the name is null or blank.
  public Utility {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Utility name cannot be null or blank!");
    }
  }

  @Override
  public int rent() {
    return 10;
  }
}
