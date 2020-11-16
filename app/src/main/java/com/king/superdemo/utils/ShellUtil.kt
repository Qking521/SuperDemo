package com.king.superdemo.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object ShellUtil {
    fun execShell(cmd: String?): String {
        val sb = StringBuilder()
        try {
            val process = Runtime.getRuntime().exec(cmd)
            val br = BufferedReader(InputStreamReader(process.inputStream))
            var read: Int
            val buffers = CharArray(1024)
            while (br.read(buffers).also { read = it } > 0) {
                sb.append(buffers, 0, read)
            }
            br.close()
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}