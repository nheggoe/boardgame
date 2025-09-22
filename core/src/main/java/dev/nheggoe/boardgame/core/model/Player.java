package dev.nheggoe.boardgame.core.model;

import dev.nheggoe.boardgame.core.util.StringFormatter;
import java.util.UUID;

/**
 * The {@code Player} class represents a player in the board game. Each player has a name and a
 * current tile position on the board.
 *
 * @author Mihailo Hranisavljevic and Nick Heggø
 * @version 2025.05.14
 */
public abstract class Player {

  // player info
  private final UUID id;
  private String name;
  private Figure figure;

  // game state
  private int position;

  /**
   * Constructs a new player with the specified name and places them at the start tile (position 0)
   * of the board.
   *
   * @param name the name of the player
   * @param figure the figure player has chosen to play as
   */
  protected Player(String name, Figure figure) {
    id = UUID.randomUUID();
    setName(name);
    setFigure(figure);
  }

  // ------------------------  getters and setters  ------------------------

  public String getName() {
    return name;
  }

  /**
   * Sets the name of the player. The name cannot be null or blank.
   *
   * @param name the name to set for the player
   * @throws IllegalArgumentException if the provided name is null or blank
   */
  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be empty!");
    }
    this.name = name;
  }

  /**
   * Returns the figure of the player.
   *
   * @return the figure of the player
   */
  public Figure getFigure() {
    return figure;
  }

  /**
   * Sets the figure of the player. The figure cannot be null.
   *
   * @param figure the figure to assign to the player
   * @throws IllegalArgumentException if the provided figure is null
   */
  public void setFigure(Figure figure) {
    if (figure == null) {
      throw new IllegalArgumentException("Invalid figure, please try again.");
    }
    this.figure = figure;
  }

  /**
   * Returns the current position of the player on the board.
   *
   * @return the current position of the player
   */
  public int getPosition() {
    return position;
  }

  /**
   * Sets the current position of the player on the board. The position must not be negative.
   *
   * @param position the new position to set for the player
   * @throws IllegalArgumentException if the provided position is negative
   */
  public void setPosition(int position) {
    if (position < 0) {
      throw new IllegalArgumentException("Positon cannot be negative!");
    }
    this.position = position;
  }

  public UUID getId() {
    return id;
  }

  /**
   * Represents a collection of predefined figures that players can choose from in a game.
   *
   * <p>Each enum constant corresponds to a unique figure, such as a battleship or a hat, that a
   * player can use as their playing piece on the board.
   */
  public enum Figure {
    BATTLE_SHIP,
    CAR,
    CAT,
    DUCK,
    HAT
  }

  @Override
  public String toString() {
    return "%s[figure=%s, position=%d]"
        .formatted(getName(), StringFormatter.formatEnum(getFigure()), getPosition());
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Player player)) {
      return false;
    }
    return id.equals(player.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Converts a player's name and figure into a line formatted as a CSV entry. The resulting format
   * will be "name,figure" and elements are split into an array.
   *
   * @param player the player whose name and figure will be converted to a CSV line
   * @return an array of strings containing the player's name and figure as CSV fields
   * @throws NullPointerException if the provided player is null
   */
  public static String[] toCsvLine(Player player) {
    return "%s,%s".formatted(player.getName(), player.getFigure()).split(",", -1);
  }
}
