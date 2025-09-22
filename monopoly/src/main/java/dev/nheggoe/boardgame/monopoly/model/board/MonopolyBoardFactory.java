package dev.nheggoe.boardgame.monopoly.model.board;

import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.TileFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code BoardGameFactory} class is a factory class that creates different types of boards. It
 * also is able to read a board from a file.
 *
 * @author Mihailo Hranisavljevic and Nick Heggø
 * @version 2025.04.22
 */
public class MonopolyBoardFactory {

  private MonopolyBoardFactory() {}

  /**
   * Generates a Monopoly game board based on the specified layout.
   *
   * @param layout the layout configuration for the board, which determines its structure
   * @return a {@link MonopolyBoard} instance created according to the specified layout
   */
  public static MonopolyBoard generateBoard(MonopolyBoard.Layout layout) {
    return switch (layout) {
      case NORMAL, EASY, UNFAIR -> generateNormalBoard();
    };
  }

  private static MonopolyBoard generateNormalBoard() {
    var tmp = TileFactory.generateOwnableTiles();
    List<MonopolyTile> tiles = new ArrayList<>(tmp);
    tiles.addAll(TileFactory.generateCornerTiles());
    Collections.shuffle(tiles);
    return new MonopolyBoard(tiles);
  }
}
