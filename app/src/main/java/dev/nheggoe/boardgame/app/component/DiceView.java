package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.ui.Component;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * {@code DiceView} is a visual component for displaying and animating dice rolls. It dynamically
 * loads dice face images and provides an animated rolling effect before displaying the final
 * result.
 *
 * <p>This class uses JavaFX's {@link Timeline} for simple roll animation, and can be used to
 * visually represent dice outcomes in the board game. Expected dice face images should be placed
 * under the {@code /images} directory with filenames matching the pattern {@code dice1.png}, {@code
 * dice2.png}, etc.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * DiceView diceView = new DiceView();
 * diceView.animateDiceRoll(new DiceRoll(1, 2));
 * }</pre>
 *
 * @version 2025.05.16
 */
public class DiceView extends Component {

  private static final String BASE_PATH = "/images/dice";
  private static final int FACE_COUNT = 6;
  private static final int STEPS = 30; // More frames for smoother animation
  private static final int TOTAL_DURATION_MS = 1000;
  private static final int FRAME_DURATION_MS = TOTAL_DURATION_MS / STEPS;

  private final Random random = new Random();

  /**
   * Constructs a {@code DiceView} component that visually represents dice rolls and aligns its
   * children to the center with specified spacing.
   */
  public DiceView() {
    setAlignment(Pos.CENTER);
    setSpacing(20);
  }

  /**
   * Animates the dice roll, showing random faces during the animation, then settling on the final
   * diceRoll provided by the {@link DiceRoll}.
   *
   * @param diceRoll the final dice roll diceRoll to display
   */
  public void animateDiceRoll(DiceRoll diceRoll) {
    getChildren().clear();
    int diceCount = diceRoll.rolls().length;
    var diceImages = new ImageView[diceCount];
    for (int i = 0; i < diceCount; i++) {
      ImageView imageView = new ImageView();
      imageView.setFitWidth(64);
      imageView.setFitHeight(64);
      diceImages[i] = imageView;
    }
    var diceContainer = new HBox(diceImages);
    diceContainer.setAlignment(Pos.CENTER);
    getChildren().add(diceContainer);

    Timeline timeline = new Timeline();
    timeline.setCycleCount(STEPS);

    timeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.millis(FRAME_DURATION_MS),
                event -> {
                  for (ImageView imageView : diceImages) {
                    int randomFace = random.nextInt(FACE_COUNT) + 1;
                    imageView.setImage(loadFace(randomFace));
                  }
                }));

    timeline.setOnFinished(
        event -> {
          for (int i = 0; i < diceImages.length; i++) {
            int face = diceRoll.rolls()[i];
            diceImages[i].setImage(loadFace(face));
          }
        });

    timeline.play();
  }

  /**
   * Loads the image corresponding to a given dice face value.
   *
   * @param face the face value (1–6) of the die
   * @return the {@link Image} representing the face
   */
  private Image loadFace(int face) {
    return new Image(BASE_PATH + face + ".png");
  }
}
