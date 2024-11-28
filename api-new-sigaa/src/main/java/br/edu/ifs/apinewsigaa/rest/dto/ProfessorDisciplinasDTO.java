package br.edu.ifs.apinewsigaa.rest.dto;

import br.edu.ifs.apinewsigaa.model.projection.DisciplinaProfessorProjection;
import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ProfessorDisciplinasDTO {
    private ProfessorDto professor;
    private List<DisciplinaDto> disciplinas;
}