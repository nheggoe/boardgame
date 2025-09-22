package dev.nheggoe.boardgame.core.common.event;

import static org.junit.jupiter.api.Assertions.*;

import dev.nheggoe.boardgame.common.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventBusTest {

  @Mock private EventListener listener;

  @Test
  void testBasic() {
    var eventBus = new EventBus();
    assertNotNull(eventBus);
  }

  @Test
  void testNullListener() {
    var eventBus = new EventBus();
    assertDoesNotThrow(() -> eventBus.addListener(CoreEvent.PlayerRemoved.class, listener));
    assertThrows(
        NullPointerException.class,
        () -> eventBus.addListener(CoreEvent.PlayerRemoved.class, null));
    assertThrows(NullPointerException.class, () -> eventBus.addListener(null, listener));
  }

  @Test
  void testRemoveListener() {
    var eventBus = new EventBus();
    eventBus.addListener(CoreEvent.PlayerRemoved.class, listener);
    assertDoesNotThrow(() -> eventBus.removeListener(CoreEvent.PlayerRemoved.class, listener));
    assertThrows(
        NullPointerException.class,
        () -> eventBus.removeListener(CoreEvent.PlayerRemoved.class, null));
    assertThrows(NullPointerException.class, () -> eventBus.removeListener(null, listener));
  }

  @Test
  void testPublishEvent() {
    var eventBus = new EventBus();
    eventBus.addListener(CoreEvent.PlayerRemoved.class, listener);
    assertThrows(NullPointerException.class, () -> eventBus.publishEvent(null));
  }
}
