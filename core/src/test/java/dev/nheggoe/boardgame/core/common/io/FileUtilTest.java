package dev.nheggoe.boardgame.core.common.io;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;

import dev.nheggoe.boardgame.core.io.FileUtil;
import org.junit.jupiter.api.Test;

class FileUtilTest {

  @Test
  void test_generatedFilPath() {
    assertThat(FileUtil.generateFilePath("test", "json")).isEqualTo(Path.of("data/json/test.json"));
    assertThat(FileUtil.generateFilePath("test", "csv")).isEqualTo(Path.of("data/csv/test.csv"));
  }

  @Test
  void testUnsupportedFileFormat() {
    assertThatThrownBy(() -> FileUtil.generateFilePath("test", "html"))
        .isInstanceOf(UnsupportedOperationException.class);
    assertThatCode(() -> FileUtil.generateFilePath("test", "json")).doesNotThrowAnyException();
    assertThatCode(() -> FileUtil.generateFilePath("test", "csv")).doesNotThrowAnyException();
  }

  @Test
  void test_nullInputs() {

    assertThatCode(() -> FileUtil.generateFilePath("test", "json")).doesNotThrowAnyException();

    assertThatThrownBy(() -> FileUtil.generateFilePath(null, "json"))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(() -> FileUtil.generateFilePath("test", null))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(() -> FileUtil.ensureFileAndDirectoryExists(null))
        .isInstanceOf(NullPointerException.class);
  }
}
