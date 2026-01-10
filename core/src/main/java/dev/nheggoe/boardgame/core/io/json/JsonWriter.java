package dev.nheggoe.boardgame.core.io.json;

import static java.util.Objects.requireNonNull;

import com.google.gson.Gson;
import dev.nheggoe.boardgame.core.io.FileUtil;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

/// Utility class for writing collections of objects to JSON files. The JSON files are created
/// dynamically based on the provided target class type, and their location is determined by whether
/// the class is in a test or production environment.
///
/// The class relies on a shared instance of [Gson] from the [CustomGson] class for
/// efficient serialization operations. It creates the necessary directory structure before writing
/// the data if it does not already exist.
///
/// This class is designed for use with collections of objects and does not support writing
/// individual objects directly.
///
/// @author Nick Heggø
/// @version 2025.05.22
public class JsonWriter<T> {

  private final Gson gson = CustomGson.getInstance();
  private final Class<T> targetClass;

  /// Constructs a new instance of the JsonWriter class for serializing collections of objects to a
  /// JSON file. The file location is determined dynamically based on the provided target class type
  /// and whether the operation is in a test or production environment.
  ///
  /// @param targetClass the class type of the objects to be serialized; must not be null
  /// @throws IllegalArgumentException if the targetClass parameter is null
  public JsonWriter(Class<T> targetClass) {
    this.targetClass = requireNonNull(targetClass, "Target class must not be null");
  }

  /// Serializes the provided set of objects into a JSON file. The JSON file is created at a location
  /// determined based on the target class type and whether the operation is performed in a test or
  /// production environment. The method ensures that the necessary directory structure exists before
  /// writing the file.
  ///
  /// @param set the set of objects to serialize and write into the JSON file
  public void writeJsonFile(Set<T> set) {
    var jsonFile = FileUtil.generateFilePath(targetClass.getSimpleName(), "json");

    try {
      FileUtil.ensureFileAndDirectoryExists(jsonFile);

      String json = gson.toJson(set, JsonType.setOf(targetClass));

      try (BufferedWriter writer = Files.newBufferedWriter(jsonFile)) {
        writer.write(json);
      }
    } catch (IOException e) {
      throw new JsonException("Could not write to JSON file: " + jsonFile + "\n" + e.getMessage());
    }
  }
}
