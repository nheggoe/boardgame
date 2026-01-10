package dev.nheggoe.boardgame.core.event;

import dev.nheggoe.boardgame.core.event.type.Event;

/// Represents a mechanism for publishing events and managing event listeners.
///
/// The EventPublisher interface provides methods for registering, deregistering, and notifying
/// listeners of specific event types. Implementations of this interface are responsible for
/// maintaining the mappings between event types and their corresponding listeners, as well as
/// ensuring appropriate dispatching of events to those listeners.
///
/// It is designed to facilitate an event-driven architecture, where various components can react
/// to occurrences represented as events without direct coupling between the publishers and
/// listeners.
///
/// Responsibilities of implementing classes include: - Allowing the addition of listeners for
/// specific event types. - Allowing the removal of listeners for specific event types. - Dispatching
/// published events to all relevant listeners in a type-safe manner.
///
/// This interface is particularly useful in systems where decoupling components is beneficial for
/// scalability, modularity, or testability.
///
/// @see Event
/// @see EventListener
public interface EventPublisher {

  /// Registers a listener for a specific type of event. When an event of the specified type is
  /// published, the provided listener will be notified.
  ///
  /// @param eventType the class of the event type that the listener should listen to; must not be
  ///     null
  /// @param listener the event listener to be notified for the specified event type; must not be
  ///     null
  /// @throws NullPointerException if either the eventType or listener is null
  void addListener(Class<? extends Event> eventType, EventListener listener);

  /// Removes a listener for a specific type of event. After removal, the listener will no longer be
  /// notified for events of the specified type.
  ///
  /// @param eventType the class of the event type for which the listener should be removed; must not
  ///     be null
  /// @param listener the event listener to be removed for the specified event type; must not be null
  /// @throws NullPointerException if either the eventType or listener is null
  void removeListener(Class<? extends Event> eventType, EventListener listener);

  /// Publishes an event to all registered listeners that are subscribed to the type of the provided
  /// event. Listeners of the appropriate type will be notified and their respective event handling
  /// logic will be executed. If no listeners are registered for the event type, the event is
  /// ignored.
  ///
  /// @param event the event to be published; must not be null
  /// @throws NullPointerException if the event is null
  void publishEvent(Event event);
}
