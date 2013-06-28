package org.itspunchy.data.domain.spring.neo4j

import org.itspunchy.data.domain.IdentifiableEntity
import org.springframework.data.neo4j.annotation.GraphId

trait Neo4jIdentifiableEntity[ID] extends IdentifiableEntity[ID] {

  @GraphId
  protected var nodeId: java.lang.Long = _

}
