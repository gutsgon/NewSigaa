package br.edu.ifs.apinewsigaa.service;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.TurmaModel;
import br.edu.ifs.apinewsigaa.repository.TurmaRepository;
import br.edu.ifs.apinewsigaa.rest.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Transactional(readOnly = true)
    public List<TurmaDto> ObterTurmas(){
        List<TurmaModel> turmaList = turmaRepository.findAll();
        return turmaList.stream()
                .map(turma -> turma.ModelToDto(turma))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurmaDto> ObterTurmaProfessorDisciplina(@Valid int idProfessor, @Valid int idDisciplina){
        Optional<List<TurmaModel>> turmaOptionalList = turmaRepository.findByIdProfessorAndIdDisciplina(idProfessor, idDisciplina);
        List<TurmaModel> turmaModelList = turmaOptionalList.get();
        return turmaModelList.stream()
                .map(turma -> turma.ModelToDto(turma))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurmaDto> ObterTurmaDataInicio(@Valid Date dataInicio){
        List<TurmaModel> turmaList = turmaRepository.findByDataInicio(dataInicio);
        return turmaList.stream()
                .map(turma -> turma.ModelToDto(turma))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurmaDto> ObterTurmaDataFim(@Valid Date dataFim){
        List<TurmaModel> turmaList = turmaRepository.findByDataFim(dataFim);
        return turmaList.stream()
                .map(turma -> turma.ModelToDto(turma))
                .collect(Collectors.toList());
    }

    @Transactional
    public TurmaDto Criar(@Valid TurmaModel turmaModel){
        if(turmaRepository.existsByIdProfessorAndIdDisciplina(turmaModel.getIdProfessor(), turmaModel.getIdDisciplina())) throw new DataIntegrityException("Turma já existe!");
        if(turmaRepository.existsById(turmaModel.getId())) throw new DataIntegrityException("Turma já existe!");
        turmaRepository.save(turmaModel);
        return turmaModel.ModelToDto(turmaModel);
    }

    @Transactional
    public TurmaDto Atualizar(@Valid TurmaModel turmaModel){
        if(turmaRepository.existsById(turmaModel.getId())){
            throw new DataIntegrityException("Turma já existe!");
        }
        turmaRepository.save(turmaModel);
        return turmaModel.ModelToDto(turmaModel);
    }

    @Transactional
    public TurmaDto AtualizarDataInicio(@Valid TurmaModel turmaModel, @Valid Date dataInicio){
        turmaModel.setDataInicio(dataInicio);
        turmaRepository.flush();
        return turmaModel.ModelToDto(turmaModel);
    }

    @Transactional
    public TurmaDto AtualizarDataFim(@Valid TurmaModel turmaModel, @Valid Date dataFim){
        turmaModel.setDataFim(dataFim);
        turmaRepository.flush();
        return turmaModel.ModelToDto(turmaModel);
    }

    @Transactional
    public void DeletarTurmaId(@Valid int id){
        try{
            if (!turmaRepository.existsById(id)) {
                throw new DataIntegrityException("Turma não existe!");
            }
            turmaRepository.deleteById(id);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("ERRO: Turma não encontrada!");
        }
    }

    @Transactional
    public void DeletarTurmaProfessorDisciplina(@Valid int idProfessor, int idDisciplina){
        try{
            if (!turmaRepository.existsByIdProfessorAndIdDisciplina(idProfessor, idDisciplina)) {
                throw new DataIntegrityException("Turma não existe!");
            }
            turmaRepository.deleteByIdProfessorAndIdDisciplina(idProfessor, idDisciplina);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("ERRO: Turma não encontrada!");
        }
    }
}
