package dev.nheggoe.boardgame.app.view;

import dev.nheggoe.boardgame.app.component.SettingButton;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import dev.nheggoe.boardgame.app.ui.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/// Represents the primary UI view for the application, providing functionality for navigating to
/// specific game-related views and accessing application settings.
///
/// The view is built using a [BorderPane] as the root layout and contains: - A center pane
/// with buttons for navigating to the "Snake and Ladder" and "Monopoly" game views. - A settings
/// button positioned on the right side of the layout.
///
/// Extends [View] to enable advanced layout management and resource handling.
public class MainView extends View {

  /// Constructs the MainView and initializes its layout components.
  ///
  /// The MainView utilizes a [BorderPane] as the root layout. It assigns a center pane,
  /// which contains buttons for navigating to "Snake and Ladder" and "Monopoly" game views.
  /// Additionally, it places a settings button on the right side of the layout to handle application
  /// settings.
  ///
  /// @param sceneSwitcher the `SceneSwitcher` used for managing scene transitions, must not be
  ///     `null`
  /// @param snake the `EventHandler<ActionEvent>` assigned to handle actions triggered by the
  ///     "Snake and Ladder" button
  /// @param monopoly the `EventHandler<ActionEvent>` assigned to handle actions triggered by
  ///     the "Monopoly" button
  /// @throws NullPointerException if any of the parameters are `null`
  public MainView(
      SceneSwitcher sceneSwitcher,
      EventHandler<ActionEvent> snake,
      EventHandler<ActionEvent> monopoly) {
    var root = new BorderPane();
    setRoot(root);

    root.setCenter(createCenterPane(snake, monopoly));
    root.setRight(new SettingButton(sceneSwitcher));
  }

  private Pane createCenterPane(
      EventHandler<ActionEvent> snake, EventHandler<ActionEvent> monopoly) {
    var center = new HBox();
    center.setAlignment(Pos.CENTER);
    center.setSpacing(10);
    var monopolyButton = new Button();
    monopolyButton.setGraphic(createIcon("/icons/monopoly-icon.png"));

    var snakeButton = new Button();
    snakeButton.setGraphic(createIcon("/icons/snake-icon.png"));

    setHoverEffect(snakeButton, monopolyButton);

    snakeButton.setOnAction(snake);
    monopolyButton.setOnAction(monopoly);
    center.getChildren().addAll(monopolyButton, snakeButton);
    return center;
  }

  private static ImageView createIcon(String classpathPath) {
    var iconView = new ImageView(classpathPath);
    iconView.setFitWidth(200);
    iconView.setFitHeight(200);
    return iconView;
  }

  private void setHoverEffect(Button... buttons) {
    for (Button button : buttons) {
      button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
      button.setOnMouseEntered(
          e ->
              button.setStyle(
                  "-fx-background-color: transparent; -fx-border-color: transparent;"
                      + " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.3, 0, 2);"));
      button.setOnMouseExited(
          e ->
              button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"));
    }
  }
}
