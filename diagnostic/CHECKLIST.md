# Procédure de diagnostic détaillée — Hizpo UIS7862 (Fiesta 2009)

Complète `docs/DIAGNOSTIC_AUTORADIO.md` (qui reste la référence de niveau). Contexte et sources : `docs/RECHERCHE_FYT_CAN.md`. Captures à faire : `CAPTURES.md`. Résultats : copier `RESULTATS_TEMPLATE.md` **hors du dépôt** (données potentiellement sensibles).

## Règles de sécurité (valables pour tous les tests)

- Véhicule **à l'arrêt, frein à main serré, en lieu sûr**. Un passager manipule l'écran si le moteur tourne. Aucun test en roulant.
- **Aucun** root, flash, changement de profil CAN/CAN box, code « usine », écriture de trame, codage, effacement de codes défaut, modification MCU.
- Ne **changer aucun réglage** pour « voir ». Un écran qui propose de modifier : lire, photographier, quitter par Retour.
- Ne pas installer d'APK sans accord explicite (voir B).
- Les captures montrent souvent des noms Bluetooth, SSID, positions : les flouter/masquer avant tout partage ; VIN, plaque, adresses interdits.
- Un test qui échoue ne prouve pas l'impossibilité : noter « non observé ».

Légende du résultat : **Confirme** = ce que le test démontre ; **Ne prouve pas** = ce qu'il ne faut pas en déduire.

---

## PARTIE A — Depuis les réglages Android et les écrans d'origine (aucune installation)

### A1 — Identité système
- **Où :** Réglages > Système/À propos de la tablette (variable) ; écran « Informations » du firmware.
- **Relever :** modèle, version Android affichée, niveau de correctif, numéro de build, **version MCU**, RAM, stockage, noyau si affiché.
- **Captures :** C01 (page À propos complète), C02 (version MCU / infos firmware).
- **Confirme :** version/étiquette de build, indices de famille (chaîne de build contenant FYT/QF/TS…). **Ne prouve pas :** la version réelle d'Android (étiquette parfois gonflée) → recoupé par B (`sdkInt`).
- **Risque :** aucun. Ne pas appuyer à répétition sur « numéro de build » (active le mode développeur : inutile ici).

### A2 — Résolution et densité
- **Où :** Réglages > Affichage ; capture d'écran native (ou bouton capture du firmware).
- **Relever :** dimensions en pixels de l'image capturée (propriétés du fichier), taille d'affichage/densité si proposée.
- **Captures :** C03 (Affichage), C04 (une capture brute d'écran d'accueil, dont on lira les dimensions).
- **Confirme :** résolution utile du rendu. **Ne prouve pas :** la résolution physique de la dalle (la capture peut être redimensionnée) → B donne `realWidthPx/realHeightPx` et `smallestWidthDp`.
- **Risque :** aucun ; ne pas modifier la taille d'affichage.

### A3 — Section CAN / voiture du firmware (consultation)
- **Où :** application « Voiture / Car settings / Véhicule » du firmware, **partie utilisateur** uniquement.
- **Relever :** marque du décodeur et profil Ford/Fiesta s'ils sont **affichés en lecture**.
- **Captures :** C05, C06.
- **Confirme :** niveau 2 (décodeur) si un profil est visible. **Ne prouve pas :** que les données arrivent à Android (niveau 3/4).
- **Risque :** **élevé si l'on bascule en « Réglages usine »** : ne pas saisir de code, ne pas ouvrir ce menu ; un mauvais profil peut couper volant/caméra. Si l'écran propose une liste déroulante, **ne pas y toucher**.

