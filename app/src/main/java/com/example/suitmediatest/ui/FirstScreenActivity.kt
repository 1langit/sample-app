package com.example.suitmediatest.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.suitmediatest.R
import com.example.suitmediatest.databinding.ActivityFirstScreenBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FirstScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            btnCheck.setOnClickListener {
                if (isPalindrome(edtPalindrome.text.toString())) {
                    showPalindromeDialog("isPalindrome")
                } else {
                    showPalindromeDialog("not palindrome")
                }
            }

            btnNext.setOnClickListener {
                val newIntent = Intent(this@FirstScreenActivity, SecondScreenActivity::class.java)
                newIntent.putExtra("NAME", edtName.text.toString())
                startActivity(newIntent)
            }
        }
    }

    private fun isPalindrome(input: String): Boolean {
        val trimmedInput = input.replace(" ", "").lowercase()
        return trimmedInput == trimmedInput.reversed()
    }

    private fun showPalindromeDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
}