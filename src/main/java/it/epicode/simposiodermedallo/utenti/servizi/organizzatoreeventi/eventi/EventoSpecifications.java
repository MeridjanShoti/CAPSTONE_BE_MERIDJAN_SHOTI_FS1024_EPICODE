package it.epicode.simposiodermedallo.utenti.servizi.organizzatoreeventi.eventi;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EventoSpecifications {
    public static Specification<Evento> filterBy(EventoFilter filter) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            if (filter.getNomeParziale() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeEvento")), "%" + filter.getNomeParziale().toLowerCase() + "%"));
            }
            if (filter.getCitta() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("citta")), "%"+ filter.getCitta().toLowerCase()+ "%"));
            }
            if (filter.getTipoEvento() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("tipoEvento"), filter.getTipoEvento()));
            }
            if (filter.getData1() != null && filter.getData2() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.between(root.get("dataEvento"), filter.getData1(), filter.getData2())
                );
            } else if (filter.getData1() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dataEvento"), filter.getData1())
                );
            } else if (filter.getData2() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("dataEvento"), filter.getData2())
                );
            }
            if (filter.getArtista() != null && !filter.getArtista().isBlank()) {
                Join<Evento, String> artistiJoin = root.join("artistiPartecipanti");
                query.distinct(true);

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(artistiJoin),
                                "%" + filter.getArtista().toLowerCase() + "%"
                        )
                );
            }
            if (filter.getSoloFuturi() != null && filter.getSoloFuturi()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("dataEvento"), LocalDate.now()));
            }

            return predicate;
        };


    }
}
