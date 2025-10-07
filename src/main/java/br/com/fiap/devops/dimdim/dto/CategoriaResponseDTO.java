package br.com.fiap.devops.dimdim.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoriaResponseDTO {

    private Long idCategoria;
    private String nomeCategoria;
    private String descricaoCategoria;
    private Long usuarioId;
    private LocalDateTime dataCriacao;
}
