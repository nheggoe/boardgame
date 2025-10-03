package dev.nheggoe.boardgame.app.util;

import static org.assertj.core.api.Assertions.*;

import dev.nheggoe.boardgame.core.event.EventBus;
import dev.nheggoe.boardgame.core.event.type.Event;
import dev.nheggoe.boardgame.core.event.type.UserInterfaceEvent;
import dev.nheggoe.boardgame.core.io.csv.CSVHandler;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.monopoly.model.ownable.MonopolyPlayer;
import dev.nheggoe.boardgame.snake.model.SnakeAndLadderPlayer;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerManagerTest {

  @Mock private CSVHandler csvHandler;
  @Mock private EventBus eventBus;

  @Captor ArgumentCaptor<Event> eventCaptor;

  @InjectMocks private PlayerManager playerManager;

  @BeforeEach
  void setUp() throws IOException {
    playerManager = new PlayerManager(eventBus, csvHandler);
    Mockito.when(csvHandler.readAll())
        .thenReturn(
            List.of(
                new String[] {"Name", "Figure"},
                new String[] {"John", "CAR"},
                new String[] {"Jane", "BATTLE_SHIP"}));
  }

  @Test
  void test_loadPlayersFromCSV() throws Exception {
    var monopolyPlayers = playerManager.loadCsvAsMonopolyPlayers();
    assertThat(monopolyPlayers)
        .anyMatch(
            player -> player.getName().equals("John") && player.getFigure() == Player.Figure.CAR)
        .anyMatch(
            player ->
                player.getName().equals("Jane") && player.getFigure() == Player.Figure.BATTLE_SHIP)
        .allMatch(MonopolyPlayer.class::isInstance);

    var snakeAndLadderPlayers = playerManager.loadCsvAsSnakeAndLadderPlayers();
    assertThat(snakeAndLadderPlayers)
        .anyMatch(
            player -> player.getName().equals("John") && player.getFigure() == Player.Figure.CAR)
        .anyMatch(
            player ->
                player.getName().equals("Jane") && player.getFigure() == Player.Figure.BATTLE_SHIP)
        .allMatch(SnakeAndLadderPlayer.class::isInstance);
  }

  @Test
  void test_loadPlayersFromCSV_invalid() throws Exception {
    Mockito.when(csvHandler.readAll())
        .thenReturn(List.of(new String[] {"John", "CAR"}, new String[] {"Jane", "INVALID"}));

    Assertions.assertThatCode(() -> playerManager.loadCsvAsMonopolyPlayers())
        .doesNotThrowAnyException();

    Mockito.verify(eventBus).publishEvent(eventCaptor.capture());
    assertThat(eventCaptor.getValue()).isInstanceOf(UserInterfaceEvent.Output.class);
    assertThat(((UserInterfaceEvent.Output) eventCaptor.getValue()).message())
        .contains("Invalid player data: ");
  }
}
