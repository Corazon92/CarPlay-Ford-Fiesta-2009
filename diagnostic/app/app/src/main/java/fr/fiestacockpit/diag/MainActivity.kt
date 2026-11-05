package fr.fiestacockpit.diag

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Écran unique : [Lancer le diagnostic] -> JSON + texte dans le dossier privé de l'appli
 * (Android/data/fr.fiestacockpit.diag/files/reports/). Rien n'est envoyé sur le réseau.
 * L'utilisateur relit le rapport AVANT tout partage.
 */
class MainActivity : Activity() {

    private lateinit var out: TextView
    private var lastText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 24, 24, 24) }
        fun btn(label: String, onClick: () -> Unit) = Button(this).apply {
            text = label; setOnClickListener { onClick() }; root.addView(this)
        }
        btn("1. Autoriser la localisation (GPS)") { requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1) }
        btn("2. Activer l'accès aux notifications (sessions média)") {
            try { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) } catch (_: Throwable) { out.text = "Écran indisponible sur ce firmware." }
        }
        btn("3. Lancer le diagnostic (GPS : 30 s, ciel dégagé)") { run() }
        btn("4. Partager le rapport texte (optionnel)") { share() }
        out = TextView(this).apply { textSize = 13f; setTextIsSelectable(true) }
        root.addView(ScrollView(this).apply { addView(out) }, ViewGroup.LayoutParams(-1, -1))
        setContentView(root)
        out.text = "Lecture seule. Aucune donnée n'est envoyée. Véhicule à l'arrêt."
    }

    private fun run() {
        out.text = "Diagnostic en cours (GPS 30 s)…"
        val report = JSONObject()
            .put("schemaVersion", 1)
            .put("tool", "FiestaDiag 0.1.0")
            .put("date", SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date()))
            .put("device", Collectors.device(this))
            .put("display", Collectors.display(this))
            .put("props", Collectors.props())
            .put("packages", Collectors.packages(this))
            .put("fytProbe", Collectors.fytProbe(this))
            .put("mediaSessions", Collectors.mediaSessions(this))
            .put("permissions", Collectors.permissions(this))
        Collectors.gps(this, 30) { gps ->
            report.put("gps", gps)
            report.put("hints", Hints.from(report))
            val dir = File(getExternalFilesDir(null), "reports").apply { mkdirs() }
            val stamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(Date())
            val json = report.toString(2)
            lastText = TextReport.render(report)
            File(dir, "diag-$stamp.json").writeText(json)
            File(dir, "diag-$stamp.txt").writeText(lastText)
            out.text = lastText + "\n\nFichiers : ${dir.absolutePath}/diag-$stamp.(json|txt)"
        }
    }

    private fun share() {
        if (lastText.isEmpty()) { out.text = "Lancer d'abord le diagnostic."; return }
        try {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, lastText), "Partager"))
        } catch (_: Throwable) {
            out.append("\n\nPartage indisponible : récupérer le fichier via USB / explorateur de fichiers.")
        }
    }
}
