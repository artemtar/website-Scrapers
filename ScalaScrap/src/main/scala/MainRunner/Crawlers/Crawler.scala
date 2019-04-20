package MainRunner.Crawlers

import java.net.URL

import akka.actor.TypedActor.Supervisor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

case class FailToCrawl(msg: String)
case class WebSite(webSite: URL)
case class Finished()

object Crawler {
    def getCrawler(scType: String, system: ActorSystem, supervisor: ActorRef): Crawler[_ <: CrawlerType] ={
      CrawlerType(scType) match {
      case GlassDoor => GlassDoorCrawler(system: ActorSystem, supervisor)
      }
    }
}

case class Content(url: URL, content: String)

trait Crawler[T <: CrawlerType] extends LazyLogging with Actor{
  def parse(url: URL): Option[Seq[String]]
}
