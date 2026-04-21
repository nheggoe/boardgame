package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.ui.Component;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/// The `SettingButton` class represents a customizable button component for accessing the
/// application's settings. It extends the `Component` class and provides functionality to open
/// a settings dialog.
///
/// This button is positioned in the top-right corner of its parent container and allows users to
/// configure various settings or preferences. It integrates a `SettingDialog` that can be
/// invoked on button action, offering options such as navigating to different application scenes or
/// modifying in-game parameters.
public class SettingButton extends Component {

  private final Button settings;
  private boolean inGame;

  /// Constructs a new SettingButton instance for managing and opening the settings dialog. The
  /// button is aligned to the top-right of its parent container and opens a `SettingDialog`
  /// when clicked.
  ///
  /// @param sceneSwitcher the `SceneSwitcher` instance responsible for navigating between
  ///     scenes within the application
  public SettingButton(SceneSwitcher sceneSwitcher) {
    super();
    setAlignment(Pos.TOP_RIGHT);
    setPadding(new Insets(10));
    settings = new Button("Settings");
    settings.setOnAction(ignored -> new SettingDialog(sceneSwitcher, inGame).showAndWait());
    getChildren().add(settings);
  }

  /// Updates the state of whether the application is in-game or not.
  ///
  /// @param inGame a boolean value representing the current state of the game; `true` if the
  ///     application is in-game, `false` otherwise
  public void isInGame(boolean inGame) {
    this.inGame = inGame;
  }
}
