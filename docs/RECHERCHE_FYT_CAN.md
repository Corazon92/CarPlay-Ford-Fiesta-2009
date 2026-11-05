# Recherche technique — Hizpo UIS7862, plateforme, CAN et `com.syu.ms`

**Date de la recherche : 05/11/2026.** L'autoradio n'est **pas disponible** : rien ici n'a été vérifié sur le matériel. Légende de fiabilité :

- **[LU]** lu directement par l'auteur de cette note dans une source citée (README, code, manuel) ;
- **[SNIPPET]** vu uniquement dans un extrait de moteur de recherche (page non lue) ;
- **[TÉMOIGNAGE]** affirmation d'un projet/forum communautaire, non vérifiée ;
- **[HYPOTHÈSE]** déduction de cette note, à tester.

## 1. Constat principal : « UIS7862 » ne veut pas dire « FYT »

UIS7862 (Unisoc) est une **puce** utilisée par plusieurs familles de firmware. Le projet communautaire Dashwheel (voir §6) distingue notamment :

| Famille | Exemples cités | Services/propriétés cités | Fiabilité |
| --- | --- | --- | --- |
| **QF** | « Hizpo / ROCO **K706** (QF001 UIS7862, QF005 UIS8581a) » | `com.qf.framework`, `ro.qf.platform`, `persist.sys.qf.launcher`, services binder `mcu_service`/`util_service` | [LU] dans le wiki, [TÉMOIGNAGE] sur le fond (le projet dit lui-même que c'est de la rétro-ingénierie) |
| **FYT / SYU** | Joying, Atoto, Mekede, Junsun, NaviFly… (UIS7862, UIS7870, UIS8581a) | `ro.fyt.*`, `ro.build.fytid`, package `com.syu.ms` (`app.ToolkitService`), `com.syu.canbus` | [TÉMOIGNAGE] |
| **Topway TS** | TS10 (UIS7862), Hikity, Podofo | `com.tw.car*`, `com.tw.carinfoservice` | [TÉMOIGNAGE] |

**Conséquence directe (hypothèse n°1) :** un « Hizpo UIS7862 » peut être de la famille **QF** et non FYT. Dans ce cas `com.syu.ms` **n'existerait pas** et la piste FYTCanbusMonitor/FET ne s'appliquerait pas. L'étude initiale du dépôt qualifiait FYT de « probable » : cette recherche ne le confirme pas et le rend **incertain**. Le diagnostic doit donc d'abord identifier la famille (propriétés système et packages), avant toute autre démarche.

## 2. Variantes du Hizpo « UIS7862 »

- **[SNIPPET]** Une page manuals.plus pour un Hizpo « 9.7 pouces série 7862 » (fiche AliExpress différente : `1005010351871469`) indique Ford Fiesta 2009–2017, Android 13, CarPlay/Android Auto sans fil, 4G, DSP. La page n'a pas pu être lue (contrôle anti-robot). Ce n'est **ni la fiche `1005007877212404`**, ni une preuve pour cet exemplaire.
- **[HYPOTHÈSE]** Les annonces AliExpress indiquent souvent « Android 13 » alors que des firmwares affichent une version supérieure à celle du noyau réel (Dashwheel : « Most of these units claim a newer Android than they run »). **Relever `ro.build.version.sdk`**, pas seulement l'étiquette « Android 13 ».
- Résolution (1280×720, 1024×600…), RAM/stockage, variante CAN : inconnues, à mesurer.
- Les variantes se distinguent par : famille de firmware, version MCU, boîtier CAN fourni, présence ou non de ZLink/TLink pour CarPlay.

## 3. Identification du MCU et du décodeur CAN

À relever **sans rien modifier** :

- Écran « À propos / Informations système » : modèle, version système, **version MCU** (souvent une chaîne du type `…-MCU-…`). Les entêtes de menu varient selon famille.
- Section « Réglages voiture / CAN » du firmware : marque de décodeur (Raise, Hiworld, Simple Soft, etc.) et modèle de véhicule choisi. **[SNIPPET]** Les manuels de décodeurs (ex. Hiworld) placent ce réglage dans un menu « Réglages usine » protégé par un code ; plusieurs sites listent des codes courants (3368, 1617, 8888…) mais ce sont des pages SEO/forum **non fiables** : **ne pas tester de codes**. Ce menu permet de **modifier** le profil CAN, ce que le projet interdit : on ne regarde que l'écran en consultation s'il est accessible sans code, sans toucher aux sélecteurs.
- Le décodeur peut être **externe (boîtier entre faisceau et radio)** ou intégré ; le câblage visible derrière la radio le dira (hors périmètre tant que la radio n'est pas démontée — éviter).
- Rappel d'un point de la fiche du dépôt : les commandes au volant fonctionnelles peuvent venir d'un fil analogique (résistif) et **ne prouvent pas** la présence de CAN.

## 4. Service `com.syu.ms` (famille FYT uniquement) — état des connaissances

- **[LU]** FET (FYT Extra Tool, hvdwolf, MIT) embarque `FytCanBusMonitor.kt`, `IPCConnection.kt` et crédite la bibliothèque **FYTCanbusMonitor d'AxesOfEvil** ; ces classes se connectent via `MsToolkitConnection` à un « toolkit » distant (`IRemoteToolkit.getRemoteModule(moduleId)`) avec enregistrement de callbacks. C'est la preuve qu'une **app tierce a pu**, sur certains firmwares FYT, se lier au service CAN et recevoir des valeurs.
- **[TÉMOIGNAGE]** Dashwheel indique lier `com.syu.ms/app.ToolkitService` et lire, sur FYT, les modules (module 7 : vitesse code 1031, régime 1032 « non confirmés » ; module 2 : Bluetooth ; module 0 : touches). Ces codes sont **par véhicule/profil** (les codes cités viennent d'un VW Sharan, pas d'une Fiesta). Le projet précise « waiting for testers ».
- **[LU]** Aucune documentation éditeur FYT publique du service n'a été trouvée : l'interface est **non officielle, non garantie, spécifique au firmware**.
- **[HYPOTHÈSE]** Sur Fiesta 2009 (profil Ford du décodeur), les données exposées seraient limitées (portières, marche arrière, frein à main, éventuellement vitesse/régime). À tester.
- **Sécurité :** le même service permet aussi des **écritures** (commandes). Le projet n'en implémente aucune ; tout futur lecteur CAN restera limité à l'enregistrement de callbacks de lecture.

## 5. Outils compatibles

| Outil | Rôle | Remarques | Fiabilité |
| --- | --- | --- | --- |
| **FET – FYT Extra Tool** (hvdwolf) | Utilitaire communautaire pour unités FYT (7862/8581/SC9863) ; installation par sideload ; dernière version vue 1.3.3 (avr. 2024) | **[LU]** Utilise `libsu` et des scripts (`7862lsec.sh`…) : plusieurs fonctions sont orientées **root/modification système** → à ne **pas** lancer sans revue. Ne l'utiliser, au mieux, que pour consulter son éventuel écran de moniteur CAN, et seulement après accord explicite | Code source public, licence MIT |
| **FYTCanbusMonitor** (AxesOfEvil) | Bibliothèque/app de moniteur CAN FYT | Dépôt non retrouvé sous l'URL tentée ; à localiser avant tout usage | [TÉMOIGNAGE] |
| **Dashwheel** (launcher communautaire) | Lit CAN FYT/QF si le firmware le permet ; « Signal Finder » apprend les signaux | **[LU]** Projet récent et très orienté IA, plusieurs fonctions « non confirmées ». Précédent utile pour la faisabilité, pas une source de vérité ; pas à installer par défaut | [TÉMOIGNAGE] |
| **Appli CAN d'origine** (`com.syu.canbus` FYT, CarInfo/QF…) | Écrans véhicule du firmware | L'**écran d'origine est la meilleure preuve** de ce que le décodeur sait fournir | [HYPOTHÈSE] |

## 6. Comment savoir si le CAN est accessible à une app tierce (méthode)

Les quatre niveaux à ne pas confondre :

1. **Le véhicule émet** sur ses réseaux (HS/MS-CAN) — acquis sur une Fiesta, non démontré ici.
2. **Le décodeur CAN le comprend** pour le profil choisi — visible dans l'app véhicule d'origine (portières, etc.).
3. **Le MCU/firmware le publie** à Android — visible par la présence/exportation d'un service (`com.syu.ms`, `com.qf.framework`) et par des écrans système.
4. **Une app tierce a le droit de s'y lier** — seule preuve : un test réel en lecture seule (service exporté sans permission signature, callbacks reçus).

Le diagnostic en `diagnostic/CHECKLIST.md` établit les niveaux 2 et 3 sans rien modifier ; le niveau 4 est volontairement différé (Niveau 3 du dépôt) et réclame un accord distinct.

## 7. Limites de cette recherche

- Pages manuals.plus et AliExpress : non lues (anti-robot). Le dépôt signalait déjà ces liens comme inaccessibles.
- Recherche web généraliste : nombreux résultats SEO inutilisables, écartés.
- Aucune source ne documente le profil CAN **Ford Fiesta 2009** sur cette plateforme. Ne pas en déduire qu'il est ou n'est pas supporté.
- Non consulté : fil XDA FET (`xdaforums.com/t/fet-fyt-extra-tool.4653315/`), dépôt `cssbruno/honda-customizer` (audit `com.syu.ms`), `open-headunit`. À lire par une prochaine IA.

## Sources

- FET (README et code) : https://github.com/hvdwolf/FET — fil XDA : https://xdaforums.com/t/fet-fyt-extra-tool.4653315/
- Dashwheel, familles de firmware : https://github.com/deviloufr-ai/Dashwheel/wiki/Supported-Head-Units ; pages « Car data » du wiki ; `docs/unit-platforms.md`
- Manuel Hizpo 7862 (extrait de recherche) : https://manuals.plus/ae/1005010351871469
- Honda Customizer (audit `com.syu.ms`) : https://github.com/cssbruno/honda-customizer
