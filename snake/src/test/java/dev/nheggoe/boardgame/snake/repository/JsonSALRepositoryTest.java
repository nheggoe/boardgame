package dev.nheggoe.boardgame.snake.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import dev.nheggoe.boardgame.common.event.EventBus;
import dev.nheggoe.boardgame.common.io.FileUtil;
import dev.nheggoe.boardgame.common.io.json.JsonService;
import dev.nheggoe.boardgame.core.model.Game;
import dev.nheggoe.boardgame.core.model.Player;
import dev.nheggoe.boardgame.games.snake.model.SnakeAndLadderBoard;
import dev.nheggoe.boardgame.games.snake.model.SnakeAndLadderGame;
import dev.nheggoe.boardgame.games.snake.model.SnakeAndLadderPlayer;
import dev.nheggoe.boardgame.games.snake.model.tile.NormalTile;
import dev.nheggoe.boardgame.games.snake.model.tile.SnakeTile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
class JsonSALRepositoryTest {

  private MockedStatic<FileUtil> mockedFileUtil;

  @TempDir private Path tempDir;

  private Path jsonFile;

  private JsonSALRepository jsonSALRepository;
  private SnakeAndLadderGame game;

  @BeforeEach
  void setUp() throws IOException {
    jsonFile = tempDir.resolve("test.json");
    FileUtil.ensureFileAndDirectoryExists(jsonFile);
    mockedFileUtil = mockStatic(FileUtil.class);
    mockedFileUtil.when(() -> FileUtil.generateFilePath(any(), any())).thenReturn(jsonFile);

    var jsonService = new JsonService<>(SnakeAndLadderGame.class);
    jsonSALRepository = new JsonSALRepository(jsonService, Game::getId);

    var eventBus = mock(EventBus.class);
    var board =
        new SnakeAndLadderBoard(List.of(new NormalTile(), new NormalTile(), new SnakeTile(1)));
    var players = List.of(new SnakeAndLadderPlayer("Nick", Player.Figure.HAT));
    game = new SnakeAndLadderGame(eventBus, board, players);
  }

  @AfterEach
  void tearDown() {
    mockedFileUtil.close();
  }

  @Test
  void testAdd() {
    jsonSALRepository.add(game);
    assertThat(jsonSALRepository.getAll()).containsExactly(game);
  }

  @Test
  void testGetById() {
    jsonSALRepository.add(game);

    UUID gameId = game.getId();
    Optional<SnakeAndLadderGame> retrievedGame = jsonSALRepository.getById(gameId);

    assertThat(retrievedGame).isPresent().contains(game);
  }

  @Test
  void testGetByIdWithNonExistentId() {
    // Try to get a game with a non-existent ID
    UUID nonExistentId = UUID.randomUUID();
    Optional<SnakeAndLadderGame> retrievedGame = jsonSALRepository.getById(nonExistentId);

    // Verify
    assertThat(retrievedGame).isEmpty();
  }

  @Test
  void testUpdate() {

    // Add to repository
    jsonSALRepository.add(game);

    // Update game state
    game.endGame();
    jsonSALRepository.update(game);

    // Get the updated game
    Optional<SnakeAndLadderGame> updatedGame = jsonSALRepository.getById(game.getId());

    // Verify
    assertThat(updatedGame).isPresent();
    assertThat(updatedGame.get().isEnded()).isTrue();
  }

  @Test
  void testRemove() {
    // Create a game

    // Add to repository
    jsonSALRepository.add(game);

    // Verify game is in repository
    assertThat(jsonSALRepository.getAll()).containsExactly(game);

    // Remove the game
    boolean removed = jsonSALRepository.remove(game.getId());

    // Verify game was removed
    assertThat(removed).isTrue();
    assertThat(jsonSALRepository.getAll()).isEmpty();
  }

  @Test
  void testSaveToSource() {

    // Add to repository
    jsonSALRepository.add(game);

    // Save to source
    Set<SnakeAndLadderGame> games = Set.of(game);
    jsonSALRepository.saveToSource(games);
    assertThat(jsonFile).isNotEmptyFile().content().contains("start");
  }
}
