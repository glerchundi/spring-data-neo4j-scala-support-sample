package org.itspunchy.data.transaction

trait Transaction {
}

trait TransactionManager {

  def execute[T](propagation: Propagation.Value,
                 isolation: Isolation.Value,
                 readOnly: Boolean,
                 timeout: Int,
                 rollbackFor: List[Class[_ <: Throwable]] = List(),
                 noRollbackFor: List[Class[_ <: Throwable]] = List())(f: Transaction => T): T

}
