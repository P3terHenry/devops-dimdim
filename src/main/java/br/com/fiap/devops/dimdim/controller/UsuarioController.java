package br.com.fiap.devops.dimdim.controller;

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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Operações relacionadas aos Usuários.")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Operation(summary = "Retorna todas os usuários cadastradao.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listar")
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado");
        }
        return usuarios;
    }

    @Operation(summary = "Insere um novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario inserido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Campos obrigatórios não informados.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/inserir")
    public Usuario inserirUsuario(@RequestBody Usuario usuario) {
        if(usuario.getEmailUsuario() == null ||
           usuario.getNomeUsuario() == null ||
           usuario.getSenhaUsuario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos os campos do usuario são obrigatórios.");
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return novoUsuario;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário para atualização não encontrada.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(value = "/atualizar/{id}")
    public Usuario atualizarUsuario(@RequestBody Usuario usuario, @PathVariable Long id) {
        Optional<Usuario> op = usuarioRepository.findById(id);

        if (op.isPresent()) {

            Usuario atualizarUsuario = op.get();

            atualizarUsuario.setEmailUsuario(usuario.getEmailUsuario());
            atualizarUsuario.setNomeUsuario(usuario.getNomeUsuario());
            atualizarUsuario.setSenhaUsuario(usuario.getSenhaUsuario());

            usuarioRepository.save(atualizarUsuario);
            return atualizarUsuario;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário para atualização não encontrado.");
        }
    }

    @Operation(summary = "Exclui um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário para exclusão não encontrado.", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping(value = "/deletar/{id}")
    public Usuario deletarUsuario(@PathVariable Long id) {
        Optional<Usuario> op = usuarioRepository.findById(id);

        if (op.isPresent()) {
            Usuario deletarUsuario = op.get();

            boolean possuiCategorias = !categoriaRepository.findByUsuarioId(id).isEmpty();
            if (possuiCategorias) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível excluir o usuário pois possui categorias associadas.");
            }

            usuarioRepository.delete(deletarUsuario);
            return deletarUsuario;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário para exclusão não encontrado.");
        }
    }
}
