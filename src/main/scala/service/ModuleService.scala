package service

import model.Tables._
import slick.jdbc.SQLiteProfile.api._

object ModuleService
{
    def getModules(db: Database) =
    {
        db.run(Module.result)
    }
}
