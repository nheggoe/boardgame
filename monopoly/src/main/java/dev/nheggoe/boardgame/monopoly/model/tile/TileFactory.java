package dev.nheggoe.boardgame.monopoly.model.tile;

import dev.nheggoe.boardgame.core.io.csv.CSVReader;
import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;
import dev.nheggoe.boardgame.monopoly.model.ownable.Property;
import dev.nheggoe.boardgame.monopoly.model.ownable.Utility;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides factory methods for generating Monopoly game tiles. These tiles can represent both
 * ownable tiles such as properties and utilities, and special tiles like corner tiles.
 *
 * <p>This class is utility-based and should not be instantiated.
 */
public class TileFactory {

  private TileFactory() {}

  /**
   * Generates a list of ownable Monopoly tiles from a predefined CSV file. The CSV file is expected
   * to define tile attributes such as name, type, color, and cost. Tiles are created based on these
   * attributes using the `generateTilesFromCSV` method. In case of an I/O error when reading the
   * file, an empty list is returned.
   *
   * @return a list of {@link MonopolyTile}, representing ownable tiles such as properties and
   *     utilities
   */
  public static List<MonopolyTile> generateOwnableTiles() {
    try {
      return generateTilesFromCSV(CSVReader.readAll(Path.of("data/csv/monopoly.csv")), 4);
    } catch (IOException ignored) {
      return List.of();
    }
  }

  /**
   * Generates a list of predefined corner tiles for a Monopoly game board. These tiles occupy the
   * four corner positions of the board, with each representing a distinct feature or rule: "Free
   * Parking," "Go to Jail," "Jail," and "Start."
   *
   * @return a list of {@link CornerMonopolyTile} objects, each corresponding to a unique corner
   *     position on a Monopoly board: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, or BOTTOM_RIGHT
   */
  public static List<CornerMonopolyTile> generateCornerTiles() {
    return List.of(
        new FreeParkingMonopolyTile(CornerMonopolyTile.Position.TOP_LEFT),
        new GoToJailMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT),
        new JailMonopolyTile(CornerMonopolyTile.Position.BOTTOM_LEFT),
        new StartMonopolyTile(CornerMonopolyTile.Position.BOTTOM_RIGHT));
  }

  /**
   * Generates a list of {@link MonopolyTile} objects from data provided in CSV format. The method
   * reads a list of CSV lines, where each line contains attributes for a Monopoly tile, validates
   * the structure of each line against the expected column count, and creates {@link
   * OwnableMonopolyTile} objects for supported tile types (e.g., Property, Utility).
   *
   * @param lines the list of string arrays, where each array represents a CSV row containing
   *     information about a Monopoly tile. The first row (header) is ignored in processing.
   * @param expectedColumnCount the number of columns expected in the CSV data for each row, used
   *     for validation.
   * @return a list of {@link MonopolyTile} objects parsed and constructed from the provided CSV
   *     data.
   * @throws IllegalStateException if any CSV row does not match the expected column count.
   * @throws UnsupportedOperationException if an unsupported tile type is encountered in the data.
   */
  public static List<MonopolyTile> generateTilesFromCSV(
      List<String[]> lines, int expectedColumnCount) {
    List<MonopolyTile> monopolyTiles = new ArrayList<>();
    lines = lines.subList(1, lines.size());
    for (var tokens : lines) {
      // Name, Type, Color, Cost
      //   0,    1,    2,     3
      if (tokens.length != expectedColumnCount) {
        throw new IllegalStateException(
            "Row count doesn't match the expected result. Expected: %d, Actual :%d."
                .formatted(expectedColumnCount, tokens.length));
      }
      Ownable ownable =
          switch (tokens[1].toUpperCase()) {
            case "PROPERTY" ->
                new Property(
                    tokens[0],
                    Property.Color.valueOf(tokens[2].toUpperCase().replace(' ', '_')),
                    Integer.parseInt(tokens[3]));
            case "UTILITY" -> new Utility(tokens[0], Integer.parseInt(tokens[3]));
            default ->
                throw new UnsupportedOperationException(
                    "Unexpected value: " + tokens[1].toUpperCase());
          };

      monopolyTiles.add(new OwnableMonopolyTile(ownable));
    }

    return monopolyTiles;
  }
}
