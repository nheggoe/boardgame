package dev.nheggoe.boardgame.core.common.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.util.StringFormatter;
import org.junit.jupiter.api.Test;

class StringFormatterTest {

  @Test
  void testAlgorithm() {
    assertThat(StringFormatter.formatEnum(Player.Figure.BATTLE_SHIP)).isEqualTo("Battle Ship");
  }
}
