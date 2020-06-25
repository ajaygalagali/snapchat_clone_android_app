package com.astro.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var editTextUserEmail : EditText? = null
    var editTextPassword : EditText?  = null
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mAuth.currentUser != null){
            logIn()
        }

        editTextUserEmail = findViewById(R.id.editTextUserEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
    }

    fun goClicked(view: View) {
        // Check if user can log in
        mAuth.signInWithEmailAndPassword(editTextUserEmail?.text.toString(), editTextPassword?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    logIn()
                } else {
                    // If sign in fails, display a message to the user.
                    //Sign Up
                    mAuth.createUserWithEmailAndPassword(editTextUserEmail?.text.toString(), editTextPassword?.text.toString())
                        .addOnCompleteListener(this){task ->
                            if (task.isSuccessful){

                                FirebaseDatabase.getInstance().getReference().child("users").child(
                                    task.result?.user?.uid.toString())
                                    .child("email").setValue(editTextUserEmail?.text.toString());
                                Log.i("DB","After")
                                logIn()
                            }else{
                                Toast.makeText(this,"Log In failder",Toast.LENGTH_SHORT).show()


                            }
                        }
                }


            }

    }
    fun logIn(){
        //Move to Log In
        val intent = Intent(this,SnapActivity::class.java)
        startActivity(intent)
    }
}