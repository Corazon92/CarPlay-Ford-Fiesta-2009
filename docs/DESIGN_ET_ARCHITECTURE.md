# Concept B — design et architecture cible

**Statut : proposition, pas implémentation validée.** Référence visuelle : concept B choisi parmi trois propositions du 05/11/2026. Le mockup comporte des données de démonstration, **pas des relevés de la voiture**.

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
