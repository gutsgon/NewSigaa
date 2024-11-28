package br.edu.ifs.apinewsigaa.rest.controller;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.ProfessorModel;
import br.edu.ifs.apinewsigaa.model.projection.DisciplinaProfessorProjection;
import br.edu.ifs.apinewsigaa.rest.dto.AlunoDto;
import br.edu.ifs.apinewsigaa.rest.dto.ProfessorDisciplinasAlunosDTO;
import br.edu.ifs.apinewsigaa.rest.dto.ProfessorDisciplinasDTO;
import br.edu.ifs.apinewsigaa.rest.dto.ProfessorDto;
import br.edu.ifs.apinewsigaa.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorDto>> ObterProfessorAsc(){
        List<ProfessorDto> professorDto = professorService.ObterProfessorAsc();
        return ResponseEntity.ok(professorDto);
    }

    @GetMapping("/buscar/disciplinas/{matricula}")
    public ResponseEntity<ProfessorDisciplinasDTO> ObterDisciplinasProfessor(@Valid @PathVariable("matricula") String matricula){
        if(matricula.isEmpty()){
            throw new DataIntegrityException("Deve colocar uma matrícula");
        }
        ProfessorDisciplinasDTO professorDisciplinasDTO = professorService.ObterDisciplinasProfessor(matricula);
        return ResponseEntity.ok(professorDisciplinasDTO);
    }

    @GetMapping("/buscar/disciplinasAluno/{matricula}")
    public ResponseEntity<ProfessorDisciplinasAlunosDTO> ObterDisciplinasProfessorAluno(@Valid @PathVariable("matricula") String matricula){
        if(matricula.isEmpty()){
            throw new DataIntegrityException("Deve colocar uma matrícula");
        }
        ProfessorDisciplinasAlunosDTO professorDisciplinasAlunosDTO = professorService.ObterDisciplinasProfessorAluno(matricula);
        return ResponseEntity.ok(professorDisciplinasAlunosDTO);
    }

    @GetMapping("/buscar/nome/{nome}")
    public ResponseEntity<List<ProfessorDto>> ObterProfessorContendoNome(@PathVariable("nome") @Valid String nome){
        List<ProfessorDto> professorDto = professorService.ObterProfessorContendoNome(nome);
        return ResponseEntity.ok(professorDto);
    }

    @GetMapping("/buscar/matricula/{matricula}")
    public ResponseEntity<ProfessorDto> ObterPorMatricula(@PathVariable("matricula") @Valid String matricula) {
        ProfessorDto professorDto = professorService.ObterPorMatricula(matricula);
        try{
            professorDto.validarMatricula(matricula);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException((e.getMessage()));
        }
        return ResponseEntity.ok(professorDto);
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<ProfessorDto> ObterPorEmail(@PathVariable("email") @Valid String email) {
        ProfessorDto professorDto = professorService.ObterPorEmail(email);
        try{
            professorDto.validarEmail(email);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException((e.getMessage()));
        }
        return ResponseEntity.ok(professorDto);
    }

    @GetMapping("/buscar/celular/{celular}")
    public ResponseEntity<ProfessorDto> ObterPorCelular(@PathVariable("celular") @Valid String celular) {
        ProfessorDto professorDto = professorService.ObterPorCelular(celular);
        try{
            professorDto.validarCelular(celular);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException((e.getMessage()));
        }
        return ResponseEntity.ok(professorDto);
    }

    @PostMapping("/criar")
    public ResponseEntity<ProfessorDto> Criar(@RequestBody @Valid ProfessorModel professorModel) {
        ProfessorDto professorDto = professorModel.ModelToDto(professorModel);
        professorDto.validarMatricula(professorDto.getMatricula());
        professorDto.validarEmail(professorDto.getEmail());
        professorDto.validarCelular(professorDto.getCelular());
        professorDto.validarCpf(professorModel.getCpf());
        professorService.CriarProfessor(professorModel);
        return new ResponseEntity<>(professorDto, HttpStatus.OK);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ProfessorDto> Atualizar(@RequestBody ProfessorModel professorModel) {
        ProfessorDto professorDto = professorModel.ModelToDto(professorModel);
        professorService.AtualizarProfessor(professorModel);
        return new ResponseEntity<>(professorDto, HttpStatus.OK);
    }

    @PutMapping("/atualizar/matricula/{matricula}")
    public ResponseEntity<ProfessorDto> AtualizarMatricula(@RequestBody @Valid ProfessorModel professorModel, @PathVariable("matricula") @Valid String matricula){
        ProfessorDto professorDto = professorService.AtualizarMatricula(professorModel, matricula);
        try{
            professorDto.validarMatricula(matricula);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
        return ResponseEntity.ok(professorDto);
    }

    @PutMapping("/atualizar/celular/{celular}")
    public ResponseEntity<ProfessorDto> AtualizarCelular(@RequestBody @Valid ProfessorModel professorModel, @PathVariable("celular") @Valid String celular){
        ProfessorDto professorDto = professorService.AtualizarCelular(professorModel, celular);
        try{
            professorDto.validarCelular(celular);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
        return ResponseEntity.ok(professorDto);
    }

    @PutMapping("/atualizar/email/{email}")
    public ResponseEntity<ProfessorDto> AtualizarEmail(@RequestBody @Valid ProfessorModel professorModel, @PathVariable("email") @Valid String email){
        ProfessorDto professorDto = professorService.AtualizarEmail(professorModel, email);
        try{
            professorDto.validarEmail(email);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(e.getMessage());
        }
        return ResponseEntity.ok(professorDto);
    }

    @DeleteMapping("/deletar/matricula/{matricula}")
    public ResponseEntity<Void> DeletarPorMatricula(@PathVariable("matricula") String matricula ){
        try{
            ProfessorDto professorDto = new ProfessorDto();
            professorDto.validarMatricula(matricula);
            professorService.DeletarPorMatricula(matricula);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityViolationException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> DeletarPorId(@PathVariable("id") Integer id){
        try{
            professorService.DeletarPorId(id);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/email/{email}")
    public ResponseEntity<Void> DeletarPorEmail(@PathVariable("email") String email ){
        try{
            ProfessorDto professorDto = new ProfessorDto();
            professorDto.validarEmail(email);
            professorService.DeletarPorEmail(email);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityViolationException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/celular/{celular}")
    public ResponseEntity<Void> DeletarPorCelular(@PathVariable("celular") String celular){
        try{
            ProfessorDto professorDto = new ProfessorDto();
            professorDto.validarCelular(celular);
            professorService.DeletarPorCelular(celular);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityViolationException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/cpf/{cpf}")
    public ResponseEntity<Void> DeletarPorCpf(@PathVariable("cpf") String cpf){
        try{
            ProfessorDto professorDto = new ProfessorDto();
            professorDto.validarCpf(cpf);
            professorService.DeletarPorCpf(cpf);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityViolationException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/nome/{nome}")
    public ResponseEntity<Void> DeletarPorNome(@PathVariable("nome") String nome){
        try{
            professorService.DeletarPorNome(nome);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityViolationException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }
}
