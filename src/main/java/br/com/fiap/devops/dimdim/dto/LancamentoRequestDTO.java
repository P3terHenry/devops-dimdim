package br.com.fiap.devops.dimdim.dto;

import lombok.Data;

@Data
public class LancamentoRequestDTO {

    private Double valorLancamento;
    private String tipoLancamento; // ENTRADA ou SAIDA
    private Long categoriaId;      // apenas o ID da categoria
}
