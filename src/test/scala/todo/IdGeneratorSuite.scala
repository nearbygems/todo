package todo

import munit.*
import todo.data.*

class IdGeneratorSuite extends FunSuite:
  test("generated Ids are monotonically increasing") {
    val generator = IdGenerator(Id(0))
    val ids       = for (i <- 1.to(1000)) yield generator.nextId()

    assert(ids.sliding(2).forall { case Seq(a, b) => a.toInt < b.toInt })
  }

  test("generated Ids start with given Id") {
    val generator = IdGenerator(Id(0))

    assertEquals(generator.nextId(), Id(0))
  }
