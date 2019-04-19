package MainRunner

import MainRunner.Crawlers.{Crawler, WebSite}
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

case class Scrap(link: String)
case class Supervisor(system: ActorSystem) extends Actor with LazyLogging {
  override def receive: Receive = {
    case Crawl(websites) => {
      websites.foreach {
        case (website, websiteType) => {
          logger.info(s"Dispatching logger for $websiteType")
          val crawler = system.actorOf(Props(Crawler.getCrawler(websiteType, system)))
          crawler ! WebSite(website)
        }
        case _ => logger.info("Link is empty, check supplied links")
      }
    }

//      sender ! crawlers
//      for (elem <- crawlers) {
//        system.actorOf(Props())
//      }
    }
  }


