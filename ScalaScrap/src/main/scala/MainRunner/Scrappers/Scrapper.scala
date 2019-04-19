package MainRunner.Scrappers

import java.net.URL

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging



object Scrapper {
  def getScrapper(scType: String, system: ActorSystem): Scrapper[_ <: ScrapperType] ={
    ScrapperType(scType) match {
        case GlassDoor => GlassDoorScrapper(system)
    }
  }
}
case class T(t:String)
trait Scrapper[T <: ScrapperType] extends LazyLogging{
  def parse(url: URL) : String
}
