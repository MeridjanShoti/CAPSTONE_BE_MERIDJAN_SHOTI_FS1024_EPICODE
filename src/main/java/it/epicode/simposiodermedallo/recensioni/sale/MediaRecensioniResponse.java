package it.epicode.simposiodermedallo.recensioni.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaRecensioniResponse {
    private Optional<Double> media;
}
