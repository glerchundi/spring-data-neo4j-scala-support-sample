package org.itspunchy.data.transaction

object Propagation extends Enumeration {
  /* Support a current transaction, throw an exception if none exists. */
  val MANDATORY = Value("MANDATORY")
  /* Execute within a nested transaction if a current transaction exists, behave like PROPAGATION_REQUIRED else. */
  val NESTED = Value("NESTED")
  /* Execute non-transactionally, throw an exception if a transaction exists. */
  val NEVER = Value("NEVER")
  /* Execute non-transactionally, suspend the current transaction if one exists. */
  val NOT_SUPPORTED = Value("NOT_SUPPORTED")
  /* Support a current transaction, create a new one if none exists. */
  val REQUIRED = Value("REQUIRED")
  /* Create a new transaction, suspend the current transaction if one exists. */
  val REQUIRES_NEW = Value("REQUIRES_NEW")
  /* Support a current transaction, execute non-transactionally if none exists. */
  val SUPPORTS = Value("SUPPORTS")
}
