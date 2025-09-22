package dev.nheggoe.boardgame.monopoly.model.ownable;

/**
 * Exception thrown to indicate that a monetary operation cannot be completed due to insufficient
 * funds.
 *
 * <p>This exception is typically utilized in scenarios where an attempt is made to spend more funds
 * than are available in a player's balance or financial state. It can provide either a custom
 * message or contain specific details about the attempted operation, such as the amount to spend
 * and the current balance.
 *
 * <p>Constructors: - {@link #InsufficientFundsException(String)} allows specifying a custom error
 * message. - {@link #InsufficientFundsException(int, int)} generates a formatted message indicating
 * the specific amount attempted to be spent and the available balance.
 */
public class InsufficientFundsException extends RuntimeException {

  /**
   * Constructs a new InsufficientFundsException with a specified error message.
   *
   * @param message the detail message indicating the reason for the exception.
   */
  public InsufficientFundsException(String message) {
    super(message);
  }

  /**
   * Constructs a new InsufficientFundsException with a detailed error message.
   *
   * @param amountToSpend the monetary amount that was attempted to be spent.
   * @param balance the current balance available, which is insufficient for the operation.
   */
  public InsufficientFundsException(int amountToSpend, int balance) {
    super("Insufficient funds for " + amountToSpend + ", Available: " + balance);
  }
}
