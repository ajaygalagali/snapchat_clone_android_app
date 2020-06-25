package com.astro.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ViewSnapActivity : AppCompatActivity() {

var imageViewViewSnap : ImageView? = null
var textViewViewSnap : TextView? = null
    val mAuth = FirebaseAuth.getInstance()

    inner class ImageDownloader : AsyncTask<String?, Void?, Bitmap?>() {

        override fun doInBackground(vararg params: String?): Bitmap? {
            return try {
                val url = URL(params[0])
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.connect()
                val `in`: InputStream = connection.inputStream
                BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


    }




override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_snap)

    imageViewViewSnap = findViewById(R.id.imageViewViewSnap)
    textViewViewSnap = findViewById(R.id.textViewViewSnap)

    textViewViewSnap?.text = intent.getStringExtra("msg")


        val task = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage =
                task.execute(intent.getStringExtra("imageUrl"))
                    .get()!!
            imageViewViewSnap?.setImageBitmap(myImage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }



}

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps").child(intent.getStringExtra("snapKey")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()

    }

}