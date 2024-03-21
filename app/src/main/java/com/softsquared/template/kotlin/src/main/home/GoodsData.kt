package com.softsquared.template.kotlin.src.main.home

data class GoodsData(
    var productidx : Int,
    var price : String,
    var title : String,
    var img : String,
    var isFav : Boolean,
    var safepay : Boolean,
)