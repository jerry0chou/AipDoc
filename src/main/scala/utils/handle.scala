package utils

import java.text.SimpleDateFormat
import java.util.Date

object handle
{
    def nowToString: String =
    {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    }
}
