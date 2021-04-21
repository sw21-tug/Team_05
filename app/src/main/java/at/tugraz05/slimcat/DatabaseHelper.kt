package at.tugraz05.slimcat

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseHelper {
    private lateinit var database: DatabaseReference
    private lateinit var dataSnapshot: DataSnapshot
    private lateinit var userId: String

    fun initializeDatabaseReference() {
        database = Firebase.database.reference
    }

    fun createUserId(userId: String?): String {
        if(userId == null){
            this.userId = database.push().key!!
        }
        else {
            this.userId = userId
        }
        return this.userId
    }

    fun addPostEventListener() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataSnapshot = snapshot
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Cancel", "Cancelled", error.toException())
            }
        }
        database.addValueEventListener(postListener)
    }

    fun writeNewCat(cat: CatDummy) {
        val catName: String = cat.name
        database.child(userId).child(catName).setValue(cat)
    }

    fun readUserCat(catName: String): CatDummy? {
        return dataSnapshot.child(userId).child(catName).getValue(CatDummy::class.java)!!
    }

    fun editUser(catName: String?, weight: Double?) {
        //possible solution: read cat data first, then update it
        if (catName != null) {
            database.child(userId).child("cat_name").setValue(catName)
        }

        if (weight != null) {
            database.child(userId).child("weight").setValue(weight)
        }
    }
}