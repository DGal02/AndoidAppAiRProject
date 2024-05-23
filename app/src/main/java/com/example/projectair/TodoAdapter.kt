package com.example.projectair

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class TodoAdapter(
    private val todos: MutableList<Todo>,
    private val firebase: DatabaseReference
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        this.updateFirebase()
        notifyDataSetChanged()
    }

    fun updateFirebase() {

        firebase.removeValue()
        todos.forEach { element ->
            firebase.push().setValue(element)
        }
    }

    private fun toggleStrikeThrough(tVTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tVTodoTitle.paintFlags = tVTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tVTodoTitle.paintFlags = tVTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        var curTodo = todos[position]
        holder.itemView.apply {
            val todoTitleTextView = findViewById<TextView>(R.id.tVTodoTitle)
            val doneCheckBox = findViewById<CheckBox>(R.id.cBDone)
            todoTitleTextView.text = curTodo.title
            doneCheckBox.isChecked = curTodo.isChecked
            toggleStrikeThrough(todoTitleTextView, curTodo.isChecked)
            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(todoTitleTextView, isChecked)
                curTodo.isChecked = !curTodo.isChecked
                updateFirebase()
            }
        }
    }

    fun getTodos(): MutableList<Todo> {
        return todos
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}