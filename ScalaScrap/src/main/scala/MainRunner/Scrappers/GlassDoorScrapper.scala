package MainRunner.Scrappers

import java.net.URL

import MainRunner.Containers._
import MainRunner.Scrap
import akka.actor.{ActorRef, ActorSystem}
import org.jsoup.Jsoup
import play.api.libs.json._

import scala.util.Try


case class GlassDoorScrapper(writer: ActorRef, system: ActorSystem) extends Scrapper[GlassDoor] {

  override def receive: Receive = {
    case Scrap(link: String) => {
    val responce = parse(new URL(link))
    sender() ! responce
    }
    case _ => sender() ! WrongRequest(s"can't proses ${this.toString}")
  }

  override def parse(url: URL) = {
    val link: String = url.toString
    logger.info(s"In a scrapper: connecting to $link")

    try {
      val soup = Jsoup.connect(link)
        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
        .referrer("http://www.google.com")
        .get()

    val info = soup.selectFirst("script").toString
    val description = cleanDiscription(soup.getElementsByClass("jobDescriptionContent").toString)
    val entry: Entry = jsonToContainer(info, description, url)
    writer ! ResultObject(entry)
    FinishedScrap(url.toString)
    }catch {
      case e => FailedToScrap(e.toString, link)
    }
  }

  def jsonToContainer(info: String, description: String, link: URL) = {

    val pipeB = info.replace(" ", "")
      .replace("\n", " ")
      .replace("\t", "")
    val pipeC = pipeB.substring(pipeB.indexOf("[")).replaceAll("'", s""""""")
    val pipeD = pipeC.substring(0, pipeC.indexOfSlice(", \"test\":")).stripMargin('[') + "}"
    val jobInfoJson : JsValue = Json.parse(pipeD)

    val job: Job = Job( ( jobInfoJson \ "job" \ "jobTitle" ).get.toString.replace("\"", ""),
                        ( jobInfoJson \ "job" \ "country" ).get.toString.replace("\"", ""),
                        ( jobInfoJson \ "job" \ "city" ).get.toString.replace("\"", ""),
                        ( jobInfoJson \ "job" \ "id" ).get.toString.replace("\"", "")
    )
    val employer = Employer(( jobInfoJson \ "employer" \ "name" ).get.toString.replace("\"", ""),
                            ( jobInfoJson \ "employer" \ "industry" ).get.toString.replace("\"", ""),
                            ( jobInfoJson \ "employer" \ "id" ).get.toString.replace("\"", ""),
                            ( jobInfoJson \ "employer" \ "location" ).get.toString.replace("\"", "")
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
    val pBr = "<(\\/?)br>".r
    val pP = "<(\\/?)p>".r
    val pB = "<(\\/?)b>".r

    discrString.toString.replace("<div class=\"jobDescriptionContent desc module pad noMargBot\"", "")
                        .split(" ").toSeq.map(pDiv.replaceAllIn(_, ""))
                        .map(pLi.replaceAllIn(_, ""))
                        .map(pUl.replaceAllIn(_, ""))
                        .map(pStrong.replaceAllIn(_, ""))
                        .map(pBr.replaceAllIn(_, ""))
                        .map(pP.replaceAllIn(_, ""))
                        .map(pB.replaceAllIn(_, ""))
                        .filter(_ != " ")
                        .filter(_ != "<br>").mkString(" ")
  }
}
