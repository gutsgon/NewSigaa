package br.edu.ifs.apinewsigaa.model;

import br.com.caelum.stella.validation.CPFValidator;
import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.exception.ObjectNotFoundException;
import br.edu.ifs.apinewsigaa.repository.AlunoRepository;
import br.edu.ifs.apinewsigaa.rest.dto.AlunoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aluno")
public class AlunoModel {
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
    @Column(name = "apelido", length = 255, nullable = true)
    private String apelido;
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;


    public AlunoDto ModelToDto(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, AlunoDto.class);
    }

}
