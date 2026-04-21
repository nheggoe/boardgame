package dev.nheggoe.boardgame.core.repository;

import static java.util.Objects.requireNonNull;

import dev.nheggoe.boardgame.core.io.json.JsonService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Stream;

/// An abstract base class for repositories that manage entities of a specified type using JSON as
/// the underlying storage mechanism. This class integrates a JSON service to handle the
/// serialization and deserialization of entities and provides methods that adhere to the contract
/// defined in the [DataRepository] interface.
///
/// @param <T> The type of entities this repository will manage.
/// @author Nick Heggø
/// @version 2025.05.20
public abstract class JsonRepository<T> implements DataRepository<T> {

  private final ConcurrentMap<UUID, T> entities;
  private final JsonService<T> jsonService;
  private final Function<T, UUID> idExtractor;

  /// Constructs an instance of the JsonRepository class. This protected constructor allows
  /// subclasses to provide the required [JsonService] for JSON operations and a function to
  /// extract unique identifiers from entities. It initializes an in-memory store of entities by
  /// deserializing them from the JSON source.
  ///
  /// @param jsonService the JSON service responsible for serialization and deserialization of
  ///     entities
  /// @param idExtractor a function for extracting the unique identifier (UUID) from an entity
  /// @throws NullPointerException if either jsonService or idExtractor is null
  protected JsonRepository(JsonService<T> jsonService, Function<T, UUID> idExtractor) {
    this.jsonService = requireNonNull(jsonService, "jsonService cannot be null!");
    this.idExtractor = requireNonNull(idExtractor, "idExtractor cannot be null!");
    this.entities = new ConcurrentHashMap<>();
    initializeEntities();
  }

  private void initializeEntities() {
    loadFromSource().forEach(entity -> entities.put(idExtractor.apply(entity), entity));
  }

  @Override
  public Stream<T> loadFromSource() {
    return jsonService.deserializeFromSource();
  }

  @Override
  public void saveToSource(Set<T> entities) {
    jsonService.serializeToSource(entities);
  }

  @Override
  public T add(T entity) {
    return entities.put(idExtractor.apply(entity), entity);
  }

  @Override
  public Stream<T> getAll() {
    return entities.values().stream();
  }

  @Override
  public Optional<T> getById(UUID id) {
    return Optional.ofNullable(entities.get(id));
  }

  @Override
  public T update(T entity) {
    return entities.computeIfPresent(idExtractor.apply(entity), (k, v) -> entity);
  }

  @Override
  public boolean remove(UUID id) {
    return entities.remove(id) != null;
  }
}
