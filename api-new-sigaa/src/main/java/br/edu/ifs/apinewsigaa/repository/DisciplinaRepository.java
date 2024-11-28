package br.edu.ifs.apinewsigaa.repository;

import br.edu.ifs.apinewsigaa.model.DisciplinaModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunosDisciplinaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<DisciplinaModel, Integer> {
    Optional<DisciplinaModel> findByNome(String nome);
    List<DisciplinaModel> findByNomeContains(String nome);
    List<DisciplinaModel> findByOrderByNomeAsc();
    List<DisciplinaModel> findByOrderByNomeDesc();
    List<DisciplinaModel> findByNumeroCreditos(int numeroCreditos);
    List<DisciplinaModel> findByOrderByNumeroCreditosDesc();
    List<DisciplinaModel> findByOrderByNumeroCreditosAsc();
    void deleteByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByNumeroCreditos(int numeroCreditos);

    @Query(value = """
            SELECT DISTINCT
                a.matricula, a.nome, a.id, a.celular, a.cpf, a.email, a.apelido, a.dataNascimento
            FROM
                aluno a
            INNER JOIN matricula m
                ON (a.id = m.idAluno)
            INNER JOIN turma t
                ON (t.id = m.idTurma)
            INNER JOIN disciplina d
                ON (d.id = t.idDisciplina)
            INNER JOIN professor p
                ON (p.id = t.idProfessor)
            WHERE d.id = :id
            """, nativeQuery = true)
    List<AlunosDisciplinaProjection> ObterAlunosDeDisciplina(@Param("id") int id);


    
}
