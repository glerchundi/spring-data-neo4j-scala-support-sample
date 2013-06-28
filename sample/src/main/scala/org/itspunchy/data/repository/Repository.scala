package org.itspunchy.data.repository

import scala.reflect._

import org.itspunchy.data.domain.IdentifiableEntity

abstract class Repository[T <: IdentifiableEntity[ID] : ClassTag, ID] {

  private val ct: ClassTag[T] = classTag[T]

  protected def clazz: Class[T] = ct.runtimeClass.asInstanceOf[Class[T]]

}
