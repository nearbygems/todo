package todo
package data

import java.time.ZonedDateTime


enum State:

  def completed: Boolean =
    this match {
      case _: State.Completed => true
      case _ => false
    }

  def active: Boolean =
    this match {
      case State.Active => true
      case _ => false
    }

  case Active
  case Completed(data: ZonedDateTime)

object State:

  def completedNow: State =
    Completed(ZonedDateTime.now())
