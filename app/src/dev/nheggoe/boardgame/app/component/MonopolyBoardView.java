package dev.nheggoe.boardgame.app.component;

import dev.nheggoe.boardgame.app.ui.Component;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.monopoly.model.ownable.Ownable;
import dev.nheggoe.boardgame.monopoly.model.ownable.Property;
import dev.nheggoe.boardgame.monopoly.model.ownable.Railroad;
import dev.nheggoe.boardgame.monopoly.model.ownable.Utility;
import dev.nheggoe.boardgame.monopoly.model.tile.FreeParkingMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.GoToJailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.JailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.OwnableMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.StartMonopolyTile;
import dev.nheggoe.boardgame.monopoly.model.tile.TaxMonopolyTile;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/// A visual representation of the Monopoly game board.
///
/// The class handles rendering the board with its tiles and players, managing visual updates for
/// player movements, property ownership changes, and upgrades. It leverages suppliers to dynamically
/// retrieve the current game state for tiles and players. The class also binds to the scene
/// properties to ensure a responsive layout during gameplay.
public class MonopolyBoardView extends Component {
  private final GridPane board;

  private final Supplier<List<MonopolyTile>> tilesSupplier;
  private final Supplier<List<MonopolyPlayer>> playersSupplier;
  private static final Color[] PLAYER_COLORS = {
    Color.web("#fff3cd"),
    Color.web("#d1e7dd"),
    Color.web("#cfe2ff"),
    Color.web("#f8d7da"),
    Color.web("#e2e3e5")
  };

  /// Constructs a MonopolyBoardView that serves as the visual representation of a Monopoly game
  /// board. This class initializes the board layout.
  ///
  /// @param playersSupplier a supplier providing the list of Monopoly players in the game
  /// @param tilesSupplier a supplier providing the list of tiles (spaces) on the Monopoly board
  public MonopolyBoardView(
      Supplier<List<MonopolyPlayer>> playersSupplier, Supplier<List<MonopolyTile>> tilesSupplier) {
    this.playersSupplier = playersSupplier;
    this.tilesSupplier = tilesSupplier;
    board = new GridPane();
    getChildren().add(board);
    setAlignment(Pos.CENTER);
    initialize(tilesSupplier, playersSupplier);
  }

  /// Updates the visual representation of a player's position on the game board. This method is
  /// called when a player moves to a new position on the board.
  ///
  /// @param player the player who has moved
  /// @param position the new position of the player on the board
  public void playerMoved(Player player, int position) {
    // Remove light ring from all tiles
    removeAllLightRings();

    // Clear any existing player figure from the board
    clearPlayerFigures(player);

    // Calculate the grid position for the new tile based on number of tiles
    var tiles = tilesSupplier.get();
    int size = (tiles.size() + 4) / 4;
    int row = 0;
    int col = 0;

    // Position logic for clockwise movement
    if (position == 0) {
      // Bottom right corner
      row = size - 1;
      col = size - 1;
    } else if (position < size) {
      // Bottom row (going left)
      row = size - 1;
      col = size - 1 - position;
    } else if (position < size * 2 - 1) {
      // Left column (going up)
      row = size - 1 - (position - (size - 1));
      col = 0;
    } else if (position < size * 3 - 2) {
      // Top row (going right)
      row = 0;
      col = position - (size * 2 - 2);
    } else {
      // Right column (going down)
      row = position - (size * 3 - 3);
      col = size - 1;
    }

    // Get the tile at the calculated position
    StackPane tilePane = getTileAtPosition(row, col);
    if (tilePane != null) {
      // Add light ring around the current tile
      addLightRingToTile(tilePane);

      // Create and add player figure to the tile
      addPlayerFigure(tilePane, player);

      // Update the property background color if it's owned
      updateTileBackgroundColor(tilePane);
    }
  }

  /// Adds a light ring effect around the tile that the player is currently on.
  ///
  /// @param tilePane the tile pane to add the light ring to
  private void addLightRingToTile(StackPane tilePane) {
    // Create a glowing border effect for the current tile
    BorderStroke glowStroke =
        new BorderStroke(
            Color.DEEPSKYBLUE, // Brighter blue color
            BorderStrokeStyle.SOLID,
            new CornerRadii(2),
            new BorderWidths(4)); // Wider border

    Border glowBorder = new Border(glowStroke);

    // Set a unique ID to identify this border as our light ring
    tilePane.setBorder(glowBorder);
    tilePane.setId("current-tile");

    // Override any inline styles that might be affecting the border
    tilePane.setStyle(
        "-fx-border-color: transparent; -fx-effect: dropshadow(three-pass-box, deepskyblue, 10, 0.5, 0, 0);");
  }

