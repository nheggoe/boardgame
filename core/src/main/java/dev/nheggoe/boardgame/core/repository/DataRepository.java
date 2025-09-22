package dev.nheggoe.boardgame.core.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Defines the interface of a generic repository.
 *
 * @param <T> the type of entity that the repository will manage
 * @author Nick Heggø
 * @version 2025.05.08
 */
public interface DataRepository<T> {

  /**
   * Loads entities from the underlying data source and returns them as a stream.
   *
   * @return a stream of entities loaded from the data source
   */
  Stream<T> loadFromSource();

  /**
   * Persists a given set of entities to the underlying data source.
   *
   * @param entities the set of entities to save to the data source
   */
  void saveToSource(Set<T> entities);

  /**
   * Adds a new entity in the repository.
   *
   * @param entity the entity to add
   * @return the added entity
   */
  T add(T entity);

  /**
   * Gets all entities managed by this repository.
   *
   * @return a stream of all entities
   */
  Stream<T> getAll();

  /**
   * Gets an entity by its unique identifier.
   *
   * @param id the UUID of the entity
   * @return an Optional containing the entity if found
   */
  Optional<T> getById(UUID id);

  /**
   * Updates an existing entity in the repository.
   *
   * @param entity the entity to update
   * @return the updated entity, or null if the entity doesn't exist
   */
  T update(T entity);

  /**
   * Removes an entity from the repository.
   *
   * @param id the unique identifier of the entity to remove
   * @return true if the entity was removed, false if it doesn't exist
   */
  boolean remove(UUID id);
}
