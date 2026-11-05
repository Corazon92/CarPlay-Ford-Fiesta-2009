package fr.fiestacockpit.diag

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.LocationManager
import android.media.session.MediaSessionManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Collecteurs en LECTURE SEULE. Chacun renvoie un JSONObject et ne lève jamais :
 * une erreur devient {"error": "..."} pour que le rapport reste exploitable.
 * Aucun bindService() sur un service tiers, aucun broadcast, aucun accès CAN.
 */
object Collectors {

    /** Clés getprop lues (liste blanche). Pas de numéro de série, IMEI, MAC, etc. */
    private val PROPS = listOf(
        "ro.product.manufacturer", "ro.product.brand", "ro.product.model", "ro.product.device",
        "ro.product.board", "ro.board.platform", "ro.hardware", "ro.soc.model", "ro.soc.manufacturer",
        "ro.build.version.release", "ro.build.version.sdk", "ro.build.version.security_patch",
        "ro.build.display.id", "ro.build.fingerprint", "ro.build.type", "ro.build.tags",
        "ro.product.cpu.abi",
        // Indices de famille de firmware (niveau « témoignage communautaire », voir docs/RECHERCHE_FYT_CAN.md)
        "ro.build.fytmanufacturer", "ro.build.fytid", "ro.fyt.platform", "ro.qf.platform",
        "persist.sys.qf.launcher", "sys.qf.is.acc.on", "sys.carlink.type", "ro.vendor.certificate"
    )

    /** Préfixes/mots de package pertinents. Une correspondance oriente la recherche, ne prouve rien. */
    private val HINTS = listOf(
        "com.syu", "com.fyt", "com.qf", "com.microntek", "com.tw.", "com.tv.carinfo", "com.hcn",
        "zjinnova", "zlink", "carlink", "carplay", "com.apple", "projection",
        "com.spotify", "deezer", "com.google.android.apps.maps", "com.waze",
        "com.android.bluetooth", "launcher"
    )

    private fun err(t: Throwable) = JSONObject().put("error", t.javaClass.simpleName + ": " + (t.message ?: ""))

    private fun getprop(key: String): String? = try {
        val p = ProcessBuilder("getprop", key).redirectErrorStream(true).start()
        val out = p.inputStream.bufferedReader().readText().trim()
        p.waitFor(2, TimeUnit.SECONDS)
        out.ifEmpty { null }
    } catch (_: Throwable) {
        null
    }

    fun device(ctx: Context): JSONObject = try {
        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo().also { am.getMemoryInfo(it) }
        val stat = StatFs(Environment.getDataDirectory().path)
        JSONObject()
            .put("manufacturer", Build.MANUFACTURER).put("brand", Build.BRAND)
            .put("model", Build.MODEL).put("device", Build.DEVICE).put("board", Build.BOARD)
            .put("hardware", Build.HARDWARE)
            .put("androidRelease", Build.VERSION.RELEASE).put("sdkInt", Build.VERSION.SDK_INT)
            .put("securityPatch", Build.VERSION.SECURITY_PATCH)
            .put("buildDisplayId", Build.DISPLAY)
            .put("abis", JSONArray(Build.SUPPORTED_ABIS.toList()))
            .put("socModel", if (Build.VERSION.SDK_INT >= 31) Build.SOC_MODEL else JSONObject.NULL)
            .put("socManufacturer", if (Build.VERSION.SDK_INT >= 31) Build.SOC_MANUFACTURER else JSONObject.NULL)
            .put("ramTotalMB", mi.totalMem / (1024 * 1024))
            .put("storageTotalGB", stat.totalBytes / 1_000_000_000L)
            .put("storageFreeGB", stat.availableBytes / 1_000_000_000L)
            .put("cpuCores", Runtime.getRuntime().availableProcessors())
    } catch (t: Throwable) {
        err(t)
    }

