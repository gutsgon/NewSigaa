package br.edu.ifs.apinewsigaa.repository;

import br.edu.ifs.apinewsigaa.model.TurmaModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunoTurmaProjection;
import br.edu.ifs.apinewsigaa.model.projection.TurmaMesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaModel, Integer> {
    Optional<List<TurmaModel>> findByIdProfessorAndIdDisciplina(int idProfessor, int idDisciplina);
    List<TurmaModel> findByDataInicio(Date dataInicio);
    List<TurmaModel> findByDataFim(Date dataFim);
    void deleteByIdProfessorAndIdDisciplina(int idProfessor, int idDisciplina);
    boolean existsByIdProfessorAndIdDisciplina(int idProfessor, int idDisciplina);
    boolean existsByDataInicio(Date dataInicio);
    boolean existsByDataFim(Date dataFim);

    @Query(value = """
            SELECT DISTINCT
                a.*
            FROM
                aluno a
            INNER JOIN matricula m
                ON (a.id = m.id_aluno)
            INNER JOIN turma t
                ON (t.id = m.id_turma)
            WHERE t.id = :id
            """, nativeQuery = true)
    List<AlunoTurmaProjection> ObterAlunosDeUmaTurma(@Param("id") int id);

    @Query(value = """
            SELECT MONTH(data_inicio),
                   COUNT(id)
            FROM
                turma
            GROUP BY
                MONTH(data_inicio)
            ORDER BY
                COUNT(id) DESC
            """, nativeQuery = true)
    List<TurmaMesProjection> ObterMesComMaisTurmas();

    @Query(value = """
            SELECT
                COUNT(id)
            FROM
                matricula
            WHERE id_turma = :id
            """, nativeQuery = true)
    Optional<Integer> numeroDeMatriculasNumaTurma(@Param("id") int id);
}
