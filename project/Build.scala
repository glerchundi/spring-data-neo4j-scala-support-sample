import sbt._
import Keys._

object ApplicationBuildSettings {

  //
  // VERSIONS
  //

  val versions = new {

    val scala             = "2.10.1"

    val spring = new {
      val core            = "3.1.2.RELEASE"
      val test            = core
      val data = new {
        val neo4j         = "2.1.0.RELEASE"
      }
    }
    
    val neo4j             = "1.9.RC2"
    
    val junit             = "4.11"
    val scalatest         = "1.9.1"
    val scalamock         = "3.0.1"
  }

  //
  // SETTINGS
  //

  val defaultSettings =
    Seq[Setting[_]](
      organization        :=  "org.itspunchy",
      version             :=  "1.0-SNAPSHOT",
      scalaVersion        :=  versions.scala,
      scalacOptions       ++= Seq( "-deprecation", "-unchecked", "-feature" ),
      conflictWarning     :=  ConflictWarning.default("global").copy(level = Level.Debug)
    )
  
  val resolverSettings =
    Seq[Setting[_]](
      resolvers           ++= {
        Seq(
          "Typesafe Repository"         at "http://repo.typesafe.com/typesafe/releases/",
          "Neo4j Cypher DSL Repository" at "http://m2.neo4j.org/content/repositories/releases"
        )
      }
    )

  val unitTestingLibrarySettings =
    Seq[Setting[_]](
      libraryDependencies ++= 
        Seq(
          "junit"                        %  "junit"                       % versions.junit       % "test",
          "org.scalatest"                %% "scalatest"                   % versions.scalatest   % "test",
          "org.scalamock"                %% "scalamock-scalatest-support" % versions.scalamock   % "test"
        )
    )

  val springLibrarySettings =
    Seq[Setting[_]](
      libraryDependencies ++=
        Seq(
          "org.springframework"          %  "spring-core"                 % versions.spring.core,

          "cglib"                        %  "cglib"                       % "2.2.2"              % "test",
          "org.springframework"          %  "spring-test"                 % versions.spring.test % "test"
        )
    )

  val springDataWithNeo4jLibrarySettings =
    springLibrarySettings ++
    Seq[Setting[_]](
      libraryDependencies ++=
        Seq(
          "org.springframework.data"     %  "spring-data-neo4j"           % versions.spring.data.neo4j
            excludeAll(
              ExclusionRule(organization = "org.neo4j", name = "neo4j")
            ),
          "org.neo4j"                    %  "neo4j"                       % versions.neo4j,

          "org.neo4j"                    %  "neo4j-kernel"                % versions.neo4j       % "test" classifier "tests"
        )
    )

  val commonSettings = defaultSettings ++ resolverSettings

}

object ApplicationBuild extends Build {

  import ApplicationBuildSettings._

  // 
  // SPRING DATA NEO4J - SCALA SUPPORT -
  //

  val springDataNeo4jScalaSupport =
    Project(
      "sdn-scala-support",
      file("spring-data-neo4j-scala-support"),
      settings =
        Defaults.defaultSettings ++
        commonSettings ++
        unitTestingLibrarySettings ++
        Seq(
          libraryDependencies <++= (scalaVersion)(sv =>
            Seq(
              "org.scala-lang"               %  "scala-reflect"               % sv,
              "org.springframework.data"     %  "spring-data-neo4j"           % versions.spring.data.neo4j
            )
          )
        )
    )

  val sample =
    Project(
      "sdn-scala-support-sample",
      file("sample"),
      settings =
        Defaults.defaultSettings ++
        commonSettings ++
        unitTestingLibrarySettings ++
        springDataWithNeo4jLibrarySettings ++
        Seq(
          organization  := "org.itspunchy",
          name          := "sample"
        )
    ).dependsOn(
      springDataNeo4jScalaSupport
    ).aggregate(
      springDataNeo4jScalaSupport
    )

}
