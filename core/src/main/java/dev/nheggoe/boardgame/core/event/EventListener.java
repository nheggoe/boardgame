package dev.nheggoe.boardgame.core.event;

import dev.nheggoe.boardgame.core.event.type.Event;

/**
 * An interface to represent an event listener that can respond to updates.
 *
 * <p>Classes implementing this interface must provide an implementation for the update method,
 * which is intended to define the actions that should be performed when the listener is notified of
 * an event.
 *
 * @see EventPublisher
 * @author Nick Heggø
 * @version 2025.04.02
 */
public interface EventListener extends AutoCloseable {

  /**
   * Invoked when an event occurs. Implementations of this method should define the actions to be
   * performed when the listener is notified of the specified event.
   *
   * @param event the event that has occurred; must not be null
   */
  void onEvent(Event event);
}
