package service

import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ApiInfo, ApiVar}
import model.Tables._
import slick.jdbc.GetResult
import utils.result._

object ApiService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def addApi(api: ApiVar) =
    {
        if (api.apiId == -1) {
            val insertApi = Api += ApiRow(-1, api.modId, api.apiName, api.apiType, None, None, None)
            val maxID = Api.map(_.apiId).max
            val getApi = Api.filter(_.apiId === maxID).result
            db.run((insertApi >> getApi).transactionally).map(res => success(res.headOption, "add successfully"))
        }
    }

    def getModApiNums(projId: Int) =
    {
        val query =
            sql"""
                SELECT count(mod_id) as nums from module where proj_id=${projId} UNION
                SELECT count(api_id) from api where mod_id in (SELECT mod_id from module where proj_id=${projId})
            """.as[Int]
        db.run(query).map(res => success(res, "query succesfully"))
    }

    def updateParams(apiId: Int, params: String) =
    {
        val update = Api.filter(_.apiId === apiId).map(_.params).update(Some(params))
        db.run(update).map(res => success(res, "update succesfully"))
    }

    def getApiInfo(apiId: Int) =
    {
        implicit val getApiInfoResult = GetResult(r => ApiInfo(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
        val query =
            sql"""
                SELECT module.mod_name, api.api_name,api.api_type,api.params,api.success,api.failure
                from module,api where api.mod_id = module.mod_id and api.api_id =${apiId}
               """.as[ApiInfo]
        db.run(query).map(res => success(res.headOption, "query successfully"))
    }
}
