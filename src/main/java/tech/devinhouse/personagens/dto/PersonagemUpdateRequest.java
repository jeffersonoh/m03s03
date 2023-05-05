package tech.devinhouse.personagens.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonagemUpdateRequest {

    @NotEmpty(message = "Nome deve ser informado")
    @Size(min = 1, max = 40, message = "Nome com tamanho inválido")
    private String nome;

    @PastOrPresent(message = "Data deve ser anterior à data atual")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Converte a data neste formato
    private LocalDate dataNascimento;

    @NotEmpty(message = "Série deve ser informado")
    @Size(min = 5, max = 30, message = "Série com tamanho inválido")
    private String serie;

}
