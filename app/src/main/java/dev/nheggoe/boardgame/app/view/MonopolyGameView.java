package dev.nheggoe.boardgame.app.view;

import dev.nheggoe.boardgame.app.component.DiceView;
import dev.nheggoe.boardgame.app.component.MonopolyBoardView;
import dev.nheggoe.boardgame.app.ui.GameView;
import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import java.util.List;
import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Represents the view for the Monopoly game, extending the generic {@code GameView} class to
 * include Monopoly-specific components such as the game board, dice, and Monopoly tiles and
 * players.
 *
 * <p>This class defines the layout of the Monopoly game interface, including a center pane
 * containing the background, board, and dice views. It also manages the initialization of necessary
 * UI elements specific to the Monopoly game.
 */
public class MonopolyGameView extends GameView<MonopolyTile, MonopolyPlayer> {

  /**
   * Constructs a new instance of the {@code MonopolyGameView} class, which serves as the view of
   * the Monopoly game. This class is responsible for managing the game-specific UI elements such as
   * the Monopoly board and dice.
   *
   * @param sceneSwitcher the {@link SceneSwitcher} instance responsible for handling scene
   *     transitions; must not be {@code null}
   * @param eventBus the {@link EventBus} used for communication and event handling between
   *     components; must not be {@code null}
   * @param tilesSupplier a {@link Supplier} that provides a list of {@link MonopolyTile} objects
   *     representing the game board tiles; must not be {@code null}
   * @param playersSupplier a {@link Supplier} that provides a list of {@link MonopolyPlayer}
   *     objects representing the game's players; must not be {@code null}
   * @param rollDiceHandler the {@link EventHandler} for handling dice roll actions triggered within
   *     the game; must not be {@code null}
   */
  public MonopolyGameView(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      Supplier<List<MonopolyTile>> tilesSupplier,
      Supplier<List<MonopolyPlayer>> playersSupplier,
      EventHandler<ActionEvent> rollDiceHandler) {
    super(sceneSwitcher, eventBus, tilesSupplier, playersSupplier, rollDiceHandler);
  }

  @Override
  protected Pane createCenterPane(
      EventBus eventBus,
      Supplier<List<MonopolyTile>> tiles,
      Supplier<List<MonopolyPlayer>> players) {
    var image = new Image("/images/monopoly-background.png");
    var imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(600);
    imageView.setFitWidth(800);
    var center = new StackPane();
    center.getChildren().add(imageView);
    center.prefWidthProperty().bind(this.widthProperty().multiply(0.6));
    center.prefHeightProperty().bind(this.heightProperty().multiply(0.6));

    var backGround = new Background(new BackgroundFill(Color.BLACK, null, null));
    center.setBackground(backGround);

    var monopolyBoardView = new MonopolyBoardView(players, tiles);
    var diceView = new DiceView();
    addComponents(monopolyBoardView, diceView);
    center.getChildren().addAll(monopolyBoardView, diceView);
    return center;
  }

  /**
   * Retrieves the dice view component.
   *
   * @return the {@link DiceView} instance
   */
  public DiceView getDiceView() {
    return (DiceView)
        getComponents().stream().filter(c -> c instanceof DiceView).findFirst().orElseThrow();
  }

  /**
   * Retrieves the monopoly board view component.
   *
   * @return the {@link MonopolyBoardView} instance
   */
  public MonopolyBoardView getMonopolyBoardView() {
    return (MonopolyBoardView)
        getComponents().stream()
            .filter(c -> c instanceof MonopolyBoardView)
            .findFirst()
            .orElseThrow();
  }
}
