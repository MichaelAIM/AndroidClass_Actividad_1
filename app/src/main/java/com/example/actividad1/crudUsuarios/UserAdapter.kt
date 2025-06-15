package com.example.actividad1.crudUsuarios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1.R

class UserAdapter(
    private val users: MutableList<User>,
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewUserName)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewUserEmail)
        val phoneTextView: TextView = itemView.findViewById(R.id.textViewUserPhone)
        val editButton: ImageButton = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.nameTextView.text = user.name
        holder.emailTextView.text = user.email
        holder.phoneTextView.text = user.phone

        holder.editButton.setOnClickListener {
            onEditClick(user)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    // Método para actualizar los datos del adaptador
    fun updateUsers(newUsers: List<User>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }
}