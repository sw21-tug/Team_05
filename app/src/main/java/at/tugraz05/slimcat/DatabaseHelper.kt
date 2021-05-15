package at.tugraz05.slimcat

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

open class DatabaseHelper private constructor() {
    private lateinit var database: DatabaseReference
    private lateinit var dataSnapshot: DataSnapshot
    private lateinit var userId: String
    private lateinit var storage: StorageReference

    open fun maybeInit(applicationContext: Context) {
        if (this::database.isInitialized)
            return
        initializeDatabaseReference()
        addPostEventListener()
        checkAndCreateUserId(applicationContext)
    }

    private fun initializeDatabaseReference() {
        Firebase.database.setPersistenceEnabled(true)
        database = Firebase.database.reference
        storage = FirebaseStorage.getInstance().reference
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

    private fun checkAndCreateUserId(applicationContext: Context) {
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
                userId = createUserId(null)
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

    private fun addPostEventListener() {
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


    open fun addValueEventListener(err: (error: DatabaseError) -> Unit = {}, cb: (snapshot: DataSnapshot) -> Unit) {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cb(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                err(error)
            }
        })
    }

    open fun writeNewCat(cat: CatDataClass) {
        val catName: String = cat.name!!
        database.child(userId).child(catName).setValue(cat.toMap())
    }

    open fun readUserCat(catName: String): CatDataClass? {
        return dataSnapshot.child(userId).child(catName).getValue(CatDataClass::class.java)
    }

    open fun readUserCats(): List<CatDataClass?> {
        return dataSnapshot.child(userId).children.map { it.getValue(CatDataClass::class.java) }
    }

    open fun editUser(oldCatName: String, cat: CatDataClass) {
        val catOld = readUserCat(oldCatName)!!
        val catNewMap = cat.toMap()
        val catOldMap = catOld.toMap()

        val map = catNewMap.mapValues { if(it.value == null) catOldMap[it.key] else it.value }
        if (oldCatName != cat.name)
            deleteCat(oldCatName)
        database.child(userId).child(cat.name!!).updateChildren(map)
    }

    open fun deleteCat(catName: String) {
        database.child(userId).child(catName).removeValue()
    }

    open fun uploadImagesToFirebase(name: String, contentUri: Uri, onSuccess: (Uri) -> Unit) {
        val image = storage.child(name)
        image.putFile(contentUri).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener(onSuccess)
        }.addOnFailureListener {
            Log.d("Firebase", "Upload failed ($name)")
        }
    }

    open fun getImage(name: String, destination: File, onSuccess: (FileDownloadTask.TaskSnapshot) -> Unit) {
        storage.child(name).getFile(destination).addOnSuccessListener(onSuccess)
    }

    companion object {
        private lateinit var instance: DatabaseHelper

        fun mock(d: DatabaseHelper) {
            instance = d
        }

        fun get(): DatabaseHelper {
            if (!this::instance.isInitialized)
                instance = DatabaseHelper()
            return instance
        }
    }
}