package dev.nheggoe.boardgame.app.view;

import dev.nheggoe.boardgame.app.component.PlayerRender;
import dev.nheggoe.boardgame.app.component.SnakeAndLadderBoardRender;
import dev.nheggoe.boardgame.app.ui.GameView;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import dev.nheggoe.boardgame.snake.model.tile.SnakeAndLadderTile;
import java.util.List;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * JavaFX view for the Snake & Ladder game. Displays the board and player icons and manages
 * interaction with the underlying game engine.
 *
 * @author Nick Heggø, Mihailo Hranisavljevic
 * @version 2025.05.21
 */
public class SnakeGameView extends GameView<SnakeAndLadderTile, SnakeAndLadderPlayer> {

  /**
   * Constructs the SnakeGameView with necessary suppliers and event handlers.
   *
   * @param sceneSwitcher handles switching between different JavaFX scenes
   * @param eventBus the global event bus for dispatching and receiving game events
   * @param tilesSupplier supplies the list of board tiles
   * @param playersSupplier supplies the list of game players
   * @param rollDiceAction the event handler for rolling the dice
   */
  public SnakeGameView(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      Supplier<List<SnakeAndLadderTile>> tilesSupplier,
      Supplier<List<SnakeAndLadderPlayer>> playersSupplier,
      EventHandler<ActionEvent> rollDiceAction) {
    super(sceneSwitcher, eventBus, tilesSupplier, playersSupplier, rollDiceAction);
  }

  /**
   * Creates and returns the centre pane of the game view, containing the board and players.
   *
   * @param eventBus the event bus
   * @param tiles supplier of game tiles
   * @param playersSupplier supplier of game players
   * @return the constructed JavaFX Pane
   */
  @Override
  protected Pane createCenterPane(
      EventBus eventBus,
      Supplier<List<SnakeAndLadderTile>> tiles,
      Supplier<List<SnakeAndLadderPlayer>> playersSupplier) {

    var boardRender = new SnakeAndLadderBoardRender(eventBus, tiles);
    var tileGrid = boardRender.getTileGrid();
    var gridSize = boardRender.getGridSize();

    var animationLayer = new Pane();
    animationLayer.setPickOnBounds(false);
    animationLayer.setMouseTransparent(true);

    tileGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    animationLayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    animationLayer.prefWidthProperty().bind(tileGrid.widthProperty());
    animationLayer.prefHeightProperty().bind(tileGrid.heightProperty());

    var layeredBoard = new StackPane(tileGrid, animationLayer);
    layeredBoard.setAlignment(Pos.CENTER);
    var playerRender = new PlayerRender(tileGrid, gridSize, playersSupplier, animationLayer);

    addComponents(boardRender, playerRender);

    var container = new VBox();
    container.setAlignment(Pos.CENTER);
    container.getChildren().addAll(layeredBoard);

    return container;
  }

  /**
   * Retrieves the player render component.
   *
   * @return the {@link PlayerRender} instance
   */
  public PlayerRender getPlayerRender() {
    return (PlayerRender)
        getComponents().stream().filter(c -> c instanceof PlayerRender).findFirst().orElseThrow();
  }
}
