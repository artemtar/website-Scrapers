package MainRunner.Scrappers

import java.net.URL

import akka.actor.{Actor, ActorSystem}
import com.typesafe.scalalogging.LazyLogging



object Scrapper {
  def getScrapper(scType: String, system: ActorSystem): Scrapper[_ <: ScrapperType] ={
    ScrapperType(scType) match {
        case GlassDoor => GlassDoorScrapper(system)
    }
  }
}
trait Scrapper[T <: ScrapperType] extends LazyLogging with Actor{
  def parse(url: URL) : String
}
