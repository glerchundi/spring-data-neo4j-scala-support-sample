package org.itspunchy.data.domain

trait IdentifiableEntity[ID] extends Entity {

  def id: Option[ID]

}
