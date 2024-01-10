package com.king.superdemo.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.king.superdemo.R
import java.io.*
import java.lang.reflect.Type


class GsonUtil{

    data class Mime(var key: String, var value: String)

    open fun getMimeJson(context: Context) : ArrayList<Mime>{
        val data = read(context.resources.openRawResource(R.raw.mime), "utf-8")
        val listType: Type = object : TypeToken<List<Mime?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    fun read(inputStream: InputStream, encode: String): String {
        if (inputStream != null) {
            try {
                val reader = BufferedReader(InputStreamReader(inputStream, encode))
                var line = reader.readLine()
                var result = ""
                while (line != null) {
                    result += line
                    line = reader.readLine()
                }
                inputStream.close()
                return result
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }




}