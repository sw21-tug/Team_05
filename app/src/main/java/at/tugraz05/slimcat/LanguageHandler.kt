package at.tugraz05.slimcat

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object LanguageHandler {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setAppLocale(languageFromPreference: String?, context: Context)
    {

        if (languageFromPreference != null) {

            val resources: Resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(Locale(languageFromPreference.toLowerCase(Locale.ROOT)))
            } else {
                config.setLocale(Locale(languageFromPreference.toLowerCase(Locale.ROOT)))
            }
            resources.updateConfiguration(config, dm)
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setLanguage(context: Context){
        val language = context.getSharedPreferences(Constants.USER_PREFS, AppCompatActivity.MODE_PRIVATE).getInt("language", 0 )
        if (language == SettingsActivity.ENGLISH) {
            Log.d("language", "English")
            setAppLocale("en", context)
        }
        else if (language == SettingsActivity.MANDARIN){
            Log.d("language", "Mandarin")
            setAppLocale("zh", context)
        }
    }
}