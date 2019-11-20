package service

import model.Tables.{Project, ProjectRow}
import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ID, ProjectVar}
import utils.result._

object ProjectService
{
    var db: Database = _

    protected implicit def executor = scala.concurrent.ExecutionContext.Implicits.global

    def getProjectList = db.run(Project.result).map { res => success(res, "查询项目成功") }

    def addOrEditProject(pro: ProjectVar) =
    {
        // add
        if (pro.projId == -1) {
            val addProject = Project += ProjectRow(-1, pro.projName, Some(pro.projDesc))
            val maxID=Project.map(_.projId).max
            val project=Project.filter(e=>e.projId == maxID).result
            db.run(addProject andThen(project)).map { res => success(res, "添加成功") }
        }
        // update
        else {
            val updateProject = Project.filter(_.projId === pro.projId).map(p => (p.projName, p.projDesc)).update((pro.projName, Some(pro.projDesc)))
            db.run(updateProject).map(_ => success("OK", "更新成功"))
        }
    }

    def deleteProject(i: ID) =
    {
        val deleteProject = Project.filter(_.projId === i.id).delete
        db.run(deleteProject).map(_ => success("OK", "删除成功"))
    }
}
