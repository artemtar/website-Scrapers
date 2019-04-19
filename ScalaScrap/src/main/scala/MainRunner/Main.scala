package MainRunner

import java.net.URL

import MainRunner.Crawlers.Crawler
import MainRunner.Scrappers.Scrapper
import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.util.Timeout
import akka.pattern.ask
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


case class Crawl(websites: List[(URL, String)])

object Main extends App with LazyLogging{
//  val gds = Crawler.getCrawler("GlassDoor")
//  val l = gds.parse(new URL("https://www.glassdoor.ca/Job/canada-data-engineer-intern-jobs-SRCH_IL.0,6_IN3_KO7,27.htm"))
// val links: Seq[String] = l.getOrElse(Seq())
//  val scrapper = Scrapper.getScrapper("GlassDoor")
//  scrapper.parse(new URL(links(1)))
  ///Implement arg parse
  val webSitesToScrap = List(
  (new URL("https://www.glassdoor.ca/Job/data-engineer-jobs-SRCH_KO0,13.htm"), "GlassDoor")
)

  logger.info(s"Starting program with params $webSitesToScrap")
  val actorSystem = ActorSystem()
  implicit val timeout = Timeout(60 seconds)
  val supervisor = actorSystem.actorOf(Props(Supervisor(actorSystem)), "supervisor")
  val future = supervisor ? Crawl(webSitesToScrap)
  Await.result(future, timeout.duration)
  supervisor ! PoisonPill
  actorSystem.terminate
}