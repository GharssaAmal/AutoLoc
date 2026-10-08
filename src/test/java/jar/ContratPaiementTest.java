package jar;

import jar.entity.Contrat;
import jar.entity.Paiement;
import jar.enums.ModePaiement;
import jar.repository.ContratRepository;
import jar.repository.PaiementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ContratPaiementTest {

    @Autowired
    ContratRepository contratRepository;

    @Autowired
    PaiementRepository paiementRepository;

    @Test
    @Transactional
    void cascadeEtOrphanRemoval() {
        long avant = paiementRepository.count();

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

        // CASCADE : un seul save, 2 paiements insérés
        contratRepository.saveAndFlush(c);
        assertEquals(avant + 2, paiementRepository.count());

        // ORPHAN REMOVAL : p1 retiré de la liste, supprimé en base
        c.removePaiement(p1);
        contratRepository.flush();
        assertEquals(avant + 1, paiementRepository.count());

        // REMOVE : suppression du contrat, p2 disparaît aussi
        contratRepository.delete(c);
        contratRepository.flush();
        assertEquals(avant, paiementRepository.count());
    }
}