package utils

object Config
{
    var staticHead = ""

    def staticPath(filename: String) =
    {
        var path = ""
        if (staticHead == "" || staticHead.contains("target"))
            path = "src/main/webapp/static" + filename
        else
            path = staticHead.replace("\\", "/") + "/static" + filename
        println(path)
        path
    }
}
