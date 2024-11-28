package br.edu.ifs.apinewsigaa.rest.dto;

import br.edu.ifs.apinewsigaa.model.projection.ProfessorDisciplinaAlunoProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplinaComAlunosDTO {
    private DisciplinaDto disciplina;
    private List<AlunoDto> alunos;
}