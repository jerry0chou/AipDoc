package utils

import java.io.{ByteArrayInputStream, File, PrintWriter}

import utils.Store.{ApiInfo, RequestJson}

import scala.collection.mutable.ArrayBuffer
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable

object htmlTemplate
{
    def render(projName: String, apiInfoList: List[ApiInfo]) =
    {
        def genSuccess(success: String) =
        {
            if (success.length > 0)
                s"""
                   | <div>
                   |    <span style="background-color: #87d068;color: white;padding: 3px;border-radius: 8px">Reponse Success</span>
                   | </div>
                   | <pre>
                   |${success}
                   |  </pre>
                   |""".stripMargin
            else
                ""
        }

        def genFailure(failure: String) =
        {
            if (failure.length > 0)
                s"""
                   |<div>
                   |            <span style="background-color: #f50;color: white;padding: 3px;border-radius: 8px">Reponse Failure</span>
                   |        </div>
                   |        <pre>
                   |${failure}
                   |       </pre>
                   |""".stripMargin
            else
                ""
        }

        def genTable(json: String) =
        {
            implicit val formats = DefaultFormats
            val str = json.replaceAll(""","editable":((true)|(false))""", "")
            println("replaceAll gentable", str)
            if (str.length > 0) {
                val requestJsonList = parse(str).extract[List[RequestJson]]
                val ab = new ArrayBuffer[String]
                for (request <- requestJsonList) {
                    val s =
                        s"""
                           |   <tr>
                           |      <td>${request.name}</td>
                           |         <td>
                           |           ${request.value}
                           |         </td>
                           |        <td>
                           |           ${request.desc}
                           |        </td>
                           |   </tr>
                           |""".stripMargin
                    ab += s
                }
                ab.mkString("")
            }
            else
                ""
        }

        def genApiInfo =
        {
            var ab = new ArrayBuffer[String]
            for ((apiInfo, index) <- apiInfoList.view.zipWithIndex) {
                var str =
                    s"""
                       |<div>
                       |    <span style="background-color: #e17c28;color: white;padding: 5px;border-radius: 8px">${index+1}.</span>
                       |    <span style="background-color: #9000ff;color: white;padding: 5px;border-radius: 8px">${apiInfo.modName}</span>
                       |    <span style="color: white;background-color: #108ee9;padding: 5px;border-radius: 8px">${apiInfo.apiType}  ${apiInfo.apiName}</span>
                       |    <div style="margin-top: 10px">
                       |        <table cellpadding=“0” cellspacing="0">
                       |            <tr>
                       |                <th>Param Name</th>
                       |                <th>Param Value</th>
                       |                <th>Param Desc</th>
                       |            </tr>
                       |            ${genTable(apiInfo.params)}
                       |        </table>
                       |        <div style="margin-top: 20px">
                       |        </div>
                       |        ${genSuccess(apiInfo.success.getOrElse(""))}
                       |        <div style="margin-top: 20px">
                       |        </div>
                       |        ${genFailure(apiInfo.failure.getOrElse(""))}
                       |    </div>
                       |</div>
                       |""".stripMargin
                ab += str
            }
            ab.mkString("")
        }

        val html =
            s"""
               |<!DOCTYPE html>
               |<html lang="en">
               |<head>
               |    <meta charset="UTF-8">
               |    <title>${projName}</title>
               |</head>
               |<body>
               |<style>
               |    table {
               |
               |        border: silver solid 1px;
               |        width: 690px;
               |    }
               |
               |    th {
               |        border: silver 1px solid;
               |        padding: 7px;
               |        width: 100px;
               |        background-color: #108ee9;
               |        color: white;
               |    }
               |
               |    tr, td {
               |        border: silver 1px solid;
               |        padding: 7px;
               |        word-break: break-all;
               |        word-wrap: break-word;
               |        white-space: normal
               |    }
               |
               |    pre {
               |        padding: 8px;
               |        font-size: 12px;
               |        background-color: #f6f8fa;
               |        width: 690px;
               |        font-weight: bold;
               |        border-radius: 12px;
               |    }
               |</style>
               | ${genApiInfo}
               |</body>
               |</html>
               |""".stripMargin

        val writer = new PrintWriter(new File(s"src/main/resources/download/${projName}.html"), "utf-8")
        writer.write(html)
        writer.close()
    }
}
