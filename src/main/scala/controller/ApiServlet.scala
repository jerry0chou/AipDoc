package controller

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import service.ApiService
import slick.jdbc.MySQLProfile.api._
import utils.Store.{ApiVar, ID, JsonString, ProjectApi, ShortApi}

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
    post("/editApi"){

        val shortApi = parsedBody.extract[ShortApi]
        ApiService.editApi(shortApi)
    }
    post("/getModApiNums") {
        val id = parsedBody.extract[ID]
        ApiService.getModApiNums(id.id)
    }
    post("/saveColumn") {
        val json = parsedBody.extract[JsonString]
        ApiService.saveColumn(json)
    }
    post("/getApiInfo") {
        val id = parsedBody.extract[ID]
        ApiService.getApiInfo(id.id)
    }
    post("/deleteApi") {
        val id = parsedBody.extract[ID]
        ApiService.deleteApi(id.id)
    }
    post("/genPythonCode") {
       val id=parsedBody.extract[ID]
        ApiService.genPythonCode(id.id)
    }
    post("/runApi"){
        val pa=parsedBody.extract[ProjectApi]
        ApiService.runApi(pa)
    }
}
