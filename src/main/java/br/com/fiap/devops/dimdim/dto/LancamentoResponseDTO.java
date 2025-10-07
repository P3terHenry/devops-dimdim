package br.com.fiap.devops.dimdim.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LancamentoResponseDTO {

    private Long idLancamento;
    private Double valorLancamento;
    private LocalDateTime dataLancamento;
    private String tipoLancamento;
    private Long categoriaId; // apenas ID da categoria vinculada
}
