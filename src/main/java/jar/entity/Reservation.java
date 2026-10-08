package jar.entity;

import jakarta.persistence.*;
import jar.enums.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutReservation statut;

    // Client -> Reservation : côté propriétaire
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    // Reservation -> Vehicule : unidirectionnelle, aucune cascade
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicule vehicule;

    // Reservation <-> Contrat : côté inverse, ALL
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contrat contrat;

    public void attacherContrat(Contrat contrat) {
        this.contrat = contrat;
        contrat.setReservation(this);
    }
}