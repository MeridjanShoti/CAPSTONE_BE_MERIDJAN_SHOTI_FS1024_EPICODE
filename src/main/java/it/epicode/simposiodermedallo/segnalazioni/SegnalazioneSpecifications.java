package it.epicode.simposiodermedallo.segnalazioni;


import org.springframework.data.jpa.domain.Specification;

public class SegnalazioneSpecifications {
    public static Specification<Segnalazione> filterBy(SegnalazioneFilter filter) {
        return (root, query, criteriaBuilder) ->

        {
            var predicate = criteriaBuilder.conjunction();
            if ( filter.getTipoSegnalazione() != null ) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("tipoSegnalazione"), filter.getTipoSegnalazione()));
            }
            if ( filter.getAutore() != null ) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("autore").get("username")), "%" + filter.getAutore().toLowerCase() + "%"));
            }
            return predicate;
        };

    }
}