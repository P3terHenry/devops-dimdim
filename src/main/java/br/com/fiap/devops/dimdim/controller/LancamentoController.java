package br.com.fiap.devops.dimdim.controller;

import br.com.fiap.devops.dimdim.dto.LancamentoRequestDTO;
import br.com.fiap.devops.dimdim.dto.LancamentoResponseDTO;
import br.com.fiap.devops.dimdim.model.Categoria;
import br.com.fiap.devops.dimdim.model.Lancamento;
import br.com.fiap.devops.dimdim.repository.CategoriaRepository;
import br.com.fiap.devops.dimdim.repository.LancamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lancamentos")
@Tag(name = "Lançamentos", description = "Operações relacionadas aos Lançamentos financeiros.")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ============================
    // GET - Listar todos os lançamentos
    // ============================
    @Operation(summary = "Retorna todos os lançamentos cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamentos retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum lançamento encontrado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listar")
    public List<LancamentoResponseDTO> listarTodos() {
        List<Lancamento> lancamentos = lancamentoRepository.findAll();
        if (lancamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum lançamento encontrado");
        }
        return lancamentos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ============================
    // GET - Listar lançamentos por categoria
    // ============================
    @Operation(summary = "Retorna os lançamentos vinculados a uma categoria específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamentos retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou sem lançamentos.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/categoria/{categoriaId}")
    public List<LancamentoResponseDTO> listarPorCategoria(@PathVariable Long categoriaId) {
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        }

        List<Lancamento> lancamentos = lancamentoRepository.findByCategoriaLancamentoId(categoria.get());
        if (lancamentos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum lançamento encontrado para esta categoria");
        }
        return lancamentos.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ============================
    // POST - Inserir novo lançamento
    // ============================
    @Operation(summary = "Insere um novo lançamento financeiro (informando apenas o ID da categoria).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamento inserido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios não informados ou categoria inexistente.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/inserir")
    public LancamentoResponseDTO inserirLancamento(@RequestBody LancamentoRequestDTO dto) {

        if (dto.getValorLancamento() == null || dto.getTipoLancamento() == null || dto.getCategoriaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatórios ausentes.");
        }

        Optional<Categoria> categoria = categoriaRepository.findById(dto.getCategoriaId());
        if (categoria.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria informada não existe.");
        }

        Lancamento lancamento = new Lancamento();
        lancamento.setValorLancamento(dto.getValorLancamento());
        lancamento.setTipoLancamento(dto.getTipoLancamento());
        lancamento.setDataLancamento(LocalDateTime.now());
        lancamento.setCategoriaLancamentoId(categoria.get());

        Lancamento salvo = lancamentoRepository.save(lancamento);
        return toResponseDTO(salvo);
    }

    // ============================
    // PUT - Atualizar lançamento
    // ============================
    @Operation(summary = "Atualiza um lançamento existente (informando apenas o ID da categoria).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamento atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Lançamento não encontrado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(value = "/atualizar/{id}")
    public LancamentoResponseDTO atualizarLancamento(@RequestBody LancamentoRequestDTO dto, @PathVariable Long id) {
        Optional<Lancamento> op = lancamentoRepository.findById(id);
        if (op.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento não encontrado.");
        }

        Optional<Categoria> categoria = categoriaRepository.findById(dto.getCategoriaId());
        if (categoria.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria informada não existe.");
        }

        Lancamento lancamento = op.get();
        lancamento.setValorLancamento(dto.getValorLancamento());
        lancamento.setTipoLancamento(dto.getTipoLancamento());
        lancamento.setCategoriaLancamentoId(categoria.get());
        lancamento.setDataLancamento(LocalDateTime.now());

        Lancamento atualizado = lancamentoRepository.save(lancamento);
        return toResponseDTO(atualizado);
    }

    // ============================
    // DELETE - Excluir lançamento
    // ============================
    @Operation(summary = "Exclui um lançamento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lançamento excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Lançamento para exclusão não encontrado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping(value = "/deletar/{id}")
    public LancamentoResponseDTO deletarLancamento(@PathVariable Long id) {
        Optional<Lancamento> op = lancamentoRepository.findById(id);

        if (op.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento para exclusão não encontrado.");
        }

        Lancamento deletar = op.get();
        lancamentoRepository.delete(deletar);
        return toResponseDTO(deletar);
    }

    // ============================
    // MÉTODO AUXILIAR - Conversão para DTO
    // ============================
    private LancamentoResponseDTO toResponseDTO(Lancamento lancamento) {
        LancamentoResponseDTO dto = new LancamentoResponseDTO();
        dto.setIdLancamento(lancamento.getIdLancamento());
        dto.setValorLancamento(lancamento.getValorLancamento());
        dto.setTipoLancamento(lancamento.getTipoLancamento());
        dto.setDataLancamento(lancamento.getDataLancamento());
        dto.setCategoriaId(lancamento.getCategoriaLancamentoId().getIdCategoria());
        return dto;
    }
}
