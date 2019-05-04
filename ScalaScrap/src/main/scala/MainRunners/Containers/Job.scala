package MainRunners.Containers

import play.api.libs.json.{JsValue, Json, Writes}

case class Job( jobTitle: String,
                country: String,
                city: String,
                id: String
              ) {}

object Job{
  implicit val jobWriter = new Writes[Job] {
    override def writes(job: Job)  = Json.obj(
      "jobTitle" -> job.jobTitle,
      "country" -> job.country,
      "city" -> job.city,
      "id" -> job.id
    )
  }
}