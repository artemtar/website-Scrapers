package actors

import java.net.URL

object Main extends App {
  val gds = Scrapper.getScrapper("GlassDoor")
  gds.parse(new URL("https://www.glassdoor.com/Salaries/seattle-software-engineer-salary-SRCH_IL.0,7_IM781_KO8,25.htm"))
}
