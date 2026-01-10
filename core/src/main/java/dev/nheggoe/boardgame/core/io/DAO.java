package dev.nheggoe.boardgame.core.io;

import java.util.Set;
import java.util.stream.Stream;

/// Data Access Object (DAO) interface that provides methods for serializing and deserializing
/// entities of a specified generic type. Implementations of this interface should provide concrete
/// mechanisms for reading from a data source and writing to a data source.
///
/// @param <T> the type of the entities managed by this DAO
/// @author Nick Heggø
/// @version 2025.05.08
public interface DAO<T> {

  /// Deserializes a collection of objects from a defined data source.
  ///
  /// This method reads data from the underlying source (e.g., JSON file, database, etc.), and
  /// converts it into a stream of objects of the specified type. If the data source does not exist
  /// or is empty, an empty stream will be returned.
  ///
  /// @return a stream of objects deserialized from the data source, or an empty stream if no data is
  ///     found
  Stream<T> deserializeFromSource();

  /// Serializes a set of entities to a designated data source. The implementation ensures that the
  /// entities are properly written to the source, potentially overwriting existing data if already
  /// present. The specific behavior (e.g., writing to a JSON file, database, etc.) is determined by
  /// the underlying implementation.
  ///
  /// @param entities the set of entities to be serialized; must not be null
  void serializeToSource(Set<T> entities);
}
