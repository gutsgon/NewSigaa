package br.edu.ifs.apinewsigaa.repository;

import br.edu.ifs.apinewsigaa.model.AlunoModel;
import br.edu.ifs.apinewsigaa.model.ProfessorModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunoTurmaProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Integer> {
    Optional<AlunoModel> findByMatricula(String matricula);
    Optional<AlunoModel> findByNome(String nome);
    Optional<AlunoModel> findByCpf(String cpf);
    Optional<AlunoModel> findByEmail(String email);
    Optional<AlunoModel> findByCelular(String celular);
    List<AlunoModel> findByNomeContains(String nome);
    List<AlunoModel> findByOrderByNomeAsc();
    void deleteByMatricula(String matricula);
    void deleteByCpf(String cpf);
    void deleteByEmail(String email);
    void deleteByCelular(String celular);
    void deleteByNome(String nome);
    boolean existsByMatricula(String matricula);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCelular(String celular);
    boolean existsByApelido(String apelido);
    boolean existsByNome(String nome);

    @Query(value = "SELECT * FROM aluno a " + "WHERE a.email = :email", nativeQuery = true)
    List<AlunoModel> ObterAlunoPorEmail(@Param("email") String email);

    @Query(value = """
                SELECT	t.id as numeroTurma
            	,	a.nome as nome
            	FROM matricula m
            		INNER JOIN turma t
            			ON (m.idTurma = t.id)
            	    INNER JOIN aluno a
            	        ON (m.idAluno = a.id)
            	WHERE a.nome = :nome
            """, nativeQuery = true)
    List<AlunoTurmaProjection> ObterTurmasMatriculadas (@Param("nome") String nome);
}
