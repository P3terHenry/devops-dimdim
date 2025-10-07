package br.com.fiap.devops.dimdim.repository;

import br.com.fiap.devops.dimdim.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
