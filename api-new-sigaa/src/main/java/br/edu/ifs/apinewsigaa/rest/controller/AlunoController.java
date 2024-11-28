package br.edu.ifs.apinewsigaa.rest.controller;


import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.AlunoModel;
import br.edu.ifs.apinewsigaa.rest.dto.AlunoDto;
import br.edu.ifs.apinewsigaa.service.AlunoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluno")
@SecurityRequirement(name = "Keycloak")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    //@GetMapping, @PostMapping, @PutMapping, @DeleteMapping


    @GetMapping
    public ResponseEntity<List<AlunoDto>> ObterAlunosAsc() {
        List<AlunoDto> alunoDtoList = alunoService.ObterAlunosAsc();
        return ResponseEntity.ok(alunoDtoList);
    }

    @GetMapping("/buscar/nome/{nome}")
    public ResponseEntity<List<AlunoDto>> ObterAlunosContendoNome(@PathVariable("nome") String nome) {
        List<AlunoDto> alunoDtoList = alunoService.ObterAlunosContendoNome(nome);
        return ResponseEntity.ok(alunoDtoList);
    }

    @GetMapping("/buscar/matricula/{matricula}")
    public ResponseEntity<AlunoDto> ObterPorMatricula(@PathVariable("matricula") String matricula) {
        AlunoDto alunoDto = alunoService.ObterPorMatricula(matricula);
        /*try{
            alunoDto.validarMatricula(matricula);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Matricula inválida");
        }*/
        return ResponseEntity.ok(alunoDto);
    }

    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<AlunoDto> ObterPorEmail(@PathVariable("email") String email) {
        AlunoDto alunoDto = alunoService.ObterPorEmail(email);
        try{
            alunoDto.validarEmail(email);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Matricula inválida");
        }
        return ResponseEntity.ok(alunoDto);
    }

    @GetMapping("/buscar/celular/{celular}")
    public ResponseEntity<AlunoDto> ObterPorCelular(@PathVariable("celular") String celular) {
        AlunoDto alunoDto = alunoService.ObterPorCelular(celular);
        try{
            alunoDto.validarCelular(celular);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Matricula inválida");
        }
        return ResponseEntity.ok(alunoDto);
    }

    @PostMapping("/criar")
    public ResponseEntity<AlunoDto> Criar(@RequestBody @Valid @Validated AlunoModel alunoModel) {
        AlunoDto alunoDto = alunoModel.ModelToDto();
        try{
            alunoDto.validarCelular(alunoDto.getCelular());
            alunoDto.validarEmail(alunoDto.getEmail());
            alunoDto.validarMatricula(alunoDto.getMatricula());
            alunoDto.validarCpf(alunoModel.getCpf());
        }catch (DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
        alunoService.CriarAluno(alunoModel);
        return new ResponseEntity<>(alunoDto, HttpStatus.OK);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<AlunoDto> Atualizar(@RequestBody @Valid AlunoModel alunoModel) {
        AlunoDto alunoDto = alunoModel.ModelToDto();
        try{
            alunoDto.validarCelular(alunoDto.getCelular());
            alunoDto.validarEmail(alunoDto.getEmail());
            alunoDto.validarMatricula(alunoDto.getMatricula());
            alunoDto.validarCpf(alunoModel.getCpf());
        }catch (DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
        alunoService.AtualizarAluno(alunoModel);
        return new ResponseEntity<>(alunoDto, HttpStatus.OK);
    }


    @PutMapping("/atualizar/matricula/{matricula}")
    public ResponseEntity<AlunoDto> AtualizarMatricula(@RequestBody @Valid  AlunoModel alunoModel, @PathVariable("matricula") String matricula){
        AlunoDto alunoDto = alunoService.AtualizarMatricula(alunoModel, matricula);
        try{
            alunoDto.validarMatricula(matricula);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Matricula inválida");
        }
        return ResponseEntity.ok(alunoDto);
    }

    @PutMapping("/atualizar/celular/{celular}")
    public ResponseEntity<AlunoDto> AtualizarCelular(@RequestBody @Valid AlunoModel alunoModel, @PathVariable("celular") String celular){
        AlunoDto alunoDto = alunoService.AtualizarCelular(alunoModel, celular);
        try{
            alunoDto.validarCelular(celular);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Celular inválido");
        }
        return ResponseEntity.ok(alunoDto);
    }

    @PutMapping("/atualizar/email/{email}")
    public ResponseEntity<AlunoDto> AtualizarEmail(@RequestBody @Valid AlunoModel alunoModel, @PathVariable("email") String email){
       AlunoDto alunoDto = alunoService.AtualizarEmail(alunoModel, email);
        try{
            alunoDto.validarEmail(email);
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Email inválido");
        }
        return ResponseEntity.ok(alunoDto);
    }

    @PutMapping("/atualizar/apelido/{apelido}")
    public ResponseEntity<AlunoDto> AtualizarApelido(@RequestBody @Valid AlunoModel alunoModel, @PathVariable("apelido") String apelido){
        AlunoDto alunoDto = alunoService.AtualizarApelido(alunoModel, apelido);
        return ResponseEntity.ok(alunoDto);
    }

    @DeleteMapping("/deletar/matricula/{matricula}")
    public ResponseEntity<Void> DeletarPorMatricula(@PathVariable("matricula") String matricula ){
        try{
            AlunoDto alunoDto = new AlunoDto();
            alunoDto.validarMatricula(matricula);
            alunoService.DeletarPorMatricula(matricula);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> Deletar(@PathVariable("id") Integer id){
        try{
            alunoService.DeletarPorId(id);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/deletar/email/{email}")
    public ResponseEntity<Void> DeletarPorEmail(@PathVariable("email") String email ){
        try{
            AlunoDto alunoDto = new AlunoDto();
            alunoDto.validarEmail(email);
            alunoService.DeletarPorEmail(email);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/celular/{celular}")
    public ResponseEntity<Void> DeletarPorCelular(@PathVariable("celular") String celular){
        try{
            AlunoDto alunoDto = new AlunoDto();
            alunoDto.validarCelular(celular);
            alunoService.DeletarPorCelular(celular);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/cpf/{cpf}")
    public ResponseEntity<Void> DeletarPorCpf(@PathVariable("cpf") String cpf){
        try{
            AlunoDto alunoDto = new AlunoDto();
            alunoDto.validarCpf(cpf);
            alunoService.DeletarPorCpf(cpf);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @DeleteMapping("/deletar/nome/{nome}")
    public ResponseEntity<Void> DeletarPorNome(@PathVariable("nome") String nome){
        try{
            alunoService.DeletarPorNome(nome);
            return ResponseEntity.noContent().build();
        }catch(ResourceNotFoundException | DataIntegrityException e){
            throw new DataIntegrityException(e.getMessage());
        }
    }



}
