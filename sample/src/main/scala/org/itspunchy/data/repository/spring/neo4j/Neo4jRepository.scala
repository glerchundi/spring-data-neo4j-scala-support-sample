package org.itspunchy.data.repository.spring.neo4j

import org.itspunchy.data.domain.IdentifiableEntity
import org.itspunchy.data.repository.Repository
import org.itspunchy.data.transaction.{TransactionManager, Transactional}
import org.springframework.data.neo4j.support.Neo4jTemplate
import org.springframework.data.neo4j.conversion.EndResult
import org.springframework.data.neo4j.repository.NodeGraphRepositoryImpl

trait Neo4jRepository[T <: IdentifiableEntity[ID], ID] extends Repository[T, ID] {
  this: Transactional =>

  protected def template: Neo4jTemplate
  implicit def txManager: TransactionManager

  protected lazy val repository = new NodeGraphRepositoryImpl(clazz, template)

  /* */
  implicit class EndResultIterable(er: EndResult[T]) extends Iterable[T] {

    class EndResultIterator(iterator: java.util.Iterator[T]) extends Iterator[T] {

      def hasNext: Boolean = iterator.hasNext
      def next(): T = {
        try {
          iterator.next()
        }
        finally {
          if (!iterator.hasNext) {
            er.finish()
          }
        }
      }

    }

    def iterator: Iterator[T] = new EndResultIterator(er.iterator())

  }

  /* */
  implicit class Neo4jTemplateExtensions(template: Neo4jTemplate) {

    def query(query: String, params: Map[String, Any]) = {
      val paramsMap = new java.util.HashMap[String, AnyRef]()
      params.foreach(p => paramsMap.put(p._1, p._2.asInstanceOf[AnyRef]))
      template.query(query, paramsMap)
    }

    def save(entity: T): Boolean = {
      template.save()
      Option(template.save(entity)).isDefined
    }

  }

}
