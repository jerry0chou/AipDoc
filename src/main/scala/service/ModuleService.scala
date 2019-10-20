package service

import model.Tables._
import slick.jdbc.SQLiteProfile.api._

object ModuleService
{
    var db:Database=_

    def getModules =db.run(Module.result)

}
