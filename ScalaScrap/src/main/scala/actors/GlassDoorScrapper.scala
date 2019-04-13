package actors

import java.net.URL
import org.jsoup.Jsoup

class GlassDoorScrapper extends Scrapper[GlassDoor]{
  override def parse(url:URL): Content = {
    val link: String = url.toString
    logger.info(s"Connecting to $link")
    
    val soup = Jsoup.connect(link)
                                .userAgent("Mozilla")
                                .get()

    println(soup.title())
    Content(url, "Hello")
  }
}
