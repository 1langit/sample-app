package com.example.suitmediatest.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.suitmediatest.R
import com.example.suitmediatest.databinding.ActivitySecondScreenBinding

class SecondScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondScreenBinding
    private val selectUserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedUserName = result.data?.getStringExtra("USERNAME")
            binding.tvUsername.text = selectedUserName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            val name = intent.getStringExtra("NAME")
            tvName.text = name

            topBar.setNavigationOnClickListener {
                finish()
            }

            btnChooseUser.setOnClickListener {
                val intent = Intent(this@SecondScreenActivity, ThirdScreenActivity::class.java)
                selectUserLauncher.launch(intent)
            }
        }
    }
}