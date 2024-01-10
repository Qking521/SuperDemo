package com.king.superdemo.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

/**
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 * 从6.0开始，READ_EXTERNAL_STORAGE权限也是要动态申请的。
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
 * <!--INSTALL_PACKAGES是针对于系统应用的--> 如果是静默安装，则需要INSTALL_PACKAGES权限（
 * 注意：INSTALL_PACKAGES权限是针对于系统应用的，换言之，想要实现静默安装，那你得要是系统应用
 * <uses-permission android:name="android.permission.INSTALL_PACKAGES" />
 */

/**
 * PackageUtils
 *
 * **Install package**
 *  * [PackageUtil.installNormal]
 *  * [PackageUtil.installSilent]
 *  * [PackageUtil.install]
 *
 *
 * **Uninstall package**
 *  * [PackageUtil.uninstallNormal]
 *  * [PackageUtil.uninstallSilent]
 *  * [PackageUtil.uninstall]
 *
 *
 * **Is system application**
 *  * [PackageUtil.isSystemApplication]
 *  * [PackageUtil.isSystemApplication]
 *  * [PackageUtil.isSystemApplication]
 *
 *
 * **Others**
 *  * [PackageUtil.getInstallLocation] get system install location
 *  * [PackageUtil.isTopActivity] whether the app whost package's name is packageName is on the
 * top of the stack
 *  * [PackageUtil.startInstalledAppDetails] start InstalledAppDetails Activity
 *
 *
 * @author [Trinea](http://www.trinea.cn) 2013-5-15
 */
object PackageUtil {
    const val TAG = "PackageUtils"

    /**
     * App installation location settings values, same to
     */
    const val APP_INSTALL_AUTO = 0
    const val APP_INSTALL_INTERNAL = 1
    const val APP_INSTALL_EXTERNAL = 2

    /**
     * install according conditions
     *
     *  * if system application or rooted, see [.installSilent]
     *  * else see [.installNormal]
     *
     *
     * @param context
     * @param filePath
     * @return
     */
    fun install(context: Context, filePath: String): Int {
//        val isRootOrSystem = isSystemApplication(context) || ShellUtil.checkRootPermission()
//        when (isRootOrSystem) {
//            true -> return installSilent(context, filePath)
//            false -> if (installNormal(
//                    context,
//                    filePath
//                )
//            ) INSTALL_SUCCEEDED else INSTALL_FAILED_INVALID_URI
//        }
        if (isSystemApplication(context)) {
            Log.v("wq", "install: 111")
            return installSilent(context, filePath)
        }
        return if (installNormal(
                context,
                filePath
            )
        ) INSTALL_SUCCEEDED else INSTALL_FAILED_INVALID_URI
    }

