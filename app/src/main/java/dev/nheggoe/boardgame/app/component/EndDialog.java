package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

/**
 * The {@code EndDialog} class represents a custom dialog displayed at the end of a game. It
 * provides options for the user to either navigate back to the main menu, start a new game, or exit
 * the application. The dialog integrates functionality to interact with the application's scene
 * management, ensuring a seamless user experience.
 *
 * <ul>
 *   <li>Navigation to the main menu triggers a scene switch to the main view.
 *   <li>Starting a new game resets the application state and begins a new game session.
 *   <li>Exiting the application closes the program.
 * </ul>
 *
 * <p>This dialog is displayed in a centered layout with buttons providing the aforementioned
 * actions.
 *
 * @author Nick Heggø
 * @version 2025.05.20
 */
public class EndDialog extends Dialog<ButtonType> {

  /**
   * Constructs a new EndDialog instance with the specified scene switcher. The dialog provides
   * options for the user to navigate to the main menu, start a new game, or exit the application.
   *
   * @param sceneSwitcher the {@code SceneSwitcher} instance responsible for navigating between
   *     scenes in the application
   */
  public EndDialog(SceneSwitcher sceneSwitcher) {
    super();
    setTitle("Game Over");
    setHeaderText("Please select an option to continue.");
    var dialogPane = getDialogPane();
    dialogPane.setContent(setNavigation(sceneSwitcher));
    dialogPane.setPrefWidth(300);
    dialogPane.setPrefHeight(200);
    getDialogPane().getButtonTypes().addAll(ButtonType.OK);
  }

  private VBox setNavigation(SceneSwitcher sceneSwitcher) {
    var vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(10);

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

    var newGameButton = new Button("Play Again");
    newGameButton.setOnAction(
        event -> {
          try {
            sceneSwitcher.reset();
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          this.close();
        });

    var exitButton = new Button("Exit");
    exitButton.setOnAction(event -> Platform.exit());

    vBox.getChildren().addAll(backToMainMenuButton, newGameButton, exitButton);
    return vBox;
  }
}
