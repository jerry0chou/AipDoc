import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api
import slick.jdbc.SQLiteProfile

object CodeGen extends App
{
    slick.codegen.SourceCodeGenerator.main(
        //Array(profile, jdbcDriver, url, outputFolder, pkg, user, password)
        Array(
            "slick.jdbc.SQLiteProfile",
            "org.sqlite.JDBC",
            "jdbc:sqlite:db/api.db",
            "src/main/scala",
            "model",
            )
    )
}
