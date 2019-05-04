package MainRunners.Scrappers

object ScrapperType {
  def apply(scType: String): ScrapperType ={
    scType match {
      case "GlassDoor" => GlassDoor
    }
  }
}

sealed trait ScrapperType

trait GlassDoor extends ScrapperType

case object GlassDoor extends ScrapperType
