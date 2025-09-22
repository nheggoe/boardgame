package dev.nheggoe.boardgame.app.ui;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.EventListener;
import dev.nheggoe.boardgame.core.event.type.Event;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an abstract component that listens for specific events on an {@link EventBus}. This
 * class serves as a base for components that handle events of specified types. Subclasses must
 * define their own behavior for handling specific events by implementing {@link
 * EventListener#onEvent(Event)}.
 *
 * <p>The component automatically registers itself as a listener for the events provided during
 * construction, and unregisters upon closure.
 */
public abstract class EventListeningComponent extends Component implements EventListener {

  private final EventBus eventBus;
  private final List<Class<? extends Event>> subscribedEvents;

  /**
   * Constructs an {@link EventListeningComponent} that listens for specific event types on the
   * provided {@link EventBus}. This class utilizes the provided {@code eventBus} to register itself
   * as a listener for the specified {@code eventType}s.
   *
   * @param eventBus the {@link EventBus} used to register the component as a listener
   * @param eventType the event types this component subscribes to, must be subclasses of {@link
   *     Event}
   * @throws NullPointerException if {@code eventBus} or any of the {@code eventType} values are
   *     {@code null}
   */
  @SafeVarargs
  protected EventListeningComponent(EventBus eventBus, Class<? extends Event>... eventType) {
    this.eventBus = requireNonNull(eventBus, "Event bus cannot be null!");
    this.subscribedEvents = List.copyOf(Arrays.asList(eventType));
    subscribedEvents.forEach(event -> eventBus.addListener(event, this));
  }

  @Override
  public void close() {
    subscribedEvents.forEach(event -> eventBus.removeListener(event, this));
  }

  /**
   * Provides access to the {@link EventBus} instance associated with this component.
   *
   * @return the {@link EventBus} instance used for registering and handling events.
   */
  protected EventBus getEventBus() {
    return eventBus;
  }
}
