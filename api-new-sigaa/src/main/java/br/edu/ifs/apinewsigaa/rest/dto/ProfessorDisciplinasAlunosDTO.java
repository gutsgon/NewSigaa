package br.edu.ifs.apinewsigaa.rest.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProfessorDisciplinasAlunosDTO {
        private ProfessorDto professor;
        private List<DisciplinaComAlunosDTO> disciplinas;
}
