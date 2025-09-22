package dev.nheggoe.boardgame.core.io.json;

import dev.nheggoe.boardgame.core.io.DAO;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Service class for reading from and writing to JSON files. This class utilizes {@link JsonReader}
 * for deserializing JSON content into objects and {@link JsonWriter} for serializing objects into
 * JSON files. It offers functionality to handle collections of specified types.
 *
 * <p>The behavior of the JSON operations is determined by the associated class type and an optional
 * flag for distinguishing between test and production environments.
 *
 * @author Nick Heggø
 * @version 2025.03.28
 */
public class JsonService<T> implements DAO<T> {

  private final JsonReader<T> jsonReader;
  private final JsonWriter<T> jsonWriter;

  /**
   * Constructs a new instance of the JsonService class for reading from and writing to JSON files.
   * This lightweight constructor only requires the target class type and defaults the environment
   * to production.
   *
   * @throws UnsupportedOperationException if the {@link JsonType} does not contain the necessary
   *     type for serialization
   * @throws IllegalArgumentException if the targetClass parameter is null
   */
  public JsonService(Class<T> targetClass) {
    jsonReader = new JsonReader<>(targetClass);
    jsonWriter = new JsonWriter<>(targetClass);
  }

  /**
   * Loads a collection of objects from a JSON file. The JSON file is dynamically located based on
   * the target class type and whether the operation is performed in a test or production
   * environment. If the file does not exist, it is created and an empty list is returned. If the
   * file exists, the data in the file is deserialized into a list.
   *
   * @return a stream of objects deserialized from the JSON file, or an empty stream if the file is
   *     newly created
   */
  @Override
  public Stream<T> deserializeFromSource() {
    return jsonReader.parseJsonStream();
  }

  /**
   * Writes a set of objects to a JSON file. The file is created at a location dynamically
   * determined based on the target class type and whether the operation is performed in a test or
   * production environment. The method ensures that the directory structure exists before writing.
   * It will update existing data if present
   *
   * @param entities the set of objects to be written into the JSON file
   */
  @Override
  public void serializeToSource(Set<T> entities) {
    jsonWriter.writeJsonFile(entities);
  }
}