### A4 — Applications système installées
- **Où :** Réglages > Applis > Afficher les applis système ; et le tiroir d'applications du launcher d'origine.
- **Relever :** noms visibles : récepteur CarPlay (ZLink, TLink, « CarLink »…), infos véhicule, radio, Bluetooth, lecteur, navigation.
- **Captures :** C07 à C09 (liste d'applis, fiche de l'appli CarPlay, fiche de l'appli véhicule).
- **Confirme :** les applis présentes par nom. **Ne prouve pas :** le nom de package (rarement affiché) → B.
- **Risque :** ne pas « Désactiver / Forcer l'arrêt / Vider les données » sur une appli système.

### A5 — Écrans véhicule d'origine
- **Où :** appli véhicule/CAN d'origine (portières, informations voiture).
- **Relever :** liste des écrans et champs (portières, coffre, marche arrière, frein à main, ceinture, vitesse, tension, kilométrage, température extérieure, climatisation). Pour chaque champ : visible / absent / toujours figé.
- **Captures :** C10 (chaque écran, ≥1 capture par écran).
- **Confirme :** ce que le décodeur sait afficher avec le profil actuel (niveau 2). **Ne prouve pas :** l'accessibilité par une appli tierce.
- **Risque :** aucun.

### A6 — Réaction aux événements physiques (stationné)
- **Prérequis :** contact mis, moteur arrêté ou au ralenti, frein à main serré.
- **Où :** même appli que A5.
- **Procédure :** ouvrir puis fermer chaque porte, le coffre, serrer/desserrer le frein à main, mettre la marche arrière (levier, **moteur arrêté** de préférence ; caméra de recul si présente), allumer les feux.
- **Relever :** quel indicateur réagit, avec quel délai ; notez si rien ne réagit.
- **Captures :** C11 (porte ouverte), C12 (marche arrière / caméra) — ou courte vidéo écran.
- **Confirme :** signal décodé et affiché pour chaque événement. **Ne prouve pas :** accès tiers.
- **Risque :** manipulation de la boîte à l'arrêt seulement ; ne pas démarrer.

### A7 — CarPlay : récepteur et médias
- **Où :** application CarPlay, volet de notifications, écran de verrouillage éventuel.
- **Procédure :** connecter l'iPhone (câble puis sans fil si dispo), lancer une musique ; tirer le volet de notifications ; noter si un widget média (titre/pochette/boutons) apparaît.
- **Captures :** C13 (volet notif. pendant la lecture CarPlay), C14 (écran CarPlay).
- **Confirme :** si une notification média Android existe (indice de session). **Ne prouve pas :** l'absence de notification n'est **pas** une preuve d'absence de session ; sa présence n'est pas non plus une garantie → B (`mediaSessions`).
- **Risque :** aucun ; ne pas oublier que la navigation CarPlay n'a pas d'API Android.

### A8 — Bluetooth audio et lecteur Android
- **Procédure :** mêmes essais en A2DP (téléphone Android/iPhone appairé) puis avec une appli Android de musique si disponible.
- **Captures :** C15, C16.
- **Confirme :** quelles sources produisent une notification média. **Ne prouve pas :** les métadonnées complètes.

### A9 — GPS, réseau, Bluetooth, Wi-Fi
- **Où :** Réglages > Localisation ; une appli de navigation déjà installée (observer position et état « satellites » si affichés) ; Wi-Fi/4G.
- **Captures :** C17 (Localisation), C18 (état du GPS dans l'appli de nav.). Masquer la position.
- **Confirme :** GPS activé / recevant un fix. **Ne prouve pas :** la précision ni la vitesse GNSS (B).
- **Risque :** ciel dégagé nécessaire (garage = faux négatif).

### A10 — Launcher par défaut et accès spéciaux
- **Où :** Réglages > Applis > Applis par défaut > Appli d'accueil ; Réglages > Applis > Accès spécial (Accès aux notifications, Superposition, Usage).
- **Relever :** présence de l'option « Appli d'accueil » et choix possibles ; état des accès spéciaux.
- **Captures :** C19, C20.
- **Confirme :** possibilité (ou non) de choisir un autre launcher via l'interface standard. **Ne prouve pas :** que le firmware n'écrase pas ce choix au démarrage (observé ensuite avec le launcher réel).
- **Risque :** **ne pas changer** le launcher par défaut à ce stade.

### A11 — Débogage (lecture seule)
- **Où :** Options pour les développeurs si déjà actives.
- **Relever :** état « Débogage USB » ; ne rien activer.
- **Confirme :** si ADB est déjà ouvert. **Risque :** ADB ouvert expose l'appareil ; ne pas l'activer pour ce diagnostic.

---

## PARTIE B — Avec une application supplémentaire (accord explicite requis avant installation)

### B0 — Pré-conditions
1. Construire l'app `diagnostic/app/` (voir son README) **à partir de ce dépôt**, relire le code, puis seulement l'installer (clé USB/explorateur). Autre APK : non vérifié = interdit.
2. Installation via sideload : le firmware peut bloquer « sources inconnues » ; ne pas contourner par root.
3. Garder l'autoradio à l'arrêt, ciel dégagé pour le GPS.

### B1 — Rapport Fiesta Diag
- **Où :** l'app, boutons 1 à 3 ; le bouton 2 ouvre Réglages > Accès aux notifications (activer « Fiesta Diag ») ; lancer une musique CarPlay/BT/Android avant le bouton 3.
- **Relever :** fichier `diag-*.txt/.json` : `device`, `display`, `props`, `fytProbe`, `packages`, `gps`, `mediaSessions`, `permissions`, `hints`.
- **Captures :** C21 (écran de résultat), le fichier texte (copie USB), après relecture/anonymisation.
- **Confirme :**
  - `props`/`fytProbe` : famille FYT ou QF ou autre, présence de `com.syu.ms` / `com.qf.framework` et si leurs services sont **exportés et sans permission** (condition nécessaire, **non suffisante**, pour une intégration tierce) ;
  - `display` : résolution réelle, dp, densité ;
  - `gps` : satellites vus/utilisés, délai de fix ;
  - `mediaSessions` : quelles sources publient une session Android ;
  - `packages` : noms de package exacts de CarPlay, véhicule, lecteurs, launchers.
- **Ne prouve pas :** que des données CAN sont reçues ; `present=true` n'est pas un accès.
- **Risques/limites :** la lecture des packages exige `QUERY_ALL_PACKAGES` (sideload seulement) ; `getprop` peut être filtré selon SELinux (clés `null`) ; GPS sans fix en garage ; l'activation de l'accès aux notifications est un changement de réglage mineur et réversible (le désactiver ensuite).

### B2 — Outils tiers (facultatif, **jamais avant revue**)
- **FET / FYTCanbusMonitor / Dashwheel :** voir `docs/RECHERCHE_FYT_CAN.md` §5. Ne pas installer tant que B1 n'a pas montré une famille FYT avec service exporté. FET contient des fonctions root/scripts système : consultation seulement, sans lancer d'action. Toute lecture CAN réelle = étape distincte « Niveau 3 », avec analyse de risque écrite.

### B3 — Interprétation (décisions)

| Résultat B1 | Conclusion | Suite |
| --- | --- | --- |
| `com.syu.ms` absent, `com.qf.*` présent | Famille QF probable ; pas de FYTCanbusMonitor | Rechercher les services QF ; sinon rester sur Android/GPS/média |
| `com.syu.ms` présent, services non exportés ou protégés | Intégration tierce probablement impossible sans privilèges | Ne pas insister ; envisager OBD2 (décision d'achat distincte) |
| `com.syu.ms` présent, service exporté sans permission | Possibilité technique à évaluer | Niveau 3, lecture seule, après accord |
| Aucune famille reconnue | Firmware générique/Topway | Android/GPS/média uniquement |

---

## Ordre recommandé
A1 → A2 → A4 → A5/A6 → A7/A8 → A9 → A10 → (accord) B0 → B1 → décision B3. Durée estimée : 45 à 75 minutes à l'arrêt.
