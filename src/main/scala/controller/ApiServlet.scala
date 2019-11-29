package controller

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import service.ApiService
import slick.jdbc.SQLiteProfile.api._
import utils.Store.ApiVar

class ApiServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    ApiService.db = db
    before() {
        contentType = formats("json")
    }
    post("/addOrEditApi") {
        val api= parsedBody.extract[ApiVar]
        ApiService.addOrEditApi(api)
    }
}
