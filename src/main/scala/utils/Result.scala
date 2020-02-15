package utils

object Result
{

    case class ResultSet[T](code: Int, data: T, msg: String)

    def success[T](data: T, msg: String) = ResultSet[T](200, data, msg)

    def failure[T](data: T, msg: String) = ResultSet[T](500, data, msg)
}
