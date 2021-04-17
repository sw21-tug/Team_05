package at.tugraz05.slimcat

import android.util.Log
import com.google.firebase.database.*
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
        val catName: String = cat.name
        database.child(userId).child(catName).setValue(cat)
    }

    fun readUserCat(userId: String, catName: String): CatDummy {
        // TODO: implement deserialization of data object would be nice to create a data class out of it
        var cat = dataSnapshot.child(userId).child(catName).value

        Log.w("RETURN", cat.toString())

        // TODO: return data class
        return CatDummy()
    }

    fun editUser(userId: String, catName: String?, weight: Double?) {
        if (catName != null) {
            database.child(userId).child("cat_name").setValue(catName)
        }

        if (weight != null) {
            database.child(userId).child("weight").setValue(weight)
        }
    }
}