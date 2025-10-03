package dev.nheggoe.boardgame.core.event;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.event.type.Event;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A class that implements an event-driven system for managing and dispatching events.
 *
 * <p>The EventBus class acts as the central hub for registering event listeners and broadcasting
 * events to them. It provides an implementation of the {@link EventPublisher} interface, enabling
 * the system to add and remove listeners for specific event types, as well as dispatch events
 * appropriately.
 *
 * <p>Key responsibilities of the EventBus include:
 * <li>Tracking registered listeners for various event types.
 * <li>Notifying all relevant listeners when an event is published.
 * <li>Ensuring listeners are not notified if they are removed or unregistered.
 *
 * @see EventPublisher
 * @see Event
 * @see EventListener
 */
public final class EventBus implements EventPublisher {

  private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());

  private final Map<Class<? extends Event>, List<EventListener>> listeners = new HashMap<>();

  @Override
  public void addListener(Class<? extends Event> eventType, EventListener listener) {
    requireNonNull(eventType, "Event type cannot be null!");
    requireNonNull(listener, "Listener cannot be null!");
    listeners.computeIfAbsent(eventType, _ -> new ArrayList<>()).add(listener);
  }

  @Override
  public void removeListener(Class<? extends Event> eventType, EventListener listener) {
    requireNonNull(eventType, "Event type cannot be null!");
    requireNonNull(listener, "Listener cannot be null!");
    listeners.getOrDefault(eventType, List.of()).remove(listener);
  }

  @Override
  public void publishEvent(Event event) {
    requireNonNull(event, "event cannot be null!");
    LOGGER.info(() -> LocalDateTime.now() + " sent: " + event);
    listeners
        .getOrDefault(event.getClass(), List.of())
        .forEach(listener -> listener.onEvent(event));
  }
}
