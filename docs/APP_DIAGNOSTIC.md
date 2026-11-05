# Application de diagnostic Android — étude et prototype

## Besoin
Une petite app **indépendante du launcher** qui produit un rapport anonymisé : Android, résolution réelle, packages pertinents, services FYT/QF détectables, GPS, sessions média, permissions. Pas de CAN maintenant.

## Outils existants (comparaison)

| Besoin | Outil existant | Verdict |
| --- | --- | --- |
| Infos matériel/Android générales | Apps « Device Info »/CPU-Z de magasin | Plus complètes sur le SoC, mais sans lecture des packages FYT/QF ni sessions média ; installation (Play Store parfois absent sur autoradio) à valider |
| Résolution, densité | `adb shell wm size / wm density` | Plus fiable, **si** ADB est ouvert (ne pas l'activer pour ça) |
| Packages, propriétés | `adb shell pm list packages`, `getprop` | Plus fiable et complet que l'app, mais requiert un PC + ADB (interdit d'activer sans besoin) |
| Famille FYT / CAN | FET, FYTCanbusMonitor, Dashwheel | Plus « directs » pour le CAN, mais conçus pour agir (root, scripts, écriture possible) ; à ne pas utiliser avant revue |

**Position :** l'app maison reste utile car elle est **minimale, lisible, sans réseau ni écriture**, et construite pour répondre exactement aux questions de la checklist. Si l'ADB devient disponible de façon sûre, ses commandes (ci-dessus) donnent les mêmes informations avec davantage de détails ; l'app n'est alors qu'une commodité.

## Contenu du prototype (`diagnostic/app/`)
`device` (modèle, SoC, RAM, stockage), `display` (px réels, dp, densité), `props` (liste blanche `getprop`, dont marqueurs `fyt`/`qf`), `packages` (préfixes pertinents + applis HOME), `fytProbe` (présence et **exportation** des services de `com.syu.ms`, `com.syu.canbus`, `com.qf.framework`, **sans binding**), `gps` (compteurs de satellites, pas de coordonnées), `mediaSessions` (package/état, sans titres), `permissions`, `hints` (indices prudents). Export JSON + texte local ; partage manuel.

## Anonymisation
Aucun identifiant (série, IMEI, MAC, ANDROID_ID, SSID, nom Bluetooth), aucune coordonnée, aucun titre média, aucune liste complète d'applications. Le fabricant, le modèle, le fingerprint de build et la version MCU restent (utiles et non personnels), mais **relire avant partage**.

## Futur : structure pour une intégration CAN en lecture seule (non implémentée)
Une interface `VehicleSignalSource` (`source`, `status`, `timestamp`, `reason`, conformément au contrat de `AGENTS.md`) serait implémentée par un adaptateur FYT/QF distinct, dans un module séparé, activé uniquement après accord, avec interdiction d'API d'écriture dans l'interface. `fytProbe` établit seulement la présence ; ni binding ni callback ne sont codés.

## Statut et limites
- **Non compilé ni testé** : aucune chaîne Android (SDK/Gradle/JDK) n'est disponible sur la machine de rédaction. Versions des plugins Gradle à vérifier. Relecture humaine obligatoire.
- `ProcessBuilder("getprop")` peut être restreint par SELinux ; les clés absentes sont notées `null` (ni erreur, ni preuve d'absence).
- `getActiveSessions` exige l'activation manuelle de l'accès aux notifications ; certains firmwares ne proposent pas cet écran.
- Aucune des constatations n'équivaut à un accès CAN.
