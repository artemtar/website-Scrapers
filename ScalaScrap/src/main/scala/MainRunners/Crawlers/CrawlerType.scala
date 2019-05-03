package MainRunner.Crawlers

object CrawlerType {
  def apply(scrapperType: String): CrawlerType = {
    scrapperType match{
      case "GlassDoor" => GlassDoor
    }
  }
}

sealed trait CrawlerType

trait GlassDoor extends CrawlerType

case object GlassDoor extends CrawlerType
