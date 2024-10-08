package com.example.albertsontask.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Name(
    val first: String,
    val last: String,
    val title: String
) : Parcelable