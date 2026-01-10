package dev.nheggoe.boardgame.monopoly.model.ownable;

import dev.nheggoe.boardgame.core.model.Player;
import java.util.ArrayList;
import java.util.List;

/// Represents a player in a Monopoly game. This class extends the Player class and provides
/// functionality specific to managing a player's assets, balance, and transactions in the context of
/// the Monopoly game.
public class MonopolyPlayer extends Player {

  private final List<Ownable> ownedAssets;

  private int balance = 0;

  /// Constructs a new MonopolyPlayer with the specified name and figure.
  ///
  /// @param name the player's name. Must not be null or blank.
  /// @param figure the figure selected by the player. Represents the player's piece in the game.
  public MonopolyPlayer(String name, Figure figure) {
    super(name, figure);
    this.ownedAssets = new ArrayList<>();
  }

  // ------------------------  public interface  ------------------------

  /// Transfers a specified property from the owner's assets and updates the owner's balance with the
  /// agreed monetary amount.
  ///
  /// @param ownable the property or asset to be removed from the owner's possession.
  /// @param agreedAmount the monetary value to be added to the owner's balance as part of the
  ///     transfer.
  /// @throws IllegalArgumentException if the agreed amount is negative.
  public void transferProperty(Ownable ownable, int agreedAmount) {
    this.ownedAssets.remove(ownable);
    this.addBalance(agreedAmount);
  }

  /// Purchases the specified Ownable asset by deducting its price from the player's balance and
  /// adding it to the list of owned assets.
  ///
  /// @param ownable the property, utility, or railroad to be purchased by the player.
  /// @throws IllegalArgumentException if the price of the asset is negative.
  /// @throws InsufficientFundsException if the player's balance is less than the price of the asset.
  public void purchase(Ownable ownable) {
    validateSufficientFunds(ownable.price());
    deductBalance(ownable.price());
    ownedAssets.add(ownable);
  }

  /// Deducts a specified amount from the player's balance after validating sufficient funds.
  ///
  /// @param amount the monetary value to be deducted from the player's balance. Must be a positive
  ///     integer and less than or equal to the available balance.
  /// @throws IllegalArgumentException if the amount is negative.
  /// @throws InsufficientFundsException if the player's balance is less than the specified amount.
  public void pay(int amount) {
    validateSufficientFunds(amount);
    deductBalance(amount);
  }

  /// Adds a specified amount to the player's balance.
  ///
  /// @param amount the monetary value to be added to the player's balance. Must be a non-negative
  ///     integer.
  /// @throws IllegalArgumentException if the amount is negative.
  public void addBalance(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount to add cannot be negative!");
    }
    this.balance += amount;
  }

  /// Deducts a specified amount from the player's balance after ensuring sufficient funds are
  /// available.
  ///
  /// @param amount the monetary value to be deducted from the player's balance. Must be a positive
  ///     integer and less than or equal to the available balance.
  /// @throws IllegalArgumentException if the amount is negative.
  /// @throws InsufficientFundsException if the player's balance is less than the specified amount.
  public void deductBalance(int amount) {
    if (amount > balance) {
      throw new InsufficientFundsException(amount, balance);
    }
    balance -= amount;
  }

  /// Determines if the current player is the owner of the specified Ownable asset.
  ///
  /// @param ownable the asset to check ownership for. It must be a valid implementation of the
  ///     Ownable interface.
  /// @return true if the player owns the specified asset, otherwise false.
  public boolean isOwnerOf(Ownable ownable) {
    return ownedAssets.contains(ownable);
  }

  /// Checks whether the player has sufficient funds to cover a specified monetary amount. The method
  /// validates the balance and returns a boolean indicating the result.
  ///
  /// @param amount the monetary amount to check against the player's current balance. Must be a
  ///     non-negative value.
  /// @return true if the player has sufficient funds to cover the specified amount, false otherwise.
  /// @throws IllegalArgumentException if the specified amount is negative.
  public boolean hasSufficientFunds(int amount) {
    try {
      validateSufficientFunds(amount);
      return true;
    } catch (InsufficientFundsException e) {
      return false;
    }
  }

  /// Retrieves the list of assets owned by the player.
  ///
  /// @return a list of Ownable objects that represent the player's owned assets.
  public List<Ownable> getOwnedAssets() {
    return new ArrayList<>(ownedAssets);
  }

  /// Calculates the player's total net worth by summing up the current balance and the combined
  /// prices of all owned assets.
  ///
  /// @return the total net worth as an integer, which is the sum of the player's balance and the
  ///     prices of all assets owned by the player.
  public int getNetWorth() {
    return balance + ownedAssets.stream().map(Ownable::price).reduce(Integer::sum).orElse(0);
  }

  // ------------------------  getters and setters  ------------------------

  /// Retrieves the player's current monetary balance.
  ///
  /// @return the current balance of the player as an integer.
  public int getBalance() {
    return balance;
  }

  // ------------------------  private  ------------------------

  private void validateSufficientFunds(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount to purchase can not be negative!");
    }
    if (balance < amount) {
      throw new InsufficientFundsException(amount, balance);
    }
  }
}
