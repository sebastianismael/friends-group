package ar.edu.unlam.tallerweb1.domain.exceptions;

public class UserWithoutFriendsGroup extends BusinessException {
    public UserWithoutFriendsGroup(String name) {
        super(name + " doesn't have a friends group");
    }
}