  /// Removes light ring effects from all tiles on the board.
  private void removeAllLightRings() {
    // Iterate through all tiles and reset borders
    for (Node node : board.getChildren()) {
      if (node instanceof StackPane tilePane) {
        if ("current-tile".equals(tilePane.getId())) {
          // Reset to default border and style
          tilePane.setBorder(
              new Border(
                  new BorderStroke(
                      Color.BLACK,
                      BorderStrokeStyle.SOLID,
                      CornerRadii.EMPTY,
                      BorderWidths.DEFAULT)));
          tilePane.setStyle("-fx-border-color: black;");
          tilePane.setId(null);
        }
      }
    }
  }

  /// Updates the background color of a property tile to match the owner's dashboard card color.
  ///
  /// @param tilePane the tile pane to update
  private void updateTileBackgroundColor(StackPane tilePane) {
    // Only apply to tiles that have Ownable userData
    if (tilePane.getUserData() instanceof Ownable ownable) {
      // Check each player to see if they own this property
      var players = playersSupplier.get();
      for (int i = 0; i < players.size(); i++) {
        MonopolyPlayer player = players.get(i);
        if (player.isOwnerOf(ownable)) {
          // Get the player's dashboard color
          Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];

          // Apply a background fill but preserve any property color band if it's a property
          if (ownable instanceof Property property) {
            // For properties, we'll add a semi-transparent overlay of the player's color
            // while preserving the property's color group
            Color propertyColor = colorAdapter(property.getColor());
            Color blendedColor =
                Color.color(
                    (propertyColor.getRed() * 0.6) + (playerColor.getRed() * 0.4),
                    (propertyColor.getGreen() * 0.6) + (playerColor.getGreen() * 0.4),
                    (propertyColor.getBlue() * 0.6) + (playerColor.getBlue() * 0.4),
                    0.8); // 80% opacity to allow some transparency

            tilePane.setBackground(
                new Background(new BackgroundFill(blendedColor, CornerRadii.EMPTY, Insets.EMPTY)));
          } else {
            // For railroads and utilities, use a lighter version of the player's color with
            // transparency
            Color transparentColor = playerColor.deriveColor(0, 1, 1.2, 0.6); // 60% opacity
            tilePane.setBackground(
                new Background(
                    new BackgroundFill(transparentColor, CornerRadii.EMPTY, Insets.EMPTY)));
          }
          break; // Found the owner, no need to check other players
        }
      }
    }
  }

  /// Removes all visual representations of the specified player from the board.
  ///
  /// @param player the player whose figures should be removed
  private void clearPlayerFigures(Player player) {
    // Iterate through all tiles on the board
    for (Node node : board.getChildren()) {
      if (node instanceof StackPane tilePane) {
        // Remove any player figure matching this player
        tilePane
            .getChildren()
            .removeIf(
                child ->
                    child instanceof ImageView imageView
                        && imageView.getUserData() != null
                        && imageView.getUserData().equals(player.getId()));
      }
    }
  }

  /// Gets the tile StackPane at the specified grid position.
  ///
  /// @param row the row in the grid
  /// @param col the column in the grid
  /// @return the StackPane at the specified position, or null if not found
  private StackPane getTileAtPosition(int row, int col) {
    for (Node node : board.getChildren()) {
      if (node instanceof StackPane
          && GridPane.getRowIndex(node) == row
          && GridPane.getColumnIndex(node) == col) {
        return (StackPane) node;
      }
    }
    return null;
  }

  /// Adds a visual representation of the player to the specified tile.
  ///
  /// @param tilePane the tile where the player figure should be added
  /// @param player the player whose figure should be added
  private void addPlayerFigure(StackPane tilePane, Player player) {
    String imagePath = getFigureImagePath(player.getFigure());
    ImageView playerFigure = new ImageView(imagePath);
    playerFigure.setFitWidth(30);
    playerFigure.setFitHeight(30);
    playerFigure.setUserData(player.getId()); // Store player ID for identification

    // Position the figure appropriately on the tile
    StackPane.setAlignment(playerFigure, Pos.CENTER);

    // If there are other players on this tile, adjust positioning
    int playerCount =
        (int)
            tilePane.getChildren().stream()
                .filter(child -> child instanceof ImageView && child.getUserData() != null)
                .count();

    // Adjust position based on number of players already on this tile
    switch (playerCount) {
      case 0 -> StackPane.setAlignment(playerFigure, Pos.TOP_LEFT);
      case 1 -> StackPane.setAlignment(playerFigure, Pos.TOP_RIGHT);
      case 2 -> StackPane.setAlignment(playerFigure, Pos.BOTTOM_LEFT);
      case 3 -> StackPane.setAlignment(playerFigure, Pos.BOTTOM_RIGHT);
      default -> StackPane.setAlignment(playerFigure, Pos.CENTER); // Fallback
    }

    // Add the player figure to the tile
    tilePane.getChildren().add(playerFigure);
  }

  /// Gets the image path for a player figure.
  ///
  /// @param figure the player's figure type
  /// @return the path to the corresponding image resource
  private String getFigureImagePath(Player.Figure figure) {
    return switch (figure) {
      case BATTLE_SHIP -> "images/battleship.png";
      case CAR -> "images/car.png";
      case CAT -> "images/cat.png";
      case DUCK -> "images/duck.png";
      case HAT -> "images/hat.png";
    };
  }

  private void initialize(
      Supplier<List<MonopolyTile>> tilesSuppler, Supplier<List<MonopolyPlayer>> players) {
    board.setGridLinesVisible(false); // optional: show grid lines
    board.setAlignment(Pos.CENTER);

    var tiles = tilesSuppler.get();
    int size = (tiles.size() + 4) / 4;

    // Position tiles in clockwise order, starting with 00 at bottom right

    // Bottom right corner - position 00
    board.add(createTile(tiles.getFirst()), size - 1, size - 1);

    // Right column (going up, skip bottom right)
    int pos = 1;
    for (int row = size - 2; row >= 0; row--) {
      board.add(createTile(tiles.get(pos++)), row, size - 1);
    }

    // Top row (going left, skip top right)
    for (int col = size - 2; col >= 0; col--) {
      board.add(createTile(tiles.get(pos++)), 0, col);
    }

    // Left column (going down, skip top left)
    for (int row = 1; row < size; row++) {
      board.add(createTile(tiles.get(pos++)), row, 0);
    }

    // Bottom row (going right, skip bottom left)
    for (int col = 1; col < size - 1; col++) {
      board.add(createTile(tiles.get(pos++)), size - 1, col);
    }

    players.get().forEach(player -> playerMoved(player, 0));
  }

  /// Binds the size properties of the board to the size properties of the scene containing it.
  ///
  /// This method ensures that the `board` Pane's width and height dynamically adjust to match the
  /// width and height of the scene. The binding guarantees that the board resizes automatically when
  /// the scene's size changes, maintaining a responsive layout.
  public void bindSizeProperty() {
    board.prefWidthProperty().bind(this.getScene().widthProperty());
    board.prefHeightProperty().bind(this.getScene().heightProperty());
  }

  private Pane createTile(MonopolyTile tile) {
    StackPane tilePane = new StackPane();
    int tileSize = 80;
    tilePane.setPrefSize(tileSize, tileSize);

    switch (tile) {
      case FreeParkingMonopolyTile freeParkingMonopolyTile -> {
        var imageView = new ImageView("icons/free-parking-tile.png");
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        tilePane.getChildren().add(imageView);

        // Semi-transparent background
        tilePane.setBackground(
            new Background(
                new BackgroundFill(
                    Color.color(0.5, 0.5, 0.5, 0.6), CornerRadii.EMPTY, Insets.EMPTY)));
      }
      case GoToJailMonopolyTile goToJailMonopolyTile -> {
        var imageView = new ImageView("icons/go-to-jail-tile.png");
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        tilePane.getChildren().add(imageView);

        // Semi-transparent background
        tilePane.setBackground(
            new Background(
                new BackgroundFill(Color.color(1, 1, 1, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
      }
      case JailMonopolyTile jailMonopolyTile -> {
        var imageView = new ImageView("icons/jail-tile.png");
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
        imageView.setPreserveRatio(true);
        tilePane.getChildren().add(imageView);

        // Semi-transparent background
        tilePane.setBackground(
            new Background(
                new BackgroundFill(
                    Color.color(0.9, 0.9, 0.9, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
      }
      case StartMonopolyTile startMonopolyTile -> {
        var imageView = new ImageView("icons/go-tile.png");
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        tilePane.getChildren().add(imageView);

        // Semi-transparent background
        tilePane.setBackground(
            new Background(
                new BackgroundFill(Color.color(1, 1, 1, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
      }
      case OwnableMonopolyTile(Ownable ownable) -> {
        switch (ownable) {
          case Property property -> {
            // Get the property color but make it semi-transparent
            Color propertyColor = colorAdapter(property.getColor());
            Color transparentColor =
                Color.color(
                    propertyColor.getRed(),
                    propertyColor.getGreen(),
                    propertyColor.getBlue(),
                    0.7); // 70% opacity

            tilePane.setBackground(
                new Background(
                    new BackgroundFill(transparentColor, CornerRadii.EMPTY, Insets.EMPTY)));

            var imageView = new ImageView("icons/propertyTileIcon.png");
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            tilePane.getChildren().add(imageView);

            // Add owner indicator and property name
            var nameLabel = new Label(property.getName());
            tilePane.getChildren().add(nameLabel);
            StackPane.setAlignment(nameLabel, Pos.BOTTOM_CENTER);

            // Property will store its data for updating owner later
            tilePane.setUserData(property);

            // Check for ownership and add owner initial if owned
            updatePropertyOwnership(tilePane, property);

            // Add upgrade indicators (houses and hotels)
            updatePropertyUpgrades(tilePane, property);
          }
          case Railroad railroad -> {
            tilePane.setBackground(
                new Background(
                    new BackgroundFill(
                        Color.color(0.2, 0.2, 0.2, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
            var imageView = new ImageView("icons/railroad.png");
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);
            tilePane.getChildren().add(imageView);

            // Railroad will store its data for updating owner later
            tilePane.setUserData(railroad);

            // Check for ownership and add owner initial if owned
            updatePropertyOwnership(tilePane, railroad);
          }
          case Utility utility -> {
            tilePane.setBackground(
                new Background(
                    new BackgroundFill(
                        Color.color(0.6, 0.4, 0.2, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
            var imageView = new ImageView("icons/utility.png");
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);
            tilePane.getChildren().add(imageView);

            // Utility will store its data for updating owner later
            tilePane.setUserData(utility);

            // Check for ownership and add owner initial if owned
            updatePropertyOwnership(tilePane, utility);
          }
        }
      }
      case TaxMonopolyTile taxTile -> {
        tilePane.setBackground(
            new Background(
                new BackgroundFill(
                    Color.color(0.1, 0.1, 0.1, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        var imageView = new ImageView("icons/taxTileIcon.png");
        imageView.setFitWidth(tileSize);
        imageView.setFitHeight(tileSize);
        tilePane.getChildren().add(imageView);
      }
    }
    // Add a default border that won't interfere with our light ring
    tilePane.setBorder(
        new Border(
            new BorderStroke(
                Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

    return tilePane;
  }

  /// Updates the visual representation of property ownership by adding the owner's initial with the
  /// corresponding player color from the dashboard.
  ///
  /// @param tilePane the tile to update
  /// @param ownable the property to check for ownership
  private void updatePropertyOwnership(StackPane tilePane, Ownable ownable) {
    // Remove any existing owner labels
    tilePane
        .getChildren()
        .removeIf(
            node ->
                node instanceof Label && node.getId() != null && node.getId().equals("ownerLabel"));

    // Check each player to see if they own this property
    var players = playersSupplier.get();
    for (int i = 0; i < players.size(); i++) {
      MonopolyPlayer player = players.get(i);
      if (player.isOwnerOf(ownable)) {
        // Create owner label with first capital letter
        String initial = player.getName().substring(0, 1).toUpperCase();
        Label ownerLabel = new Label(initial);
        ownerLabel.setId("ownerLabel");
        ownerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ownerLabel.setTextFill(Color.BLACK);

        // Use the same color as in PlayerDashboard
        Color playerColor = PLAYER_COLORS[i % PLAYER_COLORS.length];
        ownerLabel.setBackground(
            new Background(new BackgroundFill(playerColor, new CornerRadii(10), Insets.EMPTY)));
        ownerLabel.setPadding(new Insets(2, 6, 2, 6));
        ownerLabel.setBorder(
            new Border(
                new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    BorderWidths.DEFAULT)));

        // Add to tile and position in top-right corner
        tilePane.getChildren().add(ownerLabel);
        StackPane.setAlignment(ownerLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(ownerLabel, new Insets(5, 5, 0, 0));
        break; // Found the owner, no need to check other players
      }
    }
  }

  /// Updates the visual representation of property upgrades by adding green squares for houses and
  /// red squares for hotels.
  ///
  /// @param tilePane the tile to update
  /// @param property the property to check for upgrades
  private void updatePropertyUpgrades(StackPane tilePane, Property property) {
    // Remove any existing upgrade indicators
    tilePane
        .getChildren()
        .removeIf(
            node ->
                node instanceof Pane
                    && (node.getId() != null
                        && (node.getId().equals("houseIndicator")
                            || node.getId().equals("hotelIndicator"))));

    // Check for hotel (red square)
    if (property.hasHotel()) {
      Pane hotelIndicator = new Pane();
      hotelIndicator.setId("hotelIndicator");
      hotelIndicator.setPrefSize(6, 6);
      hotelIndicator.setBackground(
          new Background(new BackgroundFill(Color.RED, new CornerRadii(3), Insets.EMPTY)));
      hotelIndicator.setBorder(
          new Border(
              new BorderStroke(
                  Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), BorderWidths.DEFAULT)));

      tilePane.getChildren().add(hotelIndicator);
      StackPane.setAlignment(hotelIndicator, Pos.TOP_LEFT);
      StackPane.setMargin(hotelIndicator, new Insets(5, 0, 0, 5));
    } else {
      int houseCount = property.countHouses();
      if (houseCount > 0) {
        HBox houseContainer = new HBox(2);
        houseContainer.setId("houseIndicator");

        // Add a green square for each house
        for (int i = 0; i < houseCount; i++) {
          Pane houseIndicator = new Pane();
          houseIndicator.setPrefSize(10, 10);
          houseIndicator.setBackground(
              new Background(new BackgroundFill(Color.GREEN, new CornerRadii(2), Insets.EMPTY)));
          houseIndicator.setBorder(
              new Border(
                  new BorderStroke(
                      Color.BLACK,
                      BorderStrokeStyle.SOLID,
                      new CornerRadii(2),
                      BorderWidths.DEFAULT)));

          houseContainer.getChildren().add(houseIndicator);
        }

        tilePane.getChildren().add(houseContainer);
        StackPane.setAlignment(houseContainer, Pos.TOP_LEFT);
        StackPane.setMargin(houseContainer, new Insets(5, 0, 0, 5));
      }
    }
  }

  /// Updates all properties on the board to reflect current ownership and upgrades. Called after
  /// purchase events or when the board is refreshed.
  public void updateAllProperties() {
    for (Node node : board.getChildren()) {
      if (node instanceof StackPane tilePane && tilePane.getUserData() != null) {
        if (tilePane.getUserData() instanceof Property property) {
          updatePropertyOwnership(tilePane, property);
          updatePropertyUpgrades(tilePane, property);
          updateTileBackgroundColor(tilePane);
        } else if (tilePane.getUserData() instanceof Ownable ownable) {
          updatePropertyOwnership(tilePane, ownable);
          updateTileBackgroundColor(tilePane);
        }
      }
    }
  }

  private Color colorAdapter(Property.Color color) {
    return switch (color) {
      case BROWN -> Color.BROWN;
      case DARK_BLUE -> Color.DARKBLUE;
      case GREEN -> Color.GREEN;
      case LIGHT_BLUE -> Color.LIGHTBLUE;
      case ORANGE -> Color.ORANGE;
      case PINK -> Color.PINK;
      case RED -> Color.RED;
      case YELLOW -> Color.YELLOW;
    };
  }
}
