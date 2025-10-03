package dev.nheggoe.boardgame.app.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Represents an abstract base class for creating and managing views in a graphical user interface.
 *
 * <p>The {@code View} class serves as a container for {@link Component} objects and provides
 * functionality for organizing and displaying UI layouts using a root {@link Pane}. It extends
 * {@link Region} to allow for advanced layout management and implements {@link AutoCloseable} to
 * handle resource cleanup.
 *
 * @author Nick Heggø
 * @version @2025.05.08
 */
public abstract class View extends Region implements AutoCloseable {

  private final List<Component> components;

  /**
   * Constructs a {@code View} instance and initializes the internal list of components.
   *
   * <p>This constructor is protected and is intended to be called by subclasses. The {@code View}
   * class itself serves as an abstract base class for managing and organizing UI components using a
   * predefined structure.
   */
  protected View() {
    this.components = new ArrayList<>();
  }

  /**
   * Adds the specified components to the internal list of components managed by this view.
   *
   * @param components the components to be added; each component must extend the {@link Component}
   *     class
   */
  protected void addComponents(Component... components) {
    this.components.addAll(Arrays.asList(components));
  }

  /**
   * Returns the list of components managed by this view.
   *
   * @return an unmodifiable list of components
   */
  protected List<Component> getComponents() {
    return List.copyOf(components);
  }

  /**
   * Sets the specified {@link Pane} as the root of the view. Clears all existing child nodes and
   * replaces them with the provided root pane. Also binds the preferred width and height of the
   * root pane to the respective width and height properties of this view.
   *
   * @param root the {@link Pane} that will serve as the root layout of the view
   */
  protected void setRoot(Pane root) {
    getChildren().clear();
    getChildren().add(root);
    root.prefWidthProperty().bind(this.widthProperty());
    root.prefHeightProperty().bind(this.heightProperty());
  }

  @Override
  public void close() {
    for (var component : components) {
      if (component instanceof EventListeningComponent listener) {
        listener.close();
      }
    }
  }
}
