# Étude technique et faisabilité — synthèse

**Source de départ :** document fourni par le propriétaire, *Launcher Fiesta – étude technique et faisabilité*, 10 pages, daté du **05 novembre 2026**. Ce Markdown reformule les constats utiles pour le développement ; les sources du document d'origine ne sont **pas toutes vérifiées indépendamment**. Certains liens du vendeur et de manuals.plus étaient signalés inaccessibles lors de la rédaction.

## 1. Matériel

- **Voiture :** Ford Fiesta européenne de 2009, Mk7 / JA8 ; motorisation à préciser.
- **Autoradio :** Hizpo annoncé 9 pouces UIS7862, fiche n° `1005007877212404`.
- **Plateforme FYT :** **probable**, pas démontrée sur cet exemplaire.
- **Boîtier CAN :** annoncé dans le manuel d'un modèle voisin ; marque exacte (Raise, Hiworld, Simple Soft, etc.) inconnue.
- **Android, MCU, variante et résolution :** à relever. Éviter d'affirmer Android 13 ou 1280×720 sans mesure.
- **Commandes au volant :** fonctionnelles/configurées d'après l'utilisateur ; mode de connexion CAN ou analogique indéterminé.

## 2. CAN Bus : chaîne et limites

```text
Fiesta (réseau véhicule)
  → décodeur CAN (si installé / bon profil)
  → MCU de l'autoradio
  → service Android FYT potentiel (com.syu.ms)
  → application système d'infos véhicule
  → [accès tiers non officiel à valider] launcher personnel
```

La documentation communautaire rapportée dans l'étude mentionne des affichages possibles de **portières ouvertes**, **kilométrage**, **marche arrière** ou **frein à main** selon la Fiesta, le décodeur et le profil. **Aucune de ces données n'est confirmée sur ce Hizpo.** Les commandes au volant peuvent aussi être analogiques.

Une interface FYT `com.syu.ms`, l'application `com.syu.canbus` et des utilitaires communautaires tels que **FYTCanbusMonitor** / **FYT Extra Tool (FET)** sont des **pistes de recherche**. Interface non publique, potentiellement instable et à tester sans root ni écriture. La possibilité d'envoyer des commandes implique d'exclure tout chemin d'écriture du code du projet.

## 3. OBD2 : données souhaitées

| Donnée | Voie envisagée | État sur cette Fiesta |
| --- | --- | --- |
| Régime moteur | PID OBD2 standard | Plausible, non testé |
| Température liquide de refroidissement | PID OBD2 standard | Plausible, non testé |
| Vitesse | GPS Android déjà disponible, OBD2 optionnel | GPS à tester |
| Tension | Lecture adaptateur OBD2 / éventuellement CAN | Non testée |
| Carburant % | PID parfois non pris en charge | Incertain |
| Consommation instantanée | Calcul selon PIDs et motorisation | Incertaine |
| Consommation moyenne | Estimation à partir d'un trajet | Incertaine |
| Autonomie restante | Exige un carburant fiable et un modèle de calcul | Incertaine |
| Codes défaut moteur | Diagnostic OBD2, lecture seule | Plausible avec adaptateur |
| Pression turbo | Selon motorisation / PIDs | Motorisation inconnue |
| Portières | CAN carrosserie, pas un PID moteur standard | Ne pas attendre d'ELM327 générique |

**Décision actuelle :** ne pas acheter d'adaptateur avant les diagnostics CAN. Si OBD2 est nécessaire, étudier une interface compatible HS/MS-CAN plutôt qu'un ELM327 générique, en confirmant logiciel et compatibilité.

## 4. Médias et navigation

| Fonction | Source | Statut |
| --- | --- | --- |
| Heure et date | Android | Approche standard |
| Météo | Service réseau + localisation autorisée | Faisable sous conditions |
| Position et vitesse | GPS intégré | Faisable si antenne et autorisations correctes |
| État téléphone Bluetooth | API Android / intégration système | À valider sur le firmware |
| Lancer CarPlay | Package de l'application réceptrice à identifier | Faisable selon restrictions Android |
| Spotify / Deezer / YouTube Music installés | Sessions médias Android | Faisable si session exposée / permission |
| Bluetooth FYT | Lecteur système éventuellement non standard | À tester |
| CarPlay (ZLink/TLink ou autre) | Session média éventuellement absente | À tester |
| Radio FM système | Intégration FYT propriétaire | À tester |
| Guidage Android | Notifications / API du GPS installé | À tester |
| Guidage sur l'iPhone via CarPlay | Pas d'API Android générale disponible | Ne pas promettre |

**Règle :** si aucun titre ni session Android exploitable, afficher « Lecture via CarPlay » et un bouton d'ouverture, sans pochette ou commande inventée. Ne pas promettre l'ETA/prochain virage d'un trajet CarPlay.

## 5. Tableau de priorisation

**Prototype prioritaire :** heure/date, raccourcis applicatifs, GPS, météo, Bluetooth et média Android, sous réserve des tests de permissions/firmware.

**Expérimentation :** notifications de guidage, média Bluetooth/CarPlay, états éventuels de portières et autres messages CAN via FYT.

**Après validation / matériel :** température moteur, régime, diagnostics OBD2, tension, consommation.

**Reporter ou exclure faute de preuve :** climatisation, carburant et autonomie fiables, intégration complète des données véhicule.

## 6. Contraintes et avertissements

- La présence d'un décodeur CAN ne signifie ni accès au CAN brut ni disponibilité d'une API publique.
- Le service FYT est spécifique au firmware et ses interfaces ne sont pas garanties.
- L'API Android peut nécessiter des permissions ou être restreinte par la version d'Android installée.
- Le modèle 3D doit être une représentation fidèle **et sous licence appropriée**.
- Source « Démo » strictement séparée ; ne jamais faire croire que des valeurs fictives sont réelles.
- Pour les tests du matériel, voir `DIAGNOSTIC_AUTORADIO.md`.

## 7. Références citées dans l'étude initiale — à vérifier

- Manuel Hizpo série 7862 pour Fiesta 2009–2017 (modèle voisin, non équivalent garanti).
- Manuel décodeur Raise pour Ford.
- Discussions XDA FYT UIS7862, launchers FYT, CAN data logger et FET.
- Références communautaires sur commandes Fiesta et interfaces Ford.
- Forum Fiesta ST sur OBD2, HS/MS-CAN et PIDs Ford.
- Documentation AGAMA launcher sur sessions médias.
- Projet `wheelplay`, discussions sur la publication de sessions médias.

**Vérification indépendante nécessaire avant d'implémenter une intégration propriétaire.** Pour les détails et les sources originales, se référer au PDF de l'étude remis avec le projet.
