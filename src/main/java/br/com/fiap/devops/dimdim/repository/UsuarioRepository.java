package br.com.fiap.devops.dimdim.repository;

import br.com.fiap.devops.dimdim.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailUsuario(String email);
}
