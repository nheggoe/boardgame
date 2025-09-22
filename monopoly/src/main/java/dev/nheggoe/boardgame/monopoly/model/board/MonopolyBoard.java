package dev.nheggoe.boardgame.monopoly.model.board;

import dev.nheggoe.boardgame.core.model.Board;
import dev.nheggoe.boardgame.core.model.IllegalTilePositionException;
import dev.nheggoe.boardgame.core.model.InvalidBoardLayoutException;
import dev.nheggoe.boardgame.monopoly.model.tile.CornerMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.JailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.StartMonopolyTile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a game board consisting of a list of tiles. Each tile has a unique position and can
 * have specific actions that are triggered when a player lands on them. Provides methods to
 * interact with and retrieve tiles on the board.
 *
 * <p>This class is immutable; once created, the tiles cannot be modified.
 *
 * @author Mihailo Hranisavljevic and Nick Heggø
 * @version 2025.04.22
 */
public record MonopolyBoard(List<MonopolyTile> tiles) implements Board<MonopolyTile> {

  /**
   * Represents the Monopoly game board, which consists of a sequence of tiles arranged in a
   * specific layout. The constructor initializes the board, validates its layout, and arranges the
   * tiles in the appropriate configuration around the corners. The layout must meet the required
   * structural and logical constraints.
   *
   * @param tiles the list of {@link MonopolyTile} objects that make up the board. It is assumed
   *     that this list includes all necessary tiles (e.g., corner tiles, ownable tiles) and adheres
   *     to the game's layout rules. The tiles are validated and rearranged to ensure proper
   *     alignment before being stored in the board.
   * @throws InvalidBoardLayoutException if the provided tiles do not constitute a valid board
   *     layout (e.g., missing corners, incorrect number of tiles).
   */
  public MonopolyBoard {
    assertValidLayout(tiles);
    tiles = List.copyOf(alignTilesAroundCorners(tiles));
  }

  /**
   * Calculates the tile a player lands on after moving a specified number of steps from the
   * provided starting tile.
   *
   * @param currentPosition the starting {@link MonopolyTile} from which the steps are calculated
   * @param steps the number of steps to move forward from the starting tile
   * @return the {@link MonopolyTile} the player lands on after moving the specified number of steps
   */
  public MonopolyTile getTileAfterSteps(int currentPosition, int steps) {
    if (currentPosition < 0) {
      throw new IllegalTilePositionException();
    }
    int nextIndex = (currentPosition + steps) % tiles.size();
    return tiles.get(nextIndex);
  }

  /**
   * Get the starting position for the game, which is used to place players on the starting point.
   *
   * @return the staring point of the game
   */
  public StartMonopolyTile getStartingTile() {
    return tiles.stream()
        .filter(StartMonopolyTile.class::isInstance)
        .map(StartMonopolyTile.class::cast)
        .findAny()
        .orElseThrow(InvalidBoardLayoutException::new);
  }

  /**
   * Retrieves the jail tile from the board. This method searches through all tiles to find a tile
   * that is an instance of {@link JailMonopolyTile}. If no jail tile is present, an exception is
   * thrown.
   *
   * @return the {@link JailMonopolyTile} object representing the jail tile on the board
   * @throws InvalidBoardLayoutException if no jail tile is found on the board
   */
  public JailMonopolyTile getJailTile() {
    return tiles.stream()
        .filter(JailMonopolyTile.class::isInstance)
        .map(JailMonopolyTile.class::cast)
        .findAny()
        .orElseThrow(InvalidBoardLayoutException::new);
  }

  /**
   * Returns the total number of tiles on the Monopoly board.
   *
   * @return the number of tiles on the board
   */
  public int size() {
    return tiles.size();
  }

  /**
   * Retrieves the position of the specified tile on the board.
   *
   * @param tile the {@link MonopolyTile} whose position is to be determined
   * @return the zero-based position of the tile on the board, or -1 if the tile is not found
   */
  public int getTilePosition(MonopolyTile tile) {
    return tiles.indexOf(tile);
  }

  /**
   * Retrieves a tile at the provided position.
   *
   * @param position the position of the tile to retrieve
   * @return the {@link MonopolyTile} object at the specified position
   * @throws IndexOutOfBoundsException if the position is out of bounds
   */
  public MonopolyTile getTileAtIndex(int position) {
    return tiles.get(position);
  }

  /**
   * Returns the total number of tiles.
   *
   * @return the number of tiles
   */
  public int getNumberOfTiles() {
    return tiles.size();
  }

