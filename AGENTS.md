# Consignes pour tous les assistants IA — Fiesta Cockpit

Ce fichier est le point d'entrée commun pour **ChatGPT, Claude, Copilot, Cursor, Codex et autres outils**. Lire `README.md` et `docs/` avant d'agir. Ne jamais considérer le contexte d'une conversation passée comme présent si cette conversation n'est pas dans le dépôt.

## Mission actuelle

Projet au stade **cadrage et diagnostic**, pas en développement ou en production. Concept visuel retenu : **B — Cockpit futuriste** pour un autoradio **Android Hizpo sur Ford Fiesta 2009**. Cible = **launcher Android** ; **pas de réécriture d'Apple CarPlay**.

## Protocole obligatoire

1. Inspecter le dépôt et les documents, préserver les fichiers présents.
2. Énoncer les hypothèses ; ne pas transformer une promesse vendeur ou un témoignage de forum en capacité vérifiée.
3. Si une information dépend de l'appareil (version MCU, CAN box, applications natives, résolution, motorisation), demander **une preuve de diagnostic**, ou marquer **À tester**.
4. Avant de coder, proposer une étape limitée, exécutable et réversible.
5. Après changement, mettre à jour documentation, état d'avancement, procédure de vérification et limitations.
6. Ne pas produire de chiffres fictifs dans les vues réelles. Pour une démonstration, utiliser un fournisseur distinct et un badge **Démo** explicite.
7. Respecter la sécurité : aucune écriture CAN/OBD, aucun effacement DTC, aucun flash/root/changement de profil CAN sans demande distincte et analyse de risque.
8. Éviter toute manipulation quand le véhicule roule. Les tests sur l'autoradio et la voiture se font stationnés en sécurité.
9. Ne jamais publier secrets, VIN, position précise, rapports de diagnostics non expurgés, identifiants Bluetooth ou données personnelles.
10. Vérifier dépendances, API Android, licences de modèles 3D et compatibilité réelle avant ajout.

## Contrat des données

Chaque indicateur doit porter au minimum :
- `source` : Android / GPS / média / CAN FYT / OBD2 / démo ;
- `status` : `available` / `pending` / `unavailable` / `demo` ;
- `timestamp` de la dernière donnée valide quand disponible ;
- `reason` si indisponible et une indication du caractère estimé si calculé.

Ne pas déduire « portière ouverte », « carburant restant », « autonomie » ni « consommation » d'un simple appareil compatible CAN. Si une session média Android n'existe pas, proposer d'ouvrir CarPlay plutôt que simuler artiste/pochette.

## Direction technique envisagée, NON actée

- Application Android native en Kotlin avec architecture modulaire.
- UI lisant un état unifié et **adapters indépendants** (Android, GPS, médias, CAN FYT, OBD2).
- Adaptateurs CAN/OBD désactivés tant que leurs accès réels ne sont pas testés.
- Rendu Fiesta glTF via moteur léger (Filament envisagé) ; image de substitution tant que modèle légal et fidèle non obtenu.
- Adapter les densités/taille des zones tactiles à la résolution **réellement mesurée**.

## Ordre de travail

1. Diagnostic de niveau 1 / 2 : `docs/DIAGNOSTIC_AUTORADIO.md`.
2. Consolider le tableau de faisabilité.
3. Affiner le concept B et les états indisponibles.
4. POC Android pour API fiables, sans CAN/OBD.
5. Uniquement après preuve de lisibilité : expérimentation CAN FYT **strictement en lecture seule**.
6. OBD2 uniquement après décision explicite d'achat.
7. Finaliser intégration 3D et performances.

## Pour transmettre le travail à une autre IA

Lui demander : « Lis README.md, AGENTS.md et docs/, résume ce qui est confirmé vs à tester, indique les trois prochaines étapes sûres, puis propose les fichiers à modifier avant d'écrire du code. »

Documenter toute nouvelle décision dans le dépôt ; éviter les conventions implicites enfermées dans un chat.
