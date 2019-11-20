package utils

object result
{

    case class ResultSet[T](code: Int, data: T, Msg: String)

    def success[T](data: T, Msg: String) = ResultSet[T](200, data, Msg)

    def failure[T](data: T, Msg: String) = ResultSet[T](500, data, Msg)
}
