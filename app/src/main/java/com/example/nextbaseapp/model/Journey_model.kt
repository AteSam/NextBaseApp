package com.example.nextbaseapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Journey_model(

     @Expose
     @SerializedName("data")
        val data: List<Data>,

     @Expose
     @SerializedName("hasMore")
        val hasMore: Boolean
    )