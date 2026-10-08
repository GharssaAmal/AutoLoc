package jar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    private LocalDate dateSignature;
    private BigDecimal montantTotal;
    private boolean valide;

    // Reservation <-> Contrat : côté propriétaire (porte la clé étrangère)
    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    // Contrat -> Paiement : côté inverse, composition
    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();

    public void addPaiement(Paiement paiement) {
        paiements.add(paiement);
        paiement.setContrat(this);
    }

    public void removePaiement(Paiement paiement) {
        paiements.remove(paiement);
        paiement.setContrat(null);
    }
}