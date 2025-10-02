package dev.nheggoe.boardgame.core.event.type;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

/**
 * Represents user interface related events in an event-driven system. This sealed interface defines
 * a set of events designed to handle interactions between the user interface and other parts of the
 * application, such as output messages or user input requests. Implementations of this interface
 * are modeled as records for simplicity and immutability.
 */
public sealed interface UserInterfaceEvent extends Event {

  /**
   * Represents an output event that encapsulates a message for display to the user in an
   * event-driven user interface system.
   *
   * <p>This record ensures immutability and type safety when processing events related to user
   * interface outputs. The message encapsulated in this event cannot be null and must be specified
   * upon creation. It serves as a concrete implementation of the {@link UserInterfaceEvent}
   * interface.
   *
   * @param message the output message to be displayed; must not be null
   * @throws NullPointerException if the provided message is null
   */
  record Output(String message) implements UserInterfaceEvent {
    public Output {
      requireNonNull(message, "Output message cannot be null!");
    }
  }

  /**
   * Represents a request for user input in an event-driven system.
   *
   * <p>This record encapsulates the details required to prompt the user for input and handle their
   * response through a callback. It serves as a concrete implementation of the {@link
   * UserInterfaceEvent} interface, ensuring immutability and type safety when dealing with user
   * input requests.
   *
   * @param <T> the type of input expected from the user
   * @param message the message to be displayed to the user, prompting input; must not be null
   * @param callback the callback function to handle the user's input; must not be null
   */
  record UserInputRequest<T>(String message, Consumer<T> callback) implements UserInterfaceEvent {
    public UserInputRequest {
      requireNonNull(message, "Message cannot be null!");
      requireNonNull(callback, "Callback cannot be null!");
    }

    /**
     * Accepts an input and processes it using the callback function.
     *
     * @param input the input to be processed; must not be null
     * @throws NullPointerException if the input is null and the callback does not handle null
     *     values
     */
    public void accept(T input) {
      callback.accept(input);
    }
  }

  record Alert(String message, Consumer<Boolean> callback) implements UserInterfaceEvent {
    public Alert {
      requireNonNull(message);
      requireNonNull(callback);
    }
  }
}
