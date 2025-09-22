package dev.nheggoe.boardgame.core.model;

/**
 * This exception is thrown to indicate that a board layout does not meet the expected or valid
 * configuration requirements. It is specific to scenarios where a board's configuration is invalid,
 * such as missing required components, incorrect alignment, or non-compliance with predefined
 * layout rules.
 *
 * <p>The {@link InvalidBoardLayoutException} can provide additional details about the error through
 * its constructors, including a custom message describing the issue in more detail.
 *
 * <p>Common use cases for this exception include: - Asserting the presence of required tiles in a
 * game board. - Validating the structural layout or shape of a board. - Identifying issues such as
 * overlapping or missing tiles in corner positions.
 */
public class InvalidBoardLayoutException extends RuntimeException {

  /**
   * Constructs a new InvalidBoardLayoutException with the specified detail message.
   *
   * @param message the detail message providing additional context about the invalid board layout
   */
  public InvalidBoardLayoutException(String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidBoardLayoutException with a default message indicating that the board
   * layout is invalid. This constructor is useful when no specific details about the invalid
   * configuration are necessary or available.
   */
  public InvalidBoardLayoutException() {
    super("Invalid board setup");
  }
}
