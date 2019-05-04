package MainRunners.Crawlers

import java.net.URL
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

case class FailToCrawl(msg: String)
case class WebSite(webSite: URL)
case class Finished()
case class WebsiteProcessFailure(link: String, crawlerType: String, error: String)

object Crawler {
    def getCrawler(scType: String, system: ActorSystem, writer: ActorRef, supervisor: ActorRef): Crawler[_ <: CrawlerType] ={
      CrawlerType(scType) match {
      case GlassDoor => GlassDoorCrawler(system: ActorSystem, writer, supervisor)
      }
    }
}
case class CrawlFinished(site: String)
case class Content(url: URL, content: String)

trait Crawler[T <: CrawlerType] extends LazyLogging with Actor{
  def parse(url: URL): Option[Seq[String]]
}
