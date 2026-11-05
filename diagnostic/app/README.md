# Fiesta Diag — prototype (non compilé, non testé)

Application Android minimale, **lecture seule**, sans dépendance externe ni accès réseau. Voir `docs/APP_DIAGNOSTIC.md`.

## Compiler (sur un PC avec Android Studio)
1. Ouvrir le dossier `diagnostic/app/` dans Android Studio (JDK 17, SDK 34).
2. Laisser Gradle synchroniser ; si les versions de plugins de `build.gradle.kts` sont refusées, utiliser celles suggérées par l'IDE.
3. *Build > Build APK(s)* (debug suffit). L'APK se trouve dans `app/build/outputs/apk/debug/`.
4. Ligne de commande (après `gradle wrapper` si besoin) : `./gradlew assembleDebug`.

## Installer
Uniquement après relecture du code et accord du propriétaire de l'autoradio. Copier l'APK sur clé USB, l'ouvrir avec l'explorateur de fichiers (autoriser les sources inconnues pour cet explorateur si demandé). Ne pas contourner par root.

## Utiliser
Véhicule à l'arrêt, ciel dégagé : bouton 1 (localisation), bouton 2 (activer « Fiesta Diag » dans Accès aux notifications, puis lancer une musique), bouton 3 (30 s). Les rapports sont dans `Android/data/fr.fiestacockpit.diag/files/reports/`. Relire et anonymiser avant tout partage ; ne jamais committer de rapport brut.

## Désinstaller
Réglages > Applis > Fiesta Diag > Désinstaller ; désactiver l'accès aux notifications.
