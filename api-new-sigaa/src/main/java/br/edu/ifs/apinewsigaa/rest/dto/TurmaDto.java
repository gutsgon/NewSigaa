package br.edu.ifs.apinewsigaa.rest.dto;

import br.edu.ifs.apinewsigaa.model.TurmaModel;
import jakarta.persistence.Column;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
public class TurmaDto {
    @Column(name = "dataInicio", nullable = false)
    private Date dataInicio;
    @Column(name = "dataFim", nullable = true)
    private Date dataFim;
    @Column(name = "idProfessor", nullable = false)
    private int idProfessor;
    @Column(name = "idDisciplina", nullable = false)
    private int idDisciplina;

    public TurmaModel DtoToModel(TurmaDto turmaDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(turmaDto, TurmaModel.class);
    }
}
