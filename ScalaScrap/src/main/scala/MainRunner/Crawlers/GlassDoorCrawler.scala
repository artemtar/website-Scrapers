package MainRunner.Crawlers

import java.net.URL

import MainRunner.Crawlers.GlassDoorCrawler.CrawlFinished
import MainRunner.Scrap
import MainRunner.Scrappers.Scrapper
import MainRunner.Scrappers.Scrapper._
import org.jsoup.Jsoup
import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps


object GlassDoorCrawler{
  case class CrawlFinished(site: String)
}

case class GlassDoorCrawler(system: ActorSystem) extends Crawler[GlassDoor]{
  import MainRunner.Scrappers.GlassDoorScrapper._

  override def parse(url:URL): Option[Seq[String]] = {
    val linksToParse = getLinks(url)
    linksToParse
  }
  def getLinks(url: URL): Option[Seq[String]] = {
    val link: String = url.toString
    logger.info(s"Connecting to $link")

    val soup = Jsoup.connect(link)
      .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
      .referrer("http://www.google.com")
      .get()

    val jobLinks = soup
      .getElementsByAttributeValueContaining("type", "application/ld+json")
      .toString
      .split(" ")
      .toSeq.distinct
      .filter(e => e.contains("https"))
      .map(_.replace("fr.", ""))
      .map(_.replace("\"", ""))

    logger.info(s"Glassdoor links to parse ${jobLinks.size}")

    val toReturn = jobLinks.size match{
      case 0 => None
      case _ => Some(jobLinks)
    }
    toReturn
  }

  override def receive: Receive = {
    case WebSite(website) => {
      logger.info(s"Starting to parse $website")
      var links = parse(website).get
links = Seq(links.head)
      if (links.isEmpty) {
        sender ! FailToCrawl("Website list is empty")
      } else {
        logger.info(s"Links to parse from ${website}")
        logger.info(s"${links}")
        proses(links, website)
      }
    }
    case FinishedScrap(result) => logger.info(result)
    case _ => logger.info("looks like it works")

  }
  def proses(links: Seq[String], webSite: URL) = {
    links match {
      case Nil =>  CrawlFinished(webSite.toString)
      case head :: tail =>

        val scrapper = Props(Crawler.getCrawler("GlassDoor", system))
        val scrapperActor = system.actorOf(scrapper)
        implicit val timeout = Timeout(60 seconds)
        val result = scrapperActor ? Scrap(head)
val aa = result.mapTo[FinishedScrap]
        aa.recover()
        proses(tail, webSite)
    }
  }
}
