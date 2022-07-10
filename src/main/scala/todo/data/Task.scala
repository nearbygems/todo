package todo
package data


final case class Task(state:       State,
                      description: String,
                      notes:       Option[String],
                      tags:        List[Tag]):

  def complete: Task =

    val newState = state match
      case State.Active => State.completedNow
      case State.Completed(d) => State.Completed(d)

    this.copy(state = newState)
