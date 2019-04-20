package MainRunner.Crawlers

import java.net.URL

import MainRunner.Crawlers.GlassDoorCrawler.CrawlFinished
import MainRunner.{Scrap, Supervisor}
import MainRunner.Scrappers.{GlassDoorScrapper, Scrapper, ScrapperType}
import MainRunner.Scrappers.Scrapper._
import akka.actor.Status.Success
import org.jsoup.Jsoup
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.pattern.pipe

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure

object GlassDoorCrawler{
  case class CrawlFinished(site: String)
  case class FailedToScrap(e: Any)
}

case class GlassDoorCrawler(system: ActorSystem, supervisor: ActorRef) extends Crawler[GlassDoor] with Actor {
  import MainRunner.Scrappers.GlassDoorScrapper._
  import GlassDoorCrawler._

  override def parse(url:URL): Option[Seq[String]] = {
    val linksToParse = getLinks(url)
    linksToParse
  }
  def getLinks(url: URL): Option[Seq[String]] = {
    val link: String = url.toString
    logger.info(s"In crawler: connecting to $link")

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
        scrapLinks(createScrapers(links, website, Nil))
      }
    }
    case FinishedScrap(result) => {logger.info(result + "is scrappper ----------")}
//      logger.info("hello")}
    case _ => logger.info("looks like it works")

  }

  def createScrapers (links: Seq[String], webSite: URL, ls: List[Future[Any]]): List[Future[Any]] = {
    links match {
      case Nil =>  ls
      case head :: tail =>
        logger.info(s"prosessiong $head")
        val scrapper = Props(Scrapper.getScrapper("GlassDoor", system))
        val scrapperActor = system.actorOf(scrapper)
        implicit val timeout = Timeout(60 seconds)
        ls :+ scrapperActor ? Scrap(head)
        logger.info(s"interseting ---------- $ls")
        createScrapers(tail, webSite, ls)
    }

  }

  def scrapLinks (result: List[Future[Any]] ) = {
    logger.info(s"insite of scraplinkjs $result")
      result.foreach(f => logger.info(s"hello here $f"))
    }
}
