package com.king.superdemo.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

object ShellUtil {

    val COMMAND_SU       = "su"
    val COMMAND_SH       = "sh"
    val COMMAND_EXIT     = "exit\n"
    val COMMAND_LINE_END = "\n"


    /**
    * result of command
    * <ul>
    * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
    * linux shell</li>
    * <li>{@link CommandResult#successMsg} means success message of command result</li>
    * <li>{@link CommandResult#errorMsg} means error message of command result</li>
    * </ul>
    */
    data class CommandResult(var result: Int, var successMsg: String = "", var errorMsg: String = "")

    /**
     * execute shell command
     *
     * @param command command
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    fun execCommand(command: String, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
        return execCommand(arrayOf(command), isRoot, isNeedResultMsg)
    }

    /**
     * execute shell commands
     *
     * @param commands command array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     */
    fun execCommand(
        commands: Array<String?>?,
        isRoot: Boolean,
        isNeedResultMsg: Boolean
    ): CommandResult {
        var result = -1
        if (commands == null || commands.size == 0) {
            return CommandResult(result)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: java.lang.StringBuilder? = null
        var errorMsg: java.lang.StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec(if (isRoot) COMMAND_SU else COMMAND_SH)
            os = DataOutputStream(process!!.outputStream)
            for (command in commands) {
                if (command == null) {
                    continue
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.toByteArray())
                os.writeBytes(COMMAND_LINE_END)
                os.flush()
            }
            os.writeBytes(COMMAND_EXIT)
            os.flush()
            result = process.waitFor()
            // get command result
            if (isNeedResultMsg) {
                successMsg = java.lang.StringBuilder()
                errorMsg = java.lang.StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String?
                while (successResult.readLine().also { s = it } != null) {
                    successMsg.append(s)
                }
                while (errorResult.readLine().also { s = it } != null) {
                    errorMsg.append(s)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return CommandResult(result, successMsg.toString(), errorMsg.toString())
    }

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

    /**
     * check whether has root permission
     *
     * @return
     */
    fun checkRootPermission(): Boolean {
        return execCommand("echo root", false, false)?.result == 0
    }

}