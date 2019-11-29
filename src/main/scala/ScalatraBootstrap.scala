import controller.{ApiServlet, IndexServlet, ModuleServlet, ProjectServlet}
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra._
import javax.servlet.ServletContext
import org.slf4j.LoggerFactory
import slick.jdbc.SQLiteProfile.api._

class ScalatraBootstrap extends LifeCycle
{
    val logger = LoggerFactory.getLogger(getClass)
    val cpds = new ComboPooledDataSource
    logger.info("Created c3p0 connection pool")

    override def init(context: ServletContext)
    {
        val db = Database.forDataSource(cpds, None) // create the Database object

        // 路由注册
        context.mount(new IndexServlet, "/")
        context.mount(new ProjectServlet(db), "/project")
        context.mount(new ModuleServlet(db), "/module")
        context.mount(new ApiServlet(db), "/api")
    }

    override def destroy(context: ServletContext)
    {
        super.destroy(context)
        closeDbConnection
    }

    private def closeDbConnection()
    {
        logger.info("Closing c3po connection pool")
        cpds.close
    }
}
