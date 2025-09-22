package dev.nheggoe.boardgame.app.component;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Handles smooth animated movement of player icons on the Snake & Ladder board.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.05.23
 */
public class FigureAnimator {

  private final Pane animationLayer;

  /**
   * Constructs an animator that draws onto the given animation layer.
   *
   * @param animationLayer the transparent pane layered over the board
   */
  public FigureAnimator(Pane animationLayer) {
    this.animationLayer = animationLayer;
  }

  /**
   * Immediately places the given icon at the centre of the target tile, applying a visual offset
   * based on index to avoid overlap with other player icons.
   *
   * @param icon the visual node representing a player
   * @param targetTile the destination tile node
   * @param index placement index among players on the tile
   */
  public void place(Node icon, Node targetTile, int index) {
    Bounds bounds = targetTile.localToScene(targetTile.getBoundsInLocal());
    Point2D base = animationLayer.sceneToLocal(bounds.getMinX(), bounds.getMinY());
    Point2D offset = offsetForIndex(index);

    icon.setLayoutX(base.getX() + offset.getX());
    icon.setLayoutY(base.getY() + offset.getY());

    if (!animationLayer.getChildren().contains(icon)) {
      animationLayer.getChildren().add(icon);
    }
  }

  /**
   * Animates the icon from the origin tile to the destination tile. Applies a visual offset based
   * on index to prevent stacking conflicts.
   *
   * @param icon the visual node to animate
   * @param fromTile the tile where the icon starts
   * @param toTile the tile where the icon ends
   * @param index the relative position among co-occupants of the tile
   */
  public void animate(Node icon, Node fromTile, Node toTile, int index) {
    Bounds fromBounds = fromTile.localToScene(fromTile.getBoundsInLocal());
    Bounds toBounds = toTile.localToScene(toTile.getBoundsInLocal());

    Point2D from = animationLayer.sceneToLocal(fromBounds.getMinX(), fromBounds.getMinY());
    Point2D to = animationLayer.sceneToLocal(toBounds.getMinX(), toBounds.getMinY());
    Point2D offset = offsetForIndex(index);

    if (!animationLayer.getChildren().contains(icon)) {
      animationLayer.getChildren().add(icon);
    }

    icon.setLayoutX(from.getX() + offset.getX());
    icon.setLayoutY(from.getY() + offset.getY());

    TranslateTransition transition = new TranslateTransition(Duration.millis(300), icon);
    transition.setToX(to.getX() - from.getX());
    transition.setToY(to.getY() - from.getY());
    transition.setOnFinished(
        e -> {
          icon.setLayoutX(to.getX() + offset.getX());
          icon.setLayoutY(to.getY() + offset.getY());
          icon.setTranslateX(0);
          icon.setTranslateY(0);
        });
    transition.play();
  }

  /**
   * Returns a small coordinate offset used to spread out icons on the same tile.
   *
   * @param index index of the icon among its tile's occupants
   * @return a pixel offset for the icon's layout position
   */
  private Point2D offsetForIndex(int index) {
    return switch (index % 4) {
      case 0 -> new Point2D(0, 0);
      case 1 -> new Point2D(18, 0);
      case 2 -> new Point2D(0, 18);
      case 3 -> new Point2D(18, 18);
      default -> Point2D.ZERO;
    };
  }
}
