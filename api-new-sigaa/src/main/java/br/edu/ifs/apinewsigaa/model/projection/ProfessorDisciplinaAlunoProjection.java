package br.edu.ifs.apinewsigaa.model.projection;


import java.util.Date;

public interface ProfessorDisciplinaAlunoProjection {

    public int getIdDisciplina();

    public String getNomeDisciplina();

    public int getNumeroCreditos();

    public int getIdAluno();

    public String getMatriculaAluno();

    public String getNomeAluno();

    public String getCpfAluno();

    public String getEmailAluno();

    public Date getDataNascimentoAluno();

    public String getApelidoAluno();

    public String getCelularAluno();

    public String getMatriculaProfessor();

    public String getNomeProfessor();

    public String getCelularProfessor();

    public String getCpfProfessor();

    public String getEmailProfessor();

    public Date getDataNascimento();
}
