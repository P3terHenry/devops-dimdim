package br.com.fiap.devops.dimdim.repository;

import br.com.fiap.devops.dimdim.model.Categoria;
import br.com.fiap.devops.dimdim.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c WHERE c.usuarioCategoriaId = :id")
    List<Categoria> findByUsuarioId(@Param("id") Long id);
}
