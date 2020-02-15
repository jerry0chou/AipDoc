package controller

import java.io.File

import org.scalatra._
import service.ProjectService
import slick.jdbc.MySQLProfile.api._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import utils.Config
import utils.Store.{Conf, ID, ProjectVar}
import utils.Handle.deleteDir
import utils.Config.staticPath
class ProjectServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    ProjectService.db = db
    before() {
        contentType = formats("json")
    }
    get("/getProjectList") {
        val path =getServletContext.getRealPath("/")
//        val path3=getClass.getClassLoader.getResource("/").getPath()
        println("path", path)
        ProjectService.getProjectList
    }
    post("/addOrEditProject") {
        val pro = parsedBody.extract[ProjectVar]
        ProjectService.addOrEditProject(pro)
    }
    //delete
    post("/deleteProject") {
        val id = parsedBody.extract[ID]
        ProjectService.deleteProject(id.id)
    }
    post("/getProjectConf") {
        val id = parsedBody.extract[ID]
        ProjectService.getProjectConf(id.id)
    }
    post("/saveProjectConf") {
        val conf = parsedBody.extract[Conf]
        ProjectService.saveProjectConf(conf)
    }
    get("/download") {
        Config.staticHead=getServletContext().getRealPath("/")
        contentType = "application/octet-stream"
        val file = ProjectService.download(params("projId").toInt, params("typename"))
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName)
        file
    }
}

