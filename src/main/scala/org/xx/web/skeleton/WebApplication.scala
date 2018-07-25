package org.xx.web.skeleton


import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class TestDTO(name: String, value: Int)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val testDTOFormat = jsonFormat2(TestDTO)
}

object WebApplication extends HttpApp with JsonSupport {

  implicit val actorSystem = ActorSystem("test")

  def main(args: Array[String]): Unit = {

    //implicit val materializer = ActorMaterializer()

    WebApplication.startServer("0.0.0.0", 8080)

  }

  override protected def waitForShutdownSignal(system: ActorSystem)(implicit ec: ExecutionContext): Future[Done] = Future.never

  override protected def postServerShutdown(attempt: Try[Done], system: ActorSystem): Unit = {
    actorSystem.terminate()
    super.postServerShutdown(attempt, system)
  }

  override protected def routes: Route = {
    import actorSystem.dispatcher
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Hello akka http</h1>"))
      }
    } ~
      path("bye") {
        get {
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Bye akka http</h1>"))
        }
      } ~
      pathPrefix("test") {
        pathEnd {
          get {

            complete(TestDTO("test", 1))
          }
        } ~
          path("param") {
            get {
              onSuccess(Future {
                TestDTO("1", 2)
              }) {
                dto: TestDTO =>
                  complete {
                    dto
                  }
              }
            }
          }
      }

  }
}
