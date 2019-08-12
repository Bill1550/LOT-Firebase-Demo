package com.loneoaktech.test.firebasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.loneoaktech.test.firebasedemo.utility.summary
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        readFirebaseId()
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
}
