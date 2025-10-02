package dev.nheggoe.boardgame.core;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.model.Game;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.model.Tile;
import java.util.List;

/// The `GameEngine` class manages the core mechanics and progression of a game, providing
/// functionalities like player and tile management, tracking the game's state, and advancing game
/// turns.
///
/// This class is designed as a generic container, requiring a specific type of `Tile` and
/// `Player` to operate, which the game logic relies upon.
///
/// @param <T> the type of tiles used in the game
/// @param <P> the type of players participating in the game
public record GameEngine<T extends Tile, P extends Player>(Game<T, P> game) {

  /// Constructs a `GameEngine` instance for managing the core mechanics of a specific game.
  /// This constructor initializes the engine with the specified game instance, ensuring that the
  /// provided game object is not null.
  ///
  /// @param game an instance of `Game` that represents the game logic and state; must not be
  ///     null
  /// @throws NullPointerException if `game` is null
  public GameEngine {
    requireNonNull(game, "Game cannot be null!");
  }

  public List<P> getPlayers() {
    return List.copyOf(game.getPlayers());
  }

  public List<T> getTiles() {
    return List.copyOf(game.getTiles());
  }

  /// Advances the state of the game to the next player's turn, provided the game has not yet ended.
  /// This method checks if the game has concluded via the `isEnded()` method and returns
  /// without performing any operations if the game is over. If the game is still active, it
  /// proceeds
  /// to invoke the `nextTurn()` method of the associated game instance, transitioning the game
  /// to the next turn and updating the relevant state or mechanics.
  public void nextTurn() {
    if (!game.isEnded()) {
      game.nextTurn();
    }
  }

  public boolean isEnded() {
    return game.isEnded();
  }
}
