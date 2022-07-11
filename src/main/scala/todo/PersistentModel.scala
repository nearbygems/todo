package todo

import cats.implicits.*
import io.circe.parser.*
import io.circe.syntax.*
import io.circe.{Decoder, Encoder}
import todo.data.*

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable

object PersistentModel extends Model:

  import Codecs.given

  val tasksPath = Paths.get("tasks.json")
  val idPath    = Paths.get("id.json")

  def loadTasks(): Tasks =
    if Files.exists(tasksPath) then {
      load[Tasks](tasksPath)
    } else {
      Tasks.empty
    }

  def loadId(): Id =
    if Files.exists(idPath) then {
      load[Id](idPath)
    } else {
      Id(0)
    }

  def saveTasks(tasks: Tasks): Unit =
    save[Tasks](tasksPath, tasks)

  def saveId(id: Id): Unit =
    save[Id](idPath, id)

  def save[A](path: Path, data: A)(using encoder: Encoder[A]): Unit =
    if Files.exists(path) then Files.delete(path) else Files.createFile(path)
    val json = data.asJson
    Files.writeString(path, json.spaces2, StandardCharsets.UTF_8)
    ()

  def load[A](path: Path)(using decoder: Decoder[A]): A = {
    val str = Files.readString(path, StandardCharsets.UTF_8)

    decode[A](str) match {
      case Right(result) => result
      case Left(error)   => throw error
    }

  }

  def create(task: Task): Id =
    val id = loadId().next
    saveTasks(Tasks(tasks.toMap + (id -> task)))
    saveId(id)
    id

  def read(id: Id): Option[Task] = tasks.toMap.get(id)

  def update(id: Id)(f: Task => Task): Option[Task] =
    val opt = tasks.toMap
      .get(id)
      .map(f)
    if (opt.nonEmpty) then {
      saveTasks(Tasks(tasks.toMap + (id -> opt.get)))
    }
    opt

  def delete(id: Id): Boolean =
    saveTasks(Tasks(tasks.toMap - id))
    !tasks.toMap.contains(id)

  def tasks: Tasks = loadTasks()

  def tasks(tag: Tag): Tasks = Tasks(
    tasks.toMap
      .filter((id, task) => task.tags.contains(tag))
  )

  def complete(id: Id): Option[Task] = update(id)(task => task.copy(state = State.completedNow))

  def tags: Tags = Tags(
    tasks.toMap
      .flatMap((id, task) => task.tags)
      .toList
      .distinct
  )

  def clear(): Unit =
    def deleteFile(path: Path): Unit = if Files.exists(path) then Files.delete(path)

    deleteFile(idPath)
    deleteFile(tasksPath)
