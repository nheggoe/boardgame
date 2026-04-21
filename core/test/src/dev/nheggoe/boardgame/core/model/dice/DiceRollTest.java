package dev.nheggoe.boardgame.core.model.dice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DiceRollTest {

  @Test
  void testDefensiveCopy() {
    int[] numbers = {1, 2, 3};
    DiceRoll dr = new DiceRoll(numbers);
    assertThat(numbers).isEqualTo(dr.rolls());

    numbers[2] = 4;
    assertThat(numbers).isNotEqualTo(dr.rolls());
  }

  @Test
  void testRollOneDice() {
    var diceRoll = Dice.roll(1);
    assertThatThrownBy(diceRoll::areDiceEqual).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testIdenticalRolls() {
    assertThat(new DiceRoll(1, 1, 1).areDiceEqual()).isTrue();
    assertThat(new DiceRoll(2, 2, 2, 2, 2, 2, 2, 2).areDiceEqual()).isTrue();
    assertThat(new DiceRoll(1, 3, 4, 5).areDiceEqual()).isFalse();
  }

  @Test
  void testSum() {
    assertThat(new DiceRoll(2, 4, 6).getTotal()).isEqualTo(12);
  }

  @Test
  void testInvalidInput() {
    assertThatThrownBy(() -> new DiceRoll(0, 1, 2))
        .withFailMessage("Face value of dice cannot be less than 1")
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new DiceRoll(1, 3, 7))
        .withFailMessage("Face value of dice cannot exceed 6")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testToString() {
    assertThat(new DiceRoll(1, 2, 3)).hasToString("1, 2, 3 (6)");
    assertThat(new DiceRoll(1, 1)).hasToString("2 equal dice of 1! (2)");
  }

  @Test
  void test_invalid_dice_roll() {
    assertThatThrownBy(DiceRoll::new)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Dice roll must contain at least one dice");
  }
}
