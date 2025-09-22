package dev.nheggoe.boardgame.monopoly.model.tile;

import dev.nheggoe.boardgame.core.model.Player;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code JailTile} represents a jail tile on the game board.
 *
 * <p>Players can be sent to jail for a number of rounds, during which their movement may be
 * restricted. This class tracks which players are jailed and how many rounds they have left.
 *
 * <p>Extends {@link CornerMonopolyTile}.
 *
 * @author Mihailo
 * @version 2025.04.25
 */
public final class JailMonopolyTile extends CornerMonopolyTile {

  private final Map<Player, Integer> prisoners;

  /**
   * Constructs a {@code JailTile} at the given board position.
   *
   * @param position the board position of the jail tile
   */
  public JailMonopolyTile(Position position) {
    super(position);
    prisoners = new HashMap<>();
  }

  /**
   * Puts a player in jail for a specified number of rounds.
   *
   * @param player the player to send to jail
   * @param numberOfRounds the number of turns the player must remain jailed
   * @throws IllegalStateException if the player is already in jail
   */
  public void jailForNumberOfRounds(Player player, int numberOfRounds) {
    if (prisoners.containsKey(player)) {
      throw new IllegalStateException("Player is already in jail!");
    }
    prisoners.put(player, numberOfRounds);
  }

  /**
   * Checks whether the specified player is currently in jail.
   *
   * @param player the player to check
   * @return {@code true} if the player is in jail, {@code false} otherwise
   */
  public boolean isPlayerInJail(Player player) {
    return prisoners.containsKey(player);
  }

  /**
   * Decrements the number of jail rounds left for a player.
   *
   * <p>If the player has no remaining rounds after decrementing, they are released from jail.
   *
   * @param player the player to advance their jail time
   * @throws IllegalStateException if the player is not currently jailed
   */
  public boolean releaseIfPossible(Player player) {
    if (!prisoners.containsKey(player)) {
      throw new IllegalStateException("Player is not in jail!");
    }
    int roundsLeft = prisoners.merge(player, -1, Integer::sum);
    boolean shouldRelease = roundsLeft <= 0;
    if (shouldRelease) {
      prisoners.remove(player);
    }
    return shouldRelease;
  }

  /**
   * Retrieves the number of rounds remaining for the specified player who is currently in jail.
   *
   * @param player the player whose remaining jail rounds are to be retrieved
   * @return the number of rounds left for the player in jail
   * @throws IllegalStateException if the player is not currently in jail
   */
  public int getNumberOfRoundsLeft(Player player) {
    if (!prisoners.containsKey(player)) {
      throw new IllegalStateException("Player is not in jail!");
    }
    return prisoners.get(player);
  }
}
