package at.tugraz05.slimcat

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseHelper {
    private lateinit var database: DatabaseReference
    private lateinit var dataSnapshot: DataSnapshot

    fun initializeDatabaseReference() {
        database = Firebase.database.reference
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

    fun writeNewCat(userId: String, cat: CatDummy) {
        val catName: String = cat.getName()
        database.child(userId).child(catName).setValue(cat)
    }

    fun readUserCat(userId: String): CatDummy {
        var cat: Any? = dataSnapshot.child(userId).getValue()

        Log.w("RETURN", cat.toString())

        return CatDummy("test", 1, 2.2)
    }

    fun editUser(userId: String, catName: String, weight: Double) {
        if (catName != null) {
            database.child(userId).child("cat_name").setValue(catName)
        }

        if (weight != null) {
            database.child(userId).child("weight").setValue(weight)
        }
    }
}