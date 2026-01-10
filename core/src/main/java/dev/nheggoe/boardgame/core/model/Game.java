package dev.nheggoe.boardgame.core.model;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.type.UserInterfaceEvent;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/// An abstract representation of a generic game that involves a board, players, and game mechanics.
/// The class defines core behaviors and attributes for managing the state and flow of the game, but
/// requires subclasses to implement game-specific details such as actions tied to rounds or
/// determining winners.
///
/// @param <T> the type of tiles used in the game, extending [Tile]
/// @param <P> the type of players participating in the game, extending [Player]
public abstract class Game<T extends Tile, P extends Player> {

  // state
  private final UUID id;
  private String gameSaveName;
  private boolean isEnded;

  @SuppressWarnings("java:S2065")
  private final transient TurnManager<P> turnManager;

  // injected dependencies

  @SuppressWarnings("java:S2065")
  private final transient EventBus eventBus;

  private final Board<T> board;

  /// Constructs a new game instance with the specified event bus, game board, and list of players.
  /// Initializes the game with a unique identifier, a default non-ended state, and a turn manager
  /// for managing the sequence of player turns.
  ///
  /// @param eventBus the [EventBus] to use for event management; must not be null
  /// @param board the game [Board] containing tiles; must not be null
  /// @param players the list of players participating in the game; must not be null or empty
  protected Game(EventBus eventBus, Board<T> board, List<P> players) {
    this.id = UUID.randomUUID();
    this.isEnded = false;
    this.turnManager = new TurnManager<>(players);
    this.eventBus = requireNonNull(eventBus, "EventBus cannot be null!");
    this.board = requireNonNull(board, "Board cannot be null!");
  }

  // ------------------------  API  ------------------------

  /// Advances the game to the next turn. This method transitions the game state to the subsequent
  /// player's turn in the turn order, updating any relevant game mechanics or state changes specific
  /// to the implementation. It could involve triggering game-specific events, notifying systems, or
  /// making changes related to the player's actions.
  ///
  /// This method must be implemented by subclasses to define the exact behavior for progressing
  /// the game to the next turn.
  public abstract void nextTurn();

  /// Outputs the string representations of all tiles present on the game board. This method
  /// constructs a formatted string containing the list of tiles obtained from the current game board
  /// and prints it using the [#println(Object)] method.
  ///
  /// The output begins with a header "Tiles on the board:" followed by each tile's string
  /// representation on a new line. The method utilizes the [#getTiles()] method to retrieve
  /// the tiles from the game board.
  ///
  /// If there are no tiles on the board, the output will consist of only the header.
  ///
  /// Implementation details: - The string builder is used to concatenate the header and tile
  /// contents. - Extra characters, if added due to processing, are trimmed before output.
  public void printTiles() {
    StringBuilder sb = new StringBuilder();
    sb.append("Tiles on the board:\n");
    for (var tile : getTiles()) {
      sb.append(tile).append("\n");
    }
    sb.delete(sb.length() - 3, sb.length());
    println(sb);
  }

  /// Marks the game as ended by setting the `isEnded` flag to `true`. This method is
  /// typically invoked when the game reaches its conclusion, either due to a winner being determined
  /// or other game-specific conditions.
  public void endGame() {
    isEnded = true;
  }

  // ------------------------  internal  ------------------------

  /// Outputs the string representation of the provided object as a user interface event. This method
  /// encapsulates the message in a [UserInterfaceEvent.Output] and publishes the event via the
  /// associated [EventBus].
  ///
  /// @param o the object whose string representation is to be output; must not be null
  protected void println(Object o) {
    eventBus.publishEvent(new UserInterfaceEvent.Output(o.toString()));
  }

  /// Notifies the system that a player has moved by publishing a [CoreEvent.PlayerMoved] event
  /// to the associated [EventBus]. This method is typically invoked when a player's position
  /// changes during the game, enabling other components to react to the player's movement.
  ///
  /// @param player the player who has moved; must not be null
  protected void notifyPlayerMoved(Player player) {
    eventBus.publishEvent(new CoreEvent.PlayerMoved(player));
  }

  /// Notifies the system that a dice roll has occurred by publishing a [CoreEvent.DiceRolled]
  /// event to the associated [EventBus]. This method is typically invoked when a dice roll
  /// result is generated during the game, enabling other components to react to the dice roll.
  ///
  /// @param diceRoll the result of the dice roll; must not be null
  protected void notifyDiceRolled(DiceRoll diceRoll) {
    eventBus.publishEvent(new CoreEvent.DiceRolled(diceRoll));
  }

