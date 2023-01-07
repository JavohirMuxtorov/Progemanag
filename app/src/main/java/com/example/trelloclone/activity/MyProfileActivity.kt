package com.example.trelloclone.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.trelloclone.R
import com.example.trelloclone.firebase.FirestoreClass
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
        FirestoreClass().loadUserDate(this)
        iv_profile_user_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        btn_update.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            }
        } else {
            Toast.makeText(
                this,
                "Oops, you just denied the permission for storage. You can also allow it from settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.white_back)
            actionBar.title = resources.getString(R.string.my_profile)

        }
        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun setUserDataInUi(user: User) {
        mUserDetails = user
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        et_name_myProfile.setText(user.name)
        et_email_myProfile.setText(user.email)
        if (user.mobile != 0L) {
            et_mobile_myProfile.setText(user.mobile.toString())
        }
    }

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()
        var anyChangesMode = false
        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangesMode = true
        }
        if (et_name_myProfile.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = et_name_myProfile.text.toString()
            anyChangesMode = true
        }
        if (et_mobile_myProfile.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = et_mobile_myProfile.text.toString().toLong()
            anyChangesMode = true
        }
        if (anyChangesMode)
            FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    private fun uploadUserImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + Constants.getFileExtensions(
                    this,
                    mSelectedImageFileUri
                )
            )

            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            mProfileImageURL = uri.toString()
                            updateUserProfileData()

                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this@MyProfileActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("erorr", exception.message.toString())

                    hideProgressDialog()
                }
        }
    }



    fun profileUpdatesSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }


}