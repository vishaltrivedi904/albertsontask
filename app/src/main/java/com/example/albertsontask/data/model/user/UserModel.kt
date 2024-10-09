package com.example.albertsontask.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val results: MutableList<Result>
) : Parcelable