package MainRunners

import MainRunners.Crawlers.{CrawlFinished, Crawler, WebSite, WebsiteProcessFailure}
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

case class Scrap(link: String)

case class Supervisor(system: ActorSystem) extends Actor with LazyLogging {
  var webSitesToCrawl = 0
  var finishedCrawling = 0
  val writer = system.actorOf(Props(Writer(system, "/home/atara/test/")))

  override def receive: Receive = {
    case Crawl(websites) => {
      websites.foreach {
        case (website, websiteType) => {
          logger.info(s"Dispatching logger for $websiteType")
          val crawler = system.actorOf(Props(Crawler.getCrawler(websiteType, system, writer, self)))
          crawler ! WebSite(website)
          webSitesToCrawl += 1
        }
        case _ => logger.info("Link is empty, check supplied links")
      }
    }
    case CrawlFinished(website) => {
      logger.info(s"finished for $website")
      if(finishedCrawling == webSitesToCrawl){
        system.terminate()
      }
    }
    case WebsiteProcessFailure(link: String, crawlerType: String, error: String) => {
      logger.error(s"Failed to process website $link in $crawlerType, stack trace: $error")
    }

    case _ => logger.info("something does not work properly")
    }
  }


