package MainRunner

import MainRunner.Crawlers.GlassDoorCrawler.FailedToScrap
import MainRunner.Crawlers.{Crawler, WebSite}
import MainRunner.Scrappers.GlassDoorScrapper.FinishedScrap
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

case class Scrap(link: String)
case class Supervisor(system: ActorSystem) extends Actor with LazyLogging {
  override def receive: Receive = {
    case Crawl(websites) => {
      websites.foreach {
        case (website, websiteType) => {
          logger.info(s"Dispatching logger for $websiteType")
          val crawler = system.actorOf(Props(Crawler.getCrawler(websiteType, system, self)))
          crawler ! WebSite(website)
        }
        case _ => logger.info("Link is empty, check supplied links")
      }
    }
    case FinishedScrap(result) => {
      logger.info(result)
      logger.info("did not runnnnnnn---------------------?")
    }
    case FailedToScrap(sometimes) => logger.info(s"---------------Failed to scrap: stack trace $sometimes")
    case _ => logger.info("something does not work properly")

//      sender ! crawlers
//      for (elem <- crawlers) {
//        system.actorOf(Props())
//      }
    }
  }


