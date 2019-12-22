package model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Api.schema ++ Module.schema ++ Project.schema ++ Setting.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Api
   *  @param apiId Database column api_id SqlType(INT), AutoInc, PrimaryKey
   *  @param modId Database column mod_id SqlType(INT)
   *  @param apiName Database column api_name SqlType(VARCHAR), Length(255,true)
   *  @param apiType Database column api_type SqlType(VARCHAR), Length(255,true)
   *  @param params Database column params SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param success Database column success SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param failure Database column failure SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param createdTime Database column created_time SqlType(VARCHAR), Length(255,true)
   *  @param editedTime Database column edited_time SqlType(VARCHAR), Length(255,true) */
  case class ApiRow(apiId: Int, modId: Int, apiName: String, apiType: String, params: Option[String] = None, success: Option[String] = None, failure: Option[String] = None, createdTime: String, editedTime: String)
  /** GetResult implicit for fetching ApiRow objects using plain SQL queries */
  implicit def GetResultApiRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ApiRow] = GR{
    prs => import prs._
    ApiRow.tupled((<<[Int], <<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table api. Objects of this class serve as prototypes for rows in queries. */
  class Api(_tableTag: Tag) extends profile.api.Table[ApiRow](_tableTag, Some("apidoc"), "api") {
    def * = (apiId, modId, apiName, apiType, params, success, failure, createdTime, editedTime) <> (ApiRow.tupled, ApiRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(apiId), Rep.Some(modId), Rep.Some(apiName), Rep.Some(apiType), params, success, failure, Rep.Some(createdTime), Rep.Some(editedTime)).shaped.<>({r=>import r._; _1.map(_=> ApiRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column api_id SqlType(INT), AutoInc, PrimaryKey */
    val apiId: Rep[Int] = column[Int]("api_id", O.AutoInc, O.PrimaryKey)
    /** Database column mod_id SqlType(INT) */
    val modId: Rep[Int] = column[Int]("mod_id")
    /** Database column api_name SqlType(VARCHAR), Length(255,true) */
    val apiName: Rep[String] = column[String]("api_name", O.Length(255,varying=true))
    /** Database column api_type SqlType(VARCHAR), Length(255,true) */
    val apiType: Rep[String] = column[String]("api_type", O.Length(255,varying=true))
    /** Database column params SqlType(VARCHAR), Length(255,true), Default(None) */
    val params: Rep[Option[String]] = column[Option[String]]("params", O.Length(255,varying=true), O.Default(None))
    /** Database column success SqlType(VARCHAR), Length(255,true), Default(None) */
    val success: Rep[Option[String]] = column[Option[String]]("success", O.Length(255,varying=true), O.Default(None))
    /** Database column failure SqlType(VARCHAR), Length(255,true), Default(None) */
    val failure: Rep[Option[String]] = column[Option[String]]("failure", O.Length(255,varying=true), O.Default(None))
    /** Database column created_time SqlType(VARCHAR), Length(255,true) */
    val createdTime: Rep[String] = column[String]("created_time", O.Length(255,varying=true))
    /** Database column edited_time SqlType(VARCHAR), Length(255,true) */
    val editedTime: Rep[String] = column[String]("edited_time", O.Length(255,varying=true))

    /** Foreign key referencing Module (database name api_ibfk_1) */
    lazy val moduleFk = foreignKey("api_ibfk_1", modId, Module)(r => r.modId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Api */
  lazy val Api = new TableQuery(tag => new Api(tag))

  /** Entity class storing rows of table Module
   *  @param modId Database column mod_id SqlType(INT), AutoInc, PrimaryKey
   *  @param projId Database column proj_id SqlType(INT)
   *  @param modName Database column mod_name SqlType(VARCHAR), Length(255,true)
   *  @param modDesc Database column mod_desc SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param createdTime Database column created_time SqlType(VARCHAR), Length(255,true)
   *  @param editedTime Database column edited_time SqlType(VARCHAR), Length(255,true) */
  case class ModuleRow(modId: Int, projId: Int, modName: String, modDesc: Option[String] = None, createdTime: String, editedTime: String)
  /** GetResult implicit for fetching ModuleRow objects using plain SQL queries */
  implicit def GetResultModuleRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ModuleRow] = GR{
    prs => import prs._
    ModuleRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table module. Objects of this class serve as prototypes for rows in queries. */
  class Module(_tableTag: Tag) extends profile.api.Table[ModuleRow](_tableTag, Some("apidoc"), "module") {
    def * = (modId, projId, modName, modDesc, createdTime, editedTime) <> (ModuleRow.tupled, ModuleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(modId), Rep.Some(projId), Rep.Some(modName), modDesc, Rep.Some(createdTime), Rep.Some(editedTime)).shaped.<>({r=>import r._; _1.map(_=> ModuleRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column mod_id SqlType(INT), AutoInc, PrimaryKey */
    val modId: Rep[Int] = column[Int]("mod_id", O.AutoInc, O.PrimaryKey)
    /** Database column proj_id SqlType(INT) */
    val projId: Rep[Int] = column[Int]("proj_id")
    /** Database column mod_name SqlType(VARCHAR), Length(255,true) */
    val modName: Rep[String] = column[String]("mod_name", O.Length(255,varying=true))
    /** Database column mod_desc SqlType(VARCHAR), Length(255,true), Default(None) */
    val modDesc: Rep[Option[String]] = column[Option[String]]("mod_desc", O.Length(255,varying=true), O.Default(None))
    /** Database column created_time SqlType(VARCHAR), Length(255,true) */
    val createdTime: Rep[String] = column[String]("created_time", O.Length(255,varying=true))
    /** Database column edited_time SqlType(VARCHAR), Length(255,true) */
    val editedTime: Rep[String] = column[String]("edited_time", O.Length(255,varying=true))

    /** Foreign key referencing Project (database name module_ibfk_1) */
    lazy val projectFk = foreignKey("module_ibfk_1", projId, Project)(r => r.projId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Module */
  lazy val Module = new TableQuery(tag => new Module(tag))

  /** Entity class storing rows of table Project
   *  @param projId Database column proj_id SqlType(INT), AutoInc, PrimaryKey
   *  @param projName Database column proj_name SqlType(VARCHAR), Length(255,true)
   *  @param projDesc Database column proj_desc SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param createdTime Database column created_time SqlType(VARCHAR), Length(255,true)
   *  @param editedTime Database column edited_time SqlType(VARCHAR), Length(255,true) */
  case class ProjectRow(projId: Int, projName: String, projDesc: Option[String] = None, createdTime: String, editedTime: String)
  /** GetResult implicit for fetching ProjectRow objects using plain SQL queries */
  implicit def GetResultProjectRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[ProjectRow] = GR{
    prs => import prs._
    ProjectRow.tupled((<<[Int], <<[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table project. Objects of this class serve as prototypes for rows in queries. */
  class Project(_tableTag: Tag) extends profile.api.Table[ProjectRow](_tableTag, Some("apidoc"), "project") {
    def * = (projId, projName, projDesc, createdTime, editedTime) <> (ProjectRow.tupled, ProjectRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(projId), Rep.Some(projName), projDesc, Rep.Some(createdTime), Rep.Some(editedTime)).shaped.<>({r=>import r._; _1.map(_=> ProjectRow.tupled((_1.get, _2.get, _3, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column proj_id SqlType(INT), AutoInc, PrimaryKey */
    val projId: Rep[Int] = column[Int]("proj_id", O.AutoInc, O.PrimaryKey)
    /** Database column proj_name SqlType(VARCHAR), Length(255,true) */
    val projName: Rep[String] = column[String]("proj_name", O.Length(255,varying=true))
    /** Database column proj_desc SqlType(VARCHAR), Length(255,true), Default(None) */
    val projDesc: Rep[Option[String]] = column[Option[String]]("proj_desc", O.Length(255,varying=true), O.Default(None))
    /** Database column created_time SqlType(VARCHAR), Length(255,true) */
    val createdTime: Rep[String] = column[String]("created_time", O.Length(255,varying=true))
    /** Database column edited_time SqlType(VARCHAR), Length(255,true) */
    val editedTime: Rep[String] = column[String]("edited_time", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Project */
  lazy val Project = new TableQuery(tag => new Project(tag))

  /** Entity class storing rows of table Setting
   *  @param projId Database column proj_id SqlType(INT), PrimaryKey
   *  @param conf Database column conf SqlType(VARCHAR), Length(255,true), Default(None) */
  case class SettingRow(projId: Int, conf: Option[String] = None)
  /** GetResult implicit for fetching SettingRow objects using plain SQL queries */
  implicit def GetResultSettingRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[SettingRow] = GR{
    prs => import prs._
    SettingRow.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table setting. Objects of this class serve as prototypes for rows in queries. */
  class Setting(_tableTag: Tag) extends profile.api.Table[SettingRow](_tableTag, Some("apidoc"), "setting") {
    def * = (projId, conf) <> (SettingRow.tupled, SettingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(projId), conf).shaped.<>({r=>import r._; _1.map(_=> SettingRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column proj_id SqlType(INT), PrimaryKey */
    val projId: Rep[Int] = column[Int]("proj_id", O.PrimaryKey)
    /** Database column conf SqlType(VARCHAR), Length(255,true), Default(None) */
    val conf: Rep[Option[String]] = column[Option[String]]("conf", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Setting */
  lazy val Setting = new TableQuery(tag => new Setting(tag))
}
