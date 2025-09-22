package dev.nheggoe.boardgame.core.model;

import dev.nheggoe.boardgame.core.util.StringFormatter;

final class TestPlayer extends Player {

  TestPlayer(String name) {
    super(name, Figure.CAR);
  }

  @Override
  public String toString() {
    return "{name: %s, figure: %s".formatted(getName(), StringFormatter.formatEnum(getFigure()));
  }
}
