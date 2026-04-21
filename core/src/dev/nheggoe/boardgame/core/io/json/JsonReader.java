package dev.nheggoe.boardgame.core.io.json;

import static java.util.Objects.requireNonNull;

import com.google.gson.Gson;
import dev.nheggoe.boardgame.core.io.FileUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Stream;

/// Utility class for reading JSON files and deserializing their content into objects of a specified
/// target type. The JSON file is located dynamically based on the provided class type, with separate
/// support for test and production environments.
///
/// The class ensures that the directory structure for the JSON file is created if it does not
/// already exist. If the specified JSON file does not exist, an empty list is returned.
///
/// This class leverages a shared instance of [Gson] provided by the [CustomGson]
/// class for efficient JSON operations.
///
/// @author Nick Heggø
/// @version 2025.03.28
public class JsonReader<T> {

  private final Gson gson = CustomGson.getInstance();
  private final Class<T> targetClass;

  /// Constructs a new instance of the JsonReader class for reading JSON files and deserializing
  /// their content into objects of the specified target class type. The JSON file location is
  /// determined dynamically based on the provided target class and whether the operation is
  /// performed in a test or production environment.
  ///
  /// @param targetClass the class of object that will be used to serialize
  /// @throws IllegalArgumentException if the targetClass parameter is null
  public JsonReader(Class<T> targetClass) {
    this.targetClass = requireNonNull(targetClass, "Target class must not be null");
  }

  /// Parses a JSON file into a stream of the specified target class type. The JSON file is located
  /// at a dynamically determined path based on the class name and an environment flag (test or
  /// production). If the directory structure for the file does not exist, it will be created. If the
  /// file does not exist, it will be created as an empty file and an empty stream is returned.
  /// Otherwise, the JSON content will be deserialized into objects of the target class.
  ///
  /// @return a stream of objects deserialized from the JSON file; an empty stream if the file is
  ///     newly created
  public Stream<T> parseJsonStream() {
    var jsonFile = FileUtil.generateFilePath(targetClass.getSimpleName(), "json");

    try {
      FileUtil.ensureFileAndDirectoryExists(jsonFile);

      try (BufferedReader reader = Files.newBufferedReader(jsonFile)) {
        Set<T> entities = gson.fromJson(reader, JsonType.setOf(targetClass));
        return entities.stream();
      }
    } catch (IOException _) {
      throw new JsonException("Could not parse from JSON file: " + jsonFile);
    }
  }
}
