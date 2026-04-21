package dev.nheggoe.boardgame.core.model.dice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class DiceTest {

  @Test
  void testRollDice() {
    for (int i = 0; i < 100; i++) {
      DiceRoll diceRoll = Dice.roll(2);
      assertThat(diceRoll.rolls()).hasSize(2);
      assertThat(diceRoll.getTotal()).isGreaterThanOrEqualTo(2);
      assertThat(diceRoll.getTotal()).isLessThanOrEqualTo(12);
    }
  }
}
