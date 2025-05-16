package it.epicode.simposiodermedallo.utenti.persone.insegnanti;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insegnanti")
public class InsegnanteController {
    @Autowired
    private InsegnanteRepository insegnanteRepository;
    @GetMapping("/curriculum/{id}")
    public ResponseEntity<byte[]> downloadCurriculum(@PathVariable Long id) {
        Insegnante i = insegnanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insegnante non trovato"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"curriculum.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(i.getCurriculum());
    }
}
