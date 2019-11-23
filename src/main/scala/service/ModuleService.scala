package service

import model.Tables._
import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ModApiList, ModuleVar, SimpleApi}
import utils.result._

import scala.collection.mutable.ArrayBuffer

object ModuleService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getModApi(projId: Int) =
    {
       val query=sql"""
                SELECT module.mod_id ,module.mod_name,api.api_id,api.api_name
                from module,api
                 WHERE module.mod_id = api.mod_id and module.proj_id=${projId}
                 ORDER BY module.mod_id ASC
               """.as[(Int,String,Int,String)]

        db.run(query).map(ele=>{
            val map=ele.groupBy(e => (e._1, e._2)).mapValues(e => e.map(e => (e._3, e._4)))
            val res=ArrayBuffer[ModApiList]()
            for ((k, v) <- map) {
                val ab=ArrayBuffer[SimpleApi]()
                v.map{e=>
                    ab+=SimpleApi(e._1,e._2)
                }
                res+=ModApiList(k._1,k._2,ab)
            }
            success(res,"getModApi successfully")
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
    }
}
