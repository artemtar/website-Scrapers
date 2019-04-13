package actors
import java.net.URL

import com.typesafe.scalalogging.LazyLogging

object Scrapper {
    def getScrapper(scType: String): Scrapper[_ <: ScraperType] ={
      ScraperType(scType) match {
        case GlassDoor => new GlassDoorScrapper
      }
    }
}

//case class Url(url: String)
case class Content(url: URL, content: String)

trait Scrapper[T <: ScraperType] extends LazyLogging {
  def parse(url: URL): Content
}
