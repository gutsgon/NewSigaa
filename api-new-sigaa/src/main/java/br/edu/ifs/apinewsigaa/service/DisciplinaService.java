package br.edu.ifs.apinewsigaa.service;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ObjectNotFoundException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.DisciplinaModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunosDisciplinaProjection;
import br.edu.ifs.apinewsigaa.repository.DisciplinaRepository;
import br.edu.ifs.apinewsigaa.rest.dto.AlunoDto;
import br.edu.ifs.apinewsigaa.rest.dto.DisciplinaComAlunosDTO;
import br.edu.ifs.apinewsigaa.rest.dto.DisciplinaDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasAsc(){
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaList = disciplinaRepository.findByOrderByNomeAsc();
        return disciplinaList.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasDesc(){
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaList = disciplinaRepository.findByOrderByNomeDesc();
        return disciplinaList.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasNomeContendo(@Valid String nome){
        if(!disciplinaRepository.existsByNome(nome)) throw new ObjectNotFoundException("Disciplina não existe!");
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaModel = disciplinaRepository.findByNomeContains(nome);

        return disciplinaModel.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DisciplinaDto ObterDisciplinaNome(@Valid String nome){
        if(!disciplinaRepository.existsByNome(nome)) throw new ObjectNotFoundException("Disciplina não existe!");
        ModelMapper modelMapper = new ModelMapper();
        Optional<DisciplinaModel> disciplinaModelOptional = disciplinaRepository.findByNome(nome);
        disciplinaModelOptional.orElseThrow(() -> new ObjectNotFoundException("Disciplina não encontrada!"));
        return modelMapper.map(disciplinaModelOptional, DisciplinaDto.class);
    }


    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasNumeroCreditosAsc(){
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaList = disciplinaRepository.findByOrderByNumeroCreditosAsc();
        return disciplinaList.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasNumeroCreditosDesc(){
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaList = disciplinaRepository.findByOrderByNumeroCreditosDesc();
        return disciplinaList.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDto> ObterDisciplinasNumeroCreditos(@Valid int numeroCreditos){
        if(!disciplinaRepository.existsByNumeroCreditos(numeroCreditos)){
            throw new ObjectNotFoundException("Não há nenhuma disciplina com "+ numeroCreditos + " número de créditos");
        }
        ModelMapper modelMapper = new ModelMapper();
        List<DisciplinaModel> disciplinaList = disciplinaRepository.findByNumeroCreditos(numeroCreditos);
        return disciplinaList.stream()
                .map(disciplina -> modelMapper.map(disciplina, DisciplinaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DisciplinaComAlunosDTO ObterAlunos(@Valid int idDisciplina){
        List<AlunosDisciplinaProjection> alunoProjection = disciplinaRepository.ObterAlunosDeDisciplina(idDisciplina);
        List<AlunoDto> alunoDto = AlunoDto.ProjectionToDto(alunoProjection);
        Optional<DisciplinaModel> disciplinaModel = disciplinaRepository.findById(idDisciplina);
        DisciplinaDto disciplinaDto = disciplinaModel.get().ModelToDto(disciplinaModel.get());
        return new DisciplinaComAlunosDTO(disciplinaDto, alunoDto);
    }

    @Transactional
    public DisciplinaDto CriarDisciplina(@Valid DisciplinaModel disciplinaModel){
        if(disciplinaRepository.existsByNome(disciplinaModel.getNome())){
            throw new DataIntegrityException("Disciplina já existe!");
        }
        if(disciplinaRepository.existsById(disciplinaModel.getId())){
            throw new DataIntegrityException("Disciplina já existe!");
        }
        disciplinaRepository.save(disciplinaModel);
        return disciplinaModel.ModelToDto(disciplinaModel);
    }

    @Transactional
    public DisciplinaDto AtualizarDisciplina(@Valid DisciplinaModel disciplinaModel){
        if(disciplinaRepository.existsByNome(disciplinaModel.getNome())){
            throw new DataIntegrityException("Disciplina já existe!");
        }
        disciplinaRepository.save(disciplinaModel);
        return disciplinaModel.ModelToDto(disciplinaModel);
    }

    @Transactional
    public DisciplinaDto AtualizarNome(@Valid DisciplinaModel disciplinaModel){
        if(disciplinaRepository.existsByNome(disciplinaModel.getNome())){
            throw new DataIntegrityException("Já existe um disciplina com esse nome!");
        }
        disciplinaModel.setNome(disciplinaModel.getNome());
        disciplinaRepository.flush();
        return disciplinaModel.ModelToDto(disciplinaModel);
    }

    @Transactional
    public DisciplinaDto AtualizarNumeroCreditos(@Valid DisciplinaModel disciplinaModel){
        if(!disciplinaRepository.existsByNome(disciplinaModel.getNome())){
            throw new DataIntegrityException("Disciplina não existe!");
        }
        disciplinaModel.setNumeroCreditos(disciplinaModel.getNumeroCreditos());
        disciplinaRepository.flush();
        return disciplinaModel.ModelToDto(disciplinaModel);
    }

    @Transactional
    public void DeletarDisciplinaNome(@Valid String nome){
        try {
            if (!disciplinaRepository.existsByNome(nome)) {
                throw new DataIntegrityException("Disciplina não existe!");
            }
            disciplinaRepository.deleteByNome(nome);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("ERRO: Disciplina de nome: "+ nome + " não encontrada!");
        }
    }

    @Transactional
    public void DeletarDisciplinaId(@Valid int id){
        try{
            if (!disciplinaRepository.existsById(id)) {
                throw new DataIntegrityException("Disciplina não existe!");
            }
            disciplinaRepository.deleteById(id);
        }catch(ResourceNotFoundException e){
            throw new ResourceNotFoundException("ERRO: Disciplina não encontrada!");
        }
    }
}