    /**
     * install package normal by system intent
     *
     * @param context
     * @param filePath file path of package
     * @return whether apk exist
     */
    fun installNormal(context: Context, filePath: String): Boolean {
        val i = Intent(Intent.ACTION_VIEW)
        val file = File(filePath)
        if (!file.exists() || !file.isFile || file.length() <= 0) {
            return false
        }
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
//        i.setDataAndType(Uri.parse("file://$filePath"), "application/vnd.android.package-archive")
        i.setDataAndType(uri, "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
        return true
    }
    /**
     * install package silent by root
     *
     * **Attentions:**
     *  * Don't call this on the ui thread, it may costs some times.
     *  * You should add **android.permission.INSTALL_PACKAGES** in manifest, so no need to request root
     * permission, if you are system app.
     *
     *
     * @param filePath file path of package
     * @param pmParams pm install params
     * @return [PackageUtil.INSTALL_SUCCEEDED] means install success, other means failed. details see
     * [PackageUtil].INSTALL_FAILED_*. same to [PackageManager].INSTALL_*
     */
    /**
     * install package silent by root
     *
     * **Attentions:**
     *  * Don't call this on the ui thread, it may costs some times.
     *  * You should add **android.permission.INSTALL_PACKAGES** in manifest, so no need to request root
     * permission, if you are system app.
     *  * Default pm install params is "-r".
     *
     *
     * @param context
     * @param filePath file path of package
     * @return [PackageUtil.INSTALL_SUCCEEDED] means install success, other means failed. details see
     * [PackageUtil].INSTALL_FAILED_*. same to [PackageManager].INSTALL_*
     * @see .installSilent
     */
    @JvmOverloads
    fun installSilent(
        context: Context?,
        filePath: String?,
        pmParams: String? = " -i -r " + installLocationParams
    ): Int {
        if (filePath == null || filePath.length == 0) {
            return INSTALL_FAILED_INVALID_URI
        }
        val file = File(filePath)
        if (file.length() <= 0 || !file.exists() || !file.isFile) {
            return INSTALL_FAILED_INVALID_URI
        }
        /**
         * if context is system app, don't need root permission, but should add <uses-permission android:name="android.permission.INSTALL_PACKAGES"></uses-permission> in mainfest
         */
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " +
                (pmParams ?: "") + " " +
                filePath.replace(" ", "\\ ")
        val commandResult: ShellUtil.CommandResult =
            ShellUtil.execCommand(command, isSystemApplication(context), true)
        if (commandResult.successMsg != null && commandResult.successMsg.contains("Success")) {
            return INSTALL_SUCCEEDED
        }
        Log.e(TAG,
            "installSilent successMsg:" + commandResult.successMsg +
                    ", ErrorMsg:" + commandResult.errorMsg
        )
        if (commandResult.errorMsg.isEmpty()) {
            return INSTALL_FAILED_OTHER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS") == true) {
            return INSTALL_FAILED_ALREADY_EXISTS
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
            return INSTALL_FAILED_INVALID_APK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
            return INSTALL_FAILED_INVALID_URI
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
            return INSTALL_FAILED_INSUFFICIENT_STORAGE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
            return INSTALL_FAILED_DUPLICATE_PACKAGE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
            return INSTALL_FAILED_NO_SHARED_USER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
            return INSTALL_FAILED_UPDATE_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
            return INSTALL_FAILED_MISSING_SHARED_LIBRARY
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
            return INSTALL_FAILED_REPLACE_COULDNT_DELETE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
            return INSTALL_FAILED_DEXOPT
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
            return INSTALL_FAILED_OLDER_SDK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
            return INSTALL_FAILED_CONFLICTING_PROVIDER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
            return INSTALL_FAILED_NEWER_SDK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
            return INSTALL_FAILED_TEST_ONLY
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
            return INSTALL_FAILED_MISSING_FEATURE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
            return INSTALL_FAILED_CONTAINER_ERROR
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
            return INSTALL_FAILED_INVALID_INSTALL_LOCATION
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
            return INSTALL_FAILED_MEDIA_UNAVAILABLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
            return INSTALL_FAILED_VERIFICATION_TIMEOUT
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
            return INSTALL_FAILED_VERIFICATION_FAILURE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
            return INSTALL_FAILED_PACKAGE_CHANGED
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
            return INSTALL_FAILED_UID_CHANGED
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
            return INSTALL_PARSE_FAILED_NOT_APK
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
            return INSTALL_PARSE_FAILED_BAD_MANIFEST
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_NO_CERTIFICATES
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY
        }
        return if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
            INSTALL_FAILED_INTERNAL_ERROR
        } else INSTALL_FAILED_OTHER
    }

    /**
     * uninstall according conditions
     *
     *  * if system application or rooted, see [.uninstallSilent]
     *  * else see [.uninstallNormal]
     *
     *
     * @param packageName package name of app
     * @return whether package name is empty
     * @return
     */
    fun uninstall(context: Context, packageName: String?): Int {
        if (isSystemApplication(context) || ShellUtil.checkRootPermission()) {
            return uninstallSilent(context, packageName)
        }
        return if (uninstallNormal(
                context,
                packageName
            )
        ) DELETE_SUCCEEDED else DELETE_FAILED_INVALID_PACKAGE
    }

