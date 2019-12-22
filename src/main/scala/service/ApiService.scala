package service

import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ApiInfo, ApiVar, JsonString, ProjectApi, RunApiJson, ShortApi}
import model.Tables._
import requests.Requester
import slick.jdbc.GetResult
import utils.result._
import utils.handle._
import org.json4s.jackson.JsonMethods._

object ApiService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def addApi(api: ApiVar) =
    {
        if (api.apiId == -1) {
            var sign = "success"
            val insert = Api.filter(a => a.apiName === api.apiName && a.modId === api.modId).exists.result.flatMap(exists => {
                if (!exists)
                    Api += ApiRow(-1, api.modId, api.apiName, api.apiType, None, None, None, nowToString, nowToString)
                else {
                    sign = "failure"
                    DBIO.successful(None)
                }
            }).transactionally
            exec(insert, db)
            if (sign == "failure")
                failure("", "api name aliready exists")
            else {
                val getApi = Api.filter(_.apiId === Api.map(_.apiId).max).result
                db.run(getApi).map(res => success(res.headOption, "add successfully"))
            }
        }
    }

    def getModApiNums(projId: Int) =
    {
        val query =
            sql"""
                SELECT count(mod_id) as nums from module where proj_id=${projId} UNION All
                SELECT count(api_id) from api where mod_id in (SELECT mod_id from module where proj_id=${projId})
            """.as[Int]
        db.run(query).map(res => success(res, "query succesfully"))
    }

    def saveColumn(json: JsonString) =
    {
        val update = Api.filter(_.apiId === json.apiId).map(e => json.typename match {
            case "params" => e.params
            case "success" => e.success
            case _ => e.failure
        }).update(Some(json.content))
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

    def deleteApi(apiId: Int) =
    {
        val delete = Api.filter(_.apiId === apiId).delete
        db.run(delete).map(res => success(res, "delete successfully"))
    }

    def genPythonCode(apiId: Int) =
    {
        def build(apiName: String, apiType: String, success: Option[String]) =
        {
            val pattern = """\w+""".r
            val funcName = pattern.findAllIn(apiName).mkString("_")
            val code =
                s"""
                   |from flask import Flask, request, jsonify
                   |app = Flask(__name__)
                   |
                   |@app.route('${apiName}', methods=['${apiType}'])
                   |def ${funcName}():
                   |    print(request.json)
                   |    result=${success.getOrElse("""''""")}
                   |    return jsonify(result)
                   |
                   |if __name__ == '__main__':
                   |    app.run(debug=True)
                   |
                   |""".stripMargin
            code
        }

        val query = Api.filter(_.apiId === apiId).result
        db.run(query).map(res => {
            val ele = res.head
            val str = build(ele.apiName, ele.apiType, ele.success)
            success(str, "already generate python code")
        })
    }

    def editApi(api: ShortApi) =
    {
        val update = Api.filter(_.apiId === api.apiId).map(a => (a.apiName, a.apiType)).update((api.apiName, api.apiType))
        db.run(update).map(res => success(res, "update successfully"))
    }

    def runApi(pa: ProjectApi) =
    {
        val getApi = Api.filter(_.apiId === pa.apiId).result
        val api = exec(getApi, db).head
        val params = api.params.getOrElse("").replaceAll(""","desc":".*?",""", "").replaceAll(""""editable":((true)|(false))""", "")
        val suc = api.success.getOrElse("")
        val getConf = Setting.filter(_.projId === pa.projId).map(_.conf).result
        val host_port = exec(getConf, db).head
        val address = host_port.getOrElse("") + api.apiName
        println(address, params)
        val response = api.apiType match {
            case "POST" => requests.post(address, data = params, headers = Map("Content-Type" -> "application/json")).text()
            case "GET" => requests.get(address).text
            case _ => "unknown request"
        }
        success(RunApiJson(suc, response), "get successfully")
    }
}
