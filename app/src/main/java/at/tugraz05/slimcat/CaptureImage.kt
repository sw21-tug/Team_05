package at.tugraz05.slimcat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.database.core.Path
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.absolutePathString

class CaptureImage {
    companion object {
        fun captureImage(context: Activity, givenPath: String = ""): String? {
            lateinit var photoURI: Uri
            lateinit var path: String

            if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        context,
                        arrayOf(android.Manifest.permission.CAMERA),
                        100
                )
                return null
            }
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile(context, givenPath)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.w("image", ex.toString())
                return null
            }
            photoFile?.also {
                photoURI = FileProvider.getUriForFile(
                        context,
                        "at.tugraz05.slimcat.fileprovider", it
                )
                path = it.absolutePath
                Log.d("photo", it.absolutePath)
            }

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Continue only if the File was successfully created
                photoFile?.also {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(context, takePictureIntent, 100, null)
                }
            }
            return path
        }

        @Throws(IOException::class)
        fun createImageFile(context: Activity, path: String = ""): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (storageDir == null)
                Log.d("photo", "getExternalFilesDir failed")
            val newpath = Files.createDirectories(storageDir!!.toPath().resolve(path))
            Log.d("photo", "new path ${newpath.toFile().absolutePath}")
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    newpath.toFile() /* directory */
            )
        }
    }

}