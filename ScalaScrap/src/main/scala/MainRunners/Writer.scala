package MainRunner

import MainRunner.Containers.Entry
import MainRunner.Scrappers.ResultObject
import akka.actor.{Actor, ActorSystem}
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._
import java.security.MessageDigest
import java.math.BigInteger

case class Writer (system: ActorSystem, path: String) extends Actor with LazyLogging {
  override def receive: Receive = {
    case ResultObject(entry: Entry) => {
      val checksum = Writer.md5HashString(entry.description)
      
      val jsonStringOutput = Json.toJson(entry).toString
      reflect.io.File(path).appendAll(",!," + jsonStringOutput)
    }
  }
}

object Writer{
  def md5HashString(description: String): String = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(description.getBytes)
    val bigInt = new BigInteger(1,digest)
    bigInt.toString(16)
  }
}