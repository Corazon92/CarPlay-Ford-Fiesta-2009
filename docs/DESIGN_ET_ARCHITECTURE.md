# Concept B — design et architecture cible

**Statut : proposition, pas implémentation validée.** Référence visuelle : concept B choisi parmi trois propositions du 05/11/2026. Le mockup comporte des données de démonstration, **pas des relevés de la voiture**.

## Révision B2 (05/11/2026)

**Statut : proposition de design, à valider.** B2 affine le concept B sans le remplacer. Maquettes : artboards « B2 — Nuit », « B2 — Jour » et « B2 — Démo » du canvas de design. Détails : [THEMES_JOUR_NUIT.md](THEMES_JOUR_NUIT.md) et [VEHICULE_3D_REACTIF.md](VEHICULE_3D_REACTIF.md).

L'image de référence du concept B (mockup généré) contient des valeurs de démonstration présentées comme réelles : guidage, titre musical, « OBD2 Connecté » avec régime et température, adresse de domicile. B2 garde son style et remplace ces valeurs par les vrais états du MVP ; la version complète n'existe que dans l'artboard Démo, badgée.

### Disposition (1280 × 720 de référence, à recaler sur la résolution mesurée)

| Zone | Largeur | Contenu B2 |
| --- | --- | --- |
| Barre du haut | 100 %, 52 px | Heure (32 px), date en capitales espacées, météo, icônes Bluetooth / réseau / GPS, sélecteur de thème « Auto » ; centre réservé au badge Démo ou à une notification utile |
| Colonne gauche | 330 px | Carte Navigation (flexible) + carte Raccourcis : Domicile, Travail, Dernière destination |
| Centre | flexible | Fiesta verte 3/4, libellé « FORD FIESTA · MK7 », sélecteur de vue (3 vues), état « Stationné · 0 km/h (GPS) » |
| Colonne droite | 330 px | Musique (236 px), Téléphone (80 px), Véhicule (reste) |
| Dock | 100 %, 76 px | CarPlay (1,4 × plus large, sous-libellé d'état), Musique, Navigation, Téléphone, Réglages, Applis |

### Cartes et états

| Carte | État MVP réel | État enrichi (seulement si la source est validée) |
| --- | --- | --- |
| Navigation | « Aucun guidage » + « Où allez-vous ? » + bouton Ouvrir la navigation | Prochain virage, rue, arrivée, durée, distance, progression — si l'appli GPS Android le partage |
| Raccourcis | Domicile / Travail « Adresse à définir », dernière destination vide | Temps de trajet estimé si l'appli GPS le fournit |
| Musique | « Lecture via CarPlay » + « Titre non partagé par le récepteur » + bouton Ouvrir CarPlay | Pochette, titre, artiste, progression, commandes — si une session média Android existe |
| Téléphone | Nom de l'appareil + « Connecté en Bluetooth » | Batterie du téléphone si exposée (à tester) |
| Véhicule | Vitesse GPS ; « Moteur : — (OBD2 requis) », « Portières : — (CAN à tester) » ; bouton Voir le diagnostic | Régime, température, tension (OBD2) ; portières (CAN) |
| Voiture | Neutre, rotation à l'arrêt | Zones surlignées selon les états validés |

### Améliorations par rapport à B

- Météo déplacée dans la barre du haut : la colonne gauche devient 100 % navigation et contexte.
- Puces d'état homogènes : active (vert), neutre (gris), démo (ambre). Une seule couleur d'alerte (ambre), jamais de rouge décoratif.
- Chaque carte a un état vide utile (action proposée) au lieu d'un tiret seul.
- CarPlay élargi dans le dock et doublé dans la carte Musique : c'est l'action la plus fréquente.
- Chiffres en chasse fixe pour éviter que les valeurs « sautent ».
- Zones tactiles ≥ 48 px partout, ≥ 76 px dans le dock.
- Sélecteur de thème visible, mode Auto par défaut.

### Interactions

- Un appui : ouvrir une appli ou une destination. Pas de geste caché indispensable.
- Retour visuel immédiat (fond accent doux 100 ms), sans rebond ni effet élastique.
- Rotation et changement de vue de la voiture uniquement à l'arrêt ; sinon boutons grisés avec infobulle « Disponible à l'arrêt ».
- Pas de défilement dans l'écran d'accueil.
- Badge Démo dans la barre du haut **et** sur chaque carte simulée.

### Recommandation UX

**Dès le MVP :** barre du haut complète, thèmes jour / nuit (manuel + auto solaire), raccourcis dock, Domicile / Travail, état Bluetooth, vitesse GPS, carte Musique avec repli CarPlay, voiture statique avec rotation à l'arrêt, page Diagnostic des données, mode Démo séparé.

**Plus tard, après preuve :** guidage depuis l'appli GPS Android, métadonnées Bluetooth / CarPlay, portières et coffre (CAN), phares pour le thème, cartes OBD2, surlignage d'alerte moteur, temps de trajet des raccourcis.

**À éviter :** clignotants, warnings et verrouillage sur la 3D, compte-tours animé, consommation et autonomie estimées sans PID fiable, animations en roulant, notifications sonores du launcher, tout logo ou adresse personnelle dans le dépôt public.

## Composition landscape 9 pouces

1. **Top bar** : heure, date, météo, Bluetooth et connexion. Éviter les badges « CarPlay connecté » si l'état réel n'est pas détectable.
2. **Colonne gauche** : destination récente, raccourcis Domicile/Travail, météo et navigation Android. Ne montrer le guidage détaillé que s'il est exposé par l'application GPS locale.
3. **Zone centrale** : Ford Fiesta 2009 verte, représentation trois quarts sur fond sombre. Rotation au doigt uniquement à l'arrêt **et** si l'immobilité est établie de manière fiable ; sinon désactivée. Pas d'animation continue en roulant.
4. **Colonne droite** : musique (si session média exploitable), connexion du téléphone, carte contextuelle GPS (vitesse/altitude) en attendant preuve de données CAN/OBD2.
5. **Dock** : CarPlay (action principale), Musique, Navigation, Téléphone, Réglages, Applications. Zones larges, retour visuel immédiat.

## Direction artistique

- Noir très sombre / graphite ; vert émeraude utilisé pour états actifs et actions.
- Typographie sans-serif lisible ; hiérarchie nette, contraste jour/nuit.
- Cartes sobres à coins arrondis, bordures subtiles ; peu d'effets lumineux.
- Disposition responsive pour résolutions mesurées, pas seulement un mockup fixe 1280×720.
- Aucun élément visuel ne doit masquer une information de conduite ou une fonction système de sécurité.

## États d'information obligatoires

| État | Présentation |
| --- | --- |
| Disponible | Valeur et unité, source identifiable au besoin |
| En attente | Tiret neutre / chargement discret |
| Indisponible | Tiret + cause courte, par ex. « Nécessite OBD2 » |
| Démonstration | Valeur de test + badge « Démo » bien visible |
| Périmé (proposition additionnelle) | Dernière valeur horodatée, signalée comme ancienne |

Les cartes véhicule ne sont visibles qu'avec données vérifiées ; sinon proposer une information alternative fiable ou un lien « Diagnostic ».

## Architecture modulaire envisagée

```text
              Android launcher UI (Kotlin / Compose à évaluer)
                          |
             État unifié / ViewModels
                          |
   ----------------------------------------------------
   |          |         |         |          |         |
 Android     GPS     Media    Weather     CAN FYT     OBD2
 services    API     session   network   [à tester] [optionnel]
                                         lecture seule
```

- UI indépendante du matériel ; chaque adaptateur indique source, fraîcheur, état et erreur.
- Les fournisseurs CAN/OBD2 ne sont **pas activés** dans le MVP.
- Mode de démonstration séparé, activé explicitement et toujours signalé.
- Évaluer Filament + glTF pour la 3D une fois le modèle/licence validés ; prévoir un rendu statique de repli.
- Réduire charge CPU/GPU ; arrêter animations / rendu superflu quand voiture en mouvement.
- Ne pas dépendre de la communication inter-application CarPlay pour le fonctionnement essentiel.

## Parcours MVP proposé

1. Relever appareil et boîtier CAN ; compléter la checklist.
2. Valider le launcher Android et l'ouverture de l'application CarPlay sur l'appareil.
3. Tester GPS, notifications média, permissions, performances et reprise après redémarrage.
4. Réaliser les composants visuels du concept B avec vrais états et placeholders identifiés.
5. Tester stabilité et ergonomie à l'arrêt, puis sécurité d'affichage sans interaction au volant.
6. Étudier CAN en lecture seule ; considérer OBD2 ensuite.
7. Intégrer le modèle 3D sous licence après optimisation.

## Questions ouvertes

- Quelle variante exacte Hizpo et quelle résolution effective ?
- Marque du CAN box et profil actif ?
- Quelle motorisation Fiesta et quels capteurs exposés ?
- Quelle application réceptrice CarPlay et quelles sessions média publiées ?
- Quelles données sont visibles dans les applications système ?
- Quel modèle 3D Fiesta 2009 fidèle et légalement réutilisable ?
- Carrosserie 3 ou 5 portes (impacte le modèle 3D et les zones de portières) ?
- L'autoradio expose-t-il son propre état jour / nuit ou l'éclairage à Android ?