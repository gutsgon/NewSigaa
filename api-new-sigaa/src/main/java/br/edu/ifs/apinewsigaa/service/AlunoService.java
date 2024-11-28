package br.edu.ifs.apinewsigaa.service;

import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ObjectNotFoundException;
import br.edu.ifs.apinewsigaa.exception.ResourceNotFoundException;
import br.edu.ifs.apinewsigaa.exception.StandardError;
import br.edu.ifs.apinewsigaa.model.AlunoModel;
import br.edu.ifs.apinewsigaa.repository.AlunoRepository;
import br.edu.ifs.apinewsigaa.rest.dto.AlunoDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * Obtém um aluno pelo número de matrícula.
     *
     * @param matricula O número de matrícula do aluno a ser obtido.
     * @return Um objeto AlunoDto correspondente ao aluno encontrado.
     * @throws ObjectNotFoundException Caso o aluno não seja encontrado no banco de dados.
     */
    public AlunoDto ObterPorMatricula(String matricula) {
        if(matricula.isEmpty()){
            throw new DataIntegrityException("ERRO: Digite uma matrícula para pesquisar!");
        }
        if(!alunoRepository.existsByMatricula(matricula)){
            throw new DataIntegrityException("ERRO: A matricula não existe!");
        }
        Optional<AlunoModel> alunoOptional = alunoRepository.findByMatricula(matricula);
        AlunoModel alunoModel = alunoOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Matrícula não encontrada! Matrícula: " + matricula));
        return alunoModel.ModelToDto();

        /*
        AlunoDto alunoDto = new AlunoDto();
        alunoDto.setMatricula(alunoModel.getMatricula());
        alunoDto.setNome(alunoModel.getNome());
        alunoDto.setApelido(alunoModel.getApelido());
        alunoDto.setEmail(alunoModel.getEmail());
        alunoDto.setCpf(alunoModel.getCpf());
        alunoDto.setCelular(alunoModel.getCelular());
        alunoDto.setDataNascimento(alunoModel.getDataNascimento());

        return alunoDto;
        */

    }

    /**
     * Obtém todos os alunos cadastrados.
     *
     * @return Uma lista de objetos AlunoDto correspondentes aos alunos cadastrados.
     */
    @Transactional(readOnly = true)
    public List<AlunoDto> ObterAlunosAsc() {
        List<AlunoModel> alunoList = alunoRepository.findByOrderByNomeAsc();
        ModelMapper modelMapper = new ModelMapper();
        return alunoList.stream()
                .map(aluno -> modelMapper.map(aluno, AlunoDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlunoDto> ObterAlunosContendoNome(@Valid String nome) {
        if(nome.isEmpty()){
            throw new DataIntegrityException("ERRO: Digite uma matrícula para pesquisar!");
        }
        if(!alunoRepository.existsByMatricula(nome)){
            throw new DataIntegrityException("ERRO: O nome não existe!");
        }
        List<AlunoModel> alunoList = alunoRepository.findByNomeContains(nome);
        ModelMapper modelMapper = new ModelMapper();
        return alunoList.stream()
                .map(aluno -> modelMapper.map(aluno, AlunoDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoDto ObterPorEmail(@Valid String email){
        if(email.isEmpty()){
            throw new DataIntegrityException("ERRO: Digite uma matrícula para pesquisar!");
        }
        if(!alunoRepository.existsByMatricula(email)){
            throw new DataIntegrityException("ERRO: O email não existe!");
        }
        Optional<AlunoModel> alunoOptional = alunoRepository.findByEmail(email);
        AlunoModel alunoModel = alunoOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Email não encontrado ou errado! :" + email));
        return alunoModel.ModelToDto();
    }

    @Transactional(readOnly = true)
    public AlunoDto ObterPorCelular(@Valid String celular){
        if(celular.isEmpty()){
            throw new DataIntegrityException("ERRO: Digite uma matrícula para pesquisar!");
        }
        if(!alunoRepository.existsByMatricula(celular)){
            throw new DataIntegrityException("ERRO: O celular não existe!");
        }
        Optional<AlunoModel> alunoOptional = alunoRepository.findByCelular(celular);
        AlunoModel alunoModel = alunoOptional.orElseThrow(() ->
                new ObjectNotFoundException("ERRO: Celular não encontrado ou errado! :" + celular));
        return alunoModel.ModelToDto();
    }
    @Transactional
    public AlunoDto CriarAluno(@Valid AlunoModel alunoModel){
            if(alunoRepository.existsByMatricula(alunoModel.getMatricula())){
                throw new DataIntegrityException("ERRO A matricula já existe!");
            }
            if(alunoRepository.existsByEmail(alunoModel.getEmail())){
                throw new DataIntegrityException("ERRO: O email já existe!");
            }
            if(alunoRepository.existsByCelular(alunoModel.getCelular())){
                throw new DataIntegrityException("ERRO: O celular já existe!");
            }
            if(alunoRepository.existsByCpf(alunoModel.getCpf())){
                throw new DataIntegrityException("ERRO: O cpf já existe!");
            }
            if(alunoRepository.existsById(alunoModel.getId())){
                throw new DataIntegrityException("ERRO: O professor já existe!");
            }
            if(alunoModel == null){
                throw new DataIntegrityException("ERRO: Ao menos um aluno deve ser enviado");
            }
            alunoRepository.save(alunoModel);
        return alunoModel.ModelToDto();
    }

    @Transactional
    public AlunoDto AtualizarAluno(@Valid AlunoModel alunoModel){
        if(alunoRepository.existsByMatricula(alunoModel.getMatricula())){
            throw new DataIntegrityException("ERRO A matricula já existe!");
        }
        if(alunoRepository.existsByEmail(alunoModel.getEmail())){
            throw new DataIntegrityException("ERRO: O email já existe!");
        }
        if(alunoRepository.existsByCelular(alunoModel.getCelular())){
            throw new DataIntegrityException("ERRO: O celular já existe!");
        }
        if(alunoRepository.existsByCpf(alunoModel.getCpf())){
            throw new DataIntegrityException("ERRO: O cpf já existe!");
        }
        if(alunoModel == null){
            throw new DataIntegrityException("ERRO: Deve ter ao menos um atributo a modificar!");
        }
        alunoRepository.save(alunoModel);
        return alunoModel.ModelToDto();
    }

    @Transactional
    public AlunoDto AtualizarMatricula(@Valid AlunoModel alunoModel, @Valid String matricula){
        if(alunoRepository.existsByMatricula(matricula)){
            throw new ObjectNotFoundException("ERRO: Aluno já existe! ");
        }
            Optional<AlunoModel> alunoOptional = alunoRepository.findByCpf(alunoModel.getCpf());
            alunoModel = alunoOptional.get();
            alunoModel.setMatricula(matricula);
            alunoRepository.flush();
            return alunoModel.ModelToDto();
    }
    @Transactional
    public AlunoDto AtualizarEmail(@Valid AlunoModel alunoModel, @Valid String email){
        if(alunoRepository.existsByEmail(email)){
            throw new ObjectNotFoundException("ERRO: Aluno já existe! ");
        }
            Optional<AlunoModel> alunoOptional = alunoRepository.findByCpf(alunoModel.getCpf());
            alunoModel = alunoOptional.get();
            alunoModel.setEmail(email);
            alunoRepository.flush();
            return alunoModel.ModelToDto();
    }
    @Transactional
    public AlunoDto AtualizarCelular(@Valid AlunoModel alunoModel, @Valid String celular){
        if(alunoRepository.existsByCelular(celular)){
            throw new ObjectNotFoundException("ERRO: Aluno já existe! ");
        }
            Optional<AlunoModel> alunoOptional = alunoRepository.findByCpf(alunoModel.getCpf());
            alunoModel = alunoOptional.get();
            alunoModel.setCelular(celular);
            alunoRepository.flush();
            return alunoModel.ModelToDto();
    }
    @Transactional
    public AlunoDto AtualizarApelido(@Valid AlunoModel alunoModel, @Valid String apelido){
        if(alunoRepository.existsByApelido(apelido)){
            throw new DataIntegrityException("ERRO: apelido igual ao anterior!");
        }
            Optional<AlunoModel> alunoOptional = alunoRepository.findByCpf(alunoModel.getCpf());
            alunoModel = alunoOptional.get();
            alunoModel.setApelido(apelido);
            alunoRepository.flush();
            return alunoModel.ModelToDto();
    }
    @Transactional
    public void DeletarPorId(@Valid Integer id){
        if(alunoRepository.existsById(id)){
            alunoRepository.deleteById(id);
        }else throw new ResourceNotFoundException("ERRO: Aluno não encontrado!");
    }
    @Transactional
    public void DeletarPorEmail(@Valid String email){
        if(alunoRepository.existsByEmail(email)){
            alunoRepository.deleteByEmail(email);
        }else throw new ResourceNotFoundException("ERRO: email não encontrado " + email);
    }
    @Transactional
    public void DeletarPorMatricula(@Valid String matricula){
        if(alunoRepository.existsByMatricula(matricula)){
            alunoRepository.deleteByMatricula(matricula);
        }else throw new ResourceNotFoundException("ERRO: matricula não encontrado " + matricula);
    }
    @Transactional
    public void DeletarPorCelular(@Valid String celular){
        if(alunoRepository.existsByCelular(celular)){
            alunoRepository.deleteByCelular(celular);
        }else throw new ResourceNotFoundException("ERRO: Celular não encontrado " + celular);
    }
    @Transactional
    public void DeletarPorNome(@Valid String nome){
        if(alunoRepository.existsByNome(nome)){
            alunoRepository.deleteByNome(nome);
        }else throw new ResourceNotFoundException("ERRO: Nome não encontrado " + nome);
    }
    @Transactional
    public void DeletarPorCpf(@Valid String cpf){
        if(alunoRepository.existsByCpf(cpf)){
            alunoRepository.deleteByCpf(cpf);
        }else throw new ResourceNotFoundException("ERRO: Cpf não encontrado " + cpf);
    }

}
