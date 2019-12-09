package utils

import java.text.SimpleDateFormat
import java.util.Date

import slick.dbio.DBIO
import scala.concurrent.duration._
import scala.concurrent.Await
import slick.jdbc.SQLiteProfile.api._

object handle
{
    def nowToString: String =
    {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    }

    def exec[T](action: DBIO[T],db:Database): T = Await.result(db.run(action), 2.seconds)
}
