package MainRunner.Crawlers

import java.net.URL
import MainRunner.{Scrap}
import MainRunner.Scrappers.{FailedToScrap, FinishedScrap, Scrapper, WrongRequest}
import org.jsoup.Jsoup
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import scala.concurrent.{Future}
import scala.concurrent.duration._
import scala.language.postfixOps


case class GlassDoorCrawler(system: ActorSystem, writer: ActorRef, supervisor: ActorRef) extends Crawler[GlassDoor] with Actor {
  var websitesToscrap = 0
  var finishedScrap = 0
  var failedScraps = scala.collection.mutable.Map[String, Int]()

  override def receive: Receive = {
    case WebSite(website) => {
      logger.info(s"Starting to parse $website")
      var links = parse(website).get
      links = Seq(links.head)
      if (links.isEmpty) {
        sender ! FailToCrawl("Website list is empty")
      } else {
        logger.info(s"Links to parse from ${website}:")
        logger.info(s"${links}")
        createScrapers(links, website, Nil)
      }
    }
    case FinishedScrap(website) => {
      logger.info(s"Finished scrap of $website")
      finishedScrap += 1
      if (websitesToscrap == finishedScrap){
        supervisor ! FailToCrawl("Glassdoor")
      }
    }
    case FailedToScrap(error: Any, link: String) => {
      logger.error(s"Couldnot connect to $link, stack trace : $error")
      if (failedScraps.contains(link)){
        failedScraps(link) += 1
      } else {
        failedScraps += link -> 0
      }
      val checkIfEnoughRequests = failedScraps(link)
//      Theree is a magic number
      if (checkIfEnoughRequests <= 3) {
        logger.info(s"Sending another requesto to $link")
        val scrapper = Props(Scrapper.getScrapper("GlassDoor", writer, system))
        val scrapperActor = system.actorOf(scrapper)
        implicit val timeout = Timeout(60 seconds)
        scrapperActor ? Scrap(link)
      } else {
        finishedScrap += 1
        if (websitesToscrap == finishedScrap){
          supervisor ! FailToCrawl("Glassdoor")
        }
      }
    }
    case WrongRequest(request) => {logger.error(s"""Wrong parsing request to $request""")}
    case _ => logger.info("Something is wrong in receive for Glassdoor Crawler")
  }

  override def parse(url: URL): Option[Seq[String]] = {
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

    jobLinks.size match{
      case 0 => None
      case _ => Some(jobLinks)
    }
  }

  def createScrapers (links: Seq[String], webSite: URL, ls: List[Future[Any]]): List[Future[Any]] = {
    links match {
      case Nil =>  ls
      case head :: tail =>
        logger.info(s"Scheduling scrapper for $head")
        val scrapper = Props(Scrapper.getScrapper("GlassDoor", writer, system))
        val scrapperActor = system.actorOf(scrapper)
        implicit val timeout = Timeout(60 seconds)
        ls :+ scrapperActor ? Scrap(head)
        websitesToscrap += 1
        createScrapers(tail, webSite, ls)
    }
  }
}
