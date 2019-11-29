package controller

import org.scalatra._
import org.scalatra.{FutureSupport, ScalatraServlet}
import service.ModuleService
import slick.jdbc.SQLiteProfile.api._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import utils.Store.{ID, ModuleVar}

class ModuleServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    ModuleService.db = db
    before() {
        contentType = formats("json")
    }
    post("/getModApi") {
        val id = parsedBody.extract[ID]
        ModuleService.getModApi(id.id)
    }
    post("/addOrEditModule") {
        val mod = parsedBody.extract[ModuleVar]
        ModuleService.addOrEditModule(mod)
    }
    post("/getModule") {
        val id = parsedBody.extract[ID]
        ModuleService.getModule(id.id)
    }
    post("/deleteModule") {
        val id = parsedBody.extract[ID]
        ModuleService.deleteModule(id.id)
    }
}