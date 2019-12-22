package service

import java.io.{File, FileInputStream, FileOutputStream}
import model.Tables.{Project, ProjectRow, Setting, SettingRow}
import slick.jdbc.GetResult
import utils.Store.{ApiInfo, Conf, ProjectVar}
import utils.result._
import utils.handle._
import slick.jdbc.MySQLProfile.api._
import utils.htmlTemplate

object ProjectService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getProjectList = db.run(Project.result).map { res => success(res, "query successfully") }

    def addOrEditProject(pro: ProjectVar) =
    {
        // add
        if (pro.projId == -1) {
            var sign = "success"
            println(pro)
            val insert = Project.filter(p => p.projName === pro.projName).exists.result.flatMap(exists => {
                if (!exists) {
                    Project += ProjectRow(-1, pro.projName, Some(pro.projDesc), nowToString, nowToString)
                }
                else {
                    sign = "failure"
                    DBIO.successful(None)
                }
            }).transactionally
            exec(insert, db)
            if (sign == "failure")
                failure("", "project name already exists")
            else if (sign == "success") {
                val getProject = Project.filter(_.projId === Project.map(_.projId).max).result
                db.run(getProject).map { res => success(res.headOption, "add successfully") }
            }
        }
        // update
        else {
            val updateProject = Project.filter(_.projId === pro.projId).map(p => (p.projName, p.projDesc, p.editedTime)).update((pro.projName, Some(pro.projDesc), nowToString))
            val getProject = Project.filter(_.projId === pro.projId).result
            db.run((updateProject >> getProject).transactionally).map(res => success(res.headOption, "update successfully"))
        }
    }

    def deleteProject(projId: Int) =
    {
        val deleteApi =
            sqlu"""
                delete FROM api where api.mod_id in
                (SELECT mod_id from module where module.proj_id in
                 (SELECT proj_id from project where proj_id=${projId}))
                """
        val deleteModule =
            sqlu"""
                delete from module where module.proj_id in
                (SELECT proj_id from project where proj_id=${projId})
                """
        val deleteProject = Project.filter(_.projId === projId).delete
        db.run((deleteApi >> deleteModule >> deleteProject).transactionally).map(_ => success("OK", "delete Successfully"))
    }

    def getProjectConf(projId: Int) =
    {
        val getConf = Setting.filter(_.projId === projId).map(_.conf).result
        db.run(getConf).map(res => success(res.headOption.getOrElse(""), "getProjectConf successfully"))
    }

    def saveProjectConf(conf: Conf) =
    {
        val upinsert = Setting.insertOrUpdate(SettingRow(conf.projId, Some(conf.conf)))
        db.run(upinsert).map(res => success(res, "saveProjectConf successfully"))
    }

    def queryApiInfo(projId: Int) =
    {
        implicit val getApiInfoResult = GetResult(r => ApiInfo(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
        val query =
            sql"""
                SELECT module.mod_name,api_name,api.api_type, api.params,api.success,api.failure
                from module,api
                where  api.mod_id = module.mod_id and module.proj_id=${projId}
               """.as[ApiInfo]
        val apiInfoList = exec(query, db)
        val queryProjName = Project.filter(_.projId === projId).map(_.projName).result
        val projName = exec(queryProjName.head, db)
        (projName, apiInfoList.toList)
    }

    def download(projId: Int, typename: String) =
    {
        def base(str: String) = "src/main/resources" + str

        typename match {
            case "pdf" =>
                val (projName, apiInfoList) = queryApiInfo(projId)
                htmlTemplate.render(projName, apiInfoList)
                genPdf(base("/msyh.ttf"), base(s"/download/${projName}.html"), base(s"/download/${projName}.pdf"))
                new File(base(s"/download/${projName}.pdf"))
            case "flask" =>
                val (projName, apiInfoList) = queryApiInfo(projId)
                gensSingleFlask(projName, apiInfoList)
                new File(base(s"/download/${projName}.py"))
            case _ =>
                new File("src/main/resources/index.pdf")
        }
    }
}
