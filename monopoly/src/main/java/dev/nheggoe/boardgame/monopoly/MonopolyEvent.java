package dev.nheggoe.boardgame.monopoly;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;

/**
 * Represents events occurring in a Monopoly game. This sealed interface is used to define types of
 * domain-specific events that could happen during the gameplay. Implementing classes represent
 * specific actions or occurrences in the game.
 *
 * <p>All implementations of this interface define immutable records that encapsulate details of
 * relevant Monopoly game events, such as property purchase or upgrades.
 */
public sealed interface MonopolyEvent extends Event {

  /**
   * Represents the event of purchasing an ownable asset in a Monopoly game. This event occurs when
   * a player successfully acquires ownership of a specific property, railroad, or utility.
   *
   * <p>Instances of this record encapsulate information about the player who made the purchase and
   * the particular ownable entity that was acquired.
   *
   * @param monopolyPlayer The player making the purchase. Must not be null.
   * @param ownable The asset being purchased. Must not be null.
   */
  record Purchased(MonopolyPlayer monopolyPlayer, Ownable ownable) implements MonopolyEvent {
    public Purchased {
      requireNonNull(monopolyPlayer, "Owner cannot be null!");
      requireNonNull(ownable, "Ownable cannot be null!");
    }
  }

  /**
   * Represents the event of upgrading an ownable asset in a Monopoly game. This event occurs when a
   * player invests in an upgrade, such as adding a house or hotel to a property they own.
   *
   * <p>Instances of this record encapsulate information about the player performing the upgrade and
   * the specific ownable asset that is being upgraded.
   *
   * @param monopolyPlayer The player initiating the upgrade. Must not be null.
   * @param ownable The asset being upgraded. Must not be null.
   */
  record UpgradePurchased(MonopolyPlayer monopolyPlayer, Ownable ownable) implements MonopolyEvent {
    public UpgradePurchased {
      requireNonNull(monopolyPlayer, "Owner cannot be null!");
      requireNonNull(ownable, "Ownable cannot be null!");
    }
  }

  /**
   * Represents the event of a player being sent to jail in a Monopoly game. This event occurs when
   * a player lands on the "Go to Jail" space, draws a "Go to Jail" card, or is otherwise directed
   * to go to jail by the rules of the game.
   *
   * <p>An instance of this record encapsulates the information about the player who has been sent
   * to jail.
   *
   * @param player The MonopolyPlayer who is being sent to jail. Must not be null.
   */
  record PlayerSentToJail(MonopolyPlayer player) implements MonopolyEvent {
    public PlayerSentToJail {
      requireNonNull(player, "Player cannot be null!");
    }
  }
}
