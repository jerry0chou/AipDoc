package controller

import org.scalatra.{FutureSupport, ScalatraServlet}
import service.ModuleService
import slick.jdbc.SQLiteProfile.api._

class ModuleServlet(val db: Database) extends ScalatraServlet with FutureSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    get("/getModules")
    {
        ModuleService.getModules(db)
    }
}