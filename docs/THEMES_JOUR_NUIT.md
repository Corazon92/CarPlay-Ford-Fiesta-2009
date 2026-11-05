# Thème jour / nuit — concept B2

**Statut : proposition de design, non implémentée.** Maquettes de référence : artboards « B2 — Nuit » et « B2 — Jour » du canvas de design (05/11/2026).

## Principe

Un seul produit, deux éclairages. La géométrie, la typographie (Sora), les icônes, la voiture et la teinte émeraude restent identiques ; seules les surfaces, le contraste et l'intensité de l'accent changent.

- **Nuit (thème principal)** : noir graphite, cartes à peine détachées du fond, accent émeraude lumineux, halo très léger sous la voiture.
- **Jour** : fond gris-vert clair (pas de blanc pur, pour limiter l'éblouissement et les reflets), cartes blanches, accent émeraude assombri pour rester lisible en plein soleil.

## Palettes (jetons de design)

| Jeton | Nuit | Jour | Usage |
| --- | --- | --- | --- |
| `bg` | `#07090A` | `#E6ECE9` | Fond de l'écran |
| `surface` | `#0F1413` | `#FFFFFF` | Cartes, dock |
| `surface2` | `#161D1B` | `#F2F5F4` | Zones internes, boutons secondaires |
| `border` | `#1F2926` | `#CCD6D2` | Bordures 1 px |
| `text` | `#EEF3F0` | `#0E1513` | Texte principal |
| `text2` | `#A3AEA8` | `#45514C` | Texte secondaire |
| `text3` | `#86928C` | `#59655F` | Libellés, causes d'indisponibilité |
| `accent` | `#34C88E` | `#0E7A52` | Actions, états actifs |
| `accentSoft` | `#12271F` | `#E4F4EC` | Fond des puces et pastilles actives |
| `onAccent` | `#04130C` | `#FFFFFF` | Texte sur bouton accent |
| `warn` | `#E8A33D` | `#8A5200` | Alertes véhicule, badge Démo |
| `warnSoft` | `#2A2013` | `#FBEFD9` | Fond du badge Démo |

Contraste visé : **≥ 4,5:1** pour tout texte, y compris `text3` sur `surface` et `surface2` dans les deux thèmes ; **≥ 7:1** pour les valeurs critiques (heure, vitesse, virage). L'accent jour est volontairement plus sombre que l'accent nuit : le vert `#34C88E` tombe sous 2,5:1 sur blanc.

## Adaptations par composant

| Composant | Nuit | Jour |
| --- | --- | --- |
| Cartes | Fond `surface` presque noir, bordure discrète | Blanc, bordure grise visible (pas d'ombre portée lourde) |
| Ombres | Aucune ; profondeur par la bordure | Ombre très légère autorisée, 0 à 2 px de flou |
| Bouton principal | Vert lumineux, texte sombre | Vert foncé, texte blanc |
| Puces d'état | Fond vert très sombre, texte vert | Fond vert pâle, texte vert foncé |
| Badge Démo | Ambre sur brun sombre | Ambre foncé sur crème |
| Voiture | Halo émeraude au sol, ombre dense | Pas de halo, ombre adoucie, sol gris clair |
| Icônes | Traits clairs, actifs en vert | Traits sombres, actifs en vert foncé |
| Luminosité écran | Gérée par l'autoradio, non par le launcher | Idem |

## Logique de bascule

Ordre de priorité, du plus fort au plus faible :

1. **Manuel forcé** : l'utilisateur choisit Jour ou Nuit dans la barre du haut ou les réglages ; le choix reste jusqu'au retour en « Auto ».
2. **Phares** *(plus tard, si la donnée est validée)* : feux allumés → Nuit. Source possible : CAN FYT ou fil d'éclairage déjà relié à l'autoradio. **À tester, non disponible dans le MVP.**
3. **Auto solaire** : calcul local du lever et du coucher du soleil à partir d'une position approximative (GPS ou dernière position connue), sans réseau.
4. **Auto horaire de secours** : Jour de 7 h à 19 h si aucune position n'est connue.

Règles anti-scintillement : pas plus d'un changement automatique toutes les 10 minutes ; transition en fondu de 300 à 400 ms ; jamais de bascule pendant une interaction tactile.

À vérifier pendant le diagnostic : l'autoradio a son propre réglage jour / nuit. Si le firmware expose cet état à Android, le launcher peut simplement le suivre (source la plus cohérente avec l'écran).

## Avantages et compromis

| Choix | Avantage | Compromis |
| --- | --- | --- |
| Fond jour gris-vert au lieu de blanc | Moins d'éblouissement, identité conservée | Légèrement moins « éclatant » |
| Accent jour assombri | Contraste conforme au soleil | Vert moins vif qu'en nuit |
| Auto solaire local | Fonctionne sans internet ni CAN | Ignore tunnels et parkings souterrains |
| Phares comme source (plus tard) | Suit la vraie luminosité | Dépend d'une donnée CAN non confirmée |
| Pas d'ombres en nuit | Interface plus calme, moins de GPU | Profondeur portée par les bordures uniquement |

## Implémentation envisagée (non actée)

- Jetons centralisés dans un seul thème (Compose `ColorScheme` ou équivalent), aucune couleur codée en dur dans les composants.
- Le rendu 3D reçoit le thème pour ajuster sol, halo et exposition, sans changer la peinture verte.
- Les maquettes jour et nuit restent générées depuis le même gabarit pour éviter toute dérive entre les deux.