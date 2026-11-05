# Voiture 3D réactive — concept B2

**Statut : étude, rien de confirmé sur le véhicule.** Aucune réaction listée ici ne doit être activée avant d'avoir été observée sur la Fiesta et consignée dans `DIAGNOSTIC_AUTORADIO.md`.

## Principe

La Ford Fiesta Mk7 verte reste le centre visuel du launcher. Elle est **sobre par défaut** et ne réagit qu'à une donnée réelle, validée et récente. Sans donnée, elle ne montre rien de particulier : elle ne prétend pas que tout va bien.

Règle clé : **inconnu ≠ fermé**. Si l'état des portières n'est pas disponible, l'interface n'affiche pas « portières fermées » ; elle affiche « Portières : non disponible » dans la carte véhicule, et la voiture reste neutre.

## Comportement visuel

| Situation | Voiture | Texte |
| --- | --- | --- |
| Aucune donnée véhicule | Neutre, statique | Rien sur la voiture |
| Donnée disponible, état normal | Neutre | Rien |
| Alerte à l'arrêt (ex. portière ouverte) | Zone surlignée en ambre, contour pointillé | Une ligne au-dessus : « Portière avant gauche ouverte » |
| Alerte en roulant | Zone surlignée, **sans animation** | Même ligne, pas de son ni de clignotement |
| Donnée périmée (plus de 5 s sans mise à jour) | Surlignage retiré | « Portières : donnée périmée » dans la carte |
| Mode démo | Comme une alerte réelle | Badge **DÉMO** obligatoire sur la ligne |

Animations autorisées, uniquement à l'arrêt : entrée de la voiture (fondu + rotation de 8 à 10°, une fois au démarrage du launcher), changement de vue au doigt, ouverture douce d'une portière modélisée quand l'état est confirmé. En roulant : aucune animation, rendu figé, seules les couleurs de zone peuvent changer.

« À l'arrêt » = vitesse GPS < 2 km/h pendant 5 s **et** fix GPS valide. Si le GPS n'a pas de fix, la rotation reste désactivée (choix prudent). Le frein à main, s'il devient lisible via le CAN, renforcera cette détection.

## Tableau des interactions

Statuts : **Confirmé** (observé sur la voiture), **Plausible** (rapporté sur des Fiesta similaires ou standard OBD2), **À tester**, **Indisponible**. Aujourd'hui, rien n'est confirmé : le journal de diagnostic est vide.

| Interaction visuelle | Source potentielle | Statut | Complexité | Priorité |
| --- | --- | --- | --- | --- |
| Vue 3/4 fixe + rotation manuelle à l'arrêt | Android (GPS) | Plausible (API standard, précision à tester) | Moyenne | MVP |
| Adaptation au thème jour / nuit | Launcher | Faisable (logique interne) | Faible | MVP |
| Portières ouvertes (4 zones) | CAN FYT via `com.syu.ms` | Plausible (rapporté sur Fiesta 2011, profil « 08~12 Fiesta ») | Élevée | P2 |
| Coffre ouvert | CAN FYT | À tester | Élevée | P2 |
| Phares allumés | CAN FYT ou fil d'éclairage de l'autoradio | À tester | Moyenne | P2 (surtout pour le thème) |
| Frein à main | CAN FYT ou fil dédié | Plausible | Moyenne | P2 (fiabilise « à l'arrêt ») |
| Marche arrière | CAN FYT ou fil de marche arrière | Plausible | Moyenne | Éviter sur la voiture : l'autoradio affiche déjà la caméra en plein écran |
| Moteur tournant / arrêté | OBD2 (régime > 0) | Plausible avec adaptateur | Moyenne | P3 |
| Zone moteur surlignée si code défaut | OBD2, lecture seule | Plausible avec adaptateur | Moyenne | P3, à l'arrêt uniquement |
| Moteur froid (avant 60 °C) | OBD2 | Plausible avec adaptateur | Faible | P3, texte seulement |
| Kilométrage | CAN FYT | Plausible (affiché par un décodeur sur Fiesta 2009) | Moyenne | Texte dans la carte, pas sur la 3D |
| Clignotants | CAN | Indisponible (non rapporté) | Élevée | Éviter : doublon du tableau de bord, distrayant |
| Warnings | CAN | Indisponible (non rapporté) | Élevée | Éviter |
| Verrouillage / déverrouillage | CAN | Indisponible ; autoradio éteint quand la voiture est verrouillée | Élevée | Éviter |

## Repli quand la donnée n'existe pas

- La voiture reste un élément d'ambiance et d'identité : vue soignée, éclairage selon le thème, rotation à l'arrêt.
- La carte véhicule porte les causes : « Portières : CAN à tester », « Moteur : OBD2 requis ».
- Un bouton « Voir le diagnostic » mène à la liste des données et de leur état, utile pour les tests.
- Aucun état n'est déduit d'un autre (pas de « moteur tournant » parce que l'autoradio est allumé).

## Contrat de données

Chaque état réactif suit le contrat d'`AGENTS.md` (`source`, `status`, `timestamp`, `reason`). La 3D ne lit que des états `available` récents ; `pending`, `unavailable` et périmés = voiture neutre ; `demo` = réaction + badge.

```text
door_front_left: { source: CAN_FYT, status: unavailable, reason: "décodeur non testé" }
reverse:         { source: CAN_FYT, status: unavailable, reason: "décodeur non testé" }
engine_running:  { source: OBD2,    status: unavailable, reason: "pas d'adaptateur" }
stationary:      { source: GPS,     status: available,   timestamp: …, value: true }
```

## Modèle 3D : exigences

- Fiesta Mk7 / JA8 5 portes (à confirmer : 3 ou 5 portes), phase 1 (2008-2012), peinture verte paramétrable.
- Licence permettant l'usage dans une application personnelle ; aucun logo Ford ni plaque « Fiesta » intégrés au dépôt public sans vérification des droits.
- Nœuds nommés pour les pièces animables : `door_fl`, `door_fr`, `door_rl`, `door_rr`, `tailgate`, `headlights` ; zones de surlignage `zone_engine`, `zone_rear`.
- Budget : modèle allégé (cible indicative < 100 000 triangles, textures ≤ 2K), éclairage précalculé, rendu plafonné à 30 images/s et mis en pause hors écran ou en roulant.
- Repli : image fixe pré-rendue depuis le modèle si les performances de l'UIS7862 ne suffisent pas.