package todo.data

final case class Tasks(tasks: Iterable[(Id, Task)]):

  def toList: List[(Id, Task)] = tasks.toList

  def toMap: Map[Id, Task] = tasks.toMap

object Tasks:
  val empty: Tasks = Tasks(List.empty)
