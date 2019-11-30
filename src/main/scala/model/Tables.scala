package model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.SQLiteProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Api.schema ++ Module.schema ++ Project.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Api
   *  @param apiId Database column api_id SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param modId Database column mod_id SqlType(INTEGER)
   *  @param apiName Database column api_name SqlType(TEXT)
   *  @param apiType Database column api_type SqlType(TEXT)
   *  @param params Database column params SqlType(TEXT)
   *  @param success Database column success SqlType(TEXT)
   *  @param failure Database column failure SqlType(TEXT) */
  case class ApiRow(apiId: Int, modId: Int, apiName: String, apiType: String, params: Option[String], success: Option[String], failure: Option[String])
  /** GetResult implicit for fetching ApiRow objects using plain SQL queries */
  implicit def GetResultApiRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ApiRow] = GR{
    prs => import prs._
    ApiRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table api. Objects of this class serve as prototypes for rows in queries. */
  class Api(_tableTag: Tag) extends profile.api.Table[ApiRow](_tableTag, "api") {
    def * = (apiId, modId, apiName, apiType, params, success, failure) <> (ApiRow.tupled, ApiRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(apiId), Rep.Some(modId), Rep.Some(apiName), Rep.Some(apiType), params, success, failure).shaped.<>({r=>import r._; _1.map(_=> ApiRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column api_id SqlType(INTEGER), AutoInc, PrimaryKey */
    val apiId: Rep[Int] = column[Int]("api_id", O.AutoInc, O.PrimaryKey)
    /** Database column mod_id SqlType(INTEGER) */
    val modId: Rep[Int] = column[Int]("mod_id")
    /** Database column api_name SqlType(TEXT) */
    val apiName: Rep[String] = column[String]("api_name")
    /** Database column api_type SqlType(TEXT) */
    val apiType: Rep[String] = column[String]("api_type")
    /** Database column params SqlType(TEXT) */
    val params: Rep[Option[String]] = column[Option[String]]("params")
    /** Database column success SqlType(TEXT) */
    val success: Rep[Option[String]] = column[Option[String]]("success")
    /** Database column failure SqlType(TEXT) */
    val failure: Rep[Option[String]] = column[Option[String]]("failure")

    /** Foreign key referencing Module (database name module_FK_1) */
    lazy val moduleFk = foreignKey("module_FK_1", modId, Module)(r => r.modId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Api */
  lazy val Api = new TableQuery(tag => new Api(tag))

  /** Entity class storing rows of table Module
   *  @param modId Database column mod_id SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param projId Database column proj_id SqlType(INTEGER)
   *  @param modName Database column mod_name SqlType(TEXT)
   *  @param modDesc Database column mod_desc SqlType(TEXT) */
  case class ModuleRow(modId: Int, projId: Int, modName: String, modDesc: Option[String])
  /** GetResult implicit for fetching ModuleRow objects using plain SQL queries */
  implicit def GetResultModuleRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ModuleRow] = GR{
    prs => import prs._
    ModuleRow.tupled((<<[Int], <<[Int], <<[String], <<?[String]))
  }
  /** Table description of table module. Objects of this class serve as prototypes for rows in queries. */
  class Module(_tableTag: Tag) extends profile.api.Table[ModuleRow](_tableTag, "module") {
    def * = (modId, projId, modName, modDesc) <> (ModuleRow.tupled, ModuleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(modId), Rep.Some(projId), Rep.Some(modName), modDesc).shaped.<>({r=>import r._; _1.map(_=> ModuleRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column mod_id SqlType(INTEGER), AutoInc, PrimaryKey */
    val modId: Rep[Int] = column[Int]("mod_id", O.AutoInc, O.PrimaryKey)
    /** Database column proj_id SqlType(INTEGER) */
    val projId: Rep[Int] = column[Int]("proj_id")
    /** Database column mod_name SqlType(TEXT) */
    val modName: Rep[String] = column[String]("mod_name")
    /** Database column mod_desc SqlType(TEXT) */
    val modDesc: Rep[Option[String]] = column[Option[String]]("mod_desc")

    /** Foreign key referencing Project (database name project_FK_1) */
    lazy val projectFk = foreignKey("project_FK_1", projId, Project)(r => r.projId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Module */
  lazy val Module = new TableQuery(tag => new Module(tag))

  /** Entity class storing rows of table Project
   *  @param projId Database column proj_id SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param projName Database column proj_name SqlType(TEXT)
   *  @param projDesc Database column proj_desc SqlType(TEXT) */
  case class ProjectRow(projId: Int, projName: String, projDesc: Option[String])
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[String], <<?[String]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, "project") {
    def * = (projId, projName, projDesc) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(projId), Rep.Some(projName), projDesc).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column proj_id SqlType(INTEGER), AutoInc, PrimaryKey */
    val projId: Rep[Int] = column[Int]("proj_id", O.AutoInc, O.PrimaryKey)
    /** Database column proj_name SqlType(TEXT) */
    val projName: Rep[String] = column[String]("proj_name")
    /** Database column proj_desc SqlType(TEXT) */
    val projDesc: Rep[Option[String]] = column[Option[String]]("proj_desc")
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))
}
