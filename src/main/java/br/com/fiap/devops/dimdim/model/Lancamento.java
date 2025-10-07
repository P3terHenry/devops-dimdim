package br.com.fiap.devops.dimdim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "T_DIMDIM_LANCAMENTO")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LANCAMENTO", nullable = false)
    private Long idLancamento;

    @NotNull
    @Column(name = "VALOR_LANCAMENTO", nullable = false)
    private Double valorLancamento;

    @NotNull
    @Column(name = "DATA_LANCAMENTO", nullable = false)
    private LocalDateTime dataLancamento;

    @NotNull
    @Column(name = "TIPO_LANCAMENTO", nullable = false, length = 20)
    private String tipoLancamento; // ENTRADA ou SAIDA

    @ManyToOne
    @JoinColumn(name = "CATEGORIA_LANCAMENTO_ID", nullable = false)
    private Categoria categoriaLancamentoId;
}
