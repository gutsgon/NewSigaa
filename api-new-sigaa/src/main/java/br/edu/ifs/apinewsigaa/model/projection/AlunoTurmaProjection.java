package br.edu.ifs.apinewsigaa.model.projection;

import java.util.Date;

public interface AlunoTurmaProjection {
    public String getNome();
    public int getMatricula();
    public String getCpf();
    public String getEmail();
    public String getCelular();
    public String getApelido();
    public Date getDataNascimento();

}
