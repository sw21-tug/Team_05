package at.tugraz05.slimcat

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object LanguageHandler {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setAppLocale(languageFromPreference: String?, context: Context): Context
    {

        if (languageFromPreference != null) {

            val resources: Resources = context.resources
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.toLowerCase(Locale.ROOT)))

            return context.createConfigurationContext(config)
        }
        return context
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setLanguage(context: Context): Context{
        val language = context.getSharedPreferences("userprefs", AppCompatActivity.MODE_PRIVATE).getInt("language", 0 )
        if (language == SettingsActivity.ENGLISH) {
            Log.d("language", "English")
            setAppLocale("en", context)
        }
        else if (language == SettingsActivity.MANDARIN){
            Log.d("language", "Mandarin")
            setAppLocale("zh", context)
        }
        return context
    }
}