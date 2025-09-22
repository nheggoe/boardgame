package dev.nheggoe.boardgame.monopoly.board;

import static org.assertj.core.api.Assertions.*;

import dev.nheggoe.boardgame.monopoly.ownable.Property;
import dev.nheggoe.boardgame.monopoly.tile.CornerMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.FreeParkingMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.GoToJailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.JailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.MonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.OwnableMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.StartMonopolyTile;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonopolyBoardTest {

  private MonopolyBoard board;

  @BeforeEach
  void setUp() {
    var tiles = new ArrayList<MonopolyTile>();
    tiles.add(new StartMonopolyTile(CornerMonopolyTile.Position.BOTTOM_RIGHT));
    tiles.add(new JailMonopolyTile(CornerMonopolyTile.Position.BOTTOM_LEFT));
    tiles.add(new FreeParkingMonopolyTile(CornerMonopolyTile.Position.TOP_LEFT));
    tiles.add(new GoToJailMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT));
    tiles.add(new OwnableMonopolyTile(new Property("Test property", Property.Color.DARK_BLUE, 20)));
    tiles.add(new OwnableMonopolyTile(new Property("Test property", Property.Color.BROWN, 40)));
    tiles.add(new OwnableMonopolyTile(new Property("Test property", Property.Color.RED, 60)));
    tiles.add(
        new OwnableMonopolyTile(new Property("Test property", Property.Color.LIGHT_BLUE, 80)));

    var copy = List.copyOf(tiles);
    board = new MonopolyBoard(tiles);

    assertThat(tiles).withFailMessage("Board should not change the original list").isEqualTo(copy);
  }

  @Test
  void test_basic() {
    assertThat(board.tiles()).hasSize(8);
  }
}
