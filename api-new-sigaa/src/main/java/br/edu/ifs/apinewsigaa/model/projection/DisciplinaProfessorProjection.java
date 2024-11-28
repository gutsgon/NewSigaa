package br.edu.ifs.apinewsigaa.model.projection;

import java.util.Date;

public interface DisciplinaProfessorProjection {
    public String getMatriculaProfessor();

    public String getNomeProfessor();

    public String getCelularProfessor();

    public String getCpfProfessor();

    public String getEmailProfessor();

    public Date getDataNascimento();

    public String getNomeDisciplina();

    public int getNumeroCreditos();

}
