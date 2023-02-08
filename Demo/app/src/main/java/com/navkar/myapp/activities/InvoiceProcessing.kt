package com.navkar.myapp.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.*


class InvoiceProcessing {

    fun processText(text: FirebaseVisionText, context: Context?): HashMap<String, String>? {
        val blocks = text.blocks
        if (blocks.size == 0) {
             Toast.makeText(context, "No Text :(", Toast.LENGTH_LONG).show()
            return null
        }
        val map = TreeMap<String, String>()
        for (block in text.blocks) {
            for (line in block.lines) {
                val rect = line.boundingBox
                val y = rect!!.exactCenterY().toString()
                val lineTxt = line.text
                map[y] = lineTxt
            }
        }
        val dataMap = HashMap<String, String>()
        val orderedData: List<String> = ArrayList(map.values)
        try {

            for (i in orderedData.indices) {
                Log.d("AAAAA", orderedData[i])

                if (orderedData[i].toLowerCase().contains("mob")) {
                    if (dataMap["name"] == null)
                        dataMap["name"] = orderedData[i - 1]
                    else
                        dataMap["name1"] = orderedData[i - 1]

                    var a = orderedData[i].split(" ")
                    if (dataMap["mobile"] == null)
                        dataMap["mobile"] = a[a.size - 1]
                    else
                        dataMap["mobile1"] = a[a.size - 1]
                }

                if (orderedData[i].toLowerCase().contains("gst")) {
                    var a = orderedData[i].split(":")
                    dataMap["gst"] = a[a.size - 1]
                }

                if (orderedData[i].toLowerCase().contains("email")) {
                    var a = orderedData[i].split(" ")
                    dataMap["email"] = a[a.size - 1]
                }

                if (orderedData[i].toLowerCase().contains("hsn")) {
                    var a = orderedData[i].split(" ")
                    dataMap["hsn"] = a[a.size - 1]
                }

                if (orderedData[i].toLowerCase().contains("industry")) {
                    var a = orderedData[i].split(" ")
                    dataMap["industry"] = a[a.size - 1]
                }

                if (orderedData[i].toLowerCase().contains("address")) {
                    var a = orderedData[i].split(":")
                    var address: String = ""
                    for (i in a.indices) {
                        if (!a[i].toLowerCase().contains("address"))
                            address = address + " " + a[i]
                    }
                    if (dataMap["address"] == null)
                        dataMap["address"] = address
                }

                if (orderedData[i].toLowerCase().contains("product")) {
                    var a = orderedData[i].split(":")
                    var product: String = ""
                    for (i in a.indices) {
                        if (!a[i].toLowerCase().contains("product"))
                            product = product + " " + a[i]
                    }
                    if (dataMap["product"] == null)
                        dataMap["product"] = product
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataMap
    }
}