    /** Résolution réelle de l'écran, densité et « smallest width » (dp). */
    @Suppress("DEPRECATION")
    fun display(ctx: Context): JSONObject = try {
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val real = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(real)
        val app = ctx.resources.displayMetrics
        val cfg = ctx.resources.configuration
        JSONObject()
            .put("realWidthPx", real.widthPixels).put("realHeightPx", real.heightPixels)
            .put("appWidthPx", app.widthPixels).put("appHeightPx", app.heightPixels)
            .put("density", real.density.toDouble()).put("densityDpi", real.densityDpi)
            .put("xdpi", real.xdpi.toDouble()).put("ydpi", real.ydpi.toDouble())
            .put("fontScale", cfg.fontScale.toDouble())
            .put("smallestWidthDp", cfg.smallestScreenWidthDp)
            .put("widthDp", cfg.screenWidthDp).put("heightDp", cfg.screenHeightDp)
            .put("refreshRateHz", wm.defaultDisplay.refreshRate.toDouble())
            .put("note", "Comparer realWidthPx x realHeightPx avec les dimensions d'une capture d'écran.")
    } catch (t: Throwable) {
        err(t)
    }

    fun props(): JSONObject {
        val o = JSONObject()
        PROPS.forEach { o.put(it, getprop(it) ?: JSONObject.NULL) }
        return o
    }

    /** Applications pertinentes (HINTS) + applications HOME. Pas d'inventaire complet (vie privée). */
    @SuppressLint("QueryPermissionsNeeded")
    fun packages(ctx: Context): JSONObject = try {
        val pm = ctx.packageManager
        val all = pm.getInstalledPackages(0)
        val rel = JSONArray()
        all.filter { p -> HINTS.any { p.packageName.lowercase().contains(it) } }
            .sortedBy { it.packageName }
            .forEach { p ->
                val ai = p.applicationInfo
                rel.put(
                    JSONObject().put("package", p.packageName)
                        .put("versionName", p.versionName ?: JSONObject.NULL)
                        .put("system", ai != null && (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0)
                        .put("label", if (ai != null) pm.getApplicationLabel(ai).toString() else JSONObject.NULL)
                )
            }
        val homeIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val homes = JSONArray()
        pm.queryIntentActivities(homeIntent, 0).forEach { homes.put(it.activityInfo.packageName + "/" + it.activityInfo.name) }
        val def = pm.resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY)
        JSONObject().put("installedTotal", all.size).put("relevant", rel).put("homeActivities", homes)
            .put("defaultHome", def?.activityInfo?.packageName ?: JSONObject.NULL)
    } catch (t: Throwable) {
        err(t)
    }

    /**
     * Présence (PAS accès) des packages FYT/QF : services déclarés, exportés ou non, protégés ou non
     * par une permission. On ne fait JAMAIS bindService().
     */
    @Suppress("DEPRECATION")
    fun fytProbe(ctx: Context): JSONObject = try {
        val pm = ctx.packageManager
        val o = JSONObject()
        for (pkg in listOf("com.syu.ms", "com.syu.canbus", "com.qf.framework")) {
            try {
                val pi = pm.getPackageInfo(pkg, PackageManager.GET_SERVICES)
                val svcs = JSONArray()
                pi.services?.forEach { s ->
                    svcs.put(
                        JSONObject().put("name", s.name).put("exported", s.exported)
                            .put("permission", s.permission ?: JSONObject.NULL)
                    )
                }
                o.put(pkg, JSONObject().put("present", true).put("versionName", pi.versionName ?: JSONObject.NULL).put("services", svcs))
            } catch (_: PackageManager.NameNotFoundException) {
                o.put(pkg, JSONObject().put("present", false))
            }
        }
        o.put("note", "present=true ne prouve ni l'accès ni la présence de données CAN décodées.")
    } catch (t: Throwable) {
        err(t)
    }

