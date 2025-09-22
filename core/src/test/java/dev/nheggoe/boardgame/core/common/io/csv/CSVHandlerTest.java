package dev.nheggoe.boardgame.core.common.io.csv;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.nheggoe.boardgame.core.io.csv.CSVHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CSVHandlerTest {

  @TempDir private Path tempDir;

  private Path csvFile;
  private CSVHandler csvHandler;

  @BeforeEach
  void setUp() {
    csvFile = tempDir.resolve("test.csv");
    csvHandler = new CSVHandler(csvFile);
  }

  @Test
  void test_skipComment() throws IOException {
    Files.writeString(csvFile, "   #  this, is a test ");
    assertThat(csvHandler.readAll()).isNotNull().isEmpty();

    Files.writeString(csvFile, "# this is a comment");
    assertThat(csvHandler.readAll()).isNotNull().isEmpty();
  }

  @Test
  void test_readBasic() throws IOException {
    Files.writeString(csvFile, "Player,Figure\nNick,Hat\nMisha,BattleShip");
    assertThat(csvHandler.readAll()).hasSize(3);
  }
}
