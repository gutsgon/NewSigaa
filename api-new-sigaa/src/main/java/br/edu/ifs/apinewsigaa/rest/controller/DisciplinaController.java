package br.edu.ifs.apinewsigaa.rest.controller;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.DisciplinaModel;
import br.edu.ifs.apinewsigaa.model.ProfessorModel;
import br.edu.ifs.apinewsigaa.rest.dto.DisciplinaComAlunosDTO;
import br.edu.ifs.apinewsigaa.rest.dto.DisciplinaDto;
import br.edu.ifs.apinewsigaa.rest.dto.ProfessorDto;
import br.edu.ifs.apinewsigaa.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping("/buscar/nome/{nome}")
    public ResponseEntity<DisciplinaDto> ObterPorNome(@PathVariable("nome") String nome) {
        DisciplinaDto disciplinaDto;
        try{
            disciplinaDto = disciplinaService.ObterDisciplinaNome(nome);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Email inválido!");
        }
        return ResponseEntity.ok(disciplinaDto);
    }

    @GetMapping("/buscar/nome/contendo/{nome}")
    public ResponseEntity<List<DisciplinaDto>> ObterPorNomeContendo(@PathVariable("nome") String nome) {
        List<DisciplinaDto> disciplinaDtoList;
        try{
            disciplinaDtoList = disciplinaService.ObterDisciplinasNomeContendo(nome);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Email inválido!");
        }
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaDto>> ObterDisciplinasAsc() {
        List<DisciplinaDto> disciplinaDtoList = disciplinaService.ObterDisciplinasAsc();
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping("/desc")
    public ResponseEntity<List<DisciplinaDto>> ObterDisciplinasDesc() {
        List<DisciplinaDto> disciplinaDtoList = disciplinaService.ObterDisciplinasDesc();
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping("/buscar/numeroCreditos/{numeroCreditos}")
    public ResponseEntity<List<DisciplinaDto>> ObterPorNumeroCreditos(@Valid int numeroCreditos) {
        List<DisciplinaDto> disciplinaDtoList = disciplinaService.ObterDisciplinasNumeroCreditos(numeroCreditos);
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping("/buscar/numeroCreditos/asc")
    public ResponseEntity<List<DisciplinaDto>> ObterPorNumeroCreditosAsc() {
        List<DisciplinaDto> disciplinaDtoList = disciplinaService.ObterDisciplinasNumeroCreditosAsc();
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping("/buscar/numeroCreditos/desc")
    public ResponseEntity<List<DisciplinaDto>> ObterPorNumeroCreditosDesc() {
        List<DisciplinaDto> disciplinaDtoList = disciplinaService.ObterDisciplinasNumeroCreditosDesc();
        return ResponseEntity.ok(disciplinaDtoList);
    }

    @GetMapping("/buscar/alunos/{id}")
    public ResponseEntity<DisciplinaComAlunosDTO> ObterAlunos(@Valid @PathVariable("id") int idDisciplina) {
        DisciplinaComAlunosDTO disciplinaComAlunosDTO = disciplinaService.ObterAlunos(idDisciplina);
        return ResponseEntity.ok(disciplinaComAlunosDTO);
    }



    @PostMapping("/criar")
    public ResponseEntity<DisciplinaDto> Criar(@RequestBody DisciplinaModel disciplinaModel) {
        DisciplinaDto disciplinaDto = disciplinaModel.ModelToDto(disciplinaModel);
        disciplinaService.CriarDisciplina(disciplinaModel);
        return new ResponseEntity<>(disciplinaDto, HttpStatus.OK);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<DisciplinaDto> Atualizar(@RequestBody DisciplinaModel disciplinaModel) {
        DisciplinaDto disciplinaDto = disciplinaModel.ModelToDto(disciplinaModel);
        disciplinaService.AtualizarDisciplina(disciplinaModel);
        return new ResponseEntity<>(disciplinaDto, HttpStatus.OK);
    }

    @PutMapping("/atualizar/nome")
    public ResponseEntity<DisciplinaDto> AtualizarNome(@RequestBody DisciplinaModel disciplinaModel){
        DisciplinaDto disciplinaDto = disciplinaService.AtualizarNome(disciplinaModel);
        return ResponseEntity.ok(disciplinaDto);
    }

    @PutMapping("/atualizar/numeroCreditos")
    public ResponseEntity<DisciplinaDto> AtualizarNumeroCreditos(@RequestBody DisciplinaModel disciplinaModel){
        DisciplinaDto disciplinaDto = disciplinaService.AtualizarNumeroCreditos(disciplinaModel);
        return ResponseEntity.ok(disciplinaDto);
    }

    @DeleteMapping("/deletar/nome/{nome}")
    public ResponseEntity<Void> DeletarPorNome(@PathVariable("nome") String nome ){
        try{
            disciplinaService.DeletarDisciplinaNome(nome);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> DeletarPorId(@PathVariable("id") Integer id){
        try{
            disciplinaService.DeletarDisciplinaId(id);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}
