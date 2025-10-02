package dev.nheggoe.boardgame.core.event.type;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;

/**
 * Represents core game events within an event-driven system. This sealed interface defines a set of
 * events that encapsulate key actions or changes within the game state, such as dice rolls, player
 * movements, and player removals. Implementations of this interface are modeled as records to
 * ensure immutability and simplicity in representing event data.
 *
 * @author Nick Heggø
 * @version 2025.05.20
 */
public sealed interface CoreEvent extends Event {

  /**
   * Represents an event triggered when a dice roll has occurred. This is a concrete implementation
   * of the {@link CoreEvent} interface, encapsulating the result of the dice roll.
   *
   * @param diceRoll the result of the dice roll; must not be null
   */
  record DiceRolled(DiceRoll diceRoll) implements CoreEvent {
    public DiceRolled {
      requireNonNull(diceRoll, "DiceRoll cannot be null!");
    }
  }

  /**
   * Represents an event triggered when a player moves in the game. This record serves as a concrete
   * implementation of the {@link CoreEvent} interface to encapsulate information about the player's
   * movement.
   *
   * @param player the player who has moved; must not be null
   */
  record PlayerMoved(Player player) implements CoreEvent {
    public PlayerMoved {
      requireNonNull(player, "Player cannot be null!");
    }
  }

  /**
   * Represents an event triggered when a player is removed from the game.
   *
   * <p>This record serves as a concrete implementation of the {@link CoreEvent} interface,
   * encapsulating the removal of a player without additional metadata or parameters.
   *
   * <p>The purpose of this event is to signal that a player has been removed, enabling other
   * components to process and react to this action within the event-driven game system.
   */
  record PlayerRemoved(Player player) implements CoreEvent {
    public PlayerRemoved {
      requireNonNull(player, "Player cannot be null!");
    }
  }

  record GameEnded<P extends Player>(P winner) implements CoreEvent {
    public GameEnded {
      requireNonNull(winner, "Winner of the game cannot be null!");
    }
  }
}
