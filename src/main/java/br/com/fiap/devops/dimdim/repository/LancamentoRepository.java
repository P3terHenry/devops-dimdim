package br.com.fiap.devops.dimdim.repository;

import br.com.fiap.devops.dimdim.model.Categoria;
import br.com.fiap.devops.dimdim.model.Lancamento;
import br.com.fiap.devops.dimdim.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByCategoriaLancamentoId(Categoria categoriaLancamentoId);
}
