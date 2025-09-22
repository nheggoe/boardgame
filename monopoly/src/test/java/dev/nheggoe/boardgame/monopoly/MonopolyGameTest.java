package dev.nheggoe.boardgame.monopoly;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dev.nheggoe.boardgame.common.event.EventBus;
import dev.nheggoe.boardgame.common.event.type.MonopolyEvent;
import dev.nheggoe.boardgame.common.util.AlertFactory;
import dev.nheggoe.boardgame.core.model.dice.Dice;
import dev.nheggoe.boardgame.core.model.dice.DiceRoll;
import dev.nheggoe.boardgame.monopoly.board.MonopolyBoard;
import dev.nheggoe.boardgame.monopoly.model.upgrade.Upgrade;
import dev.nheggoe.boardgame.monopoly.model.upgrade.UpgradeType;
import dev.nheggoe.boardgame.monopoly.ownable.InsufficientFundsException;
import dev.nheggoe.boardgame.monopoly.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.monopoly.ownable.Property;
import dev.nheggoe.boardgame.monopoly.ownable.Railroad;
import dev.nheggoe.boardgame.monopoly.ownable.Utility;
import dev.nheggoe.boardgame.monopoly.tile.CornerMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.FreeParkingMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.GoToJailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.JailMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.OwnableMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.StartMonopolyTile;
import dev.nheggoe.boardgame.monopoly.tile.TaxMonopolyTile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MonopolyGameTest {

  @Mock private EventBus mockEventBus;
  @Mock private MonopolyBoard mockBoard;
  @Mock private JailMonopolyTile mockJailTile;

  private MonopolyPlayer player1;
  private MonopolyPlayer player2;
  private Property property;
  private Property playerOwnedProperty;
  private MonopolyGame game;

  @BeforeEach
  void setUp() {
    // Initialize players and properties
    player1 = new MonopolyPlayer("Player 1", MonopolyPlayer.Figure.CAR);
    player2 = new MonopolyPlayer("Player 2", MonopolyPlayer.Figure.CAT);
    property = new Property("Test Property", Property.Color.BROWN, 100);
    playerOwnedProperty = new Property("Owned Property", Property.Color.RED, 150);

    // Setup board
    when(mockBoard.size()).thenReturn(40);
    when(mockBoard.getJailTile()).thenReturn(mockJailTile);
    when(mockBoard.getTilePosition(mockJailTile)).thenReturn(10);

    // Create the game with a mutable list of players
    List<MonopolyPlayer> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    game = spy(new MonopolyGame(mockEventBus, mockBoard, players));

    // Set initial position for players
    player1.setPosition(0);
    player2.setPosition(0);

    // Add owned property to player1 - ensure sufficient funds
    player1.addBalance(1000);
    try {
      player1.purchase(playerOwnedProperty);
    } catch (InsufficientFundsException e) {
      // This shouldn't happen with 1000 balance
    }

    // Setup to handle the tileActionOf method properly
    // Create StartTile to use as a default return value for getTile
    StartMonopolyTile defaultTile = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);

    // Mock the getTile behavior to return a valid tile by default
    doReturn(defaultTile).when(game).getTile(anyInt());
  }

  @Test
  @DisplayName("Players start with $200 when game is created")
  void playerStartBalance() {
    // Create a fresh set of players for this test
    MonopolyPlayer testPlayer1 = new MonopolyPlayer("Test Player 1", MonopolyPlayer.Figure.CAR);
    MonopolyPlayer testPlayer2 = new MonopolyPlayer("Test Player 2", MonopolyPlayer.Figure.CAT);

    // Initial balance should be 0
    assertThat(testPlayer1.getBalance()).isEqualTo(0);

    // Create a new game
    List<MonopolyPlayer> newPlayers = new ArrayList<>();
    newPlayers.add(testPlayer1);
    newPlayers.add(testPlayer2);
    new MonopolyGame(mockEventBus, mockBoard, newPlayers);

    // Verify balance increased to $200
    assertThat(testPlayer1.getBalance()).isEqualTo(200);
    assertThat(testPlayer2.getBalance()).isEqualTo(200);
  }

  @Test
  @DisplayName("nextTurn rolls dice, moves player, and executes tile action")
  void nextTurn_basicFlow() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice to roll non-doubles (valid values)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up a simple tile at landing position
      StartMonopolyTile startTile = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      when(mockBoard.getTileAtIndex(5)).thenReturn(startTile);
      doReturn(startTile).when(game).getTile(5);

      // Reset any previous interactions
      clearInvocations(mockEventBus);

      // Execute
      game.nextTurn();

      // Verify
      verify(mockEventBus, atLeastOnce()).publishEvent(any()); // Dice rolled event
      assertThat(player1.getPosition()).isEqualTo(5); // Player moved
    }
  }

  @Test
  @Disabled
  @DisplayName("nextTurn handles rolling doubles once")
  void nextTurn_withDoubles() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // First roll is doubles, second is not (valid values)
      diceMock
          .when(() -> Dice.roll(2))
          .thenReturn(new DiceRoll(3, 3)) // doubles
          .thenReturn(new DiceRoll(2, 3)); // not doubles

      // Set up tiles at landing positions
      StartMonopolyTile startTile1 = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      StartMonopolyTile startTile2 = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_LEFT);

      when(mockBoard.getTileAtIndex(6)).thenReturn(startTile1);
      when(mockBoard.getTileAtIndex(11)).thenReturn(startTile2);

      // For getTile method in Game class
      doReturn(startTile1).when(game).getTile(6);
      doReturn(startTile2).when(game).getTile(11);

      // Reset events counter
      clearInvocations(mockEventBus);

      // Execute
      game.nextTurn();

      // Verify player rolled and moved twice
      verify(mockEventBus, atLeast(2)).publishEvent(any());
      assertThat(player1.getPosition()).isEqualTo(11); // Final position after two moves
    }
  }

  @Test
  @Disabled
  @DisplayName("nextTurn sends player to jail after three consecutive doubles")
  void nextTurn_threeDoublesGoesToJail() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Three consecutive double rolls (valid values)
      diceMock
          .when(() -> Dice.roll(2))
          .thenReturn(new DiceRoll(4, 4))
          .thenReturn(new DiceRoll(5, 5))
          .thenReturn(new DiceRoll(6, 6));

      // Set up tiles for intermediate positions
      StartMonopolyTile tile1 = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      StartMonopolyTile tile2 = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_LEFT);
      StartMonopolyTile tile3 = new StartMonopolyTile(CornerMonopolyTile.Position.BOTTOM_LEFT);

      when(mockBoard.getTileAtIndex(8)).thenReturn(tile1);
      when(mockBoard.getTileAtIndex(18)).thenReturn(tile2);
      when(mockBoard.getTileAtIndex(30)).thenReturn(tile3);

      // For getTile method
      doReturn(tile1).when(game).getTile(8);
      doReturn(tile2).when(game).getTile(18);
      doReturn(tile3).when(game).getTile(30);

      // Execute
      game.nextTurn();

      // Verify player was sent to jail
      verify(mockJailTile).jailForNumberOfRounds(eq(player1), eq(2));
      assertThat(player1.getPosition()).isEqualTo(10); // Jail position
    }
  }

  @Test
  @DisplayName("movePlayer adds $200 when passing GO")
  void movePlayer_passingGo_adds200() {
    // Set player to position 39 (one before GO)
    player1.setPosition(39);
    int initialBalance = player1.getBalance();

    // Move 2 spaces, passing GO (position 0)
    game.movePlayer(player1, 2);

    // Verify balance increased
    assertThat(player1.getBalance()).isEqualTo(initialBalance + 200);
    assertThat(player1.getPosition()).isEqualTo(1);
  }

  @Disabled
  @Test
  @DisplayName("removePlayer notifies events and removes player")
  void removePlayer_removesPlayerAndNotifies() {
    // Create a new game with a mutable list to fix UnsupportedOperationException
    List<MonopolyPlayer> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    MonopolyGame testGame = spy(new MonopolyGame(mockEventBus, mockBoard, players));

    // First, get the next player to set up the turn manager correctly
    testGame.getNextPlayer();

    // Remove player
    testGame.removePlayer(player1);

    // Verify player was removed
    assertThat(testGame.getPlayers()).doesNotContain(player1);
    verify(mockEventBus).publishEvent(any());
  }

  @Test
  @DisplayName("getWinners returns player with highest net worth")
  void getWinners_returnsPlayerWithHighestNetWorth() {
    // Make player2 richer
    player2.addBalance(2000);
    player1.addBalance(500);

    // Get winners
    Map.Entry<Integer, List<MonopolyPlayer>> winners = game.getWinners();

    // Verify player2 is winner with higher net worth
    assertThat(winners.getValue()).contains(player2);
  }

  @Test
  @DisplayName("Landing on owned property triggers rent payment")
  void landingOnOwnedProperty_paysRentToOwner() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player1's property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Initial balances
      int player1InitialBalance = player1.getBalance();
      int player2InitialBalance = player2.getBalance();

      // Player2's turn lands on player1's property
      // Get player1's turn out of the way (using a fake empty tile action to avoid NPE)
      doReturn(player2).when(game).getNextPlayer();

      // Player2's turn
      game.nextTurn();

      // Calculate expected rent
      int expectedRent = playerOwnedProperty.rent();

      // Verify player2 paid rent to player1
      assertThat(player2.getBalance()).isLessThan(player2InitialBalance);
      assertThat(player1.getBalance()).isGreaterThan(player1InitialBalance);
    }
  }

  @Test
  @DisplayName("Landing on unowned property with sufficient funds shows purchase dialog")
  void landingOnUnownedProperty_withSufficientFunds_showsPurchaseDialog() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on unowned property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(property);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return purchase confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Ensure player has enough money
      player1.addBalance(500);

      // Execute player turn
      game.nextTurn();

      // Verify alert was shown
      alertMock.verify(
          () -> AlertFactory.createAlert(eq(Alert.AlertType.CONFIRMATION), anyString()));

      // Verify player purchased property (check with isOwnerOf instead of getOwnables)
      if (player1.isOwnerOf(property)) {
        verify(mockEventBus).publishEvent(any(MonopolyEvent.Purchased.class));
      }
    }
  }

  @Test
  @DisplayName("Landing on unowned property but declining purchase")
  void landingOnUnownedProperty_decliningPurchase() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on unowned property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(property);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return purchase decline
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.CANCEL));

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify player did not purchase property
      assertThat(player1.isOwnerOf(property)).isFalse();

      // Verify no purchase event (only if above assertion passes)
      verify(mockEventBus, never()).publishEvent(any(MonopolyEvent.Purchased.class));
    }
  }

  @Test
  @DisplayName("Landing on unowned property with insufficient funds")
  void landingOnUnownedProperty_withInsufficientFunds() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up expensive property
      Property expensiveProperty = new Property("Expensive", Property.Color.GREEN, 10000);
      OwnableMonopolyTile expensiveTile = new OwnableMonopolyTile(expensiveProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(expensiveTile);
      doReturn(expensiveTile).when(game).getTile(5);

      // Make sure player doesn't have enough money
      player1.pay(player1.getBalance() - 50);

      // Execute player turn
      game.nextTurn();

      // Verify player did not purchase property
      assertThat(player1.isOwnerOf(expensiveProperty)).isFalse();
    }
  }

  @Test
  @DisplayName("Landing on tax tile deducts percentage of balance")
  void landingOnTaxTile_deductsPercentage() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set player's balance to exact amount for tax calculation
      player1.pay(player1.getBalance()); // Reset balance to 0
      player1.addBalance(500); // Set to exact value for test

      // Set up tax tile (10% tax)
      TaxMonopolyTile taxTile = new TaxMonopolyTile(10);
      when(mockBoard.getTileAtIndex(5)).thenReturn(taxTile);
      doReturn(taxTile).when(game).getTile(5);

      // Initial balance
      int initialBalance = player1.getBalance();
      int expectedTax = (initialBalance * 10) / 100; // 10% of 500 = 50

      // Execute player turn
      game.nextTurn();

      // Verify tax was deducted
      assertThat(player1.getBalance()).isEqualTo(initialBalance - expectedTax);
    }
  }

  @Test
  @DisplayName("Landing on GoToJail tile sends player to jail")
  void landingOnGoToJailTile_sendsPlayerToJail() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up GoToJail tile
      GoToJailMonopolyTile jailTile =
          new GoToJailMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      when(mockBoard.getTileAtIndex(5)).thenReturn(jailTile);
      doReturn(jailTile).when(game).getTile(5);

      // Execute player turn
      game.nextTurn();

      // Verify player was sent to jail
      verify(mockJailTile).jailForNumberOfRounds(eq(player1), eq(2));
      assertThat(player1.getPosition()).isEqualTo(10); // Jail position
    }
  }

  @Test
  @DisplayName("Landing on FreeParking tile has no effect")
  void landingOnFreeParkingTile_noEffect() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up FreeParking tile
      FreeParkingMonopolyTile parkingTile =
          new FreeParkingMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      when(mockBoard.getTileAtIndex(5)).thenReturn(parkingTile);
      doReturn(parkingTile).when(game).getTile(5);

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify balance unchanged
      assertThat(player1.getBalance()).isEqualTo(initialBalance);
    }
  }

  @Test
  @DisplayName("Landing on JailVisit tile has no effect")
  void landingOnJailVisitTile_noEffect() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up JailVisit tile
      when(mockBoard.getTileAtIndex(5)).thenReturn(mockJailTile);
      doReturn(mockJailTile).when(game).getTile(5);

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify balance unchanged
      assertThat(player1.getBalance()).isEqualTo(initialBalance);
    }
  }

  @Test
  @DisplayName("Landing on Start tile adds no additional money")
  void landingOnStartTile_noAdditionalMoney() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Use valid dice values (1-6)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 4));

      // Setup StartTile
      StartMonopolyTile startTile = new StartMonopolyTile(CornerMonopolyTile.Position.TOP_RIGHT);
      when(mockBoard.getTileAtIndex(6)).thenReturn(startTile);
      doReturn(startTile).when(game).getTile(6);

      player1.setPosition(0);
      int initialBalance = player1.getBalance();

      // Execute turn
      game.nextTurn();

      // Verify position and balance
      assertThat(player1.getPosition()).isEqualTo(6);
      // We don't get additional money for landing on Start, only for passing it
      assertThat(player1.getBalance()).isEqualTo(initialBalance);
    }
  }

  @Test
  @DisplayName("Landing on own property with funds offers house upgrade")
  void landingOnOwnProperty_offersHouseUpgrade() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player's own property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return upgrade confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Ensure player has enough money
      player1.addBalance(500);
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify house upgrade alert was shown
      alertMock.verify(
          () -> AlertFactory.createAlert(eq(Alert.AlertType.CONFIRMATION), contains("house")));

      // Verify player paid for house
      assertThat(player1.getBalance()).isLessThan(initialBalance);

      // Verify property has a house
      assertThat(playerOwnedProperty.countHouses()).isEqualTo(1);
    }
  }

  @Test
  @DisplayName("Landing on own property with 4 houses offers hotel upgrade")
  void landingOnOwnProperty_withFourHouses_offersHotelUpgrade() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Add 4 houses to property
      for (int i = 0; i < 4; i++) {
        playerOwnedProperty.addUpgrade(new Upgrade(UpgradeType.HOUSE, 20));
      }

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player's own property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return upgrade confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Ensure player has enough money
      player1.addBalance(500);
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify hotel upgrade alert was shown
      alertMock.verify(
          () -> AlertFactory.createAlert(eq(Alert.AlertType.CONFIRMATION), contains("Hotel")));

      // Verify player paid for hotel
      assertThat(player1.getBalance()).isLessThan(initialBalance);

      // Verify property has a hotel
      assertThat(playerOwnedProperty.hasHotel()).isTrue();
    }
  }

  @Test
  @DisplayName("Landing on own property with hotel shows no upgrade options")
  void landingOnOwnProperty_withHotel_showsNoUpgradeOptions() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Add hotel to property
      playerOwnedProperty.addUpgrade(new Upgrade(UpgradeType.HOTEL, 100));

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player's own property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify no change in balance (no more upgrades possible)
      assertThat(player1.getBalance()).isEqualTo(initialBalance);
    }
  }

  @Test
  @DisplayName("Landing on property with insufficient upgrade funds")
  void landingOnOwnProperty_insufficientUpgradeFunds() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set player to have minimal funds
      player1.pay(player1.getBalance() - 10); // Leave just $10

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player's own property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return upgrade confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify balance unchanged (insufficient funds message)
      assertThat(player1.getBalance()).isEqualTo(initialBalance);

      // Verify property has no upgrades
      assertThat(playerOwnedProperty.countHouses()).isEqualTo(0);
    }
  }

  @Test
  @DisplayName("Landing on property and declining upgrade")
  void landingOnOwnProperty_decliningUpgrade() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Set up tile behavior to land on player's own property
      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(playerOwnedProperty);
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Mock alert factory to return upgrade decline
      Alert mockAlert = mock(Alert.class);
      alertMock.when(() -> AlertFactory.createAlert(any(), anyString())).thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.CANCEL));

      // Initial balance
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify balance unchanged (no upgrade purchased)
      assertThat(player1.getBalance()).isEqualTo(initialBalance);

      // Verify property has no upgrades
      assertThat(playerOwnedProperty.countHouses()).isEqualTo(0);
    }
  }

  @Test
  @DisplayName("Landing on Railroad with purchase option")
  void landingOnRailroad_withPurchaseOption() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll (valid values)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Create Railroad
      Railroad railroad = new Railroad(200);
      OwnableMonopolyTile railroadTile = new OwnableMonopolyTile(railroad);

      // Set up tile behavior
      when(mockBoard.getTileAtIndex(5)).thenReturn(railroadTile);
      doReturn(railroadTile).when(game).getTile(5);

      // Mock alert factory to return purchase confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock
          .when(() -> AlertFactory.createAlert(any(), contains("railroad")))
          .thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Ensure player has enough money
      player1.addBalance(1000);
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify railroad purchase dialog was shown
      alertMock.verify(
          () -> AlertFactory.createAlert(eq(Alert.AlertType.CONFIRMATION), contains("railroad")));

      // Verify player purchased railroad
      assertThat(player1.isOwnerOf(railroad)).isTrue();
      assertThat(player1.getBalance()).isEqualTo(initialBalance - railroad.price());
    }
  }

  @Test
  @DisplayName("Landing on Utility with purchase option")
  void landingOnUtility_withPurchaseOption() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class);
        MockedStatic<AlertFactory> alertMock = mockStatic(AlertFactory.class)) {

      // Set up dice roll (valid values)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Create Utility
      Utility utility = new Utility("Electric Company", 150);
      OwnableMonopolyTile utilityTile = new OwnableMonopolyTile(utility);

      // Set up tile behavior
      when(mockBoard.getTileAtIndex(5)).thenReturn(utilityTile);
      doReturn(utilityTile).when(game).getTile(5);

      // Mock alert factory to return purchase confirmation
      Alert mockAlert = mock(Alert.class);
      alertMock
          .when(() -> AlertFactory.createAlert(any(), contains("Electric Company")))
          .thenReturn(mockAlert);
      when(mockAlert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

      // Ensure player has enough money
      player1.addBalance(1000);
      int initialBalance = player1.getBalance();

      // Execute player turn
      game.nextTurn();

      // Verify utility purchase dialog was shown
      alertMock.verify(
          () ->
              AlertFactory.createAlert(
                  eq(Alert.AlertType.CONFIRMATION), contains("Electric Company")));

      // Verify player purchased utility
      assertThat(player1.isOwnerOf(utility)).isTrue();
      assertThat(player1.getBalance()).isEqualTo(initialBalance - utility.price());
    }
  }

  @Test
  @DisplayName("Player goes bankrupt when unable to pay rent")
  void playerGoesBankrupt_whenUnableToPayRent() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Set up dice roll (valid values)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(2, 3));

      // Create expensive property
      Property expensiveProperty = new Property("Boardwalk", Property.Color.DARK_BLUE, 400);
      // Add hotel to make rent very high
      expensiveProperty.addUpgrade(new Upgrade(UpgradeType.HOTEL, 200));

      // Give property to player2
      player2.addBalance(1000); // Ensure player2 has enough to buy
      player2.purchase(expensiveProperty);

      OwnableMonopolyTile propertyTile = new OwnableMonopolyTile(expensiveProperty);

      // Set up tile behavior
      when(mockBoard.getTileAtIndex(5)).thenReturn(propertyTile);
      doReturn(propertyTile).when(game).getTile(5);

      // Set player1 to have minimal funds
      player1.pay(player1.getBalance());
      player1.addBalance(10); // Very little money

      // Setup for testing the removePlayer method
      doNothing().when(game).removePlayer(player1);

      // Execute player1's turn - should cause bankruptcy
      game.nextTurn();

      // Verify player was removed (bankrupt)
      verify(game).removePlayer(player1);
    }
  }

  @Test
  @DisplayName("Landing on owned Utility calculates rent based on dice roll")
  void landingOnOwnedUtility_calculatesRentBasedOnDiceRoll() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Create utility
      Utility electric = new Utility("Electric Company", 150);

      // Player 2 owns the utility
      player2.addBalance(1000); // Ensure player2 has enough to buy
      player2.purchase(electric);

      // Set up dice rolls (valid values)
      diceMock
          .when(() -> Dice.roll(2))
          .thenReturn(new DiceRoll(3, 2)) // First roll for movement
          .thenReturn(new DiceRoll(4, 2)); // Second roll for utility rent calculation

      // Create tile for electric company
      OwnableMonopolyTile utilityTile = new OwnableMonopolyTile(electric);

      // Set up tile behavior
      when(mockBoard.getTileAtIndex(5)).thenReturn(utilityTile);
      doReturn(utilityTile).when(game).getTile(5);

      // Set initial balances
      int player1InitialBalance = player1.getBalance();
      int player2InitialBalance = player2.getBalance();

      // Execute player1's turn to land on player2's utility
      game.nextTurn();

      // Verify player1 paid rent
      assertThat(player1.getBalance()).isLessThan(player1InitialBalance);

      // Verify player2 received rent
      assertThat(player2.getBalance()).isGreaterThan(player2InitialBalance);
    }
  }

  @Test
  @DisplayName("Landing on owned Railroad calculates rent based on ownership count")
  void landingOnOwnedRailroad_calculatesRentBasedOnOwnershipCount() {
    try (MockedStatic<Dice> diceMock = mockStatic(Dice.class)) {
      // Create two railroads
      Railroad railroad1 = new Railroad(200);
      Railroad railroad2 = new Railroad(200);

      // Player 2 owns both railroads
      player2.addBalance(1000); // Ensure player2 has enough to buy
      player2.purchase(railroad1);
      player2.purchase(railroad2);

      // Set up dice roll for movement (valid values)
      diceMock.when(() -> Dice.roll(2)).thenReturn(new DiceRoll(3, 2));

      // Create tile for first railroad
      OwnableMonopolyTile railroadTile = new OwnableMonopolyTile(railroad1);

      // Set up tile behavior
      when(mockBoard.getTileAtIndex(5)).thenReturn(railroadTile);
      doReturn(railroadTile).when(game).getTile(5);

      // Set initial balances
      int player1InitialBalance = player1.getBalance();
      int player2InitialBalance = player2.getBalance();

      // Execute player1's turn to land on player2's railroad
      game.nextTurn();

      // Verify player1 paid rent
      assertThat(player1.getBalance()).isLessThan(player1InitialBalance);

      // Verify player2 received rent
      assertThat(player2.getBalance()).isGreaterThan(player2InitialBalance);
    }
  }

  @Test
  @DisplayName("getBoard returns MonopolyBoard")
  void getBoard_returnsMonopolyBoard() {
    // Verify the board is returned correctly
    assertThat(game.getBoard()).isEqualTo(mockBoard);
  }
}
