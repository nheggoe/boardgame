package dev.nheggoe.boardgame.core.common.io.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import dev.nheggoe.boardgame.common.event.EventBus;
import dev.nheggoe.boardgame.common.io.FileUtil;
import dev.nheggoe.boardgame.common.util.GameFactory;
import dev.nheggoe.boardgame.core.PlayerManager;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.games.snake.model.SnakeAndLadderBoard;
import dev.nheggoe.boardgame.games.snake.model.SnakeAndLadderPlayer;
import dev.nheggoe.boardgame.monopoly.board.MonopolyBoard;
import dev.nheggoe.boardgame.monopoly.ownable.MonopolyPlayer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class JsonServiceTest {

  @TempDir private Path tempDir;

  private Path jsonFile;

  private MockedStatic<FileUtil> mockedFileUtil;

  @BeforeEach
  void setUp() {
    jsonFile = tempDir.resolve("test.json");
    mockedFileUtil = mockStatic(FileUtil.class);
    mockedFileUtil
        .when(() -> FileUtil.generateFilePath(anyString(), anyString()))
        .thenReturn(jsonFile);
  }

  @AfterEach
  void tearDown() {
    mockedFileUtil.close();
  }

  @Test
  void test_read_write_SnakeAndLadderBoard() throws IOException {

    var jsonService = new JsonService<>(SnakeAndLadderBoard.class);

    var mockPlayerManager = mock(PlayerManager.class);
    when(mockPlayerManager.loadCsvAsSnakeAndLadderPlayers())
        .thenReturn(List.of(new SnakeAndLadderPlayer("John", Player.Figure.BATTLE_SHIP)));
    var board = GameFactory.createSnakeGame(new EventBus(), mockPlayerManager).getBoard();

    jsonService.serializeToSource(Set.of(board));

    var boards = jsonService.deserializeFromSource().toList();

    assertThat(boards).containsExactly(board);
  }

  @Test
  void test_read_write_MonopolyBoard() throws IOException {
    var mockEventbus = mock(EventBus.class);
    var mockPlayerManager = mock(PlayerManager.class);
    when(mockPlayerManager.loadCsvAsMonopolyPlayers())
        .thenReturn(List.of(new MonopolyPlayer("John", Player.Figure.HAT)));
    var board = GameFactory.createMonopolyGame(mockEventbus, mockPlayerManager).getBoard();

    var jsonService = new JsonService<>(MonopolyBoard.class);
    jsonService.serializeToSource(Set.of(board));

    assertThat(jsonFile)
        .isNotEmptyFile()
        .content()
        .contains("start")
        .contains("property")
        .contains("position");
  }
}
