package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.SystemUtils.readFile
import br.com.astrosoft.framework.util.toLocalDateTime
import br.com.astrosoft.framework.util.toTimeStamp
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.sql2o.Connection
import org.sql2o.Query
import org.sql2o.Sql2o
import org.sql2o.converters.Converter
import org.sql2o.converters.ConverterException
import org.sql2o.quirks.NoQuirks
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.reflect.KClass

typealias QueryHandle = Query.() -> Unit

open class QueryDB(
  val name: String,
  driver: String,
  url: String,
  username: String,
  password: String
) {
  protected val sql2o: Sql2o

  init {
    registerDriver(driver)
    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = username
    config.password = password
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    config.isAutoCommit = false
    config.poolName = name
    val ds = HikariDataSource(config)
    ds.maximumPoolSize = 5
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

  protected fun <T : Any> query(
    file: String,
    classes: KClass<T>,
    lambda: QueryHandle = {}
  ): List<T> {
    val statements = toStratments(file)
    if (statements.isEmpty()) return emptyList()
    val lastIndex = statements.lastIndex
    val query = statements[lastIndex]
    val updates = if (statements.size > 1) statements.subList(0, lastIndex) else emptyList()
    return transaction { con ->
      scriptSQL(con, updates, lambda)
      val ret: List<T> = querySQL(con, query, classes, lambda)
      ret
    }
  }

  private fun <T : Any> querySQL(
    con: Connection,
    sql: String?,
    classes: KClass<T>,
    lambda: QueryHandle = {}
  ): List<T> {
    val query = con.createQuery(sql)
    query.lambda()
    println(sql)
    return query.executeAndFetch(classes.java)
  }

  protected fun script(file: String, lambda: QueryHandle = {}) {
    val stratments = toStratments(file)
    transaction { con ->
      scriptSQL(con, stratments, lambda)
    }
  }

  fun toStratments(file: String): List<String> {
    return if (file.startsWith("/")) readFile(file)?.split(";")
      .orEmpty()
      .filter { it.isNotBlank() || it.isNotEmpty() }
    else listOf(file)
  }

  private fun scriptSQL(con: Connection, stratments: List<String>, lambda: QueryHandle = {}) {
    stratments.forEach { sql ->
      val query = con.createQuery(sql)
      query.lambda()
      query.executeUpdate()
      println(sql)
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

  fun Query.addOptionalParameter(name: String, value: List<Int>): Query {
    if (this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  fun Query.addOptionalParameter(name: String, value: Double): Query {
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
      is Date -> value.toLocalDateTime()
      is Timestamp -> value.toLocalDateTime()
      else -> null
    }
  }

  override fun toDatabaseParam(value: LocalDateTime?): Any? {
    value ?: return null
    return value.toTimeStamp()
  }
}