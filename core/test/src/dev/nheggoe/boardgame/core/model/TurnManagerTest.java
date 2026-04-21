package dev.nheggoe.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class TurnManagerTest {

  private TurnManager<TestPlayer> tm(List<String> names) {
    var players = names.stream().map(TestPlayer::new).toList();
    return new TurnManager<>(players);
  }

  @Test
  void constructor_setsCurrentAndIteratorAfterFirst() {
    var t = tm(List.of("A", "B", "C"));
    assertEquals("A", t.getCurrentPlayer().toString());
    // first next should advance to B (not repeat A)
    assertEquals("B", t.getNextPlayer().toString());
  }

  @Test
  void next_wrapsAndIncrementsRound() {
    var t = tm(List.of("A", "B"));
    assertEquals("A", t.getCurrentPlayer().toString());
    assertEquals(0, t.getRoundNumber());

    assertEquals("B", t.getNextPlayer().toString());
    // wrap
    assertEquals("A", t.getNextPlayer().toString());
    assertEquals(1, t.getRoundNumber());
  }

  @Test
  void remove_nonCurrent_keepsOrderAndIteratorAfterCurrent() {
    var t = tm(List.of("A", "B", "C"));
    assertEquals("A", t.getCurrentPlayer().toString());
    assertEquals("B", t.getNextPlayer().toString()); // current = B

    t.removePlayer(new TestPlayer("C"));
    assertEquals(List.of("A", "B"), t.getPlayers().stream().map(Object::toString).toList());

    // next should wrap to A and increase round
    assertEquals("A", t.getNextPlayer().toString());
    assertEquals(1, t.getRoundNumber());
  }

  @Test
  void remove_current_movesToNext() {
    var t = tm(List.of("A", "B", "C"));
    // current = A
    t.removePlayer(new TestPlayer("A"));

    // with A gone, current becomes B
    assertEquals("B", t.getCurrentPlayer().toString());

    // next should be C
    assertEquals("C", t.getNextPlayer().toString());
  }

  @Test
  void getPlayers_isUnmodifiable() {
    var t = tm(List.of("A", "B"));
    var list = t.getPlayers();
    assertThrows(UnsupportedOperationException.class, () -> list.add(new TestPlayer("X")));
  }

  @Test
  void removingToEmpty_listLeavesManagerConsistent() {
    var t = tm(List.of("A"));
    t.removePlayer(new TestPlayer("A"));
    assertNull(t.getCurrentPlayer());
    assertTrue(t.getPlayers().isEmpty());
  }

  @Test
  void constructor_rejectsEmptyList() {
    assertThrows(IllegalArgumentException.class, () -> new TurnManager<TestPlayer>(List.of()));
  }
}
