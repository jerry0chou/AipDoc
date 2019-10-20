package service

import model.Tables.Project
import slick.jdbc.SQLiteProfile.api._

object ProjectService
{
    var db:Database=_

    def getProjects = db.run(Project.result)

}
