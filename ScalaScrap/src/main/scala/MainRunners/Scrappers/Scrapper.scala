package MainRunner.Scrappers

import java.net.URL

import MainRunner.Containers.Entry
import akka.actor.{Actor, ActorRef, ActorSystem}
import com.typesafe.scalalogging.LazyLogging

case class FailedToScrap(e: Any, link: String)
case class ResultObject(entry: Entry)
case class FinishedScrap(result: String)
case class WrongRequest(msg: String)

object Scrapper {
  def getScrapper(scType: String, writer: ActorRef, system: ActorSystem): Scrapper[_ <: ScrapperType] ={
    ScrapperType(scType) match {
        case GlassDoor => GlassDoorScrapper(writer, system)
    }
  }
}

trait Scrapper[T <: ScrapperType] extends LazyLogging with Actor{
  def parse(url: URL) : Product
}
