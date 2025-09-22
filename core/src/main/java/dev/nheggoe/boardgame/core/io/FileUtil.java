package dev.nheggoe.boardgame.core.io;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class for handling file and directory operations. Provides methods to ensure that a
 * specified file and its parent directories exist, creating them if necessary. Supports generating
 * paths for standard and test environments with validated extensions.
 *
 * @author Nick Heggø
 * @version 2025.05.22
 */
public class FileUtil {

  private static final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());

  private static final String FILE_PATH_TEMPLATE = "data/%s/%s.%s";
  private static final List<String> SUPPORTED_FILE_EXTENSIONS = List.of("json", "csv");

  private FileUtil() {}

  /**
   * Generates a file path based on a given file name and file extension. The method validates the
   * file extension against a predefined list of supported extensions and formats the file path
   * using a specified template.
   *
   * @param fileName the name of the file; must not be null
   * @param fileExtension the file extension; must not be null and must be supported
   * @return the generated file path
   * @throws NullPointerException if fileName or fileExtension is null
   * @throws UnsupportedOperationException if the file extension is not supported
   */
  public static Path generateFilePath(String fileName, String fileExtension) {
    requireNonNull(fileName, "File name cannot be null");
    requireNonNull(fileExtension, "File extension cannot be null");
    String ext = fileExtension.toLowerCase().strip();
    if (!SUPPORTED_FILE_EXTENSIONS.contains(ext)) {
      throw new UnsupportedOperationException("Unsupported file extension: " + fileExtension);
    }
    String name = fileName.strip();
    return Path.of(FILE_PATH_TEMPLATE.formatted(ext, name, ext));
  }

  /**
   * Ensures that the specified file and its parent directories exist. If the directories do not
   * exist, they will be created. If the file does not exist, it will be created.
   *
   * @param path the path to the file to ensure exists; must not be null
   * @throws IOException if file or directories cannot be created
   */
  public static void ensureFileAndDirectoryExists(Path path) throws IOException {
    requireNonNull(path, "The path cannot be null");

    Files.createDirectories(path.getParent());

    if (Files.notExists(path)) {
      Files.createFile(path);
      LOGGER.fine(() -> "Created file: " + path.toAbsolutePath());
    }
  }

  /**
   * Ensures that the specified file exists. If the file doesn't exist or is corrupted, it will be
   * created from the default resource file.
   *
   * @param path the path to the file to ensure exists
   * @param defaultResourcePath the path to the default resource file (e.g.,
   *     "/csv/players_default.csv")
   * @throws IOException if file operations fail
   */
  public static void ensureFileWithDefault(Path path, String defaultResourcePath)
      throws IOException {
    requireNonNull(path, "The path cannot be null");
    requireNonNull(defaultResourcePath, "The default resource path cannot be null");

    try {
      // First ensure directory exists
      Files.createDirectories(path.getParent());

      // Check if file exists and is readable
      if (!Files.exists(path) || !Files.isReadable(path) || Files.size(path) == 0) {
        copyFromResource(defaultResourcePath, path);
        LOGGER.info(() -> "Created file from default resource: " + path.toAbsolutePath());
      }
    } catch (IOException e) {
      LOGGER.severe(() -> "Failed to ensure file exists: " + path + " — " + e.getMessage());
    }
  }

  /**
   * Copies a resource file to the specified destination path.
   *
   * @param resourcePath the path to the resource file (must start with /)
   * @param destinationPath the destination path
   * @throws IOException if the resource cannot be found or copied
   */
  public static void copyFromResource(String resourcePath, Path destinationPath)
      throws IOException {
    try (InputStream resourceStream = FileUtil.class.getResourceAsStream(resourcePath)) {
      if (resourceStream == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }

      Files.copy(resourceStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
      LOGGER.fine(() -> "Copied resource " + resourcePath + " to " + destinationPath);
    }
  }

  /**
   * Validates if a file exists and is not corrupted (readable and non-empty).
   *
   * @param path the file path to validate
   * @return true if the file is valid, false otherwise
   */
  public static boolean isFileValid(Path path) {
    try {
      return Files.exists(path) && Files.isReadable(path) && Files.size(path) > 0;
    } catch (IOException e) {
      LOGGER.warning(() -> "Error checking file validity: " + path + " — " + e.getMessage());
      return false;
    }
  }
}
