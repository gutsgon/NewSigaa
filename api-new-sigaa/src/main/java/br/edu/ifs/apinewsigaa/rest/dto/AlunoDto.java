package br.edu.ifs.apinewsigaa.rest.dto;

import br.com.caelum.stella.validation.CPFValidator;
import br.edu.ifs.apinewsigaa.exception.DataIntegrityException;
import br.edu.ifs.apinewsigaa.model.AlunoModel;
import br.edu.ifs.apinewsigaa.model.projection.AlunosDisciplinaProjection;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Pattern(regexp = "[\\p{L} .'-]+",
            message = "ERRO: O nome deve conter apenas letras e alguns caracteres especiais permitidos(espaços, acentos, apóstrofos e hífens)")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @CPF
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;

    @Email(message = "Erro: O endereço de email está inválido."
            , regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "dataNascimento", nullable = false)
    private Date dataNascimento;

    @Column(name = "apelido", length = 255, nullable = true)
    private String apelido;

    @Pattern(regexp = "^\\d{2} \\d{5}-\\d{4}$", message = "Erro: Número de celular inválido. Formato esperado: XX XXXXX-XXXX.")
    @Column(name = "celular", length = 14, nullable = false, unique = true)
    private String celular;

    public AlunoModel DtoToModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, AlunoModel.class);
    }

    public static List<AlunoDto> ProjectionToDto(List<AlunosDisciplinaProjection> alunosDisciplinaProjection){
        List<AlunoDto> alunoDto = alunosDisciplinaProjection.stream()
                .map(projection -> new AlunoDto(projection.getId(), projection.getMatricula(),projection.getNome(),
                projection.getCpf(),
                projection.getEmail(),
                projection.getDataNascimento(),
                projection.getApelido(),
                projection.getCelular())).collect(Collectors.toList());
        return alunoDto;
    }

    public boolean validarCpf(String cpf){
        CPFValidator cpfValidator = new CPFValidator();
        try{
            cpfValidator.assertValid(cpf);
        }catch(Exception e){
            throw new DataIntegrityException("CPF inválido!");
        }
        return true;
    }

    public boolean validarEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            throw new DataIntegrityException("Email inválido!");
        }
        return result;
    }

    public boolean validarCelular(String celular) {
        //Baseado no original para javascript:
        //https://gist.github.com/jonathangoncalves/7bdec924e9bd2bdf353d6b7520820b62

        //retira todos os caracteres não-numéricos (incluindo espaço,tab, etc)
        celular = celular.replaceAll("\\D","");

        //verifica se tem a qtde de numeros correta
        if (!(celular.length() >= 10 && celular.length() <= 11)) throw new DataIntegrityException("Celular inválido!");

        //Se tiver 11 caracteres, verificar se começa com 9 o celular
        if (celular.length() == 11 && Integer.parseInt(celular.substring(2, 3)) != 9) throw new DataIntegrityException("Celular inválido!");

        //verifica se o numero foi digitado com todos os dígitos iguais
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(celular.charAt(0)+"{"+celular.length()+"}");
        java.util.regex.Matcher m = p.matcher(celular);
        if(m.find()) return false;

        //DDDs validos
        Integer[] codigosDDD = {
                11, 12, 13, 14, 15, 16, 17, 18, 19,
                21, 22, 24, 27, 28, 31, 32, 33, 34,
                35, 37, 38, 41, 42, 43, 44, 45, 46,
                47, 48, 49, 51, 53, 54, 55, 61, 62,
                64, 63, 65, 66, 67, 68, 69, 71, 73,
                74, 75, 77, 79, 81, 82, 83, 84, 85,
                86, 87, 88, 89, 91, 92, 93, 94, 95,
                96, 97, 98, 99};
        //verifica se o DDD é valido (sim, da pra verificar rsrsrs)
        if ( java.util.Arrays.asList(codigosDDD).indexOf(Integer.parseInt(celular.substring(0, 2))) == -1) throw new DataIntegrityException("Celular inválido!");

        //Se o número só tiver dez digitos não é um celular e por isso o número logo após o DDD deve ser 2, 3, 4, 5 ou 7
        Integer[] prefixos = {2, 3, 4, 5, 7};

        if (celular.length() == 10 && java.util.Arrays.asList(prefixos).indexOf(Integer.parseInt(celular.substring(2, 3))) == -1) throw new DataIntegrityException("Celular inválido!");

        //se passar por todas as validações acima, então está tudo certo
        return true;
    }

    public boolean validarMatricula(String matricula){
        try {

            if (matricula.length() < 12 || matricula.length() > 12) {
                throw new DataIntegrityException("Matricula inválida");
            }
            if (matricula.charAt(5) == '2') {
                throw new DataIntegrityException("Matricula inválida");
            }
            if (matricula.charAt(5) == '1') {
                if((matricula.charAt(6) == '0' && matricula.charAt(7) == '1') || (matricula.charAt(6) == '0' && matricula.charAt(7) == '2')
                        || (matricula.charAt(6) == '0' && matricula.charAt(7) == '3') || (matricula.charAt(6) == '0' && matricula.charAt(7) == '4')
                        || (matricula.charAt(6) == '0' && matricula.charAt(7) == '5') || (matricula.charAt(6) == '0' && matricula.charAt(7) == '6')
                        || (matricula.charAt(6) == '0' && matricula.charAt(7) == '7') || (matricula.charAt(6) == '0' && matricula.charAt(7) == '8')
                        || (matricula.charAt(6) == '0' && matricula.charAt(7) == '9') || (matricula.charAt(6) == '1' && matricula.charAt(7) == '0')
                        || (matricula.charAt(6) == '1' && matricula.charAt(7) == '1') || (matricula.charAt(6) == '1' && matricula.charAt(7) == '2')){
                    return true;
                }
            }
        }catch(DataIntegrityException e){
            throw new DataIntegrityException("Matricula inválida");
        }
        return false;
    }
}
