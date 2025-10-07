package br.com.fiap.devops.dimdim.controller;

import br.com.fiap.devops.dimdim.dto.CategoriaRequestDTO;
import br.com.fiap.devops.dimdim.dto.CategoriaResponseDTO;
import br.com.fiap.devops.dimdim.model.Categoria;
import br.com.fiap.devops.dimdim.model.Usuario;
import br.com.fiap.devops.dimdim.repository.CategoriaRepository;
import br.com.fiap.devops.dimdim.repository.UsuarioRepository;
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
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Operações relacionadas às Categorias de despesas e receitas.")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ============================
    // GET - Listar todas as categorias
    // ============================
    @Operation(summary = "Retorna todas as categorias cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhuma categoria encontrada.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listar")
    public List<CategoriaResponseDTO> listarTodas() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma categoria encontrada");
        }
        return categorias.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ============================
    // GET - Listar categorias por usuário
    // ============================
    @Operation(summary = "Retorna as categorias associadas a um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou sem categorias.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/usuario/{usuarioId}")
    public List<CategoriaResponseDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        List<Categoria> categorias = categoriaRepository.findByUsuarioId(usuarioId);
        if (categorias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma categoria encontrada para o usuário informado");
        }

        return categorias.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // ============================
    // POST - Inserir nova categoria
    // ============================
    @Operation(summary = "Insere uma nova categoria vinculada a um usuário (informando apenas o ID do usuário).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria inserida com sucesso."),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios não informados ou usuário inexistente.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/inserir")
    public CategoriaResponseDTO inserirCategoria(@RequestBody CategoriaRequestDTO dto) {

        if (dto.getNomeCategoria() == null || dto.getDescricaoCategoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e descrição da categoria são obrigatórios.");
        }

        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário informar o ID do usuário vinculado à categoria.");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuarioId());
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário informado não existe.");
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setDescricaoCategoria(dto.getDescricaoCategoria());
        categoria.setUsuarioCategoriaId(usuario.get());
        categoria.setDataCriacao(LocalDateTime.now());

        Categoria salva = categoriaRepository.save(categoria);
        return toResponseDTO(salva);
    }

    // ============================
    // PUT - Atualizar categoria
    // ============================
    @Operation(summary = "Atualiza uma categoria existente (informando apenas o ID do usuário).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria para atualização não encontrada.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(value = "/atualizar/{id}")
    public CategoriaResponseDTO atualizarCategoria(@RequestBody CategoriaRequestDTO dto, @PathVariable Long id) {
        Optional<Categoria> op = categoriaRepository.findById(id);
        if (op.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada.");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(dto.getUsuarioId());
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário informado não existe.");
        }

        Categoria categoria = op.get();
        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setDescricaoCategoria(dto.getDescricaoCategoria());
        categoria.setUsuarioCategoriaId(usuario.get());

        Categoria atualizada = categoriaRepository.save(categoria);
        return toResponseDTO(atualizada);
    }

    // ============================
    // DELETE - Deletar categoria
    // ============================
    @Operation(summary = "Exclui uma categoria existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria para exclusão não encontrada.", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping(value = "/deletar/{id}")
    public CategoriaResponseDTO deletarCategoria(@PathVariable Long id) {
        Optional<Categoria> op = categoriaRepository.findById(id);

        if (op.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria para exclusão não encontrada.");
        }

        Categoria deletar = op.get();
        categoriaRepository.delete(deletar);
        return toResponseDTO(deletar);
    }

    // ============================
    // MÉTODO AUXILIAR - Conversão para DTO
    // ============================
    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNomeCategoria(categoria.getNomeCategoria());
        dto.setDescricaoCategoria(categoria.getDescricaoCategoria());
        dto.setUsuarioId(categoria.getUsuarioCategoriaId().getIdUsuario());
        dto.setDataCriacao(categoria.getDataCriacao());
        return dto;
    }
}
