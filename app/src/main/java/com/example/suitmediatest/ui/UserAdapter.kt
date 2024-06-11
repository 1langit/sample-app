package com.example.suitmediatest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suitmediatest.data.models.User
import com.example.suitmediatest.databinding.ItemUserBinding

class UserAdapter(private val onItemClick: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = ArrayList<User>()

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(root).load(user.avatar).into(imgAvatar)
                "${user.first_name} ${user.last_name}".also { tvName.text = it }
                tvEmail.text = user.email
            }

            itemView.setOnClickListener {
                onItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    fun setUser(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }
}