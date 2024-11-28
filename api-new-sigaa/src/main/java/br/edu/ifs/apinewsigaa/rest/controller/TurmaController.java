package br.edu.ifs.apinewsigaa.rest.controller;

import br.edu.ifs.apinewsigaa.model.TurmaModel;
import br.edu.ifs.apinewsigaa.rest.dto.TurmaDto;
import br.edu.ifs.apinewsigaa.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/turma")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<List<TurmaDto>> ObterTurmas(){
        List<TurmaDto> turmaDtoList = turmaService.ObterTurmas();
        return ResponseEntity.ok(turmaDtoList);
    }

    @GetMapping("/buscar/professorDisciplina/{idProfessor}/{idDisciplina}")
    public ResponseEntity<List<TurmaDto>> ObterTurmasProfessorDisciplina(@PathVariable("idProfessor") @Valid int idProfessor,@PathVariable("idDisciplina") @Valid int idDisciplina){
        List<TurmaDto> turmaDtoList = turmaService.ObterTurmaProfessorDisciplina(idProfessor,idDisciplina);
        return ResponseEntity.ok(turmaDtoList);
    }

    @GetMapping("/buscar/dataInicio/{dataInicio}")
    public ResponseEntity<List<TurmaDto>> ObterTurmasDataInicio(@Valid @PathVariable("dataInicio") Date dataInicio){
        List<TurmaDto> turmaDtoList = turmaService.ObterTurmaDataInicio(dataInicio);
        return ResponseEntity.ok(turmaDtoList);
    }

    @GetMapping("/buscar/dataInicio/{dataFim}")
    public ResponseEntity<List<TurmaDto>> ObterTurmasDataFim(@Valid @PathVariable("dataFim") Date dataFim){
        List<TurmaDto> turmaDtoList = turmaService.ObterTurmaDataFim(dataFim);
        return ResponseEntity.ok(turmaDtoList);
    }

    @PostMapping("/criar")
    public ResponseEntity<TurmaDto> CriarTurma(@RequestBody @Valid TurmaModel turmaModel){
        turmaService.Criar(turmaModel);
        TurmaDto turmaDto = turmaModel.ModelToDto(turmaModel);
        return ResponseEntity.ok(turmaDto);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<TurmaDto> AtualizarTurma(@RequestBody @Valid TurmaModel turmaModel){
        turmaService.Atualizar(turmaModel);
        TurmaDto turmaDto = turmaModel.ModelToDto(turmaModel);
        return ResponseEntity.ok(turmaDto);
    }

    @PutMapping("/atualizar/dataInicio/{dataInicio}")
    public ResponseEntity<TurmaDto> AtualizarTurmaDataInicio(@RequestBody TurmaModel turmaModel, @Valid @PathVariable("dataInicio") Date dataInicio ){
        turmaService.AtualizarDataInicio(turmaModel, dataInicio);
        TurmaDto turmaDto = turmaModel.ModelToDto(turmaModel);
        return ResponseEntity.ok(turmaDto);
    }

    @PutMapping("/atualizar/dataInicio/{dataFim}")
    public ResponseEntity<TurmaDto> AtualizarTurmaDataFim(@RequestBody TurmaModel turmaModel, @Valid @PathVariable("dataFim") Date dataFim ){
        turmaService.AtualizarDataFim(turmaModel, dataFim);
        TurmaDto turmaDto = turmaModel.ModelToDto(turmaModel);
        return ResponseEntity.ok(turmaDto);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> DeletarTurma(@Valid @PathVariable("id") int id){
        turmaService.DeletarTurmaId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar/professorDisciplina/{idProfessor}/{idDisciplina}")
    public ResponseEntity<Void> DeletarTurmaProfessorDisciplina(@Valid @PathVariable("idProfessor") int idProfessor, @Valid @PathVariable("idDisciplina") int idDisciplina){
        turmaService.DeletarTurmaProfessorDisciplina(idProfessor, idDisciplina);
        return ResponseEntity.noContent().build();
    }
}