    /**
     * uninstall package normal by system intent
     *
     * @param packageName package name of app
     * @return whether package name is empty
     */
    fun uninstallNormal(context: Context, packageName: String?): Boolean {
        if (packageName == null || packageName.length == 0) {
            return false
        }
        val i = Intent(
            Intent.ACTION_DELETE, Uri.parse(
                StringBuilder(32).append("package:")
                    .append(packageName).toString()
            )
        )
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
        return true
    }
    /**
     * uninstall package silent by root
     *
     * **Attentions:**
     *  * Don't call this on the ui thread, it may costs some times.
     *  * You should add **android.permission.DELETE_PACKAGES** in manifest, so no need to request root
     * permission, if you are system app.
     *
     *
     * @param context file path of package
     * @param packageName package name of app
     * @param isKeepData whether keep the data and cache directories around after package removal
     * @return
     *  * [.DELETE_SUCCEEDED] means uninstall success
     *  * [.DELETE_FAILED_INTERNAL_ERROR] means internal error
     *  * [.DELETE_FAILED_INVALID_PACKAGE] means package name error
     *  * [.DELETE_FAILED_PERMISSION_DENIED] means permission denied
     */
    /**
     * uninstall package and clear data of app silent by root
     *
     * @param context
     * @param packageName package name of app
     * @return
     * @see .uninstallSilent
     */
    @JvmOverloads
    fun uninstallSilent(context: Context?, packageName: String?, isKeepData: Boolean = true): Int {
        if (packageName == null || packageName.length == 0) {
            return DELETE_FAILED_INVALID_PACKAGE
        }
        /**
         * if context is system app, don't need root permission, but should add <uses-permission android:name="android.permission.DELETE_PACKAGES"></uses-permission> in mainfest
         */
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall" +
                (if (isKeepData) " -k " else " ") +
                packageName.replace(" ", "\\ ")
        val commandResult: ShellUtil.CommandResult =
            ShellUtil.execCommand(command, !isSystemApplication(context), true)
        if (commandResult.successMsg != null
            && (commandResult.successMsg.contains("Success"))
        ) {
            return DELETE_SUCCEEDED
        }
        Log.e(
            TAG,
            "uninstallSilent successMsg:" + commandResult.successMsg +
                    ", ErrorMsg:" + commandResult.errorMsg
        )
        if (commandResult.errorMsg == null) {
            return DELETE_FAILED_INTERNAL_ERROR
        }
        return if (commandResult.errorMsg.contains("Permission denied")) {
            DELETE_FAILED_PERMISSION_DENIED
        } else DELETE_FAILED_INTERNAL_ERROR
    }

    /**
     * whether context is system application
     *
     * @return
     */
    fun isSystemApplication(context: Context?): Boolean {
        return if (context == null) {
            false
        } else isSystemApplication(context, context.packageName)
    }

    /**
     * whether packageName is system application
     *
     */
    fun isSystemApplication(context: Context?, packageName: String?): Boolean {
        return if (context == null) {
            false
        } else isSystemApplication(context.packageManager, packageName)
    }

