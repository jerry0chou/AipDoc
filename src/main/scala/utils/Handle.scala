package utils

import java.io.{File, FileInputStream, FileOutputStream, InputStream, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date
import com.itextpdf.html2pdf.{ConverterProperties, HtmlConverter}
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import service.ProjectService.queryApiInfo
import slick.dbio.DBIO
import scala.concurrent.duration._
import scala.concurrent.Await
import slick.jdbc.SQLiteProfile.api._
import utils.Store.{ApiInfo, RequestJson}
import utils.Config.staticPath
import scala.collection.mutable.ArrayBuffer

object Handle
{
    def nowToString: String =
    {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    }

    def exec[T](action: DBIO[T], db: Database): T = Await.result(db.run(action), 2.seconds)

    def genHtml(projId: Int) =
    {
        val (projName, apiInfoList) = queryApiInfo(projId)
        val html = views.html.project(projName, apiInfoList: List[ApiInfo])
        val filename = staticPath(s"/download/${projName}.html")
        val writer = new PrintWriter(new File(filename), "utf-8")
        writer.write(html.toString())
        writer.close()
        projName
    }

    def genPdf(font: String, html: String, dest: String) =
    {
        val props = new ConverterProperties
        val fontprogram = FontProgramFactory.createFont(font)
        val defaultFontProvider = new DefaultFontProvider()
        defaultFontProvider.addFont(fontprogram)
        props.setFontProvider(defaultFontProvider)
        val writer = new PdfWriter(dest)
        val pdf = new PdfDocument(writer)
        pdf.setDefaultPageSize(PageSize.A4)
        pdf.setTagged()
        HtmlConverter.convertToPdf(new FileInputStream(html), new FileOutputStream(dest), props)
    }

    def genSingleFlask(projName: String, apiInfoList: List[ApiInfo]) =
    {
        def build(apiInfoList: List[ApiInfo]) =
        {
            val ab = new ArrayBuffer[String]
            apiInfoList.foreach(apiInfo => {
                val pattern = """\w+""".r
                val funcName = pattern.findAllIn(apiInfo.apiName).mkString("_")
                val code =
                    s"""
                       |
                       |@app.route('${apiInfo.apiName}', methods=['${apiInfo.apiType}'])
                       |def ${funcName}():
                       |    print(request.json)
                       |    result=${apiInfo.success.getOrElse("""''""")}
                       |    return jsonify(result)
                       |
                       |""".stripMargin
                ab += code
            })
            ab.mkString("")
        }

        val frame =
            s"""
               |from flask import Flask, request, jsonify
               |app = Flask(__name__)
               |
               | ${build(apiInfoList)}
               |
               |if __name__ == '__main__':
               |    app.run(debug=True)
               |
               |""".stripMargin
        val writer = new PrintWriter(new File(staticPath(s"/download/${projName}.py")), "utf-8")
        writer.write(frame)
        writer.close()
    }

    def parseStringIntoJson(json: String) =
    {
        implicit val formats = DefaultFormats
        val str = json.replaceAll(""","editable":((true)|(false))""", "")
        println("replaceAll gentable", str)
        if (str.length > 0) {
            val requestJsonList = parse(str).extract[List[RequestJson]]
            requestJsonList
        }
        else
            null
    }

    def deleteDir(dir: File): Unit =
    {
        val files = dir.listFiles()
        files.foreach(f => {
            if (f.isDirectory) {
                deleteDir(f)
            } else {
                f.delete()
                println("delete file " + f.getAbsolutePath)
            }
        })
    }
}
