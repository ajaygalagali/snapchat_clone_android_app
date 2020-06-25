package com.astro.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChooseUserActivity : AppCompatActivity() {

    var listViewUser : ListView? = null
    var emailList : ArrayList<String>? = ArrayList()
    var userKeysList : ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)
        Toast.makeText(this,intent.getStringExtra("imageUrl"),Toast.LENGTH_SHORT).show()

        listViewUser = findViewById(R.id.listViewUsers)
        var Listadapter = emailList?.let {
            ArrayAdapter(this,android.R.layout.simple_list_item_1,
                it
            )
        }
        listViewUser?.adapter = Listadapter
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var email = snapshot.child("email").value as String
                emailList?.add(email)
                userKeysList.add(snapshot.key.toString())
                Listadapter?.notifyDataSetChanged()

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })

        listViewUser?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var snapMap : Map<String, String> = mapOf("from" to FirebaseAuth.getInstance().currentUser?.email.toString(),
                "imageUrl" to intent.getStringExtra("imageUrl").toString(), "imageName" to intent.getStringExtra("imageName"),
                "msg" to intent.getStringExtra("msg"))
            FirebaseDatabase.getInstance().getReference().child("users").child(userKeysList.get(position))
                .child("snaps").push().setValue(snapMap)

            var intent = Intent(this,SnapActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}