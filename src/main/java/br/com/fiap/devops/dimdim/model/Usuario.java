package br.com.fiap.devops.dimdim.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "T_DIMDIM_USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @NotNull
    @Column(name = "NOME_USUARIO", nullable = false, length = 100)
    private String nomeUsuario;

    @NotNull
    @Column(name = "EMAIL_USUARIO", nullable = false, unique = true, length = 120)
    private String emailUsuario;

    @NotNull
    @Column(name = "SENHA_USUARIO", nullable = false)
    private String senhaUsuario;
}
