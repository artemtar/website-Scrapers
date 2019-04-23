package MainRunner.Scrappers

import java.net.URL

import MainRunner.Scrap
import akka.actor.{Actor, ActorSystem}
import org.jsoup.Jsoup

object GlassDoorScrapper{
  case class FinishedScrap(result: String)
  case class WrongRequest(msg: String)
}

case class GlassDoorScrapper(system: ActorSystem) extends Scrapper[GlassDoor] {

  import GlassDoorScrapper._

  override def parse(url: URL): String = {
    val link: String = url.toString
    logger.info(s"In a scrapper: connecting to $link")

    val soup = Jsoup.connect(link)
      .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
      .referrer("http://www.google.com")
      .get()
    val a = soup.selectFirst("script")
    val pattern = raw"\\[(.*?)\\]".r
    val b = a.toString.replace(" ", "").replace("\n", " ").replace("\t", "")
    val c = b.substring(b.indexOf("["))
    val d = c.substring(0, c.indexOfSlice(";   window.getGdGlobals"))
    println(d)
    "why you not work"
  }

  override def receive: Receive = {
    case Scrap(link) => {
     val result = parse(new URL(link))
      logger.info(result + "in scrapper")
      sender() ! FinishedScrap(result)
    }
    case _ => WrongRequest(s"can't proses ${this.toString}")
  }
}
