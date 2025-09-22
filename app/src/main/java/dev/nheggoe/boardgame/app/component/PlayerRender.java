package dev.nheggoe.boardgame.app.component;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.app.ui.EventListeningComponent;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.UnhandledEventException;
import dev.nheggoe.boardgame.core.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Responsible for rendering and animating the players on the Snake and Ladder board.
 *
 * <p>Listens to PlayerMoved events and updates player visuals using the FigureAnimator. Maintains
 * the position and figure state of each player, ensuring consistent animation and synchronisation
 * with the game model.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.05.23
 */
public class PlayerRender extends EventListeningComponent {

  private final Supplier<List<SnakeAndLadderPlayer>> players;
  private final GridPane tileGrid;
  private final int gridSize;
  private final FigureAnimator animator;

  private static class RenderState {
    int visualIndex = -1;
    ImageView icon;
  }

  private final Map<SnakeAndLadderPlayer, RenderState> renderStates = new HashMap<>();

  /**
   * Constructs a PlayerRender responsible for updating player visuals on the board.
   *
   * @param eventBus global event dispatcher
   * @param tileGrid grid containing board tiles
   * @param boardDimension square root of tile count, used to calculate tile position
   * @param players supplier providing the list of players
   * @param animationLayer transparent layer used to draw animated icons
   */
  public PlayerRender(
      EventBus eventBus,
      GridPane tileGrid,
      int boardDimension,
      Supplier<List<SnakeAndLadderPlayer>> players,
      Pane animationLayer) {
    super(eventBus, CoreEvent.PlayerMoved.class);
    this.tileGrid = tileGrid;
    this.gridSize = boardDimension;
    this.players = requireNonNull(players);
    this.animator = new FigureAnimator(animationLayer);
    Platform.runLater(this::renderAll);
  }

  /**
   * Responds to PlayerMoved events by re-rendering all player icons.
   *
   * @param event event indicating a player has moved
   */
  @Override
  public void onEvent(Event event) {
    if (requireNonNull(event) instanceof CoreEvent.PlayerMoved) {
      Platform.runLater(this::renderAll);
    } else {
      throw new UnhandledEventException(event);
    }
  }

  /**
   * Renders all players according to their current positions. Calculates player offsets for correct
   * stacking when multiple players occupy the same tile.
   */
  private void renderAll() {
    Map<Integer, List<SnakeAndLadderPlayer>> tileOccupants = new HashMap<>();
    for (SnakeAndLadderPlayer p : players.get()) {
      tileOccupants.computeIfAbsent(p.getPosition(), k -> new java.util.ArrayList<>()).add(p);
    }

    for (SnakeAndLadderPlayer player : players.get()) {
      int newIndex = player.getPosition();
      StackPane targetTile = tileAtIndex(newIndex);
      List<SnakeAndLadderPlayer> occupants = tileOccupants.get(newIndex);
      int offsetIndex = occupants.indexOf(player);

      RenderState state =
          renderStates.computeIfAbsent(
              player,
              p -> {
                RenderState s = new RenderState();
                s.icon = createFigureVisual(p.getFigure());
                animator.place(s.icon, targetTile, offsetIndex);
                s.visualIndex = player.getPosition();
                return s;
              });

      if (state.visualIndex != newIndex) {
        StackPane oldTile = tileAtIndex(state.visualIndex);
        animator.animate(state.icon, oldTile, targetTile, offsetIndex);
        state.visualIndex = newIndex;
      }
    }
  }

  /**
   * Converts a 1-based tile index to a StackPane in the grid.
   *
   * @param index tile number (1-based)
   * @return the StackPane at the tile's grid coordinates
   */
  private StackPane tileAtIndex(int index) {
    if (index < 1 || index > gridSize * gridSize) return tileAt(0, 0);
    var pos = SnakeBoardLayout.toGrid(index, gridSize);
    return tileAt((int) pos.getY(), (int) pos.getX());
  }

  /**
   * Retrieves the tile StackPane at the given grid coordinates.
   *
   * @param row row index
   * @param col column index
   * @return tile node at the specified coordinates
   * @throws IllegalArgumentException if no node exists at the given location
   */
  private StackPane tileAt(int row, int col) {
    for (var node : tileGrid.getChildren()) {
      int c = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
      int r = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
      if (c == col && r == row) return (StackPane) node;
    }
    throw new IllegalArgumentException("Tile not found at (" + row + ',' + col + ')');
  }

  /**
   * Creates a graphical icon representing the player's figure. Uses image resource if found,
   * otherwise renders a default coloured square.
   *
   * @param figure the enum describing the player's figure type
   * @return an ImageView representing the figure
   */
  private ImageView createFigureVisual(Player.Figure figure) {
    String fileName = figure.name().toLowerCase().replace("_", "");
    String resourcePath = "images/" + fileName + ".png";
    InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
    if (is != null) {
      Image img = new Image(is, 27, 27, true, true);
      ImageView view = new ImageView(img);
      view.setUserData(figure);
      return view;
    }

    WritableImage tiny = new WritableImage(1, 1);
    tiny.getPixelWriter().setColor(0, 0, Color.DODGERBLUE);
    ImageView fallback = new ImageView(tiny);
    fallback.setFitWidth(18);
    fallback.setFitHeight(18);
    fallback.setPreserveRatio(false);
    fallback.setUserData(figure);
    return fallback;
  }
}
