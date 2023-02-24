package ar.edu.unlam.tallerweb1.domain.exceptions;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super (message);
    }
}
