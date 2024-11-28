package br.edu.ifs.apinewsigaa.model;

import br.edu.ifs.apinewsigaa.rest.dto.ProfessorDto;
import jakarta.persistence.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "professor")
public class ProfessorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;
    @Column(name = "dataNascimento", nullable = false)
    private Date dataNascimento;
    @Column(name = "celular", length = 14, nullable = false, unique = true)
    private String celular;
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    /*@ManyToMany
    @JoinTable(
            name = "professor_disciplina",
            joinColumns = @JoinColumn(name = "professor_fk"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_fk")
    )
    private Set<DisciplinaModel> disciplinas;*/

    public ProfessorDto ModelToDto(ProfessorModel professorModel) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(professorModel, ProfessorDto.class);
    }


}