  /// Moves the specified player by a given number of tiles on the game board. The player's position
  /// is updated, and if crossing the end of the board, a round completion action is triggered. After
  /// the move, a notification event is dispatched for the player's movement.
  ///
  /// @param player the player to move; must not be null
  /// @param numberOfTiles the number of tiles to move; must be a non-negative integer
  public void movePlayer(P player, int numberOfTiles) {
    int oldPositon = player.getPosition();
    int newPosition = (player.getPosition() + numberOfTiles) % board.size();
    player.setPosition(newPosition);
    if (oldPositon > newPosition) {
      completeRoundAction(player);
    }
    notifyPlayerMoved(player);
  }

  /// Removes the specified player from the game and notifies interested components by dispatching a
  /// [CoreEvent.PlayerRemoved] event via the associated [EventBus]. This method updates
  /// the player list in the `turnManager`, ensuring the game state properly reflects the
  /// removal of the player.
  ///
  /// @param player the player to be removed from the game; must not be null
  protected void removePlayer(P player) {
    turnManager.removePlayer(player);
    eventBus.publishEvent(new CoreEvent.PlayerRemoved(player));
  }

  /// Completes the round-specific action for the given player. This method is intended to be
  /// implemented in subclasses of the game to define custom behavior that occurs when a player
  /// completes a round. It could involve score updates, triggering special events, or applying
  /// game-specific rules.
  ///
  /// @param player the player for whom the round action is to be completed; must not be null
  protected abstract void completeRoundAction(P player);

  // ------------------------  Getters and Setters  ------------------------

  /// Retrieves the players with the highest score at the end of the game. The method returns a map
  /// entry where the key represents the highest score, and the value is a list of players who
  /// achieved that score.
  ///
  /// @return a map entry containing the highest score as the key and a list of players with that
  ///     score as the value
  public abstract Map.Entry<Integer, List<P>> getWinners();

  public P getCurrentPlayer() {
    return turnManager.getNextPlayer();
  }

  /// Retrieves the tile at the specified position on the game board.
  ///
  /// @param position the index of the tile to retrieve, must be within the valid range of the board
  /// @return the tile of type `T` located at the specified position
  public T getTile(int position) {
    return board.getTileAtIndex(position);
  }

  /// Retrieves the unique identifier of the game.
  ///
  /// @return the UUID of the game
  public UUID getId() {
    return id;
  }

  /// Retrieves the name used to save the game's state. This name is typically used to identify the
  /// game when saving or loading its progress.
  ///
  /// @return the name associated with the saved game
  public String getGameSaveName() {
    return gameSaveName;
  }

  /// Sets the name used to save the game's state. This name is typically used to identify the game
  /// when saving or loading its progress.
  ///
  /// @param gameSaveName the name to associate with the saved game; must not be null or empty
  public void setGameSaveName(String gameSaveName) {
    this.gameSaveName = gameSaveName;
  }

  /// Retrieves the list of tiles that make up the game board. This method delegates to the
  /// underlying [Board#tiles()] method to obtain the tiles.
  ///
  /// @return a list of tiles of type `T` present on the board
  public List<T> getTiles() {
    return board.tiles();
  }

  /// Determines whether the game has ended.
  ///
  /// @return `true` if the game has ended, `false` otherwise
  public boolean isEnded() {
    return isEnded;
  }

  /// Retrieves the [EventBus] associated with this game instance. The [EventBus] is used
  /// for managing and dispatching events to the relevant listeners during the game.
  ///
  /// @return the [EventBus] instance associated with the game
  protected EventBus getEventBus() {
    return eventBus;
  }

  /// Retrieves the next player in the turn order. This method leverages the `TurnManager` to
  /// determine and return the player whose turn is next in the sequence.
  ///
  /// @return the player of type `P` who is next in the turn order
  public P getNextPlayer() {
    return turnManager.getNextPlayer();
  }

  /// Retrieves the game board associated with this game instance.
  ///
  /// @return the board of type `Board<T>` used in the game
  public Board<T> getBoard() {
    return board;
  }

  /// Retrieves the list of players participating in the game. This method delegates the call to the
  /// underlying turn manager to obtain a copy of the current players list.
  ///
  /// @return a list of players of type `P` participating in the game
  public List<P> getPlayers() {
    return turnManager.getPlayers();
  }
}
