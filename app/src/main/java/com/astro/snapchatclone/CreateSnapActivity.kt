package com.astro.snapchatclone

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*

class CreateSnapActivity : AppCompatActivity() {

    var imageViewCreateSnap : ImageView? = null
    var editTextYourMsg : EditText? = null
    var imageName : String = UUID.randomUUID().toString() + ".jpg"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)

        imageViewCreateSnap = findViewById(R.id.imageViewCreateSnap)
        editTextYourMsg = findViewById(R.id.editTextYourMsg)
    }

    fun ChooseImageClicked(view: View) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val selImage = data!!.data
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, selImage)
                imageViewCreateSnap?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun NextClicked(view: View) {
        // Get the data from an ImageView as bytes
        imageViewCreateSnap?.isDrawingCacheEnabled = true
        imageViewCreateSnap?.buildDrawingCache()
        val bitmap = (imageViewCreateSnap?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"UPLOAD FAILURE",Toast.LENGTH_SHORT).show()
            Log.i("Failure","Failed")
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Toast.makeText(this,"UPLOAD SUCCESS",Toast.LENGTH_SHORT).show()
            var url: String = ""
            var task = it.storage.downloadUrl
                task.addOnSuccessListener {
                    url = task.getResult().toString()
                    Log.i("URL   ", url)

                    var intent = Intent(this,ChooseUserActivity::class.java)
                    intent.putExtra("imageUrl",url)
                    intent.putExtra("msg",editTextYourMsg?.text.toString())
                    intent.putExtra("imageName",imageName)
                    startActivity(intent)


                }




        }
    }
}