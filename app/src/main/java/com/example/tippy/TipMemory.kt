package com.example.tippy

import java.io.Serializable

class TipMemory(
    val baseAmount: String,
    val percent: String,
    val peopleCount: String,
    val tipAmount: String,
    val totalAmount: String
) : Serializable {
}