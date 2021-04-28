package at.tugraz05.slimcat

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File

class DatabaseHelper {
    private lateinit var database: DatabaseReference
    private lateinit var dataSnapshot: DataSnapshot
    private lateinit var userId: String

    fun initializeDatabaseReference() {
        database = Firebase.database.reference
    }

    private fun createUserId(userId: String? = null): String {
        if(userId == null){
            this.userId = database.push().key!!
        }
        else {
            this.userId = userId
        }
        return this.userId
    }

    fun checkAndCreateUserId(applicationContext: Context) {
        try {
            val slimCatDir = File(applicationContext.filesDir, "slimCat")
            if (!slimCatDir.exists())
                slimCatDir.mkdirs()

            val file = File(slimCatDir, "userId.txt")
            var userId = ""

            if(!file.exists())
                file.createNewFile()

            userId = file.readText()
            if (userId == "") {
                userId = createUserId()
                file.writeText(userId)
            }
            else {
                createUserId(userId)
            }
        }
        catch (e: Exception){
            Log.w("ERROR", e)
        }

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
        val catName: String = cat.name!!
        database.child(userId).child(catName).setValue(cat)
    }

    fun readUserCat(catName: String): CatDummy? {
        return dataSnapshot.child(userId).child(catName).getValue(CatDummy::class.java)
    }

    fun editUser(catName: String, cat: CatDummy) {
        val catOld = readUserCat(catName)!!
        val catNewMap = cat.toMap()
        val catOldMap = catOld.toMap()

        val map = catNewMap.mapValues { if(it.value == null) catOldMap[it.key] else it.value }
        database.child(userId).child(catName).updateChildren(map)
    }
}