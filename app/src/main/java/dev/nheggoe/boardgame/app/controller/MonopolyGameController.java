package dev.nheggoe.boardgame.app.controller;

import dev.nheggoe.boardgame.app.component.EndDialog;
import dev.nheggoe.boardgame.app.ui.AlertFactory;
import dev.nheggoe.boardgame.app.ui.Controller;
import dev.nheggoe.boardgame.app.ui.SceneSwitcher;
import dev.nheggoe.boardgame.app.view.MonopolyGameView;
import dev.nheggoe.boardgame.core.GameEngine;
import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.EventListener;
import dev.nheggoe.boardgame.core.event.UnhandledEventException;
import dev.nheggoe.boardgame.core.event.type.CoreEvent;
import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.core.event.type.UserInterfaceEvent;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;
import dev.nheggoe.boardgame.monopoly.MonopolyEvent;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.monopoly.model.tile.MonopolyTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * MonopolyGameController is responsible for orchestrating the gameplay flow of a Monopoly game
 * within the application's architecture. It acts as a concrete implementation of the abstract
 * {@link Controller} class, integrating the game logic, view updates, and scene transitions for the
 * Monopoly game.
 *
 * <p>The controller manages the game's lifecycle, including handling player turns and determining
 * when the game has ended. Upon game completion, a dialog is displayed to notify players.
 *
 * <p>The controller implements {@link EventListener} and orchestrates all UI components by
 * listening to events and calling component public APIs directly.
 */
public class MonopolyGameController extends Controller implements EventListener {

  private final EventBus eventBus;
  private final MonopolyGameView view;

  /**
   * Constructs a new instance of the MonopolyGameController.
   *
   * @param sceneSwitcher the {@link SceneSwitcher} responsible for managing scene transitions in
   *     the application; must not be null
   * @param eventBus the {@link EventBus} used for handling and dispatching events across different
   *     components; must not be null
   * @param gameEngine the {@link GameEngine} managing the core logic for the Monopoly game,
   *     including tiles and players; must not be null
   * @throws NullPointerException if any of the provided arguments are null
   */
  public MonopolyGameController(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      GameEngine<MonopolyTile, MonopolyPlayer> gameEngine) {
    super(sceneSwitcher, createGameView(sceneSwitcher, eventBus, gameEngine));
    this.eventBus = eventBus;
    this.view = (MonopolyGameView) getView();

    // Register as listener for all relevant events
    eventBus.addListener(CoreEvent.DiceRolled.class, this);
    eventBus.addListener(CoreEvent.PlayerMoved.class, this);
    eventBus.addListener(CoreEvent.PlayerRemoved.class, this);
    eventBus.addListener(CoreEvent.GameEnded.class, this);
    eventBus.addListener(MonopolyEvent.Purchased.class, this);
    eventBus.addListener(MonopolyEvent.UpgradePurchased.class, this);
    eventBus.addListener(MonopolyEvent.PlayerSentToJail.class, this);
    eventBus.addListener(MonopolyEvent.RolledDouble.class, this);
    eventBus.addListener(UserInterfaceEvent.Output.class, this);
    eventBus.addListener(UserInterfaceEvent.Alert.class, this);
  }

  @Override
  public void onEvent(Event event) {
    switch (event) {
      case MonopolyEvent monopolyEvent -> onMonopolyEvent(monopolyEvent);
      case CoreEvent coreEvent -> onCoreEvent(coreEvent);
      case UserInterfaceEvent userInterfaceEvent -> onUIEvent(userInterfaceEvent);
      default -> throw new UnhandledEventException(event);
    }
  }

  private void onMonopolyEvent(MonopolyEvent monopolyEvent) {
    switch (monopolyEvent) {
      case MonopolyEvent.Purchased _ -> {
        view.getMonopolyBoardView().updateAllProperties();
        view.getPlayerDashboard().refresh();
      }

      case MonopolyEvent.UpgradePurchased _ -> view.getMonopolyBoardView().updateAllProperties();

      case MonopolyEvent.PlayerSentToJail(MonopolyPlayer player) -> {
        AlertFactory.createAlert(
                Alert.AlertType.INFORMATION,
                "Player has rolled doubles 3 times in a row. They are forced to go to jail.")
            .showAndWait();
        view.getMonopolyBoardView().playerMoved(player, player.getPosition());
      }

      case MonopolyEvent.RolledDouble(MonopolyPlayer player) ->
          AlertFactory.createAlert(
                  Alert.AlertType.INFORMATION,
                  "Player %s rolled a double! They need to move again.".formatted(player.getName()))
              .showAndWait();
    }
  }

  private void onUIEvent(UserInterfaceEvent uiEvent) {
    switch (uiEvent) {
      case UserInterfaceEvent.Output(String message) ->
          view.getMessagePanel().animateMessage(message);

      case UserInterfaceEvent.Alert(String message, var callback) -> {
        var result = AlertFactory.createAlert(Alert.AlertType.CONFIRMATION, message).showAndWait();
        callback.accept(
            result
                .map(ButtonType::getButtonData)
                .map(ButtonBar.ButtonData::isDefaultButton)
                .orElse(false));
      }
    }
  }

  private void onCoreEvent(CoreEvent coreEvent) {
    switch (coreEvent) {
      case CoreEvent.DiceRolled(DiceRoll diceRoll) -> view.getDiceView().animateDiceRoll(diceRoll);

      case CoreEvent.PlayerMoved(Player player) -> {
        view.getMonopolyBoardView().playerMoved(player, player.getPosition());
        view.getPlayerDashboard().highlightPlayer(player);
      }

      case CoreEvent.PlayerRemoved(Player player) ->
          view.getPlayerDashboard().handlePlayerRemoval(player);

      case CoreEvent.GameEnded(Player winner) ->
          AlertFactory.createAlert(
                  Alert.AlertType.INFORMATION,
                  "%s has won the game with net worth of %d!"
                      .formatted(winner.getName(), ((MonopolyPlayer) winner).getNetWorth()))
              .showAndWait();
    }
  }

  @Override
  public void close() {
    eventBus.removeListener(CoreEvent.DiceRolled.class, this);
    eventBus.removeListener(CoreEvent.PlayerMoved.class, this);
    eventBus.removeListener(CoreEvent.PlayerRemoved.class, this);
    eventBus.removeListener(CoreEvent.GameEnded.class, this);
    eventBus.removeListener(MonopolyEvent.Purchased.class, this);
    eventBus.removeListener(MonopolyEvent.UpgradePurchased.class, this);
    eventBus.removeListener(MonopolyEvent.PlayerSentToJail.class, this);
    eventBus.removeListener(MonopolyEvent.RolledDouble.class, this);
    eventBus.removeListener(UserInterfaceEvent.Output.class, this);
    eventBus.removeListener(UserInterfaceEvent.Alert.class, this);
  }

  private static MonopolyGameView createGameView(
      SceneSwitcher sceneSwitcher,
      EventBus eventBus,
      GameEngine<MonopolyTile, MonopolyPlayer> gameEngine) {
    return new MonopolyGameView(
        sceneSwitcher,
        eventBus,
        gameEngine::getTiles,
        gameEngine::getPlayers,
        nextTurnEventHandler(sceneSwitcher, gameEngine));
  }

  private static EventHandler<ActionEvent> nextTurnEventHandler(
      SceneSwitcher sceneSwitcher, GameEngine<MonopolyTile, MonopolyPlayer> gameEngine) {
    return _ -> {
      gameEngine.nextTurn();
      if (gameEngine.isEnded()) {
        new EndDialog(sceneSwitcher).showAndWait();
      }
    };
  }
}
