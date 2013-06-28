package org.itspunchy.data.repository.spring.neo4j

import org.itspunchy.data.domain.IdentifiableEntity
import org.itspunchy.data.repository.CrudRepository
import org.itspunchy.data.transaction.Transactional

//import scala.collection.JavaConverters._

trait Neo4jCrudRepository[T <: IdentifiableEntity[ID], ID]
  extends CrudRepository[T, ID]
  with Neo4jRepository[T, ID] {
  this: Transactional =>

  //      Option(
  //        template
  //          .query(
  //            """|START n=node:__types__(className={className})
  //               |WHERE n.{id-key} = {id}
  //               |RETURN n""".stripMargin,
  //            Map(
  //              "className" -> clazz.getName,
  //              "id-key"    -> IdentifiableEntity.id,
  //              "id"        -> id
  //            )
  //          )
  //          .to(clazz)
  //          .singleOrNull()
  //      )

  def count(): Long = {
    transactional { tx =>
      repository.count()
    }
  }

  def findOne(id: ID): Option[T] = {
    transactional { tx =>
      Option(repository.findByPropertyValue("id", Some(id)))
    }
  }

  def findAll(): Traversable[T] = {
    transactional { tx =>
      repository.findAll()
    }
  }

  def findAll(ids: Traversable[ID]): Traversable[T] = {
    transactional { tx =>
      ???
    }
  }

  def save(entity: T): Unit = {
    transactional { tx =>
      template.save(entity)
    }
  }

  def delete(entity: T): Unit = {
    transactional { tx =>
      template.delete(entity)
    }
  }

  def delete(id: ID): Unit = {
    transactional { tx =>
      ???
    }
  }

  def deleteAll(): Unit = {
    transactional { tx =>
      ???
    }
  }

}
