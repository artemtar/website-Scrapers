package actors

object ScraperType {
  def apply(scrapperType: String): ScraperType = {
    scrapperType match{
      case "GlassDoor" => GlassDoor
    }
  }
}

sealed trait ScraperType

trait GlassDoor extends ScraperType

case object GlassDoor extends ScraperType
