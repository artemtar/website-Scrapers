package MainRunner.Scrappers

import java.net.URL

import MainRunner.Containers._
import MainRunner.Scrap
import akka.actor.ActorSystem
import org.jsoup.Jsoup
import play.api.libs.json._

object GlassDoorScrapper{
  case class FinishedScrap(result: String)
  case class WrongRequest(msg: String)
}

case class GlassDoorScrapper(system: ActorSystem) extends Scrapper[GlassDoor] {

  import GlassDoorScrapper._

  override def receive: Receive = {
    case Scrap(link) => {
      val result = parse(new URL(link))
      logger.info(result + "in scrapper")
      sender() ! FinishedScrap(result)
    }
    case _ => WrongRequest(s"can't proses ${this.toString}")
  }

  override def parse(url: URL): String = {
    val link: String = url.toString
    logger.info(s"In a scrapper: connecting to $link")

    val soup = Jsoup.connect(link)
              .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
              .referrer("http://www.google.com")
              .get()

    val info = soup.selectFirst("script").toString
    val description = cleanDiscription(soup.getElementsByClass("jobDescriptionContent").toString)
    val entry: Entry = jsonToContainer(info, description, url)
    import play.api.libs.json._
    val jsonStringOutput = Json.toJson(entry).toString()
    reflect.io.File("/home/atarasov/test/out").appendAll(jsonStringOutput)
println("------------------")


    "why you not work"
  }

  def jsonToContainer(info: String, description: String, link: URL) = {

    val pipeB = info.replace(" ", "")
      .replace("\n", " ")
      .replace("\t", "")
    val pipeC = pipeB.substring(pipeB.indexOf("[")).replaceAll("'", s""""""")
    val pipeD = pipeC.substring(0, pipeC.indexOfSlice(", \"test\":")).stripMargin('[') + "}"
    val jobInfoJson : JsValue = Json.parse(pipeD)

    val job: Job = Job( ( jobInfoJson \ "job" \ "jobTitle" ).toString,
                        ( jobInfoJson \ "job" \ "country" ).toString,
                        ( jobInfoJson \ "job" \ "city" ).toString,
                        ( jobInfoJson \ "job" \ "id" ).toString
    )
    val employer = Employer(( jobInfoJson \ "employer" \ "name" ).toString,
                            ( jobInfoJson \ "employer" \ "industry" ).toString,
                            ( jobInfoJson \ "employer" \ "id" ).toString,
                            ( jobInfoJson \ "employer" \ "location" ).toString
                           )
    Entry( employer,
           job,
           link.toString,
           description )
  }

  def cleanDiscription(discrString: String) = {

    val pDiv = "<(\\/?)div>".r
    val pLi = "<(\\/?)li>".r
    val pUl = "<(\\/?)ul>".r
    val pStrong = "<(\\/?)strong>".r
    val pBr = "<( ?)br>".r

    discrString.toString.replace("<div class=\"jobDescriptionContent desc module pad noMargBot\"", "")
                        .split(" ").toSeq.map(pDiv.replaceAllIn(_, ""))
                        .map(pLi.replaceAllIn(_, ""))
                        .map(pUl.replaceAllIn(_, ""))
                        .map(pStrong.replaceAllIn(_, ""))
                        .map(pBr.replaceAllIn(_, ""))
                        .filter(_ != " ")
                        .filter(_ != "<br>").mkString(" ")
  }
}
