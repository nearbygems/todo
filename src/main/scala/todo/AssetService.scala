package todo

import cats.effect.*
import org.http4s.HttpRoutes
import org.http4s.server.staticcontent.*

object AssetService:
  def service(blocker: Blocker)(using cs: ContextShift[IO]): HttpRoutes[IO] =
    fileService(FileService.Config("src/main/resources/todo/", blocker))
