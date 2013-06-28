package org.itspunchy.data.repository.spring.neo4j

import java.util.UUID

import org.junit.runner.RunWith

import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import org.itspunchy.data.domain.spring.neo4j.Neo4jIdentifiableEntity
import org.itspunchy.data.transaction.{TransactionManager, Transactional}
import org.itspunchy.data.transaction.spring.SpringTransactionManager

import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.neo4j.annotations._
import org.springframework.data.neo4j.config.CustomNeo4jConfiguration
import org.springframework.data.neo4j.support.Neo4jTemplate

@NodeEntity
case class Dummy @PersistenceConstructor() (
    @Indexed
    id: Option[Int] = None,

    stringValue: String = "",
    stringValueOpt: Option[String] = None,
    intValue: Int = 0,
    intValueOpt: Option[Int] = None,
    longValue: Long = 0,
    longValueOpt: Option[Long] = None,

    listOfJavaInts: List[Integer] = List(),
    listOfScalaInts: List[Int] = List(),

    setOfJavaInts: Set[Integer] = Set(),
    setOfScalaInts: Set[Int] = Set())
  extends Neo4jIdentifiableEntity[Int] {

  // SDN needs no-arg constructor (waiting for @PersistenceConstructor!!!)
  private def this() = this(None)

}

class DummyRepository(val template: Neo4jTemplate, val txManager: TransactionManager)
  extends Neo4jCrudRepository[Dummy, Int]
  with Transactional {
}

@RunWith(classOf[JUnitRunner])
class Neo4jCrudRepositorySpec extends FunSpec with ShouldMatchers with MockFactory {

  val (template, txManager) = {
    val neo4jConfiguration = new CustomNeo4jConfiguration {
      def graphDatabaseService = {
        val impermanentGraphDatabase = new org.neo4j.test.ImpermanentGraphDatabase("test-data/impermanent-db")
        impermanentGraphDatabase
      }
    }
    (neo4jConfiguration.neo4jTemplate(), new SpringTransactionManager(neo4jConfiguration.neo4jTransactionManager()))
  }

  val dummyRepository = new DummyRepository(template, txManager)

  describe("Neo4jCrudRepository") {

    it ("") {
      val notPersistedDummy =
        Dummy(
          Some(1),
          "s1", Some("s2"),
          2, Some(3), 4, Some(5),
          List(6, 7), List(8, 9),
          Set(10, 11), Set(12, 13)
        )

      dummyRepository.save(notPersistedDummy)
      val persistedDummy = dummyRepository.findOne(1).get

      persistedDummy should be (notPersistedDummy)
    }

  }

}
