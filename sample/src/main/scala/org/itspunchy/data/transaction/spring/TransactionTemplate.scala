package org.itspunchy.data.transaction.spring

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionOperations

/**
 * Scala-based convenience wrapper for the Spring
 * [[org.itspunchy.data.transaction.spring.TransactionTemplate]], taking
 * advantage of functions and Scala types.
 *
 * @author Arjen Poutsma
 * @since 1.0
 * @constructor Creates a `TransactionTemplate` that wraps the given Java template
 * @param javaTemplate the Java `TransactionTemplate` to wrap
 */
class TransactionTemplate(val javaTemplate: TransactionOperations) {

  def this(transactionManager: PlatformTransactionManager) {
    this (new org.springframework.transaction.support.TransactionTemplate(transactionManager))
  }

  /**
   * Construct a new TransactionTemplate using the given transaction manager,
   * taking its default settings from the given transaction definition.
   * @param transactionManager the transaction management strategy to be used
   * @param transactionDefinition the transaction definition to copy the default settings from. Local properties can still be set to change values.
   */
  def this(transactionManager: PlatformTransactionManager, transactionDefinition: TransactionDefinition) {
    this (new org.springframework.transaction.support.TransactionTemplate(transactionManager, transactionDefinition))
  }

  /**
   * Execute the action specified by the given function within a transaction.
   * <p>Allows for returning a result object created within the transaction, that is,
   * a domain object or a collection of domain objects. A RuntimeException thrown
   * by the callback is treated as a fatal exception that enforces a rollback.
   * Such an exception gets propagated to the caller of the template.
   * @param action the callback object that specifies the transactional action
   * @return a result object returned by the callback, or <code>null</code> if none
   * @throws TransactionException in case of initialization, rollback, or system errors
   */
  def execute[T](action: TransactionStatus => T): T = {
    javaTemplate.execute(new TransactionCallback[T] {
      def doInTransaction(status: TransactionStatus) = {
        action.apply(status)
      }
    })
  }

}