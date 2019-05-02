package MainRunner

import MainRunner.Containers.Entry
import MainRunner.Scrappers.ResultObject
import akka.actor.{Actor, ActorSystem}
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._

case class Writer (system: ActorSystem, path: String) extends Actor with LazyLogging {
  override def receive: Receive = {
    case ResultObject(entry: Entry) => {
      val jsonStringOutput = Json.toJson(entry).toString
      reflect.io.File(path).appendAll('\n' + jsonStringOutput)
    }
  }
}
