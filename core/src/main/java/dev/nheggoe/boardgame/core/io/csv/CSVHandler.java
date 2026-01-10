package dev.nheggoe.boardgame.core.io.csv;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.io.FileUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/// The `CSVHandler` class is responsible for reading and writing data to a CSV file. The file
/// is created if it does not exist.
///
/// @author Mihailo Hranisavljevic and Nick Heggø
/// @version 2025.05.19
public class CSVHandler {

  private final Path csvFile;

  /// Constructs a new `CSVHandler` for managing the specified CSV file. Ensures that the file
  /// and its parent directories exist, creating them if necessary.
  ///
  /// @param csvFile the path to the CSV file to be managed; must not be null
  /// @throws IllegalStateException if the file cannot be created
  public CSVHandler(Path csvFile) {
    this.csvFile = requireNonNull(csvFile);
    try {
      FileUtil.ensureFileAndDirectoryExists(this.csvFile);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to create CSV file: " + csvFile, e);
    }
  }

  /// Reads all data rows from the associated CSV file. The lines in the file starting with a comment
  /// character (#) are ignored. Each line is split by comma into an array of strings, with leading
  /// and trailing whitespace for each value trimmed.
  ///
  /// @return a list of string arrays, where each array represents a row of data from the CSV file,
  ///     excluding lines considered as comments
  /// @throws IOException if an I/O error occurs while reading the file
  public List<String[]> readAll() throws IOException {
    return CSVReader.readAll(csvFile);
  }

  /// Writes a list of string arrays to the associated CSV file. Each array element in the list
  /// corresponds to a row in the CSV file, with its elements representing comma-separated values.
  /// The method ensures that the file and its parent directories exist before writing.
  ///
  /// @param rows the list of string arrays to write to the file, where each array represents a row
  ///     of data in the CSV file; must not be null
  /// @throws IOException if an I/O error occurs while writing to the file
  public void writeLines(List<String[]> rows) throws IOException {
    CSVWriter.writeLines(csvFile, rows);
  }
}
