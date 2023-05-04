package com.king.superdemo.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.king.superdemo.gson.GsonUtil
import java.io.File

object FileUtil {
    fun getVideoThumbnail(filePath: String?): Bitmap? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)
        return retriever.frameAtTime
    }

    /**
     * 使用mediaProvider获取文件的URI
     * @param file
     */
    fun getFileIntent(context: Context, file: File): Intent {
        val projection = MediaStore.MediaColumns._ID;
        val where = MediaStore.MediaColumns.DISPLAY_NAME + " = ?"
        var baseUri = MediaStore.Files.getContentUri("external")
        val name = file.name;
        var uri : Uri = baseUri
        val cursor = context.contentResolver.query(baseUri, arrayOf(projection), where, arrayOf(name), null)
        cursor?.let { cursor -> cursor.moveToNext()
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            if (id != 0L){
                uri = Uri.withAppendedPath(baseUri, id.toString())
            }
        }
        val intent = Intent()
        with(intent) {
            action = Intent.ACTION_VIEW
            intent.setDataAndType(uri,  getMIMEType(file, context))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return intent
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    fun getMIMEType(file: File, context: Context): String {
        Log.i("wq", "getMIMEType: filePath = ${file.absolutePath}");
        var type = "*/*"
        val fileName = file.name
        //获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fileName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名 */
        val end = fileName.substring(dotIndex + 1, fileName.length).toLowerCase()
        Log.i("wq", "getMIMEType: end = ${end}");
        if (end === "") {
            return type
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        type = GsonUtil().getMimeJson(context).filter { it.key == end }.get(0).value
        Log.i("wq", "getMIMEType: type= ${type}");
        return type
    }
}