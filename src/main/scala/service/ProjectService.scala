package service

import model.Tables.Project
import slick.jdbc.SQLiteProfile.api._
import util.result._
import scala.util.Success

object ProjectService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getProjects = db.run(Project.result).map { res => success(res, "查询项目成功") }
}
