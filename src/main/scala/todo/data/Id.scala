package todo.data

final case class Id(toInt: Int):
  def next: Id = this.copy(toInt = toInt + 1)

object Id:
  given idOrdering: Ordering[Id] = Ordering.by(id => id.toInt)
