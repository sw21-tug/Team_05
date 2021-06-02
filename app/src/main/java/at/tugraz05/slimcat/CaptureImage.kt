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
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.*


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
                    Constants.TAKE_IMAGE
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
                        Constants.provider, it
                )
                path = it.absolutePath
            }

            val capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Continue only if the File was successfully created
                photoFile?.also {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
            val get = Intent(Intent.ACTION_PICK).also { getPictureIntent ->
                getPictureIntent.type = "image/*"
            }
            val chooser = Intent.createChooser(capture, null).also { chooserIntent ->
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(get))
            }
            startActivityForResult(context, chooser, Constants.TAKE_IMAGE, null)

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
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    newpath.toFile() /* directory */
            )
        }

        fun receiveIntent(requestCode: Int, resultCode: Int, data: Intent?, context: Context, file: File) {
            if (requestCode != Constants.TAKE_IMAGE || data == null || resultCode != Activity.RESULT_OK)
                return

            if (data.data != null) {
                context.contentResolver.openInputStream(data.data!!)?.copyTo(file.outputStream())
            }
        }
    }

}