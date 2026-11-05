# REPRISE DU PROJET — à lire en premier

**Dernière consolidation : 05/11/2026.** Ce fichier doit permettre de changer de PC, de compte, de conversation ou d'IA **sans perdre le contexte**. L'état de GitHub prévaut sur les souvenirs de chat.

## Résumé exécutif

Le propriétaire conçoit un **launcher Android** pour un **autoradio Hizpo 9 pouces annoncé UIS7862**, monté sur une **Ford Fiesta 2009 (Mk7/JA8)**. Il **ne modifie pas Apple CarPlay** : un raccourci du launcher doit ouvrir l'application réceptrice CarPlay de l'autoradio.

Le **concept B — Cockpit futuriste** a été préféré aux versions A et C. Une révision **B2** a été préparée avec **thèmes jour/nuit**, écrans d'état **MVP réel** et **Démo**, ainsi que l'étude d'une voiture 3D réactive. **La préférence initiale du propriétaire reste le rendu central 3D réaliste vert émeraude du mockup premium** ; le dessin plat de Fiesta visible dans les maquettes B2 est seulement un placeholder. **Conserver la structure B2 ET viser le rendu 3D premium**, sans revenir à l'illustration plate comme résultat final.

### Design retenu et interfaces

- Haut : heure, date, météo et connexions + commutateur Jour/Nuit/Auto.
- Gauche : navigation locale, Domicile, Travail et dernière destination.
- Centre : Fiesta 2009 verte réaliste en 3/4, orientation à l'arrêt seulement, ombre et éclairage adaptés au thème.
- Droite : musique, téléphone connecté, GPS/état véhicule et diagnostic.
- Bas : CarPlay (action principale), Musique, Navigation, Téléphone, Réglages, Applis.
- Pas de valeurs inventées : les images « Démo » portent un badge explicite ; les données CAN et OBD2 restent non validées.

### Références conceptuelles

- Canvas Claude original : https://claude.ai/artifact/LeDiQ9obvtUCRjw3mNsUGq
- Images de travail partagées dans la conversation (à archiver comme **fichiers binaires** dans `design/reference/` quand un outil d'upload le permet) :
  - `cockpit_automobile_connecté_vert_émeraude.png` — **rendu premium initial à conserver comme cible visuelle de la voiture et du raffinement général**.
  - `B2 — Nuit · états réels MVP@1x.png` — B2 nuit, état par défaut honnête.
  - `B2 — Jour · états réels MVP@1x.png` — B2 jour.
  - `B2 — Nuit · démonstration (données simulées)@1x.png` — B2 démo, valeurs fictives identifiées.
- **État des médias :** les fichiers PNG/PDF ne sont pas encore présents dans ce dépôt ; leurs noms, intentions et documents associés sont consignés ici. Ne pas prétendre que les images sont archivées sur GitHub tant que ce n'est pas vérifié.

## État GitHub

- **Branche `main` :** README, AGENTS et études de faisabilité et de design B2.
- **PR diagnostic ouverte :** [PR #1 — diagnostic Hizpo / Fiesta Diag](https://github.com/Corazon92/CarPlay-Ford-Fiesta-2009/pull/1), branche `feat/diagnostic-autoradio`. 19 fichiers, dont checklist, recherche FYT/QF et prototype Kotlin **non compilé / non testé**.
- **Ne pas supposer que la PR #1 est fusionnée** ; vérifier son état réel avant toute intervention.
- Ne pas installer le prototype Fiesta Diag tant qu'il n'a pas été compilé et revu.
- Une PR ne vaut ni validation technique ni test sur l'autoradio.

## Important : recherche technique de Claude

Claude a distingué plusieurs familles de firmwares UIS7862. **Hizpo UIS7862 n'implique pas forcément FYT** ; une plateforme **QF** est également possible. Par conséquent, l'interface `com.syu.ms` ne peut pas être présumée disponible.

État matériel :
- commandes au volant configurées et fonctionnelles selon le propriétaire ; CAN vs analogique non établi ;
- marque de boîtier CAN et profil choisis non relevés ;
- résolution effective inconnue (1280×720 seulement *résolution de maquette*) ;
- version Android, MCU, firmware et application CarPlay inconnus ;
- motorisation essence/diesel inconnue ;
- aucun test réel d'état portières, médias CarPlay, CAN ni OBD2 ;
- pas d'achat OBD2 avant diagnostic.

## Décisions de produit et de sécurité

1. Séparer UI et sources : Android, GPS, média, météo, CAN propriétaire si prouvé, OBD2 si équipement ajouté.
2. Chacune des valeurs expose source + statut + fraîcheur + raison d'indisponibilité.
3. Aucun flash, root, écriture CAN/OBD2, effacement de défauts ni changement de profil.
4. À l'arrêt uniquement pour les tests et la rotation du modèle 3D ; en mouvement, éviter toute animation / interaction distrayante.
5. La voiture ne doit pas apparaître « portes fermées » si les données ne sont pas disponibles.
6. Jour/Nuit/Auto, avec mode nuit premium et mode jour lisible ; pas de dépendance aux phares tant que l'accès n'est pas prouvé.
7. Modèle 3D fidèle et licencié à obtenir ; proportions 2009 phase 1 et nombre de portes à confirmer.
8. Ne jamais enregistrer traces GPS précises, VIN, IMEI, coordonnées personnelles ni secrets dans GitHub public.

## Prochain travail recommandé

**A.** Faire une revue de la PR #1, corriger les éventuels problèmes de compilation, puis valider localement la construction du prototype Android sur PC **sans l'installer sur le véhicule** ; ne pas fusionner comme « testé » sans preuve.

**B.** Mettre les PNG B2 et la référence premium dans `design/reference/` via un environnement ayant l'accès binaire à GitHub ; vérifier leur affichage. Ajouter si voulu le PDF de faisabilité initial dans `docs/reference/`.

**C.** Mettre en place un projet source launcher Android minimal (Kotlin) respectant la structure B2. En première itération : heure/date, thèmes, menus et lancement des applications, avec placeholders explicites. Ne pas se bloquer sur le CAN/OBD2.

**D.** Lorsque l'autoradio sera disponible : effectuer les tests de `diagnostic/CHECKLIST.md` (après fusion ou sur branche PR), puis intégrer les résultats anonymisés.

**E.** Remplacer l'illustration plate par une représentation réaliste compatible avec les performances de l'appareil et sa licence.

## Prompt de reprise pour n'importe quelle IA

> Ouvre le dépôt `Corazon92/CarPlay-Ford-Fiesta-2009`. Lis `REPRISE_PROJET.md`, `AGENTS.md`, `README.md` et `docs/`. Examine également la PR #1 sans supposer qu'elle est fusionnée. Le design retenu est B2, mais la cible de la voiture 3D est le mockup vert émeraude réaliste. Récapitule confirmé vs à tester, propose les prochaines modifications sûres et travaille dans une branche/PR sans écraser les documents. Ne revendique pas une compilation, un test physique ou une disponibilité de données CAN tant que tu n'as pas de preuve.
