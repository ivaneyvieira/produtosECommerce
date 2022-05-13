package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.SystemUtils.readFile
import br.com.astrosoft.framework.util.toLocalDateTime
import br.com.astrosoft.framework.util.toTimeStamp
import org.sql2o.Connection
import org.sql2o.Query
import org.sql2o.Sql2o
import org.sql2o.converters.Converter
import org.sql2o.converters.ConverterException
import org.sql2o.quirks.NoQuirks
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.reflect.KClass

typealias QueryHandle = Query.() -> Unit

open class QueryDB(val name: String, driver: String, url: String, username: String, password: String) {
  protected val sql2o: Sql2o

  init {
    registerDriver(driver)

    val maps = HashMap<Class<*>, Converter<*>>()
    maps[LocalDate::class.java] = LocalDateConverter()
    maps[LocalTime::class.java] = LocalSqlTimeConverter()
    maps[LocalDateTime::class.java] = LocalSqlDateTimeConverter()
    this.sql2o = Sql2o(url, username, password, NoQuirks(maps))
  }

  private fun registerDriver(driver: String) {
    try {
      Class.forName(driver)
    } catch (e: ClassNotFoundException) { //throw RuntimeException(e)
    }
  }

  fun dataBaseTest() {
    query("select 1 from dual", Int::class)
  }

  fun <T> timeExec(log: String, block: () -> T): T {
    val ti = System.nanoTime()
    val ret = block()
    val tf = System.nanoTime()
    println(log)
    val segundo = (tf - ti) * 1.0 / 1000000000
    val sdf = DecimalFormat("#,##0.00")
    println("Tempo: ${sdf.format(segundo)}")
    return ret
  }

  protected fun <T : Any> query(file: String, classes: KClass<T>, lambda: QueryHandle = {}): List<T> = timeExec(file) {
    val statements = toStratments(file)
    if (statements.isEmpty()) return@timeExec emptyList()
    val lastIndex = statements.lastIndex
    val query = statements[lastIndex]
    val updates = if (statements.size > 1) statements.subList(0, lastIndex) else emptyList()
    transaction { con ->
      scriptSQL(con, updates, lambda)
      val ret: List<T> = querySQL(con, query, classes, lambda)
      ret
    }
  }

  protected fun <R : Any> querySerivce(file: String,
                                       complemento: String? = null,
                                       lambda: QueryHandle = {},
                                       result: (Query) -> R): R = timeExec(file) {
    val statements = toStratments(file, complemento)
    val lastIndex = statements.lastIndex
    val query = statements[lastIndex]
    val updates = if (statements.size > 1) statements.subList(0, lastIndex) else emptyList()
    transaction { con ->
      scriptSQL(con, updates, lambda)
      val q = querySQLResult(con, query, lambda)
      result(q)
    }
  }

  private fun querySQLResult(con: Connection, sql: String?, lambda: QueryHandle = {}): Query {
    val query = con.createQuery(sql)
    query.lambda()
    return query
  }

  private fun <T : Any> querySQL(con: Connection, sql: String?, classes: KClass<T>, lambda: QueryHandle = {}): List<T> {
    val query = con.createQuery(sql)
    query.lambda()
    return query.executeAndFetch(classes.java)
  }

  protected fun script(file: String, lambda: QueryHandle = {}) {
    val stratments = toStratments(file)
    transaction { con ->
      scriptSQL(con, stratments, lambda)
    }
  }

  fun toStratments(file: String, complemento: String? = null): List<String> {
    val sql = if (file.startsWith("/")) readFile(file) ?: ""
    else file
    val sqlComplemento = if (complemento == null) sql else "$sql;\n$complemento"
    return sqlComplemento.split(";").filter { it.isNotBlank() || it.isNotEmpty() }
  }

  private fun scriptSQL(con: Connection, stratments: List<String>, lambda: QueryHandle = {}) {
    stratments.forEach { sql ->
      val query = con.createQuery(sql)
      query.lambda()
      query.executeUpdate()
    }
  }

  fun Query.addOptionalParameter(name: String, value: String?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Int): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: LocalDateTime?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: List<*>): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Double?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: LocalDate?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: LocalTime?): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  private fun <T> transaction(block: (Connection) -> T): T {
    return sql2o.beginTransaction().use { con ->
      val ret = block(con)
      con.commit()
      ret
    }
  }
}

class LocalDateConverter : Converter<LocalDate?> {
  @Throws(ConverterException::class)
  override fun convert(value: Any?): LocalDate? {
    if (value !is Date) return null
    return value.toLocalDate()
  }

  override fun toDatabaseParam(value: LocalDate?): Any? {
    value ?: return null
    return Date(value.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
  }
}

class LocalSqlTimeConverter : Converter<LocalTime?> {
  @Throws(ConverterException::class)
  override fun convert(value: Any?): LocalTime? {
    if (value !is Time) return null
    return value.toLocalTime()
  }

  override fun toDatabaseParam(value: LocalTime?): Any? {
    value ?: return null
    return Time.valueOf(value)
  }
}

class LocalSqlDateTimeConverter : Converter<LocalDateTime?> {
  @Throws(ConverterException::class)
  override fun convert(value: Any?): LocalDateTime? {
    return when (value) {
      is Date      -> value.toLocalDateTime()
      is Timestamp -> value.toLocalDateTime()
      else         -> null
    }
  }

  override fun toDatabaseParam(value: LocalDateTime?): Any? {
    value ?: return null
    return value.toTimeStamp()
  }
}