package com.example.suitmediatest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitmediatest.R
import com.example.suitmediatest.data.models.User
import com.example.suitmediatest.databinding.ActivityThirdScreenBinding

class ThirdScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private val userViewModel: UserViewModel by viewModels()
    private val userAdapter = UserAdapter() { user ->
        returnSelectedUsername(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            rvUser.layoutManager = LinearLayoutManager(this@ThirdScreenActivity)
            rvUser.adapter = userAdapter

            topBar.setNavigationOnClickListener {
                finish()
            }

            swipeRefreshLayout.setOnRefreshListener {
                userViewModel.refreshUsers()
            }

            rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!swipeRefreshLayout.isRefreshing && lastVisibleItem + 1 >= totalItemCount) {
                        userViewModel.loadMoreUsers()
                    }
                }
            })

            userViewModel.users.observe(this@ThirdScreenActivity, { users ->
                swipeRefreshLayout.isRefreshing = false
                if (users.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    btnRetry.visibility = View.VISIBLE
                } else {
                    tvEmpty.visibility = View.GONE
                    btnRetry.visibility = View.GONE
                    userAdapter.setUser(users)
                }
            })

            userViewModel.isLoading.observe(this@ThirdScreenActivity, { isLoading ->
                swipeRefreshLayout.isRefreshing = isLoading
            })

            userViewModel.error.observe(this@ThirdScreenActivity, { errorMessage ->
                errorMessage?.let {
                    if (it == "Failed to load data") {
                        tvEmpty.text = it
                        tvEmpty.visibility = View.VISIBLE
                        btnRetry.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this@ThirdScreenActivity, it, Toast.LENGTH_SHORT).show()
                    }
                    userViewModel.clearError()
                }
            })

            btnRetry.setOnClickListener {
                userViewModel.refreshUsers()
                tvEmpty.visibility = View.GONE
                btnRetry.visibility = View.GONE
            }
        }
    }

    private fun returnSelectedUsername(user: User) {
        val resultIntent = Intent()
        resultIntent.putExtra("USERNAME", "${user.first_name} ${user.last_name}")
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}