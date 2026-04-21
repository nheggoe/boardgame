package dev.nheggoe.boardgame.core.io.json;

/// Custom exception used to indicate errors related to JSON handling operations. This exception is
/// typically thrown to encapsulate issues encountered during JSON parsing, serialization, or
/// deserialization processes.
///
/// @author Nick Heggø
/// @version 2025.03.25
public class JsonException extends RuntimeException {

  /// Constructs a new instance of `JsonException` with the specified detail message. This
  /// exception is used to signal errors encountered during JSON handling operations, such as
  /// parsing, serialization, or deserialization issues.
  ///
  /// @param message the detail message that describes the error; must not be null or empty
  public JsonException(String message) {
    super(message);
  }
}
