package dev.nheggoe.boardgame.snake.model;

import dev.nheggoe.boardgame.core.io.csv.CSVReader;
import dev.nheggoe.boardgame.snake.model.tile.LadderTile;
import dev.nheggoe.boardgame.snake.model.tile.NormalTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeTile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Factory for creating Snake and Ladder boards from external files or data structures.
 *
 * @author Nick Heggø, Mihailo Hranisavljevic
 * @version 2025.05.22
 */
public class SnakeAndLadderBoardFactory {

  private SnakeAndLadderBoardFactory() {}

  /**
   * Returns a standard board from the default file.
   *
   * @return a new SnakeAndLadderBoard
   * @throws IllegalStateException if the file fails to load or parse
   */
  public static SnakeAndLadderBoard createBoard() {
    return createBoardFromCsv(Path.of("data/csv/snakeAndLadder.csv"));
  }

  /**
   * Reads a CSV file and constructs a {@link SnakeAndLadderBoard} from its rows.
   *
   * <p>Automatically detects and skips a header row if it begins with "Index,". Blank lines are
   * ignored. Each data row must have exactly three comma-separated fields: index, tile type
   * ("SNAKE", "LADDER" or "NORMAL"), and offset.
   *
   * @param csvFile the CSV file to read; may include a header line and blank lines
   * @return a newly created {@code SnakeAndLadderBoard} containing one tile per row
   * @throws IllegalStateException if the file is empty, cannot be read, or any data row does not
   *     have exactly three columns or contains invalid values
   */
  public static SnakeAndLadderBoard createBoardFromCsv(Path csvFile) {
    try {
      List<String[]> lines = CSVReader.readAll(csvFile);
      if (lines.isEmpty()) {
        throw new IllegalStateException("Board file is empty: " + csvFile);
      }
      verifyCsvStructure(lines);
      List<SnakeAndLadderTile> tiles =
          lines.stream().map(SnakeAndLadderBoardFactory::createTile).toList();

      return new SnakeAndLadderBoard(tiles);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read board file at " + csvFile, e);
    }
  }

  /**
   * Ensures each CSV data row has exactly 3 columns.
   *
   * @param lines raw CSV input including header
   * @throws IllegalStateException if the column count is invalid
   */
  private static void verifyCsvStructure(List<String[]> lines) {
    for (int i = 0; i < lines.size(); i++) {
      var tokens = lines.get(i);
      if (tokens.length != 2) {
        throw new IllegalStateException(
            "Invalid board file format at line %d: each row must have exactly 2 columns (TileType, Data)."
                .formatted(i + 1));
      }
    }
  }

  /**
   * Instantiates a tile based on its row data.
   *
   * @param tokens the CSV line
   * @return a tile instance
   * @throws IllegalStateException on malformed input
   */
  private static SnakeAndLadderTile createTile(String[] tokens) {
    try {
      // TileType, Data
      //    0       1
      String type = tokens[0];
      int offset = tokens[1].isEmpty() ? 0 : Integer.parseInt(tokens[1]);
      return switch (type.toUpperCase()) {
        case "SNAKE" -> new SnakeTile(offset);
        case "LADDER" -> new LadderTile(offset);
        case "NORMAL" -> new NormalTile();
        default -> throw new IllegalStateException("Invalid tile type: " + type);
      };
    } catch (NumberFormatException ignored) {
      throw new IllegalStateException("Invalid CSV value: " + Arrays.toString(tokens));
    }
  }
}
