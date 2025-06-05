package it.epicode.simposiodermedallo.commenti;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentoRequest {
    @NotBlank(message = "Il testo è obbligatorio")
private String testo;
}
