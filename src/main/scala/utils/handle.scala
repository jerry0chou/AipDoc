package utils

import java.io.{FileInputStream, FileOutputStream, InputStream}
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
import scala.xml.XML

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
}
