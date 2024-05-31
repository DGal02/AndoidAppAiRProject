package com.example.projectair

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainApp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var fireBaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchData().start()
        auth = Firebase.auth
        val buttonLogout = findViewById<Button>(R.id.logout)
        val user = auth.currentUser
        if (user == null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        fireBaseRef = auth.uid?.let {
            FirebaseDatabase.getInstance("https://androidstudioair-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference(it)
        }!!

        buttonLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        val startingTodoList = mutableListOf<Todo>()
        fireBaseRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val todo = snapshot.getValue(Todo::class.java)
                        if (todo != null) {
                            todoAdapter.addTodo(todo)
                        }
                    }
                }
            }
        }


        todoAdapter = TodoAdapter(startingTodoList, fireBaseRef)
        val todoItemsRV = findViewById<RecyclerView>(R.id.rVTodoItems)
        todoItemsRV.adapter = todoAdapter
        todoItemsRV.layoutManager = LinearLayoutManager(this)

        val buttonAdd = findViewById<Button>(R.id.btnAddTodo)
        val buttonDelete = findViewById<Button>(R.id.btnDeleteDoneTodos)
        val textEditTitle = findViewById<EditText>(R.id.eTTodoTitle)

        buttonAdd.setOnClickListener {
            val todoTitle = textEditTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                fireBaseRef.push().setValue(todo)
                todoAdapter.addTodo(todo)
                textEditTitle.text.clear()
            }
        }

        buttonDelete.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
    }

    private fun fetchData(): Thread {
        return Thread {
            val textTime = findViewById<TextView>(R.id.timeAPI)
            val url = URL("https://timeapi.io/api/Time/current/zone?timeZone=Europe/Amsterdam")
            val connection = url.openConnection() as HttpURLConnection
            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateTime(request)
                inputStreamReader.close()
                inputSystem.close()
                fetchData().start()
            } else {
                textTime.text = String.format("Request Error")
            }
        }
    }

    private fun updateTime(request: Request) {
        runOnUiThread {
            kotlin.run {
                val textTime = findViewById<TextView>(R.id.timeAPI)
                textTime.text = String.format("%02d:%02d:%02d %02d.%02d.%04d", request.hour, request.minute, request.seconds, request.day, request.month , request.year)
            }
        }
    }

}