    fun hasLocationPermission(ctx: Context) =
        ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    /** Écoute GNSS pendant [seconds] s. AUCUNE coordonnée n'est enregistrée : seulement des compteurs. */
    @SuppressLint("MissingPermission")
    fun gps(ctx: Context, seconds: Int, done: (JSONObject) -> Unit) {
        val lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = try { lm.isProviderEnabled(LocationManager.GPS_PROVIDER) } catch (_: Throwable) { false }
        val o = JSONObject().put("gpsProviderEnabled", enabled).put("permissionGranted", hasLocationPermission(ctx))
        if (!hasLocationPermission(ctx)) {
            done(o.put("status", "permission localisation manquante"))
            return
        }
        val handler = Handler(Looper.getMainLooper())
        var maxView = 0
        var maxUsed = 0
        var firstFixMs: Long? = null
        val cb = object : GnssStatus.Callback() {
            override fun onFirstFix(ttffMillis: Int) { firstFixMs = ttffMillis.toLong() }
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                var used = 0
                for (i in 0 until status.satelliteCount) if (status.usedInFix(i)) used++
                maxView = maxOf(maxView, status.satelliteCount)
                maxUsed = maxOf(maxUsed, used)
            }
        }
        try {
            lm.registerGnssStatusCallback(cb, handler)
        } catch (t: Throwable) {
            done(o.put("error", t.toString()))
            return
        }
        handler.postDelayed({
            try { lm.unregisterGnssStatusCallback(cb) } catch (_: Throwable) {}
            done(
                o.put("windowSeconds", seconds).put("maxSatellitesInView", maxView)
                    .put("maxSatellitesUsedInFix", maxUsed)
                    .put("timeToFirstFixMs", firstFixMs ?: JSONObject.NULL)
                    .put(
                        "status",
                        if (maxUsed >= 4) "fix probable"
                        else if (maxView > 0) "satellites vus, pas de fix"
                        else "aucun satellite vu (antenne / ciel / fourniture du MCU ?)"
                    )
            )
        }, seconds * 1000L)
    }

    fun notificationListenerEnabled(ctx: Context): Boolean {
        val flat = Settings.Secure.getString(ctx.contentResolver, "enabled_notification_listeners") ?: return false
        return flat.contains(ctx.packageName)
    }

    /** Sessions média actives. Ni titre ni artiste : package, état de lecture, présence de métadonnées. */
    fun mediaSessions(ctx: Context): JSONObject = try {
        val enabled = notificationListenerEnabled(ctx)
        val o = JSONObject().put("notificationListenerEnabled", enabled)
        if (!enabled) {
            o.put("status", "Activer « Fiesta Diag » dans Réglages > Accès aux notifications, lancer une lecture, puis relancer le diagnostic.")
        } else {
            val msm = ctx.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
            val list = JSONArray()
            msm.getActiveSessions(ComponentName(ctx, DiagListenerService::class.java)).forEach { c ->
                list.put(
                    JSONObject().put("package", c.packageName)
                        .put("playbackState", c.playbackState?.state ?: JSONObject.NULL)
                        .put("hasMetadata", c.metadata != null)
                )
            }
            o.put("sessions", list).put("count", list.length())
        }
        o
    } catch (t: Throwable) {
        err(t)
    }

    /** Permissions demandées par CETTE application : accordées / manquantes. */
    fun permissions(ctx: Context): JSONObject = try {
        val pi = ctx.packageManager.getPackageInfo(ctx.packageName, PackageManager.GET_PERMISSIONS)
        val granted = JSONArray()
        val missing = JSONArray()
        pi.requestedPermissions?.forEachIndexed { i, p ->
            val ok = (pi.requestedPermissionsFlags!![i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
            (if (ok) granted else missing).put(p)
        }
        JSONObject().put("granted", granted).put("missing", missing)
            .put("notificationListenerEnabled", notificationListenerEnabled(ctx))
    } catch (t: Throwable) {
        err(t)
    }
}
