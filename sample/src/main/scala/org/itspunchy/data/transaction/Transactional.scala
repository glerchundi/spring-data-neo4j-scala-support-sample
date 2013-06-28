package org.itspunchy.data.transaction

trait Transactional {

  def transactional[T](propagation: Propagation.Value = Propagation.REQUIRED,
                       isolation: Isolation.Value = Isolation.DEFAULT,
                       readOnly: Boolean = false,
                       timeout: Int = 60000,
                       rollbackFor: List[Class[_ <: Throwable]] = List(),
                       noRollbackFor: List[Class[_ <: Throwable]] = List())(f: Transaction => T)(implicit txManager: TransactionManager): T = {
    txManager.execute(
      propagation, isolation, readOnly, timeout, rollbackFor, noRollbackFor
    )(f)
  }

  def transactional[T](f: Transaction => T)(implicit txManager: TransactionManager): T = transactional()(f)

}

object Transactional extends Transactional {
}
