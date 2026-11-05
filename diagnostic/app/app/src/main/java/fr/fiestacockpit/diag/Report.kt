package fr.fiestacockpit.diag

import org.json.JSONArray
import org.json.JSONObject

/**
 * Déductions prudentes. Chaque indice porte son niveau : ce sont des HYPOTHÈSES d'orientation,
 * jamais une confirmation que le CAN est lisible.
 */
object Hints {
    fun from(r: JSONObject): JSONArray {
        val a = JSONArray()
        fun add(level: String, text: String) { a.put(JSONObject().put("level", level).put("text", text)) }

        val props = r.optJSONObject("props")
        val probe = r.optJSONObject("fytProbe")
        fun prop(k: String) = props?.opt(k)?.takeIf { it != JSONObject.NULL }?.toString()

        if (prop("ro.build.fytid") != null || prop("ro.build.fytmanufacturer") != null || prop("ro.fyt.platform") != null)
            add("indice", "Propriétés ro.*fyt* présentes : firmware de famille FYT/SYU probable.")
        if (prop("ro.qf.platform") != null || probe?.optJSONObject("com.qf.framework")?.optBoolean("present") == true)
            add("indice", "Marqueurs « qf » présents : famille QF (Hizpo/ROCO K706 selon sources communautaires), pas forcément com.syu.ms.")
        val ms = probe?.optJSONObject("com.syu.ms")
        if (ms?.optBoolean("present") == true) {
            add("indice", "Package com.syu.ms installé. Reste à savoir si un service est exporté et accessible : non testé par cet outil.")
        } else if (ms != null) {
            add("constat", "com.syu.ms absent : l'intégration CAN via ce service est écartée sur ce firmware.")
        }
        val gps = r.optJSONObject("gps")
        if (gps != null && gps.optInt("maxSatellitesUsedInFix") >= 4) add("constat", "GNSS : fix obtenu pendant la fenêtre de mesure.")
        val ms2 = r.optJSONObject("mediaSessions")
        if (ms2 != null && ms2.optBoolean("notificationListenerEnabled") && ms2.optInt("count") == 0)
            add("constat", "Aucune session média active pendant le test (absence de lecture, ou source non publiée : à interpréter avec la checklist).")
        return a
    }
}

/** Rendu texte lisible du rapport (mêmes informations que le JSON). */
object TextReport {
    fun render(r: JSONObject): String {
        val sb = StringBuilder()
        sb.appendLine("RAPPORT FIESTA DIAG — ${r.optString("date")} — lecture seule")
        for (section in listOf("device", "display", "props", "fytProbe", "packages", "gps", "mediaSessions", "permissions", "hints")) {
            sb.appendLine().appendLine("== $section ==")
            sb.appendLine(r.opt(section)?.let { if (it is JSONObject) it.toString(2) else if (it is JSONArray) it.toString(2) else it.toString() } ?: "(absent)")
        }
        return sb.toString()
    }
}
