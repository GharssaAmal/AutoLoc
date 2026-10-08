package jar;

import jar.entity.Client;
import jar.entity.Contrat;
import jar.entity.Reservation;
import jar.enums.StatutReservation;
import jar.repository.ClientRepository;
import jar.repository.ContratRepository;
import jar.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationContratTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ContratRepository contratRepository;

    @Test
    @Transactional
    void persistClientEtCascadeReservationContrat() {
        long reservationsAvant = reservationRepository.count();
        long contratsAvant = contratRepository.count();

        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Sami");
        client.setEmail("sami@test.com");
        client.setDateInscription(LocalDate.now());

        Reservation r = new Reservation();
        r.setDateDebut(LocalDate.now());
        r.setDateFin(LocalDate.now().plusDays(3));
        r.setStatut(StatutReservation.CONFIRMEE);

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("300"));
        contrat.setValide(true);

        r.attacherContrat(contrat);
        client.addReservation(r);

        // PERSIST : le client enregistre la réservation, ALL : la réservation enregistre le contrat
        clientRepository.saveAndFlush(client);
        assertEquals(reservationsAvant + 1, reservationRepository.count());
        assertEquals(contratsAvant + 1, contratRepository.count());

        /// REMOVE sur la réservation : le contrat est supprimé aussi
        client.getReservations().remove(r);   // retirer r de la liste du client d'abord
        reservationRepository.delete(r);
        reservationRepository.flush();
        assertEquals(contratsAvant, contratRepository.count());
    }
}