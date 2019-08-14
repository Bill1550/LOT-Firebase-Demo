package com.loneoaktech.test.firebasedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.loneoaktech.test.firebasedemo.utility.summary
import com.loneoaktech.test.firebasedemo.utility.toMap
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        const val FAVORITE_DOG_KEY = "favorite_dog"
    }

    val remoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent?.dumpExtras()

        savedInstanceState ?: let {
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(30)
                .build().let { settings ->
                    remoteConfig.setConfigSettings(settings)
                }

            remoteConfig.setDefaults(R.xml.remote_config_defaults)
        }
    }

    override fun onStart() {
        super.onStart()
        readFirebaseId()

        readRemoteConfig()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Timber.i("onNewIntent")
        intent?.dumpExtras()
    }

    private fun readFirebaseId() {

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isComplete ) {
                    Timber.i("Firebase token: ${task.result?.token}" )

                } else {
                    Timber.e("Firebase task failed to complete, ex=${task.exception?.summary()}")
                }
            }
    }

    private fun Intent.dumpExtras() {
        Timber.i("Intent extra keys=${extras?.toMap()?.toMap()}")
    }

    private fun readRemoteConfig() {

        Timber.i("Starting, favorite dog=${getFavoriteDog()}")

        // initiate a read
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful ) {
                    Timber.i("Read successful, favorte is now=${getFavoriteDog()}")
                } else {
                    Timber.e("Read failed, error=${task.exception?.summary()}")
                }
            }


    }

    private fun getFavoriteDog(): String {
        return remoteConfig.getString(FAVORITE_DOG_KEY)
    }
}
