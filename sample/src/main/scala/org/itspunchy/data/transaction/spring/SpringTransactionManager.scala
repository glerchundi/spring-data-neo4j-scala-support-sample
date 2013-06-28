package org.itspunchy.data.transaction.spring

import org.itspunchy.data.transaction.{Transaction, TransactionManager, Isolation, Propagation}

import org.springframework.transaction.{TransactionStatus, PlatformTransactionManager}
import org.springframework.transaction.annotation.{ Isolation => SpringIsolation }
import org.springframework.transaction.annotation.{ Propagation => SpringPropagation }

class SpringTransaction(val transactionStatus: TransactionStatus) extends Transaction

class SpringTransactionManager(txManager: PlatformTransactionManager) extends TransactionManager {

  def execute[T](propagation: Propagation.Value,
                 isolation: Isolation.Value,
                 readOnly: Boolean,
                 timeout: Int,
                 rollbackFor: List[Class[_ <: Throwable]] = List(),
                 noRollbackFor: List[Class[_ <: Throwable]] = List())(f: Transaction => T): T = {
    val txAttribute =
      new TransactionAttributeWithRollbackRules(
        propagation, isolation, readOnly, timeout, rollbackFor, noRollbackFor
      )
    new TransactionTemplate(txManager, txAttribute).execute(s => f(new SpringTransaction(s)))
  }

  import scala.language.implicitConversions

  implicit def toSpringPropagation(propagation: Propagation.Value): SpringPropagation = {
    propagation match {
      case Propagation.REQUIRED       => SpringPropagation.REQUIRED
      case Propagation.SUPPORTS       => SpringPropagation.SUPPORTS
      case Propagation.MANDATORY      => SpringPropagation.MANDATORY
      case Propagation.REQUIRES_NEW   => SpringPropagation.REQUIRES_NEW
      case Propagation.NOT_SUPPORTED  => SpringPropagation.NOT_SUPPORTED
      case Propagation.NEVER          => SpringPropagation.NEVER
      case Propagation.NESTED         => SpringPropagation.NESTED
      case _                          => throw new IllegalArgumentException()
    }
  }

  implicit def toSpringIsolation(isolation: Isolation.Value): SpringIsolation = {
    isolation match {
      case Isolation.DEFAULT          => SpringIsolation.DEFAULT
      case Isolation.READ_COMMITTED   => SpringIsolation.READ_COMMITTED
      case Isolation.READ_UNCOMMITTED => SpringIsolation.READ_UNCOMMITTED
      case Isolation.REPEATABLE_READ  => SpringIsolation.REPEATABLE_READ
      case Isolation.SERIALIZABLE     => SpringIsolation.SERIALIZABLE
      case _                          => throw new IllegalArgumentException()
    }
  }

}
