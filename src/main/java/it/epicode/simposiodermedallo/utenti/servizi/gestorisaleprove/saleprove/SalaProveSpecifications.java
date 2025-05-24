package it.epicode.simposiodermedallo.utenti.servizi.gestorisaleprove.saleprove;

import org.springframework.data.jpa.domain.Specification;

public class SalaProveSpecifications {
    public static Specification<SalaProve> filterBy(SalaProveFilter filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();
            if (filter.getCitta() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("citta"),  "%" + filter.getCitta().toLowerCase() + "%"));
            }

            if (filter.getGiornoApertura() != null) {
                predicate = cb.and(predicate,
                        cb.isMember(filter.getGiornoApertura(), root.get("giorniApertura"))
                );
            }
            if (filter.getCapienzaMin() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("capienzaMax"), filter.getCapienzaMin()));
            }
            if (filter.getPrezzoOrarioMax() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("prezzoOrario"), filter.getPrezzoOrarioMax()));
            }
            return predicate;
        };

    }
}
