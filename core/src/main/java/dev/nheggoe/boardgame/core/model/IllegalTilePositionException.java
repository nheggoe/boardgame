package dev.nheggoe.boardgame.core.model;

/// Exception thrown to indicate that a tile has been accessed or utilized with an invalid or
/// non-existing position within the context of a board game, such as a Monopoly board.
///
/// This exception is typically used to signal errors in computations, validations, or board
/// setups where the tile position provided does not correspond to a valid index or does not meet the
/// positional constraints expected by the board.
///
/// It extends [RuntimeException], meaning it is unchecked and can be thrown during runtime
/// without requiring explicit declaration in the method signature.
public class IllegalTilePositionException extends RuntimeException {

  /// Constructs a new `IllegalTilePositionException` with a default error message.
  ///
  /// This exception is thrown to indicate that an illegal or invalid tile position has been used.
  /// It is typically utilized in scenarios where operations on a tile in a board game configuration
  /// or validation fail due to a tile position that does not conform to expected rules or
  /// constraints.
  public IllegalTilePositionException() {
    super("Illegal tile position");
  }
}
