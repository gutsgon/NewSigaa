package br.edu.ifs.apinewsigaa.service;

import br.edu.ifs.apinewsigaa.exception.ObjectNotFoundException;
import br.edu.ifs.apinewsigaa.model.MatriculaModel;
import br.edu.ifs.apinewsigaa.repository.MatriculaRepository;
import br.edu.ifs.apinewsigaa.rest.dto.MatriculaDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Transactional(readOnly = true)
    public MatriculaDto ObterMatricula(@Valid int idTurma, @Valid int idAluno) {
        if(!matriculaRepository.existsByIdTurmaAndIdAluno(idTurma, idAluno)) throw new ObjectNotFoundException("Matricula não existe!");
        Optional<MatriculaModel> matriculaOptional = matriculaRepository.findByIdTurmaAndIdAluno(idTurma, idAluno);
        MatriculaModel matriculaModel = matriculaOptional.get();
        return matriculaModel.ModelToDto(matriculaModel);
    }

    @Transactional
    public void DeletarMatricula(@Valid MatriculaModel matriculaModel) {
        if(!matriculaRepository.existsByIdTurmaAndIdAluno(matriculaModel.getIdTurma(), matriculaModel.getIdAluno())){
            throw new ObjectNotFoundException("Matricula não existe!");
        }
        matriculaRepository.deleteByIdTurmaAndIdAluno(matriculaModel.getIdTurma(), matriculaModel.getIdAluno());
    }

    @Transactional
    public MatriculaDto AtualizarMatricula(@Valid MatriculaModel matriculaModel) {
        matriculaRepository.save(matriculaModel);
        return matriculaModel.ModelToDto(matriculaModel);
    }

    @Transactional
    public MatriculaDto CriarMatricula(@Valid MatriculaModel matriculaModel) {
        matriculaRepository.save(matriculaModel);
        return matriculaModel.ModelToDto(matriculaModel);
    }
}
