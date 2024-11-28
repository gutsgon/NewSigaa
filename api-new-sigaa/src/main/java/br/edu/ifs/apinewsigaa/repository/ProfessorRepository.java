package br.edu.ifs.apinewsigaa.repository;

import br.edu.ifs.apinewsigaa.model.AlunoModel;
import br.edu.ifs.apinewsigaa.model.ProfessorModel;
import br.edu.ifs.apinewsigaa.model.projection.DisciplinaProfessorProjection;
import br.edu.ifs.apinewsigaa.model.projection.ProfessorDisciplinaAlunoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Integer> {
    Optional<ProfessorModel> findByMatricula(String matricula);
    Optional<ProfessorModel> findByNome(String nome);
    Optional<ProfessorModel> findByCpf(String cpf);
    Optional<ProfessorModel> findByEmail(String email);
    Optional<ProfessorModel> findByCelular(String celular);
    List<ProfessorModel> findByOrderByNomeAsc();
    List<ProfessorModel> findByNomeContains(String nome);
    void deleteByMatricula(String matricula);
    void deleteByCpf(String cpf);
    void deleteByEmail(String email);
    void deleteByCelular(String celular);
    void deleteByNome(String nome);
    boolean existsByMatricula(String matricula);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCelular(String celular);
    boolean existsByNome(String nome);
    @Query(value = """
            SELECT p.matricula as matriculaProfessor
            	,	p.nome as nomeProfessor
            	,   p.celular as celularProfessor
            	,   p.cpf  as cpfProfessor
            	,   p.email as emailProfessor
            	,   p.dataNascimento as dataNascimento
            	,	d.nome as nomeDisciplina
            	,	d.numeroCreditos as numeroCreditos
            	FROM professor p
            		INNER JOIN turma t
            			ON (p.id = t.idProfessor)
            		INNER JOIN disciplina d
            			ON (d.id = t.idDisciplina)
            	WHERE p.matricula = :matricula and t.dataFim IS NULL
            """, nativeQuery = true)
    List<DisciplinaProfessorProjection> ObterDisciplinasProfessor(@Param("matricula") String matricula);

    @Query(value = """
             SELECT DISTINCT d.id as idDisciplina
                ,    d.nome as nomeDisciplina
            	,	d.numeroCreditos as numeroCreditos
                ,   a.id as idAluno
            	,   a.matricula as matriculaAluno
            	,   a.nome as nomeAluno
            	,   a.cpf as cpfAluno
            	,   a.email as emailAluno
            	,   a.dataNascimento as dataNascimentoAluno
            	,   a.apelido as apelidoAluno
            	,   a.celular as celularAluno
            	,   p.matricula as matriculaProfessor
            	,	p.nome as nomeProfessor
            	,   p.celular as celularProfessor
            	,   p.cpf  as cpfProfessor
            	,   p.email as emailProfessor
            	,   p.dataNascimento as dataNascimento
            	FROM matricula m
            		INNER JOIN aluno a
            			ON (a.id = m.idAluno)
            		INNER JOIN turma t
            			ON (m.idTurma = t.id)
            		INNER JOIN disciplina d
            		    ON (d.id = t.idDisciplina)
            		INNER JOIN professor p
            		    ON (p.id = t.idProfessor)
            	WHERE p.matricula = :matricula and t.dataFim IS NULL
            """, nativeQuery = true)
    List<ProfessorDisciplinaAlunoProjection> ObterDisciplinasProfessorAluno(@Param("matricula") String matricula);

}
