package dev.nheggoe.boardgame.core.common.event.type;

import static org.junit.jupiter.api.Assertions.*;

import dev.nheggoe.boardgame.core.event.type.UserInterfaceEvent;
import org.junit.jupiter.api.Test;

class OutputEventTest {

  @Test
  void testInvalidData() {
    assertThrows(NullPointerException.class, () -> new UserInterfaceEvent.Output(null));
  }

  @Test
  void testBasic() {
    var output = "test";
    var outputEvent = new UserInterfaceEvent.Output(output);
    assertEquals(output, outputEvent.message());
  }
}
