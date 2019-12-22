package utils

import java.io.{File, FileInputStream, FileOutputStream, InputStream, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date
import com.itextpdf.html2pdf.{ConverterProperties, HtmlConverter}
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import slick.dbio.DBIO
import scala.concurrent.duration._
import scala.concurrent.Await
import slick.jdbc.SQLiteProfile.api._
import utils.Store.ApiInfo
import scala.collection.mutable.ArrayBuffer


object handle
{
    def nowToString: String =
    {
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
    }

    def exec[T](action: DBIO[T], db: Database): T = Await.result(db.run(action), 2.seconds)

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

    def gensSingleFlask(projName: String, apiInfoList: List[ApiInfo]) =
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
        val writer = new PrintWriter(new File(s"src/main/resources/download/${projName}.py"), "utf-8")
        writer.write(frame)
        writer.close()
    }
}
