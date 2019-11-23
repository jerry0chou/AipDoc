package utils

import scala.collection.mutable.ArrayBuffer

object Store
{

    // 一定要放在Servlet 外面，只要防止一些隐式转换
    //Outside Servlet definition
    case class ProjectVar(projId: Int, projName: String, projDesc: String)

    case class ModuleVar(modId: Int, projId: Int, modName: String, modDesc: String)

    case class SimpleApi(apiId: Int, apiName: String)

    case class ModApiList(modId: Int, modName: String, apiList: ArrayBuffer[SimpleApi])

    case class ID(id: Int)

}
