spring-data-neo4j-scala-support-sample
======================================

A lot of boilerplate just to being able to do this:

```scala
@NodeEntity
case class User @PersistenceConstructor() (
    @Indexed
    id: Option[Int] = None,
    
    @Indexed
    username: String = "",
    
    firstName: String = "",
    lastName: Option[String] = None)
  extends Neo4jIdentifiableEntity[Int] {

  // SDN needs no-arg constructor (waiting for @PersistenceConstructor!!!)
  private def this() = this(None)

}
```

Persist:

```scala
  val user = User(Some(1), "glerchundi", "Gorka", Some("Lerchundi"))
  userRepository.save(user)      
```

Lookup:

```scala
  val userOpt: Option[User] = userRepository.findOne(1)
```

Promoting immutablility using case classes and scala collection which are also immutable.

Pull requests are encouraged! :)
