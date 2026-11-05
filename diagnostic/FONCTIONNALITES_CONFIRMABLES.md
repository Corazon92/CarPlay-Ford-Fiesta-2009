# Fonctionnalités du launcher confirmables après les tests

Aucune n'est confirmée à ce jour. « Test » renvoie à `CHECKLIST.md`. Mettre à jour la colonne État avec des preuves.

| Fonctionnalité | Condition de confirmation | Test | État |
| --- | --- | --- | --- |
| Heure/date, lancement d'applis | Launcher choisissable (ou lancé manuellement) ; packages connus | A4, A10, B1 | À tester |
| Ouvrir CarPlay depuis le dock | Package du récepteur relevé et lancement OK | A4, B1 | À tester |
| Disposition adaptée à l'écran | `realWidthPx/HeightPx` et `smallestWidthDp` mesurés | A2, B1 | À tester |
| GPS / vitesse GNSS | Fix + permissions accordées | A9, B1 | À tester |
| Contrôles média Android | Session publiée (`mediaSessions.count > 0`) pour la source | A7, A8, B1 | À tester |
| Titre/pochette CarPlay | Session présente sur CarPlay ; sinon « Lecture via CarPlay » | A7, B1 | À tester (probable : non) |
| Guidage dans une appli Android | Notification exploitable | (extension, non couvert) | À tester |
| Portières / coffre / marche arrière / frein à main | Visibles dans l'appli d'origine **et** service FYT/QF exposé et accessible | A5, A6, B1, puis Niveau 3 | À tester |
| Vitesse/régime véhicule via CAN | Idem + codes propres au profil Fiesta | Niveau 3 | À tester |
| Température moteur, tension, carburant, conso | Non attendu via CAN décodeur ; OBD2 | — | Reporté |

Règle : une fonction passe à « Confirmée » uniquement avec capture/rapport daté ; une absence observée = « Non observée », pas « impossible ».
