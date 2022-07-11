package todo.data

final case class Tags(tags: List[Tag]):
  def toList: List[Tag] = tags
