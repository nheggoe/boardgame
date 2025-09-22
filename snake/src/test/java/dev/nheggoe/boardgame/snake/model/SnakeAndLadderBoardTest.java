package dev.nheggoe.boardgame.snake.model;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import dev.nheggoe.boardgame.games.snake.model.tile.LadderTile;
import dev.nheggoe.boardgame.games.snake.model.tile.NormalTile;
import dev.nheggoe.boardgame.games.snake.model.tile.SnakeAndLadderTile;
import dev.nheggoe.boardgame.games.snake.model.tile.SnakeTile;
import dev.nheggoe.boardgame.monopoly.board.InvalidBoardLayoutException;
import java.util.ArrayList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SnakeAndLadderBoardTest {

  @Test
  void test_constructor_null() {
    var tiles = new ArrayList<SnakeAndLadderTile>();
    tiles.add(null);
    assertThatThrownBy(() -> new SnakeAndLadderBoard(tiles))
        .isInstanceOf(InvalidBoardLayoutException.class);
  }

  @Test
  void test_basic() {
    var tiles = new ArrayList<SnakeAndLadderTile>();
    for (int i = 0; i < 90; i++) {
      tiles.add(new NormalTile());
    }
    var board = new SnakeAndLadderBoard(tiles);
    assertThat(board.tiles()).hasSize(90).isEqualTo(tiles);
  }

  @Test
  void test_constructor_invalid_snakeTile() {
    var tiles = new ArrayList<SnakeAndLadderTile>();
    tiles.add(new NormalTile());
    tiles.add(new SnakeTile(2));
    assertThatThrownBy(() -> new SnakeAndLadderBoard(tiles))
        .isInstanceOf(InvalidBoardLayoutException.class);

    tiles.removeLast();
    tiles.add(new SnakeTile(1));
    assertThat(tiles).hasSize(2).filteredOn(SnakeTile.class::isInstance).hasSize(1);

    assertThatCode(() -> new SnakeAndLadderBoard(tiles)).doesNotThrowAnyException();
  }

  @Test
  @Disabled("LadderTile is not allowed to skip 0 tiles")
  void test_constructor_invalid_ladderTile() {
    var tiles = new ArrayList<SnakeAndLadderTile>();
    tiles.add(new LadderTile(1));
    assertThatThrownBy(() -> new SnakeAndLadderBoard(tiles))
        .isInstanceOf(InvalidBoardLayoutException.class);

    tiles.add(new NormalTile());
    assertThatCode(() -> new SnakeAndLadderBoard(tiles)).doesNotThrowAnyException();
  }

  @Test
  void test_retrieve_tiles() {
    var tiles = new ArrayList<SnakeAndLadderTile>();
    tiles.add(new NormalTile());
    tiles.add(new SnakeTile(1));
    tiles.add(new LadderTile(1));
    tiles.add(new NormalTile());
    var board = new SnakeAndLadderBoard(tiles);
    assertThat(board.getTileAtIndex(3)).isEqualTo(tiles.get(3));
    assertThat(board.size()).isEqualTo(tiles.size());
    assertThatThrownBy(() -> board.getTileAtIndex(90))
        .isInstanceOf(IndexOutOfBoundsException.class);
    assertThatThrownBy(() -> board.getTileAtIndex(-1))
        .isInstanceOf(IndexOutOfBoundsException.class);
  }
}
