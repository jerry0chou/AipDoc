package controller

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{FutureSupport, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport
import slick.jdbc.SQLiteProfile.api._
import utils.Store.ID

class RequestServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    before() {
        contentType = formats("json")
    }

    post("/"){
        val id=parsedBody.extract[ID]
        println(id)
        val r=requests.get("https://api.github.com/users/lihaoyi")
        println(r.statusCode,r.headers("content-type"),r.text)
    }

}
