import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.MySQLProfile

object CodeGen extends App
{
    slick.codegen.SourceCodeGenerator.main(
        //Array(profile, jdbcDriver, url, outputFolder, pkg, user, password)
        Array(
            "slick.jdbc.MySQLProfile",
            "com.mysql.cj.jdbc.Driver", //6.0 8.0 "com.mysql.cj.jdbc.Driver",
            "jdbc:mysql://localhost:3306/apidoc?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&nullNamePatternMatchesAll=true",
            "src/main/scala",
            "model",
            "root",
            "12345"
        )
    )
}
