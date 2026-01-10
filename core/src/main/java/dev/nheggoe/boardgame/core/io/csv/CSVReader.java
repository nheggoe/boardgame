package dev.nheggoe.boardgame.core.io.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// The CSVReader class provides utility methods for reading data from a CSV file. It filters out
/// lines starting with a comment character (#).
///
/// @author Nick Heggø
/// @version 2025.05.22
public class CSVReader {

  private CSVReader() {}

  /// Reads all data rows from the specified CSV file. Lines starting with a comment character (#)
  /// are ignored. Each line is split by a comma into an array of strings, with leading and trailing
  /// whitespace for each value trimmed.
  ///
  /// @param csvFile the path to the CSV file to read
  /// @return a list of string arrays, where each array represents a row of data from the CSV file,
  ///     excluding lines considered as comments
  /// @throws IOException if an I/O error occurs while reading the file
  public static List<String[]> readAll(Path csvFile) throws IOException {
    List<String[]> lines = new ArrayList<>();
    try (BufferedReader br = Files.newBufferedReader(csvFile)) {
      String line;
      while ((line = br.readLine()) != null) {
        line = line.strip();
        if (!line.startsWith("#")) {
          String[] values =
              Arrays.stream(line.split(",", -1)).map(String::trim).toArray(String[]::new);
          lines.add(values);
        }
      }
    }
    return lines;
  }
}
