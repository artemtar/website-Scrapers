package MainRunner.Containers

import play.api.libs.json.{Json, Writes}

case class Employer( name: String,
                     industry: String,
                     id: String,
                     location: String
                   ) {}

object Employer{
  implicit val employerWriter = new Writes[Employer] {
    def writes(employer: Employer) = Json.obj(
      "name" -> employer.name,
      "id" -> employer.id,
      "industry" -> employer.industry,
      "location" -> employer.location
    )
  }
}

