package com.example.Assignment2app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.Assignment2app.databinding.ActivityMainBinding
import com.example.Assignment2app.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var quizList: ArrayList<Quiz>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = mAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        quizList = Quiz().createQuizList()
        val adapter = QuizAdapter(quizList, this)
        binding.rvQuizzes.adapter = adapter
        val gridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvQuizzes.layoutManager = gridLayoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_profile -> {
                if (mAuth.currentUser != null) {
                    startActivity(Intent(this@MainActivity, UserProfile::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Please login first", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            R.id.exit -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.exit))
                builder.setMessage(getString(R.string.exit_confirm_msg))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    run {
                        moveTaskToBack(true)
                        android.os.Process.killProcess(android.os.Process.myPid())
                        exitProcess(1)
                    }
                }
                builder.setNegativeButton(getString(R.string.no)) { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            R.id.logout -> {
                mAuth.signOut()
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}