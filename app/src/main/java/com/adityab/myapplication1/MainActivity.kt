package com.adityab.myapplication1

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        submit_newUser.setOnClickListener() {
            val email = emailAddress_new.text.toString()
            val password = password_new.text.toString()
            createNewUser(email, password)
        }
        login_btn.setOnClickListener() {
            val email = emailAddress_new.text.toString()
            val password = password_new.text.toString()
            login(email, password)
        }
        signuot_newUser.setOnClickListener() {
            val user = auth.currentUser
            if (user != null) {
                auth.signOut()
                Toast.makeText(baseContext, "Successfully LOGGED OUT.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(baseContext, "UNABLE to LOG OUT.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Success, Loading...", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    val err = task.exception.toString()
                    Toast.makeText(baseContext, "ERROR : $err", Toast.LENGTH_SHORT).show()
                    Snackbar.make(main_layout, "0ops! Something Went Wrong!", Snackbar.LENGTH_LONG)
                }
            }
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Successfully Created an Account...",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(user)
                } else {
                    val err = task.exception.toString()
                    Toast.makeText(baseContext, "ERROR : $err", Toast.LENGTH_SHORT).show()
                    Snackbar.make(main_layout, "0ops! Something Went Wrong!", Snackbar.LENGTH_LONG)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val snack = Snackbar.make(main_layout, "Success...", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
            snack.setActionTextColor(Color.YELLOW)
            snack.show()
            val user1 = auth.currentUser
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("****Account Info****")
            builder.setIcon(R.drawable.ic_baseline_account_circle_24)
            val verficationStatus = user1?.isEmailVerified
            val emailAddress = user1?.email
            val displayName = user1?.displayName
            builder.setMessage("Display Name : $displayName, Email Address : $emailAddress, Verified Email : $verficationStatus")
            builder.setPositiveButton("OK"){ _, _ -> }
            builder.show()
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val currentUseris = auth.currentUser
            val emailAddress = currentUseris?.email
            Toast.makeText(
                baseContext,
                "$emailAddress You are already Logged in.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(baseContext, "You NOT Logged in!!", Toast.LENGTH_SHORT).show()
        }
    }


}