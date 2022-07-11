package todo

import cats.implicits.*
import todo.data.*

import scala.collection.mutable

object InMemoryModel extends Model:

  val defaultTasks = List(
    Id(0) -> Task(
      State.completedNow,
      "Complete Effective Scala Week 2",
      None,
      List(Tag("programming"), Tag("scala"))
    ),
    Id(1) -> Task(
      State.Active,
      "Complete Effective Scala Week 3",
      Some("Finish the todo list exercise"),
      List(Tag("programming"), Tag("scala"), Tag("encapsulation"), Tag("sbt"))
    ),
    Id(2) -> Task(
      State.Active,
      "Make a sandwich",
      Some("Cheese and salad or ham and tomato?"),
      List(Tag("food"), Tag("lunch"))
    )
  )

  private val idGenerator = IdGenerator(Id(3))

  private val idStore: mutable.LinkedHashMap[Id, Task] = mutable.LinkedHashMap.from(defaultTasks)

  def create(task: Task): Id =
    val id = idGenerator.nextId()
    idStore += id -> task
    id

  def read(id: Id): Option[Task] = idStore.get(id)

  def complete(id: Id): Option[Task] = idStore.updateWith(id)(opt => opt.map(task => task.complete))

  def update(id: Id)(f: Task => Task): Option[Task] = idStore.updateWith(id)(opt => opt.map(f))

  def delete(id: Id): Boolean = idStore.remove(id).nonEmpty

  def tasks: Tasks = Tasks(idStore)

  def tags: Tags = Tags(idStore.flatMap((id, task) => task.tags).toList.distinct)

  def tasks(tag: Tag): Tasks = Tasks(idStore.filter((id, task) => task.tags.contains(tag)))

  def clear(): Unit = idStore.clear()
