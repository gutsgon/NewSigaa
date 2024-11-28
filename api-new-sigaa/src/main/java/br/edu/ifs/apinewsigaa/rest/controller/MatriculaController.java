package br.edu.ifs.apinewsigaa.rest.controller;

import br.edu.ifs.apinewsigaa.model.MatriculaModel;
import br.edu.ifs.apinewsigaa.rest.dto.MatriculaDto;
import br.edu.ifs.apinewsigaa.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping("/buscar/turmaAluno/{idTurma}/{idAluno}")
    public ResponseEntity<MatriculaDto> ObterMatricula(@PathVariable("idTurma") @Valid int idTurma, @PathVariable("idAluno") @Valid int idAluno) {
        MatriculaDto matriculaDto = matriculaService.ObterMatricula(idTurma, idAluno);
        return ResponseEntity.ok(matriculaDto);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<MatriculaDto> AtualizarMatricula(@Valid @RequestBody MatriculaModel matriculaModel) {
        MatriculaDto matriculaDto = matriculaService.AtualizarMatricula(matriculaModel);
        return ResponseEntity.ok(matriculaDto);
    }

    @PostMapping("/criar")
    public ResponseEntity<MatriculaDto> CriarMatricula(@Valid @RequestBody MatriculaModel matriculaModel) {
        MatriculaDto matriculaDto = matriculaService.CriarMatricula(matriculaModel);
        return ResponseEntity.ok(matriculaDto);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> DeletarMatricula(@Valid @RequestBody MatriculaModel matriculaModel) {
        matriculaService.DeletarMatricula(matriculaModel);
        return ResponseEntity.noContent().build();
    }
}
