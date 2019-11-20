package controller

import org.scalatra._
import service.ProjectService
import slick.jdbc.SQLiteProfile.api._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.json4s.jackson.JsonMethods._
import utils.Store.{ID, ProjectVar}

class ProjectServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    ProjectService.db = db
    before() {
        contentType = formats("json")
    }
    get("/getProjectList") {
        println("/getProjectList")
        ProjectService.getProjectList
    }
    //add
    post("/addOrEditProject") {
        println("POST /projets")
        val pro = parsedBody.extract[ProjectVar]
        println(pro)
        ProjectService.addOrEditProject(pro)
    }

    //delete
    post("/deleteProject")
    {
        val id=parsedBody.extract[ID]
        println(id)
        ProjectService.deleteProject(id)
    }
}

