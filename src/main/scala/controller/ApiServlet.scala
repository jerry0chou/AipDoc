package controller

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import service.ApiService
import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ApiVar, ID, JsonString}

class ApiServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    ApiService.db = db
    before() {
        contentType = formats("json")
    }
    post("/addApi") {
        val api = parsedBody.extract[ApiVar]
        ApiService.addApi(api)
    }
    post("/getModApiNums") {
        val id = parsedBody.extract[ID]
        ApiService.getModApiNums(id.id)
    }
    post("/updateParams") {
        val json = parsedBody.extract[JsonString]
        ApiService.updateParams(json.apiId, json.params)
    }
    post("/getApiInfo") {
        val id = parsedBody.extract[ID]
        ApiService.getApiInfo(id.id)
    }
}
