package dev.nheggoe.boardgame.app.component;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.app.ui.EventListeningComponent;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.UnhandledEventException;
import dev.nheggoe.boardgame.core.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.monopoly.MonopolyEvent;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * {@code UIPane} is a dynamic sidebar for the Monopoly GUI. It displays each player as a visually
 * styled card including: name, balance, position, figure image, and owned properties.
 *
 * @author Mihailo Hranisavljevic and Nick Heggø
 * @version 2025.05.19
 */
public class PlayerDashboard<P extends Player> extends EventListeningComponent {

  private final HashMap<Player, PlayerInfoBox> playerRegistry = new HashMap<>();

  private static final Color[] CARD_COLORS = {
    Color.web("#fff3cd"),
    Color.web("#d1e7dd"),
    Color.web("#cfe2ff"),
    Color.web("#f8d7da"),
    Color.web("#e2e3e5")
  };

  /**
   * Constructs a new PlayerDashboard, which serves as the UI component displaying the players'
   * information and is updated based on various events.
   *
   * @param eventBus the event bus to listen to for player-related events, must not be null
   * @param playersSupplier the list of players to display in the dashboard, must not be null
   */
  public PlayerDashboard(EventBus eventBus, Supplier<List<P>> playersSupplier) {
    super(
        eventBus,
        CoreEvent.PlayerMoved.class,
        CoreEvent.PlayerRemoved.class,
        MonopolyEvent.Purchased.class);

    setPrefWidth(320);
    setStyle(
        "-fx-background-color: linear-gradient(to bottom, #1e293b, #0f172a); "
            + "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; "
            + "-fx-selection-bar: transparent; -fx-selection-bar-non-focused: transparent;");
    setPadding(new Insets(10));

    // Prevent the component from being focusable to avoid the blue glow
    setFocusTraversable(false);
    setMouseTransparent(false);

    // Override focus behavior to prevent any visual changes
    setOnMousePressed(javafx.event.Event::consume);
    setOnMouseReleased(javafx.event.Event::consume);

    VBox content = new VBox(16);
    content.setPadding(new Insets(10));
    content.setFocusTraversable(false);
    content.setOnMousePressed(javafx.event.Event::consume);
    content.setOnMouseReleased(javafx.event.Event::consume);

    int i = 0;
    for (Player player : playersSupplier.get()) {
      PlayerInfoBox box = new PlayerInfoBox(player, CARD_COLORS[i % CARD_COLORS.length]);
      playerRegistry.put(player, box);
      content.getChildren().add(box);
      i++;
    }

    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    scrollPane.setStyle(
        "-fx-background: transparent; -fx-focus-color: transparent; "
            + "-fx-faint-focus-color: transparent; -fx-selection-bar: transparent; "
            + "-fx-selection-bar-non-focused: transparent;");
    scrollPane.setFocusTraversable(false);
    scrollPane.setOnMousePressed(javafx.event.Event::consume);
    scrollPane.setOnMouseReleased(javafx.event.Event::consume);
    getChildren().add(scrollPane);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);
  }

  @Override
  public void onEvent(Event event) {
    switch (event) {
      case CoreEvent.PlayerMoved(Player player) -> highlightPlayer(player);
      case CoreEvent.PlayerRemoved(Player player) -> handlePlayerRemoval(player);
      case MonopolyEvent.Purchased ignored -> refresh();
      default -> throw new UnhandledEventException(event);
    }
  }

  private void handlePlayerRemoval(Player player) {
    PlayerInfoBox playerBox = playerRegistry.get(player);
    if (playerBox != null) {
      playerBox.setGrayedOut(true);
      playerBox.setGlow(false);
    }
  }

  /** Refreshes all player displays in the sidebar. */
  public void refresh() {
    playerRegistry.values().forEach(PlayerInfoBox::refresh);
  }

  private void highlightPlayer(Player player) {
    for (var entry : playerRegistry.entrySet()) {
      PlayerInfoBox box = entry.getValue();
      box.setGlow(entry.getKey().equals(player));
    }
  }

  // ------------------------  inner class  ------------------------

  /** {@code PlayerInfoBox} represents a stylized card for one player. */
  private static class PlayerInfoBox extends VBox {
    private static final String TITLE_FONT = "Georgia";
    private static final String BODY_FONT = "Verdana";

    private final Player player;
    private final Label nameLabel;
    private final Label balanceLabel;
    private final Label positionLabel;
    private final ImageView figureImage;
    private final Color originalBackgroundColor;
    private boolean isGrayedOut = false;

    private PlayerInfoBox(Player player, Color backgroundColor) {
      this.player = player;
      this.originalBackgroundColor = backgroundColor;

      setSpacing(10);
      setPadding(new Insets(14));
      setAlignment(Pos.TOP_CENTER);
      setPrefWidth(240);
      setBackground(
          new Background(new BackgroundFill(backgroundColor, new CornerRadii(12), Insets.EMPTY)));
      setBorder(
          new Border(
              new BorderStroke(
                  Color.web("#b0b0b0"),
                  BorderStrokeStyle.SOLID,
                  new CornerRadii(12),
                  BorderWidths.DEFAULT)));
      setEffect(new DropShadow(6, Color.gray(0.4)));

      // Prevent focus styling on individual player cards
      setFocusTraversable(false);
      setOnMousePressed(javafx.event.Event::consume);
      setOnMouseReleased(javafx.event.Event::consume);

      nameLabel = label(Font.font(TITLE_FONT, FontWeight.BOLD, 20), "#1a1a1a");
      balanceLabel = label(Font.font(BODY_FONT, FontWeight.BOLD, 14), "#166534");
      positionLabel = label(Font.font(BODY_FONT, FontWeight.NORMAL, 13), "#1e40af");

      figureImage = new ImageView();
      figureImage.setFitHeight(90);
      figureImage.setFitWidth(90);
      figureImage.setPreserveRatio(true);
      loadFigureImage(player.getFigure());

      refresh();

      getChildren().addAll(nameLabel, balanceLabel, positionLabel, figureImage);
    }

    private Label label(Font font, String hexColor) {
      Label l = new Label();
      l.setFont(font);
      l.setTextFill(Color.web(hexColor));
      l.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.2), 1, 0.0, 1, 1);");
      return l;
    }

    private void loadFigureImage(Player.Figure figure) {
      String resourcePath =
          switch (figure) {
            case CAT -> "/images/cat.png";
            case CAR -> "/images/car.png";
            case HAT -> "/images/hat.png";
            case BATTLE_SHIP -> "/images/battleship.png";
            case DUCK -> "/images/duck.png";
          };
      figureImage.setImage(new Image(requireNonNull(getClass().getResourceAsStream(resourcePath))));
    }

    public void refresh() {
      nameLabel.setText(player.getName());
      positionLabel.setText("Tile: " + player.getPosition());

      // Update balance if player is MonopolyPlayer
      if (player instanceof MonopolyPlayer monopolyPlayer) {
        var balance = isGrayedOut ? "Bankruptcy" : "$" + monopolyPlayer.getBalance();
        balanceLabel.setText("Balance: " + balance);
      } else {
        balanceLabel.setText("");
      }
    }

    /** Toggles a bright animated blue glow effect if it's the player's turn. */
    private void setGlow(boolean active) {
      if (active && !isGrayedOut) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#3b82f6"));
        glow.setRadius(26);
        glow.setSpread(0.6);
        glow.setOffsetX(0);
        glow.setOffsetY(0);
        setEffect(glow);
      } else {
        setEffect(new DropShadow(6, Color.gray(0.4)));
      }
    }

    /** Sets the card to a grayed-out state when player is removed. */
    private void setGrayedOut(boolean grayedOut) {
      this.isGrayedOut = grayedOut;

      if (grayedOut) {
        // Gray out the background
        Color grayedColor = Color.gray(0.7, 0.5);
        setBackground(
            new Background(new BackgroundFill(grayedColor, new CornerRadii(12), Insets.EMPTY)));

        // Gray out the border
        setBorder(
            new Border(
                new BorderStroke(
                    Color.gray(0.6),
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(12),
                    BorderWidths.DEFAULT)));

        // Reduce opacity of all text labels
        nameLabel.setOpacity(0.5);
        balanceLabel.setOpacity(0.5);
        positionLabel.setOpacity(0.5);
        figureImage.setOpacity(0.5);

        // Set a subtle shadow effect
        setEffect(new DropShadow(3, Color.gray(0.2)));
      } else {
        // Restore original appearance
        setBackground(
            new Background(
                new BackgroundFill(originalBackgroundColor, new CornerRadii(12), Insets.EMPTY)));

        setBorder(
            new Border(
                new BorderStroke(
                    Color.web("#b0b0b0"),
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(12),
                    BorderWidths.DEFAULT)));

        // Restore opacity
        nameLabel.setOpacity(1.0);
        balanceLabel.setOpacity(1.0);
        positionLabel.setOpacity(1.0);
        figureImage.setOpacity(1.0);

        // Restore normal shadow
        setEffect(new DropShadow(6, Color.gray(0.4)));
      }
    }
  }
}
