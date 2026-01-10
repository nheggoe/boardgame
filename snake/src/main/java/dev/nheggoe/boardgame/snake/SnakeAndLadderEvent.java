package dev.nheggoe.boardgame.snake;

import dev.nheggoe.boardgame.core.event.type.Event;

/// Represents events specific to the game of Snakes and Ladders. These events capture occurrences
/// related to the interaction of players with snakes and ladders during gameplay.
///
/// This sealed interface defines a set of implementations that signify specific types of events,
/// such as encountering a snake or a ladder, and includes relevant data for handling these events in
/// an event-driven system. Using this structure ensures type safety and simplifies the processing
/// and handling of game-specific events.
public sealed interface SnakeAndLadderEvent extends Event {
  /// Represents an event in which a player encounters a snake during gameplay.
  ///
  /// This is a specific implementation of the [SnakeAndLadderEvent] interface, designed to
  /// signal the occurrence of a snake encounter, along with the position on the board where it
  /// occurred. Snake encounters in the game typically result in the player moving back to a lower
  /// position on the board.
  ///
  /// As a record implementation, this class is immutable, ensuring the integrity of the event's
  /// data once it is created.
  ///
  /// @param position the position on the board where the snake was encountered
  record SnakeEncountered(int position) implements SnakeAndLadderEvent {}

  /// Represents an event occurring when a player encounters a ladder during gameplay in Snakes and
  /// Ladders.
  ///
  /// This record is a concrete implementation of the [SnakeAndLadderEvent] interface,
  /// capturing the specific details of the ladder encounter, particularly the position at which the
  /// event occurred. Ladder encounters typically allow players to ascend to a higher position on the
  /// board.
  ///
  /// As an immutable data type, this record ensures the integrity and consistency of the event
  /// data once it is created, facilitating its usage within an event-driven system.
  ///
  /// @param position the position on the board where the ladder encounter occurred
  record LadderEncountered(int position) implements SnakeAndLadderEvent {}
}
