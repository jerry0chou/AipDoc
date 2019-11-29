package controller

import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.SQLiteProfile.api._
import org.scalatra._

class IndexServlet extends ScalatraServlet with FutureSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    get("/") {
        views.html.index()
    }
}
