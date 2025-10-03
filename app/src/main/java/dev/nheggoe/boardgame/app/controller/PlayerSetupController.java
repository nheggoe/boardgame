package dev.nheggoe.boardgame.app.controller;

import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.SceneSwitcher;
import dev.nheggoe.boardgame.app.ui.View;
import dev.nheggoe.boardgame.app.view.PlayerSetupView;

/**
 * The PlayerSetupController class is responsible for managing the player setup view. It facilitates
 * initialization and interaction between the player setup interface and the application backend.
 *
 * <p>This controller connects the player setup view with the scene management system, enabling
 * smooth transitions and dynamic resizing of the view relative to the application's main scene.
 */
public class PlayerSetupController extends Controller {

  /**
   * Constructs a PlayerSetupController instance that initializes the player setup view to manage
   * the player's configuration setup within the application.
   *
   * @param sceneSwitcher the SceneSwitcher responsible for managing scene transitions; must not be
   *     null
   */
  public PlayerSetupController(SceneSwitcher sceneSwitcher) {
    super(sceneSwitcher, createPlayerSetupView(sceneSwitcher));
  }

  private static View createPlayerSetupView(SceneSwitcher sceneSwitcher) {
    return new PlayerSetupView(sceneSwitcher);
  }
}
