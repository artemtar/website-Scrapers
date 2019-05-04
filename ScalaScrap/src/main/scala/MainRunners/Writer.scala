package MainRunners

import MainRunners.Containers.Entry
import MainRunners.Scrappers.ResultObject
import akka.actor.{Actor, ActorSystem}
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json._
import java.security.MessageDigest
import java.math.BigInteger
import scala.io.{BufferedSource, Source}


case class Writer (system: ActorSystem, path: String) extends Actor with LazyLogging {
  val checksumBuffer: BufferedSource = Source.fromFile(path + "checksum")
  var checksumList: List[String] = checksumBuffer.getLines().toList
  checksumBuffer.close()
  override def receive: Receive = {
    case ResultObject(entry: Entry) => {
      val checksum = Writer.md5HashString(entry.description)
      if (! checksumList.contains(checksum)) {
        logger.info(s"Flashing new record with checksum: $checksum")
        reflect.io.File(path + "checksum").appendAll(checksum)
        checksumList::checksum.toList
        print(checksumList)
        val jsonStringOutput = Json.toJson(entry).toString
        reflect.io.File(path + "out").appendAll(",!," + jsonStringOutput)
      }else{
        logger.info("Record exists in elastic, nothing has been writen")
      }
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
