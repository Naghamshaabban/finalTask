package com.example.finaltask

import android.os.Bundle
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mysql.jdbc.log.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private val userList = mutableListOf<petList>()
    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(userList)
        recyclerView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            fetchdatabase()
        }


    }

    private suspend fun fetchdatabase() {
        val query = "SELECT * FROM info"
        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = databasehelper.getConnection()
            if (connection != null) {
                android.util.Log.d("tag0", "data in query")
                val statement = connection.createStatement()
                resultSet = statement.executeQuery(query)

                if (resultSet != null) {
                    val newList = mutableListOf<petList>()
                    while (resultSet.next()) {
                        val id = resultSet.getInt("id")
                        val petNAme = resultSet.getString("name")
                        val petEyeColor = resultSet.getString("eyeColor")

                        val pet = petList(id, petNAme, petEyeColor)
                        newList.add(pet)
                    }
                    withContext(Dispatchers.Main) {
                        userList.clear()
                        userList.addAll(newList)
                        updateUI()
                    }
                }else{
                    android.util.Log.d("tag6", "No data in query")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "connection error" , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("tag1", "error in query ", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity,"error in query", Toast.LENGTH_SHORT).show()
            }
        }
        finally{
            try {
                resultSet?.close()
                connection?.close()
            }catch (e: SQLException){
                e.printStackTrace()
            }
        }
    }

    private fun updateUI() {
        adapter.notifyDataSetChanged()
    }



}
