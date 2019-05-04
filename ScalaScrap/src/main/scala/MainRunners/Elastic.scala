import com.sksamuel.elastic4s.index.RichIndexResponse
import com.sksamuel.elastic4s.searches.RichSearchResponse
import com.sksamuel.elastic4s.{ElasticsearchClientUri, TcpClient}
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import org.elasticsearch.common.settings.Settings
import scala.concurrent.{Future}

object Scala4s {
  val settings = Settings.builder().put("cluster.name" ,"docker-cluster").put("client.transport.sniff", false).build()
  implicit val client = TcpClient.transport(settings, ElasticsearchClientUri("localhost", 9300))

  def createElasticIndex(implicit client: TcpClient) : Future[CreateIndexResponse] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    client.execute {
      createIndex("").mappings(
        mapping("") as(
        )
      )
    }
  }
  def insertDocument(implicit client: TcpClient) : Future[RichIndexResponse] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    client.execute {
      indexInto("" / "") doc "" refresh(RefreshPolicy.IMMEDIATE)
    }
  }
  def queryDocument(artist: String)(implicit client: TcpClient) : Future[RichSearchResponse] = {
    import com.sksamuel.elastic4s.ElasticDsl._
    client.execute {
      search("" / "") query artist
    }
  }
  def close() ={
    client.close()
  }

}
