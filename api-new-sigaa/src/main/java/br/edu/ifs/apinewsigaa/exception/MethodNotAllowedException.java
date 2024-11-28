package br.edu.ifs.apinewsigaa.exception;

public class MethodNotAllowedException  extends RuntimeException{
    public MethodNotAllowedException(){super("Metodo incorreto!");}

    public MethodNotAllowedException(String message){super(message);}
}
