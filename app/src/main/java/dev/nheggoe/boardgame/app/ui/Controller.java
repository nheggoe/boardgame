package dev.nheggoe.boardgame.app.ui;

import dev.nheggoe.boardgame.app.SceneSwitcher;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

/**
 * The Controller class serves as an abstract base class designed to connect the logical core of an
 * application with its UI view using JavaFX. It is responsible for managing the relationship
 * between a {@link View} instance and a {@link SceneSwitcher}, enabling responsive user interface
 * navigation and dynamically binding UI size properties.
 *
 * @author Nick Heggø
 * @version 2025.05.08
 */
public abstract class Controller {

  private final SceneSwitcher sceneSwitcher;
  private final View view;

  /**
   * Constructs a Controller instance, linking a {@link SceneSwitcher} with a {@link View}, and
   * binds the View's size properties to the Scene controlled by the SceneSwitcher.
   *
   * @param sceneSwitcher the SceneSwitcher responsible for managing scene transitions; must not be
   *     null
   * @param view the View associated with this Controller, representing the user interface; must not
   *     be null
   */
  protected Controller(SceneSwitcher sceneSwitcher, View view) {
    this.sceneSwitcher = requireNonNull(sceneSwitcher, "SceneSwitcher cannot be null!");
    this.view = requireNonNull(view, "View cannot be null!");
    bindSizeProperty();
  }

  private void bindSizeProperty() {
    view.prefWidthProperty().bind(sceneSwitcher.getScene().widthProperty());
    view.prefHeightProperty().bind(sceneSwitcher.getScene().heightProperty());
  }

  protected SceneSwitcher getSceneSwitcher() {
    return sceneSwitcher;
  }

  /**
   * Retrieves the associated {@link View} instance managed by this controller.
   *
   * @return the {@link View} instance linked with this controller.
   */
  public View getView() {
    return view;
  }

  /**
   * Switches the current view to the specified scene using the associated {@link SceneSwitcher}.
   *
   * @param name the name of the target scene to switch to; must be one of the defined {@link
   *     SceneSwitcher.SceneName} values
   * @throws IOException if an I/O error occurs during the scene switch process
   */
  public void switchTo(SceneSwitcher.SceneName name) throws IOException {
    getSceneSwitcher().switchTo(name);
  }
}
