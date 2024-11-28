package br.edu.ifs.apinewsigaa.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
