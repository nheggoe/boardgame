package dev.nheggoe.boardgame.core.common.io.csv;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CSVWriterTest {

  @TempDir private Path tempDir;

  private Path csvFile;

  @BeforeEach
  void setUp() {
    csvFile = tempDir.resolve("test.csv");
  }

  @DisplayName("Test valid CSV format")
  @ParameterizedTest
  @MethodSource("validCsvDate")
  void test_writeLines_with_validData(List<String[]> rows) throws IOException {

    CSVWriter.writeLines(csvFile, rows);
    assertThat(csvFile).exists().isNotEmptyFile();

    String expectedOutput =
        """
        Player,Figure
        Nick,Hat
        Misha,BattleShip
        """;
    assertThat(csvFile).content().isEqualTo(expectedOutput);
  }

  @DisplayName("Test invalid CSV format")
  @ParameterizedTest
  @MethodSource("invalidCsvDate")
  void test_writeLines_with_invalidData(List<String[]> rows) {
    assertThatCode(() -> CSVWriter.writeLines(csvFile, rows)).doesNotThrowAnyException();
  }

  static Stream<List<String[]>> validCsvDate() {
    return Stream.of(
        List.of(
            new String[] {"Player", "Figure"},
            new String[] {"Nick", "Hat"},
            new String[] {"Misha", "BattleShip"}));
  }

  static Stream<List<String[]>> invalidCsvDate() {
    return Stream.of(
        List.of(
            new String[] {"Player", "Figure"},
            new String[] {"Nick", ""},
            new String[] {"Misha", "BattleShip"}));
  }
}
