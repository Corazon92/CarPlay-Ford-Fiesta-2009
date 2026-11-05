# Diagnostic Hizpo — checklist en lecture seule

**But :** remplacer les suppositions par des preuves sur l'installation réelle. Procéder **à l'arrêt, véhicule stationné en sécurité**. Photographier, relever, ne pas modifier les paramètres.

## Niveau 1 — identifier le matériel, sans installation

- [ ] Paramètres > À propos : photographier version Android, modèle, RAM, stockage, version système et **MCU**.
- [ ] Mesurer la résolution de l'affichage (dimensions réelles d'une capture d'écran).
- [ ] Vérifier la section CAN/voiture **en consultation uniquement** : marque de CAN box, version/profil Fiesta choisi. **Ne changer aucun profil.**
- [ ] Paramètres > Applications système : identifier l'application CarPlay (`ZLink`, `TLink` ou autre), les apps d'infos véhicule et les packages éventuels `com.syu.*`.
- [ ] Dans l'application véhicule d'origine, relever les écrans et les informations effectivement visibles.
- [ ] À l'arrêt, observer si l'ouverture de chaque portière, du coffre ou la marche arrière produit un affichage. Ne pas effectuer ces manipulations sur la route.
- [ ] Confirmer motorisation exacte (essence/diesel et cylindrée), sans publier de VIN.

## Niveau 2 — multimédia et GPS

- [ ] Musique par CarPlay : regarder si un titre/pochette apparaît dans une notification média Android ; absence de notification = **pas de preuve** d'une session accessible.
- [ ] Même essai en Bluetooth audio ordinaire.
- [ ] Même essai avec Spotify ou lecteur Android installé.
- [ ] Lancer un trajet dans une application GPS Android installée et regarder si le prochain virage apparaît dans une notification exploitable.
- [ ] Vérifier GPS, connexion météo et l'état Bluetooth avec les permissions pertinentes.

## Niveau 3 — seulement après revue des résultats et accord

- [ ] Si nécessaire, évaluer la provenance et la sécurité d'un outil d'information FYT et consulter **uniquement** ses écrans en lecture seule.
- [ ] Sur preuve de compatibilité, expérimenter la lecture CAN via un outil communautaire revu ; ne pas activer d'écriture et ne pas exposer des logs bruts dans le dépôt.
- [ ] Décider ensuite seulement si un adaptateur OBD2 doit être acheté.

## Journal de résultats (à compléter)

| Test | Résultat observé | Capture / note non sensible | Statut |
| --- | --- | --- | --- |
| Android / MCU / modèle | — | — | À faire |
| Résolution | — | — | À faire |
| CAN box / profil | — | — | À faire |
| Application CarPlay | — | — | À faire |
| Infos voiture d'origine | — | — | À faire |
| Portières / coffre / marche arrière | — | — | À faire |
| Musique CarPlay | — | — | À faire |
| Musique Bluetooth | — | — | À faire |
| Musique Android native | — | — | À faire |
| GPS et notification de guidage | — | — | À faire |

### Interdictions

Pas de flash, root, changement de CAN box/profil, écriture de trames, codage véhicule, effacement DTC ou modification de MCU. Ne pas copier de secrets, adresses, VIN, journaux bruts ou identifiants dans un dépôt public.

## Complément (étape 2 — 05/11/2026)

Procédure détaillée, captures et modèle de relevé : dossier [`diagnostic/`](../diagnostic/README.md). Recherche : [`RECHERCHE_FYT_CAN.md`](RECHERCHE_FYT_CAN.md). Point d'attention : « UIS7862 » ne prouve pas la famille FYT (la famille QF existe pour certains Hizpo) ; vérifier avant de miser sur `com.syu.ms`.
