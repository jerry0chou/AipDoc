package service

import slick.jdbc.SQLiteProfile.api._
import utils.Store.ApiVar
import model.Tables._
import utils.result._

object ApiService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def addOrEditApi(api: ApiVar) =
    {
        if (api.apiId == -1) {
            val insertApi = Api += ApiRow(-1, api.modId, api.apiName, api.apiType, Some(api.success), Some(api.failure))
            val maxID = Api.map(_.apiId).max
            val getApi = Api.filter(_.apiId === maxID).result
            db.run((insertApi >> getApi).transactionally).map(res => success(res.headOption, "add successfully"))
        }
        else {
            val updatApi = Api.filter(_.apiId === api.apiId).map(a => (a.apiName, a.apiType, a.success, a.failure)).update((api.apiName, api.apiType, Some(api.success), Some(api.failure)))
            val getApi = Api.filter(_.apiId === api.apiId).result
            db.run(updatApi >> getApi).map(res => success(res.headOption, "update successfully"))
        }
    }

    def getModApiNums(id: Int) =
    {
        val query =
            sql"""
                SELECT count(mod_id) as nums from module where proj_id=12 UNION
                SELECT count(api_id) from api where mod_id in (SELECT mod_id from module where proj_id=12)
            """.as[Int]
        db.run(query).map(res => success(res, "query succesfully"))
    }
}
