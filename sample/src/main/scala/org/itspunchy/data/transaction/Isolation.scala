package org.itspunchy.data.transaction

object Isolation extends Enumeration {
  /* Use the default isolation level of the underlying datastore. */
  val DEFAULT = Value("DEFAULT")
  /* A constant indicating that dirty reads are prevented; non-repeatable reads and phantom reads can occur. */
  val READ_COMMITTED = Value("READ_COMMITTED")
  /* A constant indicating that dirty reads, non-repeatable reads and phantom reads can occur. */
  val READ_UNCOMMITTED = Value("READ_UNCOMMITTED")
  /* A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur. */
  val REPEATABLE_READ = Value("REPEATABLE_READ")
  /* A constant indicating that dirty reads, non-repeatable reads and phantom reads are prevented. */
  val SERIALIZABLE = Value("SERIALIZABLE")
}
