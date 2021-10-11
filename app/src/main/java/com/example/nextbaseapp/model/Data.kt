package com.example.nextbaseapp.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("bearing")
    val bearing: Float,

    @SerializedName("datetime")
    val datetime: String,

    @SerializedName("distanceFromLast")
    val distanceFromLast: Int,

    @SerializedName("gpsStatus")
    val gpsStatus: String,

    @SerializedName("lat")
    val lat: Float,

    @SerializedName("lon")
    val lon: Float,

    @SerializedName("speed")
    val speed: Float,

    @SerializedName("xAcc")
    val xAcc: Float,

    @SerializedName("yAcc")
    val yAcc: Float,

    @SerializedName("zAcc")
    val zAcc: Float
)