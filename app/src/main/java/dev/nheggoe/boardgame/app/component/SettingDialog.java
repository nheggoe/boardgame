package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.SceneSwitcher;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

/// Represents a custom JavaFX settings dialog for applications, extending the `Dialog` with
/// custom behavior for navigation and application management.
public class SettingDialog extends Dialog<ButtonType> {

  /// Constructs a new `SettingDialog` instance to display a settings dialog in the
  /// application. This dialog offers navigation options such as editing saved players, returning to
  /// the main menu, or exiting the application.
  ///
  /// @param sceneSwitcher an instance of `SceneSwitcher` used to manage scene transitions;
  ///     must not be null
  /// @param inGame a boolean flag indicating if the dialog is displayed during a game session; if
  ///     true, certain options may be disabled
  public SettingDialog(SceneSwitcher sceneSwitcher, boolean inGame) {
    var root = getDialogPane();
    root.setPrefWidth(300);
    root.setPrefHeight(200);
    root.getButtonTypes().addAll(ButtonType.OK);
    root.setContent(createContentPane(sceneSwitcher, inGame));
  }

  private VBox createContentPane(SceneSwitcher sceneSwitcher, boolean inGame) {
    var navigation = new VBox();
    navigation.setAlignment(Pos.CENTER);
    navigation.setSpacing(10);

    var editSavedPlayerButton = new Button("Edit Saved Players");
    editSavedPlayerButton.setOnAction(
        event -> {
          try {
            sceneSwitcher.switchTo(SceneSwitcher.SceneName.PLAYER_SETUP_VIEW);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          this.close();
        });
    editSavedPlayerButton.setDisable(inGame);

    var backToMainMenuButton = new Button("Back to Main Menu");
    backToMainMenuButton.setOnAction(
        event -> {
          try {
            sceneSwitcher.switchTo(SceneSwitcher.SceneName.MAIN_VIEW);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          this.close();
        });

    var exitButton = new Button("Exit");
    exitButton.setOnAction(event -> Platform.exit());

    navigation.getChildren().addAll(editSavedPlayerButton, backToMainMenuButton, exitButton);
    return navigation;
  }
}
