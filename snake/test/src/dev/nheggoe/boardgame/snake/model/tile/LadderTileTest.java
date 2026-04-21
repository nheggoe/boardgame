package dev.nheggoe.boardgame.snake.model.tile;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LadderTileTest {

  @Test
  void test_basic() {
    var ladderTile = new LadderTile(1);

    assertThat(ladderTile.tilesToSkip()).isEqualTo(1);
    assertThat(ladderTile).hasToString("LadderTile[tilesToSkip=1]");

    var ladderTile2 = new LadderTile(1);
    assertThat(ladderTile).isEqualTo(ladderTile2).hasSameClassAs(ladderTile2);

    var ladderTile3 = new LadderTile(2);
    assertThat(ladderTile).isNotEqualTo(ladderTile3).doesNotHaveSameHashCodeAs(ladderTile3);
  }

  @Test
  void test_invalid_data() {
    assertThatThrownBy(() -> new LadderTile(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Tiles to skip must be non-zero");

    assertThatThrownBy(() -> new LadderTile(-1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Tiles to skip must be non-negative");

    assertThatThrownBy(() -> new LadderTile(999))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Tiles to skip exceeds maximum of 100");
  }
}
