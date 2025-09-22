package dev.nheggoe.boardgame.core.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/// The `TurnManager` class is responsible for managing the turn order and round progression of
/// players in a game. It allows for managing a sequence of players, retrieving the current or next
/// player, removing players from the turn sequence, and tracking the current round number.
///
/// @param <P> the type parameter representing a [Player]
public class TurnManager<P extends Player> {

  private final List<P> players;
  private int roundNumber = 0;
  private Iterator<P> iterator;
  private P currentPlayer;

  /// Constructs a `TurnManager` to manage the turn order for a list of players.
  ///
  /// @param players the list of players to be managed in the turn sequence; must not be null
  /// @throws NullPointerException if the players list is null
  protected TurnManager(List<P> players) {
    requireNonNull(players, "Players cannot be null!");
    this.players = new ArrayList<>(players);
    this.iterator = this.players.iterator();
    this.currentPlayer = this.players.getFirst();
  }

  /// Retrieves the current player in the turn sequence.
  ///
  /// @return the current player of type P
  protected P getCurrentPlayer() {
    return currentPlayer;
  }

  /// Advances the turn to the next player in the sequence. If the end of the player list is
  /// reached,
  /// it starts over from the beginning, and increments the round number.
  ///
  /// @return the next player in the turn sequence
  protected P getNextPlayer() {
    if (!iterator.hasNext()) {
      iterator = players.iterator();
      roundNumber++;
    }
    currentPlayer = iterator.next();
    return currentPlayer;
  }

  /// Removes the specified player from the turn manager's player list. If the player to be removed
  /// is the current player, they are directly removed. Otherwise, the method iterates through the
  /// list to locate and remove the player, then resets the iterator to maintain the correct
  /// position
  /// in the turn order.
  ///
  /// @param player the player to be removed from the turn manager; must not be null
  /// @throws NullPointerException if the player is null
  protected void removePlayer(Player player) {
    requireNonNull(player, "Player to remove cannot be null!");
    if (player.equals(currentPlayer)) {
      iterator.remove();
    } else {
      // new iterator
      iterator = players.iterator();
      while (iterator.hasNext()) {
        if (iterator.next().equals(player)) {
          iterator.remove();
        }
      }
      // reset back to the current position
      iterator = players.iterator();
      while (iterator.hasNext() && !iterator.next().equals(currentPlayer)) {
        // iterate until it found the previous user
      }
    }
  }

  /// Retrieves the current round number in the game.
  ///
  /// @return the current round number
  protected int getRoundNumber() {
    return roundNumber;
  }

  /// Retrieves an unmodifiable view of the list of players managed by the turn manager.
  ///
  /// @return a list of players as an unmodifiable copy of the internal player list
  protected List<P> getPlayers() {
    return List.copyOf(players);
  }
}
