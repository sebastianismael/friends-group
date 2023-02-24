package ar.edu.unlam.tallerweb1.domain.exceptions;

public class UserNotExists extends BusinessException{
    public UserNotExists(String name) {
        super(name + " do not exist");
    }
}