    /**
     * whether packageName is system application
     *
     * @param packageManager
     * @param packageName
     * @return
     *  * if packageManager is null, return false
     *  * if package name is null or is empty, return false
     *  * if package name not exit, return false
     *  * if package name exit, but not system app, return false
     *  * else return true
     *
     */
    fun isSystemApplication(packageManager: PackageManager?, packageName: String?): Boolean {
        if (packageManager == null || packageName == null || packageName.length == 0) {
            return false
        }
        try {
            val app = packageManager.getApplicationInfo(packageName, 0)
            return app != null && app.flags and ApplicationInfo.FLAG_SYSTEM > 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * whether the app whost package's name is packageName is on the top of the stack
     *
     * **Attentions:**
     *  * You should add **android.permission.GET_TASKS** in manifest
     *
     *
     * @param context
     * @param packageName
     * @return if params error or task stack is null, return null, otherwise retun whether the app is on the top of
     * stack
     */
    fun isTopActivity(context: Context?, packageName: String): Boolean? {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null
        }
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasksInfo = activityManager.getRunningTasks(1)
        return if (tasksInfo == null || tasksInfo.isEmpty()) {
            null
        } else try {
            packageName == tasksInfo[0].topActivity!!.packageName
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * get app version code
     *
     * @param context
     * @return
     */
    fun getAppVersionCode(context: Context?): Int {
        if (context != null) {
            val pm = context.packageManager
            if (pm != null) {
                val pi: PackageInfo?
                try {
                    pi = pm.getPackageInfo(context.packageName, 0)
                    if (pi != null) {
                        return pi.versionCode
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        return -1
    }

    /**
     * get system install location<br></br>
     * can be set by System Menu Setting->Storage->Prefered install location
     *
     * @return
     * @see
     */
    val installLocation: Int
        get() {
            val commandResult: ShellUtil.CommandResult = ShellUtil.execCommand(
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location",
                false, true
            )
            if (commandResult.result === 0 && commandResult.successMsg != null && commandResult.successMsg.length > 0) {
                try {
                    Log.v("wq", "commandResult.successMsg: ${commandResult.successMsg}")
                    val location: Int = commandResult.successMsg.substring(0, 1).toInt()
                    when (location) {
                        APP_INSTALL_INTERNAL -> return APP_INSTALL_INTERNAL
                        APP_INSTALL_EXTERNAL -> return APP_INSTALL_EXTERNAL
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    Log.e(TAG, "pm get-install-location error")
                }
            }
            return APP_INSTALL_AUTO
        }

    /**
     * get params for pm install location
     *
     * @return
     */
    private val installLocationParams: String
        private get() {
            val location = installLocation
            when (location) {
                APP_INSTALL_INTERNAL -> return "-f"
                APP_INSTALL_EXTERNAL -> return "-s"
            }
            return ""
        }

    /**
     * start InstalledAppDetails Activity
     *
     * @param context
     * @param packageName
     */
    fun startInstalledAppDetails(context: Context, packageName: String?) {
        val intent = Intent()
        val sdkVersion = Build.VERSION.SDK_INT
        if (sdkVersion >= 9) {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null)
        } else {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra(
                if (sdkVersion == 8) "pkg" else "com.android.settings.ApplicationPkgName",
                packageName
            )
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Installation return code<br></br>
     * install success.
     */
    const val INSTALL_SUCCEEDED = 1

    /**
     * Installation return code<br></br>
     * the package is already installed.
     */
    const val INSTALL_FAILED_ALREADY_EXISTS = -1

    /**
     * Installation return code<br></br>
     * the package archive file is invalid.
     */
    const val INSTALL_FAILED_INVALID_APK = -2

    /**
     * Installation return code<br></br>
     * the URI passed in is invalid.
     */
    const val INSTALL_FAILED_INVALID_URI = -3

    /**
     * Installation return code<br></br>
     * the package manager service found that the device didn't have enough storage space to install the app.
     */
    const val INSTALL_FAILED_INSUFFICIENT_STORAGE = -4

    /**
     * Installation return code<br></br>
     * a package is already installed with the same name.
     */
    const val INSTALL_FAILED_DUPLICATE_PACKAGE = -5

    /**
     * Installation return code<br></br>
     * the requested shared user does not exist.
     */
    const val INSTALL_FAILED_NO_SHARED_USER = -6

    /**
     * Installation return code<br></br>
     * a previously installed package of the same name has a different signature than the new package (and the old
     * package's data was not removed).
     */
    const val INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7

    /**
     * Installation return code<br></br>
     * the new package is requested a shared user which is already installed on the device and does not have matching
     * signature.
     */
    const val INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8

    /**
     * Installation return code<br></br>
     * the new package uses a shared library that is not available.
     */
    const val INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9

    /**
     * Installation return code<br></br>
     * the new package uses a shared library that is not available.
     */
    const val INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10

    /**
     * Installation return code<br></br>
     * the new package failed while optimizing and validating its dex files, either because there was not enough storage
     * or the validation failed.
     */
    const val INSTALL_FAILED_DEXOPT = -11

    /**
     * Installation return code<br></br>
     * the new package failed because the current SDK version is older than that required by the package.
     */
    const val INSTALL_FAILED_OLDER_SDK = -12

    /**
     * Installation return code<br></br>
     * the new package failed because it contains a content provider with the same authority as a provider already
     * installed in the system.
     */
    const val INSTALL_FAILED_CONFLICTING_PROVIDER = -13

    /**
     * Installation return code<br></br>
     * the new package failed because the current SDK version is newer than that required by the package.
     */
    const val INSTALL_FAILED_NEWER_SDK = -14

    /**
     * Installation return code<br></br>
     * the new package failed because it has specified that it is a test-only package and the caller has not supplied
     * the  flag.
     */
    const val INSTALL_FAILED_TEST_ONLY = -15

    /**
     * Installation return code<br></br>
     * the package being installed contains native code, but none that is compatible with the the device's CPU_ABI.
     */
    const val INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16

    /**
     * Installation return code<br></br>
     * the new package uses a feature that is not available.
     */
    const val INSTALL_FAILED_MISSING_FEATURE = -17

    /**
     * Installation return code<br></br>
     * a secure container mount point couldn't be accessed on external media.
     */
    const val INSTALL_FAILED_CONTAINER_ERROR = -18

    /**
     * Installation return code<br></br>
     * the new package couldn't be installed in the specified install location.
     */
    const val INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19

    /**
     * Installation return code<br></br>
     * the new package couldn't be installed in the specified install location because the media is not available.
     */
    const val INSTALL_FAILED_MEDIA_UNAVAILABLE = -20

    /**
     * Installation return code<br></br>
     * the new package couldn't be installed because the verification timed out.
     */
    const val INSTALL_FAILED_VERIFICATION_TIMEOUT = -21

    /**
     * Installation return code<br></br>
     * the new package couldn't be installed because the verification did not succeed.
     */
    const val INSTALL_FAILED_VERIFICATION_FAILURE = -22

    /**
     * Installation return code<br></br>
     * the package changed from what the calling program expected.
     */
    const val INSTALL_FAILED_PACKAGE_CHANGED = -23

    /**
     * Installation return code<br></br>
     * the new package is assigned a different UID than it previously held.
     */
    const val INSTALL_FAILED_UID_CHANGED = -24

    /**
     * Installation return code<br></br>
     * if the parser was given a path that is not a file, or does not end with the expected '.apk' extension.
     */
    const val INSTALL_PARSE_FAILED_NOT_APK = -100

    /**
     * Installation return code<br></br>
     * if the parser was unable to retrieve the AndroidManifest.xml file.
     */
    const val INSTALL_PARSE_FAILED_BAD_MANIFEST = -101

    /**
     * Installation return code<br></br>
     * if the parser encountered an unexpected exception.
     */
    const val INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102

    /**
     * Installation return code<br></br>
     * if the parser did not find any certificates in the .apk.
     */
    const val INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103

    /**
     * Installation return code<br></br>
     * if the parser found inconsistent certificates on the files in the .apk.
     */
    const val INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104

    /**
     * Installation return code<br></br>
     * if the parser encountered a CertificateEncodingException in one of the files in the .apk.
     */
    const val INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105

    /**
     * Installation return code<br></br>
     * if the parser encountered a bad or missing package name in the manifest.
     */
    const val INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106

    /**
     * Installation return code<br></br>
     * if the parser encountered a bad shared user id name in the manifest.
     */
    const val INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107

    /**
     * Installation return code<br></br>
     * if the parser encountered some structural problem in the manifest.
     */
    const val INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108

    /**
     * Installation return code<br></br>
     * if the parser did not find any actionable tags (instrumentation or application) in the manifest.
     */
    const val INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109

    /**
     * Installation return code<br></br>
     * if the system failed to install the package because of system issues.
     */
    const val INSTALL_FAILED_INTERNAL_ERROR = -110

    /**
     * Installation return code<br></br>
     * other reason
     */
    const val INSTALL_FAILED_OTHER = -1000000

    /**
     * Uninstall return code<br></br>
     * uninstall success.
     */
    const val DELETE_SUCCEEDED = 1

    /**
     * Uninstall return code<br></br>
     * uninstall fail if the system failed to delete the package for an unspecified reason.
     */
    const val DELETE_FAILED_INTERNAL_ERROR = -1

    /**
     * Uninstall return code<br></br>
     * uninstall fail if the system failed to delete the package because it is the active DevicePolicy manager.
     */
    const val DELETE_FAILED_DEVICE_POLICY_MANAGER = -2

    /**
     * Uninstall return code<br></br>
     * uninstall fail if pcakge name is invalid
     */
    const val DELETE_FAILED_INVALID_PACKAGE = -3

    /**
     * Uninstall return code<br></br>
     * uninstall fail if permission denied
     */
    const val DELETE_FAILED_PERMISSION_DENIED = -4
}