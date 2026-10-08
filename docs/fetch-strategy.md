# Stratégie de fetch et de cascade — AutoLoc

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition) ; supprimer le contrat supprime ses paiements, retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence ; la liste n'est chargée que si on l'utilise. |
| Agence → Employe | LAZY | Aucune | Un employé a un cycle de vie propre ; on évite de charger tous les employés à chaque lecture d'agence. |
| Vehicule ↔ Equipement | LAZY | Aucune | Équipements partagés entre véhicules : supprimer un véhicule ne doit pas supprimer un équipement. Set pour éviter doublons et réécritures de la table de jointure. |
| Client → Reservation | LAZY | PERSIST | Une nouvelle réservation créée avec un client est enregistrée avec lui ; pas de REMOVE pour conserver l'historique des réservations. |
| Reservation → Vehicule | LAZY | Aucune | Le véhicule existe indépendamment des réservations ; LAZY évite de charger le véhicule (et sa chaîne) à chaque lecture de réservation. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Le contrat découle de la réservation : enregistrer, modifier ou supprimer la réservation se propage au contrat. |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance créée avec un véhicule est enregistrée avec lui ; pas de REMOVE pour garder l'historique d'entretien. |

## Principes retenus
- `@ManyToOne` et `@OneToOne` forcés en LAZY (leur défaut est EAGER).
- `@OneToMany` et `@ManyToMany` conservés en LAZY (leur défaut).
- Cascade uniquement en cas de composition ou de dépendance forte.
- Lombok ciblé (`@Getter`, `@Setter`), jamais `@Data`, pour éviter les boucles `toString`/`hashCode`.