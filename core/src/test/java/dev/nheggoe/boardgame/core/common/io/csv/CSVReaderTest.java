package dev.nheggoe.boardgame.core.common.io.csv;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.nheggoe.boardgame.core.io.csv.CSVReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CSVReaderTest {

  @Test
  void test_skipComments(@TempDir Path tempDir) throws IOException {
    var testCsv = tempDir.resolve("test.csv");
    Files.writeString(testCsv, "   #  this, is a test ");
    assertThat(CSVReader.readAll(testCsv)).isEmpty();
  }
}
