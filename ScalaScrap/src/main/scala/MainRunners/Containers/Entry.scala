package MainRunners.Containers

import MainRunners.Writer
import play.api.libs.json.{Json, Writes}

case class Entry( employer: Employer,
                  job: Job,
                  link: String,
                  description: String
                ) {}

object Entry{

implicit val entryWriter = new Writes[Entry] {
  def writes(entry: Entry) = Json.obj(
    "employer" -> Json.toJson(entry.employer),
    "job" -> Json.toJson(entry.job),
    "link" -> entry.link,
    "description" -> entry.description,
    "checksum" -> Writer.md5HashString(entry.description)
    )
  }

  def toString(entry: Entry): String = {
    s"""{hello, employer: {${Employer.toString(entry.employer)}}"""
  }
}