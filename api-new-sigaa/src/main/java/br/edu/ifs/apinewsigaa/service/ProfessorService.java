package br.edu.ifs.apinewsigaa.service;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ObjectNotFoundException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.model.ProfessorModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunosDisciplinaProjection;
import br.edu.ifs.apinewsigaa.model.projection.DisciplinaProfessorProjection;
import br.edu.ifs.apinewsigaa.model.projection.ProfessorDisciplinaAlunoProjection;
import br.edu.ifs.apinewsigaa.repository.ProfessorRepository;
import br.edu.ifs.apinewsigaa.rest.dto.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to professors.
 * Provides methods for CRUD operations and other operations related to professors.
 */

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaService disciplinaService;

    /**
     * Retrieves a list of professors ordered by their names in ascending order.
     *
     * @return a list of {@link ProfessorDto} objects representing professors.
     */
    @Transactional(readOnly = true)
    public List<ProfessorDto> ObterProfessorAsc() {
        List<ProfessorModel> professorList = professorRepository.findByOrderByNomeAsc();
        ModelMapper modelMapper = new ModelMapper();
        return professorList.stream()
                .map(professor -> modelMapper.map(professor, ProfessorDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessorDisciplinasDTO ObterDisciplinasProfessor(@Valid String matricula){
        List<DisciplinaProfessorProjection> disciplinaProfessorProjectionList = professorRepository.ObterDisciplinasProfessor(matricula);

        if(disciplinaProfessorProjectionList.isEmpty()){
            throw new ObjectNotFoundException("Nenhuma disciplina para esse professor!");
        }

        if(!professorRepository.existsByMatricula(matricula)) throw new ObjectNotFoundException("Matricula não existe!");

        ProfessorDto professorDto = ProfessorDto.ProjectionToDto(disciplinaProfessorProjectionList.get(0));
        List<DisciplinaDto> disciplinaDto = disciplinaProfessorProjectionList.stream()
                .map((DisciplinaProfessorProjection disciplinaProfessorProjection) -> DisciplinaDto.ProjectionToDto(disciplinaProfessorProjectionList))
                .collect(Collectors.toList());
        ProfessorDisciplinasDTO professorDisciplinasDTO = new ProfessorDisciplinasDTO();
        professorDisciplinasDTO.setProfessor(professorDto);
        professorDisciplinasDTO.setDisciplinas(disciplinaDto);
        if(disciplinaProfessorProjectionList.isEmpty()){
            throw new ObjectNotFoundException("Nenhuma disciplina para esse professor!");
        }
        return professorDisciplinasDTO;
    }

    @Transactional(readOnly = true)
    public ProfessorDisciplinasAlunosDTO ObterDisciplinasProfessorAluno(@Valid String matricula){
        List<ProfessorDisciplinaAlunoProjection> professorDisciplinaAlunoProjection = professorRepository.ObterDisciplinasProfessorAluno(matricula);
        if(professorDisciplinaAlunoProjection.isEmpty()){
            throw new ObjectNotFoundException("Nenhuma disciplina para esse professor!");
        }

        if(!professorRepository.existsByMatricula(matricula)) throw new ObjectNotFoundException("Matricula não existe!");


        List<DisciplinaComAlunosDTO> disciplinaComAluno = new ArrayList<>();
       for(ProfessorDisciplinaAlunoProjection projection: professorDisciplinaAlunoProjection){
           DisciplinaComAlunosDTO disciplinaComAlunosDTO = disciplinaService.ObterAlunos(projection.getIdDisciplina());
           DisciplinaComAlunosDTO disciplina = disciplinaComAluno.stream()
                   .filter(d -> d.getDisciplina().equals(disciplinaComAlunosDTO.getDisciplina()))
                   .findFirst()
                   .orElse(null);
           if(disciplina == null){
               disciplinaComAluno.add(disciplinaComAlunosDTO);
           }
           /*// Criar DTO da disciplina
           DisciplinaDto disciplinaDTO = new DisciplinaDto();
           disciplinaDTO.setId(projection.getIdDisciplina());
           disciplinaDTO.setNome(projection.getNomeDisciplina());
           disciplinaDTO.setNumeroCreditos(projection.getNumeroCreditos());

           // Tentar encontrar a disciplina na lista
           DisciplinaComAlunosDTO disciplinaComAlunosDTO = disciplinaComAluno.stream()
                   .filter(d -> d.getDisciplina().equals(disciplinaDTO))
                   .findFirst()
                   .orElse(null);

           if (disciplinaComAlunosDTO == null) {
               // Se a disciplina não está na lista, criar nova entrada
               disciplinaComAlunosDTO = new DisciplinaComAlunosDTO();
               disciplinaComAlunosDTO.setDisciplina(disciplinaDTO);
               disciplinaComAlunosDTO.setAlunos(new ArrayList<>());
               disciplinaComAluno.add(disciplinaComAlunosDTO);
           }

           // Criar DTO do aluno
           AlunoDto alunoDTO = new AlunoDto();
           alunoDTO.setId(projection.getIdAluno());
           alunoDTO.setMatricula(projection.getMatriculaAluno());
           alunoDTO.setNome(projection.getNomeAluno());
           alunoDTO.setCpf(projection.getCpfAluno());
           alunoDTO.setEmail(projection.getEmailAluno());
           alunoDTO.setDataNascimento(projection.getDataNascimentoAluno());
           alunoDTO.setApelido(projection.getApelidoAluno());
           alunoDTO.setCelular(projection.getCelularAluno());

           // Adicionar aluno se não estiver presente
           if (!disciplinaComAlunosDTO.getAlunos().contains(alunoDTO)) {
               disciplinaComAlunosDTO.getAlunos().add(alunoDTO);
           }*/
       }

        ProfessorDto professorDto = ProfessorDto.ProjectionToDtoAluno(professorDisciplinaAlunoProjection.get(0));

        ProfessorDisciplinasAlunosDTO professorDisciplinasAlunosDTO = new ProfessorDisciplinasAlunosDTO();
        professorDisciplinasAlunosDTO.setProfessor(professorDto);
        professorDisciplinasAlunosDTO.setDisciplinas(disciplinaComAluno);

        if(professorDisciplinaAlunoProjection.isEmpty()){
            throw new ObjectNotFoundException("Nenhuma disciplina para esse professor!");
        }
        return professorDisciplinasAlunosDTO;
    }

    /**
     * Retrieves a list of professors whose names contain the specified substring.
     *
     * @param nome the substring to search for in professor names.
     * @return a list of {@link ProfessorDto} objects representing professors.
     */
    @Transactional(readOnly = true)
    public List<ProfessorDto> ObterProfessorContendoNome(@Valid String nome) {
        List<ProfessorModel> professorList = professorRepository.findByNomeContains(nome);
        ModelMapper modelMapper = new ModelMapper();
        return professorList.stream()
                .map(professor -> modelMapper.map(professor, ProfessorDto.class))
                .collect(Collectors.toList());
    }


    /**
     * Retrieves a professor by their matricula.
     *
     * @param matricula the matricula of the professor to retrieve.
     * @return a {@link ProfessorDto} object representing the professor.
     * @throws DataIntegrityException if the matricula is empty or does not exist.
     * @throws ObjectNotFoundException if the professor is not found.
     */
    @Transactional(readOnly = true)
    public ProfessorDto ObterPorMatricula(@Valid String matricula) {
        if (matricula.isEmpty()) {
            throw new DataIntegrityException("ERRO: Digite uma matrícula para pesquisar!");
        }
        if (!professorRepository.existsByMatricula(matricula)) {
            throw new DataIntegrityException("ERRO: A matricula não existe!");
        }
        Optional<ProfessorModel> professorOptional = professorRepository.findByMatricula(matricula);
        ProfessorModel professorModel = professorOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Professor não encontrado!"));
        return professorModel.ModelToDto(professorModel);
    }

    /**
     * Retrieves a professor by their email.
     *
     * @param email the email of the professor to retrieve.
     * @return a {@link ProfessorDto} object representing the professor.
     * @throws DataIntegrityException if the email is empty or does not exist.
     * @throws ObjectNotFoundException if the professor is not found.
     */
    @Transactional(readOnly = true)
    public ProfessorDto ObterPorEmail(@Valid String email) {
        if (email.isEmpty()) {
            throw new DataIntegrityException("ERRO: Digite um email para pesquisar!");
        }
        if (!professorRepository.existsByEmail(email)) {
            throw new DataIntegrityException("ERRO: O email não existe!");
        }
        Optional<ProfessorModel> professorOptional = professorRepository.findByEmail(email);
        ProfessorModel professorModel = professorOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Professor não encontrado!"));
        return professorModel.ModelToDto(professorModel);
    }

    /**
     * Retrieves a professor by their cellphone number.
     *
     * @param celular the cellphone number of the professor to retrieve.
     * @return a {@link ProfessorDto} object representing the professor.
     * @throws DataIntegrityException if the cellphone number is empty or does not exist.
     * @throws ObjectNotFoundException if the professor is not found.
     */
    @Transactional(readOnly = true)
    public ProfessorDto ObterPorCelular(@Valid String celular) {
        if (celular.isEmpty()) {
            throw new DataIntegrityException("ERRO: Digite um celular para pesquisar!");
        }
        if (!professorRepository.existsByCelular(celular)) {
            throw new DataIntegrityException("ERRO: O celular não existe!");
        }
        Optional<ProfessorModel> professorOptional = professorRepository.findByCelular(celular);
        ProfessorModel professorModel = professorOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Professor não encontrado!"));
        return professorModel.ModelToDto(professorModel);
    }

    /**
     * Creates a new professor record.
     *
     * @param professorModel the {@link ProfessorModel} object to be created.
     * @return a {@link ProfessorDto} object representing the created professor.
     * @throws DataIntegrityException if a professor with the same matricula, email, celular, cpf, or id already exists.
     */
    @Transactional
    public ProfessorDto CriarProfessor(@Valid ProfessorModel professorModel) {
        if (professorRepository.existsByMatricula(professorModel.getMatricula())) {
            throw new DataIntegrityException("ERRO: A matricula já existe!");
        }
        if (professorRepository.existsByEmail(professorModel.getEmail())) {
            throw new DataIntegrityException("ERRO: O email já existe!");
        }
        if (professorRepository.existsByCelular(professorModel.getCelular())) {
            throw new DataIntegrityException("ERRO: O celular já existe!");
        }
        if (professorRepository.existsByCpf(professorModel.getCpf())) {
            throw new DataIntegrityException("ERRO: O cpf já existe!");
        }
        if (professorRepository.existsById(professorModel.getId())) {
            throw new DataIntegrityException("ERRO: O professor já existe!");
        }
        professorRepository.save(professorModel);
        return professorModel.ModelToDto(professorModel);
    }

    /**
     * Updates an existing professor record.
     *
     * @param professorModel the {@link ProfessorModel} object containing updated data.
     * @return a {@link ProfessorDto} object representing the updated professor.
     * @throws DataIntegrityException if a professor with the same matricula, email, celular, or cpf already exists.
     * @throws IllegalArgumentException if the professorModel is null.
     */
    @Transactional
    public ProfessorDto AtualizarProfessor(@Valid ProfessorModel professorModel) {
        if (professorRepository.existsByMatricula(professorModel.getMatricula())) {
            throw new DataIntegrityException("ERRO: A matricula já existe!");
        }
        if (professorRepository.existsByEmail(professorModel.getEmail())) {
            throw new DataIntegrityException("ERRO: O email já existe!");
        }
        if (professorRepository.existsByCelular(professorModel.getCelular())) {
            throw new DataIntegrityException("ERRO: O celular já existe!");
        }
        if (professorRepository.existsByCpf(professorModel.getCpf())) {
            throw new DataIntegrityException("ERRO: O cpf já existe!");
        }
        if (professorModel == null) {
            throw new DataIntegrityException("ERRO: Deve ter ao menos um professor para ser enviado!");
        }
        professorRepository.save(professorModel);
        return professorModel.ModelToDto(professorModel);
    }

    /**
     * Updates the matricula of a professor.
     *
     * @param professorModel the {@link ProfessorModel} object containing updated data.
     * @param matricula      the new matricula to be set.
     * @return a {@link ProfessorDto} object representing the updated professor.
     * @throws ObjectNotFoundException if the professor with the provided matricula already exists.
     */
    @Transactional
    public ProfessorDto AtualizarMatricula(@Valid ProfessorModel professorModel, String matricula) {
        if (professorRepository.existsByMatricula(matricula)) {
            throw new ObjectNotFoundException("ERRO: Professor já existe com essa matrícula!");
        } else {
            Optional<ProfessorModel> professorOptional = professorRepository.findByCpf(professorModel.getCpf());
            professorModel = professorOptional.get();
            professorModel.setMatricula(matricula);
            professorRepository.flush();
            return professorModel.ModelToDto(professorModel);
        }
    }

    /**
     * Updates the email of a professor.
     *
     * @param professorModel the {@link ProfessorModel} object containing updated data.
     * @param email          the new email to be set.
     * @return a {@link ProfessorDto} object representing the updated professor.
     * @throws ObjectNotFoundException if the professor with the provided email already exists.
     */
    @Transactional
    public ProfessorDto AtualizarEmail(@Valid ProfessorModel professorModel, String email) {
        if (professorRepository.existsByEmail(email)) {
            throw new ObjectNotFoundException("ERRO: Professor já existe com esse email!");
        } else {
            Optional<ProfessorModel> professorOptional = professorRepository.findByCpf(professorModel.getCpf());
            professorModel = professorOptional.get();
            professorModel.setEmail(email);
            professorRepository.flush();
            return professorModel.ModelToDto(professorModel);
        }
    }

    /**
     * Updates the cellphone number of a professor.
     *
     * @param professorModel the {@link ProfessorModel} object containing updated data.
     * @param celular        the new cellphone number to be set.
     * @return a {@link ProfessorDto} object representing the updated professor.
     * @throws ObjectNotFoundException if the professor with the provided cellphone number already exists.
     */
    @Transactional
    public ProfessorDto AtualizarCelular(@Valid ProfessorModel professorModel, String celular) {
        if (professorRepository.existsByCelular(celular)) {
            throw new ObjectNotFoundException("ERRO: Professor já existe com esse celular!");
        } else {
            Optional<ProfessorModel> professorOptional = professorRepository.findByCpf(professorModel.getCpf());
            professorModel = professorOptional.get();
            professorModel.setCelular(celular);
            professorRepository.flush();
            return professorModel.ModelToDto(professorModel);
        }
    }

    /**
     * Deletes a professor by their ID.
     *
     * @param id the ID of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided ID.
     */
    @Transactional
    public void DeletarPorId(@Valid Integer id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com ID " + id);
        }
    }

    /**
     * Deletes a professor by their email.
     *
     * @param email the email of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided email.
     */
    @Transactional
    public void DeletarPorEmail(@Valid String email) {
        if (professorRepository.existsByEmail(email)) {
            professorRepository.deleteByEmail(email);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com email " + email);
        }
    }

    /**
     * Deletes a professor by their matricula.
     *
     * @param matricula the matricula of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided matricula.
     */
    @Transactional
    public void DeletarPorMatricula(@Valid String matricula) {
        if (professorRepository.existsByMatricula(matricula)) {
            professorRepository.deleteByMatricula(matricula);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com matricula " + matricula);
        }
    }

    /**
     * Deletes a professor by their cellphone number.
     *
     * @param celular the cellphone number of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided cellphone number.
     */
    @Transactional
    public void DeletarPorCelular(@Valid String celular) {
        if (professorRepository.existsByCelular(celular)) {
            professorRepository.deleteByCelular(celular);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com celular " + celular);
        }
    }

    /**
     * Deletes a professor by their CPF.
     *
     * @param cpf the CPF of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided CPF.
     */
    @Transactional
    public void DeletarPorCpf(@Valid String cpf) {
        if (professorRepository.existsByCpf(cpf)) {
            professorRepository.deleteByCpf(cpf);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com CPF " + cpf);
        }
    }

    /**
     * Deletes a professor by their name.
     *
     * @param nome the name of the professor to delete.
     * @throws ResourceNotFoundException if no professor is found with the provided name.
     */
    @Transactional
    public void DeletarPorNome(@Valid String nome) {
        if (professorRepository.existsByNome(nome)) {
            professorRepository.deleteByNome(nome);
            System.out.println("Professor deletado com sucesso!");
        } else {
            throw new ResourceNotFoundException("ERRO: Professor não encontrado com nome " + nome);
        }
    }
}
