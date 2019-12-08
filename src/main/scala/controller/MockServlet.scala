package controller

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{FutureSupport, ScalatraServlet}
import slick.jdbc.SQLiteProfile.api._
import model.Tables._
import utils.result._
import utils.Store._

class MockServlet(val db: Database) extends ScalatraServlet with FutureSupport with JacksonJsonSupport
{
    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    protected implicit lazy val jsonFormats: Formats = DefaultFormats
    before() {
        contentType = formats("json")
    }
    get("/*") {
        handleRequest(request.getPathInfo, request.body, "GET")
    }
    post("/*") {
        handleRequest(request.getPathInfo, request.body, "POST")
    }

    def handleRequest(path: String, params: String, typo: String) =
    {
        println("-----------Mock Start-------------")
        println("Request Path : " + typo + " " + path)
        println("Request params : \n" + params)
        println("-----------Mock End---------------")
        val getApi = Api.filter(_.apiName === path).map(_.success.getOrElse("")).result
        db.run(getApi).map(_.head)
    }
}
