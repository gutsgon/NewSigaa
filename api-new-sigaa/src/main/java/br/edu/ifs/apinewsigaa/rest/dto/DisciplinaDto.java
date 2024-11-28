package br.edu.ifs.apinewsigaa.rest.dto;

import br.edu.ifs.apinewsigaa.model.DisciplinaModel;
import br.edu.ifs.apinewsigaa.model.projection.DisciplinaProfessorProjection;
import br.edu.ifs.apinewsigaa.model.projection.ProfessorDisciplinaAlunoProjection;
import br.edu.ifs.apinewsigaa.repository.DisciplinaRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisciplinaDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nome", length = 255, nullable = false, unique = true)
    private String nome;
    @Column(name = "numeroCreditos", nullable = false)
    private int numeroCreditos;

    public static DisciplinaModel DtoToModel(DisciplinaDto disciplinaDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(disciplinaDto, DisciplinaModel.class);
    }

    public static DisciplinaDto ProjectionToDto(List<DisciplinaProfessorProjection> disciplinaProfessorProjection){
        DisciplinaDto disciplinaDto = new DisciplinaDto();
        disciplinaDto.setNumeroCreditos(disciplinaProfessorProjection.get(0).getNumeroCreditos());
        disciplinaDto.setNome(disciplinaProfessorProjection.get(0).getNomeDisciplina());
        return disciplinaDto;
    }

}
