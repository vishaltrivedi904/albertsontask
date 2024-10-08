package com.example.albertsontask.data.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val gender: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val name: String?,
    val picture: String?
) : Parcelable