package dev.nheggoe.boardgame.core.model.dice;

import java.util.Random;

/**
 * Singleton class representing a die with two six-sided dice. Uses lazy initialization to ensure
 * only one instance is created when needed. Uses Singleton to ensure non-biased dice rolls.
 *
 * @author Mihailo Hranisavljevic and Nick Heggø
 * @version 2025.05.08
 */
public class Dice {

  private static final Random random = new Random();

  private Dice() {}

  /**
   * Simulates rolling the specified number of dice, each with a face value ranging from 1 to 6.
   *
   * @param numberOfDice the number of dice to roll; must be greater than or equal to 1
   * @return a {@link DiceRoll} object representing the results of the dice rolls
   * @throws IllegalArgumentException if {@code numberOfDice} is less than 1
   */
  public static DiceRoll roll(int numberOfDice) {
    int[] rolls = new int[numberOfDice];
    for (int i = 0; i < rolls.length; i++) {
      rolls[i] = random.nextInt(6) + 1;
    }
    return new DiceRoll(rolls);
  }
}
