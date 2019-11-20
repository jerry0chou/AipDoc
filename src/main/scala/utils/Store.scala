package utils

object Store
{
    // 一定要放在Servlet 外面，只要防止一些隐式转换
    //Outside Servlet definition
    case class ProjectVar(projId: Int, projName: String, projDesc: String)

    case class ID(id:Int)
}
