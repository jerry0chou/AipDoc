package controller

import org.scalatra._
import slick.jdbc.SQLiteProfile.api._
import model.Tables._
import service.ProjectService

class ProjectServlet(val db: Database) extends ScalatraServlet with FutureSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    get("/getProjects")
    {
         ProjectService.getProjects(db)
    }
}

