package service

import model.Tables._
import slick.jdbc.MySQLProfile.api._
import utils.Store.{ID, ModApiList, ModuleVar, SimpleApi}
import utils.result._
import utils.handle._
import scala.collection.mutable.ArrayBuffer

object ModuleService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getModApi(projId: Int) =
    {
        val query = sql"""
               SELECT module.mod_id ,module.mod_name,api.api_id,api.api_name,api.api_type
               from module LEFT JOIN api ON module.mod_id = api.mod_id
               WHERE module.proj_id=${projId}  ORDER BY module.mod_id ASC
               """.as[(Int, String, Int, String, String)]
        db.run(query).map(ele => {
            val map = ele.groupBy(e => (e._1, e._2)).mapValues(e => e.map(e => (e._3, e._4, e._5)))
            val res = ArrayBuffer[ModApiList]()
            for ((k, v) <- map) {
                val ab = ArrayBuffer[SimpleApi]()
                v.filterNot(_._2 == null).map { e =>
                    ab += SimpleApi(e._1, e._2, e._3)
                }
                res += ModApiList(k._1, k._2, ab)
            }
            success(res, "getModApi successfully")
        })
    }

    def addOrEditModule(mod: ModuleVar) =
    {
        // add
        if (mod.modId == -1) {
            var sign = "success"
            val insert = Module.filter(m => m.projId === mod.projId && m.modName === mod.modName).exists.result.flatMap(exists => {
                if (!exists)
                    Module += ModuleRow(-1, mod.projId, mod.modName, Some(mod.modDesc), nowToString, nowToString)
                else {
                    sign = "failure"
                    DBIO.successful(None)
                }
            }).transactionally
            exec(insert, db)
            if (sign == "failure")
                failure("", "module name already exists")
            else {
                val getModule = Module.filter(_.modId === Module.map(_.modId).max).result
                db.run(getModule).map { res => success(res.headOption, "add successfully") }
            }
        }
        else {
            val updatModule = Module.filter(_.modId === mod.modId).map(m => (m.modName, m.modDesc, m.editedTime)).update((mod.modName, Some(mod.modDesc), nowToString))
            val getModule = Module.filter(_.modId === mod.modId).result
            db.run(updatModule >> getModule).map(res => success(res.headOption, "update successfully"))
        }
    }

    def getModule(modId: Int) =
    {
        val mod = Module.filter(_.modId === modId).result
        db.run(mod).map(res => success(res.headOption, "get Module successfully"))
    }

    def deleteModule(modId: Int) =
    {
        val deleteApi =
            sqlu"""
                delete FROM api where api.mod_id in
                (SELECT mod_id from module where module.mod_id =${modId})
                """
        val deleteModule = Module.filter(_.modId === modId).delete
        db.run((deleteApi >> deleteModule).transactionally).map(res => success(res, "delete successfully"))
    }
}
