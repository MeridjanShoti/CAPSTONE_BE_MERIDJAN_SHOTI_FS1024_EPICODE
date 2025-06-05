package it.epicode.simposiodermedallo.utenti.servizi.scuole.corsi;

import it.epicode.simposiodermedallo.utenti.persone.insegnanti.Insegnante;
import it.epicode.simposiodermedallo.utenti.persone.utentinormali.UtenteNormale;
import it.epicode.simposiodermedallo.utenti.servizi.scuole.Scuola;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CorsoSpecifications {
    public static Specification<Corso> filterBy(CorsoFilter filter) {
return (root, query, cb) -> {
var predicate = cb.conjunction();
if (filter.getStatoCorso() != null) {
    predicate = cb.and(predicate, cb.equal(root.get("statoCorso"), filter.getStatoCorso()));
}
if (filter.getLivello() != null) {
    predicate = cb.and(predicate, cb.equal(root.get("livello"), filter.getLivello()));
}

if (filter.getCosto() != null) {
    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("costo"), filter.getCosto()));
}
if (filter.getDataInizio() != null) {
    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("dataInizio"), filter.getDataInizio()));
}
if (filter.getDataFine() != null) {
    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("dataFine"), filter.getDataFine()));
}
if (filter.getNomeCorso() != null && !filter.getNomeCorso().isBlank()) {
    predicate = cb.and(predicate, cb.like(cb.lower(root.get("nomeCorso")), "%" + filter.getNomeCorso().toLowerCase() + "%"));
}
if (filter.getStrumenti() != null && !filter.getStrumenti().isBlank()) {
    Join<Corso, String> strumentoJoin = root.join("strumenti");
    predicate = cb.and(predicate, cb.like(cb.lower(strumentoJoin), "%" + filter.getStrumenti().toLowerCase() + "%"));
}
if (filter.getInsegnanteId() != null) {
    Join<Corso, Insegnante> insegnanteJoin = root.join("insegnante");
    predicate = cb.and(predicate, cb.equal(insegnanteJoin.get("id"), filter.getInsegnanteId()));
}

if (filter.getGiorniASettimana() != null) {
    predicate = cb.and(predicate, cb.equal(cb.size(root.get("giorniLezione")), filter.getGiorniASettimana()));
}
    if (filter.getPartecipanteId() != null) {
        Join<Corso, UtenteNormale> partecipantiJoin = root.join("partecipanti");
        predicate = cb.and(predicate,
                cb.equal(partecipantiJoin.get("id"), filter.getPartecipanteId()));
    }
    if (filter.getScuolaId() != null) {
        Join<Corso, Scuola> scuolaJoin = root.join("scuola");
        predicate = cb.and(predicate,
                cb.equal(scuolaJoin.get("id"), filter.getScuolaId()));
    }
return predicate;
};

    }
}