  /**
   * The {@code Layout} enumeration defines the possible configurations or structures for a Monopoly
   * board. This enum is designed to categorize different types of board setups that can be utilized
   * in a game of Monopoly.
   *
   * <p>Each enum constant represents a unique board layout: - {@code NORMAL}: Represents the
   * standard or default board layout. - {@code UNFAIR}: Represents a layout with potentially
   * imbalanced features or setups. - {@code EASY}: Represents a simplified or beginner-friendly
   * board layout.
   *
   * <p>This enum is used in conjunction with the MonopolyBoardFactory class to specify the desired
   * board layout when generating a Monopoly board. It allows for clear and concise representation
   * of the board configuration without requiring additional parameters or metadata.
   */
  public enum Layout {
    NORMAL,
    UNFAIR,
    EASY
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof MonopolyBoard(List<MonopolyTile> tiles1))) {
      return false;
    }
    return tiles.equals(tiles1);
  }

  @Override
  public int hashCode() {
    return tiles.hashCode();
  }

  // ------------------------  validations  ------------------------

  private List<MonopolyTile> alignTilesAroundCorners(List<MonopolyTile> tiles) {
    tiles = new ArrayList<>(tiles);
    List<CornerMonopolyTile> cornerTiles =
        tiles.stream()
            .filter(CornerMonopolyTile.class::isInstance)
            .map(CornerMonopolyTile.class::cast)
            .toList();

    var topLeft = findCornerTileByPosition(cornerTiles, CornerMonopolyTile.Position.TOP_LEFT);
    var topRight = findCornerTileByPosition(cornerTiles, CornerMonopolyTile.Position.TOP_RIGHT);
    var bottomLeft = findCornerTileByPosition(cornerTiles, CornerMonopolyTile.Position.BOTTOM_LEFT);
    var bottomRight =
        findCornerTileByPosition(cornerTiles, CornerMonopolyTile.Position.BOTTOM_RIGHT);

    var orderedTiles = List.of(bottomRight, bottomLeft, topLeft, topRight);

    int tilesPerSide = tiles.size() / 4;

    int i = 0;
    for (var tile : orderedTiles) {
      Collections.swap(tiles, i, tiles.indexOf(tile));
      i += tilesPerSide;
    }

    return tiles;
  }

  private CornerMonopolyTile findCornerTileByPosition(
      List<CornerMonopolyTile> tiles, CornerMonopolyTile.Position position) {
    return tiles.stream().filter(tile -> tile.getPosition() == position).findAny().orElseThrow();
  }

  private void assertValidLayout(List<MonopolyTile> tiles) {
    var cornerTiles =
        tiles.stream()
            .filter(CornerMonopolyTile.class::isInstance)
            .map(CornerMonopolyTile.class::cast)
            .toList();
    assertCornerTiles(cornerTiles);
    assertLayoutShape(tiles);
  }

  private void assertLayoutShape(List<MonopolyTile> tiles) {
    int tilesPerSide = tiles.size() - 4;
    if ((tilesPerSide % 4) != 0) {
      throw new InvalidBoardLayoutException(
          "The board must be a square shape! There are currently %.2f tiles per side."
              .formatted(tilesPerSide / 4.0));
    }
  }

  private void assertCornerTiles(List<CornerMonopolyTile> tiles) {
    // assert there are four corners
    if (tiles.size() != 4) {
      throw new InvalidBoardLayoutException("A valid board layout will need 4 corner tiles!");
    }

    // assert there are one tile of each type
    var sortByType = new HashMap<Class<?>, Integer>();
    for (var tile : tiles) {
      sortByType.merge(tile.getClass(), 1, Integer::sum);
    }
    if (sortByType.size() != 4) {
      StringBuilder sb = new StringBuilder();
      sortByType.forEach(
          (key, value) -> {
            if (value > 1) {
              sb.append("\n").append(key.getSimpleName()).append("=").append(value);
            }
          });
      throw new InvalidBoardLayoutException("Duplicated corner tile types!" + sb);
    }

    // assert there are one tile per corner
    var sortByPositon =
        new EnumMap<CornerMonopolyTile.Position, List<CornerMonopolyTile>>(
            CornerMonopolyTile.Position.class);
    for (var tile : tiles) {
      sortByPositon.computeIfAbsent(tile.getPosition(), k -> new ArrayList<>()).add(tile);
    }
    if (sortByPositon.size() != 4) {
      String errorMessage = constructErrorMessage(sortByPositon);
      throw new InvalidBoardLayoutException("Missing tiles on all four corners!" + errorMessage);
    }
  }

  private static <K, V> String constructErrorMessage(Map<K, List<V>> map) {
    StringBuilder sb = new StringBuilder();
    for (var mapEntry : map.entrySet()) {
      if (mapEntry.getValue().size() > 1) {
        sb.append("\n");
        if (mapEntry.getKey() instanceof Class<?> c) {
          sb.append(c.getSimpleName());
        } else {
          sb.append(mapEntry.getKey());
        }
        sb.append("=[");
        mapEntry.getValue().forEach(tile -> sb.append(tile.getClass().getSimpleName()).append(","));
        sb.delete(sb.length() - 1, sb.length()).append("]");
      }
    }
    return sb.toString();
  }
}
