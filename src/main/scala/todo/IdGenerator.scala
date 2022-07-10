package todo

import todo.data.Id


class IdGenerator(private var id: Id):

  def nextId(): Id =
    val currentId = id
    id = currentId.next
    currentId
