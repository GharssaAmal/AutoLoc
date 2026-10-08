package jar.entity;

import jakarta.persistence.*;
import jar.enums.CategorieVehicule;
import jar.enums.StatutVehicule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    private String immatriculation;
    private String marque;
    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    // Agence -> Vehicule : côté propriétaire (porte la clé étrangère)
    @ManyToOne(fetch = FetchType.LAZY)
    private Agence agence;

    // Vehicule <-> Equipement : côté propriétaire, aucune cascade, Set
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "vehicule_equipement")
    private Set<Equipement> equipements = new HashSet<>();

    // Vehicule -> Maintenance : côté inverse, cascade PERSIST
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Maintenance> maintenances = new ArrayList<>();
}