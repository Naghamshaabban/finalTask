package com.example.finaltask

import android.database.SQLException
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object databasehelper { private const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    private const val DB_URL = "jdbc:mysql://10.0.2.2:3306/pet"
    private const val USER = "root"
    private const val PASS = ""

    init {
        try {
            Class.forName(JDBC_DRIVER)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(DB_URL, USER, PASS)
                .also{ Log.d("tag1", "data in query")}
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            Log.e("tag2", "connection error")
            null
        }
    }

    fun executeQuery(query: String): ResultSet? {
        Log.d("tag3", "excuting query")
        return try {

            val conn = getConnection()
            val stmt: Statement? = conn?.createStatement()
            stmt?.executeQuery(query).also { Log.d("tag4", "excuting query") }
        } catch (e: java.sql.SQLException) {
            e.printStackTrace()
            Log.e("tag5", "excuting query")
            null
        }
    }
}
