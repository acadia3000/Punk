package com.acadia.punk.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Beer(
    val id: Int,
    val name: String?,
    val tagline: String?,
    val description: String?,
    val imageUrl: String?
) : Parcelable
