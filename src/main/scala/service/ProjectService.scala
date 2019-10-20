package service

import model.Tables.Project
import slick.jdbc.SQLiteProfile.api._

object ProjectService
{
    def getProjects(db: Database) =
    {
        db.run(Project.result)
    }
}
