package br.com.fiap.devops.dimdim.dto;

import lombok.Data;

@Data
public class CategoriaRequestDTO {

    private String nomeCategoria;
    private String descricaoCategoria;
    private Long usuarioId;
}
