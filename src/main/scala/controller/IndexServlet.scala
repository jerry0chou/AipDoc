package controller

import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.MySQLProfile.api._
import org.scalatra._
import service.ProjectService.queryApiInfo
import utils.Store.ApiInfo

class IndexServlet extends ScalatraServlet with FutureSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    get("/") {
        views.html.index()
    }
}
