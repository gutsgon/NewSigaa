package br.edu.ifs.apinewsigaa.model.projection;

import java.util.Date;

public interface AlunosDisciplinaProjection {
    public int getId();
    public String getMatricula();
    public String getNome();
    public String getCelular();
    public String getCpf();
    public String getEmail();
    public String getApelido();
    public Date getDataNascimento();

}
