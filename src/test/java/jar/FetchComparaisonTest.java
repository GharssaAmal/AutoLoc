package jar;

import jar.entity.Contrat;
import jar.entity.Paiement;
import jar.enums.ModePaiement;
import jar.repository.ContratRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
class FetchComparaisonTest {

    @Autowired
    ContratRepository contratRepository;

    // Pas de @Transactional ici : on veut que findById aille vraiment en base
    @Test
    void comparerFetch() {
        // 1. Préparer les données : un contrat avec 2 paiements
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("500"));
        c.setValide(true);

        Paiement p1 = new Paiement();
        p1.setMontant(new BigDecimal("200"));
        p1.setDatePaiement(LocalDate.now());
        p1.setModePaiement(ModePaiement.CARTE);

        Paiement p2 = new Paiement();
        p2.setMontant(new BigDecimal("300"));
        p2.setDatePaiement(LocalDate.now());
        p2.setModePaiement(ModePaiement.VIREMENT);

        c.addPaiement(p1);
        c.addPaiement(p2);

        Long id = contratRepository.save(c).getIdContrat();

        // 2. Le moment à observer
        System.out.println("=========== DEBUT findById ===========");
        Contrat charge = contratRepository.findById(id).orElseThrow();
        System.out.println("=========== FIN findById ===========");
        System.out.println("Contrat charge, id = " + charge.getIdContrat());

        // 3. Nettoyer la base
        contratRepository.deleteById(id);
    }
}