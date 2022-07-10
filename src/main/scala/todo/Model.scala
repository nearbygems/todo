package todo

import todo.data.*

trait Model:

  def create(task: Task): Id

  def read(id: Id): Option[Task]

  def update(id: Id)(f: Task => Task): Option[Task]

  def delete(id: Id): Boolean

  def tasks: Tasks

  def tasks(tag: Tag): Tasks

  def complete(id: Id): Option[Task]

  def tags: Tags

  def clear(): Unit
