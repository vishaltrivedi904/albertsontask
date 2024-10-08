package com.example.albertsontask.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val gender: String?,
    val email: String?,
    val phone: String?,
    val location: Location?,
    val name: Name?,
    val picture: Picture?
) : Parcelable