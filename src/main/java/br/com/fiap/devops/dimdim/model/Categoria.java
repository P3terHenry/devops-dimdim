package br.com.fiap.devops.dimdim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "T_DIMDIM_CATEGORIA")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIA")
    private Long idCategoria;

    @NotNull
    @Column(name = "NOME_CATEGORIA", nullable = false, length = 100)
    private String nomeCategoria;

    @NotNull
    @Column(name = "DESCRICAO", length = 255)
    private String descricaoCategoria;

    @ManyToOne
    @JoinColumn(name = "USUARIO_CATEGORIA_ID", nullable = false)
    private Usuario usuarioCategoriaId;

    @NotNull
    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;
}
