package com.astro.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var listViewSnaps :ListView? =null
    var snapList : ArrayList<String>? = ArrayList()
    var snaps : ArrayList<DataSnapshot> =ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap)

        listViewSnaps = findViewById(R.id.listViewSnaps)

        var snapAdapter = snapList?.let {
            ArrayAdapter(this,android.R.layout.simple_list_item_1,
                it
            )
        }

        listViewSnaps?.adapter = snapAdapter

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps")
            .addChildEventListener(object : ChildEventListener{
                override fun onCancelled(error: DatabaseError) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapList?.add(snapshot.child("from").value as String)
                    snaps.add(snapshot)
                    snapAdapter?.notifyDataSetChanged()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var index = 0
                    for(snap : DataSnapshot in snaps){

                        if(snap.key == snapshot?.key){
                            snaps.removeAt(index)
                            snapList?.removeAt(index)
                        }
                        index++

                    }
                    snapAdapter?.notifyDataSetChanged()
                }
            })

        listViewSnaps?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var snapst = snaps.get(position)
            var intent = Intent(this,ViewSnapActivity::class.java)
            intent.putExtra("imageName",snapst.child("imageName").value as String)
            intent.putExtra("imageUrl",snapst.child("imageUrl").value as String)
            intent.putExtra("msg",snapst.child("msg").value as String)
            intent.putExtra("snapKey",snapst.key)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.snap_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.createSnap){
            // Create Snap
            val intent = Intent(this,CreateSnapActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.logout){
            // Log Out
            mAuth.signOut()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }
}