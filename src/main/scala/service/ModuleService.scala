package service

import model.Tables._
import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ID, ModApiList, ModuleVar, SimpleApi}
import utils.result._
import scala.collection.mutable.ArrayBuffer

object ModuleService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getModApi(projId: Int) =
    {
        val query = sql"""
               SELECT module.mod_id ,module.mod_name,api.api_id,api.api_name
               from module LEFT JOIN api ON module.mod_id = api.mod_id
               WHERE module.proj_id=${projId}  ORDER BY module.mod_id ASC
               """.as[(Int, String, Int, String)]
        db.run(query).map(ele => {
            val map = ele.groupBy(e => (e._1, e._2)).mapValues(e => e.map(e => (e._3, e._4)))
            val res = ArrayBuffer[ModApiList]()
            for ((k, v) <- map) {
                val ab = ArrayBuffer[SimpleApi]()
                v.filterNot(_._2 == null).map { e =>
                    ab += SimpleApi(e._1, e._2)
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
            println(mod)
            val insert = Module += ModuleRow(-1, mod.projId, mod.modName, Some(mod.modDesc))
            val maxID = Module.map(_.modId).max
            val getModule = Module.filter(_.modId === maxID).result
            db.run((insert >> getModule).transactionally).map { res => success(res.headOption, "add successfully") }
        }
        else {
            val updatModule = Module.filter(_.modId === mod.modId).map(m => (m.modName, m.modDesc)).update((mod.modName, Some(mod.modDesc)))
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
        val deleteModule = Module.filter(_.modId === modId).delete
        db.run(deleteModule).map(res => success(res, "delete successfully"))
    }
}
