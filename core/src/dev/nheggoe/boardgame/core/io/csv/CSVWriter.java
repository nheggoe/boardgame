package dev.nheggoe.boardgame.core.io.csv;

import dev.nheggoe.boardgame.core.io.FileUtil;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

/// The CSVWriter class provides utility methods for writing data to a CSV file. It writes rows of
/// data provided as a list of string arrays, with values in each row joined by commas.
///
/// Each call ensures that the file and its parent directory exist before writing.
///
/// @author Nick Heggø
/// @version 2025.05.22
public class CSVWriter {

  private static final Logger LOGGER = Logger.getLogger(CSVWriter.class.getName());

  private CSVWriter() {}

  /// Writes the rows of data to the specified CSV file. Each row is represented as a string array,
  /// with the values joined by commas. The method ensures that the file and its parent directories
  /// exist before writing.
  ///
  /// @param csvFile the path to the CSV file where the data should be written
  /// @param rows the list of string arrays representing rows of data to write to the file
  /// @throws IOException if an I/O error occurs during file creation or writing
  public static void writeLines(Path csvFile, List<String[]> rows) throws IOException {
    FileUtil.ensureFileAndDirectoryExists(csvFile);
    try (BufferedWriter writer = Files.newBufferedWriter(csvFile)) {
      for (var row : rows) {
        writer.write(String.join(",", row));
        writer.newLine();
      }
    }
    LOGGER.fine(() -> "Wrote %d rows to %s".formatted(rows.size(), csvFile));
  }
}
