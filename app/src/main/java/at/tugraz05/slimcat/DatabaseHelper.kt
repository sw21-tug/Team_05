package at.tugraz05.slimcat

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File

object DatabaseHelper {
    private lateinit var database: DatabaseReference
    private lateinit var dataSnapshot: DataSnapshot
    private lateinit var userId: String

    fun initializeDatabaseReference() {
        database = Firebase.database.reference
        Firebase.database.setPersistenceEnabled(true)
    }

    private fun createUserId(userId: String?): String {
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
            var slimCatDir = File(applicationContext.filesDir, "slimCat")
            if (!slimCatDir.exists())
                slimCatDir.mkdirs()

            var file = File(slimCatDir, "userId.txt")
            var userId: String? = ""

            if(file.exists()){
                userId = file.readText()
                if(userId == ""){
                    userId = createUserId(null)
                    file.writeText(userId)
                }
                else {
                    userId = createUserId(userId)
                }
            }
            else {
                userId = createUserId(null)
                file.createNewFile()
                file.writeText(userId)
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

    fun writeNewCat(cat: CatDataClass) {
        val catName: String = cat.name!!
        database.child(userId).child(catName).setValue(cat)
    }

    fun readUserCat(catName: String): CatDataClass? {
        return dataSnapshot.child(userId).child(catName).getValue(CatDataClass::class.java)
    }

    fun editUser(catName: String, cat: CatDataClass) {
        var catOld = readUserCat(catName)!!
        var catNewMap = cat.toMap()
        var catOldMap = catOld.toMap()

        val map = catNewMap.mapValues { if(it.value == null) catOldMap[it.key] else it.value }
        database.child(userId).child(catName).updateChildren(map)
    }

    fun deleteCat(catName: String) {
        database.child(userId).child(catName).removeValue()
    }
}