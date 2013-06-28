package org.itspunchy.data.repository

import org.itspunchy.data.domain.IdentifiableEntity

trait CrudRepository[T <: IdentifiableEntity[ID], ID] extends Repository[T, ID] {

  /* Returns the number of entities available. */
  def count(): Long

  /* Retrieves an entity by its id. */
  def findOne(id: ID): Option[T]

  /* Returns all instances of the type. */
  def findAll(): Traversable[T]

  /* Returns all instances of the type with the given IDs. */
  def findAll(ids: Traversable[ID]): Traversable[T]

  /* Saves a given entity. */
  def save(entity: T): Unit

  /* Deletes a given entity. */
  def delete(entity: T): Unit

  /* Deletes the entity with the given id. */
  def delete(id: ID): Unit

  /* Deletes all entities managed by the crudRepository. */
  def deleteAll(): Unit

}