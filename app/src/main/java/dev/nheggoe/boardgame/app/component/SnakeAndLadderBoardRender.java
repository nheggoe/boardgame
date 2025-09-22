package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.ui.Component;
import dev.nheggoe.boardgame.app.ui.EventListeningComponent;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.snake.model.tile.LadderTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import dev.nheggoe.boardgame.snake.model.tile.SnakeTile;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * JavaFX component responsible for rendering a static Snake & Ladder board.
 *
 * <p>This class paints the tiles (normal, snake, ladder) and tile numbers based on the model. The
 * board is rendered as a square whose side length is determined by taking the ceiling of the
 * square‐root of the number of tiles supplied.
 *
 * @author Nick Heggø, Mihailo Hranisavljevic
 * @version 2025.05.21
 */
public class SnakeAndLadderBoardRender extends Component {

  /** Width/height of a single tile in pixels. */
  private static final int TILE_SIZE = 60;

  /** Side length of the board in tiles. Assumes a square board. */
  private final int boardDimension;

  /** Supplies the full list of tiles to render. */
  private final Supplier<List<SnakeAndLadderTile>> tilesSupplier;

  /** Grid container holding the tile visuals. */
  private final GridPane tileGrid = new GridPane();

  /**
   * Constructs a new board renderer and draws the static tiles.
   *
   * <p>Computes {@code boardDimension} as {@code ceil(sqrt(tilesSupplier.get().size()))} so that
   * all tiles fit into a square.
   *
   * @param eventBus (unused here but required by {@link EventListeningComponent})
   * @param tilesSupplier a supplier returning the current list of tiles from the game model
   */
  public SnakeAndLadderBoardRender(
      EventBus eventBus, Supplier<List<SnakeAndLadderTile>> tilesSupplier) {
    setAlignment(Pos.CENTER);
    this.tilesSupplier = tilesSupplier;
    int tileCount = tilesSupplier.get().size();
    this.boardDimension = (int) Math.ceil(Math.sqrt(tileCount));

    buildStaticBoard();
    getChildren().add(new StackPane(tileGrid));
  }

  /**
   * Returns the grid structure that holds all tile visuals.
   *
   * @return tile grid as a {@link GridPane}
   */
  public GridPane getTileGrid() {
    return tileGrid;
  }

  /**
   * Returns the number of tiles per board side (assumes square).
   *
   * @return dimension length
   */
  public int getGridSize() {
    return boardDimension;
  }

  /**
   * Builds and lays out all tile panes in the grid.
   *
   * <p>Each tile is mapped to a position based on {@link SnakeBoardLayout}, looping from 1 to the
   * supplied tile count.
   */
  private void buildStaticBoard() {
    int totalTiles = tilesSupplier.get().size();
    for (int tile = 1; tile <= totalTiles; tile++) {
      Point2D p = SnakeBoardLayout.toGrid(tile, boardDimension);
      StackPane tilePane = createTileVisual(tile);
      tileGrid.add(tilePane, (int) p.getX(), (int) p.getY());
    }
  }

  /**
   * Creates a styled tile pane with a label and colour.
   *
   * @param tileNumber the tile's number on the board
   * @return the configured tile visual
   */
  private StackPane createTileVisual(int tileNumber) {

    StackPane pane = new StackPane();
    pane.setPrefSize(TILE_SIZE, TILE_SIZE);

    var label = new Label(String.valueOf(tileNumber));
    label.setStyle("-fx-font-size: 11;");
    StackPane.setAlignment(label, Pos.TOP_LEFT);
    pane.getChildren().add(label);

    var tiles = tilesSupplier.get();
    SnakeAndLadderTile modelTile = (tileNumber <= tiles.size()) ? tiles.get(tileNumber - 1) : null;
    pane.setStyle(tileStyle(modelTile));
    return pane;
  }

  /**
   * Returns the background style based on the tile type.
   *
   * @param tile the tile to determine visual style for
   * @return CSS style string
   */
  private String tileStyle(SnakeAndLadderTile tile) {
    if (tile instanceof SnakeTile) {
      return "-fx-border-color: black; -fx-background-color: lightcoral;";
    } else if (tile instanceof LadderTile) {
      return "-fx-border-color: black; -fx-background-color: lightgreen;";
    } else {
      return "-fx-border-color: black; -fx-background-color: lightyellow;";
    }
  }
}
