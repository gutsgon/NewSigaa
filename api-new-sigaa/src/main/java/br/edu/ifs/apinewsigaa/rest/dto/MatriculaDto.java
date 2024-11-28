package br.edu.ifs.apinewsigaa.rest.dto;

import br.edu.ifs.apinewsigaa.model.MatriculaModel;
import jakarta.persistence.Column;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class MatriculaDto {
    @Column(name = "idTurma", nullable = false)
    private int idTurma;
    @Column(name = "idAluno", nullable = false)
    private int idAluno;

    public MatriculaModel DtoToModel(MatriculaDto matriculaDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(matriculaDto, MatriculaModel.class);
    }
}
