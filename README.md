# Fiesta Cockpit — launcher Android pour Ford Fiesta 2009

> **Statut : cadrage / faisabilité — aucune application déployée.**  
> Projet personnel d'écran d'accueil Android pour un autoradio Hizpo, **pas une modification de l'interface Apple CarPlay**.

## Vision

Créer un launcher automobile premium, rapide et réellement utilisable sur une **Ford Fiesta européenne de 2009 (génération Mk7 / JA8)**, avec une visualisation 3D verte du véhicule et un accès immédiat à CarPlay, à la navigation, à la musique et aux applications Android.

**Concept retenu : B — Cockpit futuriste**, distinct des concepts A (minimaliste premium) et C (sport & luxe).

### Design cible

- Mode sombre ; accents vert émeraude, lisibilité élevée, grandes surfaces tactiles.
- **Trois zones :** gauche = navigation / météo / destinations ; centre = Fiesta verte en vue trois quarts ; droite = média / téléphone / état véhicule.
- Bandeau supérieur : heure, date, météo et connexions.
- Dock inférieur à six entrées : **CarPlay**, Musique, Navigation, Téléphone, Réglages, Applications.
- Effets d'animation discrets ; aucune rotation interactive de la voiture pendant la conduite.
- Aucune valeur fictive présentée comme un relevé réel.

## Matériel à valider

| Élément | État |
| --- | --- |
| Voiture | Ford Fiesta 2009, génération Mk7/JA8, couleur verte |
| Motorisation | **Non renseignée** |
| Autoradio | Hizpo 9 pouces, annonce AliExpress `1005007877212404` |
| SoC | UIS7862 **annoncé**, à relever sur l'appareil |
| Android / MCU / RAM | **À relever** |
| Résolution | 1280×720 ou 1024×600 **hypothèses**, à mesurer |
| CAN Bus | Commandes au volant configurées ; **décodeur et profil à identifier** |
| CarPlay | Application réceptrice et package Android à identifier |
| Adaptateur OBD2 | **Non confirmé / ne rien acheter avant les tests** |

Fiche vendeur : https://fr.aliexpress.com/item/1005007877212404.html

**Attention :** les commandes au volant ne démontrent pas à elles seules que les autres signaux véhicule sont exposés à Android. Ne pas confondre données présentes sur les réseaux du véhicule, données décodées par le MCU, et données lisibles par une application tierce.

## Ce qui est réalisable en priorité

L'objectif du premier prototype est limité aux API Android documentées : heure/date, état de connexion accessible, lancement d'applications (dont le récepteur CarPlay), GPS si autorisé, météo via service réseau, et contrôles média **uniquement lorsque la source publie une session Android**.

**À investiguer avant intégration :** notifications de guidage, métadonnées Bluetooth/CarPlay, service FYT `com.syu.ms`, informations CAN (portières, kilométrage, marche arrière). **À reporter :** relevés moteur via OBD2, consommation, carburant, autonomie, diagnostics.

Une carte sans source valide affiche **indisponible / en attente**, jamais une valeur inventée.

## Organisation du dépôt

- [AGENTS.md](AGENTS.md) — règles de collaboration compatibles avec plusieurs assistants IA.
- [docs/ETUDE_TECHNIQUE.md](docs/ETUDE_TECHNIQUE.md) — synthèse traçable de l'étude technique du 05/11/2026.
- [docs/DIAGNOSTIC_AUTORADIO.md](docs/DIAGNOSTIC_AUTORADIO.md) — contrôles en lecture seule à effectuer sur le Hizpo.
- [docs/DESIGN_ET_ARCHITECTURE.md](docs/DESIGN_ET_ARCHITECTURE.md) — spécification fonctionnelle du concept B et architecture envisagée.

Le document de référence initial est intitulé **« Launcher Fiesta – étude technique et faisabilité »** (10 pages, 05/11/2026). Les notes ci-dessus en restituent les conclusions ; leurs hypothèses **ne sont pas** des validations matérielles sur le véhicule.

## Méthode de reprise — humain ou IA

1. Lire ce README, puis **AGENTS.md** et les trois documents de `docs/`.
2. Vérifier l'arborescence actuelle et les changements Git avant toute modification.
3. Consulter les derniers résultats de diagnostic et distinguer **confirmé / plausible / à tester / indisponible**.
4. Proposer une modification ciblée ; documenter décisions et incertitudes.
5. Développer le launcher seulement après validation du périmètre et des informations matérielles manquantes.

## Principes non négociables

- **Lecture seule** pour CAN/OBD2 ; aucune commande, injection de trames, écriture véhicule ou effacement de défauts.
- Ne pas rooter, flasher le MCU, modifier un profil CAN ou installer un APK non vérifié sans accord explicite.
- Ne pas promettre l'accès aux données de guidage/musique CarPlay ; le récepteur CarPlay peut ne rien publier aux API Android.
- Sécurité conduite : priorité à la lisibilité, à l'accès en un geste et à la stabilité.
- Aucun token, mot de passe, donnée personnelle, trace GPS précise ou fichier de diagnostic sensible dans Git.
- Ne jamais attribuer à un modèle 3D généré une fidélité technique sans validation des références et des droits de licence.

## État d'avancement

- [x] Direction visuelle choisie : concept B
- [x] Étude de faisabilité initiale et limites documentées
- [x] Dépôt initialisé pour collaboration inter-IA
- [ ] Relevé Android / MCU / résolution / décodeur CAN
- [ ] Identification de l'application CarPlay et tests des sessions média
- [ ] Tests des informations véhicule réellement visibles
- [ ] Maquette B mise à jour avec états de données réels
- [ ] Prototype launcher Android
- [ ] Recherche et validation d'un modèle 3D Fiesta utilisable
- [ ] Décision sur un éventuel adaptateur OBD2

**Dernière mise à jour : 05 novembre 2026.**
